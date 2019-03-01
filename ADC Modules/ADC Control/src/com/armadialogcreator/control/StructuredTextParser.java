package com.armadialogcreator.control;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 A parser for Structured Text

 @author Kayler
 @since 07/03/2017 */
public class StructuredTextParser {

	private final String parsedText;

	public StructuredTextParser(@NotNull String toParse) {
		this.parsedText = toParse;
	}

	/** @return the text that was requested to be parsed */
	@NotNull
	public String getParsedText() {
		return parsedText;
	}

	/**
	 @return list of sections for the parsed text.
	 @throws StructuredTextParseException when the text couldn't be parsed
	 @see TextSection
	 */
	@NotNull
	public List<TextSection> parse() throws StructuredTextParseException {
		Document document = parseXml();
		return traverse(document.getDocumentElement(), new HashMap<>(), new ArrayList<>());
	}

	@NotNull
	private Document parseXml() throws StructuredTextParseException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			//shouldn't happen
			throw new RuntimeException(e);
		}
		String text = "<?xml version='1.0' encoding='UTF-8' ?><_.r>" + parsedText + "</_.r>";
		// the xml parser requires the prolog and a root element. So we'll just them in there
		// and ignore them after the parse

		ByteArrayInputStream stm = new ByteArrayInputStream(text.getBytes());
		Document document = null;
		try {
			document = documentBuilder.parse(stm);
		} catch (SAXException e) {
			throw new StructuredTextParseException(e);
		} catch (IOException e) {
			//shouldn't happen
			throw new RuntimeException(e);
		}
		document.getDocumentElement().normalize();

		return document;
	}

	private List<TextSection> traverse(@NotNull Element start, @NotNull Map<String, String> currentAttributes,
									   @NotNull List<TextSection> sections) {

		TextSection.TagName tagName = TextSection.TagName.Root;

		String elementTagName = start.getTagName();
		for (TextSection.TagName tName : TextSection.TagName.values()) {
			if (tName.name().equalsIgnoreCase(elementTagName)) {
				tagName = tName;
				break;
			}
		}

		NamedNodeMap attributes = start.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Attr attr = (Attr) attributes.item(i);
			currentAttributes.put(attr.getName(), attr.getValue());

		}

		NodeList childNodes = start.getChildNodes();
		if (childNodes.getLength() == 0) {
			sections.add(new TextSection(tagName, "", currentAttributes));
			return sections;
		}
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				sections.add(new TextSection(tagName, child.getTextContent(), currentAttributes));
			} else if (child.getNodeType() == Node.ELEMENT_NODE) {
				Map<String, String> copy = new HashMap<>();
				copy.putAll(currentAttributes);
				traverse((Element) child, copy, sections);
			}
		}

		return sections;
	}

}
