package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.application.DataLevel;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.data.ConfigClassRegistry;
import com.armadialogcreator.gui.main.fxControls.ChooseItemDialog;
import com.armadialogcreator.lang.Lang;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;

/**
 This dialog asks the user to select a {@link ConfigClass} that exists in the {@link ConfigClassRegistry}.

 @author Kayler
 @since 11/13/2016 */
public class ChooseConfigClassDialog extends ChooseItemDialog<ConfigClass> {

	public ChooseConfigClassDialog() {
		super(new Category[]{
						new AllCategory(), new ScopeCategory(DataLevel.Project),
						new ScopeCategory(DataLevel.Workspace),
						new ScopeCategory(DataLevel.Application),
						new ScopeCategory(DataLevel.System)
				}, ConfigClassRegistry.instance.iterateAllConfigClasses(), null, null
		);
		ResourceBundle bundle = Lang.ApplicationBundle();
		setTitle(bundle.getString("Popups.ChooseCustomControl.dialog_title"));
		setHeaderTitle(bundle.getString("Popups.ChooseCustomControl.header_title"));


	}

	private static abstract class Category implements ItemCategory<ConfigClass> {
		protected ResourceBundle bundle = Lang.ApplicationBundle();

		@NotNull
		@Override
		public String noItemsPlaceholderText() {
			return bundle.getString("Popups.ChooseCustomControl.no_items_placeholder");
		}

		@NotNull
		@Override
		public String availableItemsDisplayText() {
			return bundle.getString("Popups.ChooseCustomControl.available_items");
		}
	}

	private static class AllCategory extends Category {
		private final CategoryNode categoryNode;

		public AllCategory() {
			this.categoryNode = new CategoryNode();
		}

		@NotNull
		@Override
		public String categoryDisplayName() {
			return bundle.getString("Popups.ChooseCustomControl.Category.All.display_title");
		}

		@Override
		public boolean itemInCategory(@NotNull ConfigClass item) {
			return true;
		}

		@Nullable
		@Override
		public Node getMiscCategoryNode() {
			return categoryNode;
		}

		@Override
		public void newItemSelected(ConfigClass item) {
			categoryNode.setToControlClass(item);
		}
	}

	private static class CategoryNode extends StackPane {

		private final ResourceBundle bundle = Lang.ApplicationBundle();
		private final TextArea taComment = new TextArea();
		private final Label lblNoComment = new Label(bundle.getString("Popups.ChooseCustomControl.Category.All.no_comment"));
		private final StackPane stackPaneComment = new StackPane();

		public CategoryNode() {
			setAlignment(Pos.TOP_CENTER);

			taComment.setEditable(false);

			Label lblComment = new Label(bundle.getString("Popups.ChooseCustomControl.Category.All.comment"));
			lblComment.setAlignment(Pos.TOP_CENTER);

			VBox vbox = new VBox(5, lblComment, stackPaneComment);
			VBox.setVgrow(stackPaneComment, Priority.ALWAYS);
			stackPaneComment.setAlignment(Pos.TOP_CENTER);
			vbox.setAlignment(Pos.TOP_CENTER);
			vbox.setFillWidth(true);
			getChildren().add(vbox);
		}

		public void setToControlClass(@NotNull ConfigClass configClass) {
			stackPaneComment.getChildren().clear();
			if (configClass.getUserComment() == null) {
				stackPaneComment.getChildren().add(lblNoComment);
			} else {
				stackPaneComment.getChildren().add(taComment);
				taComment.setText(configClass.getUserComment());
			}
		}
	}

	private static class ScopeCategory extends Category {

		private final CategoryNode categoryNode;
		private final DataLevel scope;

		public ScopeCategory(@NotNull DataLevel scope) {
			this.scope = scope;
			this.categoryNode = new CategoryNode();
		}

		@NotNull
		@Override
		public String categoryDisplayName() {
			return scope.name();
		}

		@Override
		public boolean itemInCategory(@NotNull ConfigClass item) {
			return ConfigClassRegistry.instance.getDataLevel(item) == scope;
		}

		@Nullable
		@Override
		public Node getMiscCategoryNode() {
			return categoryNode;
		}

		@Override
		public void newItemSelected(ConfigClass item) {
			categoryNode.setToControlClass(item);
		}
	}
}
