import java.awt.Graphics2D; // to display image
import java.awt.Image; // to display image
import java.awt.Toolkit; // to access image from directory
import java.awt.image.BufferedImage; // to display image
 
public class Sprite {
 
    private Image spriteImage; // image of sprite
    private double spriteXPosition;
    private double spriteYPosition;
   
    BufferedImage spriteDoubleBuffer; // The double buffer which the Sprite needs to draw into (draw onto the image first)
    Graphics2D spriteDoubleBufferG2D; // Draw Graphics2D into BufferedImage (then into the screen)
 
    public Sprite(int width, int height, BufferedImage doubleBuffer){ // Sprite Object
        this.spriteXPosition = 0; // default variables
        this.spriteYPosition = 0; // default variables
        this.spriteDoubleBufferG2D = (Graphics2D) doubleBuffer.getGraphics(); // create the Graphics2D (screen)
    }
 
    public void loadSpriteImage(String name) { // find the directory of the image
       this.setSpriteImage(Toolkit.getDefaultToolkit().getImage("src/Sprites/"+name));
 
    }
 
    public void draw(int x, int y){ // update position of the Image
        this.spriteDoubleBufferG2D.drawImage(this.getSpriteImage(), x, y, Main.getInstance()); // Backbuffer as ImageObserver
    }
 
    public Image getSpriteImage() { // return Image
        return spriteImage;
    }
 
    public void setSpriteImage(Image spriteImage) { // sets the Image
        this.spriteImage = spriteImage;
    }
 
    public double getSpriteXPosition() { // returns the Sprites X position, so that the attributes of each object can be accessed in the Main.java
        return spriteXPosition;
    }
 
    public void setSpriteXPosition(double spriteXPosition) { // returns the Sprites X position, so that the attributes of each object can be controlled in the Main.java
        this.spriteXPosition = spriteXPosition;
    }
 
    public double getSpriteYPosition() { // returns the Sprites X position, so that the attributes of each object can be accessed in the Main.java
        return spriteYPosition;
    }
 
    public void setSpriteYPosition(double spriteYPosition) { // returns the Sprites X position, so that the attributes of each object can be controlled in the Main.java
        this.spriteYPosition = spriteYPosition;
    }	
}
