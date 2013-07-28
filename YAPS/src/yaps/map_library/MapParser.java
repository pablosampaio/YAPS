package yaps.map_library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;


/**
 * This class parses the map information (coded in "YAPS Map Format") from a InputStream,
 * and returns a Map object. 
 * 
 * @author Pablo A. Sampaio
 */
public class MapParser {
	private String mapName;
	
	private List<String> nodeAttributes = new LinkedList<>();
	private NodeInfo[] nodes;

	private List<String> edgeAttributes = new LinkedList<>();
	private EdgeInfo[] edges;

	private boolean has2Dcoordinates;
	private boolean hasNodeImportances;
	private boolean hasLengths;
	private boolean isDirected; //obs.: a mixed graph is considered directed

	MapLexer lexer;

	public MapParser() {		
	}
	
	public Map loadFromFile(String fileName) throws MapParsingException {
		try {
			return loadFromFile(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			File f = new File(fileName);
			throw new MapParsingException("File not found: " + f.getAbsolutePath());
		}
	}
	
	public Map loadFromFile(InputStream input) throws MapParsingException {
		lexer = new MapLexer(input);
		
		parse();
		
		Map map = new Map(this.mapName);
		
		map.setGraph(this.nodes, this.edges);
		map.setProperties(this.hasLengths, this.isDirected, this.has2Dcoordinates, this.hasNodeImportances);
		
		return map;
	}
	
	private void parse() throws MapParsingException {
		lexer.checkString("mapname");	
		this.mapName = lexer.getString();

		lexer.advanceLines();
		
		parseNodes();
		parseEdges();

		//obs.: it doesn't verify for EOF
	}

	private void parseNodes() throws MapParsingException {
		lexer.checkString("nodes");
		lexer.checkChar(':');
		
		int numNodes = lexer.getInteger();
		
		if (numNodes <= 0) {
			throw new MapParsingException("Invalid number of nodes: " + numNodes);
		}
		this.nodes = new NodeInfo[numNodes];
		
		lexer.advanceLines();
		lexer.checkString("nodes-attributes");
		lexer.checkChar(':');
		
		String attr;
		char c;
		
		do {
			attr = lexer.getString();
			nodeAttributes.add(attr);
			
			if (attr.equals("importance")) {
				this.hasNodeImportances = true;	
			} else if (attr.equals("2d-position")) {
				this.has2Dcoordinates = true;	
			}
			
			c = lexer.getSymbol();
		} while (c == ',');
		
		lexer.advanceLines();

		int x, y;
		String attrValue;
		
		for (int id = 0; id < numNodes; id ++) {
			lexer.checkInteger(id);			
			lexer.checkChar(':');
			
			this.nodes[id] = new NodeInfo(id);			
		
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

	private void parseEdges() throws MapParsingException {
		lexer.checkString("edges");
		lexer.checkChar(':');
		
		int numEdges = lexer.getInteger();
		
		if (numEdges <= 0) {
			throw new MapParsingException("Invalid number of edges: " + numEdges);
		}
		this.edges = new EdgeInfo[numEdges];
		
		lexer.advanceLines();
		lexer.checkString("edges-attributes");
		lexer.checkChar(':');
		
		String attr;
		char c;
		
		do {
			attr = lexer.getString();
			edgeAttributes.add(attr);
			
			if (attr.equals("length")) {
				this.hasLengths = true;	
			}
			
			c = lexer.getSymbol();
		} while (c == ',');
		
		lexer.advanceLines();
		
		int nodeFrom, nodeTo;
		boolean directed;
		String attrValue;

		this.isDirected = false;
		
		for (int id = 0; id < numEdges; id ++) {
			lexer.checkInteger(id);
			
			nodeFrom = lexer.getInteger();
			directed = (lexer.getEdgeType() == '>');
			nodeTo = lexer.getInteger();
			
			if (directed) {
				this.isDirected = true;
			}
			
			lexer.checkChar(':');
			
			this.edges[id] = new EdgeInfo(id, directed, nodeFrom, nodeTo);
			double length;
			
			for (int a = 0; a < this.edgeAttributes.size(); a ++) {
				attrValue = this.edgeAttributes.get(a);
				
				if (attrValue.equals("label")) {
					this.edges[id].setLabel(lexer.getString()); 
				
				} else if (attrValue.equals("length")) {
					length = lexer.getDecimal();
					if (length > 0) {
						this.edges[id].setLength(length);
					} else {
						throw new MapParsingException("Invalid edge length: " + length);
					}
				
				} else {
					throw new MapParsingException("Unsupported edge attribute : " + attrValue);

				}
			}
			
			if (! this.hasLengths) {
				this.edges[id].setLength(1.0d);
			}

			lexer.advanceLines();
		}
		
	}
	
	public static void main(String[] args) throws MapParsingException {
		MapParser map = new MapParser();
		
		map.loadFromFile("map-example.ymf");
		
		System.out.println(map.mapName);
		System.out.println(map.nodeAttributes);
		for (int i = 0; i < map.nodes.length; i++) {
			System.out.println(map.nodes[i]);
		}
		for (int i = 0; i < map.edges.length; i++) {
			System.out.println(map.edges[i]);
		}
		
	}

}
