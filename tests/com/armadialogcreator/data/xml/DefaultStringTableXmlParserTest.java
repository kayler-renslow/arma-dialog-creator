package com.armadialogcreator.data.xml;

import com.armadialogcreator.core.stringtable.*;
import com.armadialogcreator.data.DefaultStringTableXmlParser;
import com.armadialogcreator.data.SimpleStringTableKey;
import com.armadialogcreator.util.MapObserver;
import com.armadialogcreator.util.XmlParseException;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 @author Kayler
 @since 12/12/2016 */
public class DefaultStringTableXmlParserTest {

	private static TestStringTableKey[] getTestKeys() {
		TestStringTableKey k1 = new TestStringTableKey(
				"str_myTag_another_key",
				null, null,
				new KnownLanguage[]{
						KnownLanguage.Original
				},
				new String[]{
						"another key"
				}
		);
		TestStringTableKey k2 = new TestStringTableKey(
				"str_myTag_inPackage",
				"Mission One", null,
				new KnownLanguage[]{
						KnownLanguage.Original
				},
				new String[]{
						"inside package"
				}
		);
		TestStringTableKey k3 = new TestStringTableKey(
				"str_myTag_Yes",
				"Mission One", "Some Words",
				new KnownLanguage[]{
						KnownLanguage.Original,
						KnownLanguage.English,
						KnownLanguage.Czech
				},
				new String[]{
						"yes",
						"yes",
						"ano"
				}
		);
		TestStringTableKey k4 = new TestStringTableKey(
				"str_myTag_No",
				"Mission One", "Some Words",
				new KnownLanguage[]{
						KnownLanguage.Original,
				},
				new String[]{
						"no"
				}
		);
		TestStringTableKey k5 = new TestStringTableKey(
				"str_myTag_formatted",
				"Mission One", "Another Container",
				new KnownLanguage[]{
						KnownLanguage.Original,
				},
				new String[]{
						"Hello, %1."
				}
		);

		return new TestStringTableKey[]{k1, k2, k3, k4, k5};
	}

	@NotNull
	private static DefaultStringTableXmlParser getParser() throws XmlParseException {
		return new DefaultStringTableXmlParser(new File("./tests/com/armadialogcreator/data/xml/stringtable.xml"));
	}

	@Test
	public void createStringTableInstance() throws Exception {
		TestStringTableKey[] testKeys = getTestKeys();
		DefaultStringTableXmlParser parser = getParser();
		StringTable tableInstance = parser.createStringTableInstance();

		testTable(testKeys, tableInstance, false);
	}

	@Test
	public void createStringTableInstanceExpectErrorId() throws Exception {
		TestStringTableKey[] testKeys = getTestKeys();
		try {
			testKeys[0].setId("error");
			org.junit.Assert.assertEquals("Didn't throw an exception", true, false);
		} catch (IllegalArgumentException e) {
			org.junit.Assert.assertEquals(true, true);
		}
	}

	@Test
	public void createStringTableInstanceExpectErrorContainer() throws Exception {
		TestStringTableKey[] testKeys = getTestKeys();
		testKeys[0].getPath().getContainers().add("error");
		DefaultStringTableXmlParser parser = getParser();
		StringTable tableInstance = parser.createStringTableInstance();

		testTable(testKeys, tableInstance, true);
	}

	@Test
	public void createStringTableInstanceExpectErrorPackage() throws Exception {
		TestStringTableKey[] testKeys = getTestKeys();
		testKeys[0].getPath().setPackageName("error");
		DefaultStringTableXmlParser parser = getParser();
		StringTable tableInstance = parser.createStringTableInstance();

		testTable(testKeys, tableInstance, true);
	}

	@Test
	public void createStringTableInstanceExpectErrorTokens() throws Exception {
		TestStringTableKey[] testKeys = getTestKeys();
		testKeys[0].setTokens(testKeys[1].getLanguageTokenMap());
		DefaultStringTableXmlParser parser = getParser();
		StringTable tableInstance = parser.createStringTableInstance();

		testTable(testKeys, tableInstance, true);
	}

	private void testTable(TestStringTableKey[] testKeys, StringTable tableInstance, boolean expectError) {
		List<StringTableKey> keys = tableInstance.getKeys();
		String errMsg = "";
		boolean hasError = false;
		for (StringTableKey key : keys) {
			boolean matched = false;
			for (TestStringTableKey testKey : testKeys) {
				if (key.equalsKey(testKey)) {
					matched = true;
					break;
				}
			}
			if (!matched) {
				errMsg += String.format("key '%s' doesn't exist\n", key.getId());
				hasError = true;
			}
		}
		org.junit.Assert.assertEquals(errMsg, hasError, expectError);
	}

	private static class TestStringTableKey extends SimpleStringTableKey {
		public TestStringTableKey(String id, String packageName, String containerName, Language[] langs, String[] vals) {
			super(id, new StringTableKeyPath(packageName), getMap(langs, vals));
			if (containerName != null) {
				getPath().getContainers().add(containerName);
			}
		}

		private static MapObserver<Language, String> getMap(Language[] langs, String[] vals) {
			MapObserver<Language, String> map = new MapObserver<>(new HashMap<>());
			int i = 0;
			for (Language language : langs) {
				map.put(language, vals[i++]);
			}
			return map;
		}

		public void setTokens(MapObserver<Language, String> map) {
			this.values = map;
		}

	}

}
