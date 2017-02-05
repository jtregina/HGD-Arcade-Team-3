package arcadia;

import arcadia.*;
import arcadia.util.*;
import intro.IntroGame;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import dodge.DodgeGame;
import shooter.Shooter;

public class Arcadia extends JPanel implements KeyListener,Runnable {
	private BufferedImage buffer;
	private BufferedImage completeBuffer;
	private final Queue<Overlay> overlays;
	private final Set<Integer> pressed;

	final int FPS_GOAL = 30;

	//Framerate information
	private String framerate = "";
	private String updateRate = "";
	private int framesCounted = 0;
	private int updatesCounted = 0;
	private long lastFramerateUpdate = 0;
	private long lastUpdateUpdate = 0;
	private static boolean showFPS = false;	//Set to true by passing in "-debug" as a parameter
	private static boolean limitFPS = true;	//Set to false by passing in "-nolimit" as a parameter

	private Arcadia() {
		this.buffer   		= new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		this.completeBuffer 	= new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		this.overlays = Collections.asLifoQueue(new LinkedList<Overlay>());
		this.pressed  = new HashSet<Integer>();
	}

	public Arcadia(Game... games) {
		this();
		this.overlays.add(new SelectOverlay(overlays, games));
	}

	public Arcadia(Game game) {
		this();
		this.overlays.add(new GameOverlay(overlays, game));
	}

	public void paint(Graphics g) {
		long start = System.currentTimeMillis();
		framesCounted++;

		synchronized(completeBuffer) {
			((Graphics2D)g).drawImage(completeBuffer, 0, 0, Game.WIDTH, Game.HEIGHT, null); 
		}

		if (showFPS) {
			g.setColor(Color.WHITE);
			g.drawString(framerate, 10, 20);
			g.drawString(updateRate, 10, 35);
		}

		if (System.currentTimeMillis() - lastFramerateUpdate > 1000) {
			lastFramerateUpdate = System.currentTimeMillis();
			framerate = framesCounted + " fps";
			framesCounted = 0;
		}

		long total = System.currentTimeMillis() - start;
		if(limitFPS && total < 1000 / FPS_GOAL) {
			try { Thread.sleep(1000 / FPS_GOAL - total); }
			catch(InterruptedException e) { }
		}

		repaint();
	}

	public void run() {
		
		

		while (true) {
			updatesCounted++;
			long start = System.currentTimeMillis();
		
			Graphics2D g2d = buffer.createGraphics();
	
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
	
			Queue<Overlay> toUpdate = Collections.asLifoQueue(new LinkedList<Overlay>());
			for(Overlay overlay : overlays) {
				toUpdate.add(overlay);
				if(overlay.focused()) { break; }
			}
	
			Queue<Overlay> toDraw = Collections.asLifoQueue(new LinkedList<Overlay>());
			for(Overlay overlay : overlays) {
				toDraw.add(overlay);
				if(overlay.opaque()) { break; }
			}
	
			for(Overlay overlay : toUpdate) { overlay.update(pressed);  }
			for(Overlay overlay : toDraw  ) { overlay.draw(g2d); }
			
			synchronized(completeBuffer) {
				BufferedImage tmp = buffer;
				buffer = completeBuffer;
				completeBuffer = tmp;
			}
	
			long total = System.currentTimeMillis() - start;
	
			if (System.currentTimeMillis() - lastUpdateUpdate > 1000) {
				lastUpdateUpdate = System.currentTimeMillis();
				updateRate = updatesCounted + " ups";
				updatesCounted = 0;
			}
			
			if(limitFPS && total < 1000 / FPS_GOAL) {
				try { Thread.sleep(1000 / FPS_GOAL - total); }
				catch(InterruptedException e) { }
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		pressed.add(e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {
		pressed.remove(e.getKeyCode());
	}

	public void keyTyped(KeyEvent e) { }

	public static void display(Arcadia arcadia) {
		arcadia.setMinimumSize(new Dimension(Game.WIDTH, Game.HEIGHT));
		arcadia.setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));

		new Thread(arcadia).start();

		JFrame frame = new JFrame("Arcadia");
		frame.add(arcadia);
		frame.addKeyListener(arcadia);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();

		hideMouse(frame);

		frame.setVisible(true);
	}

	public static void hideMouse(JFrame frame) {
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		frame.getContentPane().setCursor(blankCursor);
	}

	public static void main(String[] args) {

		//Handle arguments
		for (String s : args) {
			//Handle all commands that begin with "-"
			if (s.startsWith("-")) {
				switch (s.substring(1).toLowerCase()) {
				case "debug":
					showFPS = true;
					break;
				case "nolimit":
					limitFPS = false;
					break;
				}
			}

			//Handle all other commands
		}

		Arcadia.display(new Arcadia(new Game[] { new IntroGame(), new Shooter(), new DodgeGame(), new IntroGame() }));
		//Arcadia.display(new Arcadia(new Game[] { new Shooter() }));
	}
}