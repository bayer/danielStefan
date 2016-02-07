import java.util.Deque;
import java.util.LinkedList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import com.pengyifan.commons.collections.tree.*;

import parser.LTLBaseListener;
import parser.LTLParser;
import DataStructures.NodeObject;
/***
 * builds negated tree !φ of LTL formula φ, with negations only directly in front of atomic propositions
 * @author bayer
 */
public class LTLTree extends LTLBaseListener {
	private class LTLTreeNode extends TreeNode {
		
	}
	public  LTLTreeNode root;
	private LTLTreeNode currentNode, node1, node2;
	private LinkedList<TreeNode> deque = new LinkedList<TreeNode>();
	private boolean negFlag = true;
	
	public LTLTree() {
		super();
		root = new LTLTreeNode();
		currentNode = root;
		deque.add(root);
	}

	@Override public void enterUnary(LTLParser.UnaryContext ctx) {
		String connective = new String();
		currentNode = (LTLTreeNode) deque.removeLast();
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
			node1 = new LTLTreeNode();
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
		currentNode = (LTLTreeNode) deque.removeLast();
		String nodeName = ctx.getChild(0).getChild(0).getText();
		if (ctx.getChild(0).getChildCount() > 1) {
			nodeName += 	ctx.getChild(0).getChild(1).getText() +
							ctx.getChild(0).getChild(2).getText();
		}
		NodeObject nodeObject = new NodeObject(nodeName, negFlag, ctx.getChild(0));
		
		if (negFlag) {
			nodeObject.negative = true;
		}
		currentNode.setObject(nodeObject);	

	}
	
	@Override public void enterBinaryOp(LTLParser.BinaryOpContext ctx) { 
		
		String connective;
		
		currentNode = (LTLTreeNode) deque.removeLast();
		
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

		node1 = new LTLTreeNode();
		node2 = new LTLTreeNode();
		currentNode.add(node1);
		currentNode.add(node2);
		deque.add(node2);		
		deque.add(node1);

	}
	
}
