package org.unityencoding.antlr.toolkit.listener;

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
	protected TreeNode<Payload> current;
	
	protected Date startTime, endTime;
	
	public UnityAntlr4Listener() {}
	
	@Override public void enterJson(UnityParser.JsonContext ctx) { 
		// start
		startTime = new Date();
	}
	
	@Override public void exitJson(UnityParser.JsonContext ctx) { 
		// end
		endTime = new Date();
	}
	
	@Override public void enterArray(UnityParser.ArrayContext ctx) { 
		String name = trim(ctx.STRING().getText());
		if(root == null){
			root = Nodes.element(name);
			current = root;
		}else{
			Node e = Nodes.element(name);
			current.add(e);
			current = e;
		}
	}
	
	@Override public void enterObject(UnityParser.ObjectContext ctx) { 
		JsonElement je = (JsonElement) current.data;
		je.attribs = new Attributes();
		
	}
	
	@Override public void enterPair(UnityParser.PairContext ctx) { 
		String name = trim(ctx.STRING(0).getText());
		String value = trim(ctx.STRING(1).getText());
		JsonElement je = (JsonElement) current.data;
		je.getAttribs().add(name, value);
	}
	
	@Override public void enterStringValue(UnityParser.StringValueContext ctx) {
		String val = trim(ctx.getText());
		current.add(new JsonText(val));
	}
	
	@Override public void enterNumberValue(UnityParser.NumberValueContext ctx) { 
		String val = ctx.NUMBER().getText();
		current.add(new JsonNumber(val));
	}
	
	@Override public void enterAtom(UnityParser.AtomContext ctx) { 
		String val = ctx.getText();
		if(val.equals("true")){
			current.add(new JsonBool(true));
		}else if(val.equals("false")){
			current.add(new JsonBool(false));
		}else if(val.equals("null")){
			current.add(new JsonNil());
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

	public TreeNode<Payload> getCurrent() {
		return current;
	}

	public void setCurrent(TreeNode<Payload> current) {
		this.current = current;
	}
	
	public long parseTimeMillis(){
		return endTime.getTime()-startTime.getTime();
	}
	
}
