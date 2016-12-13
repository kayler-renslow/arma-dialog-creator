package com.kaylerrenslow.armaDialogCreator.data.io.xml;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.*;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.impl.StringTableValueImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 @author Kayler
 @since 12/12/2016 */
public class DefaultStringTableXmlParserTest {


	@Test
	public void createStringTableInstance() throws Exception {
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


		TestStringTableKey[] testKeys = {k1, k2, k3, k4, k5};

		DefaultStringTableXmlParser parser = new DefaultStringTableXmlParser(new File("./tests/com/kaylerrenslow/armaDialogCreator/data/io/xml/stringtable.xml"));
		StringTable tableInstance = parser.createStringTableInstance();

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
		org.junit.Assert.assertEquals(errMsg, hasError, false);
	}

	private static class TestStringTableKey implements StringTableKey {
		private final String id;
		private final String packageName;
		private final String containerName;
		private StringTableValue value;

		public TestStringTableKey(String id, String packageName, String containerName, Language[] langs, String[] vals) {
			this.id = id;
			this.packageName = packageName;
			this.containerName = containerName;
			int i = 0;
			Map<Language, String> map = new HashMap<>();
			value = new StringTableValueImpl(this, map);
			for (Language language : langs) {
				map.put(language, vals[i++]);
			}
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

		@Override
		@Nullable
		public String getPackageName() {
			return packageName;
		}

		@Override
		@Nullable
		public String getContainerName() {
			return containerName;
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