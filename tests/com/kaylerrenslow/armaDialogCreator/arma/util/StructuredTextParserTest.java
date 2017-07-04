package com.kaylerrenslow.armaDialogCreator.arma.util;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 @since 07/03/2017 */
public class StructuredTextParserTest {

	@Test
	public void parseEmpty() throws Exception {
		StructuredTextParser parser = new StructuredTextParser("");
		assertEquals("", parser.getParsedText());
		ArrayList<TextSection> sections = new ArrayList<>();
		sections.add(new TextSection(TextSection.TagName.Root, "", new HashMap<>()));
		assertEquals(sections, parser.parse());
	}

	@Test
	public void parseRootOnly() throws Exception {
		String parsedText = "hello world";
		StructuredTextParser parser = new StructuredTextParser(parsedText);
		assertEquals(parsedText, parser.getParsedText());
		ArrayList<TextSection> sections = new ArrayList<>();
		sections.add(new TextSection(TextSection.TagName.Root, parsedText, new HashMap<>()));
		assertEquals(sections, parser.parse());
	}

	@Test
	public void parseBr() throws Exception {
		String parsedText = "hello <br />world";
		StructuredTextParser parser = new StructuredTextParser(parsedText);
		assertEquals(parsedText, parser.getParsedText());
		ArrayList<TextSection> sections = new ArrayList<>();
		sections.add(new TextSection(TextSection.TagName.Root, "hello ", new HashMap<>()));
		sections.add(new TextSection(TextSection.TagName.Br, "", new HashMap<>()));
		sections.add(new TextSection(TextSection.TagName.Root, "world", new HashMap<>()));
		assertEquals(sections, parser.parse());
	}

	@Test
	public void parseT() throws Exception {
		String parsedText = "hello <t color='#ff0000'>world</t>";
		StructuredTextParser parser = new StructuredTextParser(parsedText);
		assertEquals(parsedText, parser.getParsedText());
		ArrayList<TextSection> sections = new ArrayList<>();
		sections.add(new TextSection(TextSection.TagName.Root, "hello ", new HashMap<>()));
		sections.add(new TextSection(TextSection.TagName.T, "world", createAttributes(
				strArr("color", "#ff0000")
		)));
		assertEquals(sections, parser.parse());
	}

	@Test
	public void parseT2() throws Exception {
		String parsedText = "hello <t color='#ff0000'>from the <t size='2'>other</t> side</t>";
		StructuredTextParser parser = new StructuredTextParser(parsedText);
		assertEquals(parsedText, parser.getParsedText());
		ArrayList<TextSection> sections = new ArrayList<>();
		sections.add(new TextSection(TextSection.TagName.Root, "hello ", new HashMap<>()));
		sections.add(new TextSection(TextSection.TagName.T, "from the ", createAttributes(
				strArr("color", "#ff0000")
		)));
		sections.add(new TextSection(TextSection.TagName.T, "other", createAttributes(
				strArr("color", "#ff0000", "size", "2")
		)));
		sections.add(new TextSection(TextSection.TagName.T, " side", createAttributes(
				strArr("color", "#ff0000")
		)));
		assertEquals(sections, parser.parse());
	}

	@Test
	public void parseT3() throws Exception {
		String parsedText = "hello <t color='#ff0000'>from the<br /><t size='2'>other</t><br />side</t>";
		StructuredTextParser parser = new StructuredTextParser(parsedText);
		assertEquals(parsedText, parser.getParsedText());
		ArrayList<TextSection> sections = new ArrayList<>();
		sections.add(new TextSection(TextSection.TagName.Root, "hello ", new HashMap<>()));
		sections.add(new TextSection(TextSection.TagName.T, "from the", createAttributes(
				strArr("color", "#ff0000")
		)));
		sections.add(new TextSection(TextSection.TagName.Br, "", createAttributes(
				strArr("color", "#ff0000")
		)));
		sections.add(new TextSection(TextSection.TagName.T, "other", createAttributes(
				strArr("color", "#ff0000", "size", "2")
		)));
		sections.add(new TextSection(TextSection.TagName.Br, "", createAttributes(
				strArr("color", "#ff0000")
		)));
		sections.add(new TextSection(TextSection.TagName.T, "side", createAttributes(
				strArr("color", "#ff0000")
		)));
		assertEquals(sections, parser.parse());
	}

	private String[] strArr(@NotNull String... strings) {
		return strings;
	}

	private static HashMap<String, String> createAttributes(@NotNull String... attrsAndVals) {
		HashMap<String, String> map = new HashMap<>();
		for (int i = 0; i < attrsAndVals.length; i += 2) {
			map.put(attrsAndVals[i], attrsAndVals[i + 1]);
		}
		return map;
	}
}