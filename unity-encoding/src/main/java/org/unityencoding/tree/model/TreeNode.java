package org.unityencoding.tree.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Modified from https://github.com/gt4dev/yet-another-tree-structure
 * 
 * @author Dave
 *
 * @param <T>
 */
public class TreeNode<T> implements Iterable<TreeNode<T>> {
	
	public T data;
	public TreeNode<T> parent;
	public List<TreeNode<T>> children; // lazy create
	public int depth; 

	public TreeNode(T data, TreeNode<T> parent) {
		super();
		this.data = data;
		this.parent = parent;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeaf() {
		return children == null || children.size() == 0;
	}
	
	public String dataAsString() {
		return String.valueOf(data);
	}

	public TreeNode(T data) {
		this.data = data;
	}

	public TreeNode<T> add(T value) {
		// lazy init, this saves memory on leaf nodes
		if(children == null) {
			children = new LinkedList<TreeNode<T>>();
		}
		TreeNode<T> child = new TreeNode<T>(value);
		child.parent = this;
		children.add(child);
		return child;
	}
	
	public TreeNode<T> add(TreeNode<T> child) {
		// lazy init, this saves memory on leaf nodes
		if(children == null) {
			children = new LinkedList<TreeNode<T>>();
		}
		child.parent = this;
		children.add(child);
		return child;
	}
	
	public void addAll(TreeNode<T> [] list) {
		// lazy init, this saves memory on leaf nodes
		if(children == null) {
			children = new LinkedList<TreeNode<T>>();
		}
		
		for(TreeNode<T> child: list){
			child.parent = this;
			children.add(child);
		}

	}

	public int getLevel() {
		if (this.isRoot())
			return 0;
		else
			return parent.getLevel() + 1;
	}

	@Override
	public String toString() {
		return data != null ? data.toString() : "[data null]";
	}

	@Override
	public Iterator<TreeNode<T>> iterator() {
		TreeNodeIterator<T> iter = new TreeNodeIterator<T>(this);
		return iter;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

}
