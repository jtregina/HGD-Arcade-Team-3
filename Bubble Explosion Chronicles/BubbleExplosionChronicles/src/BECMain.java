import arcadia.*;
import arcadia.Button;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Math;

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
	private int nDX, nDY, nX, nY, aX, aY;
	private boolean fire;
	private int levelCounter = 2;
	private String levelName;
	private double ang = 90;
	private double arrowAng = 90;
	private double locationX = 24;
	private double locationY = 30;
	private double rotationRequired = Math.toRadians(arrowAng - 90);
	AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
	AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

	public BECMain() {
		try {
			bg = ImageIO.read(new File("src/images/backgrounds/random.png"));
			nukeDukem = ImageIO.read(new File("src/images/NukeDukemSprite.png"));
			arrow = ImageIO.read(new File("src/images/arrow.png"));
		} catch (IOException e) {
			System.out.println("BG/Nuke Dukem Image Error");
		}
		
		p = new Parser();
	
		levelName = "level" + levelCounter;
		
		int [] level = p.getLevel(levelName);

		generateLevel(level, level.length);

		reset();
	}

	public void reset() {
		nDX = 240;
		nDY = 20;
		nX = nDX;
		nY = nDY;
		aX = nDX;
		aY = nDY + 175;

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
		bubbles = new Bubble [numBubbles];
		
		int type;
		
		for (int i = 0; i < numBubbles; i++) {
			type = level[i];
			
			bubbles[i] = new Bubble();
			bubbles[i].setType(type);
			
			//	Check adjacent bubbles and create a chain
			
			
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
				bubbles[i].setType(0);
				break;
			default:
				try {
					bubbles[i] = new Bubble();
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
		int rand = (int)(Math.random() * 5) + 1;	// Generates random number between 1 and 5
		
		nextNuke = new Nuke();
		nextNuke.setType(rand);
		
		switch (rand) {
		case 1:
			try {
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeDiamond.png")), ImageIO.read(new File("src/images/bubbles/bubbleDiamond.png")));
			} catch (IOException e) {
				System.out.println("Bubble Image Error");
			}
			break;
		case 2:
			try {
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeLightning.png")), ImageIO.read(new File("src/images/bubbles/bubbleLightning.png")));
			} catch (IOException e) {
				System.out.println("Bubble Image Error");
			}
			break;
		case 3:
			try {
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukePentagon.png")), ImageIO.read(new File("src/images/bubbles/bubblePentagon.png")));
			} catch (IOException e) {
				System.out.println("Bubble Image Error");
			}
			break;
		case 4:
			try {
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeSquare.png")), ImageIO.read(new File("src/images/bubbles/bubbleSquare.png")));
			} catch (IOException e) {
				System.out.println("Bubble Image Error");
			}
			break;
		case 5:
			try {
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeTriangle.png")),ImageIO.read(new File("src/images/bubbles/bubbleTriangle.png")));
			} catch (IOException e) {
				System.out.println("Bubble Image Error");
			}
			break;
		default:
			try {
				nextNuke.setType(1);
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeDiamond.png")), ImageIO.read(new File("src/images/bubbles/bubbleDiamond.png")));	// Diamond is the default
			} catch (IOException e) {
				System.out.println("Bubble Image Error");
			}
			break;
		}
	}

	@Override
	public void tick(Graphics2D graphics, Input input, Sound sound) {
		
		// Draw the background
		graphics.drawImage(bg, 0, 0, WIDTH, HEIGHT, null);

		// Display next Nuke
		graphics.setColor(Color.BLACK);
		graphics.fillRoundRect(10, 10, 80, 106, 20, 30);
		graphics.drawImage(nextNuke.getImage(), 15, 10, 70, 96, null);
		
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

		// Control main character
		if (nDX > 0) if (input.pressed(Button.L)) {
			nDX -= 4;
			aX -= 4;
		}
		if (nDX < 480) if (input.pressed(Button.R)) {
			nDX += 4;
			aX += 4;
		}
		if (input.pressed(Button.A)) {	// Fire nuke
			if (fire == false) {
				nuke = nextNuke;
				randNuke();
				nX = nDX;
				fire = true;
			}
		}
		
		// Control direction to fire
		if (!fire) {
			ang = arrowAng;
		}
		
		// Control arrow
		if (arrowAng <= 135) {
			if (input.pressed(Button.B)) {
				arrowAng += 5;
			}
		}
		if (arrowAng >= 45) {
			if (input.pressed(Button.C)) {
				arrowAng -= 5;
			}
		}
		
		// Nuke has been fired
		if (fire) {
			graphics.drawImage(nuke.getImage(), nX, nY, 70, 96, null);

			// Check if nuke collides with bubble
			for (int i = 0; i < bubbles.length; i++) {
				if (bubbles[i].getType() != 0) {
					for (int j = 0; j < bubbles[i].getColl().length; j++) {
	
						for (int k = 0; k < 10; k++) {
							if (((nX + 35) == bubbles[i].getColl()[j][0]) && ((nY + k + 96) == bubbles[i].getColl()[j][1])) {
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
										
								int actualPos;
								
								if ((bubbles[i].getColl()[j][0] - (bubbles[i].getXPos() - 5)) < 29) {
									bubbles[numBubbles - 1].setPos(bubbles[i].getXPos() - 30, bubbles[i].getYPos() - 52);
									actualPos = i + 9;
								} else {
									bubbles[numBubbles - 1].setPos(bubbles[i].getXPos() + 30, bubbles[i].getYPos() - 52);
									actualPos = i + 10;
								}
								
								int adjCounter = 0;
								
								adjCounter = checkBubbleChain(actualPos, adjCounter);
								
								if (adjCounter > 2) {
									//clearBubbles();
								}
								
								nY = nDY;
							}
						}
					}
				}
			}
			
			// Update nuke position
			nX += (int)(5 * Math.cos(ang * Math.PI / 180));
			nY += (int)(5 * Math.sin(ang * Math.PI / 180));
		}

		// Check if nuke flies of screen
		if (nY > 800) {
			nY = nDY;
			fire = false;
		}
		
		if (nX <= 0) {
			ang = ang - 90;
		} else if (nX >= 515) {
			ang = ang + 90;
		}
	}
	
	private int checkBubbleChain(int actualPos, int adjCounter) {
		
		/*//	Check for adjacent bubbles and clear bubbles if they form a chain greater than 2
		if (!(actualPos + 9 > numBubbles - 1)) {
			if (bubbles[actualPos + 9].type == bubbles[numBubbles - 1].type)
				adjCounter++;
		}
		
		if (!(actualPos + 10 > numBubbles - 1)) {
			if (bubbles[actualPos + 10].type == bubbles[numBubbles - 1].type)
				adjCounter++;
		}
		
		if (!(actualPos + 1 > numBubbles - 1)) {
			if (bubbles[actualPos + 1].type == bubbles[numBubbles - 1].type)
				adjCounter++;
		}
		
		if (!(actualPos - 1 > numBubbles - 1)) {
			if (bubbles[actualPos - 1].type == bubbles[numBubbles - 1].type)
				adjCounter++;
		}*/
		
		return adjCounter;
	}
}