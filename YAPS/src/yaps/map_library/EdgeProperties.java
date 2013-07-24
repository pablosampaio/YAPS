package yaps.map_library;


public class EdgeProperties {
	public final int index;
	
	public final int nodeA;
	public final int nodeB;
	public final boolean directed;
	
	private double length;
	private String label;	
	
	private String informationMark;
	
	public EdgeProperties(int i, boolean directed, int nodeFrom, int nodeTo) {
		this.index = i;
		this.directed = directed;
		this.nodeA = nodeFrom;
		this.nodeB = nodeTo;
	}

	public String getInformationMark() {
		return informationMark;
	}

	public void setInformationMark(String informationMark) {
		this.informationMark = informationMark;
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
