package shooter;
import java.awt.*;
import java.util.*;

public class CogParallaxer {
	public int xoffset;
	public int yoffset;
	public int width;
	public int height;
	public Vector<Image> frames = new Vector<Image>();
	public Vector<Integer> framew = new Vector<Integer>();
	public Vector<Integer> frameh = new Vector<Integer>();
	public Vector<Integer> yoffsets = new Vector<Integer>();
	public Vector<Double> xmults = new Vector<Double>();
	public Vector<Double> xscrolls = new Vector<Double>();

	public CogParallaxer(int xoff, int yoff, int w, int h) {
		xoffset = xoff;
		yoffset = yoff;
		width = w;
		height = h;
	}

	public void addLayer(Image i, int w, int h, int y, double m) {
		frames.add(i);
		framew.add(w);
		frameh.add(h);
		yoffsets.add(y);
		xmults.add(m);
		xscrolls.add(0.0);
	}
	
	public void scroll(int scrollx) {
		for(int x=0;x<frames.size();x++) {
			xscrolls.set(x, (xmults.get(x).doubleValue()*scrollx) + (xscrolls.get(x).doubleValue()));
		}
	}
	public void scroll(double scrollx) {
		for(int x=0;x<frames.size();x++) {
			xscrolls.set(x, (xmults.get(x).doubleValue()*scrollx) + (xscrolls.get(x).doubleValue()));
		}
	}
	
	public int size() {return frames.size();}
	
	public void draw(Graphics g) {
		for(int x=0;x<frames.size();x++) {draw(x,g);}
	}
	
	public void draw(int layer, Graphics g) {
		int iw = framew.get(layer);
		int ih = frameh.get(layer);
		int xcounter = xoffset;
		int ixcounter = (int)(xscrolls.get(layer).doubleValue()) % iw;
		while (ixcounter < 0) {ixcounter += iw;}
		int yoff = yoffsets.get(layer);
		Image img = frames.get(layer);
		int dwidth = width + xoffset;
		
		while (xcounter < dwidth) {
			
			int grabx1 = ixcounter;
			int grabx2 = iw;
			ixcounter = 0;
			if (xcounter + (grabx2 - grabx1) >= dwidth) {
				grabx2 += (dwidth - (xcounter+(grabx2 - grabx1)));
			}
			
			int graby1 = 0;
			int graby2 = ih-1;
			int x1 = xcounter;
			int x2 = xcounter+(grabx2-grabx1);
			int y1 = yoffset+yoff;
			int y2 = yoffset+yoff+ih;
			
			xcounter += grabx2-grabx1;

			g.drawImage(img, x1, y1, x2, y2, grabx1, graby1, grabx2, graby2, null);
		}
	}
}