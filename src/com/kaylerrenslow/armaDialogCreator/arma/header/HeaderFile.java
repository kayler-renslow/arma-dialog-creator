package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

/**
 @author Kayler
 @since 03/19/2017 */
public class HeaderFile {
	private final File file;
	private List<HeaderAssignment> assignments;
	private List<HeaderClass> classes;

	protected HeaderFile(@NotNull File file, @NotNull List<HeaderAssignment> assignments, @NotNull List<HeaderClass> classes) {
		this.file = file;
		this.assignments = assignments;
		this.classes = classes;
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
