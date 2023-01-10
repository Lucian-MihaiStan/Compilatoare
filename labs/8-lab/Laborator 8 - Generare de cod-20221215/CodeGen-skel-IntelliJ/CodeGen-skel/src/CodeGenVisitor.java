import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

public class CodeGenVisitor implements ASTVisitor<ST>{
	static STGroupFile templates = new STGroupFile("cgen.stg");
	int i = 0;
	int offset = 0;
	ST mainSection;	// filled directly (through visitor returns)
	ST dataSection; // filled collaterally ("global" access)
	ST funcSection; // filled collaterally ("global" access)


	@Override
	public ST visit(Int val) {
		return templates.getInstanceOf("literal")
				.add("value", val.getToken().getText());
	}

	@Override
	public ST visit(FloatNum val) {
		return null;
	}

	@Override
	public ST visit(Bool val) {
		return templates.getInstanceOf("literal")
				.add("value", val.getToken().getText().equals("true")?1:0);
	}

	@Override
	public ST visit(UnaryMinus uMinus) {
		return templates.getInstanceOf("uminus")
				.add("e", uMinus.expr.accept(this))
				.add("dStr", uMinus.debugStr);
	}


	@Override
	public ST visit(Plus expr) {
		ST st = templates.getInstanceOf("plus");
		st.add("e1", expr.left.accept(this))
				.add("e2",  expr.right.accept(this))
				.add("dStr", expr.debugStr);

		return st;
	}

	@Override
	public ST visit(Minus expr) {
		ST st = templates.getInstanceOf("minus");
		st.add("e1", expr.left.accept(this))
				.add("e2",  expr.right.accept(this))
				.add("dStr", expr.debugStr);

		return st;
	}

	@Override
	public ST visit(Mult expr) {
		ST st = templates.getInstanceOf("mult");
		st.add("e1", expr.left.accept(this))
				.add("e2",  expr.right.accept(this))
				.add("dStr", expr.debugStr);

		return st;
	}

	@Override
	public ST visit(Div expr) {
		ST st = templates.getInstanceOf("div");
		st.add("e1", expr.left.accept(this))
				.add("e2", expr.right.accept(this))
				.add("dStr", expr.debugStr);

		return st;
	}

	@Override
	public ST visit(Relational expr) {
		String op = switch (expr.getToken().getText()) {
			case "==" -> "seq";
			case "<" -> "slt";
			default -> "sle";
		};

		return templates.getInstanceOf("binaryOp")
				.add("e1", expr.left.accept(this))
				.add("e2",  expr.right.accept(this))
				.add("dStr", expr.debugStr)
				.add("op", op);
	}

	@Override
	public ST visit(If iff) {
		ST st = templates.getInstanceOf("if");
		st.add("e1", iff.cond.accept(this))
				.add("e2", iff.thenBranch.accept(this))
				.add("e3", iff.elseBranch.accept(this))
				.add("thn", "then" + i)
				.add("els", "else" + i)
				.add("end", "end" + i)
				.add("dStr", iff.debugStr);
		i++;
		return st;
	}

	@Override
	public ST visit(Call call) {
		ST st = templates.getInstanceOf("call");
		ST fParam = templates.getInstanceOf("fparam");

		ST fAddPar = templates.getInstanceOf("add_par");
		for (int i = call.args.size() - 1; i >= 0; i--)
			fParam.add("e", fAddPar.add("e", call.args.get(i).accept(this)));

		st.add("f", call.id.getToken().getText());
		st.add("e", fParam);
		return st;
	}

	@Override
	public ST visit(Assign assign) {
		ST st = templates.getInstanceOf("assign");
		st
				.add("name", assign.id.getToken().getText())
				.add("value", assign.expr.accept(this));
		return st;
	}

	@Override
	public ST visit(VarDef varDef) {
		ST st = templates.getInstanceOf("varDef");
		st.add("name", varDef.id.getToken().getText());
		st.add("type", "word");
		st.add("value", 0);
		dataSection.add("e", st);

		if (varDef.initValue != null)
			return templates.getInstanceOf("assign")
					.add("name", varDef.id.getToken().getText())
					.add("value", varDef.initValue.accept(this));

		return null;
	}

	@Override
	public ST visit(FuncDef funcDef) {
		ST st = templates.getInstanceOf("fdef");
		offset = 1;
		for (Formal formal: funcDef.formals) {
			formal.accept(this);
			offset++;
		}

		st.add("f", funcDef.id.getToken().getText());
		st.add("e", funcDef.body.accept(this));
		st.add("offset", funcDef.formals.size() * 4 + 8);

		funcSection.add("e", st);
		return null;
	}

	/*
	 * META
	 */

	@Override
	public ST visit(Id id) {
		if (id.getSymbol().IsFormal)
			return templates.getInstanceOf("localVar").add("offset", id.getSymbol().offset);

		return templates.getInstanceOf("id").add("name", id.getToken().getText());
	}

	@Override
	public ST visit(Formal formal) {
		formal.id.getSymbol().setFormal(true);
		formal.id.getSymbol().setOffset(4 * offset);
		return null;
	}

	@Override
	public ST visit(Type type) {
		return null;
	}

	@Override
	public ST visit(Program program) {
		dataSection = templates.getInstanceOf("sequenceSpaced");
		funcSection = templates.getInstanceOf("sequenceSpaced");
		mainSection = templates.getInstanceOf("sequence");

		for (ASTNode e : program.stmts)
			mainSection.add("e", e.accept(this));

		//assembly-ing it all together. HA! get it?
		ST programST = templates.getInstanceOf("program");
		programST.add("data", dataSection);
		programST.add("textFuncs", funcSection);
		programST.add("textMain", mainSection);

		return programST;
	}

}
