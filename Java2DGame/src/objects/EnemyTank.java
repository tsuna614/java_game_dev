package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import HUD.GUI;
import main.GamePanel;
import main.KeyHandler;
import main.GamePanel.GameState;
import objects.Player.DeleteGUI;
import utils.Animation;
import utils.ObjectManager;
import utils.SATCollision;
import utils.Sprite;
import utils.Vector2;


public class EnemyTank extends GameObject {
	GamePanel gp;
	KeyHandler keyH;
	
	// Tank Properties
	double speed = 1;
	public float rotationAngle = 0;
	Vector2 velocity = new Vector2(0, 0);
	
	TankState tankState = TankState.idle;
	
	private Animation movingAnimation;
	private Animation dyingAnimation;
	private Animation currentAnimation;
	
	private int bulletInterval = 0;
	private int immunityFrames = -1;
	private int deathTimeout = 0; // if this reach a certain amount (300 for example), delete the tank
	
	int[] xPoly = {0, 0, 0, 0};
	int[] yPoly = {0, 0, 0, 0};
	
	private Polygon newHitbox = new Polygon(xPoly, yPoly, 4);
	
	Timer timer = new Timer();
	class DeleteGUI extends TimerTask {

	    private final GUI gui;


	    DeleteGUI ( GUI gui )
	    {
	      this.gui = gui;
	    }

	    public void run() {
	    	gp.GUIList.remove(gui);
	    }
	}

	
	public EnemyTank(Point2D position, GamePanel gp, KeyHandler keyH) {
		super(position);
		this.gp = gp;
		this.keyH = keyH;
		
		this.setWidth(gp.tileSize);
		this.setHeight(gp.tileSize);
		
		loadTankAnimation();
	}
	
	private void loadTankAnimation() {
		Sprite.loadSprite("res/player/tank3.png");
		final BufferedImage[] movingSprites = {Sprite.getSprite256(0, 0), Sprite.getSprite256(1, 0)};
		
		Sprite.loadSprite("res/player/bullets.png");
		final BufferedImage[] dyingSprites = {Sprite.getSprite256(0, 0), Sprite.getSprite256(1, 0), Sprite.getSprite256(2, 0), Sprite.getSprite256(3, 0), Sprite.getSprite256(4, 0), Sprite.getSprite256(5, 0), Sprite.getSprite256(6, 0), Sprite.getSprite256(7, 0)};
		
		
		movingAnimation = new Animation(movingSprites, 10);
		dyingAnimation = new Animation(dyingSprites, 5);
	}


	public void update() {
		updateTankAnimation();
		if (tankState == TankState.die) {
			deletingTank();			
		} else {			
			updateEnemyMovemonet();
			updateTankImmunityFrames();
			checkCollision();
			updateVelocityBasedOnAngle();
			updateTankVerticalPosition();
			updateTankHorizontalPosition();
			updateNewHitbox();
		}
	}
	
	private void deletingTank() {
		if (deathTimeout >= 5 * 9) {
			gp.removeGameObject(this);
		}
		deathTimeout++;
	}

	private void updateTankImmunityFrames() {
		if (immunityFrames != -1) {
			immunityFrames++;
			if (immunityFrames >= 100) {
				immunityFrames = -1;
			}
		}
	}
	
	private void shootAfterInterval() {
		bulletInterval++;
		if (bulletInterval >= 100) {
			this.shoot();
			bulletInterval = 0;
		}
	}

	public double calculateDistance(Point2D A, Point2D B) {
		return Math.sqrt((A.getX() - B.getX()) * (A.getX() - B.getX()) + (A.getY() - B.getY()) * (A.getY() - B.getY()));
	}
	
	// generate by chatGPT
	public static double calculateAngle(Point2D A, Point2D O, Point2D B) {
        // Calculate vectors OA and OB
        double[] OA = {A.getX() - O.getX(), A.getY() - O.getY()};
        double[] OB = {B.getX() - O.getX(), B.getY() - O.getY()};

        // Calculate the dot product and magnitudes of OA and OB
        double dotProduct = OA[0] * OB[0] + OA[1] * OB[1];
        double magnitudeOA = Math.sqrt(OA[0] * OA[0] + OA[1] * OA[1]);
        double magnitudeOB = Math.sqrt(OB[0] * OB[0] + OB[1] * OB[1]);

        // Calculate the angle in radians between OA and OB
        double angle = Math.acos(dotProduct / (magnitudeOA * magnitudeOB));
        

        // Calculate the cross product to determine the sign of the angle
        double crossProduct = OA[0] * OB[1] - OA[1] * OB[0];
        if (crossProduct < 0) {
            angle = 2 * Math.PI - angle;
        }

        return Math.toDegrees(angle);
    }
	
	
	
	private void rotateToPlayer(Point2D enemyCenterPoint, Point2D enemyTopPoint, Point2D playerCenterPoint) {
//		Point2D enemyCenterPoint = new Point2D.Double(this.getPosition().getX() + this.getWidth() / 2, this.getPosition().getY() + this.getHeight() / 2);			
//		Point2D enemyTopPoint = new Point2D.Double(this.getPosition().getX() + this.getWidth() / 2, this.getPosition().getY() + this.getHeight() / 2 - 10);
//		Point2D playerCenterPoint = new Point2D.Double(gp.tank.getPosition().getX() + gp.tank.getWidth() / 2, gp.tank.getPosition().getY() + gp.tank.getHeight() / 2);			
		
		//	WOW THIS WHOLE METHOD WAS HARD TO FIGURE OUT
		if (Math.abs(calculateAngle(enemyTopPoint, enemyCenterPoint, playerCenterPoint) - rotationAngle) > 2) {			
			if (calculateAngle(enemyTopPoint, enemyCenterPoint, playerCenterPoint) - rotationAngle > 0) {
				if (calculateAngle(enemyTopPoint, enemyCenterPoint, playerCenterPoint) - rotationAngle <= 180) {
					rotationAngle++;
				} else rotationAngle--;
			} else {
				if (calculateAngle(enemyTopPoint, enemyCenterPoint, playerCenterPoint) - rotationAngle > -180) {
					rotationAngle--;
				} else rotationAngle++;
			}	
		}
			
		if (rotationAngle > 360) {
			rotationAngle = 1;
		} else if (rotationAngle < 0) {
			rotationAngle = 359;
		}
	}
	

	private void updateEnemyMovemonet() {
		Point2D enemyCenterPoint = new Point2D.Double(this.getPosition().getX() + this.getWidth() / 2, this.getPosition().getY() + this.getHeight() / 2);
		Point2D enemyTopPoint = new Point2D.Double(this.getPosition().getX() + this.getWidth() / 2, this.getPosition().getY() + this.getHeight() / 2 - 10);
		Point2D playerCenterPoint = new Point2D.Double(gp.tank.getPosition().getX() + gp.tank.getWidth() / 2, gp.tank.getPosition().getY() + gp.tank.getHeight() / 2);
		// Enemy movement AI
		velocity.x = speed;
		velocity.y = speed;
		rotateToPlayer(enemyCenterPoint, enemyTopPoint, playerCenterPoint);
		
		if (calculateDistance(enemyCenterPoint, playerCenterPoint) < 200) {
			velocity.x = 0;
			velocity.y = 0;
			shootAfterInterval();
		}
		
	}

	private void updateNewHitbox() {
		float radius = this.getWidth() + 24;
		for (int i=0; i<xPoly.length; i++) {
			float angle = rotationAngle - 45 + i * 90;
			xPoly[i] = (int) (Math.sin(Math.toRadians(angle)) * radius / 2 + this.position.getX() + this.getWidth() / 2);
			
			yPoly[i] = (int) (-Math.cos(Math.toRadians(angle)) * radius / 2 + this.position.getY() + this.getHeight() / 2);
		}
		
		newHitbox = new Polygon(xPoly, yPoly, 4);
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
			
			if (SATCollision.checkCollision(this.newHitbox, block.getHitboxAsPolygon()) && block.hasCollision && block != this) {
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
					if (((Bullet) block).isPlayerBullet) {
						if (immunityFrames != -1) return;
						immunityFrames = 0;
						
						// increment the score, play sound and show +100 text
						gp.score += 100;
						gp.playSFX("explosion2.wav");
						GUI gui = new GUI(position.getX(), position.getY(), "+100");
						gp.GUIList.add(gui);
						timer.schedule(new DeleteGUI(gui), 1000);
						
						// get random location
						Random rand = new Random();
						int randomNumber = rand.nextInt(35) + 3; // get random number range 3 - 35
						int yLocation;
						
						if (rand.nextInt(2) + 1 == 1) {
							yLocation = 5;
						} else {
							yLocation = 41;
						}
						
//						// spawn new enemy tank
						Point2D newLocation = new Point2D.Double(randomNumber * gp.tileSize, yLocation * gp.tileSize);
						ObjectManager.spawnEnemyTank(newLocation);
						
//						Point2D newLocation = new Point2D.Double(randomNumber * gp.tileSize, yLocation * gp.tileSize);
//						this.setPosition(newLocation);
						
						// set tankState to die, delete bullet and this tank
						tankState = TankState.die;
						this.isBlocking = false;
						gp.removeGameObject(block);
//						gp.removeGameObject(this);
						return;
					}
				}
			}
		}
		
	}

	
	private void updateTankHorizontalPosition() {
		position.setLocation(position.getX() + velocity.x, position.getY());
	}

	private void updateTankVerticalPosition() {
		position.setLocation(position.getX(), position.getY() + velocity.y);
	}
	
	public void shoot() {
		gp.addGameObject(new Bullet(new Point2D.Double(position.getX(), position.getY()), gp, rotationAngle, false));
	}

	// this method essentially convert the vx and vy so that the object's movespeed will always be constant no matter the move angle
	private void updateVelocityBasedOnAngle() {
		velocity.x = Math.sin(Math.toRadians(rotationAngle)) * velocity.x;
		velocity.y = -Math.cos(Math.toRadians(rotationAngle)) * velocity.y;
	}

	private void updateTankAnimation() {
		currentAnimation = movingAnimation;
		
		if (tankState == TankState.die) {
			currentAnimation = dyingAnimation;
			currentAnimation.isLoop = false;
		} else {
			if (velocity.x != 0) tankState = TankState.moving;
			else tankState = TankState.idle;
			
			if (tankState == TankState.moving) {
				currentAnimation.start();
			} else if (tankState == TankState.idle) {
				currentAnimation.stop();
			}
		}
		
//		
		currentAnimation.update();
	}


	public void draw(Graphics2D g2) {
		g2.rotate(Math.toRadians(rotationAngle), position.getX() + this.getWidth() / 2, position.getY() + this.getHeight() / 2);
		g2.drawImage(currentAnimation.getCurrentFrame(), (int) position.getX(), (int) position.getY(), (int) this.getWidth(), (int) this.getHeight(), null);
//		g2.setColor(Color.red);
//		g2.drawRect((int) this.position.getX(), (int) this.position.getY(), (int) this.getWidth(), (int) this.getHeight());
		// reset the g2 after rotating it to draw the player (uncomment to see funny shit)
		g2.rotate(Math.toRadians(-rotationAngle), position.getX() + this.getWidth()/ 2, position.getY() + this.getHeight() / 2);
//		g2.drawPolygon(newHitbox);
	}
	
	
}
