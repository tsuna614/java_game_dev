package HUD;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class GUI {
	private float positionX, positionY;
	private String text;
	private final float speed = 1;
	private final Font arial_20;
	
	public GUI(float x, float y, String text) {
		this.positionX = x;
		this.positionY = y;
		this.text = text;
		this.arial_20 = new Font("Arial", Font.PLAIN, 20);
	}
	
	public void update() {
		updatePosition();
	}
	
	void updatePosition() {
		positionY += -speed;
	}
	
	public void draw(Graphics2D g2) {
		g2.setFont(arial_20);
		g2.setColor(Color.white);
		g2.drawString(text, positionX, positionY);
	}
}
