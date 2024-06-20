package objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import utils.Sprite;

public class TreeObject extends GameObject {
	public int treeType;
	
	public TreeObject(Point2D position, int treeType, GamePanel gp) {
		super(position);
		this.gp = gp;
		
		// the width and height of this tree object is only for drawing sprite
		// it has nothing to do with checking collisions
		// this is because tree objects have their own custom hitbox
		this.setWidth(gp.tileSize * 2);
		this.setHeight(gp.tileSize * 1);
		
		try {
			sprite = ImageIO.read(new File("res/tiles/tree.png"));
			sprite = sprite.getSubimage(treeType * 16 * 2, 0, 16 * 2, 16 * 3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (treeType == 2) {
			this.setHitBox(new Rectangle(32, 16, (int) gp.tileSize * 2 - 32 * 2, (int) gp.tileSize - 32));
		} else {
			this.setHitBox(new Rectangle(16, 0, (int) gp.tileSize * 2 - 16 * 2, (int) gp.tileSize - 16));			
		}
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.drawImage(this.sprite, (int) position.getX(), (int) position.getY()  - gp.tileSize * 2, gp.tileSize * 2, gp.tileSize * 3, null);
//		g2.drawPolygon(getHitboxAsPolygon());
//		g2.drawRect((int) this.position.getX(), (int) this.position.getY(), gp.tileSize * 2, gp.tileSize * 1);
	}
}
