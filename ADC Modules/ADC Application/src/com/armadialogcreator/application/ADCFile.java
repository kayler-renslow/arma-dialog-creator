package com.armadialogcreator.application;

import com.armadialogcreator.util.UTF8FileReader;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 @author K
 @since 3/10/19 */
public abstract class ADCFile {

	public enum FileType {
		Application(ApplicationPathFile.PREFIX),
		Workspace(WorkspacePathFile.PREFIX),
		Project(ProjectPathFile.PREFIX),
		Absolute(ApplicationPathFile.PREFIX),
		Jar(JarFile.PREFIX);

		private String prefix;

		FileType(String prefix) {
			this.prefix = prefix;
		}

		@NotNull
		public static FileType matchByPrefix(@NotNull String prefix) {
			for (FileType type : values()) {
				if (type.prefix.equals(prefix)) {
					return type;
				}
			}
			throw new IllegalArgumentException();
		}

	}


	/**
	 @return a String for a path that identifies the type of the path for use in saving to .adc files
	 @see ADCFile#toADCFile(String)
	 */
	@NotNull
	public abstract String getSpecialPath();

	/** @return the relative path to whatever this file is anchored to */
	@NotNull
	public abstract String getRelPath();

	/** @return a File instance for this path such that {@link File#getAbsolutePath()} resolves to the correct file */
	@NotNull
	public abstract File toFile();

	/** @return true if this file exists, false if it doesn't */
	public abstract boolean exists();

	/** Creates a new file */
	public abstract void createNewFile() throws IOException;

	/** Creates a new directory and creates all parent directories */
	public abstract void mkDir();

	/** @return a reader for this file */
	@NotNull
	public abstract Reader newReader() throws IOException;

	@NotNull
	public abstract ADCFile getFileInOwnerDirectory(@NotNull String name);

	private ADCFile() {
	}

	/** Gets an {@link ADCFile} instance by relativizing the given {@link File} */
	@NotNull
	public static ADCFile toADCFile(@NotNull FileType type, @NotNull File file) {
		switch (type) {
			case Application: {
				return new ApplicationPathFile(file);
			}
			case Workspace: {
				return new WorkspacePathFile(file);
			}
			case Project: {
				return new ProjectPathFile(file);
			}
			case Absolute: {
				return new AbsFile(file);
			}
			case Jar: {
				throw new UnsupportedOperationException();
			}
		}
		throw new IllegalArgumentException();
	}

	/** @see #getSpecialPath() */
	@NotNull
	public static ADCFile toADCFile(@NotNull FileType type, @NotNull String path) {
		switch (type) {
			case Application: {
				return new ApplicationPathFile(path);
			}
			case Workspace: {
				return new WorkspacePathFile(path);
			}
			case Project: {
				return new ProjectPathFile(path);
			}
			case Absolute: {
				return new AbsFile(path);
			}
			case Jar: {
				return new JarFile(path);
			}
		}
		throw new IllegalArgumentException();
	}

	@NotNull
	public static ADCFile toADCJarFile(@NotNull String path, @NotNull String ownerModule) {
		return new JarFile(path, ownerModule);
	}

	/** @see #getSpecialPath() */
	@NotNull
	public static ADCFile toADCFile(@NotNull String path) {
		if (path.charAt(0) != '$') {
			throw new IllegalArgumentException();
		}
		int prefixLen = 0;
		for (int i = 1; i < path.length(); i++) {
			char c = path.charAt(i);
			prefixLen++;
			if (c == '$') {
				break;
			}
		}
		String prefix = path.substring(0, prefixLen + 1);
		final String relPath = path.substring(prefixLen + 1);
		return toADCFile(FileType.matchByPrefix(prefix), relPath);
	}

	@NotNull
	public static ADCFile toADCFile(@NotNull File file) {
		return new ADCFile.AbsFile(file);
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
		public String getRelPath() {
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

		@Override
		@NotNull
		public Reader newReader() throws IOException {
			return new UTF8FileReader(toFile());
		}

		@Override
		@NotNull
		public ADCFile getFileInOwnerDirectory(@NotNull String name) {
			return new ApplicationPathFile(toFile().getParentFile().getAbsolutePath() + File.separator + name);
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
		public String getRelPath() {
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
		public Reader newReader() throws IOException {
			return new UTF8FileReader(toFile());
		}

		@Override
		@NotNull
		public String getSpecialPath() {
			return PREFIX + relativePath;
		}

		@Override
		@NotNull
		public ADCFile getFileInOwnerDirectory(@NotNull String name) {
			return new WorkspacePathFile(toFile().getParentFile().getAbsolutePath() + File.separator + name);
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
		public String getRelPath() {
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

		@Override
		@NotNull
		public Reader newReader() throws IOException {
			return new UTF8FileReader(toFile());
		}

		@Override
		@NotNull
		public ADCFile getFileInOwnerDirectory(@NotNull String name) {
			return new ProjectPathFile(toFile().getParentFile().getAbsolutePath() + File.separator + name);
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
		public Reader newReader() throws IOException {
			return new UTF8FileReader(toFile());
		}

		@Override
		@NotNull
		public String getSpecialPath() {
			return PREFIX + f.getPath();
		}

		@Override
		@NotNull
		public String getRelPath() {
			return f.getPath();
		}

		@Override
		@NotNull
		public ADCFile getFileInOwnerDirectory(@NotNull String name) {
			return new AbsFile(toFile().getParentFile().getAbsolutePath() + File.separator + name);
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
		private final String ownerModule;

		public JarFile(@NotNull String path) {
			if (path.charAt(0) != '$') {
				throw new IllegalArgumentException();
			}
			this.ownerModule = path.substring(1, path.lastIndexOf('$'));
			this.path = path.substring(path.lastIndexOf('$') + 1);
		}

		public JarFile(@NotNull String path, @NotNull String ownerModule) {
			this.ownerModule = ownerModule;
			if (path.charAt(0) != '/') {
				throw new IllegalArgumentException();
			}
			this.path = path;
		}

		@Override
		@NotNull
		public String getSpecialPath() {
			return PREFIX + '$' + ownerModule + '$' + path;
		}

		@Override
		@NotNull
		public String getRelPath() {
			return path;
		}

		@Override
		@NotNull
		public File toFile() {
			return new File(path);
		}

		@Override
		public boolean exists() {
			InputStream is = getStream();
			boolean exists = is != null;
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException ignore) {

			}
			return exists;
		}

		private InputStream getStream() {
			InputStream[] is = {null};
			ModuleLayer.boot().findModule(ownerModule).ifPresent(module -> {
				try {
					is[0] = module.getResourceAsStream(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			return is[0];
		}

		@Override
		public void createNewFile() throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void mkDir() {
			throw new UnsupportedOperationException();
		}

		@Override
		@NotNull
		public Reader newReader() throws IOException {
			return new InputStreamReader(getStream(), StandardCharsets.UTF_8);
		}

		@Override
		@NotNull
		public ADCFile getFileInOwnerDirectory(@NotNull String name) {
			String parentPath = path.substring(0, path.lastIndexOf('/'));
			return new JarFile(parentPath + '/' + name, ownerModule);
		}
	}
}
