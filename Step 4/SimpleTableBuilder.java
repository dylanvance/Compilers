import java.util.*;

public class SimpleTableBuilder extends LittleBaseListener {
	private static List<SymbolTable> tables = new ArrayList<>();
	private static Stack<SymbolTable> stack = new Stack<>();
	private static List<String> errors = new ArrayList<>();

	static int numBlocks = 0;

	public static void addVariable(String name, String type, String value) {
		SymbolTable currentTable = stack.peek();

		if (currentTable.table.containsKey(name)) {
			errors.add("DECLARATION ERROR " + name);
		} else {
			currentTable.addElement(name, type, value);
		}
	}

	public static void createTable(String name) {
		SymbolTable newTable = new SymbolTable(name);

		tables.add(newTable);
		stack.push(newTable);
	}

	public static void exitBlock() {
		stack.pop();
	}

	public static void createUnnamedTable() {
		numBlocks += 1;
		createTable("BLOCK " + numBlocks);
	}

	@Override
	public void enterProgram(LittleParser.ProgramContext ctx) {
		createTable("GLOBAL");
	}

	@Override
	public void exitProgram(LittleParser.ProgramContext ctx) {
		exitBlock();
	}

	@Override
	public void enterFunc_decl(LittleParser.Func_declContext ctx) {
		createTable(ctx.id().getText());
	}

	@Override
	public void exitFunc_decl(LittleParser.Func_declContext ctx) {
		exitBlock();
	}

	@Override
	public void enterIf_stmt(LittleParser.If_stmtContext ctx) {
		createUnnamedTable();
	}

	@Override
	public void exitIf_stmt(LittleParser.If_stmtContext ctx) {
		exitBlock();
	}

	@Override
	public void enterElse_part(LittleParser.Else_partContext ctx) {
		if (ctx.getText().contains("ELSE")) {
			createUnnamedTable();
		}
	}

	@Override
	public void exitElse_part(LittleParser.Else_partContext ctx) {
		if (ctx.getText().contains("ELSE")) {
			exitBlock();
		}
	}

	@Override
	public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {
		createUnnamedTable();
	}

	@Override
	public void exitWhile_stmt(LittleParser.While_stmtContext ctx) {
		exitBlock();
	}

	@Override
	public void enterVar_decl(LittleParser.Var_declContext ctx) {
		String name = ctx.id_list().getText();

		String[] names = name.split("[\\s,]+");

		String type = ctx.var_type().getText();

		for (String varName : names) {
			addVariable(varName, type, null);
		}
	}

	@Override
	public void enterParam_decl(LittleParser.Param_declContext ctx) {
		String name = ctx.id().getText();

		String type = ctx.var_type().getText();

		addVariable(name, type, null);
	}

	@Override
	public void enterString_decl(LittleParser.String_declContext ctx) {
		String name = ctx.id().getText();
		String type = "STRING";
		String value = ctx.str().getText();

		addVariable(name, type, value);
	}

	public void prettyPrint() {
		if (errors.isEmpty()) {
			for (int i = 0; i < tables.size(); i++) {
				SymbolTable currentSymbolTable = tables.get(i);
				System.out.println("Symbol table " + currentSymbolTable.name);

				for (int j = 0; j < currentSymbolTable.variableOrder.size(); j++) {
					String variableName = currentSymbolTable.variableOrder.get(j);

					System.out.print("name " + variableName);
					System.out.print(" type " + currentSymbolTable.table.get(variableName).getKey());

					String value = currentSymbolTable.table.get(variableName).getValue();
					if (value == null) {
						System.out.println();
					} else {
						System.out.println(" value " + value);
					}
				}

				if (i != tables.size() - 1) {
					System.out.println();
				}
			}
		}
		else {
			// for(String error: errors) {
			// 	System.out.println(error);
			// }

			System.out.println(errors.get(0));
		}
	}
}

class SymbolTable {
	public HashMap<String, AbstractMap.SimpleEntry<String, String>> table = new HashMap<>();
	public String name;
	public ArrayList<String> variableOrder = new ArrayList<>();

	SymbolTable(String inputName) {
		this.name = String.valueOf(inputName);
	}

	public void addElement(String name, String type, String value) {
		AbstractMap.SimpleEntry<String, String> mapValue = new AbstractMap.SimpleEntry<>(type, value);

		table.put(name, mapValue);

		variableOrder.add(name);
	}
}