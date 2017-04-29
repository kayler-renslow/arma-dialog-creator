package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 @author Kayler
 @since 03/19/2017 */
public interface HeaderClass {
	@NotNull List<HeaderAssignment> getAssignments();

	@NotNull List<HeaderClass> getNestedClasses();

	@NotNull String getClassName();

	@Nullable String getExtendClassName();

	default boolean equalsClass(@NotNull HeaderClass o) {
		if (o == this) {
			return true;
		}
		if (!getAssignments().equals(o.getAssignments())) {
			return false;
		}
		if (!getNestedClasses().equals(o.getNestedClasses())) {
			return false;
		}
		if (!getClassName().equals(o.getClassName())) {
			return false;
		}

		if (getExtendClassName() == null) {
			return o.getExtendClassName() == null;
		}

		return getExtendClassName().equals(o.getExtendClassName());
	}
}
