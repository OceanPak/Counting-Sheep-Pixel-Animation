import java.awt.Graphics2D; // to display image
import java.awt.Image; // to display image
import java.awt.Toolkit; // to access image from directory
import java.awt.image.BufferedImage; // to display image

public class AnimatedSprites extends Sprite { // AnimatedSprites is the same as sprites - just animated :)

    public int frameNumber;
    public int frameWidth;
    public int frameHeight;
    public int frameX;
    public int totalWidth;
    public int totalHeight;
    public int totalFrames;
    private Image animimage; // holds the sprite sheet
    public int frameDelay; // the delay between each frame - used to control the FPS
    public int frameTicker; // how long it has been on each frame - the seconds of FPS

    BufferedImage tempImage; // holds a temporary image, so that it will be passed into the Sprite and drawn
    Graphics2D tempSurface; // holds a temporary screen

    public AnimatedSprites(int totalFrames, int frameWidth, int frameHeight, int totalWidth, int totalHeight, BufferedImage doubleBuffer) {
        // frameWidth and frameHeight is the size of a single frame, totalWidth and totalHeight is the size of the entire sprite sheet, doubleBuffer so to draw onto the Image
    	super(totalWidth, totalHeight, doubleBuffer); // create the Sprite Object
        this.frameWidth = frameWidth; // set all these Variables
        this.frameHeight = frameHeight;
        this.frameX = 0; // x coordinate of the current frame in the sprite sheet
        this.frameNumber = 0; // the current frame number
        this.totalFrames = totalFrames;
        this.totalWidth = totalWidth;
        this.totalHeight = totalHeight;
        this.frameDelay = 6; // since the frameDelay in the Main class is 50ms, a new frame is shown every 50ms*6 = 300ms
    }

    public void loadSpriteImage(String name) { // find the directory of the image
        animimage = Toolkit.getDefaultToolkit().getImage("src/Sprites/" + name);
        tempImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB); // creates the image, to be passed it into the parent class for drawing
        tempSurface = tempImage.createGraphics(); // create the surface, to be passed it into the parent class for drawing

    }
    
    public void updateAnimation() { // find the current frame
        if (this.getFrameTicker() >= this.getFrameDelay()) { // FrameDelay -> How long the frame needs to go. FrameTicker -> How long the frame has been
            this.setFrameNumber(this.getFrameNumber() + 1); // frame moves up
            if (this.getFrameNumber() > this.getTotalFrames() - 1) { // current frame number is more than the last frame (3)
                this.setFrameNumber(0); //Update the animation frame to 0 if the count is above the last frame in the sequence of frames
            } 
            this.setFrameTicker(0); // reset the timer for each frame
        } else {
            this.setFrameTicker(this.getFrameTicker()+1); // increase the ticker time
        }

    }

    @Override // overrides the super draw class
    public void draw(int x, int y) {
        if (tempImage == null) { // if the animation is not created, create it
            tempImage = new BufferedImage(this.getFrameWidth(), this.getFrameHeight(), BufferedImage.TYPE_INT_ARGB); 
            tempSurface = tempImage.createGraphics();
        }
        this.setFrameX(this.getFrameNumber() * this.getFrameWidth()); // calculates the current frame's x position on the sprite sheet

        //copy the frame (animimage) onto the temp image (tempSurface)
        tempSurface.drawImage(animimage, 0, 0, this.getFrameWidth() - 1, this.getFrameHeight() - 1,
                this.getFrameX(), 0, this.getFrameX() + this.getFrameWidth(), 0 + this.getFrameHeight(), Main.getInstance());
        // this.getFrameX() + this.getFrameWidth() = current frame + width/height = the edges of the frame
        
        super.setSpriteImage(tempImage); // pass the image on to the parent class and draw it
        this.spriteDoubleBufferG2D.drawImage(this.getSpriteImage(), x, y, Main.getInstance()); // draws the image onto the backbuffer
        
        tempSurface.dispose(); // destroy these so next image doesn't write over the previous one
        tempImage = null;
    }

    public int getFrameHeight() { // height of the current frame
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) { // sets the height of the current frame
        this.frameHeight = frameHeight;
    }

    public int getFrameNumber() { // return the current frame number
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) { // set the current frame number
        this.frameNumber = frameNumber;
    }

    public int getFrameWidth() { // get the width of the current frame
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) { // set the width of the current frame
        this.frameWidth = frameWidth;
    }

    public int getFrameX() { // get the x coordinate of the current frame
        return frameX;
    }

    public void setFrameX(int frameX) { // set the x coordinate of the current frame
        this.frameX = frameX;
    }

    public int getTotalFrames() { // get the total frames
        return totalFrames;
    }

    public void setTotalFrames(int totalFrames) { // set the total frames
        this.totalFrames = totalFrames;
    }

    public int getFrameDelay() { // get the frame delay
        return frameDelay;
    }

    public void setFrameDelay(int frameDelay) { // set the frame delay
        this.frameDelay = frameDelay;
    }

    public int getFrameTicker() { // get the frame ticker
        return frameTicker;
    }

    public void setFrameTicker(int frameTicker) { // set the frame ticker
        this.frameTicker = frameTicker;
    }
}
