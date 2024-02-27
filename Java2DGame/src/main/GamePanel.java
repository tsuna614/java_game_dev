package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import entity.CollisionBlock;
import entity.Player;
import objects.DoorObject;
import objects.GameObject;
import objects.KeyObject;
import tile.TileManager;

// implements Runnable is for the gameThread
public class GamePanel extends JPanel implements Runnable {
	
	// SCREEN SETTINGS
	final int originalTileSize = 16;
	final int scale = 3; // scale up the sprite or else 16x16 is going to be very small
	
	public final int tileSize = originalTileSize * scale;
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = maxScreenCol * tileSize; // 768 pixels
	public final int screenHeight = maxScreenRow * tileSize; // 576 pixels
	
	int camX, camY;
	
	// WORLD SETTINGS
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = maxWorldCol * tileSize;
	public final int worldHeight = maxWorldRow * tileSize;
	
	int FPS = 60;
	
	KeyHandler keyHandler = new KeyHandler();
	Thread gameThread;
	
	public Player player = new Player(this, keyHandler);
	TileManager tileManager = new TileManager(this);
	
	// COLLISION BLOCKS
	public ArrayList<GameObject> gameObjects = new ArrayList<>();
	
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		
		this.addKeyListener(keyHandler);
		this.setFocusable(true); // with this, this GamePanel can be 'focused' to receive key input
		
		tileManager.loadMap("map02.txt");
		
		KeyObject key = new KeyObject(1000, 1000, this);
		DoorObject door = new DoorObject(tileSize * 10, tileSize * 11, this);
		KeyObject dummyObject = new KeyObject(-10000, -10000, this);
		
		gameObjects.add(door);
		gameObjects.add(key);
		gameObjects.add(dummyObject);
	}
	
	public void addGameObject(GameObject object) {
		this.gameObjects.add(object);
	}
	
	public void removeGameObject(GameObject object) {
		// wow for some reason if the object you're removing is the last object in the arraylist
		// this exception will occur: Exception in thread "Thread-0" java.util.ConcurrentModificationException
		// unreal
		this.gameObjects.remove(gameObjects.indexOf(object));
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

//	@Override
//	public void run() {
//		
//		double drawInterval = 1000000000/FPS; // 0.01666 seconds
//		double nextDrawTime = System.nanoTime() + drawInterval;
//		
//		
//		while (gameThread != null) {
//			// System.out.println("The game loop is running");
//			
//			// 1. UPDATE: update informations such as player's position
//			update();
//			
//			// 2. DRAW: re-draw the screen with the updated information
//			repaint(); // this somehow calls to the paintComponent method
//			
//			
//			try {
//				double remainingTime = nextDrawTime - System.nanoTime();
//				remainingTime /= 1000000; // change from nanosecond to millisecond
//				
//				if (remainingTime < 0) {
//					remainingTime = 0;
//				}
//				
//				Thread.sleep((long) remainingTime);
//				
//				nextDrawTime += drawInterval;
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//	}
	
	public boolean checkCollision(Player player, GameObject block) {
		final float playerX = player.worldX + player.hitbox.x;
		final float playerY = player.worldY + player.hitbox.y;
		final float playerWidth = player.hitbox.width;
		final float playerHeight = player.hitbox.height;
		
		return playerX < block.x + tileSize &&
				playerX + playerWidth > block.x &&
				playerY < block.y + tileSize &&
				playerY + playerHeight > block.y;
	}
	
	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while (gameThread != null) {
			
			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			
			lastTime = currentTime;
			
			if (delta >= 1) {
				update();
				repaint();
				delta--;
			}
		}
	}
	
	public void update() {
		
		player.update();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g; // change Graphics to Graphics2D
		
		g2.translate(- (player.worldX - screenWidth / 2), - (player.worldY - screenHeight / 2));
		// wny we have to put minus in front? i have no god damn idea
		
//		tileManager.draw(g2);
		
//		for (final CollisionBlock block : collisionBlocks) {
//			block.draw(g2);
//		}
		
		for (GameObject object : gameObjects) {
			if (object instanceof KeyObject) {
				object.draw(g2);
			} else if (object instanceof DoorObject) {
				object.draw(g2);
			} else {
				tileManager.drawSingle(g2, object.x, object.y);
			}
			
//			else {
//				g2.setColor(Color.white);
//				
//				g2.fillRect(object.x, object.y, 16 * 3, 16 * 3);
//			}
		}
		
		player.draw(g2);
		
		g2.dispose();
	}
}
