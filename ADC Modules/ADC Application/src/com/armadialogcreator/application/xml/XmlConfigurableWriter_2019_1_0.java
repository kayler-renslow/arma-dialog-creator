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
public class XmlConfigurableWriter_2019_1_0<D extends ADCData> extends XmlConfigurableWriter<D> {

	public XmlConfigurableWriter_2019_1_0(@NotNull ADCDataListManager<D> manager, @NotNull File rootSaveFile) {
		super(manager, rootSaveFile, "2019.1.0");
	}

	public void write() throws IOException, TransformerException {
		super.write();

		String[] includes = new String[manager.getDataList().size()];
		int i = 0;
		for (D d : manager.getDataList()) {
			Element include = writer.appendElementToRoot("include");
			String file = "includes" + File.separator + d.getDataID() + ".adc";
			include.setAttribute("f", file);
			includes[i++] = file;
		}
		writer.writeToFile(0);
		i = 0;
		for (D d : manager.getDataList()) {
			File includeFile = new File(rootSaveFile.getParentFile().getAbsolutePath() + File.separator + includes[i++]);
			if (!includeFile.exists()) {
				if (!includeFile.getParentFile().exists()) {
					includeFile.getParentFile().mkdirs();
				}
				includeFile.createNewFile();
			}
			XmlWriter includeWriter = new XmlWriter(includeFile, ROOT_TAG_NAME);
			Element startElement = includeWriter.appendElementToRoot(d.getDataID());
			XMLNodeConfigurable rootConfigurable = new XMLNodeConfigurable(startElement);
			d.exportToConfigurable(rootConfigurable);

			includeWriter.writeToFile(0);
		}
	}

}
