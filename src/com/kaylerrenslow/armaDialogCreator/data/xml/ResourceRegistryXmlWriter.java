package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.data.ExternalResource;
import com.kaylerrenslow.armaDialogCreator.data.ResourceRegistry;
import com.kaylerrenslow.armaDialogCreator.data.Workspace;
import com.kaylerrenslow.armaDialogCreator.data.WorkspaceResourceRegistry;
import com.kaylerrenslow.armaDialogCreator.util.KeyValueString;
import com.kaylerrenslow.armaDialogCreator.util.XmlWriter;
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
		 Writes the {@link WorkspaceResourceRegistry} to file

		 @param workspace the {@link Workspace} to write resources for
		 @throws TransformerException when the XML couldn't be written
		 */
		public static void writeAndClose(@NotNull Workspace workspace) throws TransformerException {
			new WorkspaceResourceRegistryXmlWriter().doWriteAndClose(workspace);
		}

		private void doWriteAndClose(@NotNull Workspace workspace) throws TransformerException {
			WorkspaceResourceRegistry registry = workspace.getGlobalResourceRegistry();
			if (!registry.getResourcesFile().exists()) {
				boolean made = registry.getResourcesFile().getParentFile().mkdirs();
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
	 Writes the given {@link ResourceRegistry} to the provided XML element. This will not write anything to file

	 @param resourceRegistry the registry
	 @param writer the writer
	 @param writeToEle element to write to
	 */
	public static void write(@NotNull ResourceRegistry resourceRegistry, @NotNull XmlWriter writer, @NotNull Element writeToEle) {
		Element externalResourcesEle = writer.appendElementToElement("external-resources", writeToEle);
		for (ExternalResource resource : resourceRegistry.getResourceList()) {

			Element externalResourceEle = writer.appendElementToElement("external-resource", externalResourcesEle);
			writer.appendTextNode(resource.getExternalFile().getPath(), externalResourceEle);
			for (KeyValueString keyValue : resource.getProperties()) {
				Element resourcePropertyEle = writer.appendElementToElement("resource-property", externalResourceEle);
				resourcePropertyEle.setAttribute("key", keyValue.getKey());
				writer.appendTextNode(keyValue.getValue(), resourcePropertyEle);
			}
		}
	}
}
