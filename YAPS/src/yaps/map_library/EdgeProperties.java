package yaps.map_library;

public class EdgeProperties {
	public final int index;
	public final String label;
	
	private String informationMark;
	
	public EdgeProperties(int i, String lbl) {
		this.index = i;
		this.label = lbl;
	}

	public String getInformationMark() {
		return informationMark;
	}

	public void setInformationMark(String informationMark) {
		this.informationMark = informationMark;
	}
	
}
