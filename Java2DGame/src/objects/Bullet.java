package objects;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import main.GamePanel;
import main.KeyHandler;
import utils.Animation;
import utils.Sprite;
import utils.Vector2;

enum BulletType {
	light,
	heavy,
	sniper,
}


public class Bullet extends GameObject {
	GamePanel gp;
	
	// BULLET PROPERTIES
	Vector2 velocity = new Vector2(0, 0);
	double speed = 10;
	float rotationAngle;
	
	BulletType bulletType = BulletType.light;
	
	int bulletExistedFramesCount = 0;
	
	boolean isPlayerBullet;
	
	int[] xPoly = {0, 0, 0, 0};
	int[] yPoly = {0, 0, 0, 0};
	
	private Polygon newHitbox = new Polygon(xPoly, yPoly, 4);
	
	
	// ANIMATIONS
	Animation bulletAnimation;
	Animation currentAnimation;
	
	public Bullet(Point2D position, GamePanel gp, float rotationAngle, boolean isPlayerBullet) {
		super(position);
		this.gp = gp;
		this.rotationAngle = rotationAngle;
		this.isPlayerBullet = isPlayerBullet;
		
		this.isBlocking = false;
		
//		this.setWidth(128);
//		this.setHeight(128);
		
		loadBulletAnimation();
		updateVelocityBasedOnAngle();
	}
	
	private void loadBulletAnimation() {
		Sprite.loadSprite("res/player/bullets.png");
		
		final BufferedImage[] bulletSprites = {Sprite.getSprite256(2, 1)};
		
		bulletAnimation = new Animation(bulletSprites, 10);
		currentAnimation = bulletAnimation;
	}

	public void update() {
//		updateAnimation();
		updatePosition();
		updateNewHitbox();
		selfDestructBullet();
	}

//	private void updateAnimation() {
//		currentAnimation = bulletAnimation;
//		currentAnimation.start();
//		currentAnimation.update();
//	}
	
	private void updateNewHitbox() {
		float radius = gp.tileSize / 4;
		for (int i=0; i<xPoly.length; i++) {
			float angle = rotationAngle - 45 + i * 90;
			xPoly[i] = (int) (Math.sin(Math.toRadians(angle)) * radius / 2 + this.position.getX() + this.getWidth() / 2);
			
			yPoly[i] = (int) (-Math.cos(Math.toRadians(angle)) * radius / 2 + this.position.getY() + this.getHeight() / 2);
		}
		
		newHitbox = new Polygon(xPoly, yPoly, 4);
	}
	
	private void updatePosition() {
		position.setLocation(position.getX() + velocity.x, position.getY() + velocity.y);
	}
	
	private void updateVelocityBasedOnAngle() {
		velocity.x = Math.sin(Math.toRadians(rotationAngle)) * speed;
		velocity.y = -Math.cos(Math.toRadians(rotationAngle)) * speed;
	}
	
	private void selfDestructBullet() {
		if (bulletExistedFramesCount >= 300)
		{
			remove();
		}
		bulletExistedFramesCount++;
	}
	
	private void remove() {
		System.out.print("Deleted");
//		gp.gameObjects.remove(this);
//		this.remove();
		gp.removeGameObject(this);
	}

	public void draw(Graphics2D g2) {
		g2.rotate(Math.toRadians(rotationAngle), position.getX() + gp.tileSize / 2, position.getY() + gp.tileSize / 2);
		g2.drawImage(currentAnimation.getCurrentFrame(), (int) position.getX(), (int) position.getY(), (int) this.getWidth(), (int) this.getHeight(), null);
//		g2.drawRect((int) position.getX(), (int) position.getY(), (int) getWidth(), (int) getHeight());
		g2.rotate(Math.toRadians(-rotationAngle), position.getX() + gp.tileSize / 2, position.getY() + gp.tileSize / 2);
//		g2.drawPolygon(newHitbox);
	}
}
