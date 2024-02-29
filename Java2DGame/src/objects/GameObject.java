package objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class GameObject {
	public int x;
	public int y;
	
	public boolean hasCollision = true;
	public boolean isBlocking = true;
	
	public BufferedImage sprite;
	
	GamePanel gp;
	
	public GameObject(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void getObjectImage(String imagePath) {
		try {
			sprite = ImageIO.read(getClass().getResourceAsStream("/objects/" + imagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		g2.drawImage(sprite, x, y, gp.tileSize, gp.tileSize, null);
	}
}
