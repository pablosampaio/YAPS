package yaps.experiments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import yaps.util.YapsLexer;
import yaps.util.YapsParsingException;


public class ExperimentSetupParser {
	private YapsLexer lexer;
	private ExperimentSetup experiment;
	
	public ExperimentSetupParser() {
	}

	// colocar dentro da classe experiment e tornar experiment "de pacote?"
	public ExperimentSetup loadFromFile(String fileName) throws YapsParsingException {
		try {
			return loadFromFile(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			File f = new File(fileName);
			throw new YapsParsingException("File not found: " + f.getAbsolutePath());
		}
	}
	
	public ExperimentSetup loadFromFile(InputStream input) throws YapsParsingException {
		this.lexer = new YapsLexer(input);
		
		parse();
		
		return this.experiment;
	}
	
	private void parse() throws YapsParsingException {
		parseBasicInfo();

		do {
			parseMapSimulations();
			lexer.advanceLines();
		} while (!lexer.eof() && lexer.readString().equals("map"));
		
		//accepts "garbage" text in the end 
	}

	private void parseBasicInfo() throws YapsParsingException {
		lexer.checkString("experiment");
		experiment = new ExperimentSetup(lexer.readString());
		
		lexer.advanceLines();
		
		lexer.checkString("fulltime");
		experiment.setFullTime(lexer.readInteger());
		lexer.advanceLines();

		String token = lexer.readString();
		
		if (token.equals("eval-init")) {
			experiment.setEvaluationStart(lexer.readInteger());
			lexer.advanceLines();
			token = lexer.readString();
		} else {
			experiment.setEvaluationStart(0);
		}
		
		if (token.equals("repeated-executions")) {
			experiment.setRepeatedExecutions(lexer.readInteger());
			lexer.advanceLines();
			token = lexer.readString();
		} else {
			experiment.setRepeatedExecutions(1);
		}
		
		if (token.equals("maps-directory")) {
			File mapsDir = new File(lexer.readPathString());
			if (!mapsDir.isDirectory()) {
				throw new YapsParsingException("Not a directory: " + mapsDir);
			}
			experiment.setMapsDirectory(mapsDir);
			lexer.advanceLines();
			token = lexer.readString();
		} else {
			experiment.setMapsDirectory(new File("."));
		}
		
		if (! token.equals("map")) {
			throw new YapsParsingException("map", token, lexer.getCurrentLine());
		}		
	}

	private void parseMapSimulations() throws YapsParsingException {
		String filename;
		char c;
		File mapFile;
		
		filename = lexer.readPathString();

		mapFile = new File(experiment.getMapDirectory(), filename);
		if (! mapFile.exists()) {
			throw new YapsParsingException("Map file not found: " + mapFile);
		}
		lexer.advanceLines();

		lexer.checkString("map-societies");
		lexer.checkSymbol(':');
		
		List<Integer> societiesSizes = new LinkedList<>();
		do {
			societiesSizes.add(lexer.readInteger());
			c = lexer.readSymbol();
		} while (c == ',');
		
		lexer.advanceLines();
		
		AgentInitialInfo[] agents;

		for (Integer socSize : societiesSizes) {
			agents = new AgentInitialInfo[socSize];
			for (int i = 0; i < socSize; i++) {
				agents[i] = new AgentInitialInfo(lexer.readString(), lexer.readInteger());
			}
			experiment.addSimulation(new SimulationInfo(mapFile, agents));
			
			lexer.advanceLines();
		}

	}
	
	public static void main(String[] args) throws YapsParsingException {
		ExperimentSetupParser parser = new ExperimentSetupParser();
		
		ExperimentSetup experiment = parser.loadFromFile("experiment-example.yef");

		for (String map : experiment.getMaps()) {
			System.out.println("MAP: " + map);
			List<SimulationInfo> simulations = experiment.getSimulationList(map);
			for (SimulationInfo sim : simulations) {
				System.out.println(sim);
			}
		}
		
	}
	
}
