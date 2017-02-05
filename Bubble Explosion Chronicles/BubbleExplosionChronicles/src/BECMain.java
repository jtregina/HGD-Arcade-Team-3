import static arcadia.Game.HEIGHT;
import static arcadia.Game.WIDTH;
import arcadia.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BECMain extends Game {

    private Image nukeDukem;

    public BECMain() {
       try {
    	   nukeDukem = ImageIO.read(new File("src/random.png"));
       } catch (IOException e) {
    	   System.out.println("Hello");
       }
        
        reset();    
    }
    
    public void reset() {

    }
    
    public Image banner() { 
        return null;
    }

    public static void main(String[] args) {
        Arcadia.display(new Arcadia(new BECMain()));
    }

	@Override
	public Image cover() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tick(Graphics2D graphics, Input input, Sound sound) {
		graphics.drawImage(nukeDukem, 0, 0, WIDTH, HEIGHT, null);
	}
}