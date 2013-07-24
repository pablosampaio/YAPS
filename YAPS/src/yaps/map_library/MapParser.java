package yaps.map_library;

import java.util.LinkedList;
import java.util.List;


public class MapParser {
	private String name;
	
	private int numNodes;
	private List<String> nodeAttributes = new LinkedList<>();
	private NodeProperties[] nodes;

	private int numEdges;
	private List<String> edgeAttributes = new LinkedList<>();
	private EdgeProperties[] edges;

	private boolean has2Dcoordinates;
	private boolean hasNodeImportances;
	private boolean hasLengths;
	private boolean isDirected;

	MapLexer lexer;

	public MapParser() {		
	}
	
	public void loadFromFile(String fileName) throws MapParsingException {
		lexer = new MapLexer(fileName);
		
		lexer.checkString("mapname");	
		this.name = lexer.getString();

		lexer.advanceLines();
		
		readNodes();
		
		readEdges();
		
		//verificar EOF?
		
		//retornar um grafo??
	}
	
	private void readNodes() throws MapParsingException {
		lexer.checkString("nodes");
		lexer.checkChar(':');
		
		this.numNodes = lexer.getInteger();
		if (this.numNodes <= 0) {
			throw new MapParsingException("Invalid number of nodes: " + this.numNodes);
		}
		this.nodes = new NodeProperties[this.numNodes];
		
		lexer.advanceLines();
		lexer.checkString("nodes-attributes");
		lexer.checkChar(':');
		
		String attr;
		char c;
		
		do {
			attr = lexer.getString();
			nodeAttributes.add(attr);
			c = lexer.getSymbol();
		} while (c == ',');
		
		lexer.advanceLines();

		int x, y;
		String attrValue;
		
		for (int id = 0; id < this.numNodes; id ++) {
			lexer.checkInteger(id);			
			lexer.checkChar(':');
			
			this.nodes[id] = new NodeProperties(id);			
		
			for (int a = 0; a < this.nodeAttributes.size(); a ++) {
				attrValue = this.nodeAttributes.get(a);
				
				if (attrValue.equals("label")) {
					this.nodes[id].setLabel(lexer.getString()); 
				
				} else if (attrValue.equals("importance")) {
					this.nodes[id].setImportance(lexer.getDecimal()); 
				
				} else if (attrValue.equals("2d-position")) {
					lexer.checkChar('(');
					x = lexer.getInteger();
					lexer.checkChar(',');
					y = lexer.getInteger();
					lexer.checkChar(')');
					this.nodes[id].set2Dposition(x, y);
					
				} else {
					throw new MapParsingException("Unsupported node attribute : " + attrValue);

				}
			}
			
			lexer.advanceLines();
		}

	}

	private void readEdges() throws MapParsingException {
		lexer.checkString("edges");
		lexer.checkChar(':');
		
		this.numEdges = lexer.getInteger();
		if (this.numEdges <= 0) {
			throw new MapParsingException("Invalid number of edges: " + this.numEdges);
		}
		this.edges = new EdgeProperties[this.numEdges];
		
		lexer.advanceLines();
		lexer.checkString("edges-attributes");
		lexer.checkChar(':');
		
		String attr;
		char c;
		
		do {
			attr = lexer.getString();
			edgeAttributes.add(attr);
			c = lexer.getSymbol();
		} while (c == ',');
		
		lexer.advanceLines();
		
		int nodeFrom, nodeTo;
		boolean directed;
		String attrValue;

		for (int id = 0; id < this.numEdges; id ++) {
			lexer.checkInteger(id);
			
			nodeFrom = lexer.getInteger();
			directed = (lexer.getEdgeType() == '>');
			nodeTo = lexer.getInteger();
			
			lexer.checkChar(':');
			
			this.edges[id] = new EdgeProperties(id, directed, nodeFrom, nodeTo);
			
			for (int a = 0; a < this.edgeAttributes.size(); a ++) {
				attrValue = this.edgeAttributes.get(a);
				
				if (attrValue.equals("label")) {
					this.edges[id].setLabel(lexer.getString()); 
				
				} else if (attrValue.equals("length")) {
					this.edges[id].setLength(lexer.getDecimal()); 
				
				} else {
					throw new MapParsingException("Unsupported edge attribute : " + attrValue);

				}
			}

			lexer.advanceLines();
		}
		
	}
	
	public static void main(String[] args) throws MapParsingException {
		MapParser map = new MapParser();
		
		map.loadFromFile("map-example");
		
		System.out.println(map.name);
		System.out.println(map.nodeAttributes);
		for (int i = 0; i < map.numNodes; i++) {
			System.out.println(map.nodes[i]);
		}
		for (int i = 0; i < map.numEdges; i++) {
			System.out.println(map.edges[i]);
		}	
	}
	
}
