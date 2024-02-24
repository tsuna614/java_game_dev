package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
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
	
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		
		this.addKeyListener(keyHandler);
		this.setFocusable(true); // with this, this GamePanel can be 'focused' to receive key input
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
		
		tileManager.draw(g2);
		
		player.draw(g2);
		
		
		g2.dispose();
	}
}
