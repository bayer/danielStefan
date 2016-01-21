import java.util.HashSet;

import com.pengyifan.commons.collections.tree.TreeNode;

public class TableauNode {
	String 					name;
	HashSet<String> 		incoming;
	HashSet<TreeNode> 		new1;
	HashSet<TreeNode> 		old;
	HashSet<TreeNode> 		next;
	HashSet<Character>			label;
	
	public TableauNode(String name, HashSet<String> incoming, HashSet<TreeNode> new1, HashSet<TreeNode> old,
			HashSet<TreeNode> next, HashSet<Character> label) {
		super();
		this.name = name;
		this.incoming = incoming;
		this.new1 = new1;
		this.old = old;
		this.next = next;
		this.label = label;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((next == null) ? 0 : next.hashCode());
		result = prime * result + ((old == null) ? 0 : old.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableauNode other = (TableauNode) obj;
		if (next == null) {
			if (other.next != null)
				return false;
		} else if (!next.equals(other.next))
			return false;
		if (old == null) {
			if (other.old != null)
				return false;
		} else if (!old.equals(other.old))
			return false;
		return true;
	}
	
	
	
}
