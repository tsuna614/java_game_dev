package utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animation {

	private int frameCount;                 // Counts ticks for change
    private int frameDelay;                 // frame delay 1-12 (You will have to play around with this)
    private int currentFrame;               // animations current frame
    private int animationDirection;         // animation direction (i.e counting forward or backward)
    private int totalFrames;                // total amount of frames for your animation
    
    private boolean isStopped;
    public boolean isLoop = true;
    
    private Frame[] frames;
//    private static ArrayList<Frame> frames = new ArrayList<>();
    
    public Animation(BufferedImage[] sprites, int delayDuration) {
    	frames = new Frame[sprites.length];
    	
    	frameCount = 0;
    	frameDelay = delayDuration;
    	currentFrame = 0;
    	animationDirection = 1;
    	totalFrames = sprites.length;
    	for (int i=0; i<sprites.length; i++) {
//    		addFrame(sprites[i], delayDuration);
    		frames[i] = new Frame(sprites[i], delayDuration);
    	}
    	isStopped = false;
    }
    
    
//    private static void addFrame(BufferedImage frame, int duration) {
//    	frames.add(new Frame(frame, duration));
//    }
    
    public void start() {
    	if (frames.length == 0) {
    		return;
    	}
    	
    	isStopped = false;
    }
    
    public void stop() {
    	if (frames.length == 0) {
    		return;
    	}
    	
    	isStopped = true;
    }
    
    public void reset() {
    	if (frames.length == 0) {
    		return;
    	}
    	
    	isStopped = false;
    	currentFrame = 0;
    }
    
    // call this in draw method (g2.drawImage) to draw the current frame
    public BufferedImage getCurrentFrame() {
    	return frames[currentFrame].getFrame();
    }
    
    // this must be called in Object's update method
    public void update() {
    	if (!isStopped) {
    		frameCount++;
    		if (frameCount > frameDelay) {
    			frameCount = 0;
    			
    			// doesn't work when the animation is reverted yet
    			if (isLoop || (currentFrame < totalFrames - 1 && currentFrame >= 0))
    			{
    				currentFrame += animationDirection;    				
    			}
    			
    			if (currentFrame > totalFrames - 1) {
    				currentFrame = 0;
    			} else if (currentFrame < 0) {
    				currentFrame = totalFrames - 1;
    			}
    		}
    	}
    }
}
