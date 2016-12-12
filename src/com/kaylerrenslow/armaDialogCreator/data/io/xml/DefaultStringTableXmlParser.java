package com.kaylerrenslow.armaDialogCreator.data.io.xml;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.*;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.impl.StringTableImpl;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.impl.StringTableKeyImpl;
import com.kaylerrenslow.armaDialogCreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 Parsers an Arma 3 formatter stringtable.xml file. <a href="https://community.bistudio.com/wiki/Stringtable.xml">See this for more information</a>.

 @author Kayler
 @since 12/12/2016 */
public class DefaultStringTableXmlParser extends XmlLoader implements StringTableParser {
	private static final String ID = "ID";
	private static final String KEY = "Key";
	private static final String PACKAGE = "Package";
	private static final String CONTAINER = "Container";
	private static final String NAME = "name";
	private final File xmlFile;


	/**
	 @param xmlFile the .xml file that contains the {@link StringTable} entries
	 @throws XmlParseException
	 */
	protected DefaultStringTableXmlParser(@NotNull File xmlFile) throws XmlParseException {
		super(xmlFile, null);
		this.xmlFile = xmlFile;
	}

	@NotNull
	@Override
	public StringTable createStringTableInstance() throws IOException {
		Element rootElement = document.getDocumentElement();

		List<StringTableKey> tableKeys = new ArrayList<>();

		fetchKeys(rootElement, null, null, tableKeys);
		loadPackageElements(rootElement, tableKeys);
		loadContainerElements(rootElement, null, tableKeys);

		return new StringTableImpl(this.xmlFile, tableKeys);
	}

	private void loadPackageElements(Element element, List<StringTableKey> tableKeys) {
		List<Element> packageElements = XmlUtil.getChildElementsWithTagName(element, PACKAGE);
		for (Element packageElement : packageElements) {
			String packageName = packageElement.getAttribute(NAME);
			fetchKeys(packageElement, packageName, null, tableKeys); //may not be in a container
			loadContainerElements(packageElement, packageName, tableKeys);
		}
	}

	private void loadContainerElements(Element element, String packageName, List<StringTableKey> tableKeys) {
		List<Element> containerElements = XmlUtil.getChildElementsWithTagName(element, CONTAINER);
		for (Element containerElement : containerElements) {
			fetchKeys(containerElement, packageName, containerElement.getAttribute(NAME), tableKeys);
		}

	}

	private void fetchKeys(Element element, String packageName, String containerName, List<StringTableKey> tableKeys) {

		List<Element> keyElements = XmlUtil.getChildElementsWithTagName(element, KEY);

		for (Element keyElement : keyElements) {
			String id = keyElement.getAttribute(ID);
			if (id.length() > 0) {
				List<Element> valueElements = XmlUtil.getChildElementsWithTagName(element, null);
				Map<Language, String> map = new HashMap<>(valueElements.size());
				for (Element valueElement : valueElements) {
					Language language;
					String langName = valueElement.getTagName();
					try {
						language = KnownLanguage.valueOf(langName);
					} catch (IllegalArgumentException e) {
						language = new CustomLanguage(langName);
					}
					map.put(language, XmlUtil.getImmediateTextContent(valueElement));
				}
				tableKeys.add(new StringTableKeyImpl(id, packageName, containerName, map));
			}
		}
	}
}
