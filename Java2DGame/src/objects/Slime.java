package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Random;

import main.GamePanel;
import objects.Oldman.NPCState;
import utils.Animation;
import utils.Sprite;
import utils.Vector2;

public class Slime extends GameObject {
	
	enum NPCState {
		idle,
		moving,
		die,
	}
	
	private Vector2 velocity;
	private String direction;
	private NPCState currentState;
	
	private Animation idleAnimation;
	private Animation movingAnimation;
	private Animation currentAnimation;
	
	float speed = (float) 0.5;
	int counter = 0;

	
	public Slime(Point2D position, GamePanel gp) {
		super(position);
		
		this.gp = gp;
		this.setWidth(gp.tileSize);
		this.setHeight(gp.tileSize);
		
		this.velocity = new Vector2(0, 0);
		this.direction = "left";
		this.currentState = NPCState.moving;
		
		this.isBlocking = false;
		
		loadAnimation();
		
		this.setHitBox(new Rectangle(8, 12, 16, 12));
	}
	
	public double calculateDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	public void update() {
		updateAnimation();
		changeDirection();
		updateHorizontalMovement();
		checkHorizontalCollision();
		updateVerticalMovement();
		checkVerticalCollision();
	}
	
	public void updateAnimation() {
		if (currentState == NPCState.idle) {
			currentAnimation = idleAnimation;
		} else if (currentState == NPCState.moving) {
			currentAnimation = movingAnimation;
		}
		currentAnimation.update();
	}
	
	public void changeDirection() {
		if (currentState == NPCState.idle) return;
		
		if (calculateDistance(position.getX(), position.getY(), gp.player.getPosition().getX(), gp.player.getPosition().getY()) <= 200) {
			counter = 0;
			if (position.getX() < gp.player.getPosition().getX()) {
				velocity.x = speed;
			} else {
				velocity.x = -speed;
			}
			
			if (position.getY() < gp.player.getPosition().getY()) {
				velocity.y = speed;
			} else {
				velocity.y = -speed;
			}	
		} else {
			counter++;
			if (counter >= 100) {
				Random rand = new Random();
				int randomNumber = rand.nextInt(100) + 1; // get random number range 1 - 100
				
//			velocity.x = 0;
//			velocity.y = 0;	
				if (randomNumber <= 25) {
					direction = "left";
					if (velocity.x >= 0) velocity.x += -speed;
				} else if (randomNumber <= 50) {
					direction = "right";
					if (velocity.x <= 0) velocity.x += speed;
				} else if (randomNumber <= 75) {
					if (velocity.y >= 0) velocity.y += -speed;
				} else {
					if (velocity.y <= 0) velocity.y += speed;
				}
				
				counter = 0;
			}
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
//						this.x = blockX + blockWidth - this.getHitbox().x;
						position.setLocation(blockX + blockWidth - this.getHitbox().x, position.getY());
					}
					else if (velocity.x > 0) {
						velocity.x = 0;
//						this.x = blockX - this.getWidth() + this.getHitbox().x;
						position.setLocation(blockX - this.getWidth() + this.getHitbox().x, position.getY());
					}
				}
			}
		}
		
		// check collision with world bounder
		if (position.getX()< 0 && velocity.x < 0) {
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
			
			// excluding itself
			if (this == block) continue;

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
	
	public void updateHorizontalMovement() {
//		this.x += velocity.x;
		position.setLocation(position.getX() + velocity.x, position.getY());
	}
	
	public void updateVerticalMovement() {
//		this.y += velocity.y;	
		position.setLocation(position.getX(), position.getY() + velocity.y);
	}
	
	public void loadAnimation() {
		Sprite.loadSprite("res/player/Slime.png");
		BufferedImage[] idleSprites = {Sprite.getSprite32(0, 0), Sprite.getSprite32(1, 0), Sprite.getSprite32(2, 0), Sprite.getSprite32(3, 0)};
		BufferedImage[] movingSprites = {Sprite.getSprite32(0, 2), Sprite.getSprite32(1, 2), Sprite.getSprite32(2, 2), Sprite.getSprite32(3, 2),  Sprite.getSprite32(4, 2),  Sprite.getSprite32(5, 2)};
		
		idleAnimation = new Animation(idleSprites, 5);
		movingAnimation = new Animation(movingSprites, 5);
		
		currentAnimation = idleAnimation;
	}
	
	public void draw(Graphics2D g2) {
		if (direction == "right") {
			g2.drawImage(currentAnimation.getCurrentFrame(), (int) position.getX(), (int) position.getY(), gp.tileSize, gp.tileSize, null);
		} else {
			g2.drawImage(currentAnimation.getCurrentFrame(), (int) position.getX() + gp.tileSize, (int) position.getY(), - gp.tileSize, gp.tileSize, null);
		}
	}
}
