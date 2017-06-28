package com.kaylerrenslow.armaDialogCreator.data.xml;

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
	private static final int MAX_BUFFER_SIZE = 1000;
	public static final String UTF_8 = "UTF-8";

	protected final byte[] NEW_LINE = "\n".getBytes();
	private final FileOutputStream fos;
	private int bufferSize = 0;

	public XmlWriterOutputStream(@NotNull File writeXmlFile) throws IOException {
		if (writeXmlFile.exists()) {
			//create backup
			Files.copy(writeXmlFile.toPath(), new File(writeXmlFile.getPath() + ".backup").toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		fos = new FileOutputStream(writeXmlFile);
	}

	private void checkBufferSize() throws IOException {
		if (bufferSize > MAX_BUFFER_SIZE) {
			fos.flush();
			bufferSize = 0;
		}
	}

	/** Write the XML's prolog tag */
	public void writeProlog(@NotNull String xmlVersion, @NotNull String encoding) throws IOException {
		write("<?xml version='" + xmlVersion + "' encoding='" + encoding + "' ?>");
	}

	/** Invokes {@link #writeProlog(String, String)} with xmlVersion='1.0' and encoding='{@link #UTF_8}' */
	public void writeDefaultProlog() throws IOException {
		writeProlog("1.0", UTF_8);
	}

	/** Writes a byte[] to stream */
	public void write(@NotNull byte[] bytes) throws IOException {
		fos.write(bytes);
		bufferSize += bytes.length;
		checkBufferSize();
	}

	/** Writes a byte[] to stream */
	public void writeln(@NotNull byte[] bytes) throws IOException {
		fos.write(bytes);
		fos.write(NEW_LINE);
		bufferSize += bytes.length + 1;
		checkBufferSize();
	}

	/** Writes a string to stream */
	public void write(@NotNull String s) throws IOException {
		write(s.getBytes());
	}

	/** Writes a string to stream and then a \n character */
	public void writeln(@NotNull String s) throws IOException {
		write(s);
		fos.write(NEW_LINE);
		bufferSize += 1;
		checkBufferSize();
	}

	/** Writes a comment to stream */
	public void writeComment(@NotNull String comment) throws IOException {
		write("<!-- " + comment + " -->");
	}

	/** Writes just a basic tag. Example: "&lt;tagName&gt;" */
	public void writeBeginTag(@NotNull String tagName) throws IOException {
		write("<" + tagName + ">");
	}

	/** Writes just a basic closing tag. Example: "&lt;/tagName&gt;" */
	public void writeCloseTag(@NotNull String tagName) throws IOException {
		write("</" + tagName + ">");
	}

	/** {@link FileOutputStream#flush()} */
	public void flush() throws IOException {
		fos.flush();
	}

	/** {@link FileOutputStream#close()} */
	public void close() throws IOException {
		fos.close();
	}

	public static String esc(String value) {
		return value.replaceAll("'", "&#39;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

}
