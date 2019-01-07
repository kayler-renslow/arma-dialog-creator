package com.armadialogcreator.data;

import com.armadialogcreator.util.KeyValueString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 An {@link ExportableFileDependency} is a type of {@link FileDependency} where you can specify a path
 for the dependency such that the dependency path is still valid after export, but isn't just {@link File#getAbsolutePath()}

 @author Kayler
 @since 07/19/2016. */
public class ExportableFileDependency extends FileDependency {
	private @Nullable String exportedPath;

	public ExportableFileDependency(@NotNull File resourceFile, @NotNull KeyValueString[] properties) {
		super(resourceFile, properties);
	}

	public ExportableFileDependency(@NotNull File resourceFile) {
		super(resourceFile);
	}

	/**
	 Set the exported path, or null to just use {@link File#getAbsolutePath()}

	 @param exportedPath path
	 @see #getExternalFileObserver()
	 */
	public void setExportedPath(@Nullable String exportedPath) {
		this.exportedPath = exportedPath;
	}

	/**
	 @return the exported path specified from {@link #setExportedPath(String)},
	 or if it was never invoked then {@link File#getAbsolutePath()} is returned
	 @see #getExternalFileObserver()
	 */
	@NotNull
	public String getExportedPath() {
		if (exportedPath == null) {
			return getExternalFileObserver().getValue().getAbsolutePath();
		}
		return exportedPath;
	}
}
