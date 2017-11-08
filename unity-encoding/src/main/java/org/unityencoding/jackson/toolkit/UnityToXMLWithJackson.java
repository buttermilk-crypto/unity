package org.unityencoding.jackson.toolkit;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>Utility class to convert JSON constrained using the Unity rules into XML. </p>
 * 
 * <p>Use Jackson ObjectMapper to generate a java object binding, and then walk it
 * to generate the XML. Finally pretty print the resulting xml using the classical 
 * transformer approach. The pretty printing so far is not especially good. </p>
 * 
 * @author Dave
 *
 */

@SuppressWarnings({ "rawtypes" })
public class UnityToXMLWithJackson {

	StringBuilder builder;
	Reader reader;
	String problem;

	public UnityToXMLWithJackson(Reader reader) {
		this.reader = reader;
		builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	}

	public void walk() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ArrayList rootList = (ArrayList) mapper.readValue(reader,
					List.class);
			String retVal = walk((ArrayList) rootList);
			if (retVal.length() > 0) {
				builder.append("</" + retVal + ">");
			}
			builder.append("\n");
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	private String walk(ArrayList list) {
		int index = 0;
		int size = list.size();
		String currentElement = "";

		for (Object item : list) {

			if (item instanceof ArrayList) {
				String retVal = walk((ArrayList<?>) item);
				if (retVal.length() > 0) {
					builder.append("</" + retVal + ">");
				}
			} else {

				if (index == 0) {
					currentElement = (String) item;
				}

				if (index == 0 && list.size() == 1) {
					builder.append("<" + currentElement + "/>");
					return "";
				} else if (index == 0 && list.size() > 1) {
					// lookahead for map
					if (list.get(1) instanceof Map) {
						builder.append("<" + currentElement);
					} else {
						builder.append("<" + currentElement + ">");
					}
				}

				if (index == 1 && item instanceof Map) {
					Map map = (Map) item;
					for (Object entry : map.entrySet()) {
						Map.Entry e = (Map.Entry) entry;
						String key = (String) e.getKey();
						String value = (String) e.getValue();
						builder.append(" ");
						builder.append(key);
						builder.append("=");
						builder.append("\"" + value + "\"");
					}
					if (index == size - 1) {
						// end so close off
						builder.append("/>");
						return "";
					} else {
						builder.append(">");
					}
				}

				if (index >= 1 && (!(item instanceof List))
						&& (!(item instanceof Map))) {
					builder.append(item);
					continue;
				}

			}
			index++;
		}
		return currentElement;
	}

	public StringBuilder getBuilder() {
		return builder;
	}

	public String pretty() {
		try {
			return new Pretty(getBuilder().toString()).output();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

/**
 * implement pretty printing on the XML output from our builder.
 * 
 * @author Dave
 *
 */
class Pretty {

	Document doc;

	public Pretty(String in) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(new InputSource(new StringReader(in)));

		// attempts to clean up whitespace
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList nodeList = (NodeList) xPath.evaluate(
				"//text()[normalize-space()='']", doc, XPathConstants.NODESET);

		for (int i = 0; i < nodeList.getLength(); ++i) {
			Node node = nodeList.item(i);
			node.getParentNode().removeChild(node);
		}

	}

	public String output() throws Exception {
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");
		Writer out = new StringWriter();
		tf.transform(new DOMSource(doc), new StreamResult(out));
		return out.toString();
	}
}
