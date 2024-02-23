package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

// implements Runnable is for the gameThread
public class GamePanel extends JPanel implements Runnable {
	final int originalTileSize = 16;
	final int scale = 3; // scale up the sprite or else 16x16 is going to be very small
	
	final int tileSize = originalTileSize * scale;
	
	final int maxScreenCol = 16;
	final int maxScreenRow = 12;
	final int screenWidth = maxScreenCol * tileSize; // 768 pixels
	final int screenHeight = maxScreenRow * tileSize; // 576 pixels
	
	KeyHandler keyHandler = new KeyHandler();
	Thread gameThread;
	
	// set player's default position and speed
	int playerX = 100;
	int playerY = 100;
	int playerSpeed = 4;
	
	int FPS = 60;
	
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

	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS; // 0.01666 seconds
		double nextDrawTime = System.nanoTime() + drawInterval;
		
		
		while (gameThread != null) {
			// System.out.println("The game loop is running");
			
			// 1. UPDATE: update informations such as player's position
			update();
			
			// 2. DRAW: re-draw the screen with the updated information
			repaint(); // this somehow calls to the paintComponent method
			
			
			try {
				double remainingTime = nextDrawTime - System.nanoTime();
				remainingTime /= 1000000; // change from nanosecond to millisecond
				
				if (remainingTime < 0) {
					remainingTime = 0;
				}
				
				Thread.sleep((long) remainingTime);
				
				nextDrawTime += drawInterval;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void update() {
		if (keyHandler.upPressed) {
			playerY -= playerSpeed;
		}
		if (keyHandler.downPressed) {
			playerY += playerSpeed;
		}
		if (keyHandler.leftPressed) {
			playerX -= playerSpeed;
		}
		if (keyHandler.rightPressed) {
			playerX += playerSpeed;
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g; // change Graphics to Graphics2D
		
		g2.setColor(Color.white);
		
		g2.fillRect(playerX, playerY, tileSize, tileSize);
		
		g2.dispose();
	}
}
