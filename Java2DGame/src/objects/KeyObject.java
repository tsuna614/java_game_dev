package objects;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import main.GamePanel;

public class KeyObject extends GameObject {

	public KeyObject(Point2D position, GamePanel gp) {
		super(position);
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
