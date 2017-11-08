package org.unityencoding.tree.model;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.text.StringEscapeUtils;

/**
 * Base class for visitors which output results to a writer
 * 
 * @author Dave
 * @see PrintJSONVisitor
 * @see PrintXMLVisitor
 * 
 */
public class PrintFormattingBase {

	protected Writer writer;
	protected int depth;
	protected boolean pretty;
	
	protected boolean leaf;
	protected TreeNode<Payload> previousNode;
	
	public PrintFormattingBase() {
		super();
		writer = new StringWriter();
	}
	
	public PrintFormattingBase(boolean prettyPrint) {
		this();
		pretty = prettyPrint;
	}
	
	public PrintFormattingBase(Writer writer, boolean prettyPrint) {
		super();
		this.writer = writer;
		pretty = prettyPrint;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	protected String escapeAndQuote(String in){
		return "\""+StringEscapeUtils.escapeJson(in)+"\"";
	}
	
	protected String spaces(int count){
		StringBuilder b = new StringBuilder();
		for(int i =0;i<count;i++){
			b.append(' ');
		}
		return b.toString();
	}

	public boolean isPretty() {
		return pretty;
	}

	public void setPretty(boolean pretty) {
		this.pretty = pretty;
	}

	public Writer getWriter() {
		return writer;
	}
	
	public void write(String input){
		try {
			this.writer.write(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeln(String input){
		try {
			this.writer.write(input);
			this.writer.write('\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(char ... input){
		try {
			this.writer.write(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected boolean notStart(){
		return (previousNode != null && !(previousNode.data instanceof JsonElement));
	}
}
