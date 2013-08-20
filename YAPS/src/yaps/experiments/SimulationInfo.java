package yaps.experiments;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class SimulationInfo {
	private File mapFile;
	private AgentInitialInfo[] agents;
	
	protected SimulationInfo(File mapFile, AgentInitialInfo[] agentsInfo) {
		this.mapFile = mapFile;		
		this.agents = agentsInfo;
	}
	
	public File getMap() {
		return this.mapFile;
	}
	
	public int getNumAgent() {
		return this.agents.length;
	}
	
	public AgentInitialInfo[] getAgents() {
		return this.agents;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[ ");
		for (int i = 0; i < agents.length; i++) {
			builder.append(agents[i]);
			builder.append(' ');
		}
		builder.append(']');		
		return builder.toString();
	}
}
