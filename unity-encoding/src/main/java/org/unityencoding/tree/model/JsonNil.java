package org.unityencoding.tree.model;

public class JsonNil implements Payload {
	
	public JsonNil() {
		super();
	}
	
	public String toString() {
		return String.valueOf(null);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return obj != null && obj.getClass().equals(JsonNil.class);
	
	}

}
