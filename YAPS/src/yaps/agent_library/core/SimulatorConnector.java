package yaps.agent_library.core;

import yaps.metrics.core.VisitsList;


public abstract class SimulatorConnector {
	
	//criar uma thread "AgentManager" (se for remoto) -- isso fica na subclasse
	//na simulação local direta (sem uso de rede), adicionar agente no simulador
	//se necessário, cria uma "CallbackManager"
	
	public SimulatorConnector() {
		
	}
	
	//TODO: passar parametros: grafo, tempo de duração, quantidade de agentes
	public abstract void setupSimulation();

	//TODO: agente deve ter informações de percepções requeridas
	// no caso local, não passar o objeto do agente diretamente para o simulador, apenas
	// registrar receber as notificações por um id inteiro
	public abstract void addAgent(Agent a);	

	//a partir daqui, não pode mais adicionar
	public abstract void startSimulation();
	
	public abstract void waitEndOfSimulation();

	public abstract VisitsList getVisitsList();

}
