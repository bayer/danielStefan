import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;

import com.pengyifan.commons.collections.tree.TreeNode;

public class TableauGenerator {
	HashSet<TableauNode> nodesSet = new HashSet<TableauNode>();
	TableauNode root;
	private SecureRandom random = new SecureRandom();
	private String nextChar=(Character.valueOf((char)("a".charAt(0) - 1))).toString();
	
	public TableauGenerator(TreeNode rootNode) {

		HashSet<String> incoming = new HashSet<String>();
		HashSet<TreeNode> formula = new HashSet<TreeNode>();
				
		incoming.add("init");
		formula.add(rootNode);
		nodesSet = new HashSet<TableauNode>();
		
//		root = new TableauNode(new BigInteger(130, random).toString(32), incoming, formula, new HashSet<TreeNode>(), new HashSet<TreeNode>());
		
		root = new TableauNode(getNextName(), incoming, formula, new HashSet<TreeNode>(), new HashSet<TreeNode>(), new HashSet<Character>());
		expand(root);
	}
	
	private String getNextName() {
		nextChar = (Character.valueOf((char)(nextChar.charAt(0) + 1))).toString();
		return nextChar + nextChar + nextChar;
	}
	public void expand(TableauNode node) {
		System.out.println(node.name + " " + node.incoming + " " + node.new1 + " " + node.old + " " + node.next);
		TreeNode formula_η;
		if (node.new1.isEmpty()) {
			if (nodesSet!=null && nodesSet.contains(node)) { //old and next fields are the same
				for (TableauNode tn : nodesSet) {
			        if (tn.equals(node)) 
			        	tn.incoming.addAll(node.incoming);
			      } 
				//nodesSet.get(nodesSet.lastIndexOf(node)).incoming.addAll(node.incoming);
				//TODO: durch das addall könnten duplicate entries im incoming feld sein
				return;
			} else {
				HashSet<String> nodeNames = new HashSet<String>();
				nodeNames.add(node.name);
				//name, incoming, new, old, next
				nodesSet.add(node);
				expand(new TableauNode(getNextName(), nodeNames, new HashSet<TreeNode>(node.next), new HashSet<TreeNode>(), new HashSet<TreeNode>(), new HashSet<Character>()));
			}
		} else { //Z11
			formula_η = node.new1.iterator().next();
			node.new1.remove(formula_η);
			
			NodeObject formerlNode = (NodeObject)formula_η.getObject();
			
			switch (formerlNode.nodeType) {
			case PROPOSITION:
				if (node.old.contains(formula_η)) {
					for (TreeNode tn : node.old) {
						if (tn.equals(formula_η)) 
							if (((NodeObject)tn.getObject()).negative == formerlNode.negative)
								return; /* current node contains a contradiction */
					}
				}
				if (formerlNode.name.equals("F")) { /* current node contains a contradiction */
					return;
				} else {
					node.old.add(formula_η); 
					if (((NodeObject)formula_η.getObject()).nodeType == NodeObject.NodeType.PROPOSITION) {
						if (!((NodeObject)formula_η.getObject()).negative)
							node.label.add(((NodeObject)formula_η.getObject()).name.charAt(0));
					}
					expand(node);
				}
				break;
			case BINARY_OP:
				if (formerlNode.name.equals("&")) { /* no split of node */
					node.new1.add(formula_η.getChild(0));
					node.new1.add(formula_η.getChild(1));
					node.old.add(formula_η);
					//name, incoming, new, old next
					expand(node);
				} else { /* Until, Release, or "|" -> split node */

					TableauNode node2 = new TableauNode(getNextName(), 
												new HashSet<String>(node.incoming), new HashSet<TreeNode>(node.new1), new HashSet<TreeNode>(node.old), new HashSet<TreeNode>(node.next), new HashSet<Character>(node.label));
					
					if (formerlNode.name=="R") {
						node2.new1.add(formula_η.getChild(0));
					}
					node2.new1.add(formula_η.getChild(1));
					node2.old.add(formula_η);

					if (formerlNode.name=="R") {
						node.new1.add(formula_η.getChild(1));
					} else { /* Until or "|" */
						node.new1.add(formula_η.getChild(0));
					}
					node.old.add(formula_η);
					if (!formerlNode.name.equals("|"))
						node.next.add(formula_η);
					expand(node);
					expand(node2);
				}
				break;
			case UNARY_OP:
				System.err.println("Error in TableauGenerator.java: unimplemented unary_op '"+formerlNode.name+"' occured!");
				break;
			default:
				System.err.println("Error in TableauGenerator.java: default case reached!");
			}
		}
	}

	@Override
	public String toString() {
		String retVal = new String("Tableau\n");
		for (TableauNode tn : nodesSet) {
			retVal += "[name="+tn.name+", incoming="+tn.incoming+", old="+tn.old+", next="+tn.next+", label=" + tn.label +"]\n";
		}
		return retVal;
	}
	
	
}
