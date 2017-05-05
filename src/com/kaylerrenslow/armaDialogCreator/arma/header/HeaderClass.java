package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.function.Function;

/**
 @author Kayler
 @since 03/19/2017 */
public interface HeaderClass extends HeaderItem {
	@NotNull HeaderAssignmentList getAssignments();

	@NotNull HeaderClassList getNestedClasses();

	@NotNull String getClassName();

	/**
	 Check if {@link #getClassName()} is equal to <code>className</code>

	 @param className name to check
	 @param caseSensitive true if case sensitivity matters
	 @return true if they are equal
	 */
	default boolean classNameEquals(@NotNull String className, boolean caseSensitive) {
		if (caseSensitive) {
			return getClassName().equals(className);
		}
		return getClassName().equalsIgnoreCase(className);
	}

	@Nullable String getExtendClassName();

	/**
	 Check if {@link #getExtendClassName()} is equal to <code>className</code>

	 @param className name to check
	 @param caseSensitive true if case sensitivity matters
	 @return true if they are equal <b>and both class names aren't null</b>
	 */
	default boolean extendClassNameEquals(@Nullable String className, boolean caseSensitive) {
		if (getExtendClassName() == null) {
			return false;
		}
		if (className == null) {
			return false;
		}
		if (caseSensitive) {
			return getExtendClassName().equals(className);
		}
		return getExtendClassName().equalsIgnoreCase(className);
	}

	/**
	 @return the parent {@link HeaderClass}, or null if has no parent class (likely inside {@link HeaderFile#getClasses()})
	 */
	@Nullable HeaderClass getParentClass();

	@NotNull HeaderFile getOwnerFile();

	/**
	 An iterative way to traverse upwards the inheritance tree. This method will include the starting class in the callback

	 @param classTraverseCallback function invoked on each parent class. Return true if traversal should continue, false if it should stop
	 @return the last traversed {@link HeaderClass}
	 */
	@NotNull
	default HeaderClass traverseUpwards(@NotNull Function<HeaderClass, Boolean> classTraverseCallback) {
		HeaderClass cursor = this;
		HeaderClass discovered = cursor;
		boolean contTraversal = true;
		while (cursor != null && contTraversal) {
			discovered = cursor;
			cursor = cursor.getParentClass();
			contTraversal = classTraverseCallback.apply(discovered);
		}
		return discovered;
	}

	/**
	 An iterative way to traverse downwards the inheritance tree. This method will include the starting class in the callback.
	 The algorithm is iterative BFS

	 @param classTraverseCallback function invoked on each descendant class. Return true if traversal should continue, false if it should stop
	 @return the last traversed {@link HeaderClass}
	 */
	@NotNull
	default HeaderClass traverseDownwards(@NotNull Function<HeaderClass, Boolean> classTraverseCallback) {
		HeaderClass discovered = this;
		boolean contTraversal = true;
		LinkedList<HeaderClass> toVisit = new LinkedList<>();
		toVisit.add(this);
		while (contTraversal && !toVisit.isEmpty()) {
			discovered = toVisit.pop();
			discovered.getNestedClasses().addAllInto(toVisit);
			contTraversal = classTraverseCallback.apply(discovered);
		}
		return discovered;
	}

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
