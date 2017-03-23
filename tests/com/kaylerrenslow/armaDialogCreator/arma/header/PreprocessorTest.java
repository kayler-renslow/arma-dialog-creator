package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 @since 03/22/2017 */
public class PreprocessorTest {
	private static HashMap<String, DefineMacroContent.DefineValue> map(@NotNull String[] toMatch, @NotNull String[] replace) {
		if (toMatch.length != replace.length) {
			throw new IllegalArgumentException();
		}
		HashMap<String, DefineMacroContent.DefineValue> map = new HashMap<>(toMatch.length);
		int i = 0;
		for (String key : toMatch) {
			map.put(key, new DefineMacroContent.StringDefineValue(replace[i]));
			i++;
		}
		return map;
	}

	private static HashMap<String, DefineMacroContent.DefineValue> mapParams(@NotNull String macroKey, @NotNull String[] params, @NotNull String macroValue) {
		HashMap<String, DefineMacroContent.DefineValue> map = new HashMap<>(1);
		map.put(macroKey, new DefineMacroContent.ParameterDefineValue(params, macroValue));
		return map;
	}

	@Test
	public void replace() throws Exception {
		String f = "%s = 1 + %s + %s";
		String[] toMatch = {"ARG1", "ARG2", "ARG3"};
		String[] replace = {"a", "b", "c"};
		String base = String.format(f, toMatch[0], toMatch[1], toMatch[2]);

		String expect = String.format(f, replace[0], replace[1], replace[2]);

		assertEquals(expect, Preprocessor.replace(base, map(toMatch, replace)));
	}

	@Test
	public void replace2() throws Exception {
		String f = "%s = 1 + %s + %s";
		String[] toMatch = {"ARG", "ARG##2", "ARG3"};
		String[] replace = {"a", "b", "c"};
		String base = String.format(f, toMatch[0], toMatch[1], toMatch[2]);

		String expect = String.format(f, replace[0], replace[0] + "2", replace[2]);

		assertEquals(expect, Preprocessor.replace(base, map(toMatch, replace)));
	}

	@Test
	public void replace3() throws Exception {
		String[] toMatch = {"e", "oo"};
		String[] replace = {"a", "a"};
		String base = "The cow jumped over the moon!";

		String expect = "The cow jumped over the moon!";

		assertEquals(expect, Preprocessor.replace(base, map(toMatch, replace)));
	}


	@Test
	public void replaceParameter() throws Exception {
		String base = "Hello N(0).";
		String expect = base; //can't insert before . without ##

		//#define N(NUMBER) number NUMBER
		assertEquals(expect, Preprocessor.replace(base, mapParams("N", new String[]{"NUMBER"}, "number NUMBER")));
	}

	@Test
	public void replaceParameter2() throws Exception {
		String base = "Hello N(0)";
		String expect = "Hello number 0";

		//#define N(NUMBER) number NUMBER
		assertEquals(expect, Preprocessor.replace(base, mapParams("N", new String[]{"NUMBER"}, "number NUMBER")));
	}

	@Test
	public void replaceParameter3() throws Exception {
		String base = "Hello N(0)##.";
		String expect = "Hello number 0.";

		//#define N(NUMBER) number NUMBER
		assertEquals(expect, Preprocessor.replace(base, mapParams("N", new String[]{"NUMBER"}, "number NUMBER")));
	}

}