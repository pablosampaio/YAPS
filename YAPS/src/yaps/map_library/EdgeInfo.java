package yaps.map_library;

/**
 * Class that holds information about the edge in the simulador, including dynamic 
 * information set during simulation.
 *  
 * @author Pablo A. Sampaio
 */
public class EdgeInfo {
	public final int index;
	
	public final int nodeA;
	public final int nodeB;
	public final boolean directed;
	
	private double length;
	private String label;	

	
	public EdgeInfo(int i, boolean directed, int nodeFrom, int nodeTo) {
		this.index = i;
		this.directed = directed;
		this.nodeA = nodeFrom;
		this.nodeB = nodeTo;
	}
	
	public String getLabel() {
		return label;
	}

	void setLabel(String label) {
		this.label = label;
	}

	public double getLength() {
		return length;
	}

	void setLength(double length) {
		this.length = length;
	}

	public String toString() {
		return String.format("{edge %d (%s): %d-%s%d }", index, label, nodeA, directed?">":"-", nodeB);		
	}
	
}
