package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import java.util.ArrayList;
import java.util.Arrays;

/**
 Reads XML files and reports errors. In the case that an XML file is wrongly encoded (assuming it is wrongly encoded in UTF-16),
 the loader will read the file in UTF-16 and parse in UTF-8 to ensure the XML file will parse correctly.
 If the XML file is encoded in UTF-16, but the XML file has the &lt;?xml&gt; encoding flag set to something other than
 UTF-8, behavior is undefined. All XML files should be encoded in UTF-8 due to Arma 3's standards.

 @author Kayler
 @since 07/31/2016 */
class XmlLoader implements XmlErrorRecorder {
	protected final Document document;
	protected final @Nullable DataContext dataContext;
	private final ArrayList<ParseError> errors = new ArrayList<>();


	private void checkParams(@Nullable DataContext context, Key<?>[] keys) {
		if (keys != null && context != null && !context.keysSet(keys)) {
			throw new IllegalArgumentException("DataContext must contain keys:" + Arrays.toString(keys));
		}
	}

	/**
	 Reads and loads the XML from the given reader. Note: this constructor <b>will</b> attempt to re-read
	 the XML in UTF-16 if the encoding is wrong (see class level doc).

	 @param xmlFile XML file to read
	 @param context context to use
	 @param keys keys to use
	 @throws XmlParseException when the XML couldn't be parsed
	 */
	protected XmlLoader(@NotNull File xmlFile, @Nullable DataContext context, Key<?>... keys) throws XmlParseException {
		checkParams(context, keys);
		this.dataContext = context;
		this.document = getDocumentFromFile(xmlFile);
	}

	/**
	 Reads and loads the XML from the given reader. Note: this constructor will not attempt to re-encode
	 the XML in UTF-8 if the encoding is wrong. Use {@link #XmlLoader(File, DataContext, Key[])} to get that functionality.

	 @param r the reader that will read the XML
	 @param context context to use
	 @param keys keys to use
	 @throws XmlParseException if the xML couldn't be parsed
	 */
	protected XmlLoader(@NotNull Reader r, @Nullable DataContext context, Key<?>... keys) throws XmlParseException {
		checkParams(context, keys);
		this.dataContext = context;
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
			throw new XmlParseException(Lang.ApplicationBundle().getString("XmlParse.failed_to_read_xml"), e);
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
			throw new XmlParseException(Lang.ApplicationBundle().getString("XmlParse.failed_to_read_xml"), e);
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
						Lang.ApplicationBundle().getString("XmlParse.failed_to_read_xml"),
						new XmlParseException(Lang.ApplicationBundle().getString("XmlParse.failed_to_re-encode_xml"), e)
				);
			}
		}
		document.getDocumentElement().normalize();
		return document;
	}

	@Override
	public final ArrayList<ParseError> getErrors() {
		return errors;
	}

	/** Equivalent of doing {@link #document}.getDocumentElement() */
	protected Element getDocumentElement() {
		return document.getDocumentElement();
	}
}
