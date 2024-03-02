package utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {

	private int frameCount;                 // Counts ticks for change
    private int frameDelay;                 // frame delay 1-12 (You will have to play around with this)
    private int currentFrame;               // animations current frame
    private int animationDirection;         // animation direction (i.e counting forward or backward)
    private int totalFrames;                // total amount of frames for your animation
    
    private boolean isStopped;
    private boolean isLoop = true;
    
    private static ArrayList<Frame> frames = new ArrayList<>();
    
    public Animation(BufferedImage[] sprites, int delayDuration) {
    	frameCount = 0;
    	frameDelay = delayDuration;
    	currentFrame = 0;
    	animationDirection = 1;
    	totalFrames = sprites.length;
    	
    	for (int i=0; i<sprites.length; i++) {
    		addFrame(sprites[i], delayDuration);
    	}
    	isStopped = false;
    }
    
    
    private static void addFrame(BufferedImage frame, int duration) {
    	frames.add(new Frame(frame, duration));
    }
    
    public void start() {
    	if (frames.size() == 0) {
    		return;
    	}
    	
    	isStopped = false;
    }
    
    public void stop() {
    	if (frames.size() == 0) {
    		return;
    	}
    	
    	isStopped = true;
    }
    
    public void reset() {
    	if (frames.size() == 0) {
    		return;
    	}
    	
    	isStopped = false;
    	currentFrame = 0;
    }
    
    public BufferedImage getCurrentFrame() {
    	return frames.get(currentFrame).getFrame();
    }
    
    public void update() {
    	if (!isStopped) {
    		frameCount++;
    		if (frameCount > frameDelay) {
    			frameCount = 0;
    			currentFrame += animationDirection;
    					
    			if (currentFrame > totalFrames - 1) {
    				currentFrame = 0;
    			} else if (currentFrame < 0) {
    				currentFrame = totalFrames - 1;
    			}
    		}
    	}
    }
}
