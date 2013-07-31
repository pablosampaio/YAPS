package yaps.util;

import java.util.List;

/**
 * An interface for a complete priority queue.
 * 
 * @author Pablo A. Sampaio
 */
public interface PQueue<T extends PQueueElement> {
	
	//public void init(List<T> elements);
	
	public boolean isEmpty();
	public int size();
	
	public void add(T element);
	
	public T removeMinimum();
	public void remove(T element);
	
	public T getMinimum();
	
	public void decreaseKey(T e);
	
}
