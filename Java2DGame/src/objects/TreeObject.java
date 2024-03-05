package objects;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import utils.Sprite;

public class TreeObject extends GameObject {
	public int treeType;
	
	public TreeObject(float x, float y, int treeType, GamePanel gp) {
		super(x, y);
		this.gp = gp;
		
		this.setWidth(gp.tileSize * 2);
		this.setHeight(gp.tileSize * 1 - 16);
		
		try {
			sprite = ImageIO.read(new File("res/tiles/tree.png"));
			sprite = sprite.getSubimage(treeType * 16 * 2, 0, 16 * 2, 16 * 3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.drawImage(this.sprite, (int) x, (int) y  - gp.tileSize * 2, (int) this.getWidth(), gp.tileSize * 3, null);
	}
}
