package com.armadialogcreator.data;

import com.armadialogcreator.util.KeyValueString;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;

/**
 A {@link FileDependency} used for handling files that are referenced in the application,
 but isn't related to any ADC files.
 <p>
 This class supporting remapping files.
 Suppose you have a dependency on a file (thing.paa), but ADC is also using thing.paa.png because it which is thing.paa in a png format.
 The FileDependency would then be structured such that the {@link #getOriginalFile()} is thing.paa, but {@link #getRemappedFile()} is thing.paa.png.
 The file thing.paa.png may be deleted, but thing.paa is preserved.
 Therefore, the file dependency for thing.paa is still in tact, but the remapped version doesn't exist.
 We can then just re-remap thing.paa to another location.
 This remapping gives some versatility for handling temporary, cached, or otherwise optimized versions
 of a file that ADC will use internally, but isn't used by the user.
 <p>
 This class also supports setting a custom export path via {@link #setExportedPath(String)}.
 By default, {@link #getExportedPath()} will be {@link #getRemappedFile()}->{@link File#getAbsolutePath()}.

 @author Kayler
 @since 07/19/2016. */
public class FileDependency {
	private KeyValueString[] properties;
	private final NotNullValueObserver<File> originalFile;
	private final NotNullValueObserver<File> remappedFile;
	private @Nullable String exportedPath;

	/**
	 @param originalFile originalFile
	 @param properties other data to save
	 */
	public FileDependency(@NotNull File originalFile, @NotNull KeyValueString[] properties) {
		this.originalFile = new NotNullValueObserver<>(originalFile);
		remappedFile = new NotNullValueObserver<>(originalFile);
		this.properties = properties;
	}

	/**
	 @param resourceFile file
	 */
	public FileDependency(@NotNull File resourceFile) {
		this(resourceFile, KeyValueString.EMPTY);
	}

	protected final void setProperties(@NotNull KeyValueString[] properties) {
		this.properties = properties;
	}

	/** @return true if the {@link #getRemappedFile()} exists, return false if doesn't exist */
	public boolean fileExists() {
		return remappedFile.getValue().exists();
	}

	@Nullable
	public final String getPropertyValue(@NotNull String keyName) {
		for (KeyValueString keyValue : properties) {
			if (keyValue.getKey().equals(keyName)) {
				return keyValue.getValue();
			}
		}
		return null;
	}

	public final void setPropertyValue(@NotNull String keyName, @NotNull String value) {
		for (KeyValueString keyValue : properties) {
			if (keyValue.getKey().equals(keyName)) {
				keyValue.setValue(value);
				break;
			}
		}
	}

	@NotNull
	public KeyValueString[] getProperties() {
		return properties;
	}

	@NotNull
	public NotNullValueObserver<File> getOriginalFileObserver() {
		return originalFile;
	}

	/**
	 Gets the remapped file. If left unchanged, the remapped file is {@link #getOriginalFile()}.

	 @return remapped file
	 */
	@NotNull
	public NotNullValueObserver<File> getRemappedFileObserver() {
		return remappedFile;
	}

	/**
	 Shortcut for invoking {@link NotNullValueObserver#updateValue(Object)} on {@link #getRemappedFileObserver()}

	 @param f remapped file
	 */
	public void remapFile(@NotNull File f) {
		remappedFile.updateValue(f);
	}

	/** @return true if {@link #getRemappedFile()} is not equal to {@link #getOriginalFile()} */
	public boolean remappedIsDifferentFromOriginal() {
		return !remappedFile.getValue().equals(originalFile.getValue());
	}

	/**
	 Set the exported path, or null to just use {@link File#getAbsolutePath()} on {@link #getRemappedFile()}

	 @param exportedPath path
	 @see #getOriginalFileObserver()
	 @see #getRemappedFileObserver()
	 */
	public void setExportedPath(@Nullable String exportedPath) {
		this.exportedPath = exportedPath;
	}

	/**
	 @return the exported path specified from {@link #setExportedPath(String)},
	 or if it was never invoked then {@link File#getAbsolutePath()} is returned on the {@link #getRemappedFile()}
	 @see #getRemappedFile()
	 */
	@NotNull
	public String getExportedPath() {
		if (exportedPath == null) {
			return getRemappedFile().getAbsolutePath();
		}
		return exportedPath;
	}

	@Override
	public String toString() {
		return "FileDependency{" +
				"properties=" + Arrays.toString(properties) +
				", originalFile=" + originalFile + ", remappedFile=" + remappedFile +
				'}';
	}

	/** @return shortcut for getting values from {@link #getOriginalFileObserver()} */
	@NotNull
	public File getOriginalFile() {
		return originalFile.getValue();
	}

	/** @return shortcut for getting values from {@link #getOriginalFileObserver()} */
	@NotNull
	public File getRemappedFile() {
		return remappedFile.getValue();
	}
}
