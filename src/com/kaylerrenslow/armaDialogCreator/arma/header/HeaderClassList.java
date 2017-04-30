package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

/**
 @author Kayler
 @since 04/30/2017 */
public class HeaderClassList implements Iterable<HeaderClass> {
	private final List<HeaderClass> classList;

	public HeaderClassList(@NotNull List<HeaderClass> classList) {
		this.classList = classList;
	}

	@Nullable
	public HeaderClass getClassByName(@NotNull String className) {
		for (HeaderClass hc : classList) {
			if (hc.getClassName().equals(className)) {
				return hc;
			}
		}
		return null;
	}

	@Nullable
	public HeaderClass findClassByPath(@NotNull String... classNames) {
		if (classNames.length == 0) {
			return null;
		}
		HeaderClass cursor = getClassByName(classNames[0]);
		int cursorI = 1;
		while (cursor != null && cursorI < classNames.length) {
			cursor = cursor.getNestedClasses().getClassByName(classNames[cursorI++]);
		}
		return cursor;
	}

	@NotNull
	@Override
	public Iterator<HeaderClass> iterator() {
		return classList.iterator();
	}

}
