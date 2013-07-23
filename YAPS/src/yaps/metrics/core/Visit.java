package yaps.metrics.core;

/**
 * Keeps information about a single visit (done by an agent to a node in a specific time).
 * 
 * @author Pablo A. Sampaio
 */
public class Visit {
	public final long time;  //turn (in turn-based) or timestamp in millisecondss (in realtime)
	public final int agent;
	public final int node;
	
	Visit(long t, int vertex, int agent) {
		this.time = t;
		this.node = vertex;
		this.agent = agent;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(time); builder.append(' ');
		builder.append(agent); builder.append(' ');
		builder.append(node);
		
		return builder.toString();
	}
	
}

