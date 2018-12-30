package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 A wrapped {@link FileInputStream} instance that will read all text content in UTF-8 encoding.

 @author Kayler
 @since 11/05/2017 */
public class UTF8FileReader extends Reader {
	private final Reader reader;

	public UTF8FileReader(@NotNull File f) throws FileNotFoundException {
		reader = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8);
	}

	@Override
	public int read(@NotNull char[] cbuf, int off, int len) throws IOException {
		return reader.read(cbuf, off, len);
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	@Override
	public int read(@NotNull CharBuffer target) throws IOException {
		return reader.read(target);
	}

	@Override
	public int read() throws IOException {
		return reader.read();
	}

	@Override
	public int read(@NotNull char[] cbuf) throws IOException {
		return reader.read(cbuf);
	}

	@Override
	public long skip(long n) throws IOException {
		return reader.skip(n);
	}

	@Override
	public boolean ready() throws IOException {
		return reader.ready();
	}

	@Override
	public boolean markSupported() {
		return reader.markSupported();
	}

	@Override
	public void mark(int readAheadLimit) throws IOException {
		reader.mark(readAheadLimit);
	}

	@Override
	public void reset() throws IOException {
		reader.reset();
	}
}
