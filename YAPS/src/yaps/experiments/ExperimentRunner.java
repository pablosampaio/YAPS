package yaps.experiments;

import java.util.List;

import yaps.agent_library.core.SimulatorConnector;


public class ExperimentRunner {
	
	//tem que receber uma lista de algoritmos também...
	public static void simulateAll(ExperimentSetup experiment, SimulatorConnector simConnector) {
		
		for (String map : experiment.getMaps()) {
			List<SimulationInfo> simulations = experiment.getSimulationList(map);
			
			for (SimulationInfo sim : simulations) {	
				System.out.println(sim);
			}
		}
		
	}

}
