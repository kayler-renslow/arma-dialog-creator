package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 Reads XML files and reports errors. In the case that an XML file is wrongly encoded (assuming it is wrongly encoded in UTF-16),
 the loader will read the file in UTF-16 and parse in UTF-8 to ensure the XML file will parse correctly.
 If the XML file is encoded in UTF-16, but the XML file has the &lt;?xml&gt; encoding flag set to something other than
 UTF-8, behavior is undefined. All XML files should be encoded in UTF-8 due to Arma 3's standards.

 @author Kayler
 @since 07/31/2016 */
public class XmlReader {
	@NotNull
	protected final Document document;


	/**
	 Reads and loads the XML from the given reader. Note: this constructor <b>will</b> attempt to re-read
	 the XML in UTF-16 if the encoding is wrong (see class level doc).

	 @param xmlFile XML file to read
	 @throws XmlParseException when the XML couldn't be parsed
	 */
	public XmlReader(@NotNull File xmlFile) throws XmlParseException {
		this.document = getDocumentFromFile(xmlFile);
	}

	/**
	 Reads and loads the XML from the given reader. Note: this constructor will not attempt to re-encode
	 the XML in UTF-8 if the encoding is wrong. Use {@link #XmlReader(File)} to get that functionality.

	 @param r the reader that will read the XML
	 @throws XmlParseException if the xML couldn't be parsed
	 */
	public XmlReader(@NotNull Reader r) throws XmlParseException {
		this.document = getDocumentFromReader(r);
	}

	@NotNull
	private Document getDocumentFromReader(@NotNull Reader r) throws XmlParseException {
		Document document = null;
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
			document = documentBuilder.parse(new InputSource(r));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new XmlParseException(XmlParseException.Reason.FailedParse, e);
		}
		document.getDocumentElement().normalize();
		return document;
	}

	@NotNull
	private Document getDocumentFromFile(@NotNull File xmlFile) throws XmlParseException {
		Document document = null;
		DocumentBuilder documentBuilder;
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = builderFactory.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new XmlParseException(XmlParseException.Reason.FailedParse, e);
		}
		try {
			document = documentBuilder.parse(xmlFile);
		} catch (Exception e) {
			// read the file in UTF-16 and parse again
			// The reason why we are doing this is because in older version of Arma Dialog Creator, the XML wasn't properly being encoded in UTF-8
			// and were instead encoded in UTF-16.
			try {
				InputStreamReader stm = new InputStreamReader(new FileInputStream(xmlFile), StandardCharsets.UTF_16);
				document = documentBuilder.parse(new InputSource(stm));
			} catch (Exception e1) {
				e1.printStackTrace(System.out);
				throw new XmlParseException(
						XmlParseException.Reason.FailedReEncode
				);
			}
		}
		document.getDocumentElement().normalize();
		return document;
	}

	/** Equivalent of doing {@link #document}.getDocumentElement() */
	@NotNull
	public Element getDocumentElement() {
		return document.getDocumentElement();
	}

	@NotNull
	public Document getDocument() {
		return document;
	}
}
