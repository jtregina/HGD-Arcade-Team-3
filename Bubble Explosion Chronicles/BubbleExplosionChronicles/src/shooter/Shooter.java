package shooter;
import intro.IntroGame;

import java.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import arcadia.Arcadia;
import arcadia.Game;
import arcadia.Input;
import arcadia.Sound;
import arcadia.Button;

/*<applet code="Shooter" width=480 height=480></applet>*/

public class Shooter extends Game {
	Image dbImage;
	Graphics dbg;

	Image ship;
	Image meteor;
	Image life;
	Image rocket;
	Image blast;
	Image title;
	Image gameover;
	Image land;
	Image ground;
	
	Image cover;

	int score = 20;
	int bulletCount = 10;
	int nextBulletCounter = 00;
	int rapidFireCounter = -1;
	boolean spaceReleased = true;

	int playerx = 32;
	int playery = 240;
	int playerlives = 3;

	Vector<Meteor> rocks = new Vector<Meteor>();
	Vector<Rocket> bullets = new Vector<Rocket>();
	Vector<Explosion> explosions = new Vector<Explosion>();

	CogParallaxer pax;

	String mode = "intro";
	
	public Shooter() {
		try {
			ship = ImageIO.read(this.getClass().getResource("ship.gif"));
			meteor = ImageIO.read(this.getClass().getResource("rock.gif"));
			life = ImageIO.read(this.getClass().getResource("life.gif"));
			rocket = ImageIO.read(this.getClass().getResource("rocket.gif"));
			blast = ImageIO.read(this.getClass().getResource("blast.gif"));
			title = ImageIO.read(this.getClass().getResource("title.gif"));
			gameover =ImageIO.read(this.getClass().getResource("gameover.gif"));
			land = ImageIO.read(this.getClass().getResource("land.gif"));
			ground = ImageIO.read(this.getClass().getResource("ground.gif"));
			cover = ImageIO.read(this.getClass().getResource("cover.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}


		pax = new CogParallaxer(0,0,WIDTH,480);
		pax.addLayer(land,480,240,240,.6);
		pax.addLayer(ground,480,128,362,1);
	}

	//Made by mitch
	public static void main(String[] args) {
//		JFrame frame = new JFrame();
//		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
//
//		Shooter s = new Shooter();
//		frame.addKeyListener(s);
//		s.setPreferredSize(new Dimension(480,480));
//		s.init();
//		frame.add(s);
//
//
//		frame.setVisible(true);
//		frame.pack();

		Arcadia.display(new Arcadia(new Shooter()));

	}

	public void init() {
		//this.addKeyListener(this);
		//dbImage = createImage(this.getSize().width, this.getSize().height);
		//dbg = dbImage.getGraphics();


		/*ship = getImage(getDocumentBase(),"ship.gif");
		meteor = getImage(getDocumentBase(),"rock.gif");
		life = getImage(getDocumentBase(),"life.gif");
		rocket = getImage(getDocumentBase(),"rocket.gif");
		blast = getImage(getDocumentBase(),"blast.gif");
		title = getImage(getDocumentBase(),"title.gif");
		gameover = getImage(getDocumentBase(),"gameover.gif");
		land = getImage(getDocumentBase(),"land.gif");
		ground = getImage(getDocumentBase(),"ground.gif");*/

		
	}

	public void setup() {
		playery = 240;
		playerlives = 3;
		nextBulletCounter = 0;
		bulletCount = 3;
		rapidFireCounter = -1;
		score = 0;
		rocks.clear();
		bullets.clear();
		explosions.clear();
		mode = "main";
	}

	@Override
	public void tick(Graphics2D g, Input input, Sound sound) {
		g.translate(0, 48);
		
		Shape outline = new Rectangle(0,0,WIDTH,480);
		g.setClip(outline);
		

		g.setColor(Color.BLACK);
		g.fillRect(0,0,WIDTH,480);
		pax.draw(g);
		pax.scroll(8);

		this.handleInput(input);
		
		if (input.pressed(Button.U)) {playery-=16;}
		if (input.pressed(Button.D)) {playery+=16;}
		if (playery > 448) {playery = 448;}
		if (playery <  32) {playery = 32;}

		//Copied twice to cope from 60 to 30 fps change
		if (Math.random() <= (score+10) / 1000.0) {rocks.add(new Meteor(meteor));}
		if (Math.random() <= (score+10) / 1000.0) {rocks.add(new Meteor(meteor));}

		for(int x=0;x<rocks.size();x++) {
			Meteor rock = rocks.get(x);

			rock.draw(g);
			rock.move();

			if (mode.equals("main")) {
				int dx = playerx - rock.px;
				int dy = playery - rock.py;
				if (dx*dx + dy*dy <= 60*60) {
					playerlives--;
					rock.dead = true;
					explosions.add(new Explosion(rock.px,rock.py,blast));
					if (playerlives < 1) {mode = "dead";}
				}
			}
		}

		for(int x=0;x<bullets.size();x++) {
			Rocket bullet = bullets.get(x);

			bullet.draw(g);
			bullet.move();

			for(int y=0;y<rocks.size();y++) {
				Meteor rock = rocks.get(y);

				int dx = rock.px - bullet.px;
				int dy = rock.py - bullet.py;
				if (dx*dx + dy*dy < 64*64) {
					rock.dead=true;
					bullet.dead=true;
					score++;
					if (rapidFireCounter <= 0) {
						bulletCount+=1;
					}
					explosions.add(new Explosion(rock.px,rock.py,blast));
				}
			}
		}

		for(int x=0;x<explosions.size();x++) {
			Explosion explosion = explosions.get(x);

			explosion.draw(g);
			explosion.move();
		}

		for(int x=0;x<rocks.size();x++) {if (rocks.get(x).dead) {rocks.remove(x);}}
		for(int x=0;x<bullets.size();x++) {if (bullets.get(x).dead) {bullets.remove(x);}}
		for(int x=0;x<explosions.size();x++) {if (explosions.get(x).dead) {explosions.remove(x);}}








		if (mode.equals("main")) {
			g.drawImage(ship, playerx,(playery - 32),null);
			for(int x=0;x<playerlives;x++) {g.drawImage(life,16+(32*x),16,null);}



			//Added by mitch (All below)
			g.setFont(new Font("Verdana",Font.PLAIN,12));
			g.setColor(Color.white);
			g.drawString("Score: " + score, 1, 12);
			g.drawString("Bullets: " + bulletCount, 1, 61);

			nextBulletCounter+=2;
			if (nextBulletCounter >= 1000) {
				bulletCount++;
				nextBulletCounter = 0;
				score++;
			}

			if (score > 75 && rapidFireCounter < 0) {
				g.drawString("Press B for rapid fire", 1, 89);
			}
			if (rapidFireCounter > 0) {
				rapidFireCounter--;
				bullets.add(new Rocket(playerx+32,playery,rocket));
			}


			//Draw the next Bullet counter
			g.setColor(Color.gray);
			g.fillRect(1, 62, nextBulletCounter / 10, 10);
			g.setColor(Color.white);
			g.drawRect(1, 62, 100, 10);
		}
		else if (mode.equals("intro")) {
			g.drawImage(title,272,0,null);
			g.setColor(Color.white);
			g.setFont(new Font("Verdana",Font.PLAIN,12));
			if (!input.pressed(Button.C)) {
				g.drawString("Updated by Mitch Davis, 2014", 1, 12);
				g.drawString("Press C for details", 1, 25);
			} else {
				int i = 1;
				g.drawString("Limited FPS", 1, 13*i++);
				g.drawString("Added Score", 1, 13*i++);
				g.drawString("Created Bullet Limit", 1, 13*i++);
				g.drawString("Created Rapid Fire (at score 75)", 1, 13*i++);
				g.drawString("Game gets harder with time", 1, 13*i++);
				g.drawString("Converted from applet to JFrame", 1, 13*i++);
			}
		}
		else if (mode.equals("dead")) {
			g.drawImage(gameover,272,0,null);
		}


		
		
	}

	public void handleInput(Input i) {
		if (mode.equals("main")) {

			if (!i.pressed(Button.A)) {
				spaceReleased = true;
			}
			
			//Update by mitch, no more stream of bullets
			if (i.pressed(Button.A) && spaceReleased && bulletCount > 0) {
				bullets.add(new Rocket(playerx+32,playery,rocket));
				spaceReleased = false;
				bulletCount--;
			}

			//updated by mitch
			//rapidfire
			if (i.pressed(Button.B) && rapidFireCounter < 0 && score > 75) {
				rapidFireCounter = 300;
			}
		}
		else if (mode.equals("dead") || mode.equals("intro")) {
			if (i.pressed(Button.B)) {setup();}
		}
	}

//	public void update(Graphics g) {
//		dbg.setColor (getBackground ());
//		dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);
//		paint (dbg);
//		g.drawImage (dbImage, 0, 0, this);
//	}


	@Override
	public Image cover() {
		return cover;
	}

}