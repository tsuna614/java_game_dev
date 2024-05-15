package objects;

import java.awt.geom.Point2D;

import main.GamePanel;
import main.KeyHandler;

enum TankState {
	moving,
	idle,
}

public class Tank extends GameObject {
	GamePanel gp;
	KeyHandler keyH;
	
	// Tank Properties
	Point2D position;
	float speed = 4;
	float rotationAngle = 0;
	Point2D velocity;
	
	TankState tankState;
	
	

	
	Tank(Point2D position, GamePanel gp, KeyHandler keyH) {
		super(position);
		this.gp = gp;
		this.keyH = keyH;
		
		
	}
}
