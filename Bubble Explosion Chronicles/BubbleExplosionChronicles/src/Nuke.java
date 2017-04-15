import java.awt.Image;


public class Nuke {

	Image iType;
	Image bubbleType;
	int type;
	int xPos;
	int yPos;
	
	public void setImage(Image i, Image j) {
		iType = i;
		bubbleType = j;
	}
	
	public Image getImage() {
		return iType;
	}
	
	public Image getBubImage() {
		return bubbleType;
	}
	
	public void setType(int t) {
		type = t;
	}
	
	public int getType() {
		return type;
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
