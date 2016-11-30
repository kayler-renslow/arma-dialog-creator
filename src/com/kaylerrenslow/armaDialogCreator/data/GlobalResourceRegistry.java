package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.data.io.xml.ResourceRegistryXmlWriter;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 @author Kayler
 @since 11/23/2016 */
public class GlobalResourceRegistry extends ResourceRegistry {

	protected GlobalResourceRegistry(@NotNull Workspace workspace) {
		super(workspace.getFileForName(ResourceRegistry.RESOURCES_FILE_NAME + "/global-resources.xml"));
		if (!getResourcesFile().exists()) {
			getResourcesFile().getParentFile().mkdirs();
			try {
				getResourcesFile().createNewFile();
				ResourceRegistryXmlWriter.GlobalResourceRegistryXmlWriter.writeAndClose(this);
			} catch (IOException e) {
				ExceptionHandler.error(e);
			}
		}
	}

	@NotNull
	public static GlobalResourceRegistry getInstance() {
		return Workspace.getWorkspace().getGlobalResourceRegistry();
	}

}
