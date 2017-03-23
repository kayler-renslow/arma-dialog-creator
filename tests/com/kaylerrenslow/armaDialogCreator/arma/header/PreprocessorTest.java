package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.arma.header.Preprocessor.DefinedValueWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 @since 03/22/2017 */
public class PreprocessorTest {
	private static HashMap<String, DefinedValueWrapper> map(@NotNull String[] toMatch, @NotNull String[] replace) {
		if (toMatch.length != replace.length) {
			throw new IllegalArgumentException();
		}
		HashMap<String, DefinedValueWrapper> map = new HashMap<>(toMatch.length);
		int i = 0;
		for (String key : toMatch) {
			map.put(key, new DefinedValueWrapper(new DefineMacroContent.StringDefineValue(replace[i])));
			i++;
		}
		return map;
	}

	private static HashMap<String, DefinedValueWrapper> mapParams(@NotNull String macroKey, @NotNull String[] params, @NotNull String macroValue) {
		HashMap<String, DefinedValueWrapper> map = new HashMap<>(1);
		map.put(macroKey, new DefinedValueWrapper(new DefineMacroContent.ParameterDefineValue(params, macroValue)));
		return map;
	}

	@NotNull
	private static Preprocessor getPreprocessor(@Nullable HashMap<String, DefinedValueWrapper> toInsert) {
		Preprocessor p = new Preprocessor(new File(""), new HeaderParserContext(), (a, b, c) -> {
		});
		if (toInsert != null) {
			p.defined.putAll(toInsert);
		}
		return p;
	}

	@Test
	public void replace() throws Exception {
		String f = "%s = 1 + %s + %s";
		String[] toMatch = {"ARG1", "ARG2", "ARG3"};
		String[] replace = {"a", "b", "c"};

		String base = String.format(f, toMatch[0], toMatch[1], toMatch[2]);
		String expect = String.format(f, replace[0], replace[1], replace[2]);

		Preprocessor p = getPreprocessor(map(toMatch, replace));
		assertEquals(expect, p.replace(base));
	}

	@Test
	public void replace2() throws Exception {
		String f = "%s = 1 + %s + %s";
		String[] toMatch = {"ARG", "ARG##2", "ARG3"};
		String[] replace = {"a", "b", "c"};

		String base = String.format(f, toMatch[0], toMatch[1], toMatch[2]);
		String expect = String.format(f, replace[0], replace[0] + "2", replace[2]);

		Preprocessor p = getPreprocessor(map(toMatch, replace));
		assertEquals(expect, p.replace(base));
	}

	@Test
	public void replace3() throws Exception {
		String[] toMatch = {"e", "oo"};
		String[] replace = {"a", "a"};

		String base = "The cow jumped over the moon!";
		String expect = "The cow jumped over the moon!";

		Preprocessor p = getPreprocessor(map(toMatch, replace));
		assertEquals(expect, p.replace(base));
	}


	@Test
	public void replaceParameter() throws Exception {
		//#define N(NUMBER) number NUMBER
		String base = "Hello N(0).";
		String expect = base; //can't insert before . without ##

		Preprocessor p = getPreprocessor(mapParams("N", new String[]{"NUMBER"}, "number NUMBER"));
		assertEquals(expect, p.replace(base));
	}

	@Test
	public void replaceParameter2() throws Exception {
		//#define N(NUMBER) number NUMBER
		String base = "Hello N(0)";
		String expect = "Hello number 0";

		Preprocessor p = getPreprocessor(mapParams("N", new String[]{"NUMBER"}, "number NUMBER"));
		assertEquals(expect, p.replace(base));
	}

	@Test
	public void replaceParameter3() throws Exception {
		//#define N(NUMBER) number NUMBER
		String base = "Hello N(0)##.";
		String expect = "Hello number 0.";

		Preprocessor p = getPreprocessor(mapParams("N", new String[]{"NUMBER"}, "number NUMBER"));
		assertEquals(expect, p.replace(base));
	}

}