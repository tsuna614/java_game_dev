package objects;

import java.awt.Graphics2D;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import HUD.GUI;
import main.GamePanel;
import main.GamePanel.GameState;
import main.KeyHandler;
import utils.Animation;
import utils.Sprite;
import utils.Vector2;
 
public class Player extends GameObject {
	
	GamePanel gp;
	KeyHandler keyHandler;
	
	enum PlayerState {
		idle,
		walking,
	}
	
	float speed = 4;
	float rotationAngle = 0;
	Vector2 velocity;
	float ay = (float) 5;
	PlayerState playerState;
	
	public String direction;
	public int hasKey = 0;
	public int currentLife;
	
	
	// SET TIMER FUNCTION TO DELETE GUI TEXT AFTER 1 SECOND
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
//	class DirectionTimer extends TimerTask {
//		private final String direction;
//		Player player;
//		DirectionTimer(Player player, String direction) {
//			this.direction = direction;
//			this.player = player;
//		}
//		
//		public void run() {
//			if (direction == "upleft") {
//				player.isUpLeftRecently = false;
//			} else if (direction == "upright") {
//				player.isUpRightRecently = false;
//			} else if (direction == "downleft") {
//				player.isDownLeftRecently = false;
//			} else if (direction == "downright") {
//				player.isDownRightRecently = false;
//			}
//	    }
//	}
	
	// ANIMATIONS
	private Animation walkLeft;
	private Animation walkRight;
	private Animation walkUp;
	private Animation walkDown;
	private Animation walkUpLeft;
	private Animation walkUpRight;
	private Animation walkDownLeft;
	private Animation walkDownRight;
	private Animation currentAnimation;
	
	
	public Player(Point2D position, GamePanel gp, KeyHandler keyH) {
		super(position);
		this.gp = gp;
		this.keyHandler = keyH;
		
		this.setWidth(gp.tileSize);
		this.setHeight(gp.tileSize);

		this.setHitBox(new Rectangle(8, 16, gp.tileSize - 8 * 2, gp.tileSize - 16));
		
		
		velocity = new Vector2(0,0);
		
		currentLife = 6;
		
		setDefaultValues();
		loadPlayerAnimation();
	}
	
	public void setDefaultValues() {
//		worldX = 0;
//		worldY = 0;
		direction = "down";
		playerState = PlayerState.idle;
	}
	
	public void loadPlayerAnimation() {
		Sprite.loadSprite("res/player/player.png");
		final BufferedImage[] walkingLeft = {Sprite.getSprite(0, 2), Sprite.getSprite(1, 2), Sprite.getSprite(2, 2), Sprite.getSprite(3, 2)};
		final BufferedImage[] walkingRight = {Sprite.getSprite(0, 6), Sprite.getSprite(1, 6), Sprite.getSprite(2, 6), Sprite.getSprite(3, 6)};
		final BufferedImage[] walkingUp = {Sprite.getSprite(0, 4), Sprite.getSprite(1, 4), Sprite.getSprite(2, 4), Sprite.getSprite(3, 4)};
		final BufferedImage[] walkingDown = {Sprite.getSprite(0, 0), Sprite.getSprite(1, 0), Sprite.getSprite(2, 0), Sprite.getSprite(3, 0)};
		final BufferedImage[] walkingUpLeft = {Sprite.getSprite(0, 3), Sprite.getSprite(1, 3), Sprite.getSprite(2, 3), Sprite.getSprite(3, 3)};
		final BufferedImage[] walkingUpRight = {Sprite.getSprite(0, 5), Sprite.getSprite(1, 5), Sprite.getSprite(2, 5), Sprite.getSprite(3, 5)};
		final BufferedImage[] walkingDownLeft = {Sprite.getSprite(0, 1), Sprite.getSprite(1, 1), Sprite.getSprite(2, 1), Sprite.getSprite(3, 1)};
		final BufferedImage[] walkingDownRight = {Sprite.getSprite(0, 7), Sprite.getSprite(1, 7), Sprite.getSprite(2, 7), Sprite.getSprite(3, 7)};

		walkDown = new Animation(walkingDown, 10);
		walkLeft = new Animation(walkingLeft, 10);
		walkRight = new Animation(walkingRight, 10);
		walkUp = new Animation(walkingUp, 10);
		walkUpLeft = new Animation(walkingUpLeft, 10);
		walkUpRight = new Animation(walkingUpRight, 10);
		walkDownLeft = new Animation(walkingDownLeft, 10);
		walkDownRight = new Animation(walkingDownRight, 10);
	}
	
	public void update() {
//		if (rotationAngle > 360) {
//			rotationAngle = 1;
//		} else if (rotationAngle < 0) {
//			rotationAngle = 359;
//		}
		updatePlayerAnimation();
		checkPlayerInput();
		checkIfPlayerMoveDiagonally();
//		applyGravity(dt);
		updatePlayerVerticalPosition();
		checkVerticalCollision();
		updatePlayerHorizontalPosition();
		checkHorizontalCollision();
	}
	
	public void updatePlayerAnimation() {
		switch (direction) {
		case "up":
			currentAnimation = walkUp;
			break;
		case "down":
			currentAnimation = walkDown;
			break;
		case "left":
			currentAnimation = walkLeft;
			break;
		case "right":
			currentAnimation = walkRight;
			break;
		case "upleft":
			currentAnimation = walkUpLeft;
			break;
		case "upright":
			currentAnimation = walkUpRight;
			break;
		case "downleft":
			currentAnimation = walkDownLeft;
			break;
		case "downright":
			currentAnimation = walkDownRight;
			break;
		default:
			break;
		}
		
		if (playerState == PlayerState.idle) {
			currentAnimation.reset();
			currentAnimation.stop();
		} else if(playerState == PlayerState.walking) {
			currentAnimation.start();
		}
		
		currentAnimation.update();
	}
	
	public void checkPlayerInput() {
		velocity.x = 0;
		velocity.y = 0;
		
		playerState = PlayerState.walking;
		if (keyHandler.upPressed) {
			direction = "up";
			velocity.y -= speed;
		}
		if (keyHandler.downPressed) {
			direction = "down";
			velocity.y += speed;
		}
		if (keyHandler.leftPressed) {
			direction = "left";
			velocity.x -= speed;
		}
		if (keyHandler.rightPressed) {
			direction = "right";
			velocity.x += speed;
		}
		
		if (keyHandler.upPressed && keyHandler.leftPressed) {
			direction = "upleft";
		} else if (keyHandler.upPressed && keyHandler.rightPressed) {
			direction = "upright";
		} else if (keyHandler.downPressed && keyHandler.leftPressed) {
			direction = "downleft";
		} else if (keyHandler.downPressed && keyHandler.rightPressed) {
			direction = "downright";
		}
		
		if (velocity.x == 0 && velocity.y == 0) {
			playerState = PlayerState.idle;
		}
		
		
		this.exchangeAngleToVelocity(rotationAngle);
		
		if (keyHandler.leftPressed) {
			rotationAngle -= 2;
		}
		if (keyHandler.rightPressed) {
			rotationAngle += 2;
		}
	}
	
	private void exchangeAngleToVelocity(float rotationAngle) {
		
	}

	public void checkIfPlayerMoveDiagonally() {
		if (velocity.x != 0 && velocity.y != 0) {
			velocity.x = (float) (velocity.x / Math.sqrt(2));
			velocity.y = (float) (velocity.y / Math.sqrt(2));
		}
	}
	
	public void checkHorizontalCollision() {
		Iterator<GameObject> iter = gp.gameObjects.iterator();
		while (iter.hasNext()) {
			GameObject block = iter.next();
			
			final double blockX = block.getPosition().getX() + block.getHitbox().x;
			final double blockWidth = block.getHitbox().getWidth() == 0 ? block.getWidth() : block.getHitbox().width;
			
			if (gp.checkCollision(this, block) && block.hasCollision) {
				if (block.isBlocking) {
					if (velocity.x < 0) {
						velocity.x = 0;
//						this.x = blockX + blockWidth - this.getHitbox().x;
						position.setLocation(blockX + blockWidth - this.getHitbox().x, position.getY());
					}
					else if (velocity.x > 0) {
						velocity.x = 0;
//						this.x = blockX - this.getWidth() + this.getHitbox().x;
						position.setLocation(blockX - this.getWidth() + this.getHitbox().x, position.getY());
					}
				}
				checkOtherGameObjectCollision(block, iter);
			}
		}
		
		// check collision with world bounder
		if (position.getX() < 0 && velocity.x < 0) {
			velocity.x = 0;
//			this.x = -this.getHitbox().x;
			position.setLocation(-this.getHitbox().x, position.getY());
		} else if (position.getX() + gp.tileSize - this.getHitbox().x > gp.worldWidth && velocity.x > 0) {
//			this.x = gp.worldWidth - gp.tileSize + this.getHitbox().x;
			position.setLocation(gp.worldWidth - gp.tileSize + this.getHitbox().x, position.getY());
		}
	}
	
	public void checkVerticalCollision() {
		// using Iterator + while loop instead of just foreach loop to prevent the stupid ConcurrentModificationException bug
		Iterator<GameObject> iter = gp.gameObjects.iterator();
		while (iter.hasNext()) {
			GameObject block = iter.next();

			final double blockY = block.getPosition().getY() + block.getHitbox().y;
			final double blockHeight = block.getHitbox().getHeight() == 0 ? block.getHeight() : block.getHitbox().height;
			
			if (gp.checkCollision(this, block) && block.hasCollision) {
				if (block.isBlocking) {
					if (velocity.y < 0) {
						velocity.y = 0;
//						this.y = blockY + blockHeight - this.getHitbox().y;
						position.setLocation(position.getX(), blockY + blockHeight - this.getHitbox().y);
					}
					else if (velocity.y > 0) {
						velocity.y = 0;
//						this.y = blockY - this.getHeight();
						position.setLocation(position.getX(), blockY - this.getHeight());
					}
				}
				checkOtherGameObjectCollision(block, iter);
			}
			
		}
		
		if (position.getY() + this.getHitbox().y < 0 && velocity.y < 0) {
			velocity.y = 0;
//			this.y = -this.getHitbox().y;
			position.setLocation(position.getX(), -this.getHitbox().y);
		} else if (position.getY() + gp.tileSize > gp.worldHeight && velocity.y > 0) {
			velocity.y = 0;
//			this.y = gp.worldHeight - gp.tileSize;
			position.setLocation(position.getX(), gp.worldHeight - gp.tileSize);
		}
	}
	
	public void checkOtherGameObjectCollision(GameObject block, Iterator<GameObject> iter) {
		if (block instanceof KeyObject) {
			// play sound
			gp.playSFX("pickupCoin.wav");
			
			// show text "Key picked up" and delete after 1s
			GUI gui = new GUI(position.getX(), position.getY(), "Key picked up");
			gp.GUIList.add(gui);
			timer.schedule(new DeleteGUI(gui), 1000); // 1000 milliseconds
			
			// increment hasKey and remove block from gp.gameObjects
			hasKey++;
			iter.remove();
		} else if (block instanceof DoorObject) {
			if (hasKey > 0) {
				gp.playSFX("doorOpened.wav");
				hasKey--;
				iter.remove();
			}
		} else if (block instanceof BootsObject) {
			gp.playSFX("powerUp.wav");
			speed += 2;
			
			GUI gui = new GUI(position.getX(), position.getY(), "+Speed");
			gp.GUIList.add(gui);
			timer.schedule(new DeleteGUI(gui), 1000); // 1000 milliseconds
			
			iter.remove();
		} else if (block instanceof Oldman) {
			if (keyHandler.spacePressed) {
				gp.gameState = GameState.inDialogue;
			}
		}
	}
	
	public void applyGravity(double dt) {
//		System.out.println(dt);
		velocity.y += ay * (dt - 1);
		if (velocity.y > 30) {
			velocity.y = 30;
		}
	}
	
	public void updatePlayerHorizontalPosition() {
//		if (keyHandler.upPressed) worldX += velocity.x;
//		else if (keyHandler.downPressed) worldX -= velocity.x;
		
//		this.x += velocity.x;
		position.setLocation(position.getX() + velocity.x, position.getY());
	}
	
	public void updatePlayerVerticalPosition() {
//		if (keyHandler.upPressed) worldY += velocity.y;
//		else if (keyHandler.downPressed) worldY -= velocity.y;
//		this.y += velocity.y;
		position.setLocation(position.getX(), position.getY() + velocity.y);
	}
	
	public void gettingHit() {
		
	}
	
	@Override
	public void draw(Graphics2D g2) {
//		g2.setColor(Color.white);
//		g2.fillRect(x, y, gp.tileSize, gp.tileSize);
		
		
//		g2.rotate(Math.toRadians(rotationAngle), worldX + gp.tileSize / 2, worldY + gp.tileSize / 2);
		
//		g2.scale(-1, 1);
		g2.drawImage(currentAnimation.getCurrentFrame(), (int) position.getX(), (int) position.getY(), gp.tileSize, gp.tileSize, null);
//		g2.scale(-1, 1);
//		g2.drawRect((int) worldX, (int) worldY, gp.tileSize, gp.tileSize);
//		g2.rotate(Math.toRadians(-rotationAngle), worldX + gp.tileSize / 2, worldY + gp.tileSize / 2);
	}
}
