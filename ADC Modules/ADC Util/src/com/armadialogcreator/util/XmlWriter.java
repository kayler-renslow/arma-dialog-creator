package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 A class that aids in creating XML DOM and writing to a file.
 This class will also create a backup for existing xml files, if the names match.

 @author Kayler
 @since 11/12/2016. */
public class XmlWriter {
	public static final String UTF_8 = "UTF-8";

	private final Document document;
	private final String encoding;
	private final Element rootElement;
	private final File writeXmlFile;

	/**
	 Creates an XmlWriter with the given File and root tag name. The encoding will be set to UTF-8 ({@link #UTF_8}).
	 This constructor will also create a backup for the existing file at the writeXmlFile's path, if it exists.

	 @param writeXmlFile the file to write the XML to
	 @param rootTagName the root tag's name
	 */
	public XmlWriter(@NotNull File writeXmlFile, @NotNull String rootTagName) {
		this(writeXmlFile, rootTagName, UTF_8);
	}

	/**
	 Creates an XmlWriter with the given File and root tag name. The encoding can be set here as well.
	 This constructor will also create a backup for the existing file at the writeXmlFile's path, if it exists.

	 @param writeXmlFile the file to write the XML to
	 @param rootTagName the root tag's name
	 @param encoding the encoding to use
	 */
	public XmlWriter(@NotNull File writeXmlFile, @NotNull String rootTagName, @NotNull String encoding) {
		this.writeXmlFile = writeXmlFile;
		DocumentBuilder builder;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
		document = builder.newDocument();
		this.encoding = encoding;
		rootElement = document.createElement(rootTagName);
		document.appendChild(rootElement);
	}

	/**
	 @return the Document for the writer
	 */
	@NotNull
	public Document getDocument() {
		return document;
	}

	/** @return the encoding for the document */
	@NotNull
	public String getEncoding() {
		return encoding;
	}

	/** @return the root element of {@link #getDocument()} */
	@NotNull
	public Element getRootElement() {
		return rootElement;
	}

	/**
	 Appends a new element to the root element ({@link #getRootElement()}) and returns the element.

	 @param tagName the new element's tag name
	 @return the new element appended to root
	 */
	@NotNull
	public Element appendElementToRoot(@NotNull String tagName) {
		Element element = document.createElement(tagName);
		rootElement.appendChild(element);
		return element;
	}

	/**
	 Appends a new element to the specified element and returns the element.

	 @param tagName the new element's tag name
	 @return the new element appended to <code>appendTo</code>
	 */
	@NotNull
	public Element appendElementToElement(@NotNull String tagName, @NotNull Element appendTo) {
		Element element1 = document.createElement(tagName);
		appendTo.appendChild(element1);
		return element1;
	}

	/**
	 Appends a new text node to the root element ({@link #getRootElement()}) and returns the node.

	 @param text the text content for the text node
	 @return the new text node appended to root
	 */
	@NotNull
	public Text appendTextNodeToRoot(@NotNull String text) {
		Text textNode = document.createTextNode(text);
		rootElement.appendChild(textNode);
		return textNode;
	}

	/**
	 Appends a new text node to the specified element and returns the node.

	 @param text the text content for the text node
	 @param addTo the element to add the new text node to
	 @return the new text node appended to root
	 */
	@NotNull
	public Text appendTextNode(@NotNull String text, @NotNull Element addTo) {
		Text textNode = document.createTextNode(text);
		addTo.appendChild(textNode);
		return textNode;
	}

	/**
	 Writes the XML to file with the set encoding ({@link #getEncoding()}).

	 @param indentAmount how many spaces the indentation should be, or -1 to not indent
	 @throws TransformerException Visit {@link Transformer#transform(Source, Result)} for more info
	 */
	public void writeToFile(int indentAmount) throws TransformerException {
		Transformer transformer;
		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			transformer = factory.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		}
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(writeXmlFile);

		transformer.setOutputProperty(OutputKeys.ENCODING, this.encoding);
		if (indentAmount > 0) {
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", indentAmount + "");
		} else {
			transformer.setOutputProperty(OutputKeys.INDENT, "no");
		}
		transformer.transform(source, result);
	}

}
