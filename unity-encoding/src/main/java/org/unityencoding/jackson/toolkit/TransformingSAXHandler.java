package org.unityencoding.jackson.toolkit;

import java.io.IOException;
import java.io.Writer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * Extend DefaultHandler to implement JSON output. This uses the JacksonGenerator approach to build up the JSON.
 * 
 * @author Dave
 * @see XMLToUnityWithSAX
 *
 */
public class TransformingSAXHandler extends DefaultHandler {

	StringBuilder buf;
	final JsonFactory f;
	final JsonGenerator g;

	boolean keepIgnorableWhitespace;
	boolean prettyPrint;
	boolean trim;

	public TransformingSAXHandler(Writer writer) {
		buf = new StringBuilder();
		f = new JsonFactory();
		try {
			g = f.createGenerator(writer);
			if(prettyPrint) {
				g.useDefaultPrettyPrinter();
			}
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	public void endDocument() throws SAXException {
		try {
			g.close();
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}

	public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
			throws SAXException {

		try {
			g.writeStartArray();
			g.writeString(paramString3); // element full name

			if (paramAttributes != null && paramAttributes.getLength() > 0) {
				g.writeStartObject();
				int length = paramAttributes.getLength();
				for (int i = 0; i < length; i++) {
					g.writeObjectField(paramAttributes.getQName(i),
							paramAttributes.getValue(i));
				}
				g.writeEndObject();
			}

		} catch (IOException e) {
			throw new SAXException(e);
		}

	}

	public void endElement(String paramString1, String paramString2,
			String paramString3) throws SAXException {
		try {

			String s = buf.toString();
			if(trim) s = s.trim();
			if (s.length() > 0) {
				g.writeString(s);
			}

			g.writeEndArray();

		} catch (IOException e) {
			throw new SAXException(e);
		}

		buf = new StringBuilder();

	}

	public void characters(char[] array, int start, int count)
			throws SAXException {
		buf.append(new String(array, start, count));
	}

	public void ignorableWhitespace(char[] array, int start, int count)
			throws SAXException {
		if (keepIgnorableWhitespace) {
			buf.append(new String(array, start, count));
		}
	}

	public void warning(SAXParseException paramSAXParseException)
			throws SAXException {
		throw paramSAXParseException;
	}

	public void error(SAXParseException paramSAXParseException)
			throws SAXException {
		throw paramSAXParseException;
	}

	public void fatalError(SAXParseException paramSAXParseException)
			throws SAXException {
		throw paramSAXParseException;
	}

	public void setKeepIgnorableWhitespace(boolean keepIgnorableWhitespace) {
		this.keepIgnorableWhitespace = keepIgnorableWhitespace;
	}

	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	public void setTrim(boolean trim) {
		this.trim = trim;
	}

}
