// Generated from C:/Users/stanl/Documents/Uni/cpl/labs/3-lab/Parser-skel\CPLangParser.g4 by ANTLR 4.10.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CPLangParser}.
 */
public interface CPLangParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CPLangParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(CPLangParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLangParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(CPLangParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPLangParser#formal}.
	 * @param ctx the parse tree
	 */
	void enterFormal(CPLangParser.FormalContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPLangParser#formal}.
	 * @param ctx the parse tree
	 */
	void exitFormal(CPLangParser.FormalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code variable_def}
	 * labeled alternative in {@link CPLangParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterVariable_def(CPLangParser.Variable_defContext ctx);
	/**
	 * Exit a parse tree produced by the {@code variable_def}
	 * labeled alternative in {@link CPLangParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitVariable_def(CPLangParser.Variable_defContext ctx);
	/**
	 * Enter a parse tree produced by the {@code function_def}
	 * labeled alternative in {@link CPLangParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterFunction_def(CPLangParser.Function_defContext ctx);
	/**
	 * Exit a parse tree produced by the {@code function_def}
	 * labeled alternative in {@link CPLangParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitFunction_def(CPLangParser.Function_defContext ctx);
	/**
	 * Enter a parse tree produced by the {@code minus}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMinus(CPLangParser.MinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code minus}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMinus(CPLangParser.MinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expr_par}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr_par(CPLangParser.Expr_parContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expr_par}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr_par(CPLangParser.Expr_parContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mult}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMult(CPLangParser.MultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mult}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMult(CPLangParser.MultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bool}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBool(CPLangParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bool}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBool(CPLangParser.BoolContext ctx);
	/**
	 * Enter a parse tree produced by the {@code float}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFloat(CPLangParser.FloatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code float}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFloat(CPLangParser.FloatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code int}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterInt(CPLangParser.IntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code int}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitInt(CPLangParser.IntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code plus}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPlus(CPLangParser.PlusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code plus}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPlus(CPLangParser.PlusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code call}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCall(CPLangParser.CallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code call}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCall(CPLangParser.CallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code div}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterDiv(CPLangParser.DivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code div}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitDiv(CPLangParser.DivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code op_rel}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterOp_rel(CPLangParser.Op_relContext ctx);
	/**
	 * Exit a parse tree produced by the {@code op_rel}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitOp_rel(CPLangParser.Op_relContext ctx);
	/**
	 * Enter a parse tree produced by the {@code id}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterId(CPLangParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code id}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitId(CPLangParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code minus_expr}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMinus_expr(CPLangParser.Minus_exprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code minus_expr}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMinus_expr(CPLangParser.Minus_exprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code if}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIf(CPLangParser.IfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code if}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIf(CPLangParser.IfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assign}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAssign(CPLangParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link CPLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAssign(CPLangParser.AssignContext ctx);
}