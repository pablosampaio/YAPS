package yaps.map_library;

import yaps.graph_library.Graph;


public class Map {
	private Graph topology;
	private NodeProperties[] nodes;
	private EdgeProperties[] edges;

	//criar mapeamento de labels para nos e arestas -- dificuldade: são únicos?
	//farei únicos: realismo demais não é para este simulador
	
	private boolean has2Dpositions;
	private boolean hasNodeImportances;

	Map() {
		
	}
	
	
}
