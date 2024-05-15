package objects;

import java.awt.geom.Point2D;

import main.GamePanel;

public class BootsObject extends GameObject {
	
	public BootsObject(Point2D position, GamePanel gp) {
		super(position);
		this.gp = gp;
		
		this.setWidth(gp.tileSize);
		this.setHeight(gp.tileSize);
		
		getObjectImage("boots.png");
	}
}
