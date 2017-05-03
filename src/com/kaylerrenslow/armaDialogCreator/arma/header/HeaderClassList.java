package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 An immutable list (the underlying list is not copied) of {@link HeaderClass} instances that provides extra functionality for searching

 @author Kayler
 @since 04/30/2017 */
public class HeaderClassList implements Iterable<HeaderClass> {
	private HeaderClass ownerClass;
	private final List<HeaderClass> classList;

	/**
	 Create a immutable list of {@link HeaderClass} instances (the underlying list is not copied)

	 @param ownerClass the class that uses this list, or null if used with a {@link HeaderFile}
	 @param classList the underlying list that actually holds that instances. This list is not copied.
	 */
	public HeaderClassList(@Nullable HeaderClass ownerClass, @NotNull List<HeaderClass> classList) {
		this.ownerClass = ownerClass;
		this.classList = classList;
	}

	/**
	 @return the class that owns this list
	 @throws IllegalStateException if the list doesn't have an owner
	 @see #hasOwnerClass() to check if there is an owner class
	 */
	@NotNull
	public HeaderClass getOwnerClass() {
		if (ownerClass == null) {
			throw new IllegalStateException("Can't get the parent class if there is none");
		}
		return ownerClass;
	}

	/**
	 @return true if this list is owned by a {@link HeaderClass}, false if owned by a {@link HeaderFile}
	 @see #getOwnerClass()
	 */
	public boolean hasOwnerClass() {
		return ownerClass != null;
	}

	/**
	 Get a {@link HeaderClass} by a class name

	 @param className the class name
	 @param caseSensitive true if the class name is case sensitive, false if not case sensitive
	 @return the {@link HeaderClass} instance that matches the class name, or null if no class was matched
	 */
	@Nullable
	public HeaderClass getByName(@NotNull String className, boolean caseSensitive) {
		for (HeaderClass hc : classList) {
			if (hc.classNameEquals(className, caseSensitive)) {
				return hc;
			}
		}
		return null;
	}

	/**
	 Finds a {@link HeaderClass} by iteratively searching through descendant {@link HeaderClassList} instances.
	 Searching is done iterative calls to {@link #getByName(String, boolean)}

	 @param caseSensitive true if the class names are case sensitive, false if not case senstive
	 @param classNames the array of class names. The last name is the class that will be returned (if it exists)
	 @return null if <code>classNames.length==0</code>, null if {@link HeaderClass} couldn't be matched, or the matched instance
	 */
	@Nullable
	public HeaderClass findClassByPath(boolean caseSensitive, @NotNull String... classNames) {
		if (classNames.length == 0) {
			return null;
		}
		HeaderClass cursor = getByName(classNames[0], caseSensitive);
		int cursorI = 1;
		while (cursor != null && cursorI < classNames.length) {
			cursor = cursor.getNestedClasses().getByName(classNames[cursorI++], caseSensitive);
		}
		return cursor;
	}

	/**
	 @return size of the list
	 */
	public int size() {
		return classList.size();
	}

	@NotNull
	@Override
	public Iterator<HeaderClass> iterator() {
		return classList.iterator();
	}

	/**
	 Adds all of this list into a collection

	 @param addTo collection to add to
	 */
	public void addAllInto(@NotNull Collection<HeaderClass> addTo) {
		for (HeaderClass hc : this) {
			addTo.add(hc);
		}
	}

	@Override
	public boolean equals(Object o) {
		return o == this || o instanceof HeaderClassList && this.classList.equals(((HeaderClassList) o).classList);
	}

	public boolean isEmpty() {
		return classList.isEmpty();
	}
}
