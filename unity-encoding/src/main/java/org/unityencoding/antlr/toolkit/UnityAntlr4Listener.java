package org.unityencoding.antlr.toolkit;

import java.util.Date;

import org.unityencoding.antlr.toolkit.UnityBaseListener;
import org.unityencoding.antlr.toolkit.UnityParser;
import org.unityencoding.tree.model.*;

/**
 * <p>Extend the antlr4 generated base class to make an event listener we can pass to the antlr4 
 * ParseTreeWalker.</p> 
 * 
 * <pThis event listener builds a tree data structure using instances of 
 * the classes found in org.unityencoding.tree.model.</p> 
 * 
 * <p>Use getRoot() accessor method to get the completed tree.</p>
 * 
 * @author Dave
 * 
 */

public class UnityAntlr4Listener extends UnityBaseListener  {

	protected TreeNode<Payload> root;
	protected TreeNode<Payload> parent;
	protected TreeNode<Payload> child;
	
	protected Date startTime, endTime;
	protected int depth;
	
	public UnityAntlr4Listener() {}
	
	@Override public void enterJson(UnityParser.JsonContext ctx) { 
		// start
		startTime = new Date();
		depth = 0;
	}
	
	@Override public void exitJson(UnityParser.JsonContext ctx) { 
		// end
		endTime = new Date();
	}
	
	@Override public void enterArray(UnityParser.ArrayContext ctx) { 
		
		String name = trim(ctx.STRING().getText());
	//	System.err.println(depth+" "+name);
		
		if(depth == 0){
			root = Nodes.element(name);
			root.setDepth(depth);
			parent = root;
			child = null;
		}else{
			Node e = Nodes.element(name);
			e.setDepth(depth);
			
			if(depth == parent.getDepth()) {
				
				// up one
				parent = parent.parent;
				child = e;
				parent.add(e);
				
			}else if(depth - parent.getDepth() == 1){
				child = e;
				parent.add(e);
			}else if(depth - parent.getDepth() == 2){
				parent = child;
				parent.add(e);
				child = e;
			}else if(depth - parent.getDepth() == 0){
				child = e;
				parent.add(e);
			}
		}
		
		depth++;
		
	}
	
	@Override public void exitArray(UnityParser.ArrayContext ctx) { 
		depth--;
		//System.err.println(depth);
	}

	
	@Override public void enterObject(UnityParser.ObjectContext ctx) { 
		
		JsonElement je = (JsonElement) parent.data;
		je.attribs = new Attributes();
	}
	
	@Override public void enterPair(UnityParser.PairContext ctx) { 
		
		String name = trim(ctx.STRING(0).getText());
		String value = trim(ctx.STRING(1).getText());
		JsonElement je = (JsonElement) parent.data;
		je.getAttribs().add(name, value);
		
	}
	
	@Override public void enterStringValue(UnityParser.StringValueContext ctx) {
		
		String val = trim(ctx.getText());
		if(child == null) parent.add(new JsonText(val));
		else child.add(new JsonText(val));
		
	}
	
	@Override public void enterNumberValue(UnityParser.NumberValueContext ctx) { 
		
		String val = ctx.NUMBER().getText();
		if(child == null) parent.add(new JsonNumber(val));
		else child.add(new JsonNumber(val));
		
	}
	
	@Override public void enterAtom(UnityParser.AtomContext ctx) { 
		
		String val = ctx.getText();
		if(val.equals("true")){
			if(child == null) parent.add(new JsonBool(true));
			else child.add(new JsonBool(true));
		}else if(val.equals("false")){
			if(child == null) parent.add(new JsonBool(false));
			else child.add(new JsonBool(false));
		}else if(val.equals("null")){
			if(child == null) parent.add(new JsonNil());
			else child.add(new JsonNil());
		}
		
			
	}
	
	

	private String trim(String val){
		
		boolean startsWith = false, endsWith = false;
		if(isQuote(val.charAt(0))) {
			startsWith = true;
		}
		if(isQuote(val.charAt(val.length()-1))) {
			endsWith = true;
		}
		StringBuffer buf = new StringBuffer(val);
		if(startsWith) buf.deleteCharAt(0);
		if(endsWith) buf.deleteCharAt(buf.length()-1);
		return buf.toString();
	}
	
	private boolean isQuote(char ch){
		switch(ch){
			case '"': return true;
			case '\u201C': return true;
			case '\u201D': return true;
			case '\u2018': return true;
			case '\u2019': return true;
			default: return false;
		}
	}

	public TreeNode<Payload> getRoot() {
		return root;
	}

	public void setRoot(TreeNode<Payload> root) {
		this.root = root;
	}
	
	public long parseTimeMillis(){
		return endTime.getTime()-startTime.getTime();
	}
	
}
