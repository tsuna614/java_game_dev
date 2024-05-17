package objects;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.KeyHandler;
import utils.Animation;
import utils.Sprite;
import utils.Vector2;

enum TankState {
	moving,
	idle,
}

public class Tank extends GameObject {
	GamePanel gp;
	KeyHandler keyH;
	
	// Tank Properties
	double speed = 2;
	public float rotationAngle = 0;
	Vector2 velocity = new Vector2(0, 0);
	
	TankState tankState = TankState.idle;
	
	private Animation movingAnimation;
	private Animation currentAnimation;

	
	public Tank(Point2D position, GamePanel gp, KeyHandler keyH) {
		super(position);
		this.gp = gp;
		this.keyH = keyH;
		
		this.setWidth(64);
		this.setHeight(64);
		
		loadTankAnimation();
	}
	
	private void loadTankAnimation() {
		Sprite.loadSprite("res/player/tank2.png");
		final BufferedImage[] movingSprites = {Sprite.getSprite256(0, 0), Sprite.getSprite256(1, 0)};
		
		movingAnimation = new Animation(movingSprites, 10);
	}


	public void update() {
		updateTankAnimation();
		checkTankInput();
//		applyGravity(dt);
		updateTankVerticalPosition();
		checkVerticalCollision();
		updateTankHorizontalPosition();
		checkHorizontalCollision();	
	}

	private void checkHorizontalCollision() {
		// TODO Auto-generated method stub
		
	}

	private void checkVerticalCollision() {
		// TODO Auto-generated method stub
		
	}

	
	private void updateTankHorizontalPosition() {
		// TODO Auto-generated method stub
		position.setLocation(position.getX() + velocity.x, position.getY());
	}

	private void updateTankVerticalPosition() {
		// TODO Auto-generated method stub
		position.setLocation(position.getX(), position.getY() + velocity.y);
	}


	private void checkTankInput() {
		velocity.x = 0;
		velocity.y = 0;

		if (keyH.upPressed) {
			velocity.x += speed;
			velocity.y += speed;
		}
		if (keyH.downPressed) {
			velocity.x -= speed;
			velocity.y -= speed;
		}
		if (keyH.leftPressed) {
//			velocity.x -= speed;
			rotationAngle += 1;
		}
		if (keyH.rightPressed) {
//			velocity.x += speed;
			rotationAngle -= 1;
		}
		
		if (rotationAngle > 360) {
		rotationAngle = 1;
		} else if (rotationAngle < 0) {
		rotationAngle = 359;
		}
		
		tankState = TankState.idle;
		if (velocity.x != 0 || velocity.y != 0) {
			tankState = TankState.moving;
		}
		
		updateVelocityBasedOnAngle();
	}
	
	public void shoot() {
		gp.addGameObject(new Bullet(new Point2D.Double(position.getX(), position.getY()), gp, rotationAngle));
//		gp.addGameObject(new Bullet(new Point2D.Double(0, 0), gp, rotationAngle));
	}

	// this method essentially convert the vx and vy so that the object's movespeed will always be constant no matter the move angle
	private void updateVelocityBasedOnAngle() {
		velocity.x = Math.sin(Math.toRadians(rotationAngle)) * velocity.x;
		velocity.y = Math.cos(Math.toRadians(rotationAngle)) * velocity.y;
	}

	private void updateTankAnimation() {
		// TODO Auto-generated method stub
		currentAnimation = movingAnimation;
		
		if (tankState == TankState.moving) {
			currentAnimation.start();
		} else if (tankState == TankState.idle) {
			currentAnimation.stop();
		}
		
		currentAnimation.update();
	}


	public void draw(Graphics2D g2) {
		g2.rotate(Math.toRadians(-rotationAngle + 180), position.getX() + gp.tileSize / 2, position.getY() + gp.tileSize / 2);
		g2.drawImage(currentAnimation.getCurrentFrame(), (int) position.getX(), (int) position.getY(), (int) this.getWidth(), (int) this.getHeight(), null);
		// reset the g2 after rotating it to draw the player (uncomment to see funny shit)
		g2.rotate(Math.toRadians(rotationAngle - 180), position.getX() + gp.tileSize / 2, position.getY() + gp.tileSize / 2);
	}
	
	
}
