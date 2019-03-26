package com.armadialogcreator.application;

import com.armadialogcreator.application.xml.XMLNodeConfigurable;
import com.armadialogcreator.util.XmlParseException;
import com.armadialogcreator.util.XmlReader;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 @author K
 @since 3/25/19 */
public class XmlConfigurableLoader {

	@NotNull
	public static Configurable load(@NotNull ADCFile file) throws XmlParseException {
		return new XmlConfigurableLoader(file).getConfigurable();
	}

	private final ADCFile file;

	private XmlConfigurableLoader(@NotNull ADCFile file) {
		this.file = file;
	}

	@NotNull
	private Configurable getConfigurable() throws XmlParseException {
		XmlReader reader;
		try {
			reader = new XmlReader(file.newReader());
		} catch (FileNotFoundException e) {
			throw new XmlParseException(XmlParseException.Reason.FileNotFound, e);
		} catch (IOException e) {
			throw new XmlParseException(XmlParseException.Reason.Other, e);
		}

		return new XMLNodeConfigurable(reader.getDocumentElement());
	}

}
