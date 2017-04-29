package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 @author Kayler
 @since 03/19/2017 */
public class HeaderFile {
	private final File file;
	private List<HeaderAssignment> assignments = new LinkedList<>();
	private List<HeaderClass> classes = new LinkedList<>();

	protected HeaderFile(@NotNull File file) {
		this.file = file;
	}

	@NotNull
	public File getFile() {
		return file;
	}

	@NotNull
	public List<HeaderAssignment> getAssignments() {
		return assignments;
	}

	@NotNull
	public List<HeaderClass> getClasses() {
		return classes;
	}
}
