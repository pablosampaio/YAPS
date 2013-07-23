package yaps.agent_library.core;

import yaps.metrics.core.VisitsList;


public abstract class SimulatorConnector {
	
	//criar uma thread "AgentManager" (se for remoto) -- isso fica na subclasse
	//na simula��o local direta (sem uso de rede), adicionar agente no simulador
	//se necess�rio, cria uma "CallbackManager"
	
	public SimulatorConnector() {
		
	}
	
	//TODO: passar parametros: grafo, tempo de dura��o, quantidade de agentes
	public abstract void setupSimulation();

	//TODO: agente deve ter informa��es de percep��es requeridas
	// no caso local, n�o passar o objeto do agente diretamente para o simulador, apenas
	// registrar receber as notifica��es por um id inteiro
	public abstract void addAgent(Agent a);	

	//a partir daqui, n�o pode mais adicionar
	public abstract void startSimulation();
	
	public abstract void waitEndOfSimulation();

	public abstract VisitsList getVisitsList();

}
