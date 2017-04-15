

import java.awt.Image;

public class Bubble {

	private Image iType;
	private int type;
	private int xPos;
	private int yPos;
	private int [][] coll = new int[58][2];
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
	
	private void setColl() {
		for (int i = 0; i < 58; i++) {
			coll[i][0] = xPos + 5 + i;
			coll[i][1] = yPos + 5;
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
