package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.arma.header.DefineMacroContent.DefineValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

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
	private static Preprocessor getPreprocessor(@Nullable HashMap<String, DefineValue> toInsert) {
		return getPreprocessor(new File(""), toInsert);
	}

	@NotNull
	private static Preprocessor getPreprocessor(@Nullable File processFile, @Nullable HashMap<String, DefineValue> toInsert) {
		Preprocessor p = new Preprocessor(processFile, new HeaderParserContext(), (a, b, c) -> {
		});
		if (toInsert != null) {
			p.defined.putAll(toInsert);
		}
		p.preprocessStack.add(new Preprocessor.PreprocessState(processFile));
		return p;
	}

	@Test
	public void fullTest() {
		Preprocessor p = getPreprocessor(HeaderTestUtil.getFile("preprocessTest.h"), null);
		//todo, be sure to include #include case
	}

	@Test
	public void replace() throws Exception {
		String base = "ARG = 1 + ARG2 + ARG3";
		String expect = "a = 1 + b + c";

		Preprocessor p = getPreprocessor(map(
				array("ARG", "ARG2", "ARG3"),
				array("a", "b", "c")
		));
		assertEquals(expect, p.preprocessLine(base, fileContent));
	}

	@Test
	public void replace2() throws Exception {
		String base = "ARG = 1 + ARG##2 + ARG3";
		String expect = "a = 1 + a2 + c";

		Preprocessor p = getPreprocessor(map(
				array("ARG", "ARG2", "ARG3"),
				array("a", "b", "c")
		));
		assertEquals(expect, p.preprocessLine(base, fileContent));
	}

	@Test
	public void replace3() throws Exception {
		String[] toMatch = {"e", "oo"};
		String[] replace = {"a", "a"};

		String base = "The cow jumped over the moon!";
		String expect = "The cow jumped over the moon!";

		Preprocessor p = getPreprocessor(map(toMatch, replace));
		assertEquals(expect, p.preprocessLine(base, fileContent));
	}


	@Test
	public void replaceParameter() throws Exception {
		//#define N(NUMBER) number NUMBER
		String base = "Hello N(0).";
		String expect = base; //can't insert before . without ##

		Preprocessor p = getPreprocessor(mapParams("N", new String[]{"NUMBER"}, "number NUMBER"));
		assertEquals(expect, p.preprocessLine(base, fileContent));
	}

	@Test
	public void replaceParameter2() throws Exception {
		//#define N(NUMBER) number NUMBER
		String base = "Hello N(0)";
		String expect = "Hello number 0";

		Preprocessor p = getPreprocessor(mapParams("N", new String[]{"NUMBER"}, "number NUMBER"));
		assertEquals(expect, p.preprocessLine(base, fileContent));
	}

	@Test
	public void replaceParameter3() throws Exception {
		//#define N(NUMBER) number NUMBER
		String base = "Hello N(0)##.";
		String expect = "Hello number 0.";

		Preprocessor p = getPreprocessor(mapParams("N", new String[]{"NUMBER"}, "number NUMBER"));
		assertEquals(expect, p.preprocessLine(base, fileContent));
	}

	@Test
	public void replaceParameterGlue() throws Exception {
		//#define GLUE(g1,g2) g1##g2
		String base = "GLUE(123,456)";
		String expect = "123456";

		Preprocessor p = getPreprocessor(mapParams("GLUE", new String[]{"g1", "g2"}, "g1##g2"));
		assertEquals(expect, p.preprocessLine(base, fileContent));
	}

}