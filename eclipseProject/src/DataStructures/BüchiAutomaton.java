package DataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.pengyifan.commons.collections.tree.TreeNode;

import DataStructures.KripkeStructure.LKSNode;
import DataStructures.Tableau;

public class BüchiAutomaton {
	private HashSet<BüchiNode> automataNodes = new HashSet<BüchiNode>();
	private BüchiNode initialNode;
	
	private class BüchiNode {
		HashMap<BüchiNode, HashSet<AtomicProp>> children;
		
		public BüchiNode() {
			children = new HashMap<BüchiNode,HashSet<AtomicProp>>();
		}
		public void addChild(BüchiNode büchiNode, HashSet<AtomicProp> ap) {
			children.put(büchiNode, ap);
		}

		@Override
		public String toString() {
			String retVal= " [children=";
			for (BüchiNode c : children.keySet()) {
				retVal+=children.get(c) + ((Integer)c.hashCode()).toString().substring(((Integer)c.hashCode()).toString().length()-3, ((Integer)c.hashCode()).toString().length())+", ";
			}
			retVal+= "]";
			return retVal;
		}
		
	}
	
	public BüchiAutomaton() {
		
	}
	
	public BüchiAutomaton(KripkeStructure kripkeStructure) {
		HashMap<LKSNode, BüchiNode> lkNode2BüchiNodeMapping = new HashMap<LKSNode, BüchiNode>();
		BüchiNode newNode;
		
		initialNode = addNewNode();
		lkNode2BüchiNodeMapping.put(null, initialNode);
		
		//create büchi node for each kripke node
		for (LKSNode kNode : kripkeStructure.getNodes()) {
			newNode = addNewNode();
			lkNode2BüchiNodeMapping.put(kNode, newNode);
		}
		
		//add children to each node, and add atomic propositions to each edge
		for (LKSNode kNode : kripkeStructure.getNodes()) {
			for (LKSNode child : kNode.getChildren()) {
				lkNode2BüchiNodeMapping.get(kNode).addChild(lkNode2BüchiNodeMapping.get(child), child.getPredicates());
			}
		}
	}
	
	public BüchiAutomaton(Tableau tableau) throws Exception {
		HashMap<TableauNode, BüchiNode> tableauNode2BüchiNodeMapping = new HashMap<TableauNode, BüchiNode>();
		HashSet<AtomicProp> propertyList = new HashSet<AtomicProp>(); //list of non-negated atomic properties appearing in the formula
		BüchiNode newNode;
		
		//create init node
		initialNode = addNewNode();
		tableauNode2BüchiNodeMapping.put(tableau.getInit(), initialNode);
		
		for (TableauNode tNode : tableau.getNodesSet()) {
			newNode = addNewNode();
			
			//map name of tableau node to Büchi node
			tableauNode2BüchiNodeMapping.put(tNode, newNode);
			
			//collect all atomic properties that occur into a list P=propertyList
			for (TreeNode oldProps : tNode.getOld()) {
				if (((NodeObject)oldProps.getObject()).nodeType == NodeObject.NodeType.PROPOSITION) {
					if (((NodeObject)oldProps.getObject()).content != null)
						propertyList.add(((NodeObject)oldProps.getObject()).content);
				}
			}
			
		}
		
		//calculate D=2^P
		HashSet<HashSet<AtomicProp>> powerSet = powerSet(propertyList);
			
		//calculate Pos(node), Neg(node) and L(node)
		for (TableauNode tNode : tableau.getNodesSet()) {
			HashSet<AtomicProp> positivePropositions = new HashSet<AtomicProp>(),
							    negativePropositions = new HashSet<AtomicProp>();
			HashSet<HashSet<AtomicProp>> labelPropositionSets = new HashSet<HashSet<AtomicProp>>();
			
			//Pos(node)
			for (TreeNode node : tNode.getOld()) {
				if (((NodeObject)node.getObject()).nodeType == NodeObject.NodeType.PROPOSITION) {
					if (((NodeObject)node.getObject()).content != null && !((NodeObject)node.getObject()).negative)
						positivePropositions.add(((NodeObject)node.getObject()).content);
				}
			}

			//Neg(node)
			for (TreeNode node : tNode.getOld()) {
				if (((NodeObject)node.getObject()).nodeType == NodeObject.NodeType.PROPOSITION) {
					if (((NodeObject)node.getObject()).content != null && ((NodeObject)node.getObject()).negative)
						negativePropositions.add(((NodeObject)node.getObject()).content);
				}
			}
			
			//L(node)
			Boolean failed = true;
			for (HashSet<AtomicProp> propSet : powerSet) {
				outer: for (AtomicProp ap1 : propSet) {
					failed = true;
					for (AtomicProp ap2 : negativePropositions) {
						if (ap1.equals(ap2)) {
							break outer;
						}
					}
					failed = false;
				}
				if (!failed) {
					labelPropositionSets.add(propSet);
				}
			}
			//TODO: die sets scheinen korrekt hinzugefügt zu werden, aber was ist mit der leeren menge? und ist sie notwendig?
			
			//find smallest set that is compatible with old(node) out of the labelPropositionSets
			HashSet<AtomicProp> label = null;
			for (HashSet<AtomicProp> candidateLabel :labelPropositionSets) {
				if (label == null) {
					label = candidateLabel;
				} else {
					if (label.size() > candidateLabel.size()) {
						label = candidateLabel;
					}
				}
			}
			
			//connect nodes with children and set the label of the edge
			if (label!=null)
				for (TableauNode incomingNode : tNode.getIncoming()) {
					tableauNode2BüchiNodeMapping.get(incomingNode).addChild(tableauNode2BüchiNodeMapping.get(tNode),label);
				}
			else 
				throw new Exception("label for büchi edge must not be null!");
			
			//TODO: calculate the acceptance states of the büchi automaton
		}
		
	}
	
	
	public BüchiNode addNewNode() {
		BüchiNode node = new BüchiNode();
		automataNodes.add(node);
		return node;
	}
	
	/*
	 * calculate power set for a given set
	 */
	private static <T> HashSet<HashSet<T>> powerSet(HashSet<T> originalSet) {
	    HashSet<HashSet<T>> sets = new HashSet<HashSet<T>>();
	    if (originalSet.isEmpty()) {
	    	sets.add(new HashSet<T>());
	    	return sets;
	    }
	    List<T> list = new ArrayList<T>(originalSet);
	    T head = list.get(0);
	    HashSet<T> rest = new HashSet<T>(list.subList(1, list.size())); 
	    for (HashSet<T> set : powerSet(rest)) {
	    	HashSet<T> newSet = new HashSet<T>();
	    	newSet.add(head);
	    	newSet.addAll(set);
	    	sets.add(newSet);
	    	sets.add(set);
	    }		
	    return sets;
	}
	/*
	 * synchronous product of büchi automata
	 */
	public BüchiAutomaton product(BüchiAutomaton other) {
		BüchiAutomaton product = new BüchiAutomaton();
		HashMap<BüchiNode,BüchiNode> mapEntry;
		Integer i=0,j=0;
		BüchiNode newNode = null,newChild = null;
		Table<BüchiNode, BüchiNode, BüchiNode> nodeMap = HashBasedTable.create();

		System.out.println(this.automataNodes.size() + "x" + other.automataNodes.size());
		for (BüchiNode node1 : automataNodes) {
			for (BüchiNode node2 : other.automataNodes) {
				newNode = product.addNewNode();
				if (initialNode.equals(node1) && other.initialNode.equals(node2)){
					System.out.println("yes we have an init state"); //in der KS des programms gibt es keine init states
					product.initialNode = newNode; //TODO: wie garantiere ich dass das wirlich nur einer ist?
				}
//				mapEntry = new HashMap<BüchiNode,BüchiNode>();
//				mapEntry.put(node2, newNode);
//				nodeMap.put(node1, mapEntry);
				nodeMap.put(node1, node2, newNode);
			}
		}
		System.out.println("="+product.automataNodes.size() + "//"+nodeMap.size());
//		for (BüchiNode bn : nodeMap.keySet()) {
//			System.out.println(nodeMap.get(bn).size());
//		}
		
		HashSet<AtomicProp> props;
		for (BüchiNode node1 : automataNodes) {
			for (BüchiNode node2 : other.automataNodes) {
				//for each child of node 1
				for (BüchiNode child1 : node1.children.keySet()) {
					//for each child of node 2
					for (BüchiNode child2 : node2.children.keySet()) {
						props = node1.children.get(child1);
						//intersect atomic properties of both children
						props.retainAll(node2.children.get(child2));
						//set child relation and atomic properties for edge
						if (props!=null) {
							System.out.println("yes there are props!"+node1 + "x"+node2);
							nodeMap.get(node1, node2).addChild(nodeMap.get(child1, child2), props);
						}
					}
				}
			}
		}
		return product;
	}
	
	@Override
	public String toString() {
		String retVal;
		retVal = "BüchiAutomaton [";
		for (BüchiNode bn: automataNodes){
			if (initialNode.equals(bn)) 
				retVal+="(Init:" + ((Integer)bn.hashCode()).toString().substring(((Integer)bn.hashCode()).toString().length()-3,((Integer)bn.hashCode()).toString().length()) + bn.toString() + ")";
			else
				retVal += ((Integer)bn.hashCode()).toString().substring(((Integer)bn.hashCode()).toString().length()-3,((Integer)bn.hashCode()).toString().length()) + bn.toString();
		}
		retVal +=  "]";
		return retVal;
	}
	
}
