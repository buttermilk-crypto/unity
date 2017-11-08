package org.unityencoding.toolkit;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.unityencoding.antlr.toolkit.UnityAntlr4Listener;
import org.unityencoding.antlr.toolkit.UnityLexer;
import org.unityencoding.antlr.toolkit.UnityParser;
import org.unityencoding.tree.model.Nodes;
import org.unityencoding.tree.model.Payload;
import org.unityencoding.tree.model.TreeNode;



public class ANTLRTest {
	
	@Test
	public void test0() {
		
		
		for(int i = 0; i<9;i++){
		
			try (InputStream in = this.getClass().getResourceAsStream(
					"/data/unity/simple"+i+".json");
			) {
				ANTLRInputStream cStream = new ANTLRInputStream(in);
				UnityLexer lexer = new UnityLexer(cStream);
				CommonTokenStream tokens = new CommonTokenStream(lexer);
				UnityParser p = new UnityParser(tokens);
				UnityParser.JsonContext tree = p.json();
				UnityAntlr4Listener l = new UnityAntlr4Listener();
			//	TreePrinterListener l = new TreePrinterListener(p);
			    ParseTreeWalker.DEFAULT.walk(l, tree);
			 //   System.err.println(l);
				TreeNode<Payload> r = l.getRoot();
				Nodes.printJson(r);
				System.out.print("\n");
			//	Nodes.printXML(r);
			} catch (IOException x) {
				x.printStackTrace();
			}
		}
	}
	
	
	@Test
	public void test1() {
		
			try (InputStream in = this.getClass().getResourceAsStream(
					"/data/unity/books.json");
			) {
				ANTLRInputStream cStream = new ANTLRInputStream(in);
				UnityLexer lexer = new UnityLexer(cStream);
				CommonTokenStream tokens = new CommonTokenStream(lexer);
				UnityParser p = new UnityParser(tokens);
				UnityParser.JsonContext tree = p.json();
				UnityAntlr4Listener l = new UnityAntlr4Listener();
			//	TreePrinterListener l = new TreePrinterListener(p);
			    ParseTreeWalker.DEFAULT.walk(l, tree);
			 //   System.err.println(l);
				TreeNode<Payload> r = l.getRoot();
				Nodes.printJson(r);
				System.out.print(r);
				Nodes.printXML(r);
				System.out.print(r);
			//	Nodes.printXML(r);
			} catch (IOException x) {
				x.printStackTrace();
			}
	}

	@Test
	public void test2() {
		
	
			try (InputStream in = this.getClass().getResourceAsStream(
					"/data/unity/no-attrib.json");
			) {
				ANTLRInputStream cStream = new ANTLRInputStream(in);
				UnityLexer lexer = new UnityLexer(cStream);
				CommonTokenStream tokens = new CommonTokenStream(lexer);
				UnityParser p = new UnityParser(tokens);
				UnityParser.JsonContext tree = p.json();
				UnityAntlr4Listener l = new UnityAntlr4Listener();
			//	TreePrinterListener l = new TreePrinterListener(p);
			    ParseTreeWalker.DEFAULT.walk(l, tree);
			//    System.err.println(l);
				TreeNode<Payload> r = l.getRoot();
				Nodes.printJson(r);
			//	System.out.print(r);
			//	Nodes.printXML(r);
			} catch (IOException x) {
				x.printStackTrace();
			}
	}

}
