package shooter;
import java.awt.*;

public class Explosion {
	
	public int px;
	public int py;
	public CogSprite sprite;
	public boolean dead = false;
	
	private int lifetime = 7;
	
	public Explosion(int x, int y, Image i) {
		px = x;
		py = y;
		sprite = new CogSprite(i,64,4,false);
		
		int anim[] = {4,3,2,1,2,3,4};
		sprite.addAnim(anim);
		sprite.setAnim(0);
	}
	
	public void draw(Graphics g) {
		sprite.draw(px-32,py-32,g);
	}
	
	public void move() {
		sprite.step();
		lifetime--;
		if (lifetime < 1) {dead = true;}
	}
}