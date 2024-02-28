package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class Entity {

	public float worldX, worldY;
	public Vector2 velocity;
	public float speed = 1;

	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	
	public String direction;
	
	public int spriteCounter = 0;
	public int spriteNum = 1;
	
	public Rectangle hitbox;
	public boolean collisionOn = false;
	
	public void exchangeAngleToVelocity(float angle) {
		this.velocity.x = - (float) (Math.sin(Math.toRadians(angle)) * speed);
		this.velocity.y = (float) (Math.cos(Math.toRadians(angle)) * speed);
		System.out.print("x: " + velocity.x + " y: " + velocity.y + " | ");
	}
}
