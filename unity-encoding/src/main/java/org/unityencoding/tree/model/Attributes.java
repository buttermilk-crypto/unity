package org.unityencoding.tree.model;

/**
 * Attribute container. A lightweight alternative to a hash map. 
 * 
 * @author Dave
 *
 */
public class Attributes {

	String [] names;
	String [] values;
	
	public Attributes() {
		names = new String[0];
		values = new String[0];
	}
	
	public synchronized Attributes add(String name, String value){
		if(name == null){
			throw new RuntimeException("name field cannot be null.");
		}
		if(value == null){
			throw new RuntimeException("value field cannot be null.");
		}
		
		// expand the arrays
		expand(1);
		names[names.length-1] = name;
		values[values.length-1] = value;
		
		return this;
	}
	
	private void expand(int size){
		int length = names.length;
		String [] newNames = new String[length+size];
		System.arraycopy(names, 0, newNames, 0, length);
		names = null;
		names = newNames;
		length = values.length;
		String [] newValues = new String[length+size];
		System.arraycopy(values, 0, newValues, 0, length);
		values = null;
		values = newValues;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for(int i =0;i<names.length;i++){
			buf.append(" ");
			buf.append(names[i]);
			buf.append("=");
			buf.append("\"");
			buf.append(values[i]);
			buf.append("\"");
			//if(i<names.length-1) buf.append(" ");
		}
		return buf.toString();
	}
	
	public String toStringOneLine() {
		StringBuffer buf = new StringBuffer();
		for(int i =0;i<names.length;i++){
			buf.append(names[i]);
			buf.append("=");
			buf.append("\"");
			buf.append(values[i]);
			buf.append("\"");
			buf.append(",");
		}
		
		if(names.length>1){
			buf.deleteCharAt(buf.length()-1);
		}
		return buf.toString();
	}
	
	public String toJSON() {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		for(int i =0;i<names.length;i++){
			buf.append("\"");
			buf.append(names[i]);
			buf.append("\"");
			buf.append(":");
			buf.append("\"");
			buf.append(values[i]);
			buf.append("\"");
			if(i<names.length-1)buf.append(", ");
		}
		buf.append("}");
		return buf.toString();
	}
	
	public boolean isEmpty() {
		return names.length > 0;
	}
	
}
