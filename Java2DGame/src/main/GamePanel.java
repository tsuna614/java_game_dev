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
import entity.Oldman;
import entity.Player;
import entity.Slime;
import objects.BootsObject;
import objects.DoorObject;
import objects.GameObject;
import objects.KeyObject;
import objects.TreeObject;
import utils.ObjectManager;
import utils.TileManager;

// implements Runnable is for the gameThread
public class GamePanel extends JPanel implements Runnable {
	
	// SCREEN SETTINGS
	final int originalTileSize = 16;
	final int scale = 3; // scale up the sprite since 16x16 is going to be very small
	
	public final int tileSize = originalTileSize * scale;
	
	public final int maxScreenCol = 20;
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
	
	KeyHandler keyHandler = new KeyHandler(this);
	Thread gameThread;
	
	// PLAYER
	public Player player = new Player(100, 100 , this, keyHandler);
	
	// DRAW TILES AND SET INVISIBLE WALLS
	TileManager tileManager = new TileManager(this);
	
	// COLLISION BLOCKS
	public ArrayList<GameObject> gameObjects = new ArrayList<>();
	
	// SOUND
	Sound sound = new Sound();
	boolean playMusic = false;
	boolean playSFX = true;
	
	// HUD
	public HUD hud = new HUD(this);
	public ArrayList<GUI> GUIList = new ArrayList<>();
	
	// GAME STATE
	public enum GameState {
		paused,
		play,
		inDialogue,
		inMenu
	}
	public GameState gameState;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		
		this.addKeyListener(keyHandler);
		this.setFocusable(true); // with this, this GamePanel can be 'focused' to receive key input
		
		this.gameState = GameState.inMenu;
		
		tileManager.loadMap("map03.txt");
		
		playMusic("BlueBoyAdventure.wav");
		
		ObjectManager.loadGamePanel(this);
		ObjectManager.addObjects();
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
	
	public boolean checkCollision(GameObject player, GameObject block) {
		final float playerX = player.getPosition().x + player.getHitbox().x;
		final float playerY = player.getPosition().y + player.getHitbox().y;
		final float playerWidth = player.getHitbox().width;
		final float playerHeight = player.getHitbox().height;
		
		final float blockX = block.getPosition().x + block.getHitbox().x;
		final float blockY = block.getPosition().y + block.getHitbox().y;
		final float blockWidth = block.getHitbox().getWidth() == 0 ? block.getWidth() : block.getHitbox().width;
		final float blockHeight = block.getHitbox().getHeight() == 0 ? block.getHeight() : block.getHitbox().height;
			
		return playerX < blockX + blockWidth &&
				playerX + playerWidth > blockX &&
				playerY < blockY + blockHeight &&
				playerY + playerHeight > blockY;
//		return playerX < block.getPosition().x + block.getWidth() &&
//				playerX + playerWidth > block.getPosition().x &&
//				playerY < block.getPosition().y + block.getHeight() &&
//				playerY + playerHeight > block.getPosition().y;
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
			
			// máy mạnh -> hàm này được thực hiện nhiều lần -> update được gọi nhiều
			// máy yếu -> (currentTime - lastTime) lớn -> delta tăng nhanh hơn mỗi lần gọi -> update được gọi nhiều
			// ==> số lần gọi update() giữa máy mạnh và máy yếu như nhau, không cần phải truyền dt vào object nữa
			
			// TUY NHIÊN, nếu muốn chính xác đến từng số thập phần (tốc độ bằng nhau giữa máy mạnh và yếu)
			// người ta sẽ truyền delta time vào object
			if (delta >= 1) {
				update();
				repaint();
				delta--;
			}
		}
	}
	
	public void update() {
		
		if (gameState != GameState.play) return;
		
		// không cần phải truyền delta time vào
		
		player.update();
		
		for (GameObject object : gameObjects) {
			if (object instanceof Oldman) {
				((Oldman) object).update();
			} else if (object instanceof Slime) {
				((Slime) object).update();
			}
		}
		
		for (GUI object : GUIList) {
			object.update();
		}
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g; // change Graphics to Graphics2D
		
		if (gameState != GameState.inMenu) {
			camX = (player.getPosition().x - screenWidth / 2);
			camY = (player.getPosition().y - screenHeight / 2);
			
			g2.translate(-camX, -camY);
			
			// drawing the blue "ocean" background
			g2.setColor(Color.decode("#0090e6"));
			g2.fillRect((int) camX, (int) camY, screenWidth, screenHeight);
			
			drawingObjects(g2);
		}
		
		hud.draw(g2);
		
		for (GUI object : GUIList) {
			object.draw(g2);
		}
		
		g2.dispose();
	}
	
	public void drawingObjects(Graphics2D g2) {

		// some objects get added in here when player's y < that object's y (meaning the player is behind the object)
		ArrayList<GameObject> priorityObjects = new ArrayList<>();
		
		for (GameObject object : gameObjects) {
			if (object instanceof KeyObject) {
				object.draw(g2);
			} else if (object instanceof DoorObject) {
				object.draw(g2);
			} else if (object instanceof BootsObject) {
				object.draw(g2);
			} else if (object instanceof TreeObject || object instanceof Oldman) {
				if (player.getPosition().y < object.getPosition().y - object.getHitbox().y) {
					priorityObjects.add(object);					
				} else {
					object.draw(g2);
				}
			} else if (object instanceof Slime) {
				if (player.getPosition().y + player.getHitbox().height< object.getPosition().y + object.getHitbox().y + object.getHitbox().height) {
					priorityObjects.add(object);					
				} else {
					object.draw(g2);
				}
			}
				else {
				tileManager.drawSingle(g2, (int) object.getPosition().x, (int) object.getPosition().y);
			}
		}
		
		player.draw(g2);
		
		for (GameObject object : priorityObjects) {
			object.draw(g2);
		}
		
		priorityObjects.clear();
	}
	
	public void playMusic(String fileName) {
		if (!playMusic) return;
		sound.setFile(fileName);
		sound.play();
		sound.loop();
	}
	
	public void stopMusic(String fileName) {
		sound.stop();
	}
	
	public void playSFX(String fileName) {
		if (!playSFX) return;
		sound.setFile(fileName);
		sound.setVolume(0.2f);
		sound.play();
	}
}
