package com.armadialogcreator.gui.fxcontrol;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/12/2019 */
public class PlaceholderTitledPane extends TitledPane {
	private final Pane userContentPane;
	private final StackPane contentPane;
	private boolean hasPlaceholder;
	private Node placeholder;
	private ScrollPane scrollPane;

	public PlaceholderTitledPane(@NotNull String title,
								 @NotNull Node placeholder,
								 @NotNull Pane contentPane,
								 boolean useScrollPane) {
		super(title, null);
		this.userContentPane = contentPane;
		this.contentPane = new StackPane();
		if (useScrollPane) {
			this.scrollPane = new ScrollPane(contentPane);
			setContent(this.scrollPane);
		} else {
			setContent(this.contentPane);
		}
		this.contentPane.setAlignment(Pos.CENTER);
		if (userContentPane.getChildren().isEmpty()) {
			hasPlaceholder = true;
			this.contentPane.getChildren().add(placeholder);
		} else {
			hasPlaceholder = false;
			this.contentPane.getChildren().add(userContentPane);
		}
	}

	public void addContentChild(@NotNull Node node) {
		if (hasPlaceholder) {
			this.contentPane.getChildren().clear();
			this.contentPane.getChildren().add(userContentPane);
			hasPlaceholder = false;
		}
		userContentPane.getChildren().add(node);
	}

	public boolean removeContentChild(@NotNull Node node) {
		boolean b = userContentPane.getChildren().remove(node);
		if (userContentPane.getChildren().isEmpty()) {
			this.contentPane.getChildren().clear();
			this.contentPane.getChildren().add(placeholder);
			hasPlaceholder = true;
		}
		return b;
	}

	@NotNull
	public Node getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(@NotNull Node placeholder) {
		this.placeholder = placeholder;
	}

	/**
	 @return the underlying {@link ScrollPane} instance
	 (do not invoke {@link ScrollPane#setContent(Node)} or this class will break)
	 */
	@Nullable
	public ScrollPane getScrollPane() {
		return scrollPane;
	}

	@NotNull
	public ObservableList<Node> getContentChildren() {
		return userContentPane.getChildren();
	}
}
