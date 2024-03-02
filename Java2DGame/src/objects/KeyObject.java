package objects;

import java.awt.Graphics2D;

import main.GamePanel;

public class KeyObject extends GameObject {
	
	public KeyObject() {
		super(-10000, 10000);
		getObjectImage("key.png");
	}

	public KeyObject(int x, int y, GamePanel gp) {
		super(x, y);
		this.gp = gp;
		
		this.setWidth(gp.tileSize);
		this.setHeight(gp.tileSize);
		
		getObjectImage("key.png");
	}
	
//	@Override
//	public void draw(Graphics2D g2) {
//		g2.drawImage(animation.getCurrentFrame(), x, y, gp.tileSize, gp.tileSize, null);
//	}
}
