package com.armadialogcreator.gui.main.sveditor;

import com.armadialogcreator.canvas.Graphics;
import com.armadialogcreator.core.sv.SVColorArray;
import com.armadialogcreator.gui.GenericResponseFooter;
import com.armadialogcreator.gui.fxcontrol.inputfield.DoubleChecker;
import com.armadialogcreator.gui.fxcontrol.inputfield.InputField;
import com.armadialogcreator.img.icons.ADCIcons;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;

/**
 A {@link SVColorArray} editor

 @author Kayler
 @since 07/13/2016 */
public class ColorArrayValueEditor implements ValueEditor<SVColorArray> {
	private final ColorPicker colorPicker = new ColorPicker();
	private final HBox masterPane = new HBox(2, colorPicker);
	private final ValueObserver<SVColorArray> valueObserver = new ValueObserver<>(null);

	public ColorArrayValueEditor() {
		Button btnBrackets = new Button("", new ImageView(ADCIcons.ICON_BRACKETS));
		btnBrackets.setOnAction(event -> {
			ArrayEditorPopup editor = new ArrayEditorPopup(colorPicker.getValue());
			double x = btnBrackets.localToScreen(0, 0).getX();
			double y = masterPane.localToScreen(0, -masterPane.getHeight()).getY();
			editor.show(btnBrackets, x, y);
			editor.showingProperty().addListener((observable, oldValue, showing) -> {
				if (!showing && !editor.isCancelled()) {
					if (editor.getColor() == null) {
						valueObserver.updateValue(null);
						colorPicker.setValue(null);
					} else {
						valueObserver.updateValue(new SVColorArray(ColorUtil.toARGB(editor.getColor())));
						colorPicker.setValue(editor.getColor());
					}
				}
			});
		});
		masterPane.getChildren().add(btnBrackets);

		colorPicker.setValue(null);
		colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Color newValue = colorPicker.getValue();
				SVColorArray color;
				if (newValue == null) {
					color = null;
				} else {
					color = new SVColorArray(ColorUtil.toARGB(newValue));

				}
				valueObserver.updateValue(color);
			}
		});
	}

	@Override
	public void submitCurrentData() {

	}

	@Override
	public SVColorArray getValue() {
		return valueObserver.getValue();
	}

	@Override
	public void setValue(SVColorArray val) {
		if (val == null) {
			colorPicker.setValue(null);
		} else {
			colorPicker.setValue(Color.color(val.getRedF(), val.getGreenF(), val.getBlueF(), val.getAlphaF()));
		}
	}

	@Override
	public @NotNull Node getRootNode() {
		return masterPane;
	}

	@Override
	public boolean displayFullWidth() {
		return false;
	}

	@Override
	public void focusToEditor() {
		colorPicker.requestFocus();
	}

	@NotNull
	@Override
	public ReadOnlyValueObserver<SVColorArray> getReadOnlyObserver() {
		return valueObserver.getReadOnlyValueObserver();
	}

	private class ArrayEditorPopup extends Popup {
		private final InputField<DoubleChecker, Double> r = new InputField<>(new DoubleChecker(), 0d);
		private final InputField<DoubleChecker, Double> g = new InputField<>(new DoubleChecker(), 0d);
		private final InputField<DoubleChecker, Double> b = new InputField<>(new DoubleChecker(), 0d);
		private final InputField<DoubleChecker, Double> a = new InputField<>(new DoubleChecker(), 0d);
		private final TextField tfAsArray = new TextField();
		private boolean cancelled = true;

		public ArrayEditorPopup(@Nullable Color initialColor) {
			VBox root = new VBox(5);
			root.setPadding(new Insets(10));
			getContent().add(root);

			root.setStyle("-fx-background-color:-fx-background");
			root.setBorder(new Border(
							new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID,
									CornerRadii.EMPTY, BorderStroke.THIN
							)
					)
			);

			Canvas canvas = new Canvas(128, 32);
			StackPane stackPaneCanvas = new StackPane(canvas);
			stackPaneCanvas.setBorder(root.getBorder());
			stackPaneCanvas.setMaxWidth(canvas.getWidth());
			stackPaneCanvas.setAlignment(Pos.CENTER_LEFT);

			HBox paneHeader = new HBox(5, stackPaneCanvas, tfAsArray);
			root.getChildren().add(paneHeader);
			HBox.setHgrow(tfAsArray, Priority.ALWAYS);

			tfAsArray.setEditable(false);

			Graphics graphics = new Graphics(canvas.getGraphicsContext2D());

			ValueListener<Double> valListener = (observer, oldValue, newValue) -> {
				Color c = getCurrentColor();
				if (c != null) {

					if (c.getOpacity() < 1) {
						//draw a grid to show theres transparency
						graphics.setGlobalAlpha(1);
						graphics.setFill(AColorConstant.WHITE.argb);
						graphics.paintCheckerboard(
								0, 0, (int) canvas.getWidth(), (int) canvas.getHeight(), Color.GRAY, Color.WHITE,
								5);
					}

					graphics.setGlobalAlpha(c.getOpacity());
					graphics.setFill(ColorUtil.toARGB(c));
					graphics.fillRectangle(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

					tfAsArray.setText(AColor.toStringF(c.getRed(), c.getGreen(), c.getBlue(), c.getOpacity()));
				}
			};
			r.getValueObserver().addListener(valListener);
			g.getValueObserver().addListener(valListener);
			b.getValueObserver().addListener(valListener);
			a.getValueObserver().addListener(valListener);

			if (initialColor != null) {
				r.getValueObserver().updateValue(getRounded(initialColor.getRed()));
				g.getValueObserver().updateValue(getRounded(initialColor.getGreen()));
				b.getValueObserver().updateValue(getRounded(initialColor.getBlue()));
				a.getValueObserver().updateValue(getRounded(initialColor.getOpacity()));
			}


			//r
			root.getChildren().add(getColorEditor("r", r));
			//g
			root.getChildren().add(getColorEditor("g", g));
			//b
			root.getChildren().add(getColorEditor("b", b));
			//a
			root.getChildren().add(getColorEditor("a", a));

			//footer
			root.getChildren().add(new Separator(Orientation.HORIZONTAL));
			GenericResponseFooter footer = new GenericResponseFooter(true, true, false,
					null,
					cancelEvent -> {
						cancelled = true;
						ArrayEditorPopup.this.hide();
					},
					okEvent -> {
						if (!hasInvalid()) {
							return;
						}
						cancelled = false;
						ArrayEditorPopup.this.hide();
					}
			);
			ResourceBundle bundle = Lang.ApplicationBundle();
			footer.getBtnOk().setText(bundle.getString("ValueEditors.ColorArrayEditor.use"));
			root.getChildren().add(footer);

			setAutoHide(true);

			setHideOnEscape(true); //when push esc key, hide it

			valListener.valueUpdated(r.getValueObserver(), null, null);
		}

		private boolean hasInvalid() {
			if (r.getValue() == null) {
				return false;
			}
			if (g.getValue() == null) {
				return false;
			}
			if (b.getValue() == null) {
				return false;
			}
			if (a.getValue() == null) {
				return false;
			}

			return boundCheck(r) && boundCheck(g) && boundCheck(b) && boundCheck(a);
		}

		private boolean boundCheck(InputField<DoubleChecker, Double> tf) {
			double v = tf.getValue();
			return v >= 0 && v <= 1.0;
		}

		private Node getColorEditor(String colorLetter, InputField<DoubleChecker, Double> tf) {
			HBox hbox = new HBox(5);
			hbox.setAlignment(Pos.CENTER_LEFT);

			Label lblColor = new Label(colorLetter);
			lblColor.setFont(Font.font("monospace"));
			hbox.getChildren().add(lblColor);

			hbox.getChildren().add(tf);

			Slider slider = new Slider(0, 100, tf.getValue() == null ? 0 : (tf.getValue() * 100));
			slider.setShowTickLabels(true);
			slider.setShowTickMarks(true);
			slider.valueProperty().addListener((observable, oldValue, newValue) -> {
				double rounded = getRounded(newValue.doubleValue() / 100);
				if (tf.getValue() != null && equalTo(tf.getValue(), rounded)) {
					return;
				}
				tf.getValueObserver().updateValue(rounded);
			});
			hbox.getChildren().add(slider);

			tf.getValueObserver().addListener((observer, oldValue, newValue) -> {
				slider.setValue(newValue == null ? 0 : newValue * 100);
			});


			return hbox;
		}

		@Nullable
		public Color getColor() {
			return cancelled ? null :
					getCurrentColor();
		}

		@Nullable
		private Color getCurrentColor() {
			return !hasInvalid() ? null :
					Color.color(
							r.getValue().doubleValue(),
							g.getValue().doubleValue(),
							b.getValue().doubleValue(),
							a.getValue().doubleValue()
					);
		}

		private double getRounded(double d) {
			return SVColorArray.round(d);
		}

		private boolean equalTo(double d1, double d2) {
			return SVColorArray.equalTo(d1, d2);
		}

		public boolean isCancelled() {
			return cancelled;
		}
	}
}
