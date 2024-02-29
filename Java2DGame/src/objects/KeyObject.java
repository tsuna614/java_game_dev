package objects;

import main.GamePanel;

public class KeyObject extends GameObject {

	public KeyObject(int x, int y, GamePanel gp) {
		super(x, y);
		this.gp = gp;
		
		getObjectImage("key.png");
	}
}
