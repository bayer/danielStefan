import java.util.LinkedList;

import org.antlr.v4.runtime.tree.ParseTree;

import com.pengyifan.commons.collections.tree.TreeNode;

import parser.LKSBaseListener;
import parser.LKSParser;

public class LKSGenerator extends LKSBaseListener {
	public Integer size;
	private int rowCounter = 0;
	KripkeStructure lks;
	KripkeStructure.KSNode activeNode;
	private Boolean enteredPredicateFlag = false;
	
	public LKSGenerator() {
		super();
		lks = new KripkeStructure();
	}
	
	@Override public void enterVertex_amount(LKSParser.Vertex_amountContext ctx) { 
		size = Integer.valueOf(ctx.getText());
		for (int i=0; i<size; i++) {
			lks.addNewNode();
		}
		System.out.println("size of KS: " +lks.size());
	}
	
	@Override public void enterAdjacencyRow(LKSParser.AdjacencyRowContext ctx) { 
		int i = 0;
		
		if (ctx.getChildCount() > lks.size() || rowCounter >= lks.size()) {
			System.err.println("Error in LKS file, second section: too many nodes");
			return;
		}
		
		for (ParseTree l: ctx.children) {

			if (l.getText().equals("1")){
				lks.get(rowCounter).addChild(lks.get(i));
//				System.out.println(rowCounter + " to " + i);
			}
			i++;
		}
		rowCounter++;
	}
	
	
	@Override public void enterPredicateAssignmentRow(LKSParser.PredicateAssignmentRowContext ctx) { 
		Integer nodeNumber = 0;
//		System.out.println("children: " +ctx.getChildCount() + "; 1: "+ctx.getChild(0) + ", 2:"+ctx.getChild(1) + ", 3:"+ctx.getChild(2));
		
		nodeNumber = Integer.parseInt( ctx.getChild(0).getText());
		if (nodeNumber >= lks.size()) {
			System.err.println("Error in LKS file, third section: node " + nodeNumber + " doesn't exist!");
			return;
		}
		activeNode = lks.get(nodeNumber);
	}
	
	@Override public void enterPredicate(LKSParser.PredicateContext ctx) { 
		if (!enteredPredicateFlag) {
			activeNode.addPredicate(ctx.getText());
		}
		enteredPredicateFlag = true;
	}
	@Override public void exitPredicate(LKSParser.PredicateContext ctx) { 
		enteredPredicateFlag = false;
	}

	@Override public void enterVertexMapping(LKSParser.VertexMappingContext ctx) { 
		Integer fromNode, toNode;
		
		fromNode = Integer.parseInt(ctx.getChild(0).getText());
		toNode = Integer.parseInt(ctx.getChild(2).getText());
		
		if ( (fromNode >= lks.size()) || (toNode >= lks.size()) ) {
			System.err.println("Error in LKS file, fourth section: " + fromNode +" -> " + toNode + " exceeds amount of nodes in LKS, which is: " + lks.size());
			return;
		}
		if (lks.get(fromNode).getOriginalNode() != null) {
			System.err.println("Error in LKS file, fourth section: '" + fromNode +" ->' already declared! ");
			return;
		}
		lks.get(fromNode).addOriginalNode(toNode);
	}




}
