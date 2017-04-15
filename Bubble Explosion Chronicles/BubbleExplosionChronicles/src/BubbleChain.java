
public class BubbleChain {

	Bubble [] chain;
	
	public BubbleChain(int initialSize, Bubble b) {
		chain = new Bubble[initialSize];
		chain[initialSize - 1] = b;
	}
	
	public void addBubble(Bubble b, int pos) {
		Bubble [] newChain = new Bubble[chain.length + 1];
		
		for (int i = 0; i < chain.length; i++) {
			newChain[i] = chain[i];
		}
		
		newChain[chain.length] = b;
	}
	
}
