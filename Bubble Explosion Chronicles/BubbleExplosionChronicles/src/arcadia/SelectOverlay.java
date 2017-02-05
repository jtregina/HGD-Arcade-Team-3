package arcadia;

import arcadia.util.*;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import java.util.List;

import static arcadia.Game.*;
import static arcadia.Button.*;

/**
 *
 */
public class SelectOverlay extends Overlay {
	private final Queue<Overlay> overlays;
	private final SelectionList<Game> games;
	private Tweener tweener;
	private final Tweener pulse;
	
	HashMap<Integer,Image> covers = new HashMap<Integer, Image>();
	
	class SinTweener extends Tweener {
		private double a;
		private double b;

		public SinTweener(double a, double b) {
			this.a = a;
			this.b = b;
		}

		public double value() {
			time %= 1.0;
			return (b - a) * (1 + Math.sin(time * 2 * Math.PI)) / 2 + a;
		}
	}
		
	public SelectOverlay(Queue<Overlay> overlays, Game... games) {
		super(true, true);
		this.overlays = overlays;
		this.games = new SelectionList<>(Arrays.asList(games));
		this.pulse = new SinTweener(0.3, 1.0);	
	}
	
	public void update(Set<Integer> pressed) {
		if(tweener != null) { 
			if(tweener.isComplete()) { tweener = null; }
			else { tweener.tick(0.05); }
		}
		else {
			GameInput input = new GameInput(pressed);
			if(input.pressed(S)) { overlays.add(new CreditsOverlay(overlays)); }
			
			if(input.pressed(Button.L) && !games.firstSelected()) { 
				games.previous(); 
				tweener = new LinearTweener(-864 - 40, 0); }
			if(input.pressed(Button.R) && !games.lastSelected()) { 
				games.next();
				tweener = new LinearTweener(+864 + 40, 0);
			}
			if(input.pressed(Button.A)) {
				pressed.remove(KeyEvent.VK_Z);
				overlays.add(new GameOverlay(overlays, games.current()));
			}
		}
	}
		
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		
		int[] xCoord = { 
			(WIDTH - 864) / 2 - 864 - 40,
			(WIDTH - 864) / 2,
			(WIDTH - 864) / 2 + 864 + 40
		};
		for(int x = 0; x <= 2; x += 1) {
			int c = games.currentIndex() - 1 + x;
			if(c < 0 || games.size() <= c) { continue; }
						
			int xpos = xCoord[x];
			if(tweener != null) { xpos += tweener.value(); }
			
			//This is bad because it resizes on the fly!  Resize moved to getCover()
			//g2d.drawImage(getCover(c), xpos, 20, xpos + 864, 20 + 486, 0, 0, 1024, 576,null);
			g2d.drawImage(getCover(c), xpos, 20, null);
		}

		pulse.tick(0.025);
		
		g2d.setColor(new Color(0.0f, 0.3f, 0.3f, 1.0f));
		g2d.setStroke(new BasicStroke(2.0f));
		//g2d.drawLine(50, HEIGHT - 35, (WIDTH - 30) / 2, HEIGHT - 35);
		//g2d.drawLine((WIDTH - 30) / 2 + 30, HEIGHT - 35, WIDTH - 50, HEIGHT - 35);
		//g2d.drawLine(WIDTH / 2, HEIGHT - 51, WIDTH / 2, HEIGHT - 70);
		g2d.setStroke(new BasicStroke(10.0f));
		g2d.draw(new RoundRectangle2D.Double((WIDTH - 864) / 2, 20, 864, 486, 10, 10));
		
		g2d.setColor(new Color(0.0f, (float)pulse.value(), (float)pulse.value(), 1.0f));
		if(games.currentIndex() == 0) { 
			g2d.setColor(new Color(0.0f, 0.3f, 0.3f, 1.0f));
		}
		L.draw(g2d,          420     , HEIGHT - 50, 30, 30);
		
		g2d.setColor(new Color(0.0f, (float)pulse.value(), (float)pulse.value(), 1.0f));
		if(games.currentIndex() == games.size() - 1) { 
			g2d.setColor(new Color(0.0f, 0.3f, 0.3f, 1.0f));
		}
		R.draw(g2d,  WIDTH - 450     , HEIGHT - 50, 30, 30);
		
		g2d.setColor(new Color(0.0f, (float)pulse.value(), (float)pulse.value(), 1.0f));
		A.draw(g2d, (WIDTH -  30) / 2, HEIGHT - 50, 30, 30);
		
		g2d.setColor(Color.CYAN);
		new SkyhookFont(20).draw(g2d, 20, HEIGHT - 45, "Arcadia");
		new SkyhookFont(20).draw(g2d, WIDTH - 160, HEIGHT - 45, "Credit");
		
		g2d.setColor(new Color(0.0f, (float)pulse.value(), (float)pulse.value(), 1.0f));
		S.draw(g2d, WIDTH - 50, HEIGHT - 50, 30, 30);
		
		/*			
		g2d.setColor(Color.WHITE);
		g2d.fillRect(358,  66, 308,   6);
		g2d.fillRect(358,  66,   6, 178);
		g2d.fillRect(358, 238, 308,   6);
		g2d.fillRect(660,  66,   6, 178);
		*/
	}
	
	private Image getCover(int index) {
		if (!covers.containsKey(index)) {
			Image image = games.get(index).cover().getScaledInstance(864, 486,0);
			covers.put(index, image);
		}
		return covers.get(index);
	}
}