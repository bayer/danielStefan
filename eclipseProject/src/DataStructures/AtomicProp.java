package DataStructures;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

public class AtomicProp {
	public enum Operator {
		LT_OP, GT_OP, EQ_OP;
		
		@Override
		public String toString() {
			switch(this) {
			case LT_OP: return "<";
			case GT_OP: return ">";
			case EQ_OP: return "=";
			default: throw new IllegalArgumentException();
			}
		}
		
		public Operator negate() {
			switch(this) {
			case LT_OP: return GT_OP;
			case GT_OP: return LT_OP;
			case EQ_OP: return EQ_OP;
			default: throw new IllegalArgumentException();
			}
		}
	}
	
	public Operator operator;
	public String firstArg, secondArg;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstArg == null) ? 0 : firstArg.hashCode());
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + ((secondArg == null) ? 0 : secondArg.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AtomicProp other = (AtomicProp) obj;
		if ((firstArg == null && other.firstArg != null) || (secondArg == null && other.secondArg != null)) {
				return false;
		} else if (!((firstArg.equals(other.firstArg) && secondArg.equals(other.secondArg) && operator == other.operator) || (firstArg.equals(other.secondArg) && secondArg.equals(other.firstArg) && operator == other.operator.negate()) ))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (operator == null)
			return firstArg;
		return firstArg + operator +secondArg;
	}

	public AtomicProp() {
	}
	
	public AtomicProp(ParseTree context) {
		if (context.getChildCount() > 1) { //then it has to be 3 
			firstArg = context.getChild(0).getText();
			secondArg = context.getChild(2).getText();
			switch (context.getChild(1).getText()) {
				case "<": 
					operator = AtomicProp.Operator.LT_OP;
					break;
				case ">":
					operator = AtomicProp.Operator.GT_OP;
					break;
				case "==":
					operator = AtomicProp.Operator.EQ_OP;
					break;
				default:
					System.err.println("NodeObject.java: Unknown operator in propositional formula: '" + operator + "'");
			}
		} else { //
			firstArg = context.getChild(0).getText();
		}
		
	}
}
