package org.unityencoding.toolkit;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Test;
import org.unityencoding.jackson.toolkit.TransformingSAXHandler;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class ParseTest {
	
	@Test
	public void testSimple() {
		
		try {
		  SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		  SAXParser parser = parserFactor.newSAXParser();
		  Writer w = new StringWriter();
		  TransformingSAXHandler handler = new TransformingSAXHandler(w);
		  parser.parse(handler.getClass().getResourceAsStream("/data/xml/simple.xml"), handler);
		  String output = w.toString();
		//  ObjectMapper mapper = new ObjectMapper();
		// Object o = mapper.readValue(new StringReader(output), Object.class);
		 System.err.println(output);
		  
		}catch(Exception x){
			x.printStackTrace();
		}
	}

	@Test
	public void test0() {
		
		try {
		  SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		  SAXParser parser = parserFactor.newSAXParser();
		  Writer w = new StringWriter();
		  TransformingSAXHandler handler = new TransformingSAXHandler(w);
		  parser.parse(handler.getClass().getResourceAsStream("/data/xml/books.xml"), handler);
		  System.err.println(w.toString());
		}catch(Exception x){
			x.printStackTrace();
		}
	}
	
	@Test
    public void test1() {
		
		try {
		  SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		  SAXParser parser = parserFactor.newSAXParser();
		  Writer w = new StringWriter();
		  TransformingSAXHandler handler = new TransformingSAXHandler(w);
		  parser.parse(handler.getClass().getResourceAsStream("/data/xml/soap.xml"), handler);
		  System.err.println(w.toString());
		}catch(Exception x){
			x.printStackTrace();
		}
	}

}

class SAXHandler extends DefaultHandler {
	
	StringBuffer buf;
	JsonFactory f;
	JsonGenerator g;
	
	public SAXHandler(Writer writer){
		buf = new StringBuffer();
		f = new JsonFactory();
		try {
			g = f.createGenerator(writer);
			// g.useDefaultPrettyPrinter();
		}catch(IOException x){
			x.printStackTrace();
		}
	}
	
	public InputSource resolveEntity(String paramString1, String paramString2)
			throws IOException, SAXException{
		//	System.out.println("resolve: "+paramString1+", "+paramString2);
		    return null;
		  }

		  public void notationDecl(String paramString1, String paramString2, String paramString3)
		    throws SAXException{
		//	  System.out.println("notationDecl: "+paramString1+", "+paramString2+", "+paramString3);
		  }

		  public void unparsedEntityDecl(String paramString1, String paramString2, String paramString3, String paramString4)
		    throws SAXException {
		//	  System.out.println("unparsedEntityDecl: "+paramString1+", "+paramString2+", "+paramString3+", "+paramString4);
		  }

		  public void setDocumentLocator(Locator paramLocator) {
			 
		  }

		  public void startDocument() throws SAXException {
			
		  }

		  public void endDocument() throws SAXException {
			 try {
				g.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }

		  public void startPrefixMapping(String paramString1, String paramString2) throws SAXException {
	//			System.out.println("startPrefixMapping: "+paramString1+", "+paramString2);
		  }

		  public void endPrefixMapping(String paramString) throws SAXException {
		//		System.out.println("endPrefixMapping: "+paramString);
		  }

		  public void startElement(String paramString1, 
				  String paramString2, 
				  String paramString3, 
				  Attributes paramAttributes)
		    throws SAXException{
			
			  try {
					g.writeStartArray();
					g.writeString(paramString3); //element name
					
					if(paramAttributes != null && paramAttributes.getLength() >0){
						g.writeStartObject();
						int length = paramAttributes.getLength();
						for(int i = 0;i<length;i++){
							g.writeObjectField(paramAttributes.getQName(i), paramAttributes.getValue(i));
						}
						g.writeEndObject();
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  
		  }

		  public void endElement(String paramString1, String paramString2, String paramString3)
		    throws SAXException {
			 try {
				 
				 String s = buf.toString().trim();
				 if(s.length() > 0) {
					 g.writeString(s);
				 }
				 
				g.writeEndArray();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
				buf = new StringBuffer(); 
			 
		  }

		  public void characters(char[] array, int start, int count)
		    throws SAXException {
			  String s = new String(array,start,count);
			  buf.append(s);
		  }

		  public void ignorableWhitespace(char[] array, int start, int count)
		    throws SAXException {
			//  String s = new String(array,start,count);
			//  buf.append(s);
		  }

		  public void processingInstruction(String paramString1, String paramString2)
		    throws SAXException{
			 System.out.println("processing instruction: "+paramString1+", "+paramString2);
		  }

		  public void skippedEntity(String paramString) throws SAXException {
		  System.out.println("skipped entity:"+paramString);
		  }

		  public void warning(SAXParseException paramSAXParseException) throws SAXException  {
			  System.err.println(paramSAXParseException.toString());
		  }

		  public void error(SAXParseException paramSAXParseException) throws SAXException {
			  System.err.println(paramSAXParseException.toString());
		  }

		  public void fatalError(SAXParseException paramSAXParseException)
		    throws SAXException
		  {
		    throw paramSAXParseException;
		  }
}
