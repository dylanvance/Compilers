import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.*;
import java.util.*;

public class Driver {
	
	public static class VerboseListener extends BaseErrorListener {
		@Override
		public void syntaxError(Recognizer<?, ?> recognizer,
					Object offendingSymbol,
					int line, int charPositionInLine,
					String msg,
					RecognitionException e)
		{
			List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
			Collections.reverse(stack);
			System.out.println("Not accepted");
			System.exit(0);
		}
	}
	
	public static void main(String[] args) throws Exception {
		ANTLRInputStream input = new ANTLRInputStream(System.in);
		
		LittleLexer lexer = new LittleLexer(input);

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		LittleParser parser = new LittleParser(tokens);

		// remove ConsoleErrorListener
		parser.removeErrorListeners();

		// add VerboseListener
		parser.addErrorListener(new VerboseListener());

		// Program is the start of the grammar
		parser.program();

		System.out.println("Accepted");
	}
}
