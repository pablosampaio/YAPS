package tests;

import yaps.agent_library.core.SimulatorConnector;


public class Test1 {

	public static void main(String[] args) {
		
		SimulatorConnector connector = null;
		
		connector.setupSimulation();
		
		connector.startSimulation();
		
		connector.addAgent(null);
		connector.addAgent(null);
		
		connector.waitEndOfSimulation();
	
		connector.getVisitsList();
	}

}
