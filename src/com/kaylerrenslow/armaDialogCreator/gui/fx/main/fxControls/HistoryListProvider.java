package com.kaylerrenslow.armaDialogCreator.gui.fx.main.fxControls;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 Provides a list of {@link HistoryListItem} on request.

 @author Kayler
 @see HistoryListPopup
 @since 11/18/16 */
public interface HistoryListProvider {

	/** Get a list of {@link HistoryListItem}'s. */
	@NotNull List<HistoryListItem> collectItems();

	/** Return a string that is presentable to the user that says there are no items available from this {@link HistoryListProvider}. */
	@NotNull String noItemsPlaceholder();
}
