package org.unityencoding.tree.model;

import org.junit.Test;
import org.unityencoding.tree.model.Node;
import org.unityencoding.tree.model.Nodes;
import org.unityencoding.tree.model.PrintJSONVisitor;
import org.unityencoding.tree.model.PrintXMLVisitor;


public class TestTreeNode {
	
	@Test
	public void test1() {
		   Node root = Nodes.element("root");
					
		   	PrintJSONVisitor visitor = new PrintJSONVisitor();
			Nodes.walk(root, visitor, 0);
			System.err.println(visitor.writer.toString());
	}
	
	@Test
	public void test2() {
		   Node root = Nodes.element("root", "some text");
					
		   	PrintJSONVisitor visitor = new PrintJSONVisitor();
			Nodes.walk(root, visitor, 0);
			System.err.println(visitor.writer.toString());
	}
	
	@Test
	public void test3() {
		
		   Node root = Nodes.element("root", Nodes.attributes("id", "1"),
				Nodes.element("a", "some text"),
				Nodes.element("b", "some more text")
		   );
					
		 //  	PrintJSONVisitor visitor = new PrintJSONVisitor();
		//	Nodes.walk(root, visitor, 0);
		//	System.err.println(visitor.writer.toString());
			
		   PrintJSONVisitor visitor = new PrintJSONVisitor(true);
			Nodes.walk(root, visitor, 0);
			System.err.println(visitor.writer.toString());
	}

	@Test
	public void test4() {
		   Node root = Nodes.element("root",
			          Nodes.element("s0", Nodes.attributes("id", "1", "class", "MyClass"),
						Nodes.element("a", "some text"),
						Nodes.element("b", "Some more text")
					  ),
					 Nodes. element("s1",
						Nodes.element("a", "some text"),
					    Nodes.element("b", "Some more text")
					  ),
					   Nodes. element("s2",
						Nodes.element("c", 1, 4.7, 9, 3.30e-23),
					    Nodes.element("d", "Some more text")
					  )
					 );
					
		   			PrintJSONVisitor visitor = new PrintJSONVisitor(true);
					Nodes.walk(root, visitor, 0);
					System.err.println(visitor.writer.toString());
					
	}
	
	@Test
	public void test5() {
		   Node root = Nodes.element("root",
			          Nodes.element("s0", Nodes.attributes("id", "1", "class", "MyClass"),
						Nodes.element("a", "some text"),
						Nodes.element("b", "Some more text")
					  ),
					 Nodes. element("s1",
						Nodes.element("a", "some text"),
					    Nodes.element("b", "Some more text")
					  ),
					   Nodes. element("s2",
						Nodes.element("c", 1, 4.7, 9, 3.30e-23),
					    Nodes.element("d", "Some more text")
					  )
					 );
					
					PrintXMLVisitor visitor1 = new PrintXMLVisitor(true);
					Nodes.walk(root, visitor1, 0);
					System.err.println(visitor1.writer.toString());
					
	}
}
