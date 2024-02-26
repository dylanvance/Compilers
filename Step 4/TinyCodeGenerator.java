import java.util.*;

public class TinyCodeGenerator extends LittleBaseListener {
	int registerCount = 0;
	HashMap<String, TinyVariable> variables = new HashMap<>();
	HashMap<String, Integer> registers = new HashMap<>();

	/**
	 * Declare number vars
	 */
	@Override
	public void enterVar_decl(LittleParser.Var_declContext ctx) {
		String type = ctx.var_type().getText();
		String id = ctx.id_list().id().getText();

		System.out.println("var " + id);

		variables.put(id, new TinyVariable(type, id));

		LittleParser.Id_tailContext nextVariable = ctx.id_list().id_tail();

		while (nextVariable.id() != null) {
			String nextID = nextVariable.id().getText();

			System.out.println("var " + nextID);

			variables.put(nextID, new TinyVariable(type, nextID));

			nextVariable = nextVariable.id_tail();
		}
	}

	/**
	 * Declare and define string
	 */
	@Override
	public void enterString_decl(LittleParser.String_declContext ctx) {
		String id = ctx.id().getText();
		System.out.println("str " + id + " " + ctx.str().getText());

		variables.put(id, new TinyVariable("STRING", id));
	}

	/**
	 * Assign variable to expression or number
	 */
	@Override
	public void enterAssign_expr(LittleParser.Assign_exprContext ctx) {
		String id = ctx.id().getText();
		TinyVariable setVar = variables.get(id);

		LittleParser.ExprContext mainExpr = ctx.expr();

		while(mainExpr.getText().contains("(")) {
			mainExpr = mainExpr.factor().postfix_expr().primary().expr();
		}

		LittleParser.FactorContext addCheck = mainExpr.getText().contains("(") ? mainExpr.factor().postfix_expr().primary().expr().expr_prefix().factor() : mainExpr.expr_prefix().factor();
		LittleParser.Postfix_exprContext multCheck = mainExpr.getText().contains("(") ? mainExpr.factor().postfix_expr().primary().expr().factor().factor_prefix().postfix_expr() : mainExpr.factor().factor_prefix().postfix_expr();

		if (addCheck != null) {
			// Addition
			LittleParser.ExprContext expression = mainExpr;

			if(expression.getText().contains("(")) {
				expression = expression.factor().postfix_expr().primary().expr();
			}

			String addop = expression.expr_prefix().addop().getText();

			LittleParser.PrimaryContext leftVar = expression.expr_prefix().factor().postfix_expr().primary();
			LittleParser.PrimaryContext rightVar = expression.factor().postfix_expr().primary();

			boolean leftIsVar = leftVar.id() != null;
			boolean rightIsVar = rightVar.id() != null;

			Integer leftRegister = null;
			Integer rightRegister = null;

			// Load left register
			if (leftIsVar) {
				String leftVarName = leftVar.id().getText();

				leftRegister = registers.get(leftVarName);

				if (leftRegister == null) {
					System.out.println("move " + leftVarName + " r" + registerCount);
					registers.put(leftVarName, registerCount);
					registerCount++;

					leftRegister = registers.get(leftVarName);
				}
			} else {
				System.out.println("move " + leftVar.getText() + " r" + registerCount);
				leftRegister = registerCount;
				registerCount++;
			}

			// Load right register
			if (rightIsVar) {
				String rightVarName = rightVar.id().getText();

				rightRegister = registers.get(rightVarName);

				if (rightRegister == null) {
					System.out.println("move " + rightVarName + " r" + registerCount);
					registers.put(rightVarName, registerCount);
					registerCount++;

					rightRegister = registers.get(rightVarName);
				}
			} else {
				System.out.println("move " + rightVar.getText() + " r" + registerCount);
				rightRegister = registerCount;
				registerCount++;
			}

			// Add registers
			if (addop.equals("+")) {
				System.out.println("add" + setVar.typeShort + " r" + leftRegister + " r" + rightRegister);
				System.out.println("move r" + rightRegister + " " + setVar.id);
				if (rightIsVar) {
					registers.remove(rightVar.id().getText());
				}

				registers.put(setVar.id, rightRegister);
			} else {
				System.out.println("sub" + setVar.typeShort + " r" + rightRegister + " r" + leftRegister);
				System.out.println("move r" + leftRegister + " " + setVar.id);
				if (leftIsVar) {
					registers.remove(leftVar.id().getText());
				}

				registers.put(setVar.id, leftRegister);
			}
		} else if (multCheck != null) {
			// Multiplication
			LittleParser.FactorContext expression = mainExpr.factor();

			if(expression.getText().contains("(")) {
				expression = expression.postfix_expr().primary().expr().factor();
			}

			String mulop = expression.factor_prefix().mulop().getText();

			LittleParser.PrimaryContext leftVar = expression.factor_prefix().postfix_expr().primary();
			LittleParser.PrimaryContext rightVar = expression.postfix_expr().primary();

			boolean leftIsVar = leftVar.id() != null;
			boolean rightIsVar = rightVar.id() != null;

			Integer leftRegister = null;
			Integer rightRegister = null;

			// Load left register
			if (leftIsVar) {
				String leftVarName = leftVar.id().getText();

				leftRegister = registers.get(leftVarName);

				if (leftRegister == null) {
					System.out.println("move " + leftVarName + " r" + registerCount);
					registers.put(leftVarName, registerCount);
					registerCount++;

					leftRegister = registers.get(leftVarName);
				}
			} else {
				System.out.println("move " + leftVar.getText() + " r" + registerCount);
				leftRegister = registerCount;
				registerCount++;
			}

			// Load right register
			if (rightIsVar) {
				String rightVarName = rightVar.id().getText();

				rightRegister = registers.get(rightVarName);

				if (rightRegister == null) {
					System.out.println("move " + rightVarName + " r" + registerCount);
					registers.put(rightVarName, registerCount);
					registerCount++;

					rightRegister = registers.get(rightVarName);
				}
			} else {
				System.out.println("move " + rightVar.getText() + " r" + registerCount);
				rightRegister = registerCount;
				registerCount++;
			}

			// Multiply registers
			if (mulop.equals("*")) {
				System.out.println("mul" + setVar.typeShort + " r" + leftRegister + " r" + rightRegister);
				System.out.println("move r" + rightRegister + " " + setVar.id);
				if (rightIsVar) {
					registers.remove(rightVar.id().getText());
				}

				registers.put(setVar.id, rightRegister);
			} else {
				System.out.println("div" + setVar.typeShort + " r" + rightRegister + " r" + leftRegister);
				System.out.println("move r" + leftRegister + " " + setVar.id);
				if (leftIsVar) {
					registers.remove(leftVar.id().getText());
				}

				registers.put(setVar.id, leftRegister);
			}
		} else {
			LittleParser.FactorContext expression = mainExpr.factor();

			if(expression.getText().contains("(")) {
				expression = expression.postfix_expr().primary().expr().factor();
			}

			// No expression
			// Branch between number and variable assignment
			if (expression.postfix_expr().primary().id() == null) {
				// Number Assignment
				String number = expression.postfix_expr().primary().getText();
				System.out.println("move " + number + " " + id);
			} else {
				// Variable assignment

				// Get variable name and if it has a register
				String assignedVar = expression.postfix_expr().primary().id().getText();
				Integer assignedVarReg = registers.get(assignedVar);

				// Load register if it isnt in memory
				if (assignedVarReg == null) {
					// Load variable into register
					System.out.println("move " + assignedVar + " r" + registerCount);
					registers.put(assignedVar, registerCount);
					registerCount++;

					assignedVarReg = registers.get(assignedVar);
				}

				System.out.println("move r" + assignedVarReg + " " + id);
			}
		}
	}

	/**
	 * Read input
	 * Add variable type for readi readf reads
	 */
	@Override
	public void enterRead_stmt(LittleParser.Read_stmtContext ctx) {
		TinyVariable tinyVar = variables.get(ctx.id_list().id().getText());

		System.out.println("sys read" + tinyVar.typeShort + " " + ctx.id_list().id().getText());

		LittleParser.Id_tailContext nextVariable = ctx.id_list().id_tail();

		while (nextVariable.id() != null) {
			tinyVar = variables.get(nextVariable.id().getText());

			System.out.println("sys read" + tinyVar.typeShort + " " + nextVariable.id().getText());

			nextVariable = nextVariable.id_tail();
		}
	}

	/**
	 * Write to screen
	 * Add variable type for writei wrties writef
	 */
	@Override
	public void enterWrite_stmt(LittleParser.Write_stmtContext ctx) {
		TinyVariable tinyVar = variables.get(ctx.id_list().id().getText());

		System.out.println("sys write" + tinyVar.typeShort + " " + ctx.id_list().id().getText());

		LittleParser.Id_tailContext nextVariable = ctx.id_list().id_tail();

		while (nextVariable.id() != null) {
			tinyVar = variables.get(nextVariable.id().getText());

			System.out.println("sys write" + tinyVar.typeShort + " " + nextVariable.id().getText());

			nextVariable = nextVariable.id_tail();
		}
	}

	/**
	 * On program exit
	 */
	@Override
	public void exitProgram(LittleParser.ProgramContext ctx) {
		System.out.println("sys halt");

		// for (TinyVariable t : variables.values()) {
		// System.out.println(t);
		// }

		// System.out.println(registers);
	}

	/*
	 * @Override public void
	 * enterParam_decl_list(LittleParser.Param_decl_listContext ctx) {
	 * System.out.println(ctx.param_decl().id().getText());
	 * 
	 * String nextVariable = ctx.param_decl_tail();
	 * 
	 * while(nextVariable.param_decl().getText() != null) {
	 * System.out.println(nextVariable.param_decl().id().getText());
	 * 
	 * nextVariable = nextVariable.param_decl_tail();
	 * }
	 * }
	 */
}

class TinyVariable {
	String type;
	String id;
	char typeShort;

	TinyVariable(String type, String id) {
		this.type = type;
		this.id = id;

		if (type.equals("STRING")) {
			typeShort = 's';
		} else if (type.equals("INT")) {
			typeShort = 'i';

		} else if (type.equals("FLOAT")) {
			typeShort = 'r';
		} else {
			System.out.println("INVALID TYPE FOUND: " + type);
		}
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public char getTypeShort() {
		return this.typeShort;
	}

	public void setTypeShort(char typeShort) {
		this.typeShort = typeShort;
	}

	@Override
	public String toString() {
		return "{" +
				" type='" + getType() + "'" +
				", id='" + getId() + "'" +
				", typeShort='" + getTypeShort() + "'" +
				"}";
	}
}