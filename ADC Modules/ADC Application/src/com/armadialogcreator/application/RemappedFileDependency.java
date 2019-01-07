package com.armadialogcreator.application;

import com.armadialogcreator.util.KeyValueString;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 An {@link RemappedFileDependency} is a type of {@link ExportableFileDependency} where a dependency is placed on a new
 location of the file (either a duplicate or a converted version),
 but the original path to the file is preserved in {@link #getOriginalFile()}

 @author Kayler
 @since 1/6/2019. */
public class RemappedFileDependency extends FileDependency {
	private final File originalFile;
	private @Nullable String exportedPath;

	public RemappedFileDependency(@NotNull File originalFile, @NotNull File remappedFile, @NotNull KeyValueString[] properties) {
		super(remappedFile, properties);
		this.originalFile = originalFile;
	}

	public RemappedFileDependency(@NotNull File originalFile, @NotNull File remappedFile) {
		super(remappedFile);
		this.originalFile = originalFile;
	}

	/** @return original file path */
	@NotNull
	public File getOriginalFile() {
		return originalFile;
	}

	/** @return file path to the converted/duplicate file that isn't the original file */
	@Override
	public @NotNull NotNullValueObserver<File> getExternalFileObserver() {
		return super.getExternalFileObserver();
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
	 or if it was never invoked then {@link File#getAbsolutePath()} is returned on the {@link #getOriginalFile()}
	 @see #getOriginalFile()
	 */
	@NotNull
	public String getExportedPath() {
		if (exportedPath == null) {
			return getOriginalFile().getAbsolutePath();
		}
		return exportedPath;
	}

	/** @return shortcut for getting value from{@link #getExternalFileObserver()} */
	@Override
	@NotNull
	public File getFile() {
		return super.getFile();
	}
}
