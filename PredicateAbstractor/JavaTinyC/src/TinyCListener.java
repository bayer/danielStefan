// Generated from TinyC.g4 by ANTLR 4.0
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

public interface TinyCListener extends ParseTreeListener {
	void enterInteger(TinyCParser.IntegerContext ctx);
	void exitInteger(TinyCParser.IntegerContext ctx);

	void enterTest(TinyCParser.TestContext ctx);
	void exitTest(TinyCParser.TestContext ctx);

	void enterSum(TinyCParser.SumContext ctx);
	void exitSum(TinyCParser.SumContext ctx);

	void enterExpr(TinyCParser.ExprContext ctx);
	void exitExpr(TinyCParser.ExprContext ctx);

	void enterListOfInputIds(TinyCParser.ListOfInputIdsContext ctx);
	void exitListOfInputIds(TinyCParser.ListOfInputIdsContext ctx);

	void enterAssertStatement(TinyCParser.AssertStatementContext ctx);
	void exitAssertStatement(TinyCParser.AssertStatementContext ctx);

	void enterAsgn(TinyCParser.AsgnContext ctx);
	void exitAsgn(TinyCParser.AsgnContext ctx);

	void enterInputId(TinyCParser.InputIdContext ctx);
	void exitInputId(TinyCParser.InputIdContext ctx);

	void enterIfStatement(TinyCParser.IfStatementContext ctx);
	void exitIfStatement(TinyCParser.IfStatementContext ctx);

	void enterId(TinyCParser.IdContext ctx);
	void exitId(TinyCParser.IdContext ctx);

	void enterStatement(TinyCParser.StatementContext ctx);
	void exitStatement(TinyCParser.StatementContext ctx);

	void enterInput(TinyCParser.InputContext ctx);
	void exitInput(TinyCParser.InputContext ctx);

	void enterProg(TinyCParser.ProgContext ctx);
	void exitProg(TinyCParser.ProgContext ctx);

	void enterTerm(TinyCParser.TermContext ctx);
	void exitTerm(TinyCParser.TermContext ctx);

	void enterWhileStatement(TinyCParser.WhileStatementContext ctx);
	void exitWhileStatement(TinyCParser.WhileStatementContext ctx);

	void enterListOfIds(TinyCParser.ListOfIdsContext ctx);
	void exitListOfIds(TinyCParser.ListOfIdsContext ctx);

	void enterProgramExpr(TinyCParser.ProgramExprContext ctx);
	void exitProgramExpr(TinyCParser.ProgramExprContext ctx);

	void enterOutput(TinyCParser.OutputContext ctx);
	void exitOutput(TinyCParser.OutputContext ctx);

	void enterNotTest(TinyCParser.NotTestContext ctx);
	void exitNotTest(TinyCParser.NotTestContext ctx);
}