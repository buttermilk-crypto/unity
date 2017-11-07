package org.unityencoding.toolkit;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.Assert;

import org.junit.Test;
import org.unityencoding.jackson.toolkit.UnityToXMLWithJackson;
import org.unityencoding.jackson.toolkit.ValidateUnityWithJackson;


public class JacksonTest {

	@Test
	public void testBook() {
		try (
			InputStream in = this.getClass().getResourceAsStream("/data/jsonxf/books.json");
			InputStreamReader r =	new InputStreamReader(in);
		){
			UnityToXMLWithJackson jx = new UnityToXMLWithJackson(r);
			jx.walk();
			System.err.println(jx.pretty());
		}catch(IOException x){
			x.printStackTrace();
		}
	}
	
	@Test
	public void testValidate() {
		
		for(int i = 0;i<8;i++){
			try (
					InputStream in = this.getClass().getResourceAsStream("/data/jsonxf/simple"+i+".json");
					InputStreamReader r =	new InputStreamReader(in);
				){
					ValidateUnityWithJackson validator = new ValidateUnityWithJackson(r);
					boolean ok = validator.validate();
					Assert.assertTrue(ok);
				}catch(IOException x){
					x.printStackTrace();
					Assert.fail();
				}
		}
	}
	
	@Test
	public void test0() {
		
		for(int i = 0;i<8;i++){
			try (
					InputStream in = this.getClass().getResourceAsStream("/data/jsonxf/simple"+i+".json");
					InputStreamReader r =	new InputStreamReader(in);
				){
					UnityToXMLWithJackson jx = new UnityToXMLWithJackson(r);
					jx.walk();
					System.err.println(jx.pretty());
				}catch(IOException x){
					x.printStackTrace();
				}
		}
	}
	
	@Test
	public void test7() {
			try (
					InputStream in = this.getClass().getResourceAsStream("/data/jsonxf/simple7.json");
					InputStreamReader r =	new InputStreamReader(in);
				){
					UnityToXMLWithJackson jx = new UnityToXMLWithJackson(r);
					jx.walk();
					System.err.println(jx.getBuilder().toString());
				}catch(IOException x){
					x.printStackTrace();
				}
	}
	
	@Test
	public void addrBook() {
		try (
			InputStream in = this.getClass().getResourceAsStream("/data/relaxng/addressBook.json");
			InputStreamReader r =	new InputStreamReader(in);
		){
			UnityToXMLWithJackson jx = new UnityToXMLWithJackson(r);
			jx.walk();
			System.err.println(jx.pretty());
		}catch(IOException x){
			x.printStackTrace();
		}
	}

}
