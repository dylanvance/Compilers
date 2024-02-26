/*
 * How to run:
 * 	1. javac Little*.java Driver.java
 *	2. java Driver
 *	3. Now paste in one of the example programs
 *	4. Press Ctrl+D to let it know to stop reading
 *	5. Done. It will print out the output
*/
import org.antlr.v4.runtime.*;

public class Driver {
	public static void main(String[] args) throws Exception {
		// Create a CharStream that reads from standard input
		ANTLRInputStream input = new ANTLRInputStream(System.in);

		// Create a lexer that feeds off of input CharStream
		Little lexer = new Little(input);
		
		Token token;
		token = lexer.nextToken();

		while (!lexer._hitEOF) {
			// The token type is an integer
			// It's based on the order of the types written in the g4 file
			// PLEASE DON'T CHANGE THE ORDER IN Little.g4
			switch (token.getType()) {
				case 1:
					// Comments (skipped)
					break;
				case 2:
					System.out.println("Token Type: KEYWORD");
					break;
				case 3:
					System.out.println("Token Type: STRINGLITERAL");
					break;
				case 4:
					System.out.println("Token Type: IDENTIFIER");
					break;
				case 5:
					System.out.println("Token Type: OPERATOR");
					break;
				case 6:
					System.out.println("Token Type: FLOATLITERAL");
					break;
				case 7:
					System.out.println("Token Type: INTLITERAL");
					break;
				case 8:
					// Whitespace (skipped)
					break;
				default:
					//This should never execute
					break;
			}
			System.out.println("Value: " + token.getText());
			token = lexer.nextToken();
		}
	}
}
