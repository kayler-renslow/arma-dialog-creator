package com.armadialogcreator.data.oldprojectloader;

import com.armadialogcreator.core.stringtable.*;
import com.armadialogcreator.data.SimpleStringTable;
import com.armadialogcreator.data.SimpleStringTableKey;
import com.armadialogcreator.util.*;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 Parsers an Arma 3 formatter stringtable.xml file. <a href="https://community.bistudio.com/wiki/Stringtable.xml">See this for more information</a>.

 @author Kayler
 @since 12/12/2016 */
public class DefaultStringTableXmlParser extends XmlReader implements StringTableParser {

	private final File xmlFile;

	/**
	 @param xmlFile the .xml file that contains the {@link StringTable} entries
	 @throws XmlParseException
	 */
	public DefaultStringTableXmlParser(@NotNull File xmlFile) throws XmlParseException {
		super(xmlFile);
		this.xmlFile = xmlFile;
	}

	@NotNull
	@Override
	public StringTable createStringTableInstance() throws IOException {
		Element rootElement = document.getDocumentElement();

		ListObserver<StringTableKey> tableKeys = new ListObserver<>(new ArrayList<>());
		fetchKeys(rootElement, new StringTableKeyPath(null), tableKeys);

		loadPackageElements(rootElement, tableKeys);
		loadContainerElements(rootElement, new StringTableKeyPath(null), tableKeys);

		return new SimpleStringTable(this.xmlFile, tableKeys, rootElement.getAttribute(StringTableXmlConstants.PROJECT_NAME));
	}

	private void loadPackageElements(Element element, List<StringTableKey> tableKeys) {
		for (Element packageElement : XmlUtil.iterateChildElements(element)) {
			if (!packageElement.getNodeName().equals(StringTableXmlConstants.PACKAGE)) {
				continue;
			}
			String packageName = packageElement.getAttribute(StringTableXmlConstants.NAME);
			StringTableKeyPath path = new StringTableKeyPath(packageName);

			fetchKeys(packageElement, path, tableKeys); //may not be in a container
			loadContainerElements(packageElement, path, tableKeys);
		}
	}

	private void loadContainerElements(Element element, StringTableKeyPath path, List<StringTableKey> tableKeys) {
		for (Element containerElement : XmlUtil.iterateChildElements(element)) {
			if (!containerElement.getNodeName().equals(StringTableXmlConstants.CONTAINER)) {
				continue;
			}
			String containerName = containerElement.getAttribute(StringTableXmlConstants.NAME);
			path.getContainers().add(containerName);

			fetchKeys(containerElement, path, tableKeys);
			loadContainerElements(containerElement, path.deepCopy(), tableKeys);
			path.getContainers().remove(containerName);
		}

	}

	private void fetchKeys(Element element, StringTableKeyPath path, List<StringTableKey> tableKeys) {

		for (Element keyElement : XmlUtil.iterateChildElements(element)) {
			if (!keyElement.getNodeName().equals(StringTableXmlConstants.KEY)) {
				continue;
			}
			String id = keyElement.getAttribute(StringTableXmlConstants.ID).trim();

			if (id.length() > 0) {
				MapObserver<Language, String> map = new MapObserver<>(new HashMap<>());
				for (Element valueElement : XmlUtil.iterateChildElements(element)) {
					Language language;
					String langName = valueElement.getTagName();
					try {
						language = KnownLanguage.valueOf(langName);
					} catch (IllegalArgumentException e) {
						language = new CustomLanguage(langName);
					}
					map.put(language, XmlUtil.getImmediateTextContent(valueElement));
				}
				tableKeys.add(new SimpleStringTableKey(id, path.deepCopy(), map));
			}
		}
	}
}
