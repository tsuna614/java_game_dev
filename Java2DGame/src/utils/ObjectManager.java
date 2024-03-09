package utils;

import java.util.ArrayList;

import entity.Oldman;
import entity.Slime;
import main.GamePanel;
import objects.BootsObject;
import objects.DoorObject;
import objects.GameObject;
import objects.KeyObject;
import objects.TreeObject;

public class ObjectManager {
	private static GamePanel gp;
	
	public static void loadGamePanel(GamePanel gamePanel) {
		gp = gamePanel;
	}

	public static void addObjects() {
		
		ArrayList<GameObject> gameObjects = new ArrayList<>();
		
		gameObjects.add(new KeyObject(1000, 1000, gp));
//		gameObjects.add(new DoorObject(gp.tileSize * 10, gp.tileSize * 11, gp));
		gameObjects.add(new TreeObject(400, 100, 1, gp));
		gameObjects.add(new TreeObject(200, 300, 0, gp));
		gameObjects.add(new TreeObject(200, 600, 2, gp));
		
		gameObjects.add(new Oldman(400, 200, gp));
		gameObjects.add(new Slime(100, 200, gp));

		for (GameObject object : gameObjects) {
			gp.gameObjects.add(object);
		}
	}
}
