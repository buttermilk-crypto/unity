package org.unityencoding.tree.model;

import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.unityencoding.antlr.toolkit.UnityAntlr4Listener;
import org.unityencoding.antlr.toolkit.UnityLexer;
import org.unityencoding.antlr.toolkit.UnityParser;


/**
 * <p>Static methods for creating nodes (trees) </p> 
 * 
 * <p>
 * The inspiration for this approach is from an example found in the
 * "practicalxml" library: https://sourceforge.net/p/practicalxml/code/HEAD/tree/
 * </p>
 * 
<pre>
  
        Node root = element("root",
          element("s0", attributes("id", "1", "class", "MyClass"),
			element("a", "some text"),
			element("b", "Some more text")
		  ),
		  element("s1",
			element("a", "some text"),
		    element("b", "Some more text")
		  )
		 );
		
		printJSON(root, true);
	
</pre>
 * 
 * @author Dave
 *
 */
public class Nodes {
	
	private final static Lock lockAttributes = new ReentrantLock();
	private final static Lock lockWalk = new ReentrantLock();
	private final static Lock lockParse = new ReentrantLock();

	public static Node element(String name){
		return new Node(new JsonElement(name));
	}
	
	public static Node element(String name, String text){
		Node e = new Node(new JsonElement(name));
		e.add(new JsonText(text));
		return e;
	}
	
	public static Node element(String name, Number... n){
		Node e = new Node(new JsonElement(name));
		for(Number item: n){
			e.add(new JsonNumber(item));
		}
		return e;
	}
	
	public static Node element(String name, boolean n){
		Node e = new Node(new JsonElement(name));
		e.add(new JsonBool(n));
		return e;
	}
	
	public static Node element(String name, JsonNil n){
		Node e = new Node(new JsonElement(name));
		e.add(n);
		return e;
	}
	
	public static Node element(String name, Node... nodes){
		Node t = new Node(new JsonElement(name));
		t.addAll(nodes);
		return t;
	}
	
	public static Node element(String name, Attributes attribs, Node... nodes){
		Node t = new Node(new JsonElement(name, attribs));
		t.addAll(nodes);
		return t;
	}
	
	public static Node text(String text){
		return new Node(new JsonText(text));
	}
	
	public static Node number(String text){
		
		return new Node(new JsonNumber(text));
	}
	
	public static Node bool(boolean val){
		return new Node(new JsonBool(val));
	}
	
	public static Node nil(){
		return new Node(new JsonNil());
	}
	
	public static final Attributes attributes(String... namesValues) {
		lockAttributes.lock();
		try {
			if (namesValues.length % 2 != 0) {
				throw new RuntimeException(
						"seem to be missing a key or value: " + namesValues);
			}
			Attributes attribs = new Attributes();
			int i = 0;
			String name = null;
			for (String item : namesValues) {
				if (i % 2 == 0) {
					name = item;
				} else {
					attribs.add(name, item);
				}
				i++;
			}
			return attribs;
		} finally {
			lockAttributes.unlock();
		}
	}
	
	/**
	 * Depth-first walk for visitors, thread-safe. Used for walking a tree to print out, etc.
	 * 
	 * @param node
	 * @param visitor
	 * @param depth
	 */
	public static void walk(TreeNode<Payload> node, NodeVisitor visitor, int depth) {
		lockWalk.lock();
		try {
			if(node == null) return;
			visitor.setDepth(++depth);
			visitor.start(node.data);

			if(node.children != null){
				visitor.setLeaf(false);
				int size = node.children.size();
				for(int index = 0; index<size; index++){
					TreeNode<Payload> item = node.children.get(index);
					walk(item, visitor, depth);
				}
			}else{
				visitor.setLeaf(true);
			}
			
			visitor.end(node.data);
			visitor.setDepth(--depth);
			visitor.setPrevious(node);

		} finally {
			lockWalk.unlock();
		}
	}
	
	public static final void printJson(TreeNode<Payload> root, Writer writer, boolean prettyPrint){
		PrintJSONVisitor visitor = new PrintJSONVisitor(writer, prettyPrint);
		Nodes.walk(root, visitor, 0);
	}
	
	public static final void printXML(TreeNode<Payload> root, Writer writer, boolean prettyPrint){
		PrintXMLVisitor visitor = new PrintXMLVisitor(writer, prettyPrint);
		Nodes.walk(root, visitor, 0);
	}
	
	public static final void printJson(TreeNode<Payload> root){
		StringWriter w = new StringWriter();
		PrintJSONVisitor visitor = new PrintJSONVisitor(w, true);
		Nodes.walk(root, visitor, 0);
		System.out.println(w.toString());
	}
	
	public static final void printXML(TreeNode<Payload> root){
		StringWriter w = new StringWriter();
		PrintXMLVisitor visitor = new PrintXMLVisitor(w, true);
		Nodes.walk(root, visitor, 0);
		System.out.println(w.toString());
	}
	
	
	/**
	 * Parse and return the root node
	 * 
	 * @param in
	 * @return
	 */
	public static final TreeNode<Payload> parseUnity(String in){
		lockParse.lock();
		try {
			ANTLRInputStream cStream = new ANTLRInputStream(in);
			UnityLexer lexer = new UnityLexer(cStream);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			UnityParser p = new UnityParser(tokens);
			UnityParser.JsonContext tree = p.json();
			UnityAntlr4Listener l = new UnityAntlr4Listener();
			ParseTreeWalker.DEFAULT.walk(l, tree);
			return l.getRoot();
		}finally{
			lockParse.unlock();
		}
	}

}
