import java.awt.Image;


public class Nuke {

	Image type;
	Image bubbleType;
	int xPos;
	int yPos;
	
	public void setImage(Image i, Image j) {
		type = i;
		bubbleType = j;
	}
	
	public Image getImage() {
		return type;
	}
	
	public Image getBubImage() {
		return bubbleType;
	}
	
	public void setPos(int x, int y) {
		xPos = x;
		yPos = y;
	}
	
	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
}
