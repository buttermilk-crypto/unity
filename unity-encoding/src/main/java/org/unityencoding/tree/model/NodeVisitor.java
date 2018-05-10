package org.unityencoding.tree.model;

/**
 * Interface for node visitors who walk the tree
 * 
 * @author Dave
 * @see NodeUtil.walk
 */
public interface NodeVisitor {

	public void start(Payload data);
	public void end(Payload data);
	public void setDepth(int depth);
	public void setLeaf(boolean isLeaf);
	public void setPrevious(TreeNode<Payload> prevNode);
	
	public void setRoot(TreeNode<Payload> root);

}
