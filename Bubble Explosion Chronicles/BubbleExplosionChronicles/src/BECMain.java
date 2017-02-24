import static arcadia.Game.HEIGHT;
import static arcadia.Game.WIDTH;
import arcadia.*;
import arcadia.Button;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.Math;

import javax.imageio.ImageIO;

public class BECMain extends Game {

	private Image bg;
	private Image nukeDukem;
	private Nuke nuke;
	private Nuke nextNuke;
	private Bubble [] bubbles;
	private int [][] bubCollis;
	private int numBubbles;
	private int nDX, nDY, nX, nY;
	private boolean fire;

	public BECMain() {
		try {
			bg = ImageIO.read(new File("src/images/backgrounds/random.png"));
			nukeDukem = ImageIO.read(new File("src/images/NukeDukemSprite.png"));
		} catch (IOException e) {
			System.out.println("BG/Nuke Dukem Image Error");
		}

		numBubbles = 20;
		bubbles = new Bubble [numBubbles];

		for (int i = 0; i < numBubbles; i++) {
			int rand = (int)(Math.random() * 5) + 1;	// Generates random number between 1 and 5

			switch (rand) {
			case 1:
				try {
					bubbles[i] = new Bubble();
					bubbles[i].setImage(ImageIO.read(new File("src/images/bubbles/bubbleDiamond.png")));
				} catch (IOException e) {
					System.out.println("Bubble Image Error");
				}
				break;
			case 2:
				try {
					bubbles[i] = new Bubble();
					bubbles[i].setImage(ImageIO.read(new File("src/images/bubbles/bubbleLightning.png")));
				} catch (IOException e) {
					System.out.println("Bubble Image Error");
				}
				break;
			case 3:
				try {
					bubbles[i] = new Bubble();
					bubbles[i].setImage(ImageIO.read(new File("src/images/bubbles/bubblePentagon.png")));
				} catch (IOException e) {
					System.out.println("Bubble Image Error");
				}
				break;
			case 4:
				try {
					bubbles[i] = new Bubble();
					bubbles[i].setImage(ImageIO.read(new File("src/images/bubbles/bubbleSquare.png")));
				} catch (IOException e) {
					System.out.println("Bubble Image Error");
				}
				break;
			case 5:
				try {
					bubbles[i] = new Bubble();
					bubbles[i].setImage(ImageIO.read(new File("src/images/bubbles/bubbleTriangle.png")));
				} catch (IOException e) {
					System.out.println("Bubble Image Error");
				}
				break;
			default:
				try {
					bubbles[i] = new Bubble();
					bubbles[i].setImage(ImageIO.read(new File("src/images/bubbles/bubbleDiamond.png")));	// Diamond is the default
				} catch (IOException e) {
					System.out.println("Bubble Image Error");
				}
				break;
			}
		}

		nDX = 240;
		nDY = 20;
		nX = nDX;
		nY = nDY;

		randNuke();

		setBubPositions();

		reset();    
	}

	public void reset() {

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

	public void setBubPositions() {

		int bX = -5;
		int bY = 735;
		int temp = 10;

		for (int i = 1; i <= numBubbles; i++) {
			bubbles[i - 1].setPos(bX, bY);
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

		switch (rand) {
		case 1:
			try {
				nextNuke = new Nuke();
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeDiamond.png")), ImageIO.read(new File("src/images/bubbles/bubbleDiamond.png")));
			} catch (IOException e) {
				System.out.println("Bubble Image Error");
			}
			break;
		case 2:
			try {
				nextNuke = new Nuke();
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeLightning.png")), ImageIO.read(new File("src/images/bubbles/bubbleLightning.png")));
			} catch (IOException e) {
				System.out.println("Bubble Image Error");
			}
			break;
		case 3:
			try {
				nextNuke = new Nuke();
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukePentagon.png")), ImageIO.read(new File("src/images/bubbles/bubblePentagon.png")));
			} catch (IOException e) {
				System.out.println("Bubble Image Error");
			}
			break;
		case 4:
			try {
				nextNuke = new Nuke();
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeSquare.png")), ImageIO.read(new File("src/images/bubbles/bubbleSquare.png")));
			} catch (IOException e) {
				System.out.println("Bubble Image Error");
			}
			break;
		case 5:
			try {
				nextNuke = new Nuke();
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeTriangle.png")),ImageIO.read(new File("src/images/bubbles/bubbleTriangle.png")));
			} catch (IOException e) {
				System.out.println("Bubble Image Error");
			}
			break;
		default:
			try {
				nextNuke = new Nuke();
				nextNuke.setImage(ImageIO.read(new File("src/images/projectiles/nukeDiamond.png")), ImageIO.read(new File("src/images/bubbles/bubbleDiamond.png")));	// Diamond is the default
			} catch (IOException e) {
				System.out.println("Bubble Image Error");
			}
			break;
		}
	}

	@Override
	public void tick(Graphics2D graphics, Input input, Sound sound) {
		graphics.drawImage(bg, 0, 0, WIDTH, HEIGHT, null);

		graphics.setColor(Color.BLACK);
		graphics.fillRoundRect(10, 10, 80, 106, 20, 30);
		graphics.drawImage(nextNuke.getImage(), 15, 10, 70, 96, null);

		graphics.drawImage(nukeDukem, nDX, nDY, 120, 160, null);

		for (int i = 1; i <= numBubbles; i++) {
			graphics.drawImage(bubbles[i - 1].getImage(), bubbles[i-1].getXPos(), bubbles[i-1].getYPos(), 70, 73, null);
			//graphics.drawRect(bubbles[i-1].getXPos() + 5, bubbles[i-1].getYPos() + 5, 58, 58);
		}

		if (nDX > 0) if (input.pressed(Button.L)) nDX -= 4;
		if (nDX < 480) if (input.pressed(Button.R)) nDX += 4;
		if (input.pressed(Button.A)) {
			if (fire == false) {
				nuke = nextNuke;
				randNuke();
				nX = nDX;
				fire = true;
			}
		}

		if (fire) {
			graphics.drawImage(nuke.getImage(), nX, nY, 70, 96, null);

			for (int i = 0; i < bubbles.length; i++) {
				for (int j = 0; j < bubbles[i].getColl().length; j++) {
					//System.out.println(bubbles[i].getColl()[j][1]);
					if ((nX + 70) == bubbles[i].getColl()[j][0] && (nY + 96) == bubbles[i].getColl()[j][1]) {
						fire = false;
						
						numBubbles++;
						Bubble [] n = new Bubble[numBubbles];
						
						for (int k = 0; k < bubbles.length; k++) {
							n[k] = bubbles[k];
						}
						
						bubbles = n;
						bubbles[numBubbles - 1] = new Bubble();
						
						bubbles[numBubbles - 1].setImage(nuke.getBubImage());
								
						bubbles[numBubbles - 1].setPos(bubbles[i].getXPos() - 30, bubbles[i].getYPos() - 52);

						nY = nDY;

					}
				}
			}

			nY += 1;
		}

		if (nY > 800) {
			nY = nDY;
			fire = false;
		}
	}
}