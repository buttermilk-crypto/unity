package org.unityencoding.jackson.toolkit;

import java.io.Reader;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>Constraint checker for Unity encoding. All UNity files are expected to be valid JSON, and validate as JSON directly, 
 * but we have additional constraints to check as part of the formalism.</p>
 * 
 * <p>The constraints of Unity are as follows:</p>
 * 
 * <ol>
 * <li>
 *  The character encoding must be UTF-8.
 * </li>
 * <li>
 *  The root production must be an array and cannot be of zero size
 * </li>
 *  In the root array, the first position is always a String which must meet the documented constraints on 
 *  XML element names.
 * </li>
 * <li>
 *  The second position of the array may optionally be a JSON object. This is the only position in the array where
 * 		a JSON object may appear.
 * </li>
 * <li>
 *  If it does appear, the object's structure must meet certain constraints which match attributes in XML elements:
 * 		a) The pair's name must consist of valid characters (same constraint as an element name).
 * 		b) The pair's value must be a string type only. No other type may be used for a value
 * </li>
 * <li>
 *  The array's third position and all additional positions are optional, but normally will appear (or the document
 *  would not be very useful!)
 * </li>
 * <li>
 *  The array's third position and all additional positions beyond the third may contain any JSON type other 
 *      than an object. These items would typically be string or array types which is how XML mark-up is represented. 
 *      These nested mark-up arrays (which may be nested to any depth) must follow the same constraints as 
 *      described above for the root production. 
 * </li>
 * <li>
 * </ol>
 * 
 * @author Dave
 *
 */
public class ValidateUnityWithJackson {

	Reader reader;
	String problem;
	
	public ValidateUnityWithJackson(Reader reader) {
		this.reader = reader;
	}
	
	@SuppressWarnings("rawtypes")
	public boolean validate(){
		try {
			ObjectMapper mapper = new ObjectMapper();
			Object root = (Object) mapper.readValue(reader, Object.class);
			
			if(!(root instanceof List)){
				problem = "The root production is not an array.";
				return false;
			}
			
			List rootList = (List)root;
			
			for(Object obj: rootList){
				if(obj instanceof List) {
					walk((List)obj);
				}
			}
		
			
		}catch(Exception x){
			problem = x.getMessage();
			return false;
		}
		
		return true;
	}
	
	private boolean walk(List<?> list){
		
		if(list.size() == 0) {
			problem = "constraint: array cannot be of zero size";
			return false;
		}
		if(list.size() > 0){
			if(!(list.get(0) instanceof String)){
				problem = "constraint: first item of any array must be a JSON string";
				return false;
			}
			
			String element = (String) list.get(0);
			if(!validName(element)) {
				problem = "constraint: element name not valid: "+element;
				return false;
			}
		}
		
		if(list.size() > 1){
			
			// check object position
			int count = 0;
			for(Object item: list){
				if(item instanceof Map && count != 1){
					 // if map is not in the second position, this is a constraint error
					problem = "constraint: JSON objects can only appear at zero-based index 1 in a JSON array: "+count;
					return false;
				}
				count++;
			}
			
			// check object attributes if object is present
			Object second = list.get(1);
			if(second instanceof Map){
				// validate attributes
				Map<?,?> map = (Map<?,?>) second;
				for (Object entry : map.entrySet()) {
					Map.Entry<?,?> e = (Map.Entry<?,?>) entry;
					String key = (String) e.getKey();
					if(!validName(key)) {
						problem = "constraint: attribute name not valid: "+key;
						return false;
					}
					if(!(e.getValue() instanceof String)){
						// any type other than a string is an error here
						problem = "constraint: JSON object pair values must be Strings: "+String.valueOf(e.getValue());
						return false;
					}
				}
			}
		}
		
		for(Object item: list){
			if(item instanceof List){
				return walk((List<?>)item);
			}
		}
		
		return true;
	}
	
	/**
	 * Return true if the input string matches the rules for a valid xml element or 
	 * attribute ID name character pattern
	 * 
	 * @param name
	 * @return
	 */
	protected boolean validName(String name){
		return org.apache.xerces.util.XMLChar.isValidName(name) && !name.toLowerCase().startsWith("xml") ;
		
	}

	public String getProblem() {
		return problem;
	}
	
}
