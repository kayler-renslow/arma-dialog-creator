package com.armadialogcreator.application.xml;

import com.armadialogcreator.application.ADCData;
import com.armadialogcreator.application.ADCDataListManager;
import com.armadialogcreator.application.ADCFile;
import com.armadialogcreator.util.XmlParseException;
import com.armadialogcreator.util.XmlReader;
import com.armadialogcreator.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Stack;

/**
 @author K
 @since 01/07/2019 */
public class ADCDataConfigurableXmlReader<D extends ADCData> {
	private final ADCFile f;
	private final ADCDataListManager<D> manager;

	public ADCDataConfigurableXmlReader(@NotNull ADCFile f, @NotNull ADCDataListManager<D> manager) {
		this.f = f;
		this.manager = manager;
	}

	public void read() throws XmlParseException {
		XmlReader reader;
		try {
			reader = new XmlReader(f.newReader());
		} catch (FileNotFoundException e) {
			throw new XmlParseException(XmlParseException.Reason.FileNotFound, e);
		} catch (IOException e) {
			throw new XmlParseException(XmlParseException.Reason.Other, e);
		}
		Stack<ADCFile> includedFiles = new Stack<>();
		for (Element element : XmlUtil.iterateChildElements(reader.getDocumentElement())) {
			if (element.getTagName().equals("include")) {
				String path = element.getAttribute("f");
				ADCFile includedFile = f.getFileInOwnerDirectory(path);
				if (includedFile.exists()) {
					includedFiles.push(includedFile);
				}
			}
			for (D d : manager.getDataList()) {
				if (d.getDataID().equals(element.getTagName())) {
					d.loadFromConfigurable(new XMLNodeConfigurable(element));
				}
			}
		}
		while (!includedFiles.empty()) {
			ADCDataConfigurableXmlReader<D> r = new ADCDataConfigurableXmlReader<>(includedFiles.pop(), manager);
			r.read();
		}
	}
}
