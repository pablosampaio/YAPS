package yaps.util;


/**
 * An element of a priority queue.
 * 
 * @author Pablo A. Sampaio
 */
public abstract class PQueueElement {
	private int index;
	
	public PQueueElement() {
		index = -1;
	}
	
	// recebe a posi��o no array
	void setIndex(int i) {
		index = i;
	}
	
	// retorna a posi��o no array
	int getIndex() {
		return index;
	}
	
	public abstract int getKey();
	
}
