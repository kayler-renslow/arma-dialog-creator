package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 @since 03/22/2017 */
public class PreprocessorTest {
	@Test
	public void replace() throws Exception {
		String f = "%s = 1 + %s + %s";
		String[] toMatch = {"ARG1", "ARG2", "ARG3"};
		String[] replace = {"a", "b", "c"};
		String base = String.format(f, toMatch[0], toMatch[1], toMatch[2]);

		String expect = String.format(f, replace[0], replace[1], replace[2]);

		assertEquals(expect, Preprocessor.replace(base, toMatch, replace));
	}

	@Test
	public void replace2() throws Exception {
		String f = "%s = 1 + %s + %s";
		String[] toMatch = {"ARG", "ARG2", "ARG3"};
		String[] replace = {"a", "b", "c"};
		String base = String.format(f, toMatch[0], toMatch[1], toMatch[2]);

		String expect = String.format(f, replace[0], replace[0] + "2", replace[0] + "3");

		assertEquals(expect, Preprocessor.replace(base, toMatch, replace));
	}

	@Test
	public void replace3() throws Exception {
		String[] toMatch = {"e", "oo"};
		String[] replace = {"a", "a"};
		String base = "The cow jumped over the moon!";

		String expect = "Tha cow jumpad ovar tha man!";

		assertEquals(expect, Preprocessor.replace(base, toMatch, replace));
	}

}