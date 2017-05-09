package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.function.Function;

/**
 @see HeaderParser
 @see HeaderFile
 @author Kayler
 @since 03/19/2017 */
public interface HeaderClass extends HeaderItem {
	/** @return {@link HeaderAssignment} instances that are inside this {@link HeaderClass}. */
	@NotNull HeaderAssignmentList getAssignments();

	/** @return {@link HeaderClass} instances that are inside this {@link HeaderClass} */
	@NotNull HeaderClassList getNestedClasses();

	/** @return class name */
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

	/**
	 Get the name of the class that this class extends, or null if this class doesn't extend anything.

	 @return the class name, or null
	 @see HeaderFile#getExtendClass(HeaderClass, boolean)
	 */
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
	 Every class has a parent class, except the root class. The root class is used for populating {@link HeaderFile#getClasses()}
	 and {@link HeaderFile#getAssignments()}. The root class basically copies the references to the {@link HeaderFile}, however,
	 the root class still exists in the {@link HeaderClass} hierarchy and is reachable via this method.

	 @return the parent {@link HeaderClass}, or null if has no parent class and is the root class.
	 */
	@Nullable HeaderClass getParentClass();

	/**
	 Get the {@link HeaderFile} that owns this {@link HeaderClass}

	 @return owner
	 */
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
