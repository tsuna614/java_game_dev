package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import utils.Animation;
import utils.Sprite;
import utils.Vector2;

public class GameObject {
//	protected float x;
//	protected float y;
	protected Point2D position;
	
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
	public GameObject(Point2D position) {
		this.position = position;
//		this.x = x;
//		this.y = y;
//		
		this.height = 16 * 3;
		this.width = 16 * 3;
	}
	
	////////////////// OBJECT ATTRIBUTES /////////////////////
	public Point2D getPosition() {
//		return new Vector2(this.x, this.y);
		return position;
	}
	
	public void setPosition(Point2D position) {
		this.position = position;
	}
	
//	public void setX(float x) {
//		this.x = x;
//	}
//	
//	public void setY(float y) {
//		this.y = y;
//	}
	
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
	
	////////////////// RECTANGLE TO POLYGON /////////////////////
	
	public Polygon getHitboxAsPolygon() {
//		Rectangle rect = this.hitbox;
//		int[] xpoints = {rect.x, rect.x + rect.width, rect.x + rect.width, rect.x};
//		int[] ypoints = {rect.y, rect.y, rect.y + rect.height, rect.y + rect.height};
		int[] xpoints = {(int) this.position.getX(), (int) (this.position.getX() + this.width), (int) (this.position.getX() + this.width), (int) this.position.getX()};
		int[] ypoints = {(int) this.position.getY(), (int) this.position.getY(), (int) (this.position.getY() + this.width), (int) (this.position.getY() + this.width)};
		return new Polygon(xpoints, ypoints, 4); 
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
		g2.drawImage(sprite, (int) position.getX(), (int) position.getY(), (int) width, (int) height, null);
//		g2.setColor(Color.red);
//		g2.drawPolygon(this.getHitboxAsPolygon());
	}
}
