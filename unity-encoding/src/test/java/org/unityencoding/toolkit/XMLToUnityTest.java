package org.unityencoding.toolkit;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.unityencoding.jackson.toolkit.XMLToUnityWithSAX;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class XMLToUnityTest {

	@Test
	public void test0() {
		InputStream in = this.getClass().getResourceAsStream("/data/xml/simple.xml");
		InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
		XMLToUnityWithSAX converter = new XMLToUnityWithSAX(reader);
		converter.setKeepIgnorableWhitespace(true);
		converter.setPrettyPrint(true);
		String output = converter.parse();
		System.err.println(output);
	}
	
	@Test
	public void test1() {
		InputStream in = this.getClass().getResourceAsStream("/data/xml/books.xml");
		InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
		XMLToUnityWithSAX converter = new XMLToUnityWithSAX(reader);
		converter.setKeepIgnorableWhitespace(false);
		converter.setTrim(true);
		converter.setPrettyPrint(true);
		String output = converter.parse();
		System.err.println(output);
	}
	
	@Test
	public void test2() {
		InputStream in = this.getClass().getResourceAsStream("/data/unity/simple.json");
		JsonFactory factory = new JsonFactory();
		try {
		JsonParser parser = factory.createParser(in);
		JsonToken tok = null;
			while ( (tok = parser.nextToken()) != null) {
				System.err.println(tok);
			}
		}catch(Exception x){
			x.printStackTrace();
		}
	}
	
	@Test
	public void test3() {
		InputStream in = this.getClass().getResourceAsStream("/data/relaxng/addressBook.xml");
		InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
		XMLToUnityWithSAX converter = new XMLToUnityWithSAX(reader);
		converter.setKeepIgnorableWhitespace(false);
		converter.setPrettyPrint(false);
		String output = converter.parse();
		System.err.println(output);
	}

}
