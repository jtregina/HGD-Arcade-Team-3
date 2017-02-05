package shooter;
import java.awt.*;

import arcadia.Game;

public class Rocket {

	public int px;
	public int py;
	public Image img;
	public boolean dead = false;

	public Rocket(int x, int y, Image i) {
		px = x;
		py = y;
		img = i;
	}

	public void draw(Graphics g) {
		g.drawImage(img,px-16,py-8,null);
	}

	public void move() {
		px += 12;
		if (px >= Game.WIDTH + 50) {dead = true;}
	}
}