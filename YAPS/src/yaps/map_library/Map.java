package yaps.map_library;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import yaps.graph_library.Graph;


public class Map {
	private String name;
	
	private Graph topology;
	private NodeProperties[] nodes;
	private EdgeProperties[] edges;

	//criar mapeamento de labels para nos e arestas -- dificuldade: são únicos?
	//farei únicos: realismo demais não é para este simulador
	
	private boolean has2Dcoordinates;
	private boolean hasNodeImportances;
	private boolean hasLengths;
	private boolean isDirected;
	
	public Map() {		
		
	}

}
