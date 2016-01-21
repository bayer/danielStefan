import java.util.LinkedList;

/*
 * names of nodes are int values that correspond to position in dequeOfLKSNodes
 */

public class KripkeStructure {
	public class KSNode {
		private LinkedList<KSNode> dequeOfChildren = new LinkedList<KSNode>();
		private LinkedList<KSNode> dequeOfParents = new LinkedList<KSNode>();
		private LinkedList<String> predicates = new LinkedList<String>();
		private Integer originalNode;
		
		/**
		 * adds child <b>node</b> to <b>this</b>, and <b>this</b> as parent to <b>node</b>
		 * @param node
		 */
		public void addChild(KSNode node) {
			dequeOfChildren.add(node);
			node.dequeOfParents.add(this);
		}

		//TODO: only add predicate if it doesnt already exist
		public void addPredicate(String predicate) { 
			predicates.add(predicate);
		}

		public void addOriginalNode(Integer toNode) {
			// TODO Auto-generated method stub
			this.originalNode = toNode;
		}
		
		public Integer getOriginalNode() {
			return originalNode;
		}
	}
	
	public KSNode rootPointer;
	private LinkedList<KSNode> dequeOfLKSNodes;

	
	public KripkeStructure() {
		super();
		rootPointer = new KSNode();
		dequeOfLKSNodes = new LinkedList<KSNode>();
	}
	
	public void addNewNode() {
		dequeOfLKSNodes.add(new KSNode());
	}
	
	public KSNode get(int pos) {
		return dequeOfLKSNodes.get(pos);
	}

	public Integer size() {
		return dequeOfLKSNodes.size();
	}
	
	public Boolean checkKripkeStructure() {
		for (KSNode node: dequeOfLKSNodes) {
			if (null == node.originalNode) {
				return false;
			}
		}
		return true;
	}
}
