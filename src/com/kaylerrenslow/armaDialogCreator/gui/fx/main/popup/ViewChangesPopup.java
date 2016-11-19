/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.data.ChangeDescriptor;
import com.kaylerrenslow.armaDialogCreator.data.Changelog;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.fxControls.HistoryListItem;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.fxControls.HistoryListItemSubInfo;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.fxControls.HistoryListPopup;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.fxControls.HistoryListProvider;
import com.kaylerrenslow.armaDialogCreator.gui.img.Images;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.EpochPresentation;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 View changes collected from {@link Changelog#getChanges()}

 @author Kayler
 @since 11/19/2016 */
public class ViewChangesPopup extends HistoryListPopup {
	public ViewChangesPopup() {
		super(Lang.ApplicationBundle().getString("Popups.ViewChanges.popup_title"), ChangesHistoryListProvider.INSTANCE);
	}

	private static class ChangesHistoryListProvider implements HistoryListProvider {

		static final ChangesHistoryListProvider INSTANCE = new ChangesHistoryListProvider();

		@NotNull
		@Override
		public List<HistoryListItem> collectItems() {
			List<HistoryListItem> list = new LinkedList<>();
			ReadOnlyList<ChangeDescriptor> pastChanges = Changelog.getInstance().getChanges();
			for (ChangeDescriptor pastChange : pastChanges) {
				list.add(new ChangeHistoryListItem(pastChange));
			}
			return list;
		}

		@Override
		public @NotNull String noItemsPlaceholder() {
			return Lang.ApplicationBundle().getString("Popups.ViewChanges.no_items_placeholder");
		}
	}

	private static class ChangeHistoryListItem implements HistoryListItem {

		private final ChangeDescriptor descriptor;
		private final ImageView graphic;
		private final HistoryListItemSubInfo[] subInfo = new HistoryListItemSubInfo[2];

		public ChangeHistoryListItem(@NotNull ChangeDescriptor descriptor) {
			this.descriptor = descriptor;
			String subInfoLabel;
			switch (descriptor.getChangeType()) {
				case UNDO: {
					graphic = new ImageView(Images.ICON_UNDO);
					subInfoLabel = Lang.ApplicationBundle().getString("Popups.ViewChanges.SubInfo.change_type_undo");
					break;
				}
				case REDO: {
					graphic = new ImageView(Images.ICON_REDO);
					subInfoLabel = Lang.ApplicationBundle().getString("Popups.ViewChanges.SubInfo.change_type_redo");
					break;
				}
				case CREATED: {
					subInfoLabel = Lang.ApplicationBundle().getString("Popups.ViewChanges.SubInfo.change_type_created");
					graphic = null;
					break;
				}
				default: {
					throw new IllegalArgumentException("unknown change type:" + descriptor.getChangeType());
				}
			}
			subInfo[0] = new HistoryListItemSubInfo(subInfoLabel, "");
			subInfo[1] = new HistoryListItemSubInfo(
					Lang.ApplicationBundle().getString("Popups.ViewChanges.SubInfo.time"),
					EpochPresentation.format(EpochPresentation.Format.Hour_Minute_AM_PM, descriptor.getTimePerformed())
			);
		}

		@Override
		public @NotNull String getItemTitle() {
			return descriptor.getChange().getShortName();
		}

		@Override
		public @NotNull String getInformation() {
			return descriptor.getChange().getDescription();
		}

		@NotNull
		@Override
		public HistoryListItemSubInfo[] getSubInfo() {
			return subInfo;
		}

		@Nullable
		@Override
		public Node getGraphic() {
			return graphic;
		}
	}
}
