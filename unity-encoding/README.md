# Jsonxf - JSON XML Formalism

The JSON XML Formalism is a set of constraints applied to the JSON encoding process which makes the resulting output
more usable in the context of XML compatibility, data validation, and probably for other uses not yet identified. 

Every Jsonxf formatted file is pure JSON: it can be parsed with any JSON parser and consumed by any existing JSON-based tooling. On the other hand, few contemporary JSON files found in the wild will pass the Jsonxf validator. 
Jsonxf is quite a different animal.

The best way to understand Jsonxf is to look at a few productions:

| XML                             | JSONXF                      | 
| --------------------            |-----------------------------|
| &lt;s/&gt;                      | ["s"]                       |
| &lt;s&gt;text&lt;/s&gt;         | ["s", "text"]               |
| &lt;s id="val"/&gt;             | ["s", {"id":"val"}]         |
| &lt;s id="val"&gt;text&lt;/s&gt;| ["s", {"id":"val"}, "text"] |


<table>
<tr><th>XML</th><th>JSONXF</th></tr>
<tr><td>
 &lt;s&gt; <br/>                          
 &nbsp;&nbsp;&nbsp;&nbsp; &lt;a&gt;Some text&lt;/a&gt; <br/>      
 &nbsp;&nbsp;&nbsp;&nbsp; &lt;b&gt;More text&lt;/b&gt; <br/>      
 &lt;/s&gt;  <br/>                    
 </td><td>
 ["s", <br/>
 &nbsp;&nbsp;&nbsp;&nbsp;["a", "Some text"],<br/>
 &nbsp;&nbsp;&nbsp;&nbsp;["b", "More text"] <br/>
 ] <br/>
 </td></tr>
 </table>
 
 Here is a list of the Jsonxf constraints:
 
  - The character encoding must be UTF-8.
  - The root production must be an array and cannot be of zero size.
  - In the array, the first position is always a String which must meet the same character constraints as those for XML element names.
  - The second position of the array may be an object. This is the only position in the array where an object may appear.
  - If it appears, the object's structure must meet certain constraints which match attributes in XML elements:
  - The pair name must consist of valid characters (same constraint as an XML element name).
  - The pair value must be a string type only. No other type may be used for a value.
  - In an array the third position and all additional positions are optional. 
  - In an array the third position and all additional positions beyond the third may contain any JSON type other 
than an object. These items would typically be string or array types which is how markup is represented, but
the other types can be used. 
  - These nested arrays (which may be nested to any depth) must follow the same constraints as described 
above for the root production. 

That's pretty much all there is to know. We'll cover XML entities and whitespace handling further on. 

## Tooling

In this package are a few tools to start the conversation about Jsonxf. To convert XML into JSON, you can use
something like the following:

    try(
    InputStream in = this.getClass().getResourceAsStream("/data/xml/books.xml");
	 InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
	 ){
	     XMLToJSON converter = new XMLToJSON(reader);
	     converter.setKeepIgnorableWhitespace(false);
	     converter.setTrim(true);
	     converter.setPrettyPrint(true);
	     String output = converter.parse();
	     System.out.println(output);
	 }catch(IOException x){
		x.printStackTrace();
	 }
	 
The various whitespace-related methods are necessary because XML, being a species of markup, is overly concerned
with whitespace and its handling. This is one of the improvements we see in JSON. 

At any rate, the above is a SAXParser-based implementation and turns out efficient conversions.

To generate XML from Jsonxf, you can use the following tool:

    try (
		InputStream in = this.getClass().getResourceAsStream("/data/jsonxf/books.json");
		InputStreamReader r =	new InputStreamReader(in);
	){
	  JSONToXML jx = new JSONToXML(r);
	  jx.walk();
	  System.out.println(jx.pretty());
	}catch(IOException x){
		x.printStackTrace();
	}

## Validation

Valid XML will always convert to valid Json, but going the other way it helps to have a validator. I've provided
a very simple one:

	for(int i = 0;i<8;i++){
		try (
				InputStream in =  this.getClass()
				     .getResourceAsStream("/data/jsonxf/simple"+i+".json");
				InputStreamReader r =	new InputStreamReader(in);
			){
				ValidateJsonxf validator = new ValidateJsonxf(r);
				boolean ok = validator.validate();
				Assert.assertTrue(ok);
			}catch(IOException x){
				x.printStackTrace();
			}
	}

## Future Directions

The real work with validation will involve using XML schema validation techniques on pure Jsonxf encoded files. A high
priority is to implement XPath.



