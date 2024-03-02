package utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entity.CollisionBlock;
import main.GamePanel;
import objects.GameObject;
import objects.KeyObject;

public class TileManager {
	public final int GRASS_CENTER 				= 10;
	public final int GRASS_CENTER_BOTTOM 		= 11;
	public final int GRASS_CENTER_TOP 			= 12;
	public final int GRASS_CENTER_RIGHT 		= 13;
	public final int GRASS_CENTER_LEFT 			= 14;
	public final int GRASS_TOP_LEFT 			= 15;
	public final int GRASS_TOP_RIGHT 			= 16;
	public final int GRASS_BOTTOM_LEFT 			= 17;
	public final int GRASS_BOTTOM_RIGHT 		= 18;
	public final int GRASS_CORNER_TOP_LEFT		= 19;
	public final int GRASS_CORNER_TOP_RIGHT 	= 20;
	public final int GRASS_CORNER_BOTTOM_LEFT 	= 21;
	public final int GRASS_CORNER_BOTTOM_RIGHT 	= 22;
	public final int GRASS_CENTER_WITH_WEED 	= 23;
	
	
	public final int TILE_SIZE = 16;
	

	GamePanel gp;
//	Tile[] tile;
	BufferedImage grassSprite;
	BufferedImage[] sprites = new BufferedImage[100];
	int mapTileNum[][];
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		
//		tile = new Tile[10];
		mapTileNum = new int [gp.maxWorldCol][gp.maxWorldRow];
		
		getTileImage();
		
	}
	
	public void getTileImage() {
		try {
			grassSprite = ImageIO.read(new File("res/tiles/Grass (96x48).png"));
			
			for (int i=0; i<= 23; i++) {
				switch (i) {
				case GRASS_TOP_LEFT:
					sprites[i] = grassSprite.getSubimage(0 * TILE_SIZE, 0 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					break;
				case GRASS_TOP_RIGHT:
					sprites[i] = grassSprite.getSubimage(2 * TILE_SIZE, 0 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					break;
				case GRASS_BOTTOM_LEFT:
					sprites[i] = grassSprite.getSubimage(0 * TILE_SIZE, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					break;
				case GRASS_BOTTOM_RIGHT:
					sprites[i] = grassSprite.getSubimage(2 * TILE_SIZE, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					break;
				case GRASS_CENTER:
					sprites[i] = grassSprite.getSubimage(1 * TILE_SIZE, 1 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					break;
				case GRASS_CENTER_TOP:
					sprites[i] = grassSprite.getSubimage(1 * TILE_SIZE, 0 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					break;
				case GRASS_CENTER_BOTTOM:
					sprites[i] = grassSprite.getSubimage(1 * TILE_SIZE, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					break;
				case GRASS_CENTER_LEFT:
					sprites[i] = grassSprite.getSubimage(0 * TILE_SIZE, 1 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					break;
				case GRASS_CENTER_RIGHT:
					sprites[i] = grassSprite.getSubimage(2 * TILE_SIZE, 1 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					break;
				case GRASS_CORNER_TOP_LEFT:
					sprites[i] = grassSprite.getSubimage(3 * TILE_SIZE, 0 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					break;
				case GRASS_CORNER_TOP_RIGHT:
					sprites[i] = grassSprite.getSubimage(4 * TILE_SIZE, 0 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					break;
				case GRASS_CORNER_BOTTOM_LEFT:
					sprites[i] = grassSprite.getSubimage(3 * TILE_SIZE, 1 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					break;
				case GRASS_CORNER_BOTTOM_RIGHT:
					sprites[i] = grassSprite.getSubimage(4 * TILE_SIZE, 1 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					break;
				case GRASS_CENTER_WITH_WEED:
					sprites[i] = grassSprite.getSubimage(5 * TILE_SIZE, 0 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					break;
				default:
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String fileName) {
		// this function basically get the map data from the map.txt file
		// and load it into mapTileNum
		try {
			InputStream is = getClass().getResourceAsStream("/maps/" + fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			for (int i=0; i<gp.maxWorldRow; i++) {
				
				String line = br.readLine();
				
				for (int j=0; j<gp.maxWorldCol; j++) {
					
					String numbers[] = line.split(" ");
					
					int num = Integer.parseInt(numbers[j]);
					
					mapTileNum[j][i] = num;
					
					
					GameObject block = new GameObject(j * gp.tileSize, i * gp.tileSize);
//					if (!tile[num].hasCollision) {
//						block.isBlocking = false;
//					}
					block.isBlocking = false;
					gp.addGameObject(block);
					
				}
			}
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public void draw(Graphics2D g2) {
//		
//		for (int i=0; i<gp.maxWorldRow; i++) {
//			for (int j=0; j<gp.maxWorldCol; j++) {
//				
//				int worldX = j * gp.tileSize;
//				int worldY = i * gp.tileSize;
////				g2.drawImage(tile[mapTileNum[j][i]].tileImage, worldX, worldY, gp.tileSize, gp.tileSize, null);
//			}
//		}
//		
////		g2.drawImage(tile[0].tileImage, 0, 0, gp.tileSize, gp.tileSize, null);
//	}
	
	public void drawSingle(Graphics2D g2, int positionX, int positionY) {
		int blockType = mapTileNum[positionX / gp.tileSize][positionY / gp.tileSize];
		
		g2.drawImage(sprites[blockType], positionX, positionY, gp.tileSize, gp.tileSize, null);
	}
}
