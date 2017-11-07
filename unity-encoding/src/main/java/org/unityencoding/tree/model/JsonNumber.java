package org.unityencoding.tree.model;

public class JsonNumber extends Payload {

	public final Number val;
	
	public JsonNumber(Number val) {
		super();
		this.val = val;
	}
	
	public JsonNumber(String textVal) {
		super();
		if(textVal.contains("e") || textVal.contains("E")){
			val = Double.parseDouble(textVal);
		}else if(textVal.contains(".")){
			val = Double.parseDouble(textVal);
		}else{
			val = Integer.parseInt(textVal);
		}
	}
	
	public String toString() {
		return String.valueOf(val);
	}

}
