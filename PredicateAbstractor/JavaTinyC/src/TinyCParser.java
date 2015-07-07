// Generated from TinyC.g4 by ANTLR 4.0
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TinyCParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__16=1, T__15=2, T__14=3, T__13=4, T__12=5, T__11=6, T__10=7, T__9=8, 
		T__8=9, T__7=10, T__6=11, T__5=12, T__4=13, T__3=14, T__2=15, T__1=16, 
		T__0=17, INT=18, WHITESPACE=19, ID=20, COMMENT=21, LINE_COMMENT=22;
	public static final String[] tokenNames = {
		"<INVALID>", "'input'", "')'", "','", "'+'", "'while'", "'-'", "'('", 
		"'if'", "'<'", "'='", "';'", "'{'", "'output'", "'=='", "'}'", "'assert'", 
		"'!'", "INT", "WHITESPACE", "ID", "COMMENT", "LINE_COMMENT"
	};
	public static final int
		RULE_id = 0, RULE_prog = 1, RULE_statement = 2, RULE_programExpr = 3, 
		RULE_ifStatement = 4, RULE_whileStatement = 5, RULE_assertStatement = 6, 
		RULE_expr = 7, RULE_notTest = 8, RULE_asgn = 9, RULE_test = 10, RULE_sum = 11, 
		RULE_integer = 12, RULE_term = 13, RULE_inputId = 14, RULE_listOfInputIds = 15, 
		RULE_listOfIds = 16, RULE_input = 17, RULE_output = 18;
	public static final String[] ruleNames = {
		"id", "prog", "statement", "programExpr", "ifStatement", "whileStatement", 
		"assertStatement", "expr", "notTest", "asgn", "test", "sum", "integer", 
		"term", "inputId", "listOfInputIds", "listOfIds", "input", "output"
	};

	@Override
	public String getGrammarFileName() { return "TinyC.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public TinyCParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class IdContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(TinyCParser.ID, 0); }
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitId(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_id);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38); match(ID);
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

	public static class ProgContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public InputContext input() {
			return getRuleContext(InputContext.class,0);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public OutputContext output() {
			return getRuleContext(OutputContext.class,0);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitProg(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_prog);
		int _la;
		try {
			setState(68);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(43);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 5) | (1L << 7) | (1L << 8) | (1L << 11) | (1L << 12) | (1L << 16) | (1L << 17) | (1L << INT) | (1L << ID))) != 0)) {
					{
					{
					setState(40); statement();
					}
					}
					setState(45);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(46); input();
				setState(50);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 5) | (1L << 7) | (1L << 8) | (1L << 11) | (1L << 12) | (1L << 16) | (1L << 17) | (1L << INT) | (1L << ID))) != 0)) {
					{
					{
					setState(47); statement();
					}
					}
					setState(52);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(53); input();
				setState(54); output();
				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 5) | (1L << 7) | (1L << 8) | (1L << 11) | (1L << 12) | (1L << 16) | (1L << 17) | (1L << INT) | (1L << ID))) != 0)) {
					{
					{
					setState(55); statement();
					}
					}
					setState(60);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(61); output();
				setState(65);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 5) | (1L << 7) | (1L << 8) | (1L << 11) | (1L << 12) | (1L << 16) | (1L << 17) | (1L << INT) | (1L << ID))) != 0)) {
					{
					{
					setState(62); statement();
					}
					}
					setState(67);
					_errHandler.sync(this);
					_la = _input.LA(1);
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

	public static class StatementContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public WhileStatementContext whileStatement() {
			return getRuleContext(WhileStatementContext.class,0);
		}
		public ProgramExprContext programExpr() {
			return getRuleContext(ProgramExprContext.class,0);
		}
		public AssertStatementContext assertStatement() {
			return getRuleContext(AssertStatementContext.class,0);
		}
		public IfStatementContext ifStatement() {
			return getRuleContext(IfStatementContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitStatement(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_statement);
		int _la;
		try {
			setState(85);
			switch (_input.LA(1)) {
			case 8:
				enterOuterAlt(_localctx, 1);
				{
				setState(70); ifStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 2);
				{
				setState(71); whileStatement();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 3);
				{
				setState(72); match(12);
				setState(74); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(73); statement();
					}
					}
					setState(76); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 5) | (1L << 7) | (1L << 8) | (1L << 11) | (1L << 12) | (1L << 16) | (1L << 17) | (1L << INT) | (1L << ID))) != 0) );
				setState(78); match(15);
				}
				break;
			case 7:
			case 17:
			case INT:
			case ID:
				enterOuterAlt(_localctx, 4);
				{
				setState(80); programExpr();
				setState(81); match(11);
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 5);
				{
				setState(83); assertStatement();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 6);
				{
				setState(84); match(11);
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class ProgramExprContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ProgramExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_programExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterProgramExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitProgramExpr(this);
		}
	}

	public final ProgramExprContext programExpr() throws RecognitionException {
		ProgramExprContext _localctx = new ProgramExprContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_programExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87); expr();
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

	public static class IfStatementContext extends ParserRuleContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public IfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitIfStatement(this);
		}
	}

	public final IfStatementContext ifStatement() throws RecognitionException {
		IfStatementContext _localctx = new IfStatementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_ifStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89); match(8);
			setState(90); match(7);
			setState(91); expr();
			setState(92); match(2);
			setState(93); statement();
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

	public static class WhileStatementContext extends ParserRuleContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public WhileStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterWhileStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitWhileStatement(this);
		}
	}

	public final WhileStatementContext whileStatement() throws RecognitionException {
		WhileStatementContext _localctx = new WhileStatementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_whileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95); match(5);
			setState(96); match(7);
			setState(97); expr();
			setState(98); match(2);
			setState(99); statement();
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

	public static class AssertStatementContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AssertStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assertStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterAssertStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitAssertStatement(this);
		}
	}

	public final AssertStatementContext assertStatement() throws RecognitionException {
		AssertStatementContext _localctx = new AssertStatementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_assertStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101); match(16);
			setState(102); match(7);
			setState(103); expr();
			setState(104); match(2);
			setState(105); match(11);
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
		public TestContext test() {
			return getRuleContext(TestContext.class,0);
		}
		public SumContext sum() {
			return getRuleContext(SumContext.class,0);
		}
		public AsgnContext asgn() {
			return getRuleContext(AsgnContext.class,0);
		}
		public NotTestContext notTest() {
			return getRuleContext(NotTestContext.class,0);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_expr);
		try {
			setState(111);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(107); sum(0);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(108); test();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(109); asgn();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(110); notTest();
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

	public static class NotTestContext extends ParserRuleContext {
		public TestContext test() {
			return getRuleContext(TestContext.class,0);
		}
		public NotTestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notTest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterNotTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitNotTest(this);
		}
	}

	public final NotTestContext notTest() throws RecognitionException {
		NotTestContext _localctx = new NotTestContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_notTest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113); match(17);
			setState(114); test();
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

	public static class AsgnContext extends ParserRuleContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AsgnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asgn; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterAsgn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitAsgn(this);
		}
	}

	public final AsgnContext asgn() throws RecognitionException {
		AsgnContext _localctx = new AsgnContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_asgn);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116); id();
			setState(117); match(10);
			setState(118); expr();
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

	public static class TestContext extends ParserRuleContext {
		public SumContext sum(int i) {
			return getRuleContext(SumContext.class,i);
		}
		public List<SumContext> sum() {
			return getRuleContexts(SumContext.class);
		}
		public TestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_test; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitTest(this);
		}
	}

	public final TestContext test() throws RecognitionException {
		TestContext _localctx = new TestContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_test);
		try {
			setState(129);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(120); sum(0);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(121); sum(0);
				setState(122); match(9);
				setState(123); sum(0);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(125); sum(0);
				setState(126); match(14);
				setState(127); sum(0);
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

	public static class SumContext extends ParserRuleContext {
		public int _p;
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public SumContext sum() {
			return getRuleContext(SumContext.class,0);
		}
		public SumContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public SumContext(ParserRuleContext parent, int invokingState, int _p) {
			super(parent, invokingState);
			this._p = _p;
		}
		@Override public int getRuleIndex() { return RULE_sum; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterSum(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitSum(this);
		}
	}

	public final SumContext sum(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		SumContext _localctx = new SumContext(_ctx, _parentState, _p);
		SumContext _prevctx = _localctx;
		int _startState = 22;
		enterRecursionRule(_localctx, RULE_sum);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(132); term();
			}
			_ctx.stop = _input.LT(-1);
			setState(142);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(140);
					switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
					case 1:
						{
						_localctx = new SumContext(_parentctx, _parentState, _p);
						pushNewRecursionContext(_localctx, _startState, RULE_sum);
						setState(134);
						if (!(2 >= _localctx._p)) throw new FailedPredicateException(this, "2 >= $_p");
						setState(135); match(4);
						setState(136); term();
						}
						break;

					case 2:
						{
						_localctx = new SumContext(_parentctx, _parentState, _p);
						pushNewRecursionContext(_localctx, _startState, RULE_sum);
						setState(137);
						if (!(1 >= _localctx._p)) throw new FailedPredicateException(this, "1 >= $_p");
						setState(138); match(6);
						setState(139); term();
						}
						break;
					}
					} 
				}
				setState(144);
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

	public static class IntegerContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(TinyCParser.INT, 0); }
		public IntegerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterInteger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitInteger(this);
		}
	}

	public final IntegerContext integer() throws RecognitionException {
		IntegerContext _localctx = new IntegerContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_integer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145); match(INT);
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

	public static class TermContext extends ParserRuleContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public IntegerContext integer() {
			return getRuleContext(IntegerContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitTerm(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_term);
		try {
			setState(153);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(147); id();
				}
				break;
			case INT:
				enterOuterAlt(_localctx, 2);
				{
				setState(148); integer();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 3);
				{
				setState(149); match(7);
				setState(150); expr();
				setState(151); match(2);
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class InputIdContext extends ParserRuleContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public InputIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inputId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterInputId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitInputId(this);
		}
	}

	public final InputIdContext inputId() throws RecognitionException {
		InputIdContext _localctx = new InputIdContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_inputId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155); id();
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

	public static class ListOfInputIdsContext extends ParserRuleContext {
		public ListOfInputIdsContext listOfInputIds() {
			return getRuleContext(ListOfInputIdsContext.class,0);
		}
		public InputIdContext inputId() {
			return getRuleContext(InputIdContext.class,0);
		}
		public ListOfInputIdsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listOfInputIds; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterListOfInputIds(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitListOfInputIds(this);
		}
	}

	public final ListOfInputIdsContext listOfInputIds() throws RecognitionException {
		ListOfInputIdsContext _localctx = new ListOfInputIdsContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_listOfInputIds);
		try {
			setState(162);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(157); inputId();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(158); inputId();
				setState(159); match(3);
				setState(160); listOfInputIds();
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

	public static class ListOfIdsContext extends ParserRuleContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public ListOfIdsContext listOfIds() {
			return getRuleContext(ListOfIdsContext.class,0);
		}
		public ListOfIdsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listOfIds; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterListOfIds(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitListOfIds(this);
		}
	}

	public final ListOfIdsContext listOfIds() throws RecognitionException {
		ListOfIdsContext _localctx = new ListOfIdsContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_listOfIds);
		try {
			setState(169);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(164); id();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(165); id();
				setState(166); match(3);
				setState(167); listOfIds();
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

	public static class InputContext extends ParserRuleContext {
		public ListOfInputIdsContext listOfInputIds() {
			return getRuleContext(ListOfInputIdsContext.class,0);
		}
		public InputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_input; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterInput(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitInput(this);
		}
	}

	public final InputContext input() throws RecognitionException {
		InputContext _localctx = new InputContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_input);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171); match(1);
			setState(172); listOfInputIds();
			setState(173); match(11);
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

	public static class OutputContext extends ParserRuleContext {
		public ListOfIdsContext listOfIds() {
			return getRuleContext(ListOfIdsContext.class,0);
		}
		public OutputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_output; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).enterOutput(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyCListener ) ((TinyCListener)listener).exitOutput(this);
		}
	}

	public final OutputContext output() throws RecognitionException {
		OutputContext _localctx = new OutputContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_output);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(175); match(13);
			setState(176); listOfIds();
			setState(177); match(11);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 11: return sum_sempred((SumContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean sum_sempred(SumContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return 2 >= _localctx._p;

		case 1: return 1 >= _localctx._p;
		}
		return true;
	}

	public static final String _serializedATN =
		"\2\3\30\u00b6\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b"+
		"\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t"+
		"\20\4\21\t\21\4\22\t\22\4\23\t\23\4\24\t\24\3\2\3\2\3\3\7\3,\n\3\f\3\16"+
		"\3/\13\3\3\3\3\3\7\3\63\n\3\f\3\16\3\66\13\3\3\3\3\3\3\3\7\3;\n\3\f\3"+
		"\16\3>\13\3\3\3\3\3\7\3B\n\3\f\3\16\3E\13\3\5\3G\n\3\3\4\3\4\3\4\3\4\6"+
		"\4M\n\4\r\4\16\4N\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4X\n\4\3\5\3\5\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3"+
		"\t\3\t\3\t\5\tr\n\t\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\5\f\u0084\n\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\7"+
		"\r\u008f\n\r\f\r\16\r\u0092\13\r\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\5\17\u009c\n\17\3\20\3\20\3\21\3\21\3\21\3\21\3\21\5\21\u00a5\n\21"+
		"\3\22\3\22\3\22\3\22\3\22\5\22\u00ac\n\22\3\23\3\23\3\23\3\23\3\24\3\24"+
		"\3\24\3\24\3\24\2\25\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&\2\2\u00ba"+
		"\2(\3\2\2\2\4F\3\2\2\2\6W\3\2\2\2\bY\3\2\2\2\n[\3\2\2\2\fa\3\2\2\2\16"+
		"g\3\2\2\2\20q\3\2\2\2\22s\3\2\2\2\24v\3\2\2\2\26\u0083\3\2\2\2\30\u0085"+
		"\3\2\2\2\32\u0093\3\2\2\2\34\u009b\3\2\2\2\36\u009d\3\2\2\2 \u00a4\3\2"+
		"\2\2\"\u00ab\3\2\2\2$\u00ad\3\2\2\2&\u00b1\3\2\2\2()\7\26\2\2)\3\3\2\2"+
		"\2*,\5\6\4\2+*\3\2\2\2,/\3\2\2\2-+\3\2\2\2-.\3\2\2\2.G\3\2\2\2/-\3\2\2"+
		"\2\60\64\5$\23\2\61\63\5\6\4\2\62\61\3\2\2\2\63\66\3\2\2\2\64\62\3\2\2"+
		"\2\64\65\3\2\2\2\65G\3\2\2\2\66\64\3\2\2\2\678\5$\23\28<\5&\24\29;\5\6"+
		"\4\2:9\3\2\2\2;>\3\2\2\2<:\3\2\2\2<=\3\2\2\2=G\3\2\2\2><\3\2\2\2?C\5&"+
		"\24\2@B\5\6\4\2A@\3\2\2\2BE\3\2\2\2CA\3\2\2\2CD\3\2\2\2DG\3\2\2\2EC\3"+
		"\2\2\2F-\3\2\2\2F\60\3\2\2\2F\67\3\2\2\2F?\3\2\2\2G\5\3\2\2\2HX\5\n\6"+
		"\2IX\5\f\7\2JL\7\16\2\2KM\5\6\4\2LK\3\2\2\2MN\3\2\2\2NL\3\2\2\2NO\3\2"+
		"\2\2OP\3\2\2\2PQ\7\21\2\2QX\3\2\2\2RS\5\b\5\2ST\7\r\2\2TX\3\2\2\2UX\5"+
		"\16\b\2VX\7\r\2\2WH\3\2\2\2WI\3\2\2\2WJ\3\2\2\2WR\3\2\2\2WU\3\2\2\2WV"+
		"\3\2\2\2X\7\3\2\2\2YZ\5\20\t\2Z\t\3\2\2\2[\\\7\n\2\2\\]\7\t\2\2]^\5\20"+
		"\t\2^_\7\4\2\2_`\5\6\4\2`\13\3\2\2\2ab\7\7\2\2bc\7\t\2\2cd\5\20\t\2de"+
		"\7\4\2\2ef\5\6\4\2f\r\3\2\2\2gh\7\22\2\2hi\7\t\2\2ij\5\20\t\2jk\7\4\2"+
		"\2kl\7\r\2\2l\17\3\2\2\2mr\5\30\r\2nr\5\26\f\2or\5\24\13\2pr\5\22\n\2"+
		"qm\3\2\2\2qn\3\2\2\2qo\3\2\2\2qp\3\2\2\2r\21\3\2\2\2st\7\23\2\2tu\5\26"+
		"\f\2u\23\3\2\2\2vw\5\2\2\2wx\7\f\2\2xy\5\20\t\2y\25\3\2\2\2z\u0084\5\30"+
		"\r\2{|\5\30\r\2|}\7\13\2\2}~\5\30\r\2~\u0084\3\2\2\2\177\u0080\5\30\r"+
		"\2\u0080\u0081\7\20\2\2\u0081\u0082\5\30\r\2\u0082\u0084\3\2\2\2\u0083"+
		"z\3\2\2\2\u0083{\3\2\2\2\u0083\177\3\2\2\2\u0084\27\3\2\2\2\u0085\u0086"+
		"\b\r\1\2\u0086\u0087\5\34\17\2\u0087\u0090\3\2\2\2\u0088\u0089\6\r\2\3"+
		"\u0089\u008a\7\6\2\2\u008a\u008f\5\34\17\2\u008b\u008c\6\r\3\3\u008c\u008d"+
		"\7\b\2\2\u008d\u008f\5\34\17\2\u008e\u0088\3\2\2\2\u008e\u008b\3\2\2\2"+
		"\u008f\u0092\3\2\2\2\u0090\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091\31"+
		"\3\2\2\2\u0092\u0090\3\2\2\2\u0093\u0094\7\24\2\2\u0094\33\3\2\2\2\u0095"+
		"\u009c\5\2\2\2\u0096\u009c\5\32\16\2\u0097\u0098\7\t\2\2\u0098\u0099\5"+
		"\20\t\2\u0099\u009a\7\4\2\2\u009a\u009c\3\2\2\2\u009b\u0095\3\2\2\2\u009b"+
		"\u0096\3\2\2\2\u009b\u0097\3\2\2\2\u009c\35\3\2\2\2\u009d\u009e\5\2\2"+
		"\2\u009e\37\3\2\2\2\u009f\u00a5\5\36\20\2\u00a0\u00a1\5\36\20\2\u00a1"+
		"\u00a2\7\5\2\2\u00a2\u00a3\5 \21\2\u00a3\u00a5\3\2\2\2\u00a4\u009f\3\2"+
		"\2\2\u00a4\u00a0\3\2\2\2\u00a5!\3\2\2\2\u00a6\u00ac\5\2\2\2\u00a7\u00a8"+
		"\5\2\2\2\u00a8\u00a9\7\5\2\2\u00a9\u00aa\5\"\22\2\u00aa\u00ac\3\2\2\2"+
		"\u00ab\u00a6\3\2\2\2\u00ab\u00a7\3\2\2\2\u00ac#\3\2\2\2\u00ad\u00ae\7"+
		"\3\2\2\u00ae\u00af\5 \21\2\u00af\u00b0\7\r\2\2\u00b0%\3\2\2\2\u00b1\u00b2"+
		"\7\17\2\2\u00b2\u00b3\5\"\22\2\u00b3\u00b4\7\r\2\2\u00b4\'\3\2\2\2\20"+
		"-\64<CFNWq\u0083\u008e\u0090\u009b\u00a4\u00ab";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}