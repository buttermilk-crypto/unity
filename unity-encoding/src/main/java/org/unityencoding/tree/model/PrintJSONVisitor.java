package org.unityencoding.tree.model;

import java.io.Writer;

/**
 * Print out JSON from our simple tree data model. 
 * 
 * @author Dave
 *
 */

public class PrintJSONVisitor extends PrintFormattingBase implements NodeVisitor {

	public PrintJSONVisitor() {
		super();
	}
	
	public PrintJSONVisitor(boolean prettyPrint) {
		super(prettyPrint);
	}

	public PrintJSONVisitor(Writer writer, boolean prettyPrint) {
		super(writer, prettyPrint);
	}

	@Override
	public void start(Payload data) {
		
		if(data instanceof JsonElement){
			JsonElement e = (JsonElement) data;
			
			if(this.depth == 1){
				write("[");
			}else{
				if(pretty){
				
				  write(","); write('\n'); write(spaces(depth)); write("[");
	
				}else{
					write(", [");
				}
			}
			write(escapeAndQuote(e.name));
			
			if(e.hasAttributes()) {
				write(", ");
				write(e.attribs.toJSON());
			}
			
			
		}else if(data instanceof JsonText){
		
			JsonText t = (JsonText) data;
			
			// TODO fix quote to deal with JSON escapes
			write(", ");
			write(escapeAndQuote(t.value));
		}else if(data instanceof JsonNumber){
			JsonNumber t = (JsonNumber) data;
			write(", ");
			write(String.valueOf(t.val));
		}else if(data instanceof JsonBool){
			JsonBool t = (JsonBool) data;
			write(", ");
			write(String.valueOf(t.b));
		}else if(data instanceof JsonNil){
			write(", ");
			write("null");
		}
	}

	@Override
	public void end(Payload data) {
		
	    if(data instanceof JsonElement){
				if(pretty){
					
					if(previousNode != null && !(previousNode.data instanceof JsonElement)){
						write("]");
					}else{
						write('\n'); write(spaces(depth)); write("]");
					}
					
				}else{
					write("]");
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

}
