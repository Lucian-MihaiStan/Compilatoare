// Generated from C:/Users/stanl/Documents/Uni/cpl/labs/3-lab/Parser-skel\CPLangParser.g4 by ANTLR 4.10.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CPLangParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		IF=1, THEN=2, ELSE=3, FI=4, BOOL=5, TYPE=6, ID=7, INT=8, FLOAT=9, STRING=10, 
		SEMI=11, COMMA=12, ASSIGN=13, LPAREN=14, RPAREN=15, LBRACE=16, RBRACE=17, 
		PLUS=18, MINUS=19, MULT=20, DIV=21, EQUAL=22, LT=23, LE=24, LINE_COMMENT=25, 
		BLOCK_COMMENT=26, WS=27;
	public static final int
		RULE_prog = 0, RULE_formal = 1, RULE_definition = 2, RULE_expr = 3;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "formal", "definition", "expr"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'if'", "'then'", "'else'", "'fi'", null, null, null, null, null, 
			null, "';'", "','", "'='", "'('", "')'", "'{'", "'}'", "'+'", "'-'", 
			"'*'", "'/'", "'=='", "'<'", "'<='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "IF", "THEN", "ELSE", "FI", "BOOL", "TYPE", "ID", "INT", "FLOAT", 
			"STRING", "SEMI", "COMMA", "ASSIGN", "LPAREN", "RPAREN", "LBRACE", "RBRACE", 
			"PLUS", "MINUS", "MULT", "DIV", "EQUAL", "LT", "LE", "LINE_COMMENT", 
			"BLOCK_COMMENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "CPLangParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CPLangParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(CPLangParser.EOF, 0); }
		public List<TerminalNode> SEMI() { return getTokens(CPLangParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(CPLangParser.SEMI, i);
		}
		public List<DefinitionContext> definition() {
			return getRuleContexts(DefinitionContext.class);
		}
		public DefinitionContext definition(int i) {
			return getRuleContext(DefinitionContext.class,i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitProg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IF) | (1L << BOOL) | (1L << TYPE) | (1L << ID) | (1L << INT) | (1L << FLOAT) | (1L << LPAREN) | (1L << MINUS))) != 0)) {
				{
				{
				setState(10);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case TYPE:
					{
					setState(8);
					definition();
					}
					break;
				case IF:
				case BOOL:
				case ID:
				case INT:
				case FLOAT:
				case LPAREN:
				case MINUS:
					{
					setState(9);
					expr(0);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(12);
				match(SEMI);
				}
				}
				setState(18);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(19);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormalContext extends ParserRuleContext {
		public TerminalNode TYPE() { return getToken(CPLangParser.TYPE, 0); }
		public TerminalNode ID() { return getToken(CPLangParser.ID, 0); }
		public FormalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterFormal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitFormal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitFormal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormalContext formal() throws RecognitionException {
		FormalContext _localctx = new FormalContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_formal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(21);
			match(TYPE);
			setState(22);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefinitionContext extends ParserRuleContext {
		public DefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definition; }
	 
		public DefinitionContext() { }
		public void copyFrom(DefinitionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Function_defContext extends DefinitionContext {
		public TerminalNode TYPE() { return getToken(CPLangParser.TYPE, 0); }
		public TerminalNode ID() { return getToken(CPLangParser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(CPLangParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(CPLangParser.RPAREN, 0); }
		public TerminalNode LBRACE() { return getToken(CPLangParser.LBRACE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(CPLangParser.RBRACE, 0); }
		public List<FormalContext> formal() {
			return getRuleContexts(FormalContext.class);
		}
		public FormalContext formal(int i) {
			return getRuleContext(FormalContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(CPLangParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(CPLangParser.COMMA, i);
		}
		public Function_defContext(DefinitionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterFunction_def(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitFunction_def(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitFunction_def(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Variable_defContext extends DefinitionContext {
		public TerminalNode TYPE() { return getToken(CPLangParser.TYPE, 0); }
		public TerminalNode ID() { return getToken(CPLangParser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(CPLangParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Variable_defContext(DefinitionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterVariable_def(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitVariable_def(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitVariable_def(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionContext definition() throws RecognitionException {
		DefinitionContext _localctx = new DefinitionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_definition);
		int _la;
		try {
			setState(51);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				_localctx = new Variable_defContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(24);
				match(TYPE);
				setState(25);
				match(ID);
				setState(28);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ASSIGN) {
					{
					setState(26);
					match(ASSIGN);
					setState(27);
					expr(0);
					}
				}

				}
				}
				break;
			case 2:
				_localctx = new Function_defContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(30);
				match(TYPE);
				setState(31);
				match(ID);
				setState(32);
				match(LPAREN);
				setState(43);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==TYPE) {
					{
					{
					setState(33);
					formal();
					setState(38);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(34);
						match(COMMA);
						setState(35);
						formal();
						}
						}
						setState(40);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
					}
					setState(45);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(46);
				match(RPAREN);
				setState(47);
				match(LBRACE);
				setState(48);
				expr(0);
				setState(49);
				match(RBRACE);
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class MinusContext extends ExprContext {
		public TerminalNode MINUS() { return getToken(CPLangParser.MINUS, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public MinusContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterMinus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitMinus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitMinus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Expr_parContext extends ExprContext {
		public TerminalNode LPAREN() { return getToken(CPLangParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CPLangParser.RPAREN, 0); }
		public Expr_parContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterExpr_par(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitExpr_par(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitExpr_par(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MultContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MULT() { return getToken(CPLangParser.MULT, 0); }
		public MultContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterMult(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitMult(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitMult(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BoolContext extends ExprContext {
		public TerminalNode BOOL() { return getToken(CPLangParser.BOOL, 0); }
		public BoolContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitBool(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitBool(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FloatContext extends ExprContext {
		public TerminalNode FLOAT() { return getToken(CPLangParser.FLOAT, 0); }
		public FloatContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterFloat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitFloat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitFloat(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntContext extends ExprContext {
		public TerminalNode INT() { return getToken(CPLangParser.INT, 0); }
		public IntContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterInt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitInt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitInt(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PlusContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(CPLangParser.PLUS, 0); }
		public PlusContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterPlus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitPlus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitPlus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CallContext extends ExprContext {
		public TerminalNode ID() { return getToken(CPLangParser.ID, 0); }
		public TerminalNode LPAREN() { return getToken(CPLangParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(CPLangParser.RPAREN, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(CPLangParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(CPLangParser.COMMA, i);
		}
		public CallContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitCall(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DivContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode DIV() { return getToken(CPLangParser.DIV, 0); }
		public DivContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterDiv(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitDiv(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitDiv(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Op_relContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LT() { return getToken(CPLangParser.LT, 0); }
		public TerminalNode LE() { return getToken(CPLangParser.LE, 0); }
		public TerminalNode EQUAL() { return getToken(CPLangParser.EQUAL, 0); }
		public Op_relContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterOp_rel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitOp_rel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitOp_rel(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdContext extends ExprContext {
		public TerminalNode ID() { return getToken(CPLangParser.ID, 0); }
		public IdContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Minus_exprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MINUS() { return getToken(CPLangParser.MINUS, 0); }
		public Minus_exprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterMinus_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitMinus_expr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitMinus_expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfContext extends ExprContext {
		public ExprContext cond;
		public ExprContext thenBranch;
		public ExprContext elseBranch;
		public TerminalNode IF() { return getToken(CPLangParser.IF, 0); }
		public TerminalNode THEN() { return getToken(CPLangParser.THEN, 0); }
		public TerminalNode ELSE() { return getToken(CPLangParser.ELSE, 0); }
		public TerminalNode FI() { return getToken(CPLangParser.FI, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public IfContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterIf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitIf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitIf(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AssignContext extends ExprContext {
		public TerminalNode ID() { return getToken(CPLangParser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(CPLangParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AssignContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).enterAssign(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPLangParserListener ) ((CPLangParserListener)listener).exitAssign(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPLangParserVisitor ) return ((CPLangParserVisitor<? extends T>)visitor).visitAssign(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 6;
		enterRecursionRule(_localctx, 6, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				{
				_localctx = new CallContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(54);
				match(ID);
				setState(55);
				match(LPAREN);
				setState(62);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IF) | (1L << BOOL) | (1L << ID) | (1L << INT) | (1L << FLOAT) | (1L << LPAREN) | (1L << MINUS))) != 0)) {
					{
					{
					setState(56);
					expr(0);
					setState(58);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==COMMA) {
						{
						setState(57);
						match(COMMA);
						}
					}

					}
					}
					setState(64);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(65);
				match(RPAREN);
				}
				break;
			case 2:
				{
				_localctx = new MinusContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(66);
				match(MINUS);
				setState(67);
				expr(13);
				}
				break;
			case 3:
				{
				_localctx = new IfContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(68);
				match(IF);
				setState(69);
				((IfContext)_localctx).cond = expr(0);
				setState(70);
				match(THEN);
				setState(71);
				((IfContext)_localctx).thenBranch = expr(0);
				setState(72);
				match(ELSE);
				setState(73);
				((IfContext)_localctx).elseBranch = expr(0);
				setState(74);
				match(FI);
				}
				break;
			case 4:
				{
				_localctx = new AssignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(76);
				match(ID);
				setState(77);
				match(ASSIGN);
				setState(78);
				expr(6);
				}
				break;
			case 5:
				{
				_localctx = new Expr_parContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(79);
				match(LPAREN);
				setState(80);
				expr(0);
				setState(81);
				match(RPAREN);
				}
				break;
			case 6:
				{
				_localctx = new IdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(83);
				match(ID);
				}
				break;
			case 7:
				{
				_localctx = new IntContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(84);
				match(INT);
				}
				break;
			case 8:
				{
				_localctx = new FloatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(85);
				match(FLOAT);
				}
				break;
			case 9:
				{
				_localctx = new BoolContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(86);
				match(BOOL);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(106);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(104);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
					case 1:
						{
						_localctx = new MultContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(89);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(90);
						match(MULT);
						setState(91);
						expr(13);
						}
						break;
					case 2:
						{
						_localctx = new DivContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(92);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(93);
						match(DIV);
						setState(94);
						expr(12);
						}
						break;
					case 3:
						{
						_localctx = new PlusContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(95);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(96);
						match(PLUS);
						setState(97);
						expr(11);
						}
						break;
					case 4:
						{
						_localctx = new Minus_exprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(98);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(99);
						match(MINUS);
						setState(100);
						expr(10);
						}
						break;
					case 5:
						{
						_localctx = new Op_relContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(101);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(102);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQUAL) | (1L << LT) | (1L << LE))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(103);
						expr(9);
						}
						break;
					}
					} 
				}
				setState(108);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 3:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 12);
		case 1:
			return precpred(_ctx, 11);
		case 2:
			return precpred(_ctx, 10);
		case 3:
			return precpred(_ctx, 9);
		case 4:
			return precpred(_ctx, 8);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u001bn\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0001\u0000\u0001\u0000\u0003"+
		"\u0000\u000b\b\u0000\u0001\u0000\u0001\u0000\u0005\u0000\u000f\b\u0000"+
		"\n\u0000\f\u0000\u0012\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003"+
		"\u0002\u001d\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0005\u0002%\b\u0002\n\u0002\f\u0002(\t\u0002\u0005"+
		"\u0002*\b\u0002\n\u0002\f\u0002-\t\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0003\u00024\b\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003;\b\u0003\u0005"+
		"\u0003=\b\u0003\n\u0003\f\u0003@\t\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0003\u0003X\b\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0005\u0003i\b\u0003\n\u0003\f\u0003l\t\u0003\u0001\u0003\u0000"+
		"\u0001\u0006\u0004\u0000\u0002\u0004\u0006\u0000\u0001\u0001\u0000\u0016"+
		"\u0018~\u0000\u0010\u0001\u0000\u0000\u0000\u0002\u0015\u0001\u0000\u0000"+
		"\u0000\u00043\u0001\u0000\u0000\u0000\u0006W\u0001\u0000\u0000\u0000\b"+
		"\u000b\u0003\u0004\u0002\u0000\t\u000b\u0003\u0006\u0003\u0000\n\b\u0001"+
		"\u0000\u0000\u0000\n\t\u0001\u0000\u0000\u0000\u000b\f\u0001\u0000\u0000"+
		"\u0000\f\r\u0005\u000b\u0000\u0000\r\u000f\u0001\u0000\u0000\u0000\u000e"+
		"\n\u0001\u0000\u0000\u0000\u000f\u0012\u0001\u0000\u0000\u0000\u0010\u000e"+
		"\u0001\u0000\u0000\u0000\u0010\u0011\u0001\u0000\u0000\u0000\u0011\u0013"+
		"\u0001\u0000\u0000\u0000\u0012\u0010\u0001\u0000\u0000\u0000\u0013\u0014"+
		"\u0005\u0000\u0000\u0001\u0014\u0001\u0001\u0000\u0000\u0000\u0015\u0016"+
		"\u0005\u0006\u0000\u0000\u0016\u0017\u0005\u0007\u0000\u0000\u0017\u0003"+
		"\u0001\u0000\u0000\u0000\u0018\u0019\u0005\u0006\u0000\u0000\u0019\u001c"+
		"\u0005\u0007\u0000\u0000\u001a\u001b\u0005\r\u0000\u0000\u001b\u001d\u0003"+
		"\u0006\u0003\u0000\u001c\u001a\u0001\u0000\u0000\u0000\u001c\u001d\u0001"+
		"\u0000\u0000\u0000\u001d4\u0001\u0000\u0000\u0000\u001e\u001f\u0005\u0006"+
		"\u0000\u0000\u001f \u0005\u0007\u0000\u0000 +\u0005\u000e\u0000\u0000"+
		"!&\u0003\u0002\u0001\u0000\"#\u0005\f\u0000\u0000#%\u0003\u0002\u0001"+
		"\u0000$\"\u0001\u0000\u0000\u0000%(\u0001\u0000\u0000\u0000&$\u0001\u0000"+
		"\u0000\u0000&\'\u0001\u0000\u0000\u0000\'*\u0001\u0000\u0000\u0000(&\u0001"+
		"\u0000\u0000\u0000)!\u0001\u0000\u0000\u0000*-\u0001\u0000\u0000\u0000"+
		"+)\u0001\u0000\u0000\u0000+,\u0001\u0000\u0000\u0000,.\u0001\u0000\u0000"+
		"\u0000-+\u0001\u0000\u0000\u0000./\u0005\u000f\u0000\u0000/0\u0005\u0010"+
		"\u0000\u000001\u0003\u0006\u0003\u000012\u0005\u0011\u0000\u000024\u0001"+
		"\u0000\u0000\u00003\u0018\u0001\u0000\u0000\u00003\u001e\u0001\u0000\u0000"+
		"\u00004\u0005\u0001\u0000\u0000\u000056\u0006\u0003\uffff\uffff\u0000"+
		"67\u0005\u0007\u0000\u00007>\u0005\u000e\u0000\u00008:\u0003\u0006\u0003"+
		"\u00009;\u0005\f\u0000\u0000:9\u0001\u0000\u0000\u0000:;\u0001\u0000\u0000"+
		"\u0000;=\u0001\u0000\u0000\u0000<8\u0001\u0000\u0000\u0000=@\u0001\u0000"+
		"\u0000\u0000><\u0001\u0000\u0000\u0000>?\u0001\u0000\u0000\u0000?A\u0001"+
		"\u0000\u0000\u0000@>\u0001\u0000\u0000\u0000AX\u0005\u000f\u0000\u0000"+
		"BC\u0005\u0013\u0000\u0000CX\u0003\u0006\u0003\rDE\u0005\u0001\u0000\u0000"+
		"EF\u0003\u0006\u0003\u0000FG\u0005\u0002\u0000\u0000GH\u0003\u0006\u0003"+
		"\u0000HI\u0005\u0003\u0000\u0000IJ\u0003\u0006\u0003\u0000JK\u0005\u0004"+
		"\u0000\u0000KX\u0001\u0000\u0000\u0000LM\u0005\u0007\u0000\u0000MN\u0005"+
		"\r\u0000\u0000NX\u0003\u0006\u0003\u0006OP\u0005\u000e\u0000\u0000PQ\u0003"+
		"\u0006\u0003\u0000QR\u0005\u000f\u0000\u0000RX\u0001\u0000\u0000\u0000"+
		"SX\u0005\u0007\u0000\u0000TX\u0005\b\u0000\u0000UX\u0005\t\u0000\u0000"+
		"VX\u0005\u0005\u0000\u0000W5\u0001\u0000\u0000\u0000WB\u0001\u0000\u0000"+
		"\u0000WD\u0001\u0000\u0000\u0000WL\u0001\u0000\u0000\u0000WO\u0001\u0000"+
		"\u0000\u0000WS\u0001\u0000\u0000\u0000WT\u0001\u0000\u0000\u0000WU\u0001"+
		"\u0000\u0000\u0000WV\u0001\u0000\u0000\u0000Xj\u0001\u0000\u0000\u0000"+
		"YZ\n\f\u0000\u0000Z[\u0005\u0014\u0000\u0000[i\u0003\u0006\u0003\r\\]"+
		"\n\u000b\u0000\u0000]^\u0005\u0015\u0000\u0000^i\u0003\u0006\u0003\f_"+
		"`\n\n\u0000\u0000`a\u0005\u0012\u0000\u0000ai\u0003\u0006\u0003\u000b"+
		"bc\n\t\u0000\u0000cd\u0005\u0013\u0000\u0000di\u0003\u0006\u0003\nef\n"+
		"\b\u0000\u0000fg\u0007\u0000\u0000\u0000gi\u0003\u0006\u0003\thY\u0001"+
		"\u0000\u0000\u0000h\\\u0001\u0000\u0000\u0000h_\u0001\u0000\u0000\u0000"+
		"hb\u0001\u0000\u0000\u0000he\u0001\u0000\u0000\u0000il\u0001\u0000\u0000"+
		"\u0000jh\u0001\u0000\u0000\u0000jk\u0001\u0000\u0000\u0000k\u0007\u0001"+
		"\u0000\u0000\u0000lj\u0001\u0000\u0000\u0000\u000b\n\u0010\u001c&+3:>"+
		"Whj";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}