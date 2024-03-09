package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;
import objects.GameObject;
import utils.Animation;
import utils.Sprite;
import utils.Vector2;

public class Slime extends GameObject {
	
	enum NPCState {
		idle,
		moving,
		die,
	}
	
	private Vector2 velocity;
	private String direction;
	private NPCState currentState;
	
	private Animation idleAnimation;
	private Animation movingAnimation;
	private Animation currentAnimation;

	
	public Slime(float x, float y, GamePanel gp) {
		super(x, y);
		
		this.gp = gp;
		this.setWidth(gp.tileSize);
		this.setHeight(gp.tileSize);
		
		this.velocity = new Vector2(0, 0);
		this.direction = "left";
		this.currentState = NPCState.idle;
		
		this.isBlocking = false;
		
		loadAnimation();
		
		this.setHitBox(new Rectangle(8, 12, 16, 12));
	}
	
	public void update() {
		
	}
	
	public void loadAnimation() {
		Sprite.loadSprite("res/player/Slime.png");
		BufferedImage[] idleSprites = {Sprite.getSprite32(0, 0), Sprite.getSprite32(1, 0), Sprite.getSprite32(2, 0), Sprite.getSprite32(3, 0)};
		BufferedImage[] movingSprites = {Sprite.getSprite32(0, 1), Sprite.getSprite32(1, 1), Sprite.getSprite32(2, 1), Sprite.getSprite32(3, 1),  Sprite.getSprite32(4, 1),  Sprite.getSprite32(5, 1)};
		
		idleAnimation = new Animation(idleSprites, 10);
		movingAnimation = new Animation(movingSprites, 10);
		
		currentAnimation = idleAnimation;
	}
	
	public void draw(Graphics2D g2) {
		if (direction == "right") {
			g2.drawImage(currentAnimation.getCurrentFrame(), (int) this.x, (int) this.y, gp.tileSize, gp.tileSize, null);
		} else {
			g2.drawImage(currentAnimation.getCurrentFrame(), (int) this.x + gp.tileSize, (int) this.y, - gp.tileSize, gp.tileSize, null);
		}
	}
}
