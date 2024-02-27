package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class Entity {

	public int worldX, worldY;
	public Vector2 velocity;

	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	
	public String direction;
	
	public int spriteCounter = 0;
	public int spriteNum = 1;
	
	public Rectangle hitbox;
	public boolean collisionOn = false;
}
