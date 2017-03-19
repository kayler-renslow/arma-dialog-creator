package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 @author Kayler
 @since 03/19/2017 */
public interface HeaderClass {
	@NotNull List<HeaderAssignment> getAssignments();

	@NotNull List<HeaderClass> getNestedClasses();
}
