import java.awt.Color; // color of the white dream bubbles
import java.awt.Font; // control the font size of the SheepCounter to be displayed on screen
import java.awt.Graphics; // to display image
import java.awt.Graphics2D; // to display image
import java.awt.event.MouseEvent; // to track mouse movement
import java.awt.event.MouseMotionListener; // to track mouse movement
import java.awt.image.BufferedImage; // to display image
import java.util.ArrayList; // to store the sprite objects into array, so they can be individually controlled
import javax.swing.*; // set up the window
import java.io.*; // to help with input of music
import sun.audio.*; // to help with input of music

public class Main extends JFrame implements Runnable, MouseMotionListener{ // Runnable is to create the Threads, MouseMotionListener is to track mouse movement

	private final int WIDTH = 1100; // Width of the JFrame, final so it cannot be altered
	private final int HEIGHT = 1600; // Height of the JFrame, final so it cannot be altered
	private int x = 1000; // X coordinate of the Sheep's Position 
	private int y; // Y coordinate of the Sheep's Position
	private int sheepCounter=0; // Number of Sheep that has passed
	private boolean movement = false; // Whether there was mouse movement - to create interactivity
	private long start; // store the initial System.nanoseconds - used to time how long the mouse has not moved 
	private long end; // store the final System.nanoseconds - used to time how long the mouse has not moved  
	private long elapsedTime; // store the elapsed seconds - used to time how long the mouse has not moved 
	private boolean resetPosition = true; // used to reset the position of the sheep to the end of the screen after inactivity

	Thread animationLoop; // create 1 thread - threads are used to allow multiple things (threads) to run at once

	BufferedImage doubleBuffer; // if there is only one buffered image, there is a horrible flickering of the image. 
	Graphics2D doubleBufferG2D; // as such, there are 2 buffered images, taught by Evgheni's tutorial, explained more below

	// Lists are used so that a for loop can be used to change the attributes of each object with much better precision
	// Arraylists are specifically so that objects can be appended
	ArrayList<Sprite> frontallSprites = new ArrayList<Sprite>(); // arraylist of sprite objects, to be drawn in front layer in the animation
	ArrayList<Sprite> backallSprites = new ArrayList<Sprite>(); // arraylist of sprite objects, to be drawn in the back layer in the animation
	ArrayList<AnimatedSprites> allAnimatedSprites = new ArrayList<AnimatedSprites>(); // arraylist of animatedsprites objects

	public static Main instance = null; // 1 and only 1 display surface should be generated, as such should be a singleton. 

	private Main(){
		super(); // creates an invisible JFrame, inherited from the 'extend' above
		this.setSize(this.WIDTH, this.HEIGHT); // sets size of window
		this.setVisible(true); // show the window
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // quit the window when closed
		addMouseMotionListener(this); // create a mouse motion listener to track mouse movement
		this.doubleBuffer = new BufferedImage(this.WIDTH, this.HEIGHT, BufferedImage.TYPE_INT_RGB); // create the backbuffer as a BufferedImage object, as to allow PNGs to be imported 
		this.doubleBufferG2D = (Graphics2D) doubleBuffer.createGraphics(); // create a Graphics 2D object to allow more geometric manipulation.
		// both of this creates the double layer of images
	}

	public static void main(String[] args){
		String musicFile = "src/Sprites/lullaby.wav"; // Address of the music piece
		InputStream in = null; // instantiate the input variable (need to be null first in order to prevent errors)
		try {
			in = new FileInputStream(musicFile); // input the music file into InputStream. InputStream is used to detect and access files.
		} catch (FileNotFoundException e) {
			e.printStackTrace(); // print error if file not found
		}

		AudioStream audioStream = null; // instantiate the audiostream variable (need to be null first in order to prevent errors)
		try {
			audioStream = new AudioStream(in); // convert the file InputStream into Audiostream
		} catch (IOException e) {
			e.printStackTrace(); // if there are any errors with the input
		}

		AudioPlayer.player.start(audioStream); // play the audio clip with the AudioPlayer class

		Main.getInstance().start(); // after getInstance, run the start function for initialization
	}
	
	public static Main getInstance(){
		if (instance == null){ // check whether Main has been created. It needs to be singleton only. 
			instance = new Main(); // create if it has not been generated
		}
		return instance; // if not, nothing happens
	}

	public void start() {
		animationLoop = new Thread(this); // create 1 thread - threads are used to allow multiple things (threads) to run at once
		animationLoop.start(); // start the thread
		this.initializeSprites(); // create the sprites
	}

	public void initializeSprites(){ // creating the sprites, and put them in their initial positions

		Sprite bubbleBackground = new Sprite(883, 307, instance.doubleBuffer); // Create a Sprite object from the class. 
																			   // 883 is width of the image, 307 is height of the image, as well as the doubleBuffer BufferedImage
		bubbleBackground.loadSpriteImage("DreamBackground.png"); // load the sprite image. 
																 // DreamBackground was created with the help of Paintbrush, used to just leave the borders of the dream
																 // that way, the sheep can disappear behind the layer of the border of the dream. As such, this dream border is in the frontSprite Arraylist.
		bubbleBackground.setSpriteXPosition(-34); // set its x position on the window
		bubbleBackground.setSpriteYPosition(33); // set its y position on the window
		instance.addfrontSprite(bubbleBackground); // add it into the front Arraylist

		Sprite bubble = new Sprite(855, 280, instance.doubleBuffer); // same as above
		bubble.loadSpriteImage("JustDreams.png"); // JustDreams is the starry background, placed in a layer behind the sheep
		bubble.setSpriteXPosition(151);
		bubble.setSpriteYPosition(76);
		instance.addbackSprite(bubble); // add it to the back ArrayList

		Sprite fence = new Sprite(256, 256, instance.doubleBuffer); // same as above
		fence.loadSpriteImage("Fence.png");
		fence.setSpriteXPosition(510);
		fence.setSpriteYPosition(220);
		instance.addbackSprite(fence); // Add it to the back Arraylist

		Sprite z1 = new Sprite(128, 128, instance.doubleBuffer); // same as above
		z1.loadSpriteImage("Z.png"); // 4 Sleeping Zs are created. The code are just copied and pasted. 
		z1.setSpriteXPosition(300); // But they have different initial positions
		z1.setSpriteYPosition(400);
		instance.addfrontSprite(z1);

		Sprite z2 = new Sprite(128, 128, instance.doubleBuffer); // same as above
		z2.loadSpriteImage("Z.png");
		z2.setSpriteXPosition(200);
		z2.setSpriteYPosition(350);
		instance.addfrontSprite(z2);

		Sprite z3 = new Sprite(128, 128, instance.doubleBuffer); // same as above
		z3.loadSpriteImage("Z.png");
		z3.setSpriteXPosition(100);
		z3.setSpriteYPosition(300);
		instance.addfrontSprite(z3);

		Sprite z4 = new Sprite(128, 128, instance.doubleBuffer); // same as above
		z4.loadSpriteImage("Z.png");
		z4.setSpriteXPosition(0);
		z4.setSpriteYPosition(250);
		instance.addfrontSprite(z4);

		AnimatedSprites sheepAnimated = new AnimatedSprites(4,192,192,768,192,instance.doubleBuffer); // Create an AnimatedSprites object from the class.
		// 4 is the number of frames, 192 and 192 is the size of each frame, 768 and 192 is the size of the sprite sheet
		sheepAnimated.loadSpriteImage("SheepPiskel Walk.png"); // load the image
		instance.addAnimatedSprite(sheepAnimated); // add into the AnimatedSprites list
		sheepAnimated.setSpriteXPosition(160); // set its position
		sheepAnimated.setSpriteYPosition(230);

		AnimatedSprites bedManAnimated = new AnimatedSprites(8,320,320,2560,320,instance.doubleBuffer); // same as above
		bedManAnimated.loadSpriteImage("SleepingMan.png");
		instance.addAnimatedSprite(bedManAnimated);
		bedManAnimated.setSpriteXPosition(350);
		bedManAnimated.setSpriteYPosition(350);

	}

	public void paint(Graphics g){
		
		Graphics2D g2d = (Graphics2D) g; // override the original Graphics paint, to allow for more sophisticated drawing

		// Uncomment the code below for weird dream effect!
		//g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
		try { // drawing the backbuffer into the screen - screen is the frontbuffer
			instance.paintToBackbuffer(); //Draw whatever we need to onto the backbuffer first
			g2d.drawImage(doubleBuffer, null , 0, 0); // draw the backbuffer into the front layer
		} catch (Exception ex){
			// ex.printStackTrace(); - errors if any happen, commented out so that no errors appear on the console
		}    
	}
	
	private void paintToBackbuffer(){ // paint to the backbuffer first before painting to screen, in order to prevent the horrible flicker of the images
		Color col = new Color(0,0,51); // set new colour on RGB scale
		instance.doubleBufferG2D.setColor(col); // set the background color
		instance.doubleBufferG2D.fillRect(0, 0, instance.WIDTH, instance.HEIGHT); // draw it onto the background

		instance.doubleBufferG2D.setColor(Color.WHITE); // Dream Bubble is white in colour
		instance.doubleBufferG2D.fillOval(550, 415, 20, 20); // 550 is x, 415 is y, 20 and 20 is the size of the bubble

		instance.doubleBufferG2D.setColor(Color.WHITE); // Dream Bubble, just bigger and in another position
		instance.doubleBufferG2D.fillOval(600, 380, 40, 40);

		instance.doubleBufferG2D.setFont(new Font("TimesRoman", Font.PLAIN, 36)); // set the font size of the Sheep Counter text
		instance.doubleBufferG2D.drawString("Sheep Count: " + sheepCounter, 800, 600); // draw how many sheep has gone through the screen once, 800 and 600 is the x and y coordinates
		
		for (Sprite current : backallSprites) { // draw these images first, so they are in the back layer
			current.draw((int)current.getSpriteXPosition(), (int)current.getSpriteYPosition()); // get their X and Y positions, and draw it accordingly
		}

		this.drawAllSprites(); // now draw the other sprites
	}

	public void drawAllSprites(){ // draw all the sprites from the three lists

		if (x<=-20) { // X and Y variables are for the sheep, due to its parabolic movement
			x=1000; // if it runs off the screen, push it to the other side of the screen. That way, only one sheep object is created
			sheepCounter++; // count the number of sheep
		}

		x = x - 5; // move the sheep 5 pixels to the left
		y = (int) (((double)75/(double)32400)*(x-710)*(x-350))+100; // get the y position based on the quadratic equation
																	// since a jump is a parabolic motion

		if (x>=800 || x<=260) { // if it is not within the jumping range, it should be walking in a straight line (at 200)
			y = 200;
		}


		for (AnimatedSprites current : allAnimatedSprites){ // for all the objects in the arraylist
			current.updateAnimation(); // find out which frame the AnimatedSprite should be
			if (resetPosition==false) { // if mousemovement is detected, resetPosition would be false. As such, put it back to initial position at the right of the screen.
				x=1250;
				y=200;
				resetPosition = true; // make resetPosition back to true, refer below for more details.
			}
			if (current==allAnimatedSprites.get(0)) { // the first object - get(0) - is the sheep
				if (timeElapsed()) { // check how long mouse has moved, if it has moved
					end = (int)(System.nanoTime()/1000000000.0); // if it is, check the current time and compare with the starting time
					current.loadSpriteImage("BlankSpace.png"); // load a blank sprite to make the sheep "invisible" when mouse is moved
				}
				else if (x<800 && x>255) { // if mousemovement is not detected, if it is within the jumping range
					current.loadSpriteImage("SheepPiskel Jump.png"); // run the jumping animation
				}
				else { // if mousemovement is not detected, if it is outside of jumping range
					current.loadSpriteImage("SheepPiskel Walk.png"); // run the walking animation
				}
				current.draw(x, y); // update its position on the screen
			}
			else if (current==allAnimatedSprites.get(1)) { // the second object - get(1) - is the human
				if (movement) { // if mousemovement is detected
					current.loadSpriteImage("SleepingMan Awake.png"); // run the awake animation
				}
				else {
					current.loadSpriteImage("SleepingMan.png"); // run the asleep animation
				}
				current.draw((int)current.getSpriteXPosition(), (int)current.getSpriteYPosition()); // since the human is stationary, just get its X and Y positions from the object itself from get/set functions
			}
		}

		for (Sprite current : frontallSprites) { // now run the front layer no-frame sprites
			if (current==frontallSprites.get(1) || current==frontallSprites.get(2) || current==frontallSprites.get(3) || current==frontallSprites.get(4)) { // index 1-4 in the list are the Zs
				if (movement) { // if there is movement
					current.loadSpriteImage("BlankSpace.png"); // make the Zs disappear
				}
				else {
					current.loadSpriteImage("Z.png"); // else, display the Z sprite image
				}
				current.setSpriteXPosition(current.getSpriteXPosition()-1); // update its position, get its current X position, and minus 1 to have it float left
				current.setSpriteYPosition(current.getSpriteYPosition()-0.3); // update its position, get its current Y position, and minus 0.3 to have it float up slowly
				if (current.getSpriteXPosition()<-100) { // once again, if it floats out of the screen, move it back closer to the human
					current.setSpriteXPosition(300); // that way, an endless chain of Zs can be created, when in reality its just reusing 4 objects
					current.setSpriteYPosition(400);
				}
				current.draw((int)current.getSpriteXPosition(), (int)current.getSpriteYPosition()); // update its position on the screen
			}
			else {
				current.draw((int)current.getSpriteXPosition(), (int)current.getSpriteYPosition()); // if it is not the Zs, just update on its positions
			}
		}

	}
	
	public void run() {
		Thread current = Thread.currentThread(); // another thread to allow animation to show
		while (current == animationLoop) { // if both threads are running
			try {
				Thread.sleep(50); // sleep this thread, so that processes can be run, such as updating position data
			} catch (InterruptedException e) {
				e.printStackTrace(); // else, display error
			}
			repaint();  // redraw, refer to the paint function
		}
	}

	public void addAnimatedSprite(AnimatedSprites animatedSpriteToAdd){ // add the AnimatedSprite into the list
		instance.allAnimatedSprites.add(animatedSpriteToAdd);
	}

	public void addfrontSprite(Sprite spriteToAdd){ // add the Sprite for the front layer into the list
		instance.frontallSprites.add(spriteToAdd);
	}

	public void addbackSprite(Sprite spriteToAdd){ // add the Sprite for the back layer into the list
		instance.backallSprites.add(spriteToAdd);
	}

	public boolean timeElapsed() { // check to see how long the mouse has not moved, if it has moved
		elapsedTime = end - start; // find the elapsed seconds
		// Since the time uses nanoseconds (which is how long the computer has processed), there needs to be a conversion. 
		// In this case, it is dividing it by 1000000000.0, and turning it into an int.
		// However, if the mouse hasn't moved since the start of the program, the elapsedTime becomes a very large number, and be > than 3.
		// As such, the sheep won't even appear in the first place, when the mouse hasn't moved.
		// Therefore, the function below is <10000, so that a sheep will appear when the program first runs 
		if (elapsedTime>=3 && elapsedTime<10000) { // if its more than 3 seconds
			movement = false; // mousemovement is now false
			end = start; // make the difference in time 0
			return movement; // return false, and make the sheep appear again
		}
		return movement; // return true, and continue to hide the sheep
	}

	@Override
	public void mouseDragged(MouseEvent e) { // a function belonging to MouseAction, that has to be generated. Serves no purpose in this program, due to not dragged mouse events. 
		e.consume(); // consume so that the event doesn't eat up RAM after its done
	}

	@Override
	public void mouseMoved(MouseEvent e) { // a function belonging to MouseAction
		sheepCounter = 0;
		movement = true; // mouseMovement is now true
		resetPosition = false; // need to resetPosition of the sheep
		start = (int)(System.nanoTime()/1000000000.0); // store the starting seconds
		e.consume(); // consume so that the event doesn't eat up RAM after its done
	}
}
