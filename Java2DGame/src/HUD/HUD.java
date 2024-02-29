package HUD;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import main.GamePanel;
import objects.KeyObject;

public class HUD {
	private final GamePanel gp;
	private final Font arial_30;
	private KeyObject key;
	
	public HUD(GamePanel gp) {
		this.gp= gp;
		
		this.arial_30 = new Font("Arial", Font.PLAIN, 30);
		
		key = new KeyObject();
	}
	
	public void draw(Graphics2D g2) {
		drawKey(g2);
//		drawBoots(g2);
	}
	
	private void drawKey(Graphics2D g2) {
		g2.setFont(arial_30);
		g2.setColor(Color.white);
		g2.drawImage(key.sprite, (int) gp.camX + 30, (int) gp.camY + 20, gp.tileSize, gp.tileSize, null);
		g2.drawString("x " + gp.player.hasKey, gp.camX + 80, gp.camY + 60);
	}
}
