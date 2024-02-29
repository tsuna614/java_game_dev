package objects;

import main.GamePanel;

public class BootsObject extends GameObject {
	
	public BootsObject(int x, int y, GamePanel gp) {
		super(x, y);
		this.gp = gp;
		
		getObjectImage("boots.png");
	}
}
