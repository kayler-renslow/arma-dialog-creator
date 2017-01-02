package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.*;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.impl.StringTableImpl;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.impl.StringTableKeyImpl;
import com.kaylerrenslow.armaDialogCreator.util.XmlUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 Parsers an Arma 3 formatter stringtable.xml file. <a href="https://community.bistudio.com/wiki/Stringtable.xml">See this for more information</a>.

 @author Kayler
 @since 12/12/2016 */
public class DefaultStringTableXmlParser extends XmlLoader implements StringTableParser {

	private final File xmlFile;

	/**
	 @param xmlFile the .xml file that contains the {@link StringTable} entries
	 @throws XmlParseException
	 */
	public DefaultStringTableXmlParser(@NotNull File xmlFile) throws XmlParseException {
		super(xmlFile, null);
		this.xmlFile = xmlFile;
	}

	@NotNull
	@Override
	public StringTable createStringTableInstance() throws IOException {
		Element rootElement = document.getDocumentElement();

		ObservableList<StringTableKey> tableKeys = FXCollections.observableArrayList();
		fetchKeys(rootElement, new StringTableKeyPath(null), tableKeys);

		loadPackageElements(rootElement, tableKeys);
		loadContainerElements(rootElement, new StringTableKeyPath(null), tableKeys);

		return new StringTableImpl(this.xmlFile, tableKeys, rootElement.getAttribute(StringTableXmlConstants.PROJECT_NAME));
	}

	private void loadPackageElements(Element element, List<StringTableKey> tableKeys) {
		List<Element> packageElements = XmlUtil.getChildElementsWithTagName(element, StringTableXmlConstants.PACKAGE);
		for (Element packageElement : packageElements) {
			String packageName = packageElement.getAttribute(StringTableXmlConstants.NAME);
			StringTableKeyPath path = new StringTableKeyPath(packageName);

			fetchKeys(packageElement, path, tableKeys); //may not be in a container
			loadContainerElements(packageElement, path, tableKeys);
		}
	}

	private void loadContainerElements(Element element, StringTableKeyPath path, List<StringTableKey> tableKeys) {
		List<Element> containerElements = XmlUtil.getChildElementsWithTagName(element, StringTableXmlConstants.CONTAINER);
		for (Element containerElement : containerElements) {
			String containerName = containerElement.getAttribute(StringTableXmlConstants.NAME);
			path.getContainers().add(containerName);

			fetchKeys(containerElement, path, tableKeys);
			loadContainerElements(containerElement, path.deepCopy(), tableKeys);
			path.getContainers().remove(containerName);
		}

	}

	private void fetchKeys(Element element, StringTableKeyPath path, List<StringTableKey> tableKeys) {
		List<Element> keyElements = XmlUtil.getChildElementsWithTagName(element, StringTableXmlConstants.KEY);

		for (Element keyElement : keyElements) {
			String id = keyElement.getAttribute(StringTableXmlConstants.ID).trim();

			if (id.length() > 0) {
				List<Element> valueElements = XmlUtil.getChildElementsWithTagName(keyElement, null);
				ObservableMap<Language, String> map = FXCollections.observableMap(new HashMap<>(valueElements.size()));
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
				tableKeys.add(new StringTableKeyImpl(id, path.deepCopy(), map));
			}
		}
	}
}
