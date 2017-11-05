package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 @since 11/05/2017 */
public class XmlLoaderEncodingTest {
	@Test
	public void correctEncodingTest() throws Exception {
		XmlLoader loader = new XmlLoader(getFile("encodingTestGood.xml"), null);
		assertEquals("good-encoding-test", loader.document.getDocumentElement().getTagName());
		assertEquals("~!@#$%^&*()_+`1234567890-=[]{}\\|<>?,./abcdefÄä", XmlUtil.getImmediateTextContent(loader.getDocumentElement()));
	}

	@Test
	public void wrongEncodingTest1() throws Exception {
		File f = getFile("encodingTestBad.xml");
		Writer writer = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_16);
		//purposefully write the XML in UTF-16 to make sure that the XmlLoader will re-encode the file in UTF-8
		writer.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
		writer.write("<bad-encoding-test>~!@#$%^&amp;*()_+`1234567890-=[]{}\\|&lt;&gt;?,./abcdefÄä</bad-encoding-test>");
		writer.flush();
		writer.close();

		XmlLoader loader = new XmlLoader(f, null);
		assertEquals("bad-encoding-test", loader.document.getDocumentElement().getTagName());
		assertEquals("~!@#$%^&*()_+`1234567890-=[]{}\\|<>?,./abcdefÄä", XmlUtil.getImmediateTextContent(loader.getDocumentElement()));
	}

	@Test
	public void wrongEncodingTest2() throws Exception {
		File f = getFile("encodingTestBad2.xml");
		FileOutputStream fos = new FileOutputStream(f);
		//purposefully write the XML in non utf-8 to make sure that the XmlLoader will re-read the file in UTF-16
		fos.write("<?xml version=\"1.0\" encoding=\"utf-8\" ?>".getBytes());
		fos.write("<bad-encoding-test>~!@#$%^&amp;*()_+`1234567890-=[]{}\\|&lt;&gt;?,./abcdefÄä</bad-encoding-test>".getBytes());
		fos.flush();
		fos.close();

		XmlLoader loader = new XmlLoader(f, null);
		assertEquals("bad-encoding-test", loader.document.getDocumentElement().getTagName());
		assertEquals("~!@#$%^&*()_+`1234567890-=[]{}\\|<>?,./abcdefÄä", XmlUtil.getImmediateTextContent(loader.getDocumentElement()));
	}

	@NotNull
	private File getFile(@NotNull String name) {
		return new File("tests/com/kaylerrenslow/armaDialogCreator/data/xml/" + name);
	}

}