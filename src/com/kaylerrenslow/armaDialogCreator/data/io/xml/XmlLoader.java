/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.data.io.xml;

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
import java.util.ArrayList;
import java.util.Arrays;

/**
 Created by Kayler on 07/31/2016.
 */
class XmlLoader {
	protected final Document document;
	protected final @Nullable DataContext dataContext;
	private final ArrayList<ParseError> errors = new ArrayList<>();
	protected final DocumentBuilderFactory builderFactory;
	protected final DocumentBuilder documentBuilder;
	
	protected XmlLoader(@NotNull File xmlFile, @Nullable DataContext context, Key<?>[] keys) throws XmlParseException {
		if (keys != null && context != null && !context.keysSet(keys)) {
			throw new IllegalArgumentException("DataContext must contain keys:" + Arrays.toString(keys));
		}
		try {
			builderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = builderFactory.newDocumentBuilder();
			document = documentBuilder.parse(xmlFile);
			document.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new XmlParseException(Lang.ApplicationBundle().getString("XmlParse.failed_to_read_xml"));
		}
		
		this.dataContext = context;
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
	
	protected void addError(@NotNull ParseError error) {
		errors.add(error);
	}
	
	protected final ArrayList<ParseError> getErrors() {
		return errors;
	}
	
	/** Equivalent of doing {@link #document}.getDocumentElement() */
	protected Element getDocumentElement() {
		return document.getDocumentElement();
	}
}
