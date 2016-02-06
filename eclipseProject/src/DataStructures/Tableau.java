package DataStructures;

import java.util.HashSet;

import com.pengyifan.commons.collections.tree.TreeNode;

import DataStructures.TableauNode;
import DataStructures.NodeObject;


public class Tableau {
	
	private HashSet<TableauNode> nodesSet = new HashSet<TableauNode>();
	private String nextChar=(Character.valueOf((char)("1".charAt(0) - 1))).toString();
	private TableauNode init = new TableauNode("init", null, null, null, null, null);
	
	public Tableau(TreeNode formulaRoot) {
		
		HashSet<TableauNode> incoming = new HashSet<TableauNode>();
		HashSet<TreeNode> formula = new HashSet<TreeNode>();
		TableauNode root;
		
		incoming.add(init);
		formula.add(formulaRoot);
		nodesSet = new HashSet<TableauNode>();
		
//		root = new TableauNode(new BigInteger(130, random).toString(32), incoming, formula, new HashSet<TreeNode>(), new HashSet<TreeNode>());
		
		root = new TableauNode(getNewName(), incoming, formula, new HashSet<TreeNode>(), new HashSet<TreeNode>(), new HashSet<Character>());
		expand(root);
	}
	
	private String getNewName() {
		nextChar = (Character.valueOf((char)(nextChar.charAt(0) + 1))).toString().toUpperCase();
		return nextChar;
	}
	
	public void expand(TableauNode node) {
		TreeNode formula_η;
		//System.out.println(node.getName() + " " + node.getIncoming() + " " + node.getNew() + " " + node.getOld() + " " + node.getNext());
		if (node.getNew().isEmpty()) {
			if (nodesSet!=null && nodesSet.contains(node)) { //old and next fields are the same
				for (TableauNode tn : nodesSet) {
			        if (tn.equals(node)) 
			        	tn.getIncoming().addAll(node.getIncoming());
			      } 
				//nodesSet.get(nodesSet.lastIndexOf(node)).incoming.addAll(node.incoming);
				//TODO: durch das addall könnten duplicate entries im incoming feld sein
				return;
			} else {
				HashSet<TableauNode> incomingNodes = new HashSet<TableauNode>();
				incomingNodes.add(node);
				nodesSet.add(node);
				expand(new TableauNode(getNewName(), incomingNodes, new HashSet<TreeNode>(node.getNext()), new HashSet<TreeNode>(), new HashSet<TreeNode>(), new HashSet<Character>()));
			}
		} else { //Z11
			formula_η = node.getNew().iterator().next();
			node.getNew().remove(formula_η);
			
			NodeObject formerlNode = (NodeObject)formula_η.getObject();
			
			switch (formerlNode.nodeType) {
			case PROPOSITION:
				if (node.getOld().contains(formula_η)) {
					for (TreeNode tn : node.getOld()) {
						if (tn.equals(formula_η)) 
							if (((NodeObject)tn.getObject()).negative == formerlNode.negative)
								return; /* current node contains a contradiction */
					}
				}
				if (formerlNode.name.equals("F")) { /* current node contains a contradiction */
					return;
				} else {
					node.getOld().add(formula_η); 
					if (!formerlNode.negative)
						node.getLabel().add(formerlNode.name.charAt(0));
					expand(node);
				}
				break;
			case BINARY_OP:
				if (formerlNode.name.equals("&")) { /* no split of node */
					node.getNew().add(formula_η.getChild(0));
					node.getNew().add(formula_η.getChild(1));
					node.getOld().add(formula_η);
					
					//name, incoming, new, old next
					expand(node);
				} else { /* Until, Release, or "|" -> split node */

					TableauNode node2 = new TableauNode(getNewName(), 
												new HashSet<TableauNode>(node.getIncoming()), new HashSet<TreeNode>(node.getNew()), new HashSet<TreeNode>(node.getOld()), new HashSet<TreeNode>(node.getNext()), new HashSet<Character>(node.getLabel()));
					
					if (formerlNode.name=="R") {
						node2.getNew().add(formula_η.getChild(0));
					}
					node2.getNew().add(formula_η.getChild(1));
					node2.getOld().add(formula_η);

					if (formerlNode.name=="R") {
						node.getNew().add(formula_η.getChild(1));
					} else { /* Until or "|" */
						node.getNew().add(formula_η.getChild(0));
					}
					node.getOld().add(formula_η);
					if (!formerlNode.name.equals("|"))
						node.getNext().add(formula_η);
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

	public HashSet<TableauNode> getNodesSet() {
		return nodesSet;
	}

	public void setNodesSet(HashSet<TableauNode> nodesSet) {
		this.nodesSet = nodesSet;
	}

	public TableauNode getInit() {
		return init;
	}

	public void setInit(TableauNode init) {
		this.init = init;
	}
	
	@Override
	public String toString() {
		String retVal = new String("Tableau\n");
		retVal += "[Name="+init.getName() + " "+init+"]\n";
		for (TableauNode tn : nodesSet) {
			retVal += "[name="+tn.getName()+" "+tn+", incoming=";
				for (TableauNode tt : tn.getIncoming())
					retVal += tt.getName() + " " +",";
			retVal +=", old="+tn.getOld()+", next="+tn.getNext()+", label=" + tn.getLabel() +"]\n";
		}
		return retVal;
	}
	
	
}
