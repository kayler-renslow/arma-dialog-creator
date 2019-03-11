package com.armadialogcreator.application;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 @author K
 @since 3/10/19 */
public abstract class ADCFile {

	/**
	 @return a String for a path that identifies the type of the path for use in saving to .adc files
	 @see ADCFile#toADCFile(String)
	 */
	@NotNull
	public abstract String getSpecialPath();

	/** @return the relative path to whatever this file is anchored to */
	@NotNull
	public abstract String getPath();

	/** @return a File instance for this path such that {@link File#getAbsolutePath()} resolves to the correct file */
	@NotNull
	public abstract File toFile();

	/** @return true if this file exists, false if it doesn't */
	public abstract boolean exists();

	/** Creates a new file */
	public abstract void createNewFile() throws IOException;

	/** Creates a new directory and creates all parent directories */
	public abstract void mkDir();

	private ADCFile() {
	}

	/** @see #getSpecialPath() */
	@NotNull
	public static ADCFile toADCFile(@NotNull String specialPath) {
		if (specialPath.charAt(0) != '$') {
			throw new IllegalArgumentException();
		}
		int prefixLen = 0;
		for (int i = 1; i < specialPath.length(); i++) {
			char c = specialPath.charAt(i);
			if (c == '$') {
				break;
			}
			prefixLen++;
		}
		String prefix = specialPath.substring(0, prefixLen);
		final String relPath = specialPath.substring(prefixLen + 1);
		switch (prefix) {
			case ApplicationPathFile.PREFIX: {
				return new ApplicationPathFile(relPath);
			}
			case WorkspacePathFile.PREFIX: {
				return new WorkspacePathFile(relPath);
			}
			case ProjectPathFile.PREFIX: {
				return new ProjectPathFile(relPath);
			}
			case AbsFile.PREFIX: {
				return new AbsFile(relPath);
			}
			case JarFile.PREFIX: {
				return new JarFile(relPath);
			}
		}
		throw new IllegalArgumentException();
	}

	/**
	 For files relative to the application save directory
	 ({@link ApplicationManager#getFileInApplicationDirectory(String)})

	 @author K
	 @since 3/10/19
	 */
	private static class ApplicationPathFile extends ADCFile {
		private final String relativePath;

		public static final String PREFIX = "$ADC_APP$";

		public ApplicationPathFile(@NotNull File target) {
			Path dir = ApplicationManager.getApplicationDirectory().toPath();
			this.relativePath = target.toPath().relativize(dir).toString();
		}

		public ApplicationPathFile(@NotNull String relativePath) {
			this.relativePath = relativePath;
		}

		@Override
		@NotNull
		public String getSpecialPath() {
			return PREFIX + relativePath;
		}

		@Override
		@NotNull
		public String getPath() {
			return relativePath;
		}

		@Override
		@NotNull
		public File toFile() {
			return ApplicationManager.getFileInApplicationDirectory(relativePath);
		}

		@Override
		public boolean exists() {
			return toFile().exists();
		}

		@Override
		public void createNewFile() throws IOException {
			toFile().createNewFile();
		}

		@Override
		public void mkDir() {
			toFile().mkdirs();
		}
	}

	/**
	 For files relative to the Workspace save directory
	 ({@link Workspace#getFileForName(String)} )

	 @author K
	 @since 3/10/19
	 */
	private static class WorkspacePathFile extends ADCFile {
		public static final String PREFIX = "$WORKSPACE$";

		private final String relativePath;

		public WorkspacePathFile(@NotNull File target) {
			Path dir = Workspace.getWorkspace().getWorkspaceDirectory().toPath();
			this.relativePath = target.toPath().relativize(dir).toString();
		}

		public WorkspacePathFile(@NotNull String relativePath) {
			this.relativePath = relativePath;
		}

		@Override
		@NotNull
		public String getPath() {
			return relativePath;
		}

		@Override
		@NotNull
		public File toFile() {
			return Workspace.getWorkspace().getFileForName(relativePath);
		}

		@Override
		public boolean exists() {
			return toFile().exists();
		}

		@Override
		public void createNewFile() throws IOException {
			toFile().createNewFile();
		}

		@Override
		public void mkDir() {
			toFile().mkdirs();
		}

		@Override
		@NotNull
		public String getSpecialPath() {
			return PREFIX + relativePath;
		}
	}

	/**
	 For files relative to the project save directory
	 ({@link Project#getFileForName(String)} )

	 @author K
	 @since 3/10/19
	 */
	private static class ProjectPathFile extends ADCFile {
		public static final String PREFIX = "$PROJECT$";

		private final String relativePath;

		public ProjectPathFile(@NotNull File target) {
			Path dir = Project.getCurrentProject().getProjectSaveDirectory().toPath();
			this.relativePath = target.toPath().relativize(dir).toString();
		}

		public ProjectPathFile(@NotNull String relativePath) {
			this.relativePath = relativePath;
		}

		@Override
		@NotNull
		public String getSpecialPath() {
			return PREFIX + relativePath;
		}

		@Override
		@NotNull
		public String getPath() {
			return relativePath;
		}

		@Override
		@NotNull
		public File toFile() {
			return Project.getCurrentProject().getFileForName(relativePath);
		}

		@Override
		public boolean exists() {
			return toFile().exists();
		}

		@Override
		public void createNewFile() throws IOException {
			toFile().createNewFile();
		}

		@Override
		public void mkDir() {
			toFile().mkdirs();
		}
	}

	/**
	 Wrapper for {@link File}

	 @author K
	 @since 3/10/19
	 */
	private static class AbsFile extends ADCFile {
		public static final String PREFIX = "$FILE$";

		private final File f;

		public AbsFile(@NotNull String path) {
			this.f = new File(path);
		}

		public AbsFile(@NotNull File f) {
			this.f = f;
		}

		@Override
		@NotNull
		public File toFile() {
			return f;
		}

		@Override
		public boolean exists() {
			return f.exists();
		}

		@Override
		public void createNewFile() throws IOException {
			f.createNewFile();
		}

		@Override
		public void mkDir() {
			f.mkdirs();
		}

		@Override
		@NotNull
		public String getSpecialPath() {
			return PREFIX + f.getPath();
		}

		@Override
		@NotNull
		public String getPath() {
			return f.getPath();
		}
	}

	/**
	 For files inside a jar in the classpath (something like /com/armadialogcreator/..)

	 @author K
	 @since 3/10/19
	 */
	private static class JarFile extends ADCFile {
		public static final String PREFIX = "$JAR$";

		private final String path;

		public JarFile(@NotNull String path) {
			if (path.charAt(0) != '/') {
				throw new IllegalArgumentException();
			}
			this.path = path;
		}

		@Override
		@NotNull
		public String getSpecialPath() {
			return PREFIX + path;
		}

		@Override
		@NotNull
		public String getPath() {
			return path;
		}

		@Override
		@NotNull
		public File toFile() {
			return new File(path);
		}

		@Override
		public boolean exists() {
			InputStream is = getClass().getResourceAsStream(path);
			boolean exists = is != null;
			try {
				is.close();
			} catch (IOException ignore) {

			}
			return exists;
		}

		@Override
		public void createNewFile() throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void mkDir() {
			throw new UnsupportedOperationException();
		}
	}
}
