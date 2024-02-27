package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
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

	GamePanel gp;
	Tile[] tile;
	int mapTileNum[][];
	
	public TileManager(GamePanel gp) {
		this.gp = gp;
		
		tile = new Tile[10];
		mapTileNum = new int [gp.maxWorldCol][gp.maxWorldRow];
		
		getTileImage();
		
	}
	
	public void getTileImage() {
		try {
			tile[0] = new Tile();
			tile[0].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
			
			tile[1] = new Tile();
			tile[1].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
			tile[1].hasCollision = true;
			
			tile[2] = new Tile();
			tile[2].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
			tile[2].hasCollision = true;
			
			tile[3] = new Tile();
			tile[3].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));
			
			tile[4] = new Tile();
			tile[4].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
			tile[4].hasCollision = true;
			
			tile[5] = new Tile();
			tile[5].tileImage = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));
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
					gp.addGameObject(block);
					
					// default of hasCollision is false so if it doesn't have collision we don't need to do anything
					if (tile[num].hasCollision) {
						block.hasCollision = true;
					} 
					
				}
			}
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		
		for (int i=0; i<gp.maxWorldRow; i++) {
			for (int j=0; j<gp.maxWorldCol; j++) {
				
//				int worldX = j * gp.tileSize;
//				int worldY = i * gp.tileSize;
//				int screenX = worldX - gp.player.worldX + gp.player.screenX;
//				int screenY = worldY - gp.player.worldY + gp.player.screenY;
//				// basically, we move THE MAP to fit inside the camera (the camera stays at 0,0)
//				
//				// we add/minus 100 to fill in the black spaces
//				// in the future if you don't understand, you can try removing 100 and see how it changes
//				if (worldX >= gp.player.worldX - gp.player.screenX - 100 &&
//					worldX <= gp.player.worldX + gp.player.screenX + 100 &&
//					worldY >= gp.player.worldY - gp.player.screenY - 100 &&
//					worldY <= gp.player.worldY + gp.player.screenY+ 100) {
//					
//					g2.drawImage(tile[mapTileNum[j][i]].tileImage, screenX, screenY, gp.tileSize, gp.tileSize, null);
//				}
				
				int worldX = j * gp.tileSize;
				int worldY = i * gp.tileSize;
				g2.drawImage(tile[mapTileNum[j][i]].tileImage, worldX, worldY, gp.tileSize, gp.tileSize, null);
			}
		}
		
//		g2.drawImage(tile[0].tileImage, 0, 0, gp.tileSize, gp.tileSize, null);
	}
	
	public void drawSingle(Graphics2D g2, int positionX, int positionY) {
		g2.drawImage(tile[mapTileNum[positionX / gp.tileSize][positionY / gp.tileSize]].tileImage, positionX, positionY, gp.tileSize, gp.tileSize, null);
	}
}
