package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 Created by Kayler on 07/31/2016.
 */
class XmlLoader implements XmlErrorRecorder {
	protected final Document document;
	protected final @Nullable DataContext dataContext;
	private final ArrayList<ParseError> errors = new ArrayList<>();
	protected final DocumentBuilderFactory builderFactory;
	protected final DocumentBuilder documentBuilder;


	private void checkParams(@Nullable DataContext context, Key<?>[] keys) {
		if (keys != null && context != null && !context.keysSet(keys)) {
			throw new IllegalArgumentException("DataContext must contain keys:" + Arrays.toString(keys));
		}
	}

	protected XmlLoader(@NotNull File xmlFile, @Nullable DataContext context, Key<?>... keys) throws XmlParseException {
		checkParams(context, keys);
		this.dataContext = context;
		try {
			builderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = builderFactory.newDocumentBuilder();
			document = documentBuilder.parse(xmlFile);
			document.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new XmlParseException(Lang.ApplicationBundle().getString("XmlParse.failed_to_read_xml"), e);
		}
	}

	protected XmlLoader(@NotNull InputStream is, @Nullable DataContext context, Key<?>... keys) throws XmlParseException {
		checkParams(context, keys);
		this.dataContext = context;
		try {
			builderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = builderFactory.newDocumentBuilder();
			document = documentBuilder.parse(is);
			document.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new XmlParseException(Lang.ApplicationBundle().getString("XmlParse.failed_to_read_xml"));
		}
	}

	static class ParseResult {
		private final ArrayList<ParseError> errors;

		protected ParseResult(ArrayList<ParseError> errors) {
			this.errors = errors;
		}

		@NotNull
		public ArrayList<ParseError> getErrors() {
			return errors;
		}
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
