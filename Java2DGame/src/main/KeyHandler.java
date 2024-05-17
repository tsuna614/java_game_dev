package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;

import main.GamePanel.GameState;
import objects.Bullet;

public class KeyHandler implements KeyListener {
	public boolean upPressed, downPressed, leftPressed, rightPressed, TPressed, spacePressed, JPressed = false;
	GamePanel gp;
	
	boolean isShowingDialogue;
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		int keyCode = e.getKeyCode();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int keyCode = e.getKeyCode();
		
		if (gp.gameState == GameState.inMenu) {
			
			if (keyCode == KeyEvent.VK_ENTER) {
				gp.gameState = GameState.play;
			}
			
		} else {
			
			if (keyCode == KeyEvent.VK_W) {
				upPressed = true;
			}
			if (keyCode == KeyEvent.VK_S) {
				downPressed = true;
			}
			if (keyCode == KeyEvent.VK_A) {
				leftPressed = true;
			}
			if (keyCode == KeyEvent.VK_D) {
				rightPressed = true;
			}
			if (keyCode == KeyEvent.VK_T) {
				TPressed = !TPressed;
			}
			if (keyCode == KeyEvent.VK_J) {
				JPressed = true;
				gp.tank.shoot();
			}
			
			if (keyCode == KeyEvent.VK_P) {
				if (gp.gameState == GameState.play) {
					gp.gameState = GameState.paused;
				} else if (gp.gameState == GameState.paused) {
					gp.gameState = GameState.play;		
				}
			}
			
			if (keyCode == KeyEvent.VK_SPACE) {
				spacePressed = true;
				
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int keyCode = e.getKeyCode();
		
		if (keyCode == KeyEvent.VK_W) {
			upPressed = false;
		}
		if (keyCode == KeyEvent.VK_S) {
			downPressed = false;
		}
		if (keyCode == KeyEvent.VK_A) {
			leftPressed = false;
		}
		if (keyCode == KeyEvent.VK_D) {
			rightPressed = false;
		}
		if (keyCode == KeyEvent.VK_J) {
			JPressed = false;
		}
		
		if (keyCode == KeyEvent.VK_SPACE) {
			spacePressed = false;
			if (gp.gameState == GameState.inDialogue) {
				if (isShowingDialogue) {
					gp.gameState = GameState.play;		
					isShowingDialogue = false;
				} else {
					isShowingDialogue = true;
				}
			}
		}
	}

}
