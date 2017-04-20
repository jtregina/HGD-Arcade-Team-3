import arcadia.*;
import arcadia.Button;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public class BECMain extends Game {

	private Image bg;
	private Image nukeDukem;
	private Parser p;
	private BufferedImage arrow;
	private Nuke nuke;
	private Nuke nextNuke;
	private Bubble [] bubbles;
	private int numBubbles;
	private int originalNumBubbles;
	private int remainingBubbles;
	private int nDX, nDY, nX, nY, aX, aY;
	private boolean fire;
	private boolean storySegment;
	private boolean tutorial;
	private int levelCounter = 1;
	private String levelName;
	private int [] times = { 80, 30, 120, 120, 180, 180, 240, 240, 360, 360, 360 }; 
	
	private Timer timer = new Timer();
	private int time;
	private int delay = 1000;
	private int period = 1000;
	
	private double ang = 90;
	private double arrowAng = 90;
	private double locationX = 24;
	private double locationY = 30;
	private double rotationRequired = Math.toRadians(arrowAng - 90);
	private AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
	private AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

	public BECMain() {
		try {
			bg = ImageIO.read(new File("src/images/storysegments/level1.png"));
			nukeDukem = ImageIO.read(new File("src/images/NukeDukemSprite.png"));
			arrow = ImageIO.read(new File("src/images/arrow.png"));
		} catch (IOException e) {
			System.out.println("Image error");
		}
		
		p = new Parser();
	
		levelName = "level" + levelCounter;
		
		int [] level = p.getLevel(levelName);
		
		storySegment = false;
		tutorial = true;
		
		generateLevel(level, level.length);
		
		setTimer();

		reset();
	}
	
	public void setTimer() {
		timer.cancel();
		
		timer = new Timer();
		time = times[levelCounter - 1];
		
		//	Set timer			
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				if (time == 0) {
					timer.cancel();
				} else {
					time--;
				}
			}
			
		}, delay, period);
	}

	public void reset() {
		nDX = 240;
		nDY = 20;
		nX = nDX;
		nY = nDY;
		aX = nDX;
		aY = nDY + 175;
		ang = 90;
		arrowAng = 90;

		randNuke();

		setBubPositions();
	}

	public Image banner() { 
		return null;
	}

	public static void main(String[] args) {
		Arcadia.display(new Arcadia(new BECMain()));
	}

	@Override
	public Image cover() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void generateLevel(int [] level, int n) {
		
		numBubbles = n;
		originalNumBubbles = n;
		remainingBubbles = n;
		bubbles = new Bubble [numBubbles];
		
		int type;
		int temp = 10;
		
		for (int i = 0; i < numBubbles; i++) {
			type = level[i];
			
			System.out.println(i);
			
			bubbles[i] = new Bubble();
			bubbles[i].setType(type);
			
			//	Check adjacent bubbles and create a chain
			BubbleChain newChain = new BubbleChain(1, bubbles[i]);
			
			//	Check left
			if (i == 0 || i % 19 == 0) {
				System.out.println("Case 1: Don't check left because i == 0 or divisible by 19");
			} else if (i % temp == 0 && i % 20 != 0) {
				temp += 19;
				System.out.println("Case 2: Don't check left because first of a row of 9");
			} else {
				if (bubbles[i - 1].getType() == bubbles[i].getType()){
					if (bubbles[i - 1].getBubbleChain() == null) {
						newChain.addBubble(bubbles[i - 1]);
						System.out.println("Added left bubble with no chain.");
					} else {
						BubbleChain leftChain = bubbles[i - 1].getBubbleChain();
						
						for (int j = 0; j < leftChain.getLength(); j++) {
							newChain.addBubble(leftChain.getBubble(j));
						}
						System.out.println("Added left bubble with chain.");
					}
				}
			}
			
			// Check bottom left
			if (i <= 9) {
				System.out.println("Case 1: Don't check bottom left because i <= 9");
			} else if (i % 19 == 0) {
				System.out.println("Case 2: Don't check bottom left because i is divisible by 19");
			} else {
				boolean b = false;
				if (bubbles[i - 10].getType() == bubbles[i].getType()) {
					for (int j = 0; j < newChain.getLength(); j++) {
						if (newChain.getBubble(j).equals(bubbles[i - 10])) {
							b = true;
						}
					}
					
					if (!b) {
						if (bubbles[i - 10].getBubbleChain() == null) {
							newChain.addBubble(bubbles[i - 10]);
							System.out.println("Added bottom left bubble with no chain.");
						} else {
							BubbleChain bottomLeftChain = bubbles[i - 10].getBubbleChain();
							
							for (int j = 0; j < bottomLeftChain.getLength(); j++) {
								newChain.addBubble(bottomLeftChain.getBubble(j));
							}
							
							System.out.println("Added bottom left bubble with chain.");
						}
					}
				}
			}
			
			// Check bottom right
			if (i <= 9) {
				System.out.println("Case 1: Don't check bottom right because i <= 9");
			} else if ((i - 9) % 19 == 0){
				System.out.println("Case 2: Don't check bottom right because last in a row of 10");
			} else {
				boolean b = false;
				if (bubbles[i - 9].getType() == bubbles[i].getType()) {
					for (int j = 0; j < newChain.getLength(); j++) {
						if (newChain.getBubble(j).equals(bubbles[i - 9])) {
							b = true;
						}
					}
					
					if (!b) {
						if (bubbles[i - 9].getBubbleChain() == null) {
							newChain.addBubble(bubbles[i - 9]);
							System.out.println("Added bottom right bubble with no chain.");
						} else {
							BubbleChain bottomRightChain = bubbles[i - 9].getBubbleChain();
							
							for (int j = 0; j < bottomRightChain.getLength(); j++) {
								newChain.addBubble(bottomRightChain.getBubble(j));
							}
							
							System.out.println("Added bottom right bubble with chain.");
						}
					}
				}
			}
			
			for (int j = 0; j < newChain.getLength(); j++) {
				System.out.println(newChain.getBubble(j));
			}
			
			for (int j = 0; j < newChain.getLength(); j++) {
				newChain.getBubble(j).setBubbleChain(newChain);
			}
			
			switch (type) {
			case 1:
				try {
					bubbles[i].setImage(ImageIO.read(new File("src/images/bubbles/bubbleDiamond.png")));
				} catch (IOException e) {
					System.out.println("Bubble Image Error");
				}
				break;
			case 2:
				try {
					bubbles[i].setImage(ImageIO.read(new File("src/images/bubbles/bubbleLightning.png")));
				} catch (IOException e) {
					System.out.println("Bubble Image Error");
				}
				break;
			case 3:
				try {
					bubbles[i].setImage(ImageIO.read(new File("src/images/bubbles/bubblePentagon.png")));
				} catch (IOException e) {
					System.out.println("Bubble Image Error");
				}
				break;
			case 4:
				try {
					bubbles[i].setImage(ImageIO.read(new File("src/images/bubbles/bubbleSquare.png")));
				} catch (IOException e) {
					System.out.println("Bubble Image Error");
				}
				break;
			case 5:
				try {
					bubbles[i].setImage(ImageIO.read(new File("src/images/bubbles/bubbleTriangle.png")));
				} catch (IOException e) {
					System.out.println("Bubble Image Error");
				}
				break;
			case 6:
				bubbles[i].setType(2);
				try {
					bubbles[i].setImage(ImageIO.read(new File("src/images/bubbles/bubbleLightning.png")));
				} catch (IOException e) {
					System.out.println("Bubble Image Error");
				}
				break;
			case 7:
				bubbles[i].setType(0);
			default:
				try {
					bubbles[i].setType(1);
					bubbles[i].setImage(ImageIO.read(new File("src/images/bubbles/bubbleDiamond.png")));	// Diamond is the default
				} catch (IOException e) {
					System.out.println("Bubble Image Error");
				}
				break;
			}
		}
	}

	public void setBubPositions() {

		int bX = -5;
		int bY = 735;
		int temp = 10;

		for (int i = 1; i <= numBubbles; i++) {
			if (bubbles[i - 1].getType() != 0) {
				bubbles[i - 1].setPos(bX, bY);
			}
			
			bX += 60;

			if (i % 19 == 0) {
				bY -= 52;
				bX = -5;
			} else if (i % temp == 0 && i % 20 != 0) {
				bY -= 52;
				bX = 25;
				temp += 19;
			}
		}
	}

	public void randNuke() {
		int rand = 0;
		
		if (remainingBubbles < 15) {
			rand = (int)(Math.random() * 10) + 1;	// Generates random number between 1 and 10
			
			switch(rand) {
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
					break;
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
					for (int i = 0; i < numBubbles; i++) {
						if (bubbles[i].getType() != 0) {
							rand = bubbles[i].getType();
							break;
						}
					}
			}
		} else {
			rand = (int)(Math.random() * 5) + 1;	// Generates random number between 1 and 5
		}
		
		nextNuke = new Nuke();
		nextNuke.setType(rand);
		
		switch (rand) {
		case 1:
			try {
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeDiamond.png")), ImageIO.read(new File("src/images/bubbles/bubbleDiamond.png")));
			} catch (IOException e) {
				System.out.println("Nuke Image Error");
			}
			break;
		case 2:
			try {
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeLightning.png")), ImageIO.read(new File("src/images/bubbles/bubbleLightning.png")));
			} catch (IOException e) {
				System.out.println("Nuke Image Error");
			}
			break;
		case 3:
			try {
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukePentagon.png")), ImageIO.read(new File("src/images/bubbles/bubblePentagon.png")));
			} catch (IOException e) {
				System.out.println("Nuke Image Error");
			}
			break;
		case 4:
			try {
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeSquare.png")), ImageIO.read(new File("src/images/bubbles/bubbleSquare.png")));
			} catch (IOException e) {
				System.out.println("Nuke Image Error");
			}
			break;
		case 5:
			try {
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeTriangle.png")),ImageIO.read(new File("src/images/bubbles/bubbleTriangle.png")));
			} catch (IOException e) {
				System.out.println("Nuke Image Error");
			}
			break;
		default:
			try {
				nextNuke.setType(1);
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeDiamond.png")), ImageIO.read(new File("src/images/bubbles/bubbleDiamond.png")));	// Diamond is the default
			} catch (IOException e) {
				System.out.println("Nuke Image Error");
			}
			break;
		}
	}

	@Override
	public void tick(Graphics2D graphics, Input input, Sound sound) {
		
		String s;
		
		if (storySegment == true) {
			if (levelCounter == 12) {
				try {
					bg = ImageIO.read(new File("src/images/backgrounds/Epilogue.png"));
				} catch (IOException e) {
					System.out.println("Background Image Error");
				}
				
				// Draw the background
				graphics.drawImage(bg, 0, 0, WIDTH, HEIGHT, null);
				
				graphics.setColor(Color.WHITE);
				graphics.fillRoundRect(150, 705, 300, 50, 45, 60);
				graphics.setColor(Color.BLACK);
				graphics.setFont(new Font("Helvetica", Font.PLAIN, 30));
				
				graphics.drawString("Thanks for playing.", 175, 740);
			} else {
				s = "src/images/storysegments/level" + (levelCounter - 1) + ".png";
				
				try {
					bg = ImageIO.read(new File(s));
				} catch (IOException e) {
					System.out.println("Background Image Error");
				}
				
				// Draw the background
				graphics.drawImage(bg, 0, 0, WIDTH, HEIGHT, null);
				
				graphics.setColor(Color.WHITE);
				graphics.fillRoundRect(150, 705, 300, 50, 45, 60);
				graphics.setColor(Color.BLACK);
				graphics.setFont(new Font("Helvetica", Font.PLAIN, 30));
				
				graphics.drawString("Press S to continue.", 165, 740);

				if (input.pressed(Button.D)) {	
					setTimer();
					storySegment = false;
				}
			}
		} else {
			if (levelCounter == 1) {
				s = "src/images/backgrounds/level" + levelCounter + ".png";
			} else {
				s = "src/images/backgrounds/level" + (levelCounter - 1) + ".png";
			}

			
			// Draw the background
			graphics.drawImage(bg, 0, 0, WIDTH, HEIGHT, null);
			
			//	Draw timer
			graphics.setColor(Color.WHITE);
			graphics.fillRoundRect(435, 10, 160, 50, 45, 60);
			graphics.setColor(Color.BLACK);
			graphics.setFont(new Font("Helvetica", Font.PLAIN, 30)); 
			graphics.drawString("Timer: " + time, 445, 45);
			
			//	Draw loss line
			graphics.setColor(Color.WHITE);
			graphics.drawLine(0, 250, 600, 250);
			
			try {
				bg = ImageIO.read(new File(s));
			} catch (IOException e) {
				System.out.println("Background Image Error");
			}

			// Display next Nuke
			graphics.setColor(Color.BLACK);
			graphics.fillRoundRect(10, 10, 80, 106, 20, 30);
			
			if (levelCounter == 1) {
				try {
					nextNuke.setType(2);
					nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeLightning.png")), ImageIO.read(new File("src/images/bubbles/bubbleLightning.png")));
				} catch (IOException e) {
					System.out.println("Nuke Image Error");
				}
				graphics.drawImage(nextNuke.getImage(), 15, 10, 70, 96, null);
				
				if (tutorial) {
					graphics.setColor(Color.WHITE);
					graphics.fillRoundRect(0, 305, 600, 100, 45, 60);
					graphics.setColor(Color.BLACK);
					graphics.setFont(new Font("Helvetica", Font.PLAIN, 30));
					
					if (time > 70) {
						graphics.drawString("Press A to move left, D to move right.", 55, 350);
						graphics.drawString("Press 2 to aim left, 3 to aim right.", 75, 385);
					} else if (time > 60) {
						graphics.drawString("Press 1 to fire a nuke. Nukes turn into", 50, 350);
						graphics.drawString("bubbles when they collide with a bubble.", 35, 385);
					} else if (time > 50) {
						graphics.drawString("Clear bubbles by forming chains of", 60, 350);
						graphics.drawString("bubbles greater than 2.", 145, 385);
					} else if (time > 40) {
						graphics.drawString("If any bubble passes the white line", 65, 350);
						graphics.drawString("you will have to start over.", 125, 385);
					} else if (time > 30) {
						graphics.drawString("The next nuke to be fired is displayed in the", 15, 350);
						graphics.drawString("top left. Only one nuke can be fired at a time.", 10, 385);
					} else if (time > 20) {
						graphics.drawString("Complete the level before the time runs out!", 15, 350);
						graphics.drawString("If the time runs out you will have to start over.", 5, 385);
					} else if (time > 10){
						graphics.drawString("The tutorial is over. When this disappears,", 20, 350);
						graphics.drawString("quickly fire a bubble to beat this level.", 50, 385);
					} else if (time == 10) {
						tutorial = false;
					}
				}
			} else if (levelCounter == 2) {
				try {
					nextNuke.setType(2);
					nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeLightning.png")), ImageIO.read(new File("src/images/bubbles/bubbleLightning.png")));
				} catch (IOException e) {
					System.out.println("Nuke Image Error");
				}
				graphics.drawImage(nextNuke.getImage(), 15, 10, 70, 96, null);
			} else {
				graphics.drawImage(nextNuke.getImage(), 15, 10, 70, 96, null);
			}
			
			rotationRequired = Math.toRadians(arrowAng - 90);
			tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
			op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			
			// Display arrow
			graphics.drawImage(op.filter(arrow, null), aX, aY, null);
		
			// Display main character sprite
			graphics.drawImage(nukeDukem, nDX, nDY, 120, 160, null);

			// Draw pre-generated bubbles determined by level
			for (int i = 1; i <= numBubbles; i++) {
				if (bubbles[i - 1].getType() == 0) {
					continue;
				}
				
				graphics.drawImage(bubbles[i - 1].getImage(), bubbles[i - 1].getXPos(), bubbles[i-1].getYPos(), 70, 73, null);
			}

			//for (int i = 0; i < numBubbles; i++) {
			//	for (int j = 0; j < bubbles[i].getColl().length; j++) {
			//		graphics.drawOval(bubbles[i].getColl()[j][0], bubbles[i].getColl()[j][1], 1, 1);
			//	}
			//}
			
			// Control main character
			if (remainingBubbles != 0 && time != 0) {
				if (nDX > 14) if (input.pressed(Button.L)) {
					nDX -= 8;
					aX -= 8;
				}
				if (nDX < 512) if (input.pressed(Button.R)) {
					nDX += 8;
					aX += 8;
				}
			}

			if (remainingBubbles != 0 && !tutorial) {
				if (input.pressed(Button.A)) {	// Fire nuke
					if (fire == false) {
						nuke = nextNuke;
						randNuke();
						nX = nDX;
						fire = true;
					}
				}
			}
			
			// Control direction to fire
			if (!fire) {
				ang = arrowAng;
			}
			
			// Control arrow
			if (arrowAng <= 115) {
				if (input.pressed(Button.B)) {
					arrowAng += 5;
				}
			}
			if (arrowAng >= 60) {
				if (input.pressed(Button.C)) {
					arrowAng -= 5;
				}
			}
			
			// Nuke has been fired
			if (fire) {
				graphics.drawImage(nuke.getImage(), nX, nY, 70, 96, null);
				
				//for (int i = 0; i < 70; i++) {
				//	graphics.drawOval(nX + 33, nY + i + 40, 1, 1);
				//	graphics.drawOval(nX + 56, nY + i + 20, 1, 1);
				//	graphics.drawOval(nX + 6, nY + i + 20, 1, 1);
				//}

				// Check if nuke collides with bubble
				for (int i = 0; i < bubbles.length; i++) {
					if (bubbles[i].getType() != 0) {
						for (int j = 0; j < bubbles[i].getColl().length; j++) {
		
							for (int k = 0; k < 60; k++) {
								if (j <= 60) {
									if ((((nX + 33) == bubbles[i].getColl()[j][0]) && ((nY + k + 40) == bubbles[i].getColl()[j][1]))) {
										fire = false;
										
										numBubbles++;
										Bubble [] n = new Bubble[numBubbles];
										
										for (int l = 0; l < bubbles.length; l++) {
											n[l] = bubbles[l];
										}
										
										bubbles = n;
										bubbles[numBubbles - 1] = new Bubble();
										bubbles[numBubbles - 1].setType(nuke.getType());
																	
										bubbles[numBubbles - 1].setImage(nuke.getBubImage());
												
										remainingBubbles++;
										
										int actualPos = 0;
										
										if (j <= 30) {
											bubbles[numBubbles - 1].setPos(bubbles[i].getXPos() - 30, bubbles[i].getYPos() - 52);
											if (i >= originalNumBubbles) {
												actualPos = bubbles[i].getActualPos() + 9;
											} else {
												actualPos = i + 9;
											}
										} else if (j <= 60) {
											bubbles[numBubbles - 1].setPos(bubbles[i].getXPos() + 30, bubbles[i].getYPos() - 52);
											if (i >= originalNumBubbles) {
												actualPos = bubbles[i].getActualPos() + 10;
											} else {
												actualPos = i + 10;
											}
										}
																		
										bubbles[numBubbles - 1].setActualPos(actualPos);
										
										clearBubbles(bubbles[numBubbles - 1]);
										
										nY = nDY;
									}
								} else if (j <= 117) {
									if ((((nX + 6) == bubbles[i].getColl()[j][0]) && ((nY + k + 20) == bubbles[i].getColl()[j][1])) || (((nX + 56) == bubbles[i].getColl()[j][0]) && ((nY + k + 20) == bubbles[i].getColl()[j][1]))) {
										fire = false;
										
										numBubbles++;
										Bubble [] n = new Bubble[numBubbles];
										
										for (int l = 0; l < bubbles.length; l++) {
											n[l] = bubbles[l];
										}
										
										bubbles = n;
										bubbles[numBubbles - 1] = new Bubble();
										bubbles[numBubbles - 1].setType(nuke.getType());
																	
										bubbles[numBubbles - 1].setImage(nuke.getBubImage());
												
										remainingBubbles++;
										
										int actualPos = 0;
										
										bubbles[numBubbles - 1].setPos(bubbles[i].getXPos() - 60, bubbles[i].getYPos());
										if (i >= originalNumBubbles) {
											actualPos = bubbles[i].getActualPos() - 1;
										} else {
											actualPos = i - 1;
										}
																		
										bubbles[numBubbles - 1].setActualPos(actualPos);
										
										clearBubbles(bubbles[numBubbles - 1]);
										
										nY = nDY;
									}
								}
							}
						}
					}
				}
				
				// Update nuke position
				nX += (int)(12 * Math.cos(ang * Math.PI / 180));
				nY += (int)(12 * Math.sin(ang * Math.PI / 180));
			}

			// Check if nuke flies of screen
			if (nY > 800) {
				//TODO: Place bubble on the bottom of the screen
				nY = nDY;
				fire = false;
			}
			
			if (nX <= 0) {
				ang = ang - 90;
			} else if (nX >= 515) {
				ang = ang + 90;
			}
			
			if (remainingBubbles == 0) {	// You win!
				timer.cancel();
				
				graphics.setColor(Color.WHITE);
				graphics.fillRoundRect(150, 305, 300, 100, 45, 60);
				graphics.setColor(Color.BLACK);
				graphics.setFont(new Font("Helvetica", Font.PLAIN, 30)); 
				graphics.drawString("You win!", 245, 350);
				graphics.drawString("Press W to continue.", 165, 385);
				
				if (input.pressed(Button.U)) {
					storySegment = true;
					
					levelCounter++;
					if (levelCounter != 12) {
						levelName = "level" + levelCounter;
						
						int [] level = p.getLevel(levelName);
						
						generateLevel(level, level.length);
						
						reset();
					}
				}
			}
			
			if (bubbles[numBubbles - 1].getYPos() < 250 && bubbles[numBubbles - 1].getType() != 0 || time == 0) {	// You lose!
				timer.cancel();
				
				graphics.setColor(Color.WHITE);
				graphics.fillRoundRect(150, 305, 300, 100, 45, 60);
				graphics.setColor(Color.BLACK);
				graphics.setFont(new Font("Helvetica", Font.PLAIN, 30));
				if (levelCounter == 2) {
					graphics.drawString("You lose!", 245, 350);
					graphics.drawString("Press W to continue.", 165, 385);
				} else {
					graphics.drawString("You lose!", 245, 350);
					graphics.drawString("Press W to restart.", 175, 385);
				}
				
				if (input.pressed(Button.U)) {
					if (levelCounter == 2) {
						levelCounter++;
						storySegment = true;
					} else {
						setTimer();
					}
					
					levelName = "level" + levelCounter;
					
					int [] level = p.getLevel(levelName);

					generateLevel(level, level.length);
					
					reset();
				}
			}
		}		
	}
	
	public void clearBubbles(Bubble b) {
		int actualPos = b.getActualPos();
		boolean isLeft = false;
		boolean isRight = false;
		int temp = 10;
		
		//	Check adjacent bubbles and create a chain
		BubbleChain newChain = new BubbleChain(1, b);
		
		while (temp < numBubbles) {
			if (temp == actualPos) {
				isLeft = true;
				break;
			} else {
				temp += 19;
			}
		}
		
		//	Check left
		if (actualPos == 0 || actualPos % 19 == 0) {
			System.out.println("Case 1: Don't check left because actualPos == 0 or divisible by 19");
		} else if (isLeft) {
			System.out.println("Case 2: Don't check left because first of a row of 9");
		} else {
			boolean exists = false;
			int i;
			
			for (i = numBubbles - 2; i >= originalNumBubbles; i--) {	// Start at numBubbles - 2 cause I just added myself to the end
				if (bubbles[i].getActualPos() == actualPos - 1) {
					exists = true;
					break;
				}
			}
			
			if (exists) {
				if (bubbles[i].getType() == b.getType()){
					if (bubbles[i].getBubbleChain() == null) {
						newChain.addBubble(bubbles[i]);
						System.out.println("Added left bubble with no chain.");
					} else {
						BubbleChain leftChain = bubbles[i].getBubbleChain();
						
						for (int j = 0; j < leftChain.getLength(); j++) {
							newChain.addBubble(leftChain.getBubble(j));
						}
						System.out.println("Added left bubble with chain.");
					}
				} else {
					System.out.println("Bubble exists but not the same type.");
				}
			} else {
				if (!(actualPos - 1 >= originalNumBubbles)) {
					if (bubbles[actualPos - 1].getType() == b.getType()){
						if (bubbles[actualPos - 1].getBubbleChain() == null) {
							newChain.addBubble(bubbles[actualPos - 1]);
							System.out.println("Added left bubble with no chain.");
						} else {
							BubbleChain leftChain = bubbles[actualPos - 1].getBubbleChain();
							
							for (int j = 0; j < leftChain.getLength(); j++) {
								newChain.addBubble(leftChain.getBubble(j));
							}
							System.out.println("Added left bubble with chain.");
						}
					} else {
						System.out.println("Bubble exists but not the same type.");
					}
				}
			}
		}
		
		// Check bottom left
		if (actualPos <= 9) {
			System.out.println("Case 1: Don't check bottom left because actualPos <= 9");
		} else if (actualPos % 19 == 0) {
			System.out.println("Case 2: Don't check bottom left because actualPos == 0 or divisible by 19");
		} else {
			boolean exists = false;
			int i;
			
			for (i = numBubbles - 2; i >= originalNumBubbles; i--) {	// Start at numBubbles - 2 cause I just added myself to the end
				if (bubbles[i].getActualPos() == actualPos - 10) {
					exists = true;
					break;
				}
			}
			
			boolean inChain = false;
			
			if (exists) {
				if (bubbles[i].getType() == b.getType()){
					for (int j = 0; j < newChain.getLength(); j++) {
						if (newChain.getBubble(j).equals(bubbles[i])) {
							inChain = true;
						}
					}
					
					if (!inChain) {
						if (bubbles[i].getBubbleChain() == null) {
							newChain.addBubble(bubbles[i]);
							System.out.println("Added bottom left bubble with no chain.");
						} else {
							BubbleChain bottomLeftChain = bubbles[i].getBubbleChain();
							
							for (int j = 0; j < bottomLeftChain.getLength(); j++) {
								newChain.addBubble(bottomLeftChain.getBubble(j));
							}
							System.out.println("Added bottom left bubble with chain.");
						}
					} else {
						System.out.println("Bubble is already in chain.");
					}
				} else {
					System.out.println("Bubble exists but not the same type.");
				}
			} else {
				if (!(actualPos - 10 >= originalNumBubbles)) {
					if (bubbles[actualPos - 10].getType() == b.getType()){
						for (int j = 0; j < newChain.getLength(); j++) {
							if (newChain.getBubble(j).equals(bubbles[actualPos - 10])) {
								inChain = true;
							}
						}					
						
						if (!inChain) {
							if (bubbles[actualPos - 10].getBubbleChain() == null) {
								newChain.addBubble(bubbles[actualPos - 10]);
								System.out.println("Added bottom left bubble with no chain.");
							} else {
								BubbleChain bottomLeftChain = bubbles[actualPos - 10].getBubbleChain();
								
								for (int j = 0; j < bottomLeftChain.getLength(); j++) {
									newChain.addBubble(bottomLeftChain.getBubble(j));
								}
								System.out.println("Added bottom left bubble with chain.");
							}
						} else {
							System.out.println("Bubble is already in chain.");
						}
					} else {
						System.out.println("Bubble exists but not the same type.");
					}
				}
			}
		}
		
		// Check bottom right
		if (actualPos <= 9) {
			System.out.println("Case 1: Don't check bottom right because i <= 9");
		} else if ((actualPos - 9) % 19 == 0) {
			System.out.println("Case 2: Don't check bottom right because i - 9 is divisible by 19");
		} else {
			boolean exists = false;
			int i;
			
			for (i = numBubbles - 2; i >= originalNumBubbles; i--) {	// Start at numBubbles - 2 cause I just added myself to the end
				if (bubbles[i].getActualPos() == actualPos - 9) {
					exists = true;
					break;
				}
			}
			
			boolean inChain = false;
			
			if (exists) {
				if (bubbles[i].getType() == b.getType()){
					for (int j = 0; j < newChain.getLength(); j++) {
						if (newChain.getBubble(j).equals(bubbles[i])) {
							inChain = true;
						}
					}
					
					if (!inChain) {
						if (bubbles[i].getBubbleChain() == null) {
							newChain.addBubble(bubbles[i]);
							System.out.println("Added bottom right bubble with no chain.");
						} else {
							BubbleChain bottomRightChain = bubbles[i].getBubbleChain();
							
							for (int j = 0; j < bottomRightChain.getLength(); j++) {
								newChain.addBubble(bottomRightChain.getBubble(j));
							}
							System.out.println("Added bottom right bubble with chain.");
						}
					} else {
						System.out.println("Bubble is already in chain.");
					}
				} else {
					System.out.println("Bubble exists but not the same type.");
				}
			} else {
				if (!(actualPos - 9 >= originalNumBubbles)) {
					if (bubbles[actualPos - 9].getType() == b.getType()){
						for (int j = 0; j < newChain.getLength(); j++) {
							if (newChain.getBubble(j).equals(bubbles[actualPos - 9])) {
								inChain = true;
							}
						}
						
						if (!inChain) {
							if (bubbles[actualPos - 9].getBubbleChain() == null) {
								newChain.addBubble(bubbles[actualPos - 9]);
								System.out.println("Added bottom right bubble with no chain.");
							} else {
								BubbleChain bottomRightChain = bubbles[actualPos - 9].getBubbleChain();
								
								for (int j = 0; j < bottomRightChain.getLength(); j++) {
									newChain.addBubble(bottomRightChain.getBubble(j));
								}
								System.out.println("Added bottom right bubble with chain.");
							}	
						} else {
							System.out.println("Bubble is already in chain.");
						}
					} else {
						System.out.println("Bubble exists but not the same type.");
					}
				}
			}
		}
		
		if (!isLeft) {
			temp = 18;
			
			while (temp < numBubbles) {
				if (temp == actualPos) {
					isRight = true;
					break;
				} else {
					temp += 19;
				}
			}
		}

		//	Check right
		if ((actualPos - 9) % 19 == 0) {
			System.out.println("Case 1: Don't check right because actualPos - 9 is divisible by 19");
		} else if (isRight) {
			System.out.println("Case 2: Don't check right because last in a row of 9");
		} else {
			boolean exists = false;
			int i;
			
			for (i = numBubbles - 2; i >= originalNumBubbles; i--) {	// Start at numBubbles - 2 cause I just added myself to the end
				if (bubbles[i].getActualPos() == actualPos + 1) {
					exists = true;
					break;
				}
			}
			
			boolean inChain = false;
			
			if (exists) {
				if (bubbles[i].getType() == b.getType()){
					for (int j = 0; j < newChain.getLength(); j++) {
						if (newChain.getBubble(j).equals(bubbles[i])) {
							inChain = true;
						}
					}
					
					if (!inChain) {
						if (bubbles[i].getBubbleChain() == null) {
							newChain.addBubble(bubbles[i]);
							System.out.println("Added right bubble with no chain.");
						} else {
							BubbleChain bottomRightChain = bubbles[i].getBubbleChain();
							
							for (int j = 0; j < bottomRightChain.getLength(); j++) {
								newChain.addBubble(bottomRightChain.getBubble(j));
							}
							System.out.println("Added right bubble with chain.");
						}
					} else {
						System.out.println("Bubble is already in chain.");
					}
				} else {
					System.out.println("Bubble exists but not the same type.");
				}
			} else {
				if (!(actualPos + 1 >= originalNumBubbles)) {
					if (bubbles[actualPos + 1].getType() == b.getType()){
						for (int j = 0; j < newChain.getLength(); j++) {
							if (newChain.getBubble(j).equals(bubbles[actualPos + 1])) {
								inChain = true;
							}
						}
						
						if (!inChain) {
							if (bubbles[actualPos + 1].getBubbleChain() == null) {
								newChain.addBubble(bubbles[actualPos + 1]);
								System.out.println("Added right bubble with no chain.");
							} else {
								BubbleChain bottomRightChain = bubbles[actualPos + 1].getBubbleChain();
								
								for (int j = 0; j < bottomRightChain.getLength(); j++) {
									newChain.addBubble(bottomRightChain.getBubble(j));
								}
								System.out.println("Added right bubble with chain.");
							}	
						} else {
							System.out.println("Bubble is already in chain.");
						}
					} else {
						System.out.println("Bubble exists but not the same type.");
					}
				}
			}
		}
		
		// Check top left
		if (actualPos <= 9) {
			System.out.println("Case 1: Don't check bottom left because actualPos <= 9");
		} else if (actualPos % 19 == 0) {
			System.out.println("Case 2: Don't check bottom left because actualPos == 0 or divisible by 19");
		} else {
			boolean exists = false;
			int i;
			
			for (i = numBubbles - 2; i >= originalNumBubbles; i--) {	// Start at numBubbles - 2 cause I just added myself to the end
				if (bubbles[i].getActualPos() == actualPos + 9) {
					exists = true;
					break;
				}
			}
			
			boolean inChain = false;
			
			if (exists) {
				if (bubbles[i].getType() == b.getType()){
					for (int j = 0; j < newChain.getLength(); j++) {
						if (newChain.getBubble(j).equals(bubbles[i])) {
							inChain = true;
						}
					}
					
					if (!inChain) {
						if (bubbles[i].getBubbleChain() == null) {
							newChain.addBubble(bubbles[i]);
							System.out.println("Added bottom left bubble with no chain.");
						} else {
							BubbleChain bottomLeftChain = bubbles[i].getBubbleChain();
							
							for (int j = 0; j < bottomLeftChain.getLength(); j++) {
								newChain.addBubble(bottomLeftChain.getBubble(j));
							}
							System.out.println("Added bottom left bubble with chain.");
						}
					} else {
						System.out.println("Bubble is already in chain.");
					}
				} else {
					System.out.println("Bubble exists but not the same type.");
				}
			} else {
				if (!(actualPos + 9 >= originalNumBubbles)) {
					if (bubbles[actualPos + 9].getType() == b.getType()){
						for (int j = 0; j < newChain.getLength(); j++) {
							if (newChain.getBubble(j).equals(bubbles[actualPos + 9])) {
								inChain = true;
							}
						}					
						
						if (!inChain) {
							if (bubbles[actualPos + 9].getBubbleChain() == null) {
								newChain.addBubble(bubbles[actualPos + 9]);
								System.out.println("Added bottom left bubble with no chain.");
							} else {
								BubbleChain bottomLeftChain = bubbles[actualPos + 9].getBubbleChain();
								
								for (int j = 0; j < bottomLeftChain.getLength(); j++) {
									newChain.addBubble(bottomLeftChain.getBubble(j));
								}
								System.out.println("Added bottom left bubble with chain.");
							}
						} else {
							System.out.println("Bubble is already in chain.");
						}
					} else {
						System.out.println("Bubble exists but not the same type.");
					}
				}
			}
		}
			
		// Check top right
		if (actualPos <= 9) {
			System.out.println("Case 1: Don't check bottom right because i <= 9");
		} else if ((actualPos + 10) % 19 == 0) {
			System.out.println("Case 2: Don't check bottom right because i - 9 is divisible by 19");
		} else {
			boolean exists = false;
			int i;
			
			for (i = numBubbles - 2; i >= originalNumBubbles; i--) {	// Start at numBubbles - 2 cause I just added myself to the end
				if (bubbles[i].getActualPos() == actualPos + 10) {
					exists = true;
					break;
				}
			}
			
			boolean inChain = false;
			
			if (exists) {
				if (bubbles[i].getType() == b.getType()){
					for (int j = 0; j < newChain.getLength(); j++) {
						if (newChain.getBubble(j).equals(bubbles[i])) {
							inChain = true;
						}
					}
					
					if (!inChain) {
						if (bubbles[i].getBubbleChain() == null) {
							newChain.addBubble(bubbles[i]);
							System.out.println("Added bottom right bubble with no chain.");
						} else {
							BubbleChain bottomRightChain = bubbles[i].getBubbleChain();
							
							for (int j = 0; j < bottomRightChain.getLength(); j++) {
								newChain.addBubble(bottomRightChain.getBubble(j));
							}
							System.out.println("Added bottom right bubble with chain.");
						}
					} else {
						System.out.println("Bubble is already in chain.");
					}
				} else {
					System.out.println("Bubble exists but not the same type.");
				}
			} else {
				if (!(actualPos + 10 >= originalNumBubbles)) {
					if (bubbles[actualPos + 10].getType() == b.getType()){
						for (int j = 0; j < newChain.getLength(); j++) {
							if (newChain.getBubble(j).equals(bubbles[actualPos + 10])) {
								inChain = true;
							}
						}
						
						if (!inChain) {
							if (bubbles[actualPos + 10].getBubbleChain() == null) {
								newChain.addBubble(bubbles[actualPos + 10]);
								System.out.println("Added bottom right bubble with no chain.");
							} else {
								BubbleChain bottomRightChain = bubbles[actualPos + 10].getBubbleChain();
								
								for (int j = 0; j < bottomRightChain.getLength(); j++) {
									newChain.addBubble(bottomRightChain.getBubble(j));
								}
								System.out.println("Added bottom right bubble with chain.");
							}	
						} else {
							System.out.println("Bubble is already in chain.");
						}
					} else {
						System.out.println("Bubble exists but not the same type.");
					}
				}
			}
		}
		
		if (newChain.getLength() > 2) {
			for (int i = 0; i < newChain.getLength(); i++) {
				newChain.getBubble(i).setType(0);
				remainingBubbles--;
			}
		} else {
			for (int i = 0; i < newChain.getLength(); i++) {
				newChain.getBubble(i).setBubbleChain(newChain);
			}
		}
		
		System.out.println(remainingBubbles);
	}
}