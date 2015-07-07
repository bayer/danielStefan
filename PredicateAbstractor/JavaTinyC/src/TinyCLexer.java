// Generated from TinyC.g4 by ANTLR 4.0
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TinyCLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__16=1, T__15=2, T__14=3, T__13=4, T__12=5, T__11=6, T__10=7, T__9=8, 
		T__8=9, T__7=10, T__6=11, T__5=12, T__4=13, T__3=14, T__2=15, T__1=16, 
		T__0=17, INT=18, WHITESPACE=19, ID=20, COMMENT=21, LINE_COMMENT=22;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'input'", "')'", "','", "'+'", "'while'", "'-'", "'('", "'if'", "'<'", 
		"'='", "';'", "'{'", "'output'", "'=='", "'}'", "'assert'", "'!'", "INT", 
		"WHITESPACE", "ID", "COMMENT", "LINE_COMMENT"
	};
	public static final String[] ruleNames = {
		"T__16", "T__15", "T__14", "T__13", "T__12", "T__11", "T__10", "T__9", 
		"T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", 
		"INT", "WHITESPACE", "ID", "COMMENT", "LINE_COMMENT"
	};


	public TinyCLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "TinyC.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 18: WHITESPACE_action((RuleContext)_localctx, actionIndex); break;

		case 20: COMMENT_action((RuleContext)_localctx, actionIndex); break;

		case 21: LINE_COMMENT_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void WHITESPACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: _channel = HIDDEN;  break;
		}
	}
	private void LINE_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2: skip();  break;
		}
	}
	private void COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1: skip();  break;
		}
	}

	public static final String _serializedATN =
		"\2\4\30\u0091\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b"+
		"\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20"+
		"\t\20\4\21\t\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27"+
		"\t\27\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\23\6\23g\n\23\r\23\16\23h\3\24\6"+
		"\24l\n\24\r\24\16\24m\3\24\3\24\3\25\3\25\3\26\3\26\3\26\3\26\7\26x\n"+
		"\26\f\26\16\26{\13\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\7\27"+
		"\u0086\n\27\f\27\16\27\u0089\13\27\3\27\5\27\u008c\n\27\3\27\3\27\3\27"+
		"\3\27\3y\30\3\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13\1"+
		"\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37\21\1!\22\1#\23\1%\24\1\'\25"+
		"\2)\26\1+\27\3-\30\4\3\2\6\3\62;\5\13\f\16\17\"\"\3c|\4\f\f\17\17\u0095"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\3/\3\2\2"+
		"\2\5\65\3\2\2\2\7\67\3\2\2\2\t9\3\2\2\2\13;\3\2\2\2\rA\3\2\2\2\17C\3\2"+
		"\2\2\21E\3\2\2\2\23H\3\2\2\2\25J\3\2\2\2\27L\3\2\2\2\31N\3\2\2\2\33P\3"+
		"\2\2\2\35W\3\2\2\2\37Z\3\2\2\2!\\\3\2\2\2#c\3\2\2\2%f\3\2\2\2\'k\3\2\2"+
		"\2)q\3\2\2\2+s\3\2\2\2-\u0081\3\2\2\2/\60\7k\2\2\60\61\7p\2\2\61\62\7"+
		"r\2\2\62\63\7w\2\2\63\64\7v\2\2\64\4\3\2\2\2\65\66\7+\2\2\66\6\3\2\2\2"+
		"\678\7.\2\28\b\3\2\2\29:\7-\2\2:\n\3\2\2\2;<\7y\2\2<=\7j\2\2=>\7k\2\2"+
		">?\7n\2\2?@\7g\2\2@\f\3\2\2\2AB\7/\2\2B\16\3\2\2\2CD\7*\2\2D\20\3\2\2"+
		"\2EF\7k\2\2FG\7h\2\2G\22\3\2\2\2HI\7>\2\2I\24\3\2\2\2JK\7?\2\2K\26\3\2"+
		"\2\2LM\7=\2\2M\30\3\2\2\2NO\7}\2\2O\32\3\2\2\2PQ\7q\2\2QR\7w\2\2RS\7v"+
		"\2\2ST\7r\2\2TU\7w\2\2UV\7v\2\2V\34\3\2\2\2WX\7?\2\2XY\7?\2\2Y\36\3\2"+
		"\2\2Z[\7\177\2\2[ \3\2\2\2\\]\7c\2\2]^\7u\2\2^_\7u\2\2_`\7g\2\2`a\7t\2"+
		"\2ab\7v\2\2b\"\3\2\2\2cd\7#\2\2d$\3\2\2\2eg\t\2\2\2fe\3\2\2\2gh\3\2\2"+
		"\2hf\3\2\2\2hi\3\2\2\2i&\3\2\2\2jl\t\3\2\2kj\3\2\2\2lm\3\2\2\2mk\3\2\2"+
		"\2mn\3\2\2\2no\3\2\2\2op\b\24\2\2p(\3\2\2\2qr\t\4\2\2r*\3\2\2\2st\7\61"+
		"\2\2tu\7,\2\2uy\3\2\2\2vx\13\2\2\2wv\3\2\2\2x{\3\2\2\2yz\3\2\2\2yw\3\2"+
		"\2\2z|\3\2\2\2{y\3\2\2\2|}\7,\2\2}~\7\61\2\2~\177\3\2\2\2\177\u0080\b"+
		"\26\3\2\u0080,\3\2\2\2\u0081\u0082\7\61\2\2\u0082\u0083\7\61\2\2\u0083"+
		"\u0087\3\2\2\2\u0084\u0086\n\5\2\2\u0085\u0084\3\2\2\2\u0086\u0089\3\2"+
		"\2\2\u0087\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u008b\3\2\2\2\u0089"+
		"\u0087\3\2\2\2\u008a\u008c\7\17\2\2\u008b\u008a\3\2\2\2\u008b\u008c\3"+
		"\2\2\2\u008c\u008d\3\2\2\2\u008d\u008e\7\f\2\2\u008e\u008f\3\2\2\2\u008f"+
		"\u0090\b\27\4\2\u0090.\3\2\2\2\b\2hmy\u0087\u008b";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}