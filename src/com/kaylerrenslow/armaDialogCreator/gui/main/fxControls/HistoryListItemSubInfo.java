package com.kaylerrenslow.armaDialogCreator.gui.main.fxControls;

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
