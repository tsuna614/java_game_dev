package utils;

import java.awt.image.BufferedImage;

public class Frame {
	// the main point of this class is to asign each sprite (BufferedImage) with a duration
	
	private BufferedImage frame;
	private int duration;
	
	public Frame(BufferedImage frame, int duration) {
		this.frame = frame;
		this.duration = duration;
	}
	
	public BufferedImage getFrame() {
		return this.frame;
	}
	public void setFrame(BufferedImage frame) {
		this.frame = frame;
	}
	
	public int getDuration() {
		return this.duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
}
