package com.armadialogcreator.gui.main.fxControls;

import com.armadialogcreator.util.ColorUtil;
import com.armadialogcreator.util.UpdateGroupListener;
import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

/**
 A button that has a small box on the left side with a canvas that shows a color

 @author K
 @since 3/5/19 */
public class ColorButton extends StackPane {
	private final UpdateListenerGroup<UpdateListenerGroup.NoData> pressedUpdateGroup = new UpdateListenerGroup<>();
	private final Color color;

	public ColorButton(@NotNull Color color) {
		this.color = color;
		final int size = 8;
		Canvas canvas = new Canvas(size, size);
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.setFill(color);
		g.fillRect(0, 0, size, size);
		Button button = new Button(ColorUtil.toHex(color), canvas);
		button.setFont(Font.font("monospace"));
		button.setPadding(new Insets(4, 4, 4, 4));
		button.setOnAction(event -> {
			pressedUpdateGroup.update(UpdateListenerGroup.NoDataInstance);
		});
		getChildren().add(button);
	}

	public void addPressedListener(@NotNull UpdateGroupListener<UpdateListenerGroup.NoData> listener) {
		pressedUpdateGroup.addListener(listener);
	}

	@NotNull
	public Color getColor() {
		return color;
	}
}
