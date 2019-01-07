package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.core.CustomControlClass;
import com.armadialogcreator.data.olddata.CustomControlClassRegistry;
import com.armadialogcreator.data.olddata.Project;
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
 This dialog asks the user to select a {@link CustomControlClass} that exists in the {@link CustomControlClassRegistry}.

 @author Kayler
 @since 11/13/2016 */
public class ChooseCustomControlClassDialog extends ChooseItemDialog<CustomControlClass> {

	public ChooseCustomControlClassDialog() {
		super(new Category[]{
						new AllCategory(), new ScopeCategory(CustomControlClass.Scope.Project),
						new ScopeCategory(CustomControlClass.Scope.Workspace)
				},
				Project.getCurrentProject().getAllCustomControlClasses(), null, null
		);
		ResourceBundle bundle = Lang.ApplicationBundle();
		setTitle(bundle.getString("Popups.ChooseCustomControl.dialog_title"));
		setHeaderTitle(bundle.getString("Popups.ChooseCustomControl.header_title"));


	}

	private static abstract class Category implements ItemCategory<CustomControlClass> {
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
		public boolean itemInCategory(@NotNull CustomControlClass item) {
			return true;
		}

		@Nullable
		@Override
		public Node getMiscCategoryNode() {
			return categoryNode;
		}

		@Override
		public void newItemSelected(CustomControlClass item) {
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

		public void setToControlClass(CustomControlClass controlClass) {
			stackPaneComment.getChildren().clear();
			if (controlClass.getComment() == null) {
				stackPaneComment.getChildren().add(lblNoComment);
			} else {
				stackPaneComment.getChildren().add(taComment);
				taComment.setText(controlClass.getComment());
			}
		}
	}

	private static class ScopeCategory extends Category {

		private final CategoryNode categoryNode;
		private final CustomControlClass.Scope scope;

		public ScopeCategory(@NotNull CustomControlClass.Scope scope) {
			this.scope = scope;
			this.categoryNode = new CategoryNode();
		}

		@NotNull
		@Override
		public String categoryDisplayName() {
			switch (scope) {
				case Project: {
					return bundle.getString("Popups.ChooseCustomControl.Category.Scope.Project.display_title");
				}
				case Workspace:
					return bundle.getString("Popups.ChooseCustomControl.Category.Scope.Workspace.display_title");

				default:
					throw new IllegalStateException("unknown scope:" + scope);
			}
		}

		@Override
		public boolean itemInCategory(@NotNull CustomControlClass item) {
			return item.getScope() == scope;
		}

		@Nullable
		@Override
		public Node getMiscCategoryNode() {
			return categoryNode;
		}

		@Override
		public void newItemSelected(CustomControlClass item) {
			categoryNode.setToControlClass(item);
		}
	}
}
