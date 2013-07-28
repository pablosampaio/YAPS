package yaps.map_library;

import java.io.IOException;
import java.io.InputStream;


/**
 * A simple lexer created for parsing the "YAPS Map Format". 
 *  
 * @author Pablo A. Sampaio
 */
public class MapLexer {
	private InputStream input;
	private int nextChar = -1;
	private int line;
	
	MapLexer(InputStream inputStream) throws MapParsingException {
		try {
			this.input = inputStream;
			this.nextChar = this.input.read();
			this.line = 1;
			
			//advance spaces and lines (but doesn't obligate a new line)
			while (nextChar == ' ' || nextChar == '\t' || nextChar == '\n'  || nextChar == '\r') {
				if (nextChar == '\n') {
					this.line ++;
				}
				this.nextChar = this.input.read();
			}
		} catch (IOException e) {
			throw new MapParsingException("I/O error: " + e.getMessage());
		}
	}
	
	private void advanceNextChar() throws MapParsingException {
		try {
			this.nextChar = this.input.read();
		} catch (IOException e) {
			throw new MapParsingException("I/O error: " + e.getMessage());
		}
	}
	
	void advanceLines() throws MapParsingException {
		int oldLine = this.line;

		while (nextChar == ' ' || nextChar == '\t' || nextChar == '\n'  || nextChar == '\r') {
			if (nextChar == '\n') {
				this.line ++;
			}
			advanceNextChar();
		}
		
		if (this.line == oldLine) {
			throw new MapParsingException("line break", "" + (char)nextChar, this.line); 
		}		
	}
	
	private void advanceSpaces() throws MapParsingException {
		while (nextChar == ' ' || nextChar == '\t') {
			advanceNextChar();
		}
		if (nextChar == -1) {
			throw new MapParsingException("Unexpected end of file!");
		}
	}
	
	String getString() throws MapParsingException {
		advanceSpaces();
		
		StringBuilder builder = new StringBuilder();
		while (nextChar >= 'a' && nextChar <= 'z'
				|| nextChar >= 'A' && nextChar <= 'Z'
				|| nextChar == '-' || nextChar == '_') {
			builder.append((char)nextChar);
			advanceNextChar();
		}
		while (nextChar >= '0' && nextChar <= '9'
				|| nextChar >= 'a' && nextChar <= 'z'
				|| nextChar >= 'A' && nextChar <= 'Z'
				|| nextChar == '-' || nextChar == '_') {
			builder.append((char)nextChar);
			advanceNextChar();
		}
		
		if (builder.length() == 0) {
			throw new MapParsingException("No string found in line " + this.line);
		}
		
		return builder.toString();
	}
	
	void checkString(String requiredStr) throws MapParsingException {
		String readStr = this.getString();
		if (! readStr.equals(requiredStr)) {
			throw new MapParsingException(requiredStr, readStr, this.line);
		}
	}
	
	int getInteger() throws MapParsingException {
		advanceSpaces();

		StringBuilder builder = new StringBuilder();
		while (nextChar >= '0' && nextChar <= '9') {
			builder.append((char)nextChar);
			advanceNextChar();
		}
		if (builder.length() == 0) {
			throw new MapParsingException("No integer found in line " + this.line);
		}
		return Integer.parseInt(builder.toString());
	}

	void checkInteger(int expectedNum) throws MapParsingException {
		int readNum = this.getInteger();
		if (readNum != expectedNum) {
			throw new MapParsingException(""+expectedNum, ""+readNum, this.line);
		}
	}
	
	public double getDecimal() throws MapParsingException {
		advanceSpaces();

		StringBuilder builder = new StringBuilder();
		while (nextChar >= '0' && nextChar <= '9') {
			builder.append((char)nextChar);
			advanceNextChar();
		}
		
		if (nextChar == '.') {
			do {
				builder.append((char)nextChar);
				advanceNextChar();
			} while (nextChar >= '0' && nextChar <= '9');
		}
		
		return Double.parseDouble(builder.toString());
	}
	
	public char getEdgeType() throws MapParsingException {
		advanceSpaces();
		checkChar('-');
		
		char c = (char)nextChar;
		if (c == '-' || c == '>') {
			advanceNextChar();
			return c;
		} else {
			throw new MapParsingException("- or >", ""+c, this.line);
		}
	}
	
	boolean getBoolean() throws MapParsingException {
		String str = this.getString();
		
		if (str.equalsIgnoreCase("true")) {
			return true;
		} else if (str.equalsIgnoreCase("false")) {
			return false;
		} else {
			throw new MapParsingException("Invalid boolean value: " + str + ", in line " + this.line);
		}
	}
	
	char getSymbol() throws MapParsingException {
		advanceSpaces();

		char symbol = (char)this.nextChar;
		advanceNextChar();
		
		return symbol;
	}
	
	void checkChar(char c) throws MapParsingException {
		advanceSpaces();
		if (this.nextChar == c) {
			advanceNextChar();
		} else {
			 throw new MapParsingException(c, (char)this.nextChar, this.line);
		}
	}

}


@SuppressWarnings("serial")
class MapParsingException extends Exception {
	public MapParsingException(String str) {
		super(str);
	}
	public MapParsingException(String expected, String found, int line) {
		super("Expected \"" + expected + "\" but found \"" + found 
				+ "\" in line " + line);
	}
	public MapParsingException(char expected, char found, int line) {
		super("Expected \"" + expected + "\" but found \"" + found 
				+ "\" in line " + line);
	}
}

