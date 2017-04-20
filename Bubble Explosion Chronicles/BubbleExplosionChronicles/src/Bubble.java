

import java.awt.Image;

public class Bubble {

	private Image iType;
	private int type;
	private int xPos;
	private int yPos;
	private int actualPos = -5;
	private int [][] coll = new int[237][2];
	private BubbleChain chain;
	
	public void setImage(Image i) {
		iType = i;
	}
	
	public Image getImage() {
		return iType;
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
		
		setColl();
	}
	
	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
	
	public int getActualPos() {
		return actualPos;
	}
	
	public void setActualPos(int p) {
		actualPos = p;
	}
	
	private void setColl() {
		for (int i = 0; i < 60; i++) {
			coll[i][0] = xPos + 4 + i;
			coll[i][1] = yPos + 5;
		}
		
		for (int i = 60; i < 117; i++) {
			coll[i][0] = xPos + 5;
			coll[i][1] = yPos + 5 + i - 60;
		}
		
		for (int i = 120; i < 177; i++) {
			coll[i][0] = xPos + 63;
			coll[i][1] = yPos + 5 + i - 120;
		}
		
		for (int i = 177; i < 237; i++) {
			coll[i][0] = xPos + 4 + i - 177;
			coll[i][1] = yPos + 62;
		}
	}
	
	public int [][] getColl() {
		return coll;
	}
	
	public BubbleChain getBubbleChain() {
		return chain;
	}
	
	public void setBubbleChain(BubbleChain b) {
		chain = b;
	}
}
