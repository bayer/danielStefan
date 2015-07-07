// Generated from TinyC.g4 by ANTLR 4.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TRGenLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__22=1, T__21=2, T__20=3, T__19=4, T__18=5, T__17=6, T__16=7, T__15=8, 
		T__14=9, T__13=10, T__12=11, T__11=12, T__10=13, T__9=14, T__8=15, T__7=16, 
		T__6=17, T__5=18, T__4=19, T__3=20, T__2=21, T__1=22, T__0=23, INT=24, 
		WHITESPACE=25, VARNAME=26, COMMENT=27, LINE_COMMENT=28;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'%'", "')'", "'+'", "'-'", "'*'", "'while'", "'('", "'if'", "'int'", 
		"'<'", "'='", "'!='", "';'", "'main()'", "'<='", "'{'", "'>'", "'/'", 
		"'=='", "'else'", "'}'", "'>='", "'assert'", "INT", "WHITESPACE", "VARNAME", 
		"COMMENT", "LINE_COMMENT"
	};
	public static final String[] ruleNames = {
		"T__22", "T__21", "T__20", "T__19", "T__18", "T__17", "T__16", "T__15", 
		"T__14", "T__13", "T__12", "T__11", "T__10", "T__9", "T__8", "T__7", "T__6", 
		"T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "INT", "WHITESPACE", "VARNAME", 
		"COMMENT", "LINE_COMMENT"
	};


	public TRGenLexer(CharStream input) {
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
		case 24: WHITESPACE_action((RuleContext)_localctx, actionIndex); break;

		case 26: COMMENT_action((RuleContext)_localctx, actionIndex); break;

		case 27: LINE_COMMENT_action((RuleContext)_localctx, actionIndex); break;
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
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\36\u00b0\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\3\2\3\3\3\3\3\4\3\4"+
		"\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3"+
		"\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\24"+
		"\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\31\6\31\u0083\n\31\r\31\16\31\u0084\3\32\6\32\u0088"+
		"\n\32\r\32\16\32\u0089\3\32\3\32\3\33\6\33\u008f\n\33\r\33\16\33\u0090"+
		"\3\34\3\34\3\34\3\34\7\34\u0097\n\34\f\34\16\34\u009a\13\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\35\3\35\3\35\3\35\7\35\u00a5\n\35\f\35\16\35\u00a8\13"+
		"\35\3\35\5\35\u00ab\n\35\3\35\3\35\3\35\3\35\3\u0098\36\3\3\1\5\4\1\7"+
		"\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13\1\25\f\1\27\r\1\31\16\1\33"+
		"\17\1\35\20\1\37\21\1!\22\1#\23\1%\24\1\'\25\1)\26\1+\27\1-\30\1/\31\1"+
		"\61\32\1\63\33\2\65\34\1\67\35\39\36\4\3\2\6\3\2\62;\5\2\13\f\16\17\""+
		"\"\3\2c|\4\2\f\f\17\17\u00b5\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3"+
		"\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2"+
		"\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37"+
		"\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3"+
		"\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2"+
		"\67\3\2\2\2\29\3\2\2\2\3;\3\2\2\2\5=\3\2\2\2\7?\3\2\2\2\tA\3\2\2\2\13"+
		"C\3\2\2\2\rE\3\2\2\2\17K\3\2\2\2\21M\3\2\2\2\23P\3\2\2\2\25T\3\2\2\2\27"+
		"V\3\2\2\2\31X\3\2\2\2\33[\3\2\2\2\35]\3\2\2\2\37d\3\2\2\2!g\3\2\2\2#i"+
		"\3\2\2\2%k\3\2\2\2\'m\3\2\2\2)p\3\2\2\2+u\3\2\2\2-w\3\2\2\2/z\3\2\2\2"+
		"\61\u0082\3\2\2\2\63\u0087\3\2\2\2\65\u008e\3\2\2\2\67\u0092\3\2\2\29"+
		"\u00a0\3\2\2\2;<\7\'\2\2<\4\3\2\2\2=>\7+\2\2>\6\3\2\2\2?@\7-\2\2@\b\3"+
		"\2\2\2AB\7/\2\2B\n\3\2\2\2CD\7,\2\2D\f\3\2\2\2EF\7y\2\2FG\7j\2\2GH\7k"+
		"\2\2HI\7n\2\2IJ\7g\2\2J\16\3\2\2\2KL\7*\2\2L\20\3\2\2\2MN\7k\2\2NO\7h"+
		"\2\2O\22\3\2\2\2PQ\7k\2\2QR\7p\2\2RS\7v\2\2S\24\3\2\2\2TU\7>\2\2U\26\3"+
		"\2\2\2VW\7?\2\2W\30\3\2\2\2XY\7#\2\2YZ\7?\2\2Z\32\3\2\2\2[\\\7=\2\2\\"+
		"\34\3\2\2\2]^\7o\2\2^_\7c\2\2_`\7k\2\2`a\7p\2\2ab\7*\2\2bc\7+\2\2c\36"+
		"\3\2\2\2de\7>\2\2ef\7?\2\2f \3\2\2\2gh\7}\2\2h\"\3\2\2\2ij\7@\2\2j$\3"+
		"\2\2\2kl\7\61\2\2l&\3\2\2\2mn\7?\2\2no\7?\2\2o(\3\2\2\2pq\7g\2\2qr\7n"+
		"\2\2rs\7u\2\2st\7g\2\2t*\3\2\2\2uv\7\177\2\2v,\3\2\2\2wx\7@\2\2xy\7?\2"+
		"\2y.\3\2\2\2z{\7c\2\2{|\7u\2\2|}\7u\2\2}~\7g\2\2~\177\7t\2\2\177\u0080"+
		"\7v\2\2\u0080\60\3\2\2\2\u0081\u0083\t\2\2\2\u0082\u0081\3\2\2\2\u0083"+
		"\u0084\3\2\2\2\u0084\u0082\3\2\2\2\u0084\u0085\3\2\2\2\u0085\62\3\2\2"+
		"\2\u0086\u0088\t\3\2\2\u0087\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u0087"+
		"\3\2\2\2\u0089\u008a\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u008c\b\32\2\2"+
		"\u008c\64\3\2\2\2\u008d\u008f\t\4\2\2\u008e\u008d\3\2\2\2\u008f\u0090"+
		"\3\2\2\2\u0090\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091\66\3\2\2\2\u0092"+
		"\u0093\7\61\2\2\u0093\u0094\7,\2\2\u0094\u0098\3\2\2\2\u0095\u0097\13"+
		"\2\2\2\u0096\u0095\3\2\2\2\u0097\u009a\3\2\2\2\u0098\u0099\3\2\2\2\u0098"+
		"\u0096\3\2\2\2\u0099\u009b\3\2\2\2\u009a\u0098\3\2\2\2\u009b\u009c\7,"+
		"\2\2\u009c\u009d\7\61\2\2\u009d\u009e\3\2\2\2\u009e\u009f\b\34\3\2\u009f"+
		"8\3\2\2\2\u00a0\u00a1\7\61\2\2\u00a1\u00a2\7\61\2\2\u00a2\u00a6\3\2\2"+
		"\2\u00a3\u00a5\n\5\2\2\u00a4\u00a3\3\2\2\2\u00a5\u00a8\3\2\2\2\u00a6\u00a4"+
		"\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00aa\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9"+
		"\u00ab\7\17\2\2\u00aa\u00a9\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\u00ac\3"+
		"\2\2\2\u00ac\u00ad\7\f\2\2\u00ad\u00ae\3\2\2\2\u00ae\u00af\b\35\4\2\u00af"+
		":\3\2\2\2\t\2\u0084\u0089\u0090\u0098\u00a6\u00aa";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}