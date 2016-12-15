package com.kaylerrenslow.armaDialogCreator.data.io.xml;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.*;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.impl.StringTableValueImpl;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
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
		return new DefaultStringTableXmlParser(new File("./tests/com/kaylerrenslow/armaDialogCreator/data/io/xml/stringtable.xml"));
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
		testKeys[0].setId("error");
		DefaultStringTableXmlParser parser = getParser();
		StringTable tableInstance = parser.createStringTableInstance();

		testTable(testKeys, tableInstance, true);
	}

	@Test
	public void createStringTableInstanceExpectErrorContainer() throws Exception {
		TestStringTableKey[] testKeys = getTestKeys();
		testKeys[0].setContainerName("error");
		DefaultStringTableXmlParser parser = getParser();
		StringTable tableInstance = parser.createStringTableInstance();

		testTable(testKeys, tableInstance, true);
	}

	@Test
	public void createStringTableInstanceExpectErrorPackage() throws Exception {
		TestStringTableKey[] testKeys = getTestKeys();
		testKeys[0].setPackageName("error");
		DefaultStringTableXmlParser parser = getParser();
		StringTable tableInstance = parser.createStringTableInstance();

		testTable(testKeys, tableInstance, true);
	}

	@Test
	public void createStringTableInstanceExpectErrorValue() throws Exception {
		TestStringTableKey[] testKeys = getTestKeys();
		testKeys[0].setValue(testKeys[1].getValue());
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

	private static class TestStringTableKey implements StringTableKey {
		private String id;
		private StringTableValue value;
		private ValueObserver<String> containerNameObserver = new ValueObserver<>(null);
		private ValueObserver<String> packageNameObserver = new ValueObserver<>(null);

		public TestStringTableKey(String id, String packageName, String containerName, Language[] langs, String[] vals) {
			this.id = id;
			int i = 0;
			ObservableMap<Language, String> map = FXCollections.observableMap(new HashMap<>());
			value = new StringTableValueImpl(this, map);
			for (Language language : langs) {
				map.put(language, vals[i++]);
			}
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setPackageName(String packageName) {
			packageNameObserver().updateValue(packageName);
		}

		public void setContainerName(String containerName) {
			containerNameObserver().updateValue(containerName);
		}

		public void setValue(StringTableValue value) {
			this.value = value;
		}

		@Override
		@NotNull
		public String getId() {
			return id;
		}

		@Override
		@NotNull
		public StringTableValue getValue() {
			return value;
		}


		@NotNull
		@Override
		public ValueObserver<String> packageNameObserver() {
			return packageNameObserver;
		}

		@NotNull
		@Override
		public ValueObserver<String> containerNameObserver() {
			return containerNameObserver;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof StringTableKey) {
				StringTableKey other = (StringTableKey) o;
				return equalsKey(other);
			}
			return false;
		}
	}

}