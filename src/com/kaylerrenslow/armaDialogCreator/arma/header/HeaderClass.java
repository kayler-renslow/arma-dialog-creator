package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 @author Kayler
 @since 03/19/2017 */
public interface HeaderClass extends HeaderItem {
	@NotNull List<HeaderAssignment> getAssignments();

	@NotNull HeaderClassList getNestedClasses();

	@NotNull String getClassName();

	@Nullable String getExtendClassName();

	@Override
	@NotNull
	default String getAsString() {
		StringBuilder body = new StringBuilder();

		boolean nl = false;

		for (HeaderAssignment a : getAssignments()) {
			if (!nl) {
				body.append('\n');
				nl = true;
			}
			body.append(a.getAsString());
		}

		for (HeaderClass hc : getNestedClasses()) {
			if (!nl) {
				body.append('\n');
				nl = true;
			}
			body.append(hc.getAsString());
		}

		return String.format("class %s%s{%s}\n", getClassName(), getExtendClassName() == null ? "" : " : " + getExtendClassName(), body);
	}

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
