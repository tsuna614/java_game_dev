package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import main.GamePanel;
import main.KeyHandler;
import main.GamePanel.GameState;
import utils.Animation;
import utils.SATCollision;
import utils.Sprite;
import utils.Vector2;

enum TankState {
	moving,
	idle,
	die
}

public class Tank extends GameObject {
	GamePanel gp;
	KeyHandler keyH;
	
	// Tank Properties
	double speed = 2;
	public float rotationAngle = 0;
	Vector2 velocity = new Vector2(0, 0);
	
	TankState tankState = TankState.idle;
	
	private Animation movingAnimation;
	private Animation currentAnimation;
	
	private int bulletInterval = -1;
	private int immunityFrames = -1;
	
	public int currentLife = 6;
	
	
	int[] xPoly = {0, 0, 0, 0};
	int[] yPoly = {0, 0, 0, 0};
	
	private Polygon newHitbox = new Polygon(xPoly, yPoly, 4);

	
	public Tank(Point2D position, GamePanel gp, KeyHandler keyH) {
		super(position);
		this.gp = gp;
		this.keyH = keyH;
		
		this.setWidth(gp.tileSize);
		this.setHeight(gp.tileSize);
		
		loadTankAnimation();
	}
	
	private void loadTankAnimation() {
		Sprite.loadSprite("res/player/tank2.png");
		final BufferedImage[] movingSprites = {Sprite.getSprite256(0, 0), Sprite.getSprite256(1, 0)};
		
		movingAnimation = new Animation(movingSprites, 10);
	}


	public void update() {
		updateTankAnimation();
		updateBulletInterval();
		updateTankImmunityFrames();
		checkTankInput();
		checkCollision();
		updateVelocityBasedOnAngle();
		updateTankVerticalPosition();
		updateTankHorizontalPosition();
		updateNewHitbox();
	}

	private void updateTankImmunityFrames() {
		if (immunityFrames != -1) {
			immunityFrames++;
			if (immunityFrames >= 100) {
				immunityFrames = -1;
			}
		}
	}

	private void updateNewHitbox() {
		// dear me in the future, good luck understanding this function. no seriously.
		float radius = this.getWidth() + 24; // plus 24 here because for some reason when I draw the hitboxes, this new hitbox is somehow smaller than the original hitbox?
		for (int i=0; i<xPoly.length; i++) {
			float angle = rotationAngle - 45 + i * 90;
			xPoly[i] = (int) (Math.sin(Math.toRadians(angle)) * radius / 2 + this.position.getX() + this.getWidth() / 2);
			
			yPoly[i] = (int) (-Math.cos(Math.toRadians(angle)) * radius / 2 + this.position.getY() + this.getHeight() / 2);
		}
		
		newHitbox = new Polygon(xPoly, yPoly, 4);
		
//		System.out.println("x: " + this.position.getX());
//		System.out.println(xPoly[0]);
	}

	private void updateBulletInterval() {
		if (bulletInterval == -1) {
			return;
		}
		bulletInterval++;
		if (bulletInterval >= 50) {
			bulletInterval = -1;
		}
	}

	private boolean checkIfTankHitboxPointIsInsideBlockHitbox(int x, int y, GameObject block) {
		return x < block.getPosition().getX() + block.getWidth() &&
				x > block.getPosition().getX() &&
				y < block.getPosition().getY() + block.getHeight() &&
				y > block.getPosition().getY();
	}

	private void checkCollision() {
		Iterator<GameObject> iter = gp.gameObjects.iterator();
		while (iter.hasNext()) {
			GameObject block = iter.next();
			
//			final double blockX = block.getPosition().getX() + block.getHitbox().x;
//			final double blockWidth = block.getHitbox().getWidth() == 0 ? block.getWidth() : block.getHitbox().width;
			
			if (SATCollision.checkCollision(this.newHitbox, block.getHitboxAsPolygon()) && block.hasCollision) {
				if (block.isBlocking) {
					if (checkIfTankHitboxPointIsInsideBlockHitbox(newHitbox.xpoints[0], newHitbox.ypoints[0], block) || checkIfTankHitboxPointIsInsideBlockHitbox(newHitbox.xpoints[1], newHitbox.ypoints[1], block) || checkIfTankHitboxPointIsInsideBlockHitbox((newHitbox.xpoints[0] + newHitbox.xpoints[1]) / 2, (newHitbox.ypoints[0] + newHitbox.ypoints[1]) / 2, block))	
					{
						if (velocity.x > 0 && velocity.y > 0) {
							velocity.x = 0;
							velocity.y = 0;
						}
					}
					else if (checkIfTankHitboxPointIsInsideBlockHitbox(newHitbox.xpoints[2], newHitbox.ypoints[2], block) || checkIfTankHitboxPointIsInsideBlockHitbox(newHitbox.xpoints[3], newHitbox.ypoints[3], block) || checkIfTankHitboxPointIsInsideBlockHitbox((newHitbox.xpoints[2] + newHitbox.xpoints[3]) / 2, (newHitbox.ypoints[2] + newHitbox.ypoints[3]) / 2, block))
					{
						if (velocity.x < 0 && velocity.y < 0) {
							velocity.x = 0;
							velocity.y = 0;
						}
					}
				}
				
				if (block instanceof Bullet) {
					if (!((Bullet) block).isPlayerBullet) {
						if (immunityFrames == -1) {
							System.out.println("Player is hit!");
							gp.removeGameObject(block);
							gp.playSFX("hitHurt.wav");
							immunityFrames = 0;
							currentLife--;
							
							if (currentLife == 0) {
								gp.switchGameState(GameState.gameOver);
							}
						}
					}
				}
			}
		}
	
	}

	
	private void updateTankHorizontalPosition() {
		// TODO Auto-generated method stub
		position.setLocation(position.getX() + velocity.x, position.getY());
	}

	private void updateTankVerticalPosition() {
		// TODO Auto-generated method stub
		position.setLocation(position.getX(), position.getY() + velocity.y);
	}


	private void checkTankInput() {
		velocity.x = 0;
		velocity.y = 0;

		if (keyH.upPressed) {
			velocity.x += speed;
			velocity.y += speed;
		}
		if (keyH.downPressed) {
			velocity.x -= speed;
			velocity.y -= speed;
		}
		if (keyH.leftPressed) {
//			velocity.x -= speed;
			rotationAngle -= 1;
		}
		if (keyH.rightPressed) {
//			velocity.x += speed;
			rotationAngle += 1;
		}
		
		if (rotationAngle > 360) {
		rotationAngle = 1;
		} else if (rotationAngle < 0) {
		rotationAngle = 359;
		}
		
		tankState = TankState.idle;
		if (velocity.x != 0 || velocity.y != 0) {
			tankState = TankState.moving;
		}
	}
	
	public void shoot() {
		if (bulletInterval == -1) {
			gp.playSFX("explosion3.wav");
			bulletInterval = 0;
			gp.addGameObject(new Bullet(new Point2D.Double(position.getX(), position.getY()), gp, rotationAngle, true));
		}
//		gp.addGameObject(new Bullet(new Point2D.Double(0, 0), gp, rotationAngle));
	}

	// this method essentially convert the vx and vy so that the object's movespeed will always be constant no matter the move angle
	private void updateVelocityBasedOnAngle() {
		velocity.x = Math.sin(Math.toRadians(rotationAngle)) * velocity.x;
		velocity.y = -Math.cos(Math.toRadians(rotationAngle)) * velocity.y;
	}

	private void updateTankAnimation() {
		currentAnimation = movingAnimation;
		
		if (tankState == TankState.moving) {
			currentAnimation.start();
		} else if (tankState == TankState.idle) {
			currentAnimation.stop();
		}
		
		currentAnimation.update();
	}


	public void draw(Graphics2D g2) {
//		g2.drawRect((int) this.position.getX(), (int) this.position.getY(), (int) this.getWidth(), (int) this.getHeight());
		g2.rotate(Math.toRadians(rotationAngle), position.getX() + this.getWidth() / 2, position.getY() + this.getHeight() / 2);
		g2.drawImage(currentAnimation.getCurrentFrame(), (int) position.getX(), (int) position.getY(), (int) this.getWidth(), (int) this.getHeight(), null);
//		g2.setColor(Color.red);
		// reset the g2 after rotating it to draw the player (uncomment to see funny shit)
		g2.rotate(Math.toRadians(-rotationAngle), position.getX() + this.getWidth()/ 2, position.getY() + this.getHeight() / 2);
//		g2.drawPolygon(newHitbox);
	}	
}

//// check collision with left world border
//if (newHitbox.xpoints[0] < 0 || newHitbox.xpoints[1] < 0) {
//	if (velocity.x > 0) {
//		velocity.x = 0;
//		velocity.y = 0;
//	}
//}
//if (newHitbox.xpoints[2] < 0 || newHitbox.xpoints[3] < 0) {
//	if (velocity.x < 0) {
//		velocity.x = 0;
//		velocity.y = 0;
//	}
//}
//
//// check collision with right world border
//if (newHitbox.xpoints[0] > gp.worldWidth || newHitbox.xpoints[1] > gp.worldWidth) {
//	if (velocity.x > 0) {
//		velocity.x = 0;
//		velocity.y = 0;
//	}
//}
//if (newHitbox.xpoints[2] > gp.worldWidth || newHitbox.xpoints[3] > gp.worldWidth) {
//	if (velocity.x < 0) {
//		velocity.x = 0;
//		velocity.y = 0;
//	}
//}
//
//// check collision with top world border
//if (newHitbox.ypoints[0] < 0 || newHitbox.ypoints[1] < 0) {
//	if (velocity.x > 0) {
//		velocity.x = 0;
//		velocity.y = 0;
//	}
//}
//if (newHitbox.xpoints[2] < 0 || newHitbox.xpoints[3] < 0) {
//	if (velocity.x < 0) {
//		velocity.x = 0;
//		velocity.y = 0;
//	}
//}
//
//// check collision with bottom world border
//if (newHitbox.xpoints[0] < gp.worldHeight || newHitbox.xpoints[1] < gp.worldHeight) {
//	if (velocity.x > 0) {
//		velocity.x = 0;
//		velocity.y = 0;
//	}
//}
//if (newHitbox.xpoints[2] < gp.worldHeight || newHitbox.xpoints[3] < gp.worldHeight) {
//	if (velocity.x < 0) {
//		velocity.x = 0;
//		velocity.y = 0;
//	}
//}
