package com.kaylerrenslow.armaDialogCreator.data;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 @author Kayler
 Used to create a link to a resource outside the Project path
 Created on 07/19/2016.
 */
public class ExternalResource {
	private File externalPath;
	
	/** An ExternalResource is something that is referenced in the Project, but the actual file isn't inside the Project folder.
	 @param externalPath path of the external resource
	 */
	public ExternalResource(@NotNull File externalPath) {
		this.externalPath = externalPath;
	}
	
	@NotNull
	public final File getExternalPath() {
		return externalPath;
	}
	
	public final void setExternalPath(@NotNull File externalPath) {
		this.externalPath = externalPath;
	}
	
}
