package org.unityencoding.tree.model;

public class JsonBool implements Payload {

	public final boolean b;
	
	public JsonBool(boolean b) {
		super();
		this.b = b;
	}
	
	public String toString() {
		return String.valueOf(b);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj != null && obj.getClass().equals(JsonBool.class)) {
			JsonBool bval = (JsonBool) obj;
			if(bval.b == b) return true;
		}
		return false;
	
	}

}
