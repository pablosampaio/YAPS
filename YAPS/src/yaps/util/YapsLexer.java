package yaps.util;

import java.io.IOException;
import java.io.InputStream;



/**
 * A simple lexer created for parsing the data formats of YAPS
 * (e.g. the "YAPS Maps Format" and the "YAPS Experiment Format"). 
 *  
 * @author Pablo A. Sampaio
 */
public class YapsLexer {
	private InputStream input;
	private int nextChar = -1;
	private int line;
	
	public YapsLexer(InputStream inputStream) throws YapsParsingException {
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
			throw new YapsParsingException("I/O error: " + e.getMessage());
		}
	}
	
	public int getCurrentLine() {
		return this.line;
	}
	
	public boolean eof() {
		return this.nextChar == -1;
	}
	
	private void advanceNextChar() throws YapsParsingException {
		try {
			this.nextChar = this.input.read();
		} catch (IOException e) {
			throw new YapsParsingException("I/O error: " + e.getMessage());
		}
	}
	
	public void advanceLines() throws YapsParsingException {
		int oldLine = this.line;

		while (nextChar == ' ' || nextChar == '\t' || nextChar == '\n'  || nextChar == '\r') {
			if (nextChar == '\n') {
				this.line ++;
			}
			advanceNextChar();
		}
		
		if (this.nextChar != -1 && this.line == oldLine) {
			throw new YapsParsingException("line break", "" + (char)nextChar, this.line); 
		}
	}
	
	private void advanceSpaces() throws YapsParsingException {
		while (nextChar == ' ' || nextChar == '\t') {
			advanceNextChar();
		}
		if (nextChar == -1) {
			throw new YapsParsingException("Unexpected end of file!");
		}
	}
	
	public String readString() throws YapsParsingException {
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
			throw new YapsParsingException("No string found in line " + this.line);
		}
		
		return builder.toString();
	}
	
	public void checkString(String requiredStr) throws YapsParsingException {
		String readStr = this.readString();
		if (! readStr.equals(requiredStr)) {
			throw new YapsParsingException(requiredStr, readStr, this.line);
		}
	}
	
	public int readInteger() throws YapsParsingException {
		advanceSpaces();

		StringBuilder builder = new StringBuilder();
		while (nextChar >= '0' && nextChar <= '9') {
			builder.append((char)nextChar);
			advanceNextChar();
		}
		if (builder.length() == 0) {
			throw new YapsParsingException("No integer found in line " + this.line);
		}
		return Integer.parseInt(builder.toString());
	}

	public void checkInteger(int expectedNum) throws YapsParsingException {
		int readNum = this.readInteger();
		if (readNum != expectedNum) {
			throw new YapsParsingException(""+expectedNum, ""+readNum, this.line);
		}
	}
	
	public double readDecimal() throws YapsParsingException {
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
	
	public char readEdgeType() throws YapsParsingException {
		advanceSpaces();
		checkSymbol('-');
		
		char c = (char)nextChar;
		if (c == '-' || c == '>') {
			advanceNextChar();
			return c;
		} else {
			throw new YapsParsingException("- or >", ""+c, this.line);
		}
	}
	
	public boolean readBoolean() throws YapsParsingException {
		String str = this.readString();
		
		if (str.equalsIgnoreCase("true")) {
			return true;
		} else if (str.equalsIgnoreCase("false")) {
			return false;
		} else {
			throw new YapsParsingException("Invalid boolean value: " + str + ", in line " + this.line);
		}
	}
	
	public char readSymbol() throws YapsParsingException {
		advanceSpaces();

		char symbol = (char)this.nextChar;
		advanceNextChar();
		
		return symbol;
	}
	
	public void checkSymbol(char c) throws YapsParsingException {
		advanceSpaces();
		if (this.nextChar == c) {
			advanceNextChar();
		} else {
			 throw new YapsParsingException(c, (char)this.nextChar, this.line);
		}
	}

	public String readPathString() throws YapsParsingException {
		advanceSpaces();
		checkSymbol('\"');
		
		StringBuilder builder = new StringBuilder();
		while (nextChar != -1 && nextChar != '\n' && nextChar != '\\' && nextChar != '/' 
				&& nextChar != '*' && nextChar != '?' && nextChar != '\"' 
				&& nextChar != '<' && nextChar != '>' && nextChar != '|') {
			builder.append((char)nextChar);
			advanceNextChar();
		}
		
		if (builder.length() == 0) {
			throw new YapsParsingException("No path found in line " + this.line);
		}
		
		checkSymbol('\"');	
		return builder.toString();
	}

}

