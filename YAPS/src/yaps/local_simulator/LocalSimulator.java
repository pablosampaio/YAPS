package yaps.local_simulator;

import java.util.List;


/**
 * A turn-based local simulator.
 *  
 * @author Pablo A. Sampaio
 */
public class LocalSimulator extends Thread {
	private int currentTurn;
	private int totalTurns;
	
	private List<AgentInfo> agents;

	@Override
	public void run() {		
		
		// processar mensagens
		// para cada ação, enviar mensagem de status para o agente ?
		
	}

}


class AgentInfo {
	String name;
	int id;
	
	int nodeId;
	int edgeId;
	int walkedDistance;
	
	List<String> messagesToSimulator;
}