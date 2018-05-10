package org.unityencoding.tree.model;

public class JsonNumber implements Payload {

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((val == null) ? 0 : String.valueOf(val).hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JsonNumber other = (JsonNumber) obj;
		if (val == null) {
			if (other.val != null)
				return false;
		} else if (!val.equals(other.val))
			return false;
		return true;
	}
	
	

}
