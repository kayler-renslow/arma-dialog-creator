package com.armadialogcreator.data.xml;

import com.armadialogcreator.application.FileDependency;
import com.armadialogcreator.application.FileDependencyRegistry;
import com.armadialogcreator.application.Workspace;
import com.armadialogcreator.application.WorkspaceFileDependencyRegistry;
import com.armadialogcreator.util.KeyValueString;
import com.armadialogcreator.util.XmlWriter;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 Created by Kayler on 09/08/2016.
 */
public class ResourceRegistryXmlWriter {

	public static class WorkspaceResourceRegistryXmlWriter extends ResourceRegistryXmlWriter {

		/**
		 Writes the {@link WorkspaceFileDependencyRegistry} to file

		 @param workspace the {@link Workspace} to write resources for
		 @throws TransformerException when the XML couldn't be written
		 */
		public static void writeAndClose(@NotNull Workspace workspace) throws TransformerException {
			new WorkspaceResourceRegistryXmlWriter().doWriteAndClose(workspace);
		}

		private void doWriteAndClose(@NotNull Workspace workspace) throws TransformerException {
			WorkspaceFileDependencyRegistry registry = workspace.getWorkspaceFileDependencyRegistry();
			if (!registry.getResourcesFile().exists()) {
				boolean made = registry.getResourcesDirectory().mkdirs();
				if (!made) {
					throw new TransformerException("Couldn't create parent directories for resource file");
				}
				try {
					boolean created = registry.getResourcesFile().createNewFile();
					if (!created) {
						throw new TransformerException("Couldn't create parent resource file");
					}
				} catch (IOException e) {
					throw new TransformerException(e);
				}
			}
			if (registry.getResourcesFile().isDirectory()) {
				throw new RuntimeException("registry xml file is a directory");
			}

			XmlWriter writer = new XmlWriter(registry.getResourcesFile(), "external-resources");
			write(registry, writer, writer.getRootElement());
			writer.writeToFile(-1);
		}
	}

	/**
	 Writes the given {@link FileDependencyRegistry} to the provided XML element. This will not write anything to file

	 @param fileDependencyRegistry the registry
	 @param writer the writer
	 @param writeToEle element to write to
	 */
	public static void write(@NotNull FileDependencyRegistry fileDependencyRegistry, @NotNull XmlWriter writer, @NotNull Element writeToEle) {
		Element externalResourcesEle = writer.appendElementToElement("external-resources", writeToEle);
		for (FileDependency resource : fileDependencyRegistry.getDependencyList()) {

			Element externalResourceEle = writer.appendElementToElement("external-resource", externalResourcesEle);
			writer.appendTextNode(resource.getExternalFileObserver().getValue().getPath(), externalResourceEle);
			for (KeyValueString keyValue : resource.getProperties()) {
				Element resourcePropertyEle = writer.appendElementToElement("resource-property", externalResourceEle);
				resourcePropertyEle.setAttribute("key", keyValue.getKey());
				writer.appendTextNode(keyValue.getValue(), resourcePropertyEle);
			}
		}
	}
}
