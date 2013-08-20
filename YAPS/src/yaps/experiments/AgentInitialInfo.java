package yaps.experiments;

public class AgentInitialInfo {
	private String name;
	private int node;
	
	public AgentInitialInfo(String agentName, int startNode) {
		this.name = agentName;
		this.node = startNode;
	}
	
	public String getName() {
		return name;
	}
	
	public int getStartNode() {
		return node;
	}
	
	public String toString() {
		return name + "/" + node;
	}
}
