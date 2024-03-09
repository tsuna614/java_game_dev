package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	private static BufferedImage sprite;
	private static int TILE_SIZE = 16;
	private static int TILE_SIZE_32 = 32;
	
	public static void loadSprite(String filePath) {
		try {
			sprite = ImageIO.read(new File(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static BufferedImage getSprite(int xGrid, int yGrid) {
		// default
		if (sprite == null) {
			loadSprite("res/player/player.png");
//			loadSprite("res/tiles/Grass (48x48).png");
			
		}
		return sprite.getSubimage(xGrid * TILE_SIZE, yGrid * TILE_SIZE, TILE_SIZE, TILE_SIZE);
	}
	
	public static BufferedImage getSprite32(int xGrid, int yGrid) {
		// default
		if (sprite == null) {
			loadSprite("res/player/player.png");
		}
		return sprite.getSubimage(xGrid * TILE_SIZE_32, yGrid * TILE_SIZE_32, TILE_SIZE_32, TILE_SIZE_32);
	}
}
