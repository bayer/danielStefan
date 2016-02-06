package DataStructures;

import java.util.HashSet;

import com.pengyifan.commons.collections.tree.TreeNode;

public class TableauNode {
	private String 					name;
	private HashSet<TableauNode> 		incoming;
	private HashSet<TreeNode> 		new1;
	private HashSet<TreeNode> 		old;
	private HashSet<TreeNode> 		next;
	private HashSet<Character>			label;
	
	public TableauNode(String name, HashSet<TableauNode> incoming, HashSet<TreeNode> new1, HashSet<TreeNode> old,
			HashSet<TreeNode> next, HashSet<Character> label) {
		super();
		this.setName(name);
		this.setIncoming(incoming);
		this.setNew1(new1);
		this.setOld(old);
		this.setNext(next);
		this.setLabel(label);
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




	/**
	 * @return the new1
	 */
	public HashSet<TreeNode> getNew() {
		return new1;
	}


	/**
	 * @param new1 the new1 to set
	 */
	public void setNew1(HashSet<TreeNode> new1) {
		this.new1 = new1;
	}


	/**
	 * @return the old
	 */
	public HashSet<TreeNode> getOld() {
		return old;
	}


	/**
	 * @param old the old to set
	 */
	public void setOld(HashSet<TreeNode> old) {
		this.old = old;
	}


	/**
	 * @return the label
	 */
	public HashSet<Character> getLabel() {
		return label;
	}


	/**
	 * @param label the label to set
	 */
	public void setLabel(HashSet<Character> label) {
		this.label = label;
	}


	/**
	 * @return the incoming
	 */
	public HashSet<TableauNode> getIncoming() {
		return incoming;
	}


	/**
	 * @param incoming the incoming to set
	 */
	public void setIncoming(HashSet<TableauNode> incoming) {
		this.incoming = incoming;
	}


	/**
	 * @return the next
	 */
	public HashSet<TreeNode> getNext() {
		return next;
	}


	/**
	 * @param next the next to set
	 */
	public void setNext(HashSet<TreeNode> next) {
		this.next = next;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
