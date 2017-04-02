package com.kaylerrenslow.armaDialogCreator.arma.header.impl;

import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderAssignment;
import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 @author Kayler
 @since 03/31/2017 */
public class HeaderClassImpl implements HeaderClass {

	private final List<HeaderAssignment> assignments = new ArrayList<>();
	private final List<HeaderClass> nestedClasses = new ArrayList<>();
	private String className = "~~No class name~~";
	private String extendClassName = null;

	public HeaderClassImpl(@NotNull String className) {
		this.className = className;
	}

	public HeaderClassImpl() {
	}

	public void setClassName(@NotNull String className) {
		this.className = className;
	}

	public void setExtendClassName(@Nullable String className) {
		this.extendClassName = className;
	}

	@NotNull
	@Override
	public List<HeaderAssignment> getAssignments() {
		return assignments;
	}

	@NotNull
	@Override
	public List<HeaderClass> getNestedClasses() {
		return nestedClasses;
	}

	@NotNull
	@Override
	public String getClassName() {
		return className;
	}

	@Nullable
	@Override
	public String getExtendClassName() {
		return extendClassName;
	}
}
