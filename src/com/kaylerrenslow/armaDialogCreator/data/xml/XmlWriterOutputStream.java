package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.util.KeyValueString;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 A wrapper class for a {@link FileOutputStream} that writes XML files.

 @author Kayler
 @since 11/12/2016. */
public class XmlWriterOutputStream {
	private final FileOutputStream fos;
	public static final String UTF_8 = "UTF-8";

	public XmlWriterOutputStream(@NotNull File writeXmlFile) throws IOException {
		if (writeXmlFile.exists()) {
			//create backup
			Files.copy(writeXmlFile.toPath(), new File(writeXmlFile.getPath() + ".backup").toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		fos = new FileOutputStream(writeXmlFile);
	}

	/** Write the XML's prolog tag */
	public void writeProlog(@NotNull String xmlVersion, @NotNull String encoding) throws IOException {
		write("<?xml version='" + xmlVersion + "' encoding='" + encoding + "' ?>");
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
