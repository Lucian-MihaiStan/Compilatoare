// Generated from C:/Users/stanl/Documents/Uni/cpl/Tema1/src/cool/parser\CoolLexer.g4 by ANTLR 4.10.1

    package cool.parser;



import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CoolLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ERROR=1, CLASS=2, ELSE=3, FALSE=4, FI=5, IF=6, IN=7, INHERITS=8, ISVOID=9, 
		LET=10, LOOP=11, POOL=12, THEN=13, WHILE=14, CASE=15, ESAC=16, NEW=17, 
		OF=18, NOT=19, TRUE=20, LPAREN=21, RPAREN=22, LBRACE=23, RBRACE=24, COMMA=25, 
		COLON=26, SEMI=27, ASSIGN=28, RESULTS_CASE=29, PLUS=30, MINUS=31, MULTIPLY=32, 
		DIVIDE=33, TILDA=34, LT=35, LE=36, EQ=37, DOT=38, AT=39, QUOTE=40, TYPE=41, 
		ID=42, LINE_COMMENT=43, BLOCK_COMMENT=44, UNMATCHED_COMMENT=45, INT=46, 
		STRING=47, WS=48, INVALID_CHARACTER=49;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"CLASS", "ELSE", "FALSE", "FI", "IF", "IN", "INHERITS", "ISVOID", "LET", 
			"LOOP", "POOL", "THEN", "WHILE", "CASE", "ESAC", "NEW", "OF", "NOT", 
			"TRUE", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "COMMA", "COLON", "SEMI", 
			"ASSIGN", "RESULTS_CASE", "PLUS", "MINUS", "MULTIPLY", "DIVIDE", "TILDA", 
			"LT", "LE", "EQ", "DOT", "AT", "QUOTE", "UPPER_CASE_LETTER", "LOWER_CASE_LETTER", 
			"TYPE", "ID", "NEW_LINE", "SINGLE_LINE_COMMENT_START", "OPEN_PAREN_STAR", 
			"CLOSE_PAREN_STAR", "LINE_COMMENT", "BLOCK_COMMENT", "UNMATCHED_COMMENT", 
			"DIGIT", "INT", "STRING", "WS", "INVALID_CHARACTER"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "'('", "')'", "'{'", 
			"'}'", "','", "':'", "';'", "'<-'", "'=>'", "'+'", "'-'", "'*'", "'/'", 
			"'~'", "'<'", "'<='", "'='", "'.'", "'@'", "'\"'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ERROR", "CLASS", "ELSE", "FALSE", "FI", "IF", "IN", "INHERITS", 
			"ISVOID", "LET", "LOOP", "POOL", "THEN", "WHILE", "CASE", "ESAC", "NEW", 
			"OF", "NOT", "TRUE", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "COMMA", 
			"COLON", "SEMI", "ASSIGN", "RESULTS_CASE", "PLUS", "MINUS", "MULTIPLY", 
			"DIVIDE", "TILDA", "LT", "LE", "EQ", "DOT", "AT", "QUOTE", "TYPE", "ID", 
			"LINE_COMMENT", "BLOCK_COMMENT", "UNMATCHED_COMMENT", "INT", "STRING", 
			"WS", "INVALID_CHARACTER"
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



	    private static final int MAX_STRING_LENGTH = 1024;
	    public static final String EOF_STRING_ERROR = "EOF in string constant";
	    public static final String STRING_CONSTANT_TO_LONG_ERROR = "String constant too long";
	    public static final String INVALID_CHARACTER_ERROR = "Invalid character: ";
	    public static final String EOF_COMMENT_ERROR = "EOF in comment";
	    public static final String UNMATCHED_COMMENT_ERROR = "Unmatched *)";
	    public static final String UNTERMINATED_STRING_CONSTANT_ERROR = "Unterminated string constant";
	    public static final String STRING_NULL_CHARACTER_ERROR = "String contains null character";
	    public static final String NULL_STRING = "\0";

	    private void raiseError(String msg) {
	        setText(msg);
	        setType(ERROR);
	    }


	public CoolLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CoolLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 48:
			BLOCK_COMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 49:
			UNMATCHED_COMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 52:
			STRING_action((RuleContext)_localctx, actionIndex);
			break;
		case 54:
			INVALID_CHARACTER_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void BLOCK_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 skip(); 
			break;
		case 1:
			 raiseError(EOF_COMMENT_ERROR); 
			break;
		}
	}
	private void UNMATCHED_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:
			 raiseError(UNMATCHED_COMMENT_ERROR); 
			break;
		}
	}
	private void STRING_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:

					String str = getText().substring(1, getText().length() - 1).replace("\\\r\n", "\r\n").replace("\\\n", "\n").replace("\\n", "\n").replace("\\t", "\t").replaceAll("\\\\(?!\\\\)", "");

					if (str.length() > MAX_STRING_LENGTH) {
						raiseError(STRING_CONSTANT_TO_LONG_ERROR);
			            return;
			        }

					if (str.contains(NULL_STRING)) {
						raiseError(STRING_NULL_CHARACTER_ERROR);
					    return;
					}

					setText(str);
				
			break;
		case 4:
			 raiseError(EOF_STRING_ERROR); 
			break;
		case 5:
			 raiseError(UNTERMINATED_STRING_CONSTANT_ERROR); 
			break;
		}
	}
	private void INVALID_CHARACTER_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 6:
			 raiseError(INVALID_CHARACTER_ERROR + getText()); 
			break;
		}
	}

	public static final String _serializedATN =
		"\u0004\u00001\u016a\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002"+
		"\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002"+
		"\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002"+
		"\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002"+
		"\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002"+
		"\u001e\u0007\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007"+
		"!\u0002\"\u0007\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007"+
		"&\u0002\'\u0007\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007"+
		"+\u0002,\u0007,\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u0007"+
		"0\u00021\u00071\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u0007"+
		"5\u00026\u00076\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0001"+
		"\u0014\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0017\u0001"+
		"\u0017\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0001"+
		"\u001c\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001f\u0001"+
		"\u001f\u0001 \u0001 \u0001!\u0001!\u0001\"\u0001\"\u0001\"\u0001#\u0001"+
		"#\u0001$\u0001$\u0001%\u0001%\u0001&\u0001&\u0001\'\u0001\'\u0001(\u0001"+
		"(\u0001)\u0004)\u00fd\b)\u000b)\f)\u00fe\u0001)\u0001)\u0001)\u0001)\u0005"+
		")\u0105\b)\n)\f)\u0108\t)\u0001*\u0004*\u010b\b*\u000b*\f*\u010c\u0001"+
		"*\u0001*\u0001*\u0001*\u0005*\u0113\b*\n*\f*\u0116\t*\u0001+\u0003+\u0119"+
		"\b+\u0001+\u0001+\u0001,\u0001,\u0001,\u0001-\u0001-\u0001-\u0001.\u0001"+
		".\u0001.\u0001/\u0001/\u0005/\u0128\b/\n/\f/\u012b\t/\u0001/\u0001/\u0003"+
		"/\u012f\b/\u0001/\u0001/\u00010\u00010\u00010\u00050\u0136\b0\n0\f0\u0139"+
		"\t0\u00010\u00010\u00010\u00010\u00010\u00030\u0140\b0\u00011\u00011\u0001"+
		"1\u00012\u00012\u00013\u00043\u0148\b3\u000b3\f3\u0149\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00054\u0152\b4\n4\f4\u0155\t4\u00014\u00014\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00034\u015f\b4\u00015\u00045\u0162"+
		"\b5\u000b5\f5\u0163\u00015\u00015\u00016\u00016\u00016\u0003\u0129\u0137"+
		"\u0153\u00007\u0001\u0002\u0003\u0003\u0005\u0004\u0007\u0005\t\u0006"+
		"\u000b\u0007\r\b\u000f\t\u0011\n\u0013\u000b\u0015\f\u0017\r\u0019\u000e"+
		"\u001b\u000f\u001d\u0010\u001f\u0011!\u0012#\u0013%\u0014\'\u0015)\u0016"+
		"+\u0017-\u0018/\u00191\u001a3\u001b5\u001c7\u001d9\u001e;\u001f= ?!A\""+
		"C#E$G%I&K\'M(O\u0000Q\u0000S)U*W\u0000Y\u0000[\u0000]\u0000_+a,c-e\u0000"+
		"g.i/k0m1\u0001\u0000\u0015\u0002\u0000CCcc\u0002\u0000LLll\u0002\u0000"+
		"AAaa\u0002\u0000SSss\u0002\u0000EEee\u0002\u0000FFff\u0002\u0000IIii\u0002"+
		"\u0000NNnn\u0002\u0000HHhh\u0002\u0000RRrr\u0002\u0000TTtt\u0002\u0000"+
		"VVvv\u0002\u0000OOoo\u0002\u0000DDdd\u0002\u0000PPpp\u0002\u0000WWww\u0002"+
		"\u0000UUuu\u0001\u0000AZ\u0001\u0000az\u0001\u000009\u0003\u0000\t\n\f"+
		"\r  \u0179\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000"+
		"\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000"+
		"\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000"+
		"\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000"+
		"\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000"+
		"\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000"+
		"\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000"+
		"\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000"+
		"\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%"+
		"\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000\u0000)\u0001"+
		"\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000-\u0001\u0000\u0000"+
		"\u0000\u0000/\u0001\u0000\u0000\u0000\u00001\u0001\u0000\u0000\u0000\u0000"+
		"3\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000\u0000\u00007\u0001"+
		"\u0000\u0000\u0000\u00009\u0001\u0000\u0000\u0000\u0000;\u0001\u0000\u0000"+
		"\u0000\u0000=\u0001\u0000\u0000\u0000\u0000?\u0001\u0000\u0000\u0000\u0000"+
		"A\u0001\u0000\u0000\u0000\u0000C\u0001\u0000\u0000\u0000\u0000E\u0001"+
		"\u0000\u0000\u0000\u0000G\u0001\u0000\u0000\u0000\u0000I\u0001\u0000\u0000"+
		"\u0000\u0000K\u0001\u0000\u0000\u0000\u0000M\u0001\u0000\u0000\u0000\u0000"+
		"S\u0001\u0000\u0000\u0000\u0000U\u0001\u0000\u0000\u0000\u0000_\u0001"+
		"\u0000\u0000\u0000\u0000a\u0001\u0000\u0000\u0000\u0000c\u0001\u0000\u0000"+
		"\u0000\u0000g\u0001\u0000\u0000\u0000\u0000i\u0001\u0000\u0000\u0000\u0000"+
		"k\u0001\u0000\u0000\u0000\u0000m\u0001\u0000\u0000\u0000\u0001o\u0001"+
		"\u0000\u0000\u0000\u0003u\u0001\u0000\u0000\u0000\u0005z\u0001\u0000\u0000"+
		"\u0000\u0007\u0080\u0001\u0000\u0000\u0000\t\u0083\u0001\u0000\u0000\u0000"+
		"\u000b\u0086\u0001\u0000\u0000\u0000\r\u0089\u0001\u0000\u0000\u0000\u000f"+
		"\u0092\u0001\u0000\u0000\u0000\u0011\u0099\u0001\u0000\u0000\u0000\u0013"+
		"\u009d\u0001\u0000\u0000\u0000\u0015\u00a2\u0001\u0000\u0000\u0000\u0017"+
		"\u00a7\u0001\u0000\u0000\u0000\u0019\u00ac\u0001\u0000\u0000\u0000\u001b"+
		"\u00b2\u0001\u0000\u0000\u0000\u001d\u00b7\u0001\u0000\u0000\u0000\u001f"+
		"\u00bc\u0001\u0000\u0000\u0000!\u00c0\u0001\u0000\u0000\u0000#\u00c3\u0001"+
		"\u0000\u0000\u0000%\u00c7\u0001\u0000\u0000\u0000\'\u00cc\u0001\u0000"+
		"\u0000\u0000)\u00ce\u0001\u0000\u0000\u0000+\u00d0\u0001\u0000\u0000\u0000"+
		"-\u00d2\u0001\u0000\u0000\u0000/\u00d4\u0001\u0000\u0000\u00001\u00d6"+
		"\u0001\u0000\u0000\u00003\u00d8\u0001\u0000\u0000\u00005\u00da\u0001\u0000"+
		"\u0000\u00007\u00dd\u0001\u0000\u0000\u00009\u00e0\u0001\u0000\u0000\u0000"+
		";\u00e2\u0001\u0000\u0000\u0000=\u00e4\u0001\u0000\u0000\u0000?\u00e6"+
		"\u0001\u0000\u0000\u0000A\u00e8\u0001\u0000\u0000\u0000C\u00ea\u0001\u0000"+
		"\u0000\u0000E\u00ec\u0001\u0000\u0000\u0000G\u00ef\u0001\u0000\u0000\u0000"+
		"I\u00f1\u0001\u0000\u0000\u0000K\u00f3\u0001\u0000\u0000\u0000M\u00f5"+
		"\u0001\u0000\u0000\u0000O\u00f7\u0001\u0000\u0000\u0000Q\u00f9\u0001\u0000"+
		"\u0000\u0000S\u00fc\u0001\u0000\u0000\u0000U\u010a\u0001\u0000\u0000\u0000"+
		"W\u0118\u0001\u0000\u0000\u0000Y\u011c\u0001\u0000\u0000\u0000[\u011f"+
		"\u0001\u0000\u0000\u0000]\u0122\u0001\u0000\u0000\u0000_\u0125\u0001\u0000"+
		"\u0000\u0000a\u0132\u0001\u0000\u0000\u0000c\u0141\u0001\u0000\u0000\u0000"+
		"e\u0144\u0001\u0000\u0000\u0000g\u0147\u0001\u0000\u0000\u0000i\u014b"+
		"\u0001\u0000\u0000\u0000k\u0161\u0001\u0000\u0000\u0000m\u0167\u0001\u0000"+
		"\u0000\u0000op\u0007\u0000\u0000\u0000pq\u0007\u0001\u0000\u0000qr\u0007"+
		"\u0002\u0000\u0000rs\u0007\u0003\u0000\u0000st\u0007\u0003\u0000\u0000"+
		"t\u0002\u0001\u0000\u0000\u0000uv\u0007\u0004\u0000\u0000vw\u0007\u0001"+
		"\u0000\u0000wx\u0007\u0003\u0000\u0000xy\u0007\u0004\u0000\u0000y\u0004"+
		"\u0001\u0000\u0000\u0000z{\u0005f\u0000\u0000{|\u0007\u0002\u0000\u0000"+
		"|}\u0007\u0001\u0000\u0000}~\u0007\u0003\u0000\u0000~\u007f\u0007\u0004"+
		"\u0000\u0000\u007f\u0006\u0001\u0000\u0000\u0000\u0080\u0081\u0007\u0005"+
		"\u0000\u0000\u0081\u0082\u0007\u0006\u0000\u0000\u0082\b\u0001\u0000\u0000"+
		"\u0000\u0083\u0084\u0007\u0006\u0000\u0000\u0084\u0085\u0007\u0005\u0000"+
		"\u0000\u0085\n\u0001\u0000\u0000\u0000\u0086\u0087\u0007\u0006\u0000\u0000"+
		"\u0087\u0088\u0007\u0007\u0000\u0000\u0088\f\u0001\u0000\u0000\u0000\u0089"+
		"\u008a\u0007\u0006\u0000\u0000\u008a\u008b\u0007\u0007\u0000\u0000\u008b"+
		"\u008c\u0007\b\u0000\u0000\u008c\u008d\u0007\u0004\u0000\u0000\u008d\u008e"+
		"\u0007\t\u0000\u0000\u008e\u008f\u0007\u0006\u0000\u0000\u008f\u0090\u0007"+
		"\n\u0000\u0000\u0090\u0091\u0007\u0003\u0000\u0000\u0091\u000e\u0001\u0000"+
		"\u0000\u0000\u0092\u0093\u0007\u0006\u0000\u0000\u0093\u0094\u0007\u0003"+
		"\u0000\u0000\u0094\u0095\u0007\u000b\u0000\u0000\u0095\u0096\u0007\f\u0000"+
		"\u0000\u0096\u0097\u0007\u0006\u0000\u0000\u0097\u0098\u0007\r\u0000\u0000"+
		"\u0098\u0010\u0001\u0000\u0000\u0000\u0099\u009a\u0007\u0001\u0000\u0000"+
		"\u009a\u009b\u0007\u0004\u0000\u0000\u009b\u009c\u0007\n\u0000\u0000\u009c"+
		"\u0012\u0001\u0000\u0000\u0000\u009d\u009e\u0007\u0001\u0000\u0000\u009e"+
		"\u009f\u0007\f\u0000\u0000\u009f\u00a0\u0007\f\u0000\u0000\u00a0\u00a1"+
		"\u0007\u000e\u0000\u0000\u00a1\u0014\u0001\u0000\u0000\u0000\u00a2\u00a3"+
		"\u0007\u000e\u0000\u0000\u00a3\u00a4\u0007\f\u0000\u0000\u00a4\u00a5\u0007"+
		"\f\u0000\u0000\u00a5\u00a6\u0007\u0001\u0000\u0000\u00a6\u0016\u0001\u0000"+
		"\u0000\u0000\u00a7\u00a8\u0007\n\u0000\u0000\u00a8\u00a9\u0007\b\u0000"+
		"\u0000\u00a9\u00aa\u0007\u0004\u0000\u0000\u00aa\u00ab\u0007\u0007\u0000"+
		"\u0000\u00ab\u0018\u0001\u0000\u0000\u0000\u00ac\u00ad\u0007\u000f\u0000"+
		"\u0000\u00ad\u00ae\u0007\b\u0000\u0000\u00ae\u00af\u0007\u0006\u0000\u0000"+
		"\u00af\u00b0\u0007\u0001\u0000\u0000\u00b0\u00b1\u0007\u0004\u0000\u0000"+
		"\u00b1\u001a\u0001\u0000\u0000\u0000\u00b2\u00b3\u0007\u0000\u0000\u0000"+
		"\u00b3\u00b4\u0007\u0002\u0000\u0000\u00b4\u00b5\u0007\u0003\u0000\u0000"+
		"\u00b5\u00b6\u0007\u0004\u0000\u0000\u00b6\u001c\u0001\u0000\u0000\u0000"+
		"\u00b7\u00b8\u0007\u0004\u0000\u0000\u00b8\u00b9\u0007\u0003\u0000\u0000"+
		"\u00b9\u00ba\u0007\u0002\u0000\u0000\u00ba\u00bb\u0007\u0000\u0000\u0000"+
		"\u00bb\u001e\u0001\u0000\u0000\u0000\u00bc\u00bd\u0007\u0007\u0000\u0000"+
		"\u00bd\u00be\u0007\u0004\u0000\u0000\u00be\u00bf\u0007\u000f\u0000\u0000"+
		"\u00bf \u0001\u0000\u0000\u0000\u00c0\u00c1\u0007\f\u0000\u0000\u00c1"+
		"\u00c2\u0007\u0005\u0000\u0000\u00c2\"\u0001\u0000\u0000\u0000\u00c3\u00c4"+
		"\u0007\u0007\u0000\u0000\u00c4\u00c5\u0007\f\u0000\u0000\u00c5\u00c6\u0007"+
		"\n\u0000\u0000\u00c6$\u0001\u0000\u0000\u0000\u00c7\u00c8\u0005t\u0000"+
		"\u0000\u00c8\u00c9\u0007\t\u0000\u0000\u00c9\u00ca\u0007\u0010\u0000\u0000"+
		"\u00ca\u00cb\u0007\u0004\u0000\u0000\u00cb&\u0001\u0000\u0000\u0000\u00cc"+
		"\u00cd\u0005(\u0000\u0000\u00cd(\u0001\u0000\u0000\u0000\u00ce\u00cf\u0005"+
		")\u0000\u0000\u00cf*\u0001\u0000\u0000\u0000\u00d0\u00d1\u0005{\u0000"+
		"\u0000\u00d1,\u0001\u0000\u0000\u0000\u00d2\u00d3\u0005}\u0000\u0000\u00d3"+
		".\u0001\u0000\u0000\u0000\u00d4\u00d5\u0005,\u0000\u0000\u00d50\u0001"+
		"\u0000\u0000\u0000\u00d6\u00d7\u0005:\u0000\u0000\u00d72\u0001\u0000\u0000"+
		"\u0000\u00d8\u00d9\u0005;\u0000\u0000\u00d94\u0001\u0000\u0000\u0000\u00da"+
		"\u00db\u0005<\u0000\u0000\u00db\u00dc\u0005-\u0000\u0000\u00dc6\u0001"+
		"\u0000\u0000\u0000\u00dd\u00de\u0005=\u0000\u0000\u00de\u00df\u0005>\u0000"+
		"\u0000\u00df8\u0001\u0000\u0000\u0000\u00e0\u00e1\u0005+\u0000\u0000\u00e1"+
		":\u0001\u0000\u0000\u0000\u00e2\u00e3\u0005-\u0000\u0000\u00e3<\u0001"+
		"\u0000\u0000\u0000\u00e4\u00e5\u0005*\u0000\u0000\u00e5>\u0001\u0000\u0000"+
		"\u0000\u00e6\u00e7\u0005/\u0000\u0000\u00e7@\u0001\u0000\u0000\u0000\u00e8"+
		"\u00e9\u0005~\u0000\u0000\u00e9B\u0001\u0000\u0000\u0000\u00ea\u00eb\u0005"+
		"<\u0000\u0000\u00ebD\u0001\u0000\u0000\u0000\u00ec\u00ed\u0005<\u0000"+
		"\u0000\u00ed\u00ee\u0005=\u0000\u0000\u00eeF\u0001\u0000\u0000\u0000\u00ef"+
		"\u00f0\u0005=\u0000\u0000\u00f0H\u0001\u0000\u0000\u0000\u00f1\u00f2\u0005"+
		".\u0000\u0000\u00f2J\u0001\u0000\u0000\u0000\u00f3\u00f4\u0005@\u0000"+
		"\u0000\u00f4L\u0001\u0000\u0000\u0000\u00f5\u00f6\u0005\"\u0000\u0000"+
		"\u00f6N\u0001\u0000\u0000\u0000\u00f7\u00f8\u0007\u0011\u0000\u0000\u00f8"+
		"P\u0001\u0000\u0000\u0000\u00f9\u00fa\u0007\u0012\u0000\u0000\u00faR\u0001"+
		"\u0000\u0000\u0000\u00fb\u00fd\u0003O\'\u0000\u00fc\u00fb\u0001\u0000"+
		"\u0000\u0000\u00fd\u00fe\u0001\u0000\u0000\u0000\u00fe\u00fc\u0001\u0000"+
		"\u0000\u0000\u00fe\u00ff\u0001\u0000\u0000\u0000\u00ff\u0106\u0001\u0000"+
		"\u0000\u0000\u0100\u0105\u0005_\u0000\u0000\u0101\u0105\u0003O\'\u0000"+
		"\u0102\u0105\u0003Q(\u0000\u0103\u0105\u0003e2\u0000\u0104\u0100\u0001"+
		"\u0000\u0000\u0000\u0104\u0101\u0001\u0000\u0000\u0000\u0104\u0102\u0001"+
		"\u0000\u0000\u0000\u0104\u0103\u0001\u0000\u0000\u0000\u0105\u0108\u0001"+
		"\u0000\u0000\u0000\u0106\u0104\u0001\u0000\u0000\u0000\u0106\u0107\u0001"+
		"\u0000\u0000\u0000\u0107T\u0001\u0000\u0000\u0000\u0108\u0106\u0001\u0000"+
		"\u0000\u0000\u0109\u010b\u0003Q(\u0000\u010a\u0109\u0001\u0000\u0000\u0000"+
		"\u010b\u010c\u0001\u0000\u0000\u0000\u010c\u010a\u0001\u0000\u0000\u0000"+
		"\u010c\u010d\u0001\u0000\u0000\u0000\u010d\u0114\u0001\u0000\u0000\u0000"+
		"\u010e\u0113\u0005_\u0000\u0000\u010f\u0113\u0003O\'\u0000\u0110\u0113"+
		"\u0003Q(\u0000\u0111\u0113\u0003e2\u0000\u0112\u010e\u0001\u0000\u0000"+
		"\u0000\u0112\u010f\u0001\u0000\u0000\u0000\u0112\u0110\u0001\u0000\u0000"+
		"\u0000\u0112\u0111\u0001\u0000\u0000\u0000\u0113\u0116\u0001\u0000\u0000"+
		"\u0000\u0114\u0112\u0001\u0000\u0000\u0000\u0114\u0115\u0001\u0000\u0000"+
		"\u0000\u0115V\u0001\u0000\u0000\u0000\u0116\u0114\u0001\u0000\u0000\u0000"+
		"\u0117\u0119\u0005\r\u0000\u0000\u0118\u0117\u0001\u0000\u0000\u0000\u0118"+
		"\u0119\u0001\u0000\u0000\u0000\u0119\u011a\u0001\u0000\u0000\u0000\u011a"+
		"\u011b\u0005\n\u0000\u0000\u011bX\u0001\u0000\u0000\u0000\u011c\u011d"+
		"\u0005-\u0000\u0000\u011d\u011e\u0005-\u0000\u0000\u011eZ\u0001\u0000"+
		"\u0000\u0000\u011f\u0120\u0005(\u0000\u0000\u0120\u0121\u0005*\u0000\u0000"+
		"\u0121\\\u0001\u0000\u0000\u0000\u0122\u0123\u0005*\u0000\u0000\u0123"+
		"\u0124\u0005)\u0000\u0000\u0124^\u0001\u0000\u0000\u0000\u0125\u0129\u0003"+
		"Y,\u0000\u0126\u0128\t\u0000\u0000\u0000\u0127\u0126\u0001\u0000\u0000"+
		"\u0000\u0128\u012b\u0001\u0000\u0000\u0000\u0129\u012a\u0001\u0000\u0000"+
		"\u0000\u0129\u0127\u0001\u0000\u0000\u0000\u012a\u012e\u0001\u0000\u0000"+
		"\u0000\u012b\u0129\u0001\u0000\u0000\u0000\u012c\u012f\u0003W+\u0000\u012d"+
		"\u012f\u0005\u0000\u0000\u0001\u012e\u012c\u0001\u0000\u0000\u0000\u012e"+
		"\u012d\u0001\u0000\u0000\u0000\u012f\u0130\u0001\u0000\u0000\u0000\u0130"+
		"\u0131\u0006/\u0000\u0000\u0131`\u0001\u0000\u0000\u0000\u0132\u0137\u0003"+
		"[-\u0000\u0133\u0136\u0003a0\u0000\u0134\u0136\t\u0000\u0000\u0000\u0135"+
		"\u0133\u0001\u0000\u0000\u0000\u0135\u0134\u0001\u0000\u0000\u0000\u0136"+
		"\u0139\u0001\u0000\u0000\u0000\u0137\u0138\u0001\u0000\u0000\u0000\u0137"+
		"\u0135\u0001\u0000\u0000\u0000\u0138\u013f\u0001\u0000\u0000\u0000\u0139"+
		"\u0137\u0001\u0000\u0000\u0000\u013a\u013b\u0003].\u0000\u013b\u013c\u0006"+
		"0\u0001\u0000\u013c\u0140\u0001\u0000\u0000\u0000\u013d\u013e\u0005\u0000"+
		"\u0000\u0001\u013e\u0140\u00060\u0002\u0000\u013f\u013a\u0001\u0000\u0000"+
		"\u0000\u013f\u013d\u0001\u0000\u0000\u0000\u0140b\u0001\u0000\u0000\u0000"+
		"\u0141\u0142\u0003].\u0000\u0142\u0143\u00061\u0003\u0000\u0143d\u0001"+
		"\u0000\u0000\u0000\u0144\u0145\u0007\u0013\u0000\u0000\u0145f\u0001\u0000"+
		"\u0000\u0000\u0146\u0148\u0003e2\u0000\u0147\u0146\u0001\u0000\u0000\u0000"+
		"\u0148\u0149\u0001\u0000\u0000\u0000\u0149\u0147\u0001\u0000\u0000\u0000"+
		"\u0149\u014a\u0001\u0000\u0000\u0000\u014ah\u0001\u0000\u0000\u0000\u014b"+
		"\u0153\u0003M&\u0000\u014c\u014d\u0005\\\u0000\u0000\u014d\u0152\u0005"+
		"\"\u0000\u0000\u014e\u014f\u0005\\\u0000\u0000\u014f\u0152\u0003W+\u0000"+
		"\u0150\u0152\t\u0000\u0000\u0000\u0151\u014c\u0001\u0000\u0000\u0000\u0151"+
		"\u014e\u0001\u0000\u0000\u0000\u0151\u0150\u0001\u0000\u0000\u0000\u0152"+
		"\u0155\u0001\u0000\u0000\u0000\u0153\u0154\u0001\u0000\u0000\u0000\u0153"+
		"\u0151\u0001\u0000\u0000\u0000\u0154\u015e\u0001\u0000\u0000\u0000\u0155"+
		"\u0153\u0001\u0000\u0000\u0000\u0156\u0157\u0003M&\u0000\u0157\u0158\u0006"+
		"4\u0004\u0000\u0158\u015f\u0001\u0000\u0000\u0000\u0159\u015a\u0005\u0000"+
		"\u0000\u0001\u015a\u015f\u00064\u0005\u0000\u015b\u015c\u0003W+\u0000"+
		"\u015c\u015d\u00064\u0006\u0000\u015d\u015f\u0001\u0000\u0000\u0000\u015e"+
		"\u0156\u0001\u0000\u0000\u0000\u015e\u0159\u0001\u0000\u0000\u0000\u015e"+
		"\u015b\u0001\u0000\u0000\u0000\u015fj\u0001\u0000\u0000\u0000\u0160\u0162"+
		"\u0007\u0014\u0000\u0000\u0161\u0160\u0001\u0000\u0000\u0000\u0162\u0163"+
		"\u0001\u0000\u0000\u0000\u0163\u0161\u0001\u0000\u0000\u0000\u0163\u0164"+
		"\u0001\u0000\u0000\u0000\u0164\u0165\u0001\u0000\u0000\u0000\u0165\u0166"+
		"\u00065\u0000\u0000\u0166l\u0001\u0000\u0000\u0000\u0167\u0168\t\u0000"+
		"\u0000\u0000\u0168\u0169\u00066\u0007\u0000\u0169n\u0001\u0000\u0000\u0000"+
		"\u0012\u0000\u00fe\u0104\u0106\u010c\u0112\u0114\u0118\u0129\u012e\u0135"+
		"\u0137\u013f\u0149\u0151\u0153\u015e\u0163\b\u0006\u0000\u0000\u00010"+
		"\u0000\u00010\u0001\u00011\u0002\u00014\u0003\u00014\u0004\u00014\u0005"+
		"\u00016\u0006";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}