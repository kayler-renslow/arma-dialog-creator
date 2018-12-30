package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.data.ChangeDescriptor;
import com.armadialogcreator.data.Changelog;
import com.armadialogcreator.gui.img.ADCImages;
import com.armadialogcreator.gui.main.fxControls.HistoryListItem;
import com.armadialogcreator.gui.main.fxControls.HistoryListItemSubInfo;
import com.armadialogcreator.gui.main.fxControls.HistoryListPopup;
import com.armadialogcreator.gui.main.fxControls.HistoryListProvider;
import com.armadialogcreator.main.Lang;
import com.armadialogcreator.util.EpochPresentation;
import com.armadialogcreator.util.ReadOnlyList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 View changes collected from {@link Changelog#getRecentChanges()}

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
			ReadOnlyList<ChangeDescriptor> pastChanges = Changelog.getInstance().getRecentChanges();
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
					graphic = new ImageView(ADCImages.ICON_UNDO);
					subInfoLabel = Lang.ApplicationBundle().getString("Popups.ViewChanges.SubInfo.change_type_undo");
					break;
				}
				case REDO: {
					graphic = new ImageView(ADCImages.ICON_REDO);
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
