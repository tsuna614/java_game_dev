package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import objects.BootsObject;
import objects.DoorObject;
import objects.GameObject;
import objects.KeyObject;
 
public class Player extends Entity {
	
	GamePanel gp;
	KeyHandler keyHandler;
	
	float speed = 4;
	
	int hasKey = 0;
	
	float rotationAngle = 0;
	
//	public final int screenX;
//	public final int screenY; // these indicate where we draw the player on the screen
	
	public Player(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyHandler = keyH;
		
//		screenX = gp.screenWidth / 2 - (gp.tileSize/2);
//		screenY = gp.screenHeight / 2 - (gp.tileSize/2);
		// we have to minus half the length of the player's tileSize so that the player
		// is positioned exactly at the center
		
		hitbox = new Rectangle(8, 16, 32, 32);
		
		velocity = new Vector2(0,0);
		
		setDefaultValues();
		getPlayerImage();
	}
	
	public void setDefaultValues() {
//		worldX = 0;
//		worldY = 0;
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		direction = "down";
		collisionOn = true;
	}
	
	public void getPlayerImage() {
		try {
			up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
			up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
			down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
			left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
			left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
			right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
			right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		if (rotationAngle > 360) {
			rotationAngle = 1;
		} else if (rotationAngle < 0) {
			rotationAngle = 359;
		}
		checkPlayerInput();
		updatePlayerVerticalPosition();
		if (collisionOn) {
			checkVerticalCollision();
		}
		updatePlayerHorizontalPosition();
		if (collisionOn) {
			checkHorizontalCollision();
		}
		alternateSprite();
	}
	
	public void checkPlayerInput() {
		velocity.x = 0;
		velocity.y = 0;
		
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
		
		
//		this.exchangeAngleToVelocity(rotationAngle);
//		
//		if (keyHandler.leftPressed) {
//			rotationAngle -= 2;
//		}
//		if (keyHandler.rightPressed) {
//			rotationAngle += 2;
//		}
	}
	
	public void checkHorizontalCollision() {
		Iterator<GameObject> iter = gp.gameObjects.iterator();
		while (iter.hasNext()) {
			GameObject block = iter.next();
//		for (final GameObject block : gp.gameObjects) {
			if (gp.checkCollision(this, block) && block.hasCollision) {
				if (block.isBlocking) {
					if (velocity.x < 0) {
						velocity.x = 0;
						this.worldX = block.x + gp.tileSize - hitbox.x;
					}
					else if (velocity.x > 0) {
						velocity.x = 0;
						this.worldX = block.x - gp.tileSize + hitbox.x;
					}
				}
				checkOtherGameObjectCollision(block, iter);
			}
			
		}
	}
	
	public void checkVerticalCollision() {
		// using Iterator + while loop instead of just foreach loop to prevent the stupid ConcurrentModificationException bug
		Iterator<GameObject> iter = gp.gameObjects.iterator();
		while (iter.hasNext()) {
			GameObject block = iter.next();
//		for (final GameObject block : gp.gameObjects) {
			if (gp.checkCollision(this, block) && block.hasCollision) {
				if (block.isBlocking) {
					if (velocity.y < 0) {
						velocity.y = 0;
						this.worldY = block.y + gp.tileSize - hitbox.y;
					}
					else if (velocity.y > 0) {
						velocity.y = 0;
						this.worldY = block.y - gp.tileSize;
					}
				}
				checkOtherGameObjectCollision(block, iter);
			}
			
		}
	}
	
	public void checkOtherGameObjectCollision(GameObject block, Iterator<GameObject> iter) {
		if (block instanceof KeyObject) {
			System.out.println("Key picked up");
			gp.playSFX("pickupCoin.wav");
			hasKey++;
			iter.remove();
		} else if (block instanceof DoorObject) {
			if (hasKey > 0) {
				System.out.println("Door opened");
				gp.playSFX("doorOpened.wav");
				hasKey--;
				iter.remove();
			}
		} else if (block instanceof BootsObject) {
			System.out.println("Boots picked up");
			gp.playSFX("powerUp.wav");
			speed += 2;
			iter.remove();
		}
	}
	
	public void updatePlayerHorizontalPosition() {
//		if (keyHandler.upPressed) worldX += velocity.x;
//		else if (keyHandler.downPressed) worldX -= velocity.x;
		worldX += velocity.x;
	}
	
	public void updatePlayerVerticalPosition() {
//		if (keyHandler.upPressed) worldY += velocity.y;
//		else if (keyHandler.downPressed) worldY -= velocity.y;
		worldY += velocity.y;
	}
	
	public void alternateSprite() {
		// this is to alternate between 2 sprites to make an animation
		// as you can tell, it only work if it has only 2 different sprites, so it kinda sucks
		if (!keyHandler.upPressed && !keyHandler.downPressed && !keyHandler.leftPressed && !keyHandler.rightPressed)
			return;
		spriteCounter++;
		if (spriteCounter > 10) {
			if (spriteNum == 1)
				spriteNum = 2;
			else spriteNum = 1;
			
			spriteCounter = 0;
		}
	}
	
	public void draw(Graphics2D g2) {
//		g2.setColor(Color.white);
//		g2.fillRect(x, y, gp.tileSize, gp.tileSize);
		
		BufferedImage image = null;
		
		if (spriteNum == 1) {
			switch(direction) {
			case "up":
				image = up1;
				break;
			case "down":
				image = down1;
				break;
			case "left":
				image = left1;
				break;
			case "right":
				image = right1;
				break;
			default:
			}
		} else {
			switch(direction) {
			case "up":
				image = up2;
				break;
			case "down":
				image = down2;
				break;
			case "left":
				image = left2;
				break;
			case "right":
				image = right2;
				break;
			default:
			}
		}
		
//		g2.rotate(Math.toRadians(rotationAngle), worldX + gp.tileSize / 2, worldY + gp.tileSize / 2);
		g2.drawImage(image, (int) worldX, (int) worldY, gp.tileSize, gp.tileSize, null);
//		g2.drawRect((int) worldX, (int) worldY, gp.tileSize, gp.tileSize);
//		g2.rotate(Math.toRadians(-rotationAngle), worldX + gp.tileSize / 2, worldY + gp.tileSize / 2);
	}
}
