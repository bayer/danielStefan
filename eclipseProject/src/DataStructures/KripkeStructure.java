package DataStructures;

import java.util.HashSet;
import java.util.LinkedList;

import org.antlr.v4.runtime.ParserRuleContext;

/*
 * names of nodes are int values that correspond to position in dequeOfLKSNodes
 */

public class KripkeStructure {
	public class LKSNode  {
		private LinkedList<LKSNode> dequeOfChildren = new LinkedList<LKSNode>();
		private LinkedList<LKSNode> dequeOfParents = new LinkedList<LKSNode>();
		private HashSet<AtomicProp> predicates = new HashSet<AtomicProp>();
		private Integer originalNode; // the original node from the "vertexmapping"
										@Override
		public String toString() {
			String retVal = name + "(APs:";
			for (AtomicProp ap: predicates) {
				retVal+= ap + ",";
			}
			retVal +=") [Children:";						
			for (LKSNode child: dequeOfChildren) {
				retVal += child.getName() + (dequeOfChildren.getLast().equals(child)?"":",");
			}
			return retVal+"]";
		}

		// section in the LKS file
		private String name = new String();

		public LKSNode(String name) {
			this.name = name;
		}

		public LKSNode() {

		}

		/**
		 * adds child <b>node</b> to <b>this</b>, and <b>this</b> as parent to
		 * <b>node</b>
		 * 
		 * @param node
		 */
		public void addChild(LKSNode node) {
			dequeOfChildren.add(node);
			node.dequeOfParents.add(this);
		}

		public LinkedList<LKSNode> getChildren() {
			return dequeOfChildren;
		}

		public LinkedList<LKSNode> getParents() {
			return dequeOfParents;
		}

		public String getName() {
			return name;
		}

		// TODO: only add predicate if it doesnt already exist
		public void addPredicate(ParserRuleContext predicate) {
			AtomicProp ap = new AtomicProp(predicate);
			predicates.add(ap);
		}

		public void addOriginalNode(Integer toNode) {
			// TODO Auto-generated method stub
			this.originalNode = toNode;
		}

		public Integer getOriginalNode() {
			return originalNode;
		}

		public void setName(String name) {
			this.name = name;
		}

		public HashSet<AtomicProp> getPredicates() {
			return predicates;
		}

		public void setPredicates(HashSet<AtomicProp> predicates) {
			this.predicates = predicates;
		}
	}

	private LinkedList<LKSNode> dequeOfLKSNodes;
	private LinkedList<LKSNode> dequeOfInitNodes;

	public KripkeStructure() {
		super();
		dequeOfLKSNodes = new LinkedList<LKSNode>();
		dequeOfInitNodes = new LinkedList<LKSNode>();
	}

	@Override
	public String toString() {
		String retVal;
		retVal = "KripkeStructure [";
		for (LKSNode n: dequeOfLKSNodes) {
			retVal += n + " ";
		}
		return retVal +"]";
	}

	public boolean isInitNode(LKSNode node){
		return dequeOfInitNodes.contains(node);
	}
	public LKSNode addNewInitNode() {
		LKSNode node = addNewNode();
		dequeOfInitNodes.add(node);
		return node;
	}

	public LKSNode addNewInitNode(String name) {
		LKSNode node = addNewNode(name);
		dequeOfInitNodes.add(node);
		return node;
	}

	public LKSNode addNewNode() {
		String name = this.size().toString();
		return addNewNode(name);
	}

	public LKSNode addNewNode(String name) {
		LKSNode node = new LKSNode(name);
		dequeOfLKSNodes.add(node);
		return node;
	}

	public LKSNode getByPosition(int pos) {
		return dequeOfLKSNodes.get(pos);
	}

	public Integer size() {
		return dequeOfLKSNodes.size();
	}

	public LinkedList<LKSNode> getNodes() {
		return dequeOfLKSNodes;
	}

}
