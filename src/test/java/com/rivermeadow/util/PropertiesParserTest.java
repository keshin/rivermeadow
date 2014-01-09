package com.rivermeadow.util;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class PropertiesParserTest {

	private PropertiesParser propertiesParser;

	private Properties props;

	@Before
	public void setUp() {
		props = new Properties();
		propertiesParser = new PropertiesParser(props);
	}
	
	@Test
	public void testParse() throws FileNotFoundException, IOException {
		props.load(PropertiesParserTest.class.getClassLoader().getResourceAsStream("test.properties"));
		propertiesParser.parse();
		
		assertEquals("foo is bar", props.getProperty("baz"));
	}
}


//Properties props = Properties.load('my.properties');
//PropertiesParser parser = new PropertiesParser(props);
//parser.parse();
//assert("foo is bar".equals(props.getProperty("baz"));



//1- syntax errors which caused to compile time error
//2- Not catching Exceptions like FileNotFound and IO
//3- Since this is a util, it is better for it to be static
//4- The RegEx was not being escaped properly so I added Pattern.quote(PATTERN) which returns a literal string replacement

