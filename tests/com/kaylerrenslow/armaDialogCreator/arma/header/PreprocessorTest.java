package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.arma.header.DefineMacroContent.DefineValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;

/**
 @author Kayler
 @since 03/22/2017 */
public class PreprocessorTest {
	private static <T> T[] array(T... ts) {
		return ts;
	}

	private static HashMap<String, DefineValue> map(@NotNull String[] toMatch, @NotNull String[] replace) {
		if (toMatch.length != replace.length) {
			throw new IllegalArgumentException();
		}
		HashMap<String, DefineValue> map = new HashMap<>(toMatch.length);
		int i = 0;
		for (String key : toMatch) {
			map.put(key, new DefineMacroContent.StringDefineValue(replace[i]));
			i++;
		}
		return map;
	}

	private static HashMap<String, DefineValue> mapParams(@NotNull String macroKey, @NotNull String[] params, @NotNull String macroValue) {
		HashMap<String, DefineValue> map = new HashMap<>(1);
		map.put(macroKey, new DefineMacroContent.ParameterDefineValue(params, macroValue));
		return map;
	}

	@NotNull
	@SafeVarargs
	private static HashMap<String, DefineValue> mergeIntoFirst(@NotNull HashMap<String, DefineValue> first, @NotNull HashMap<String, DefineValue>... others) {
		for (HashMap<String, DefineValue> map : others) {
			first.putAll(map);
		}
		return first;
	}

	@NotNull
	private static Preprocessor getPreprocessor(@Nullable HashMap<String, DefineValue> toInsert) {
		return getPreprocessor(new File(""), toInsert);
	}

	@NotNull
	private static Preprocessor getPreprocessor(@Nullable File processFile, @Nullable HashMap<String, DefineValue> toInsert) {
		return getPreprocessor(processFile, toInsert, (a, c) -> {
		});
	}

	@NotNull
	private static Preprocessor getPreprocessor(@Nullable File processFile, @Nullable HashMap<String, DefineValue> toInsert, @NotNull PreprocessCallback callback) {
		Preprocessor p = new Preprocessor(processFile, new HeaderParserContext(), callback);
		if (toInsert != null) {
			p.defined.putAll(toInsert);
		}
		p.preprocessStack.add(new Preprocessor.PreprocessState(processFile));
		return p;
	}

	private void assertPreprocessLine(String base, String expect, Preprocessor p) throws HeaderParseException {
		StringBuilder actual = new StringBuilder();
		p.preprocessLine(base, new Preprocessor.StringBuilderReference(actual));
		assertEquals(expect, actual.toString());
	}

	@Test
	public void replace() throws Exception {
		String base = "ARG = 1 + ARG2 + ARG3";
		String expect = "a = 1 + b + c";

		Preprocessor p = getPreprocessor(map(
				array("ARG", "ARG2", "ARG3"),
				array("a", "b", "c")
		));
		assertPreprocessLine(base, expect, p);
	}

	@Test
	public void replace2() throws Exception {
		String base = "ARG = 1 + ARG##2 + ARG3";
		String expect = "a = 1 + a2 + c";

		Preprocessor p = getPreprocessor(map(
				array("ARG", "ARG2", "ARG3"),
				array("a", "b", "c")
		));
		assertPreprocessLine(base, expect, p);
	}

	@Test
	public void replace3() throws Exception {
		String[] toMatch = {"e", "oo"};
		String[] replace = {"a", "a"};

		String base = "The cow jumped over the moon!";
		String expect = "The cow jumped over the moon!";

		Preprocessor p = getPreprocessor(map(toMatch, replace));
		assertPreprocessLine(base, expect, p);
	}


	@Test
	public void replaceParameter() throws Exception {
		//#define N(NUMBER) number NUMBER
		String base = "Hello N(0)word";
		String expect = base; //can't insert before word without ##

		Preprocessor p = getPreprocessor(mapParams("N", new String[]{"NUMBER"}, "number NUMBER"));
		assertPreprocessLine(base, expect, p);
	}

	@Test
	public void replaceParameter2() throws Exception {
		//#define N(NUMBER) number NUMBER
		String base = "Hello N(0)";
		String expect = "Hello number 0";

		Preprocessor p = getPreprocessor(mapParams("N", new String[]{"NUMBER"}, "number NUMBER"));
		assertPreprocessLine(base, expect, p);
	}

	@Test
	public void replaceParameter3() throws Exception {
		//#define N(NUMBER) number NUMBER
		String base = "Hello N(0)##word";
		String expect = "Hello number 0word";

		Preprocessor p = getPreprocessor(mapParams("N", new String[]{"NUMBER"}, "number NUMBER"));
		assertPreprocessLine(base, expect, p);
	}

	@Test
	public void replaceParameter4() throws Exception {
		//#define BLASTOFF(UNIT,RATE) UNIT setVelocity [0,0,RATE];
		String base = "disableSerialization; BLASTOFF(car,5)";
		String expect = "disableSerialization; car setVelocity [0,0,5];";

		Preprocessor p = getPreprocessor(mapParams("BLASTOFF", new String[]{"UNIT", "RATE"}, "UNIT setVelocity [0,0,RATE];"));
		assertPreprocessLine(base, expect, p);
	}


	@Test
	public void replaceParameterGlue() throws Exception {
		//#define GLUE(g1,g2) g1##g2
		String base = "GLUE(123,456)";
		String expect = "123456";

		Preprocessor p = getPreprocessor(mapParams("GLUE", new String[]{"g1", "g2"}, "g1##g2"));
		assertPreprocessLine(base, expect, p);
	}

	@Test
	public void replaceParameterGlue2() throws Exception {
		//#define GLUE(g1,g2) g1##g2
		String base = "model = \\OFP2\\Structures\\Various\\##FOLDER##\\##FOLDER;";
		String expect = "model = \\OFP2\\Structures\\Various\\myFolder\\myFolder;";

		Preprocessor p = getPreprocessor(map(array("FOLDER"), array("myFolder")));
		assertPreprocessLine(base, expect, p);
	}

	@Test
	public void replaceParameterQuote() throws Exception {
		/*
		* #define STRINGIFY(s) #s;
		* #define FOO 456
		* test1 = STRINGIFY(123); //test1 = "123";
		* test2 = STRINGIFY(FOO); //test2 = "456";
		*/
		String base = "test1 = STRINGIFY(123);test2 = STRINGIFY(FOO);";
		String expect = "test1 = \"123\";test2 = \"456\";";

		Preprocessor p = getPreprocessor(
				mergeIntoFirst(
						map(array("FOO"), array("456")),
						mapParams("STRINGIFY", array("s"), "#s")
				)
		);
		assertPreprocessLine(base, expect, p);
	}

	@Test
	public void replaceParameterQuote2() throws Exception {
		/*
		* #define STRINGIFY(s) #s;
		*/
		String base = "STRINGIFY(quote)";
		String expect = "\"quote\"";

		Preprocessor p = getPreprocessor(
				mapParams("STRINGIFY", array("s"), "#s")
		);
		assertPreprocessLine(base, expect, p);
	}

	@Test
	public void replaceParameterQuote3() throws Exception {
		/*
		* #define STRINGIFY(s) #s;
		*/
		String base = "STRINGIFY(quote);";
		String expect = "\"quote\";";

		Preprocessor p = getPreprocessor(
				mapParams("STRINGIFY", array("s"), "#s")
		);
		assertPreprocessLine(base, expect, p);
	}

	@Test
	public void replaceParameterQuote4() throws Exception {
		/*
		* #define STRINGIFY(s) #s;
		*/
		String base = "STRINGIFY(quote)fail";
		String expect = base;

		Preprocessor p = getPreprocessor(
				mapParams("STRINGIFY", array("s"), "#s")
		);
		assertPreprocessLine(base, expect, p);
	}

	@Test
	public void fullTest() throws Exception {
		FileInputStream fis = new FileInputStream(HeaderTestUtil.getFile("preprocessTestExpected.h"));
		byte[] arr = new byte[fis.available()];
		int read = fis.read(arr);
		fis.close();

		String expected = new String(arr).replaceAll("\r\n", "\n");

		Preprocessor p = getPreprocessor(HeaderTestUtil.getFile("preprocessTest.h"), null, new PreprocessCallback() {
			@Override
			public void fileProcessed(@NotNull File file, @NotNull Preprocessor.PreprocessorInputStream fileContentStream) throws HeaderParseException {
				byte[] b = new byte[fileContentStream.available()];
				fileContentStream.read(b);
				assertEquals(expected, new String(b));
			}
		});
		p.run();

	}

}