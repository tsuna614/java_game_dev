package HUD;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.GamePanel.GameState;
import utils.Sprite;

public class HUD {
	private final GamePanel gp;
	private final Font arial_30;
	
	private BufferedImage key;
	private BufferedImage heart;
	private BufferedImage halfHeart;
	
	
	private String currentDialogue = "Hello there, Adventurer!";
	
	public HUD(GamePanel gp) {
		this.gp= gp;
		
		this.arial_30 = new Font("Arial", Font.PLAIN, 30);
		
		try {
			key = ImageIO.read(new File("res/objects/key.png"));
			Sprite.loadSprite("res/HUD/heart.png");
			heart = Sprite.getSprite(0, 0);
			halfHeart = Sprite.getSprite(1, 0);
		} catch (IOException e)   {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		if (gp.gameState == GameState.inMenu) {
			drawMenu(g2);
		} else if (gp.gameState == GameState.gameOver) {
			drawGameOverScreen(g2);
		} else {
			
//			drawKey(g2);
			
			drawHeart(g2);
			
			drawScore(g2);
			
			if (gp.gameState == GameState.inDialogue) {
				drawDialogueScreen(g2);
			}
			
			if (gp.gameState == GameState.paused) {
				drawPausedScreen(g2);			
			}
		}
		
	}
	
	private void drawGameOverScreen(Graphics2D g2) {
		g2.setFont(arial_30);
		g2.setColor(Color.white);
		g2.drawString("Game Over", gp.screenWidth / 2 - 100, gp.screenHeight / 2);
		g2.drawString("Your score: " + gp.score, gp.screenWidth / 2 - 130, gp.screenHeight / 2 + 40);
	}

	private void drawMenu(Graphics2D g2) {
		g2.setFont(arial_30);
		g2.setColor(Color.white);
		g2.drawString("Tank shooter 2D", gp.screenWidth / 2 - 100, gp.screenHeight / 2);
		g2.drawString("Press enter to start", gp.screenWidth / 2 - 120, gp.screenHeight / 2 + 50);
	}
	
	private void drawKey(Graphics2D g2) {
		g2.setFont(arial_30);
		g2.setColor(Color.white);
		g2.drawImage(key, (int) gp.camX + 30, (int) gp.camY + 20, gp.tileSize, gp.tileSize, null);
		g2.drawString("x " + gp.player.hasKey, gp.camX + 80, gp.camY + 60);
	}
	
	private void drawHeart(Graphics2D g2) {
		// every 2 currentLife player have, draw a full heart.
		for (int i=1; i<gp.tank.currentLife; i += 2) {
			g2.drawImage(heart, (int) gp.camX + 30 * i, (int) gp.camY + 20, gp.tileSize, gp.tileSize, null);			
		}
		
		// if the player's health is odd number, draw an extra half heart at the end
		if (gp.tank.currentLife % 2 != 0) {
			int positionX = (int) gp.camX + 30 * (gp.tank.currentLife);
			g2.drawImage(halfHeart, positionX, (int) gp.camY + 20, gp.tileSize, gp.tileSize, null);	
		}
	}
	
	private void drawScore(Graphics2D g2) {
		g2.setFont(arial_30);
		g2.setColor(Color.white);
		g2.drawString("Score: " + gp.score, gp.camX + 30, gp.camY + 120);
	}
	
	private void drawPausedScreen(Graphics2D g2) {
		float x = gp.camX + gp.screenWidth / 2 - gp.tileSize * (float) 1.5;
		float y = gp.camY + gp.screenHeight / 2 + gp.tileSize;
		g2.setFont(new Font("Arial", Font.PLAIN, 60));
		g2.setColor(Color.white);
		g2.drawString("Paused", x, y);
	}
	
	private void drawDialogueScreen(Graphics2D g2) {
		float x = gp.camX + gp.tileSize;
		float y = gp.camY + gp.tileSize;
		float width = gp.screenWidth - gp.tileSize * 2;
		float height = gp.tileSize * 3;
		
		Color c = new Color(0, 0, 0, 220);
		g2.setColor(c);
		g2.fillRoundRect((int) x, (int) y, (int) width, (int) height, 35, 35);
		
		g2.setColor(new Color(255, 255, 255));
		g2.drawRoundRect((int) x, (int) y, (int) width, (int) height, 35, 35);
		
		g2.setFont(new Font("Arial", Font.PLAIN, 20));
		g2.drawString(currentDialogue, x + 20, y + 40);
	}
	
	public void setCurrentDialogue(String dialogue) {
		this.currentDialogue = dialogue;
	}
}
