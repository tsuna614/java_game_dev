package utils;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import main.GamePanel;
import objects.BootsObject;
import objects.Bullet;
import objects.DoorObject;
import objects.EnemyTank;
import objects.GameObject;
import objects.KeyObject;
import objects.Oldman;
import objects.Slime;
import objects.TreeObject;
import objects.Wall;

public class ObjectManager {
	private static GamePanel gp;
	
	public static void loadGamePanel(GamePanel gamePanel) {
		gp = gamePanel;
	}

	public static void addObjects() {
		
		ArrayList<GameObject> gameObjects = new ArrayList<>();
		
//		gameObjects.add(new KeyObject(new Point2D.Double(1000, 1000), gp));
//		gameObjects.add(new DoorObject(gp.tileSize * 10, gp.tileSize * 11, gp));
//		gameObjects.add(new TreeObject(new Point2D.Double(400, 100), 1, gp));
//		gameObjects.add(new TreeObject(new Point2D.Double(200, 300), 0, gp));
//		gameObjects.add(new TreeObject(new Point2D.Double(200, 600), 2, gp));
//		gameObjects.add(new TreeObject(200, 300, 0, gp));
//		gameObjects.add(new TreeObject(200, 600, 2, gp));
		
//		gameObjects.add(new Oldman(new Point2D.Double(400, 200), gp));
//		gameObjects.add(new Slime(new Point2D.Double(100, 200), gp));
		
		gameObjects.add(new Wall(new Point2D.Double(10 * gp.tileSize, 25 * gp.tileSize), gp));
		gameObjects.add(new Wall(new Point2D.Double(10 * gp.tileSize, 26 * gp.tileSize), gp));
		gameObjects.add(new Wall(new Point2D.Double(10 * gp.tileSize, 27 * gp.tileSize), gp));
		gameObjects.add(new Wall(new Point2D.Double(10 * gp.tileSize, 28 * gp.tileSize), gp));
		gameObjects.add(new Wall(new Point2D.Double(10 * gp.tileSize, 29 * gp.tileSize), gp));
		gameObjects.add(new Wall(new Point2D.Double(10 * gp.tileSize, 30 * gp.tileSize), gp));
		
		gameObjects.add(new EnemyTank(new Point2D.Double(200, 100) , gp, gp.keyHandler));
		gameObjects.add(new EnemyTank(new Point2D.Double(500, 110) , gp, gp.keyHandler));
		gameObjects.add(new EnemyTank(new Point2D.Double(600, 150) , gp, gp.keyHandler));
		gameObjects.add(new EnemyTank(new Point2D.Double(700, 160) , gp, gp.keyHandler));
		gameObjects.add(new EnemyTank(new Point2D.Double(1000, 120) , gp, gp.keyHandler));
		gameObjects.add(new EnemyTank(new Point2D.Double(1500, 100) , gp, gp.keyHandler));
		gameObjects.add(new EnemyTank(new Point2D.Double(1200, 50) , gp, gp.keyHandler));
		
		

		
//		gameObjects.add(new Bullet(new Point2D.Double(-10, -10), gp, 90.0));

		for (GameObject object : gameObjects) {
			gp.addGameObject(object);		}
	}
}
