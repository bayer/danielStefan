public class NodeObject {
	public enum NodeType {
		UNARY_OP, BINARY_OP, PROPOSITION
	}
	public String name;
	public NodeType nodeType;
	public Boolean negative;
	
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

		@Override
	public String toString() {
		String neg = (this.negative)  ? "!" : "";
		return neg + name + " [" + nodeType + "]";
	}
	

}
