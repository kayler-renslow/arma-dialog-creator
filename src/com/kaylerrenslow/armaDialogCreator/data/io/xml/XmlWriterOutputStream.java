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

import com.kaylerrenslow.armaDialogCreator.util.KeyValueString;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 A wrapper class for a {@link FileOutputStream} that writes XML files.

 @author Kayler
 @since 11/12/2016. */
public class XmlWriterOutputStream {
	private final FileOutputStream fos;
	private boolean hasProlog = false;
	public static final String UTF_8 = "UTF-8";

	public XmlWriterOutputStream(@NotNull File writeXmlFile) throws FileNotFoundException {
		fos = new FileOutputStream(writeXmlFile);
	}

	/** Write the XML's prolog tag */
	public void writeProlog(@NotNull String xmlVersion, @NotNull String encoding) throws IOException {
		write("<?xml version='" + xmlVersion + "' encoding='" + encoding + "' ?>");
		hasProlog = true;
	}

	public void writeDefaultProlog() throws IOException {
		writeProlog("1.0", UTF_8);
	}

	/** Writes a string to stream */
	public void write(@NotNull String s) throws IOException {
		fos.write(s.getBytes());
	}

	/** Writes a comment to stream */
	public void writeComment(@NotNull String comment) throws IOException {
		write("<!-- " + comment + " -->");
	}

	/** {@link FileOutputStream#flush()} */
	public void flush() throws IOException {
		fos.flush();
	}

	/** {@link FileOutputStream#close()} */
	public void close() throws IOException {
		fos.close();
	}

	/** Writes just a basic tag. Example: "&lt;tagName&gt;" */
	public void writeBeginTag(@NotNull String tagName) throws IOException {
		write("<" + tagName + ">");
	}

	/** Writes just a basic closing tag. Example: "&lt;/tagName&gt;" */
	public void writeCloseTag(@NotNull String tagName) throws IOException {
		write("</" + tagName + ">");
	}

	/**
	 Get String that inserts the tag name and attributes (will just return &lt;tagName&gt;). If a {@link KeyValueString#getValue()} is null, attribute will be skipped. All attribute values are
	 escaped via {@link #esc(String)}
	 */
	@NotNull
	public String getTagStart(@NotNull String tagName, @NotNull KeyValueString... attributes) {
		String s = "<" + tagName;
		if (attributes.length != 0) {
			for (KeyValueString attribute : attributes) {
				if (attribute.getValue() == null) {
					continue;
				}
				s += " " + attribute.getKey() + "='" + esc(attribute.getValue()) + "'";
			}
		}
		s += ">";
		return s;
	}

	public static String esc(String value) {
		return value.replaceAll("'", "&#39;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

}
