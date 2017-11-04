package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 @since 11/03/2017 */
public class XmlWriterTest {

	@Test
	public void utf8Test() throws Exception {
		File outputFile = getOutputFile();
		XmlWriter writer = new XmlWriter(outputFile, "company", XmlWriter.UTF_8);
		{
			Element staff = writer.appendElementToRoot("staff");
			staff.setAttribute("id", "1");

			Element firstname = writer.appendElementToElement("firstname", staff);
			writer.appendTextNode("Leroy", firstname);

			Element lastname = writer.appendElementToElement("lastname", staff);
			writer.appendTextNode("Jenkins", lastname);

			Element specialcharacters = writer.appendElementToElement("specialcharacters", staff);
			writer.appendTextNode("!@#$%^&*()_+<>,.-=~`/\\", specialcharacters);
		}

		writer.writeToFile(4);

		{
			Document document = getNormalizedDocument(outputFile);
			assertEquals("company", document.getDocumentElement().getTagName());

			List<Element> staffList = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "staff");
			assertEquals(1, staffList.size());

			Element staff = staffList.get(0);

			assertEquals("1", staff.getAttribute("id"));

			List<Element> firstnameList = XmlUtil.getChildElementsWithTagName(staff, "firstname");
			assertEquals(1, firstnameList.size());
			assertEquals("Leroy", XmlUtil.getImmediateTextContent(firstnameList.get(0)));

			List<Element> lastnameList = XmlUtil.getChildElementsWithTagName(staff, "lastname");
			assertEquals(1, lastnameList.size());
			assertEquals("Jenkins", XmlUtil.getImmediateTextContent(lastnameList.get(0)));

			List<Element> specialcharactersList = XmlUtil.getChildElementsWithTagName(staff, "specialcharacters");
			assertEquals(1, lastnameList.size());
			assertEquals("!@#$%^&*()_+<>,.-=~`/\\", XmlUtil.getImmediateTextContent(specialcharactersList.get(0)));

			assertEquals("UTF-8", document.getXmlEncoding());
		}
	}

	@Test
	public void utf16Test() throws Exception {
		File outputFile = getOutputFile();
		XmlWriter writer = new XmlWriter(outputFile, "company", "UTF-16");
		{
			Element staff = writer.appendElementToRoot("staff");
			staff.setAttribute("id", "1");

			Element firstname = writer.appendElementToElement("firstname", staff);
			writer.appendTextNode("Leroy", firstname);

			Element lastname = writer.appendElementToElement("lastname", staff);
			writer.appendTextNode("Jenkins", lastname);

			Element specialcharacters = writer.appendElementToElement("specialcharacters", staff);
			writer.appendTextNode("!@#$%^&*()_+<>,.-=~`/\\", specialcharacters);
		}

		writer.writeToFile(4);

		{
			Document document = getNormalizedDocument(outputFile);
			assertEquals("company", document.getDocumentElement().getTagName());

			List<Element> staffList = XmlUtil.getChildElementsWithTagName(document.getDocumentElement(), "staff");
			assertEquals(1, staffList.size());

			Element staff = staffList.get(0);

			assertEquals("1", staff.getAttribute("id"));

			List<Element> firstnameList = XmlUtil.getChildElementsWithTagName(staff, "firstname");
			assertEquals(1, firstnameList.size());
			assertEquals("Leroy", XmlUtil.getImmediateTextContent(firstnameList.get(0)));

			List<Element> lastnameList = XmlUtil.getChildElementsWithTagName(staff, "lastname");
			assertEquals(1, lastnameList.size());
			assertEquals("Jenkins", XmlUtil.getImmediateTextContent(lastnameList.get(0)));

			List<Element> specialcharactersList = XmlUtil.getChildElementsWithTagName(staff, "specialcharacters");
			assertEquals(1, lastnameList.size());
			assertEquals("!@#$%^&*()_+<>,.-=~`/\\", XmlUtil.getImmediateTextContent(specialcharactersList.get(0)));

			assertEquals("UTF-16", document.getXmlEncoding());
		}
	}

	@NotNull
	private Document getNormalizedDocument(@NotNull File outputFile) throws Exception {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(new FileInputStream(outputFile));
		document.getDocumentElement().normalize();
		return document;
	}

	@NotNull
	private File getOutputFile() {
		return new File("tests/com/kaylerrenslow/armaDialogCreator/util/testData");
	}

}