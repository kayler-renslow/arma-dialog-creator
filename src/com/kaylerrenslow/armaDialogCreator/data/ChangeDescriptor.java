/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.data;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 11/19/2016 */
public class ChangeDescriptor {

	private final Change change;
	private final Change.ChangeType changeType;
	private final long timePerformed;

	public ChangeDescriptor(@NotNull Change change, @NotNull Change.ChangeType changeType, long timePerformed) {
		this.change = change;
		this.changeType = changeType;
		this.timePerformed = timePerformed;
	}

	/** Get the Epoch timestamp of when the change was performed. */
	public long getTimePerformed() {
		return timePerformed;
	}

	/** Return the {@link Change} */
	@NotNull
	public Change getChange() {
		return change;
	}

	/** Return the {@link com.kaylerrenslow.armaDialogCreator.data.Change.ChangeType} of {@link #getChange()}. */
	@NotNull
	public Change.ChangeType getChangeType() {
		return changeType;
	}
}
