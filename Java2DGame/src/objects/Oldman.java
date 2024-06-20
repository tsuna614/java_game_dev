package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import HUD.GUI;
import main.GamePanel;
import objects.Player.DeleteGUI;
import utils.Animation;
import utils.Sprite;
import utils.Vector2;

public class Oldman extends GameObject {
	
	enum NPCState {
		walking,
		idle,
	}
	
	private NPCState state = NPCState.walking;
	private String direction = "down";
	private Animation walkingLeft;
	private Animation walkingRight;
	private Animation walkingUp;
	private Animation walkingDown;
	private Animation currentAnimation;
	int counter = 0;
	float speed = (float) 0.5;
	Vector2 velocity = new Vector2(0, 0);
	
	public Oldman(Point2D position, GamePanel gp) {
		super(position);
		
		this.gp = gp;
		this.setWidth(gp.tileSize);
		this.setHeight(gp.tileSize);
		
		loadAnimation();
		
		this.setHitBox(new Rectangle(0, 0, gp.tileSize, gp.tileSize));
		
		this.isBlocking = false;
	}
	
	public void update() {
		changeDirection();
		updateAnimation();
		updateHorizontalMovement();
		checkHorizontalCollision();
		updateVerticalMovement();
		checkVerticalCollision();
	}
	
	public void updateAnimation() {
		switch (direction) {
		case "up":
			currentAnimation = walkingUp;
			break;
		case "down":
			currentAnimation = walkingDown;
			break;
		case "left":
			currentAnimation = walkingLeft;
			break;
		case "right":
			currentAnimation = walkingRight;
			break;
		}
		
		if (state == NPCState.idle) {
			currentAnimation.reset();
			currentAnimation.stop();
		} else if (state == NPCState.walking) {
			currentAnimation.start();
		}
		
		currentAnimation.update();
	}
	
	public void changeDirection() {
		if (state == NPCState.idle) return;
		counter++;
		if (counter >= 100) {
			Random rand = new Random();
			int randomNumber = rand.nextInt(100) + 1; // get random number range 1 - 100
			
			velocity.x = 0;
			velocity.y = 0;			
			if (randomNumber <= 25) {
				direction = "left";
				velocity.x = -speed;
			} else if (randomNumber <= 50) {
				direction = "right";
				velocity.x = speed;
			} else if (randomNumber <= 75) {
				direction = "up";
				velocity.y = -speed;
			} else {
				direction = "down";
				velocity.y = speed;
			}
			
			counter = 0;
		}
	}
	
	public void checkHorizontalCollision() {
		Iterator<GameObject> iter = gp.gameObjects.iterator();
		while (iter.hasNext()) {
			GameObject block = iter.next();
			
			// excluding itself
			if (this == block) continue;
			
			final double blockX = block.getPosition().getX() + block.getHitbox().x;
			final double blockWidth = block.getHitbox().getWidth() == 0 ? block.getWidth() : block.getHitbox().width;
			
			if (gp.checkCollision(this, block) && block.hasCollision) {
				if (block.isBlocking) {
					if (velocity.x < 0) {
						velocity.x = 0;
						position.setLocation(blockX + blockWidth - this.getHitbox().x, position.getY());
					}
					else if (velocity.x > 0) {
						velocity.x = 0;
						position.setLocation(blockX - this.getWidth() + this.getHitbox().x, position.getY());
					}
				}
			}
		}
		
		// check collision with world bounder
		if (position.getX() < 0 && velocity.x < 0) {
			velocity.x = 0;
			position.setLocation(-this.getHitbox().x, position.getY());
//			this.x = -this.getHitbox().x;
		} else if (position.getX() + gp.tileSize - this.getHitbox().x > gp.worldWidth && velocity.x > 0) {
			position.setLocation(gp.worldWidth - gp.tileSize + this.getHitbox().x, position.getY());
//			this.x = gp.worldWidth - gp.tileSize + this.getHitbox().x;
		}
	}
	
	public void checkVerticalCollision() {
		// using Iterator + while loop instead of just foreach loop to prevent the stupid ConcurrentModificationException bug
		Iterator<GameObject> iter = gp.gameObjects.iterator();
		while (iter.hasNext()) {
			GameObject block = iter.next();
			
			// excluding itself
			if (this == block) continue;

			final double blockY = block.getPosition().getY() + block.getHitbox().y;
			final double blockHeight = block.getHitbox().getHeight() == 0 ? block.getHeight() : block.getHitbox().height;
			
			if (gp.checkCollision(this, block) && block.hasCollision) {
				if (block.isBlocking) {
					if (velocity.y < 0) {
						velocity.y = 0;
						position.setLocation(position.getX(), blockY + blockHeight - this.getHitbox().y);
//						this.y = blockY + blockHeight - this.getHitbox().y;
					}
					else if (velocity.y > 0) {
						velocity.y = 0;
						position.setLocation(position.getX(), blockY - this.getHeight());
//						this.y = blockY - this.getHeight();
					}
				}
			}
			
		}
		
		// check collision with world bounder
		if (position.getY() + this.getHitbox().y < 0 && velocity.y < 0) {
			velocity.y = 0;
			position.setLocation(position.getX(), -this.getHitbox().y);
//			this.y = -this.getHitbox().y;
		} else if (position.getY() + gp.tileSize > gp.worldHeight && velocity.y > 0) {
			velocity.y = 0;
			position.setLocation(position.getX(), gp.worldHeight - gp.tileSize);
//			this.y = gp.worldHeight - gp.tileSize;
		}
	}
	
	public void updateHorizontalMovement() {
//		this.x += velocity.x;
		position.setLocation(position.getX() + velocity.x, position.getY());
	}
	
	public void updateVerticalMovement() {
//		this.y += velocity.y;		
		position.setLocation(position.getX(), position.getY() + velocity.y);
	}
	
	public void loadAnimation() {
		Sprite.loadSprite("res/player/Oldman.png");
		final BufferedImage[] walkLeft = {Sprite.getSprite(2, 0), Sprite.getSprite(3, 0)};
		final BufferedImage[] walkRight = {Sprite.getSprite(4, 0), Sprite.getSprite(5, 0)};
		final BufferedImage[] walkUp = {Sprite.getSprite(0, 1), Sprite.getSprite(1, 1)};
		final BufferedImage[] walkDown = {Sprite.getSprite(0, 0), Sprite.getSprite(1, 0)};
		
		walkingLeft = new Animation(walkLeft, 10);
		walkingRight = new Animation(walkRight, 10);
		walkingUp = new Animation(walkUp, 10);
		walkingDown = new Animation(walkDown, 10);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.drawImage(currentAnimation.getCurrentFrame(), (int) position.getX(), (int) position.getY(), (int) this.getWidth(), (int) this.getHeight(), null);
		g2.drawPolygon(getHitboxAsPolygon());
	}
}
