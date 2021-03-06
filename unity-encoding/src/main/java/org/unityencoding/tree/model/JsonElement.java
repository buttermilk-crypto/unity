package org.unityencoding.tree.model;

/**
 * An element has a name, which is required and non-null, and optional set of attributes.
 * 
 * @author Dave
 *
 */
public class JsonElement implements Payload {

	public final String name;
	public Attributes attribs;
	
	public JsonElement(String name) {
		super();
		this.name = name;
		if(!validName(name)) throw new RuntimeException("node name not valid: "+name);
		this.attribs = null;
	}
	
	public JsonElement(String name, Attributes attribs) {
		super();
		this.name = name;
		if(!validName(name)) throw new RuntimeException("node name not valid: "+name);
		this.attribs = attribs;
	}
	
	public boolean hasAttributes(){
		if(attribs == null) return false;
		else return attribs.isEmpty();
	}

	@Override
	public String toString() {
		return name;
	}

	public Attributes getAttribs() {
		return attribs;
	}

	public void setAttribs(Attributes attribs) {
		this.attribs = attribs;
	}
	
	public void addAttribute(String name, String value){
		if(attribs == null) attribs = new Attributes();
		attribs.add(name, value);
	}
	
	/**
	 * Return true if the input string matches the rules for a valid xml element or 
	 * attribute ID name character pattern
	 * 
	 * @param name
	 * @return
	 */
	protected boolean validName(String name){
		if(name == null) return false;
		return org.apache.xerces.util.XMLChar.isValidName(name) && !name.toLowerCase().startsWith("xml") ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attribs == null) ? 0 : attribs.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		JsonElement other = (JsonElement) obj;
		if (attribs == null) {
			if (other.attribs != null)
				return false;
		} else if (!attribs.equals(other.attribs))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
