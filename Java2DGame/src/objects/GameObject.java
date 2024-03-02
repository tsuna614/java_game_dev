package objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import utils.Animation;
import utils.Sprite;
import utils.Vector2;

public class GameObject {
	private float x;
	private float y;
	
	private float width = 0;
	private float height = 0;
	
	public boolean hasCollision = true;
	public boolean isBlocking = true;
	
	public BufferedImage sprite;
	
	GamePanel gp;
	
//	private BufferedImage[] testAnimation = {Sprite.getSprite(0, 0), Sprite.getSprite(0, 1), Sprite.getSprite(0, 2) };
//	public Animation animation = new Animation(testAnimation, 10);
	
	public GameObject(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2 getPosition() {
		return new Vector2(this.x, this.y);
	}
	
	////////////////// GET WIDTH AND HEIGHT /////////////////////
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
		g2.drawImage(sprite, (int) x, (int) y, gp.tileSize, gp.tileSize, null);
	}
}
