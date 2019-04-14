package com.armadialogcreator.application.xml;

import com.armadialogcreator.application.ADCData;
import com.armadialogcreator.application.ADCDataListManager;
import com.armadialogcreator.util.XmlWriter;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

/**
 @author K
 @since 3/6/19 */
public abstract class XmlConfigurableWriter<D extends ADCData> {
	protected final ADCDataListManager<D> manager;
	protected final XmlWriter writer;
	protected final File rootSaveFile;
	protected final String formatVersion;
	public static final String ROOT_TAG_NAME = "adc-data";

	public XmlConfigurableWriter(@NotNull ADCDataListManager<D> manager, @NotNull File rootSaveFile, @NotNull String formatVersion) {
		this.manager = manager;
		writer = new XmlWriter(rootSaveFile, ROOT_TAG_NAME);
		this.rootSaveFile = rootSaveFile;
		this.formatVersion = formatVersion;
	}

	public void write() throws IOException, TransformerException {
		final File rootSaveDir = rootSaveFile.getAbsoluteFile().getParentFile();
		if (!rootSaveFile.exists()) {
			rootSaveDir.mkdirs();
			rootSaveFile.createNewFile();
		}

		{ //write file details
			Element fileDetails = writer.appendElementToRoot("file-details");
			{
				Element format = writer.appendElementToElement("format", fileDetails);
				format.setAttribute("v", formatVersion);
			}
			{
				Element saveTime = writer.appendElementToElement("save-time", fileDetails);
				saveTime.setAttribute("t", System.currentTimeMillis() + "");
			}
			{
				Element dataLevel = writer.appendElementToElement("data-level", fileDetails);
				dataLevel.setAttribute("v", manager.getDataLevel().name());
			}
		}
	}
}
