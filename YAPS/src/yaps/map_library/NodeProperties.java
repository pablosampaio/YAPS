package yaps.map_library;

public class NodeProperties {
	public final int index;
	public final String label;
	public final int pos_x;
	public final int pos_y;
	
	private double importance;
	private String informationMark;
	//add other properties here... 
	//if it may change during simulation, make them private and use get/set
	
	NodeProperties(int i, String lbl) {
		this.index = i;
		this.label = lbl;
		this.pos_x = i % 10;
		this.pos_y = i / 10;
	}

	NodeProperties(int i, String lbl, int x, int y) {
		this.index = i;
		this.label = lbl;
		this.pos_x = x;
		this.pos_y = y;
	}

	public double getImportance() {
		return importance;
	}

	public void setImportance(double importance) {
		assert(importance >= 0);
		this.importance = importance;
	}

	public String getInformationMark() {
		return informationMark;
	}

	public void setInformationMark(String informationMark) {
		this.informationMark = informationMark;
	}
	
}
