package utils;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import main.GamePanel;
import objects.BootsObject;
import objects.DoorObject;
import objects.GameObject;
import objects.KeyObject;
import objects.Oldman;
import objects.Slime;
import objects.TreeObject;

public class ObjectManager {
	private static GamePanel gp;
	
	public static void loadGamePanel(GamePanel gamePanel) {
		gp = gamePanel;
	}

	public static void addObjects() {
		
		ArrayList<GameObject> gameObjects = new ArrayList<>();
		
		gameObjects.add(new KeyObject(new Point2D.Double(1000, 1000), gp));
//		gameObjects.add(new DoorObject(gp.tileSize * 10, gp.tileSize * 11, gp));
		gameObjects.add(new TreeObject(new Point2D.Double(400, 100), 1, gp));
		gameObjects.add(new TreeObject(new Point2D.Double(200, 300), 0, gp));
		gameObjects.add(new TreeObject(new Point2D.Double(200, 600), 2, gp));
//		gameObjects.add(new TreeObject(200, 300, 0, gp));
//		gameObjects.add(new TreeObject(200, 600, 2, gp));
		
		gameObjects.add(new Oldman(new Point2D.Double(400, 200), gp));
		gameObjects.add(new Slime(new Point2D.Double(100, 200), gp));

		for (GameObject object : gameObjects) {
			gp.addGameObject(object);		}
	}
}
