

import java.awt.Image;

public class Bubble {

	Image type;
	int xPos;
	int yPos;
	int [][] coll = new int[58][2];
	
	public void setImage(Image i) {
		type = i;
	}
	
	public Image getImage() {
		return type;
	}
	
	public void setPos(int x, int y) {
		xPos = x;
		yPos = y;
		
		setColl();
	}
	
	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
	
	private void setColl() {
		for (int i = 0; i < 58; i++) {
			coll[i][0] = xPos + 5 + i;
			coll[i][1] = yPos + 5;
		}
	}
	
	public int [][] getColl() {
		return coll;
	}
}
