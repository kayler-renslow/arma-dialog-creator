package com.kaylerrenslow.armaDialogCreator.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 A simple class to specify a file path. For example: file\file1.txt or ../hello.txt

 @author Kayler
 @since 05/01/2016. */
public class FilePath {

	/**
	 Create a file path with the given root name and children names

	 @param fileName root file name
	 @param childFileNames children names in order, or null if no children exist
	 @return a new FilePath instance
	 */
	@NotNull
	public static FilePath createFilePath(@NotNull String fileName, @Nullable String... childFileNames) {
		FilePath root = new FilePath(fileName, null);
		FilePath cursor = root;
		if (childFileNames != null) {
			for (String file : childFileNames) {
				if (file == null) {
					throw new IllegalArgumentException("child file name can't be null");
				}
				cursor.child = new FilePath(file, null);
				cursor = cursor.child;
			}
		}
		return root;
	}

	/**
	 Create a file path from an existing string.

	 @param path the file path (e.g. "hello\text.txt")
	 @return a new FilePath instance
	 */
	@NotNull
	public static FilePath parseFilePath(@NotNull String path) {
		String[] tokens = path.split("[\\\\|/]");

		String[] children = null;
		if (tokens.length > 1) {
			children = new String[tokens.length - 1];
			System.arraycopy(tokens, 1, children, 0, tokens.length - 1);
		}

		return createFilePath(tokens[0], children);
	}

	/** @see #findFileByPath(FilePath, File) */
	@Nullable
	public static File findFileByPath(@NotNull String filePath, @NotNull File rootDirectory) {
		return findFileByPath(parseFilePath(filePath), rootDirectory);
	}

	/**
	 Finds the file (specified by filePath) inside the given root directory. The search does not include the root directory itself.
	 For instance, filePath could be equal to root directory ("exampleRootName" and return null)

	 @param filePath FilePath object
	 @param rootDirectory the root directory to begin the search
	 @return the File that was found, or null if the given file path points to nothing at the given root directory
	 */
	@Nullable
	public static File findFileByPath(@NotNull FilePath filePath, @NotNull File rootDirectory) {
		if (!rootDirectory.isDirectory()) {
			throw new IllegalArgumentException("rootDirectory is not a directory");
		}
		File currentRootDirectory = rootDirectory;
		if (filePath.getFileName().matches("[a-zA-Z]+:")) { //starts with drive. example  d:/file/file2.txt
			return new File(filePath.getFullPath('/'));
		} else {
			while (filePath.fileNameIsDotDot()) {
				if (currentRootDirectory.getParent() == null) {
					return null;
				}
				currentRootDirectory = currentRootDirectory.getParentFile();
				filePath = filePath.getChild();
				if (filePath == null) { //just simply a ../
					return currentRootDirectory;
				}
			}
		}

		//find sub directory
		@NotNull FilePath finalFilePath = filePath;
		File[] files = currentRootDirectory.listFiles(f -> f.getName().equals(finalFilePath.getFileName()));

		if (files == null || files.length == 0) {
			return null;
		}

		File matched = files[0];
		if (matched.isDirectory()) {
			if (filePath.getChild() == null) {
				//no children left to traverse
				return matched;
			} else {
				//children to traverse
				return findFileByPath(filePath.getChild(), matched);
			}
		} else {
			//current is not a directory
			if (filePath.getChild() == null) {
				//is the end path
				return matched;
			}
		}
		return null;

	}

	private final String fileName;

	private FilePath child;

	/**
	 Create a new file path

	 @param fileName root file name
	 @param child child file path (or null if none exists)
	 */
	public FilePath(@NotNull String fileName, @Nullable FilePath child) {
		this.fileName = fileName;
		this.child = child;
	}

	/**
	 Print this file path. Example output: test/test.txt

	 @param directoryDelimiter delimiter to separate file names (e.g. '/')
	 @return String with path.
	 */
	@NotNull
	public String getFullPath(char directoryDelimiter) {
		return this.fileName + (this.child != null ? directoryDelimiter + this.child.getFullPath(directoryDelimiter) : "");
	}

	@NotNull
	public String getFileName() {
		return fileName;
	}

	public boolean fileNameIsDotDot() {
		return this.fileName.equals("..");
	}

	@Nullable
	public FilePath getChild() {
		return child;
	}

	@Override
	public String toString() {
		return getFullPath('/');
	}
}
