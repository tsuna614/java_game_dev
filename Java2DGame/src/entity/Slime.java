package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Random;

import entity.Oldman.NPCState;
import main.GamePanel;
import objects.GameObject;
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

	
	public Slime(float x, float y, GamePanel gp) {
		super(x, y);
		
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
	
	public double calculateDistance(float x1, float y1, float x2, float y2) {
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
		
		if (calculateDistance(x, y, gp.player.getPosition().x, gp.player.getPosition().y) <= 200) {
			counter = 0;
			if (x < gp.player.getPosition().x) {
				velocity.x = speed;
			} else {
				velocity.x = -speed;
			}
			
			if (y < gp.player.getPosition().y) {
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
			
			final float blockX = block.getPosition().x + block.getHitbox().x;
			final float blockWidth = block.getHitbox().getWidth() == 0 ? block.getWidth() : block.getHitbox().width;
			
			if (gp.checkCollision(this, block) && block.hasCollision) {
				if (block.isBlocking) {
					if (velocity.x < 0) {
						velocity.x = 0;
						this.x = blockX + blockWidth - this.getHitbox().x;
					}
					else if (velocity.x > 0) {
						velocity.x = 0;
						this.x = blockX - this.getWidth() + this.getHitbox().x;
					}
				}
			}
		}
		
		// check collision with world bounder
		if (this.x < 0 && velocity.x < 0) {
			velocity.x = 0;
			this.x = -this.getHitbox().x;
		} else if (this.x + gp.tileSize - this.getHitbox().x > gp.worldWidth && velocity.x > 0) {
			this.x = gp.worldWidth - gp.tileSize + this.getHitbox().x;
		}
	}
	
	public void checkVerticalCollision() {
		// using Iterator + while loop instead of just foreach loop to prevent the stupid ConcurrentModificationException bug
		Iterator<GameObject> iter = gp.gameObjects.iterator();
		while (iter.hasNext()) {
			GameObject block = iter.next();
			
			// excluding itself
			if (this == block) continue;

			final float blockY = block.getPosition().y + block.getHitbox().y;
			final float blockHeight = block.getHitbox().getHeight() == 0 ? block.getHeight() : block.getHitbox().height;
			
			if (gp.checkCollision(this, block) && block.hasCollision) {
				if (block.isBlocking) {
					if (velocity.y < 0) {
						velocity.y = 0;
						this.y = blockY + blockHeight - this.getHitbox().y;
					}
					else if (velocity.y > 0) {
						velocity.y = 0;
						this.y = blockY - this.getHeight();
					}
				}
			}
			
		}
		
		if (this.y + this.getHitbox().y < 0 && velocity.y < 0) {
			velocity.y = 0;
			this.y = -this.getHitbox().y;
		} else if (this.y + gp.tileSize > gp.worldHeight && velocity.y > 0) {
			velocity.y = 0;
			this.y = gp.worldHeight - gp.tileSize;
		}
	}
	
	public void updateHorizontalMovement() {
		this.x += velocity.x;
	}
	
	public void updateVerticalMovement() {
		this.y += velocity.y;		
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
			g2.drawImage(currentAnimation.getCurrentFrame(), (int) this.x, (int) this.y, gp.tileSize, gp.tileSize, null);
		} else {
			g2.drawImage(currentAnimation.getCurrentFrame(), (int) this.x + gp.tileSize, (int) this.y, - gp.tileSize, gp.tileSize, null);
		}
	}
}
