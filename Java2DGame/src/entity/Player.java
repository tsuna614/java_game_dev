package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
 
public class Player extends Entity {
	
	GamePanel gp;
	KeyHandler keyHandler;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyHandler = keyH;
		
		setDefaultValues();
		getPlayerImage();
	}
	
	public void setDefaultValues() {
		x = 100;
		y = 100;
		speed = 4;
		direction = "down";
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
		if (keyHandler.upPressed) {
			direction = "up";
			y -= speed;
		}
		if (keyHandler.downPressed) {
			direction = "down";
			y += speed;
		}
		if (keyHandler.leftPressed) {
			direction = "left";
			x -= speed;
		}
		if (keyHandler.rightPressed) {
			direction = "right";
			x += speed;
		}
		
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
		
		g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
	}
}