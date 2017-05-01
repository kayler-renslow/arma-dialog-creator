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
	private List<HeaderAssignment> assignmentsMutable = new LinkedList<>();
	private HeaderAssignmentList assignments = new HeaderAssignmentList(assignmentsMutable);
	private List<HeaderClass> classesMutable = new LinkedList<>();
	private HeaderClassList classes = new HeaderClassList(classesMutable);

	protected HeaderFile(@NotNull File file) {
		this.file = file;
	}

	@NotNull
	public File getFile() {
		return file;
	}

	@NotNull
	protected List<HeaderClass> getClassesMutable() {
		return classesMutable;
	}

	@NotNull
	public List<HeaderAssignment> getAssignmentsMutable() {
		return assignmentsMutable;
	}

	@NotNull
	public HeaderAssignmentList getAssignments() {
		return assignments;
	}


	@NotNull
	public HeaderClassList getClasses() {
		return classes;
	}
}
