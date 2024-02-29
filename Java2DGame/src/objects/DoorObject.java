package objects;

import main.GamePanel;

public class DoorObject extends GameObject {
	
	public DoorObject(int x, int y, GamePanel gp) {
		super(x, y);
		this.gp = gp;
		
		getObjectImage("door.png");
	}
}
