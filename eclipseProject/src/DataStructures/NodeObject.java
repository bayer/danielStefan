package DataStructures;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import parser.LTLParser;

public class NodeObject {
	public enum NodeType {
		UNARY_OP, BINARY_OP, PROPOSITION
	}
	public String name;
	public NodeType nodeType;
	public Boolean negative;
	public AtomicProp content;
	
	public NodeObject(String name, NodeType nodeType) {
		super();
		this.name = name;
		this.nodeType = nodeType;
		this.negative = false;
	}

	public NodeObject(String name, NodeType nodeType, Boolean negative) {
		this(name,nodeType);
		this.negative = negative;
	}
	
	public NodeObject(String name, Boolean negative, ParseTree context) {
		this(name,NodeType.PROPOSITION);
		this.negative = negative;
		this.content = new AtomicProp(context);
				
	}
		@Override
	public String toString() {
		String neg = (this.negative)  ? "!" : "";
		return neg + name + " [" + nodeType + "]";
	}
	

}
