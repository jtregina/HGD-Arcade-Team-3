package arcadia;

import java.awt.Graphics2D;
import java.awt.Image;

public abstract class Game {
	public static final int WIDTH  = 600;
	public static final int HEIGHT = 800;

	public abstract void tick(Graphics2D graphics, Input input, Sound sound);
	public abstract Image cover();
}