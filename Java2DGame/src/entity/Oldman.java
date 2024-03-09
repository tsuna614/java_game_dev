package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import HUD.GUI;
import entity.Player.DeleteGUI;
import main.GamePanel;
import objects.GameObject;
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
	
	public Oldman(int x, int y, GamePanel gp) {
		super(x, y);
		
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
		g2.drawImage(currentAnimation.getCurrentFrame(), (int) x, (int) y, (int) this.getWidth(), (int) this.getHeight(), null);
	}
}
