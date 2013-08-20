package yaps.experiments;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class ExperimentSetup {
	private final String name;
	
	private int fulltime;
	private int evalInit;
	
	private int repeatedExecs;	
	private File mapDirectory;
	
	private HashMap<String,List<SimulationInfo>> simInfo; //map name -> simulation setups for the map
	
	public ExperimentSetup(String name) {
		this.name = name;
		this.simInfo = new HashMap<>();
	}

	public String getName() {
		return name;
	}
	
	public int getFullTime() {
		return fulltime;
	}

	protected void setFullTime(int fulltime) {
		this.fulltime = fulltime;
	}

	public int getStartEvaluation() {
		return evalInit;
	}

	protected void setEvaluationStart(int evalInit) {
		this.evalInit = evalInit;
	}

	public int getRepeatedExecutions() {
		return repeatedExecs;
	}

	protected void setRepeatedExecutions(int repeatedExecs) {
		this.repeatedExecs = repeatedExecs;
	}

	public File getMapDirectory() {
		return mapDirectory;
	}

	protected void setMapsDirectory(File directory) {
		this.mapDirectory = directory;
	}
	
	public void addSimulation(SimulationInfo sim) {
		List<SimulationInfo> list = this.simInfo.get(sim.getMap().getName()); 
		if (list == null) {
			list = new LinkedList<SimulationInfo>();
			this.simInfo.put(sim.getMap().getName(), list);
		}
		list.add(sim);
	}

	public Set<String> getMaps() {
		return this.simInfo.keySet();
	}
	
	public List<SimulationInfo> getSimulationList(String map) {
		return this.simInfo.get(map);
	}
	
}
