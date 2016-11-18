/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.fxControls;

import org.jetbrains.annotations.NotNull;

/**
 Provides more information about a {@link HistoryListItem} such as "time" or "ID".

 @author Kayler
 @since 11/18/16 */
public class HistoryListItemSubInfo {
	public static final HistoryListItemSubInfo[] EMPTY = new HistoryListItemSubInfo[0];

	private final String label;
	private final String info;

	/**
	 @param label label of the sub-info
	 @param info actual information
	 */
	public HistoryListItemSubInfo(@NotNull String label, @NotNull String info) {
		this.label = label;
		this.info = info;
	}

	/**
	 @return text that is presentable to the user that labels {@link #getInfo()}
	 */
	@NotNull
	public String getLabel() {
		return label;
	}

	/**
	 @return actual information
	 */
	@NotNull
	public String getInfo() {
		return info;
	}
}
