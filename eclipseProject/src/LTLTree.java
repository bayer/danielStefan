import java.util.Deque;
import java.util.LinkedList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import com.pengyifan.commons.collections.tree.*;

import parser.LTLBaseListener;
import parser.LTLParser;
/***
 * builds negated tree !φ of LTL formula φ, with negations only directly in front of atomic propositions
 * @author bayer
 */
public class LTLTree extends LTLBaseListener {
	
	public  TreeNode root;
	private TreeNode currentNode, node1, node2;
	private LinkedList<TreeNode> deque = new LinkedList<TreeNode>();
	private boolean negFlag = true;
	
	public LTLTree() {
		super();
		root = new TreeNode();
		currentNode = root;
		deque.add(root);
	}

	@Override public void enterUnary(LTLParser.UnaryContext ctx) {
		String connective = new String();
		currentNode = (TreeNode) deque.removeLast();
		if (ctx.getChild(0).getText().equals("!")) {
			negFlag = !negFlag;
			deque.add(currentNode);
		} else {
			connective = ctx.getChild(0).getText();
			if (negFlag) {
				switch(ctx.getChild(0).getText()) {
				case "F":
					connective = "G";
					break;
				case "G":
					connective = "F";
					break;
				default:
					connective = ctx.getChild(0).getText();
				}
			}
			currentNode.setObject(new NodeObject(connective,NodeObject.NodeType.UNARY_OP));
			node1 = new TreeNode();
			currentNode.add(node1);
			deque.add(node1);
		}

		// System.out.println(TreeNode.PRETTY_PRINT.apply(root));
	}
	
	@Override public void exitUnary(LTLParser.UnaryContext ctx) {
		if (ctx.getChild(0).getText().equals("!")) {
			negFlag = !negFlag;
		}
	}
	
	@Override public void enterProposition(LTLParser.PropositionContext ctx) { 
		currentNode = (TreeNode) deque.removeLast();
		
		String nodeName = ctx.getChild(0).getChild(0).getText();
		NodeObject nodeObject = new NodeObject(nodeName,NodeObject.NodeType.PROPOSITION);
		
		if (negFlag) {
			nodeObject.negative = true;
		}
		currentNode.setObject(nodeObject);	

	}
	
	@Override public void enterBinaryOp(LTLParser.BinaryOpContext ctx) { 
		
		String connective;
		
		currentNode = (TreeNode) deque.removeLast();
		
		connective = ctx.getChild(1).getText();
		if (negFlag) {
			switch(ctx.getChild(1).getText()) {
			case "U":
				connective = "R";
				break;
			case "R":
				connective = "U";
				break;
			case "|":
				connective = "&";
				break;
			case "&":
				connective = "|";
				break;
			default:
				connective = ctx.getChild(1).getText();
			}
		}
		
		currentNode.setObject(new NodeObject(connective,NodeObject.NodeType.BINARY_OP));

		node1 = new TreeNode();
		node2 = new TreeNode();
		currentNode.add(node1);
		currentNode.add(node2);
		deque.add(node2);		
		deque.add(node1);

	}
	
}
