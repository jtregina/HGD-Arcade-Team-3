package dodge;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import arcadia.Game;

public class Obstacle {
	private final int height = 20;
	private int y = -height;
	private int width;
	private int x;
	
	public Obstacle(int width) {
		Random rand = new Random();
		this.width = width;
		this.x = rand.nextInt(Game.WIDTH - width);
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, y, x, height);
		g.fillRect(x+width, y, Game.WIDTH - (x+width), height);
	}
	
	public boolean isColliding(Rectangle ship) {
		Rectangle a = new Rectangle(0,y,x,height);
		if (a.intersects(ship)) { return true; }
		
		Rectangle b = new Rectangle(x+width, y, Game.WIDTH - (x+width), height);;
		return b.intersects(ship);
	}
	
	public void updateY(int amount) {
		y += amount;
	}
	
	public boolean isAlive() {
		return y < Game.HEIGHT;
	}
}
