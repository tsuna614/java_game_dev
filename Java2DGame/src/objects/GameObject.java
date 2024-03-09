package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import utils.Animation;
import utils.Sprite;
import utils.Vector2;

public class GameObject {
	protected float x;
	protected float y;
	
	private float width = 0;
	private float height = 0;
	
	public boolean hasCollision = true;
	public boolean isBlocking = true;
	
	public BufferedImage sprite;
	
	protected GamePanel gp;
	Rectangle hitbox = new Rectangle(0, 0, 0, 0);
	
//	private BufferedImage[] testAnimation = {Sprite.getSprite(0, 0), Sprite.getSprite(0, 1), Sprite.getSprite(0, 2) };
//	public Animation animation = new Animation(testAnimation, 10);
	
	
	
////////////////// CONSTRUCTOR /////////////////////
	public GameObject(float x, float y) {
		this.x = x;
		this.y = y;
		
		this.height = 16 * 3;
		this.width = 16 * 3;
	}
	
	////////////////// OBJECT ATTRIBUTES /////////////////////
	public Vector2 getPosition() {
		return new Vector2(this.x, this.y);
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setHitBox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}
	public Rectangle getHitbox() {
		return this.hitbox;
	}
	
	////////////////// LOADING SPRITE AND DRAW /////////////////////
	public void getObjectImage(String imagePath) {
		try {
			sprite = ImageIO.read(getClass().getResourceAsStream("/objects/" + imagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		g2.drawImage(sprite, (int) x, (int) y, (int) width, (int) height, null);
	}
}
