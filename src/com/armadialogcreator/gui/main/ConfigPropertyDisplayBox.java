package com.armadialogcreator.gui.main;

import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigProperty;
import com.armadialogcreator.core.ConfigPropertyKey;
import com.armadialogcreator.data.ConfigPropertyDocumentationProvider;
import com.armadialogcreator.gui.FXUtil;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import org.jetbrains.annotations.NotNull;

/**
 A javafx {@link HBox} that has a {@link MenuButton} as the left node, followed by an "=" label, then followed
 by an empty {@link StackPane}. This box is meant for displaying a single {@link ConfigProperty} that either has
 or doesn't have a value. Since {@link ConfigProperty} instances can't have a null value, {@link ConfigPropertyKey}
 is passed through constructor which is used to get the {@link ConfigProperty#getName()} attribute.

 @author Kayler
 @since 4/2/2019 */
public class ConfigPropertyDisplayBox extends HBox {
	private static final Font TOOLTIP_FONT = Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 20d);

	/** The {@link ConfigPropertyKey} that was passed through constructor */
	protected final ConfigPropertyKey configPropertyKey;
	/** The {@link ConfigClass} that was passed through constructor */
	protected ConfigClass configClass;

	/** The root pane for where to put things after the "=" label node */
	protected final StackPane contentStackPane = new StackPane();
	/** The {@link MenuButton} that comes before the "=" label node */
	protected final MenuButton menuButtonOptions = new MenuButton();
	private final Label lblEq = new Label("=");

	public ConfigPropertyDisplayBox(@NotNull ConfigClass configClass, @NotNull ConfigPropertyKey property) {
		super(5);
		this.configClass = configClass;
		this.configPropertyKey = property;

		setAlignment(Pos.CENTER_LEFT);
		setMaxWidth(Double.MAX_VALUE);

		menuButtonOptions.setText(configPropertyKey.getPropertyName());
		HBox.setHgrow(menuButtonOptions, Priority.ALWAYS);

		getChildren().addAll(menuButtonOptions, lblEq, contentStackPane);

		Tooltip tooltip = FXUtil.getMultilineTooltip(getDocumentation());
		tooltip.setFont(TOOLTIP_FONT);
		Tooltip.install(menuButtonOptions, tooltip);
	}

	protected void hideEqualSign() {
		lblEq.setVisible(false);
		lblEq.setManaged(false);
	}

	/**
	 Hides this whole node if hidden is true

	 @param hidden should hide?
	 */
	public void hide(boolean hidden) {
		setVisible(!hidden);
		setManaged(!hidden);
	}

	@NotNull
	protected String getDocumentation() {
		return ConfigPropertyDocumentationProvider.getDocumentation(configClass, configPropertyKey);
	}

	/**
	 A popup that is automatically bound to {@link #menuButtonOptions} when shown.
	 */
	protected class MenuButtonPopup extends Popup {

		private final int lineCount;
		private final Label lbl;

		/**
		 @param text the text to place in popup
		 */
		public MenuButtonPopup(@NotNull String text) {
			lbl = new Label(text);
			int lineCount = 0;
			for (int i = 0; i < text.length(); i++) {
				if (text.charAt(i) == '\n') {
					lineCount++;
				}
			}
			this.lineCount = lineCount;
			StackPane container = new StackPane(lbl);
			container.setBackground(new Background(new BackgroundFill(
					Color.DODGERBLUE, CornerRadii.EMPTY, Insets.EMPTY)
			));
			lbl.setFont(Font.font(15));
			lbl.setTextFill(Color.WHITE);
			container.setPadding(new Insets(4));
			this.getContent().add(container);
		}

		public void showPopup() {
			Control ownerNode = menuButtonOptions;
			Point2D p = ownerNode.localToScreen(0, -ownerNode.getHeight() / 2);
			this.setAutoHide(true);
			show(ownerNode, p.getX(), p.getY());
			this.setY(p.getY() - lbl.getHeight());
		}
	}

}
