package objects;

import main.GamePanel;

public class KeyObject extends GameObject {
	
	public KeyObject() {
		super(-10000, 10000);
		getObjectImage("key.png");
	}

	public KeyObject(int x, int y, GamePanel gp) {
		super(x, y);
		this.gp = gp;
		
		getObjectImage("key.png");
	}
}
