package com.kaylerrenslow.armaDialogCreator.gui.main.fxControls;

import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Provides the title of the item, main body text of item, a graphic (optional), and sub-information via {@link #getSubInfo()}

 @author Kayler
 @see HistoryListItemSubInfo
 @see HistoryListPopup
 @since 11/18/16 */
public interface HistoryListItem {
	/** Get the title of the item that is presentable to the user */
	@NotNull String getItemTitle();

	/**Get the main body text of the item.*/
	@NotNull String getInformation();

	/** Get an array of sub information about this item */
	@NotNull HistoryListItemSubInfo[] getSubInfo();

	/** Get a graphic to display. */
	@Nullable Node getGraphic();
}
