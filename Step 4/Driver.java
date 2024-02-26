import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.*;
import java.util.*;

public class Driver {

	public static void main(String args[]) throws Exception {
		// Create a CharStream that reads from standard input
		ANTLRInputStream input = new ANTLRInputStream(System.in);

		// Create a lexer that feeds off of input CharStream
		LittleLexer lexer = new LittleLexer(input);

		// Create a buffer of tokens pulled from the lexer
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		// Create a parser that feeds off the tokens buffer
		LittleParser parser = new LittleParser(tokens);

		// Begin parsing at prog rule
		ParseTree tree = parser.program();

		// Create a generic parse tree walker that can trigger callbacks
		ParseTreeWalker walker = new ParseTreeWalker();

		// Create the parse table
		SimpleTableBuilder stb = new SimpleTableBuilder();

		// Walk the tree created during the parse, trigger callbacks
		walker.walk(stb, tree);

		// Print the symbol table
		// stb.prettyPrint();

		// Create the parse table
		TinyCodeGenerator tcg = new TinyCodeGenerator();

		// Walk the tree created during the parse, trigger callbacks
		walker.walk(tcg, tree);
	}
}
