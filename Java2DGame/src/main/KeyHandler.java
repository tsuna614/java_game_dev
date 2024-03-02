package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	public boolean upPressed, downPressed, leftPressed, rightPressed, TPressed;

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int keyCode = e.getKeyCode();
		
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
	}

}
