package objects;

import java.awt.Graphics2D;
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
	double rotationAngle;
	
	BulletType bulletType = BulletType.light;
	
	int bulletExistedFramesCount = 0;
	
	
	// ANIMATIONS
	Animation bulletAnimation;
	Animation currentAnimation;
	
	public Bullet(Point2D position, GamePanel gp, double rotationAngle) {
		super(position);
		this.gp = gp;
		this.rotationAngle = rotationAngle;
		
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
		selfDestructBullet();
	}

//	private void updateAnimation() {
//		currentAnimation = bulletAnimation;
//		currentAnimation.start();
//		currentAnimation.update();
//	}
	
	private void updatePosition() {
		position.setLocation(position.getX() + velocity.x, position.getY() + velocity.y);
	}
	
	private void updateVelocityBasedOnAngle() {
		velocity.x = Math.sin(Math.toRadians(rotationAngle)) * speed;
		velocity.y = Math.cos(Math.toRadians(rotationAngle)) * speed;
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
		gp.removeGameObject(this);
	}

	public void draw(Graphics2D g2) {
		g2.rotate(Math.toRadians(-rotationAngle + 180), position.getX() + gp.tileSize / 2, position.getY() + gp.tileSize / 2);
		g2.drawImage(currentAnimation.getCurrentFrame(), (int) position.getX(), (int) position.getY(), (int) this.getWidth(), (int) this.getHeight(), null);
		g2.rotate(Math.toRadians(rotationAngle - 180), position.getX() + gp.tileSize / 2, position.getY() + gp.tileSize / 2);
	}
}
