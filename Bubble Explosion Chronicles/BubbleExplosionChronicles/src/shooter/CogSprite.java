package shooter;
import java.awt.*;
import java.util.*;

/**
* CogSprite is a simple system for drawing and
* manipulating sprite animations.
*
* a part of the CogEngine package
*
* @author John Earnest
**/
public class CogSprite {
	public boolean loop;
	public int tilesize;
	public int tilecountx;
	public int anim = 0;
	public int frame = 0;
	public Vector<int[]> anims;
	public Image spritesheet;

	/**
	* basic constructor
	*
	* @param s the spritesheet for this sprite
	* @param ts the size (square) of sprite frames
	* @param tcx the number of frames in a row of the spritesheet
	* @param l if true, animations will loop upon completion
	**/
	public CogSprite(Image s, int ts,int tcx,boolean l) {
		spritesheet = s;
		tilesize = ts;
		tilecountx = tcx;
		anims = new Vector<int[]>();
		loop = l;
	}

	/**
	* return true if the current animation is complete
	**/
	public boolean done() {
		return (frame==-1);
	}
	/**
	* draw the sprite at a specific location onscreen
	*
	* @param xoffset the x offset at which to draw the sprite
	* @param yoffset the y offset at which to draw the sprite
	* @param g the destination graphics object
	**/
	public void draw(int xoffset, int yoffset, Graphics g) {
		if (frame > -1) {
			int c = (anims.get(anim))[frame];
			int x = xoffset;
			int y = yoffset;
			int tx = tilesize*((c - 1) % tilecountx);
			int ty = tilesize*((c - 1) / tilecountx);
			g.drawImage(spritesheet,x,y,(x+tilesize),(y+tilesize),tx,ty,(tx+tilesize),(ty+tilesize),null);
		}
	}
	/**
	* advance the current animation one frame
	**/
	public void step() {
		if (frame > -1) {frame++;}
		if (frame >= (anims.get(anim)).length) {
			if (loop) {frame = 0;}
			else {frame = -1;}
		}
	}
	/**
	* reset the current animation
	**/
	public void reset() {
		frame = 0;
	}
	/**
	* change to a different animation with the same frame index
	*
	* @param newanim the new animation id
	**/
	public void changeAnim(int newanim) {
		anim = newanim;
		frame = frame% (anims.get(anim)).length;
	}
	/**
	* change to a different animation and reset the frame index
	*
	* @param newanim the new animation id
	**/
	public void setAnim(int newanim) {
		anim = newanim;
		frame = 0;		
	}
	/**
	* add an animation (sequence of ordered frames)
	*
	* @param frames an array of frame ids
	**/
	public int addAnim(int frames[]) {
		anims.add(frames);
		return anims.size()-1;
	}
}