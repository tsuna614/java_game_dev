package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import HUD.GUI;
import HUD.HUD;
import entity.CollisionBlock;
import entity.Player;
import objects.BootsObject;
import objects.DoorObject;
import objects.GameObject;
import objects.KeyObject;
import utils.TileManager;

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
	
	public float camX, camY;
	
	// WORLD SETTINGS
	public final int maxWorldCol = 43;
	public final int maxWorldRow = 41;
	public final int worldWidth = maxWorldCol * tileSize;
	public final int worldHeight = maxWorldRow * tileSize;
	
	int FPS = 60;
	
	KeyHandler keyHandler = new KeyHandler();
	Thread gameThread;
	
	// PLAYER
	public Player player = new Player(this, keyHandler);
	
	// DRAW TILES AND SET INVISIBLE WALLS
	TileManager tileManager = new TileManager(this);
	
	// COLLISION BLOCKS
	public ArrayList<GameObject> gameObjects = new ArrayList<>();
	
	// SOUND
	Sound sound = new Sound();
	
	// HUD
	public HUD hud = new HUD(this);
	public ArrayList<GUI> GUIList = new ArrayList<>();
	
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		
		this.addKeyListener(keyHandler);
		this.setFocusable(true); // with this, this GamePanel can be 'focused' to receive key input
		
		tileManager.loadMap("map03.txt");
		
		
		playMusic("BlueBoyAdventure.wav");
		
		KeyObject key = new KeyObject(1000, 1000, this);
		KeyObject key2 = new KeyObject(1400, 1000, this);
		KeyObject key3 = new KeyObject(1200, 1000, this);
		DoorObject door = new DoorObject(tileSize * 10, tileSize * 11, this);
		DoorObject door2 = new DoorObject(tileSize * 12, tileSize * 23, this);
		BootsObject boot = new BootsObject(tileSize * 23, tileSize * 39, this);
		
		gameObjects.add(key);
		gameObjects.add(key2);
		gameObjects.add(key3);
		gameObjects.add(door);
		gameObjects.add(door2);
		gameObjects.add(boot);
	}
	
	public void addGameObject(GameObject object) {
		this.gameObjects.add(object);
	}
	
	public void removeGameObject(GameObject object) {
		// wow for some reason if the object you're removing is NOT the second last object in the arraylist
		// this exception will occur: Exception in thread "Thread-0" java.util.ConcurrentModificationException
		// unreal
		this.gameObjects.remove(object);
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
		
		return playerX < block.getPosition().x + tileSize &&
				playerX + playerWidth > block.getPosition().x &&
				playerY < block.getPosition().y + tileSize &&
				playerY + playerHeight > block.getPosition().y;
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
				update(delta);
				repaint();
				delta--;
			}
		}
	}
	
	public void update(double dt) {
		
		player.update(dt);
		
//		for (GameObject object : gameObjects) {
//			if (object instanceof KeyObject) {
//				object.update();
//			}
//		}
		
		for (GUI object : GUIList) {
			object.update(dt);
		}
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g; // change Graphics to Graphics2D
		
		camX = (player.worldX - screenWidth / 2);
		camY = (player.worldY - screenHeight / 2);
		
		g2.translate(-camX, -camY);
		
		g2.setColor(Color.decode("#0090e6"));
		g2.fillRect((int) camX, (int) camY, screenWidth, screenHeight);

		long drawStart = 0;
		if (keyHandler.TPressed) {
			drawStart = System.nanoTime();
		}
		
		for (GameObject object : gameObjects) {
			if (object instanceof KeyObject) {
				object.draw(g2);
			} else if (object instanceof DoorObject) {
				object.draw(g2);
			} else if (object instanceof BootsObject) {
				object.draw(g2);
			} else {
				tileManager.drawSingle(g2, (int) object.getPosition().x, (int) object.getPosition().y);
			}
		}
		
		player.draw(g2);
		
		hud.draw(g2);
		
		for (GUI object : GUIList) {
			object.draw(g2);
		}
		
		if (keyHandler.TPressed) {
			long drawEnd = System.nanoTime();
			long timePassed = drawEnd - drawStart;
			System.out.println(timePassed);
			g2.setColor(Color.white);
			g2.drawString("Draw time: " + timePassed , camX + 20, camY + 200);
		}
		
		g2.dispose();
	}
	
	public void playMusic(String fileName) {
		sound.setFile(fileName);
		sound.play();
		sound.loop();
	}
	
	public void stopMusic(String fileName) {
		sound.stop();
	}
	
	public void playSFX(String fileName) {
		sound.setFile(fileName);
		sound.setVolume(0.2f);
		sound.play();
	}
}
