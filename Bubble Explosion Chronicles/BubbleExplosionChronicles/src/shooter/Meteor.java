package shooter;
import java.awt.*;

import arcadia.Game;

public class Meteor {

	public int px = Game.WIDTH;
	public int py = 0;
	public Image img;
	public boolean dead = false;

	public Meteor(Image i) {
		py = (int)(Math.random() * 416) + 32;
		img = i;
	}

	public void draw(Graphics g) {
		g.drawImage(img,px,(py-32),null);
	}
	
	public void move() {
		px -= 12;
		if (px <= -64) {dead = true;}
	}
}