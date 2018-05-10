# The Unity Encoding - JSON As It Could Have Been

The Unity Encoding is a set of constraints applied to the JSON which makes the resulting output
more usable in the context of XML compatibility and data validation. 

Every Unity-encoded file is *pure* JSON: it can be parsed with any JSON parser (such as in a web browser) 
and consumed by any existing JSON-based tooling. Nothing is added.   


The best way to understand Unity is to look at a few productions:

| XML                             | Unity-encoded               | 
| --------------------            |-----------------------------|
| &lt;s/&gt;                      | ["s"]                       |
| &lt;s&gt;text&lt;/s&gt;         | ["s", "text"]               |
| &lt;s id="val"/&gt;             | ["s", {"id":"val"}]         |
| &lt;s id="val"&gt;text&lt;/s&gt;| ["s", {"id":"val"}, "text"] |


<table>
<tr><th>XML</th><th>Unity-encoding</th></tr>
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
 
 Here is a list of the Unity encoding constraints:
 
  - The character encoding is expected to be UTF-8.
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

## "But it looks like a bunch of arrays!"

Yes, that's right. Unity-encoded JSON leverages JSON arrays as the fundamental data-structure. The
ubiquitous JSON Object is only found in one case, that is, for attributes of an array.

Going further, every array has a *name* token in the first slot; there are no anonymous or unnamed arrays.

What this basically means is that the Unity-encoding supports or implements both the Element and Attribute parts of XML Infoset, which are the most important parts.

https://www.w3.org/TR/2004/REC-xml-infoset-20040204/

Which JSON does not. This is the critically important difference. By supporting Elements and Attributes Infoset, we can do
many things JSON by itself cannot do. 


## Tooling

In this package are a few tools to start the conversation about the Unity encoding. 

The org.unityencoding.tree.model package contains a tree data structure for representing Unity (JSON) data. 

This package can be used directly, as in the following example.

Create a tree with root and nodes a, b, and c:

    Node root = Nodes.element("root", 
     Nodes.element("a", "some text"),
     Nodes.element("b", "some more text"),
     Nodes.element("c", "some c text")
	);

Print the tree as Unity-encoded JSON:
 
    Nodes.printJson(root);

Which outputs

    ["root",
     ["a", "some text"],
     ["b", "some more text"],
     ["c", "some c text"]
    ]
 
Print the tree as XML:

    Nodes.printXML(root);
 
Which outputs

    <?xml version="1.0" encoding="UTF-8"?>
    <root>
      <a>some text</a>
      <b>some more text</b>
      <c>some c text</c>
     </root>



## Parsing Unity-encoded Inputs with Antlr4

Antlr 4 is used to provide a Unity encoding parser. The grammar is here:

https://github.com/buttermilk-crypto/unity/blob/master/unity-encoding/src/main/antlr4/org/unityencoding/antlr/toolkit/Unity.g4

this is simply a reduced JSON grammar based on 

https://github.com/antlr/grammars-v4/blob/master/json/JSON.g4

Here's an example of using the Antlr 4 parser in unrolled form:

    try (InputStream in = this.getClass().getResourceAsStream(
					"/data/unity/books.json");
			) {
			
			   // boilerplate
				ANTLRInputStream cStream = new ANTLRInputStream(in);
				UnityLexer lexer = new UnityLexer(cStream);
				CommonTokenStream tokens = new CommonTokenStream(lexer);
				UnityParser p = new UnityParser(tokens);
				UnityParser.JsonContext tree = p.json();
				
				// our listener
            UnityAntlr4Listener l = new UnityAntlr4Listener();
            ParseTreeWalker.DEFAULT.walk(l, tree);
			 
			   // get the root node from what has been parsed
				TreeNode<Payload> r = l.getRoot();
				
			} catch (IOException x) {
				x.printStackTrace();
			}

The class UnityAntlr4Listener uses the tree model described above to build a tree from what has been parsed.


## Utilities Leveraging Jackson

The org.unityencoding.jackson.toolkit package contains classes to:

  - Transform Unity-encoded JSON into XML
  - Transform XML into Unity-encoded JSON, using SAX
  - Validate Unity-encoded input
  
  
Example: Transform Unity-encoded JSON into XML
  
    try (
			InputStream in = this.getClass().getResourceAsStream("/data/unity/books.json");
			InputStreamReader r =	new InputStreamReader(in);
		){
			UnityToXMLWithJackson jx = new UnityToXMLWithJackson(r);
			jx.walk();
			System.out.println(jx.pretty());
		}catch(IOException x){
			x.printStackTrace();
		}

Example: Transform XML into Unity-encoded JSON, using SAX/Jackson

     InputStream in = this.getClass().getResourceAsStream("/data/xml/books.xml");
     InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
	  XMLToUnityWithSAX converter = new XMLToUnityWithSAX(reader);
	  converter.setKeepIgnorableWhitespace(false);
	  converter.setTrim(true);
	  converter.setPrettyPrint(true);
	  String output = converter.parse();
	  System.out.println(output);
	 
Example: Validate Unity-encoded JSON, leveraging Jackson

    try (
			InputStream in = this.getClass().getResourceAsStream("/data/unity/books.json");
			InputStreamReader r =	new InputStreamReader(in);
		){
			ValidateUnityWithJackson validator = new ValidateUnityWithJackson(r);
			boolean ok = validator.validate();
			Assert.assertTrue(ok);
		}catch(IOException x){
			Assert.fail();
		}



  