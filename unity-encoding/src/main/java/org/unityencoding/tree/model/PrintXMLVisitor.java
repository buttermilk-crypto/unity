package org.unityencoding.tree.model;

import java.io.Writer;

import org.apache.commons.text.StringEscapeUtils;

/**
 * Print out XML from our local, simple tree data model. 
 * 
 * @author Dave
 *
 */

public class PrintXMLVisitor extends PrintFormattingBase implements NodeVisitor {
	
	TreeNode<Payload> root;
	
	public PrintXMLVisitor() {
		super();
	}
	
	public PrintXMLVisitor(boolean prettyPrint) {
		super(prettyPrint);
	}

	public PrintXMLVisitor(Writer writer, boolean prettyPrint) {
		super(writer, prettyPrint);
	}
	
	public void writeHeader() {
		write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	}

	@Override
	public void start(Payload data) {
		
		if(data instanceof JsonElement){
			JsonElement e = (JsonElement) data;
			
			if(this.depth <= 1){
				write("<");
			}else{
				if(pretty){
					 write('\n'); write(spaces(depth)); write("<");
				}else{
					write("<");
				}
			}
	
			write(e.name);
			
			if(e.hasAttributes()) {
				write(e.attribs.toString());
				write(">");
			}else{
				write(">");
			}
			
			
		}else if(data instanceof JsonText){
			
			JsonText t = (JsonText) data;
			write(StringEscapeUtils.unescapeXml(t.value));	
			
		}else if(data instanceof JsonNumber){
			JsonNumber t = (JsonNumber) data;
			if(notStart()){
				write(' ');
			}
			write(String.valueOf(t.val));
		}else if(data instanceof JsonBool){
			if(notStart()){
				write(' ');
			}
			JsonBool t = (JsonBool) data;
			write(String.valueOf(t.b));
		}else if(data instanceof JsonNil){
			if(notStart()){
				write(' ');
			}
			write("null");
		}
			
	}

	@Override
	public void end(Payload data) {
		
		if(data instanceof JsonElement){
				if(pretty){
					if(notStart()){
						write("</");
						write(((JsonElement) data).name);
						write(">");
					}else{
						write('\n'); 
						write(spaces(depth));
						write("</");
						write(((JsonElement) data).name);
						write(">");
					}
				}else{
					write("<");
					write(((JsonElement) data).name);
					write("/>");
				}
		}	
	}

	@Override
	public void setLeaf(boolean isLeaf) {
		this.leaf = isLeaf;
	}


	@Override
	public void setPrevious(TreeNode<Payload> prevNode) {
		this.previousNode = prevNode;
	}

	@Override
	public void setRoot(TreeNode<Payload> root) {
		this.root = root;
		
	}

}
