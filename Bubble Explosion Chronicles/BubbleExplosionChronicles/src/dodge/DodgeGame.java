package dodge;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import arcadia.Button;
import arcadia.Game;
import arcadia.Input;
import arcadia.Sound;
import arcadia.util.SkyhookFont;

public class DodgeGame extends Game {

	protected final SkyhookFont sFont = new SkyhookFont(15);
	Image bannerImage = null;
	int score = 10;
	int ticker = 0;

	int x;
	boolean gameOver = true;

	ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

	public DodgeGame() {
	}


	@Override
	public void tick(Graphics2D g, Input input, Sound sound) {
		if (gameOver) {
			score = 0;
			obstacles.clear();
			x = Game.WIDTH / 2;
			gameOver = false;
		}

		int speedMultiplier = 1 + (score / 10);

		ticker ++;
		if (ticker > 200 / speedMultiplier) {
			obstacles.add(new Obstacle(300));
			ticker = 0;
			score++;
		}


		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		g.setColor(Color.blue);
		g.drawRect(0, 0, Game.WIDTH-1, Game.HEIGHT-1);

		Rectangle ship = new Rectangle(x,Game.HEIGHT-100,10,10);

		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		for (int i = 0; i < obstacles.size(); i++) {
			Obstacle obs = obstacles.get(i);
			obs.draw(g);
			obs.updateY(speedMultiplier);

			if (obs.isColliding(ship)) {
				gameOver = true;
			}

			if (!obs.isAlive()) {
				toRemove.add(i);
			}
		}

		for (int i : toRemove) {
			obstacles.remove(i);
		}

		if (input != null) {
			if (input.pressed(Button.L)) { x -= 3 * speedMultiplier; }
			if (input.pressed(Button.R)) { x += 3 * speedMultiplier; }
		}

		if (x < 0) { x = 0; }
		if (x + 10 > Game.WIDTH) { x = Game.WIDTH-10;}


		g.setColor(Color.green);
		g.drawRect(x, Game.HEIGHT - 100, 10, 10);

		g.setColor(Color.BLUE);
		sFont.draw(g, new Point(10,10), "Score: " + score);
	}


	@Override
	public Image cover() {
		if (bannerImage == null) {
			Image image = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);
			//Draw the first 100 frames to our graphics object
			for (int i = 0; i < 650; i++) {
				tick((Graphics2D) image.getGraphics(),null,null);
			}
			bannerImage = image;
		}


		return bannerImage;
	}

}
