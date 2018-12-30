package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 A wrapped {@link FileOutputStream} instance that will write all text content in UTF-8 encoding.

 @author Kayler
 @since 11/03/2017 */
public class UTF8FileWriter extends Writer {
	private final Writer writer;

	public UTF8FileWriter(@NotNull File file) throws FileNotFoundException {
		writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
	}

	/**
	 Writes a String

	 @param s the String
	 @throws IOException if I/O error occurs
	 */
	@Override
	public void write(@NotNull String s) throws IOException {
		writer.write(s);
	}

	/**
	 Writes an int

	 @param i the int
	 @throws IOException if I/O error occurs
	 */
	@Override
	public void write(int i) throws IOException {
		writer.write(i);
	}

	@Override
	public void write(@NotNull char[] cbuf, int off, int len) throws IOException {
		writer.write(cbuf, off, len);
	}

	/**
	 Flushes the text to file

	 @throws IOException if I/O error occurs
	 */
	@Override
	public void flush() throws IOException {
		writer.flush();
	}

	/**
	 Closes the stream.

	 @throws IOException if an I/O error occurs
	 */
	@Override
	public void close() throws IOException {
		writer.close();
	}
}
