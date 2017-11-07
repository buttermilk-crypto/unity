package org.unityencoding.tree.model;

public class JsonText extends Payload {

	public final String value;
	
	public JsonText(String value) {
		super();
		this.value = value;
	}
	
	public String toString() {
		return value;
	}

}
