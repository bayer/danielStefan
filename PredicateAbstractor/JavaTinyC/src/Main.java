import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {

	public static void main(String[] args) throws IOException {
		File f = new File(args[0]);
		if (!f.exists()) {
			System.err.println("Can't find file " + f.getAbsolutePath());
			return;
		}

		CharStream stream = new ANTLRFileStream(args[0]);
		TinyCLexer lex = new TinyCLexer(stream);
		CommonTokenStream tokens = new CommonTokenStream(lex);

		TinyCParser parser = new TinyCParser(tokens);
		ParserRuleContext tree = parser.prog();
		ParseTreeWalker walker = new ParseTreeWalker();
		final File trFile = new File(args[0] + ".tr");
		final File abstractionFile = new File(args[0] + ".abstr");
		final File asrtsFile = new File(args[0] + ".asrts");
		final File initFile = new File(args[0] + ".init");
		if (trFile.exists()) {
			trFile.delete();
			trFile.createNewFile();
		}
		if (abstractionFile.exists()) {
			abstractionFile.delete();
			abstractionFile.createNewFile();
		}
		if (asrtsFile.exists()) {
			asrtsFile.delete();
			asrtsFile.createNewFile();
		}
		if (initFile.exists()) {
			initFile.delete();
			initFile.createNewFile();
		}

		final PrintWriter trWriter = new PrintWriter(trFile);
		final PrintWriter abstractionWriter = new PrintWriter(abstractionFile);
		final PrintWriter asrtsWriter = new PrintWriter(asrtsFile);
		final PrintWriter initWriter = new PrintWriter(initFile);

		TinyCListener extractor = new TinyCBaseListener() {

			List<String> vars = new ArrayList<String>();
			LinkedList<Integer> stateStack = new LinkedList<Integer>();
			Set<String> inputIds = new HashSet<String>();
			Set<String> abstrs = new HashSet<String>();
			Set<String> usedVars = new HashSet<String>();

			int curState = 0;
			int nextState = 0;
			int stateCounter = 0;

			String leaveVars(int j, List<String> except) {
				if (j == vars.size()) {
					return "true";
				}
				String var = vars.get(j);
				if (except.contains(var)) {
					return leaveVars(j + 1, except);
				}
				return "(and (= " + var + " " + var + "_p) "
						+ leaveVars(j + 1, except) + ")";
			}

			String nextState() {
				return "(= .s_p " + nextState + ")";
			}

			String evaluateTerm(TinyCParser.TermContext term,
					StringBuffer formula, List<String> setVars) {
				if (term.getChild(0) instanceof TinyCParser.IdContext) {
					TinyCParser.IdContext id = (TinyCParser.IdContext) term
							.getChild(0);
					formula.append("true");
					return id.getText();
				} else if (term.getChild(0) instanceof TinyCParser.IntegerContext) {
					TinyCParser.IntegerContext intg = (TinyCParser.IntegerContext) term
							.getChild(0);
					formula.append("true");
					return intg.getText();
				} else if (term.getChild(1) instanceof TinyCParser.ExprContext) {
					return evaluateExpr(
							(TinyCParser.ExprContext) term.getChild(1),
							formula, setVars);
				}
				System.err.println("ASSERTION FAILURE!!!");
				assert (false);
				return null;
			}

			// returns an integer value
			String evaluateSum(TinyCParser.SumContext sum,
					StringBuffer sideEffect, List<String> setVars) {
				if (sum.getChildCount() == 1) {
					TinyCParser.TermContext term = (TinyCParser.TermContext) sum
							.getChild(0);
					return evaluateTerm(term, sideEffect, setVars);
				} else {
					TinyCParser.SumContext lSum = (TinyCParser.SumContext) sum
							.getChild(0);
					StringBuffer sumSide = new StringBuffer();
					String sumRv = evaluateSum(lSum, sumSide, setVars);

					TinyCParser.TermContext term = (TinyCParser.TermContext) sum
							.getChild(2);
					StringBuffer termSide = new StringBuffer();
					String termRv = evaluateTerm(term, termSide, setVars);

					sideEffect.append("(and " + sumSide + " " + termSide + ")");
					if (sum.getChild(1).getText().equals("+")) {
						return "(+ " + sumRv + " " + termRv + ")";
					} else {
						return "(+ " + sumRv + " (- " + termRv + "))";
					}
				}
			}

			// returns an integer value
			String evaluateAsgn(TinyCParser.AsgnContext asgn,
					StringBuffer sideEffect, List<String> setVars) {
				StringBuffer exprSide = new StringBuffer();
				String rv = evaluateExpr(
						(TinyCParser.ExprContext) asgn.getChild(2), exprSide,
						setVars);
				String var = asgn.getChild(0).getText();
				setVars.add(var);
				usedVars.add(var);
				sideEffect.append("(and (= " + var + "_p " + rv + ") "
						+ exprSide + ")");
				return var + "_p";
			}

			// returns a boolean value
			String evaluateTest(TinyCParser.TestContext test,
					StringBuffer sideEffect, List<String> setVars) {
				if (test.getChildCount() == 1) {
					TinyCParser.SumContext sum = (TinyCParser.SumContext) test
							.getChild(0);
					return "(not (= 0 " + evaluateSum(sum, sideEffect, setVars)
							+ "))";
				} else {
					String op = test.getChild(1).getText();
					if (op.equals("=="))
						op = "=";

					TinyCParser.SumContext lSum = (TinyCParser.SumContext) test
							.getChild(0);
					StringBuffer lSide = new StringBuffer();
					String lrv = evaluateSum(lSum, lSide, setVars);

					TinyCParser.SumContext rSum = (TinyCParser.SumContext) test
							.getChild(2);
					StringBuffer rSide = new StringBuffer();
					String rrv = evaluateSum(rSum, rSide, setVars);
					sideEffect.append("(and " + lSide + " " + rSide + ")");
					return "(" + op + " " + lrv + " " + rrv + ")";
				}
			}

			// returns an integer value
			String evaluateExpr(TinyCParser.ExprContext expr,
					StringBuffer sideEffect, List<String> setVars) {
				if (expr.getChild(0) instanceof TinyCParser.TestContext) {
					TinyCParser.TestContext test = (TinyCParser.TestContext) expr
							.getChild(0);
					return "(ite " + evaluateTest(test, sideEffect, setVars)
							+ " 1 0)";
				} else if (expr.getChild(0) instanceof TinyCParser.AsgnContext) {
					TinyCParser.AsgnContext asgn = (TinyCParser.AsgnContext) expr
							.getChild(0);
					return evaluateAsgn(asgn, sideEffect, setVars);
				} else if (expr.getChild(0) instanceof TinyCParser.NotTestContext) {
					TinyCParser.NotTestContext notTest = (TinyCParser.NotTestContext) expr
							.getChild(0);
					TinyCParser.TestContext test = (TinyCParser.TestContext) notTest
							.getChild(1);
					return "(ite " + evaluateTest(test, sideEffect, setVars)
							+ " 0 1)";
				} else if (expr.getChild(0) instanceof TinyCParser.SumContext) {
					TinyCParser.SumContext sum = (TinyCParser.SumContext) expr
							.getChild(0);
					return evaluateSum(sum, sideEffect, setVars);
				}
				System.err.println("ASSERTION FAILURE!!!!");
				assert (false);
				return null;
			}

			@Override
			public void enterProgramExpr(
					@NotNull TinyCParser.ProgramExprContext ctx) {
				curState = nextState;
				stateCounter++;
				nextState = stateCounter;
				StringBuffer sideEffect = new StringBuffer();
				List<String> setVars = new ArrayList<String>();
				evaluateExpr((TinyCParser.ExprContext) ctx.getChild(0),
						sideEffect, setVars);
				String str = "(=> (= .s " + curState + ") " + "(and "
						+ nextState() + " (and " + sideEffect
						+ leaveVars(0, setVars) + ")))";
				trWriter.println(str);
			}

			@Override
			public void enterWhileStatement(
					@NotNull TinyCParser.WhileStatementContext ctx) {
				stateStack.push(stateCounter + 2);
				curState = nextState;
				stateStack.push(curState);
				stateCounter++;
				nextState = stateCounter;
				StringBuffer sideEffects = new StringBuffer();
				List<String> setVars = new ArrayList<String>();
				String ret = evaluateExpr(
						(TinyCParser.ExprContext) ctx.getChild(2), sideEffects,
						setVars);
				abstrs.add("(not (= 0 " + ret + "))");
				String sideStr = "(=> (= .s " + curState + ") " + sideEffects
						+ ")";
				trWriter.println(sideStr);
				String antecedentTrue = "(and (= .s " + curState + ") "
						+ "(not (= 0 " + ret + ")))";
				String consequentTrue = "(and " + nextState() + " (and "
						+ leaveVars(0, setVars) + " " + sideEffects + "))";
				String str = "(=> " + antecedentTrue + " " + consequentTrue
						+ ")";
				trWriter.println(str);
				stateCounter++;
				nextState = stateCounter;
				String antecedentFalse = "(and (= .s " + curState + ") "
						+ "(= 0 " + ret + "))";
				String consequentFalse = "(and " + nextState() + " (and "
						+ leaveVars(0, setVars) + " " + sideEffects + "))";
				str = "(=> " + antecedentFalse + " " + consequentFalse + ")";
				trWriter.println(str);
				nextState--;
			}

			@Override
			public void exitWhileStatement(
					@NotNull TinyCParser.WhileStatementContext ctx) {
				curState = nextState;
				nextState = stateStack.pop();
				String str = "(=> (= .s " + curState + ") " + "(and "
						+ nextState() + " "
						+ leaveVars(0, new ArrayList<String>()) + "))";
				trWriter.println(str);
				nextState = stateStack.pop();
			}

			@Override
			public void enterAssertStatement(
					@NotNull TinyCParser.AssertStatementContext ctx) {
				curState = nextState;
				stateCounter++;
				nextState = stateCounter;
				StringBuffer sideEffects = new StringBuffer();
				List<String> setVars = new ArrayList<String>();
				String ret = evaluateExpr(
						(TinyCParser.ExprContext) ctx.getChild(2), sideEffects,
						setVars);
				abstrs.add("(not (= 0 " + ret + "))");
				String sideStr = "(=> (= .s " + curState + ") " + sideEffects
						+ ")";
				trWriter.println(sideStr);
				String antecedentTrue = "(and (= .s " + curState + ") "
						+ "(not (= 0 " + ret + ")))";
				String consequentTrue = "(and " + nextState() + " (and "
						+ leaveVars(0, setVars) + " " + sideEffects + "))";
				String str = "(=> " + antecedentTrue + " " + consequentTrue
						+ ")";
				trWriter.println(str);
				stateCounter++;
				nextState = stateCounter;
				String antecedentFalse = "(and (= .s " + curState + ") "
						+ "(= 0 " + ret + "))";
				String consequentFalse = "(and " + nextState() + " (and "
						+ leaveVars(0, setVars) + " " + sideEffects + "))";
				str = "(=> " + antecedentFalse + " " + consequentFalse + ")";
				trWriter.println(str);
				curState = nextState;
				str = "(=> (= .s " + curState + ") " + "(and " + nextState()
						+ " (and " + leaveVars(0, new ArrayList<String>())
						+ ")))";
				trWriter.println(str);
				asrtsWriter.println("(not (= .s " + curState + "))");
				curState--;
				stateCounter++;
				nextState = stateCounter;
				str = "(=> (= .s " + curState + ") " + "(and " + nextState()
						+ " (and " + leaveVars(0, new ArrayList<String>())
						+ ")))";
				trWriter.println(str);
			}

			@Override
			public void enterIfStatement(
					@NotNull TinyCParser.IfStatementContext ctx) {
				curState = nextState;
				stateCounter++;
				nextState = stateCounter;
				StringBuffer sideEffects = new StringBuffer();
				List<String> setVars = new ArrayList<String>();
				String ret = evaluateExpr(
						(TinyCParser.ExprContext) ctx.getChild(2), sideEffects,
						setVars);
				abstrs.add("(not (= 0 " + ret + "))");
				String sideStr = "(=> (= .s " + curState + ") " + sideEffects
						+ ")";
				trWriter.println(sideStr);
				String antecedentTrue = "(and (= .s " + curState + ") "
						+ "(not (= 0 " + ret + ")))";
				String consequentTrue = "(and " + nextState() + " (and "
						+ leaveVars(0, setVars) + " " + sideEffects + "))";
				String str = "(=> " + antecedentTrue + " " + consequentTrue
						+ ")";
				trWriter.println(str);
				stateCounter++;
				nextState = stateCounter;
				stateStack.push(nextState);
				String antecedentFalse = "(and (= .s " + curState + ") "
						+ "(= 0 " + ret + "))";
				String consequentFalse = "(and " + nextState() + " (and "
						+ leaveVars(0, setVars) + " " + sideEffects + "))";
				str = "(=> " + antecedentFalse + " " + consequentFalse + ")";
				trWriter.println(str);
				nextState--;
			}

			@Override
			public void exitIfStatement(
					@NotNull TinyCParser.IfStatementContext ctx) {
				curState = nextState;
				nextState = stateStack.pop();
				String str = "(=> (= .s " + curState + ") " + "(and "
						+ nextState() + " "
						+ leaveVars(0, new ArrayList<String>()) + "))";
				trWriter.println(str);
			}

			@Override
			public void enterProg(@NotNull TinyCParser.ProgContext ctx) {
				for (char x = 'a'; x <= 'z'; x++) {
					vars.add(new Character(x).toString());
				}
			}

			@Override
			public void exitProg(@NotNull TinyCParser.ProgContext ctx) {
				curState = nextState;
				String str = "(=> (= .s " + curState + ") " + "(and "
						+ nextState() + " (and "
						+ leaveVars(0, new ArrayList<String>()) + ")))";
				trWriter.println(str);
				str = "(< .s " + (stateCounter + 1) + ")";
				trWriter.println(str);
				str = "(>= .s 0)";
				trWriter.println(str);

				int num = 0;
				for (String s : abstrs) {
					abstractionWriter.println(s);
					++num;
					if (num >= 5)
						break;
				}

				List<String> uv = new ArrayList<String>(usedVars);
				outer: for (int i = 0; i < uv.size(); ++i) {
					for (int j = i + 1; j < uv.size(); ++j) {
						abstractionWriter.println("(< " + uv.get(i) + " "
								+ uv.get(j) + ")");
						++num;
						if (num >= 5)
							break outer;
					}
				}

				if (num <= 1) {
					abstractionWriter.println("(< .s 1)");
					abstractionWriter.println("(> .s 1)");
				}
			}

			@Override
			public void enterInputId(@NotNull TinyCParser.InputIdContext ctx) {
				inputIds.add(ctx.getText());
			}

			@Override
			public void exitInput(@NotNull TinyCParser.InputContext ctx) {
				for (char x = 'a'; x <= 'z'; ++x) {
					String var = new Character(x).toString();
					if (!inputIds.contains(var))
						initWriter.println("(= " + var + " 0)");
				}
				initWriter.println("(= .s 0)");
			}

			@Override
			public void visitErrorNode(@NotNull ErrorNode node) {
				System.err.println(node.getText());
				System.exit(1);
			}

		};

		walker.walk(extractor, tree);

		initWriter.close();
		asrtsWriter.close();
		trWriter.close();
		abstractionWriter.close();
	}
}
