package org.unityencoding.jackson.toolkit;

import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Read XML input from reader using SAX and write the conversion out as JSONXF. Thread safe class.
 * 
 * @author Dave
 *
 */
public class XMLToUnityWithSAX {

	final Reader reader;
	final Lock lock;
	boolean keepIgnorableWhitespace;
	boolean prettyPrint;
	boolean trim;

	public XMLToUnityWithSAX(Reader reader) {
		this.reader = reader;
		lock = new ReentrantLock();
	}

	public String parse() {
		Writer writer = new StringWriter();
		parse(writer);
		String s = writer.toString();
		if(prettyPrint){
			try {
		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(s, Object.class);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			}catch(Exception x){
				x.printStackTrace();
				return s;
			}
		}else{
			return s;
		}
	}

	/**
	 * Parse the XML with a SAX parser and write the JSON encoding to the given writer.
	 * 
	 * @param writer
	 */
	public void parse(Writer writer) {
		lock.lock();
		try {
			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
			SAXParser parser = parserFactor.newSAXParser();
			TransformingSAXHandler handler = new TransformingSAXHandler(writer);
			handler.setKeepIgnorableWhitespace(keepIgnorableWhitespace);
			handler.setPrettyPrint(prettyPrint);
			handler.setTrim(trim);
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			parser.parse(is, handler);
		} catch (Exception x) {
			throw new RuntimeException(x);
		} finally {
			lock.unlock();
		}
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
