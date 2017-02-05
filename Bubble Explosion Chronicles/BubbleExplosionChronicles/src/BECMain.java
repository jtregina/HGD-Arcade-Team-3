import static arcadia.Game.HEIGHT;
import static arcadia.Game.WIDTH;

import arcadia.*;
import arcadia.Button;
import arcadia.Input;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BECMain extends Game {

    private Image bg;
    private Image nukeDukem;
    private int nDX, nDY;

    public BECMain() {
       try {
    	   bg = ImageIO.read(new File("src/images/backgrounds/random.png"));
    	   nukeDukem = ImageIO.read(new File("src/images/NukeDukemSprite.png"));
       } catch (IOException e) {
    	   System.out.println("Hello");
       }
       
       nDX = 240;
       nDY = 20;
       
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
		graphics.drawImage(bg, 0, 0, WIDTH, HEIGHT, null);
		graphics.drawImage(nukeDukem, nDX, nDY, 120, 160, null);
		
		if (nDX > 0) if (input.pressed(Button.L)) nDX -= 4;
		if (nDX < 480) if (input.pressed(Button.R)) nDX += 4;
	}
}