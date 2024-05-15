package objects;

import java.awt.geom.Point2D;

import main.GamePanel;

public class DoorObject extends GameObject {
	
	public DoorObject(Point2D position, GamePanel gp) {
		super(position);
		this.gp = gp;
		
		this.setWidth(gp.tileSize);
		this.setHeight(gp.tileSize);
		
		getObjectImage("door.png");
	}
}
