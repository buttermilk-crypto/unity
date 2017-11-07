package org.unityencoding.tree.model;

public class JsonBool extends Payload {

	public final boolean b;
	
	public JsonBool(boolean b) {
		super();
		this.b = b;
	}
	
	public String toString() {
		return String.valueOf(b);
	}

}
