package com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookupConstant;
import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.ControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Resolution;
import com.kaylerrenslow.armaDialogCreator.util.UpdateGroupListener;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import org.jetbrains.annotations.NotNull;

/**
 A utility class for rendering text with a {@link ArmaControlRenderer} which can have the following property updates:
 <ul>
 <li>Text Color</li>
 <li>Text Position (left, center, right)</li>
 </ul>

 @author Kayler
 @since 11/21/2016 */
public class BasicTextRenderer {
	private final ArmaControl control;
	private final ArmaControlRenderer renderer;

	protected Color textColor;

	private Text textObj = new Text();
	private ControlProperty sizeExProperty;

	public BasicTextRenderer(ArmaControl control, ArmaControlRenderer renderer, ControlPropertyLookupConstant text, ControlPropertyLookupConstant colorText, ControlPropertyLookupConstant style,
							 ControlPropertyLookupConstant sizeEx) {
		this.control = control;
		this.renderer = renderer;
		textColor = renderer.getBackgroundColor().invert();
		init(text, colorText, style, sizeEx);
	}

	private void init(ControlPropertyLookupConstant text, ControlPropertyLookupConstant colorText, ControlPropertyLookupConstant style, ControlPropertyLookupConstant sizeEx) {
		textObj.setTextOrigin(VPos.TOP);
		textObj.setBoundsType(TextBoundsType.VISUAL);

		control.findProperty(text).getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue == null) {
					setText("");
				} else {
					setText(newValue.toString().replaceAll("\"\"", "\""));
				}
				renderer.requestRender();
			}
		});
		control.findProperty(colorText).getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue instanceof AColor) {
					setTextColor(((AColor) newValue).toJavaFXColor());
				}
				renderer.requestRender();
			}
		});
		control.findProperty(style).getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue instanceof ControlStyleGroup) {
					ControlStyleGroup group = (ControlStyleGroup) newValue;
					for (ControlStyle style : group.getValues()) {
						if (style == ControlStyle.LEFT) {
							textObj.setTextAlignment(TextAlignment.LEFT);
							break;
						} else if (style == ControlStyle.CENTER) {
							textObj.setTextAlignment(TextAlignment.CENTER);
							break;
						} else if (style == ControlStyle.RIGHT) {
							textObj.setTextAlignment(TextAlignment.RIGHT);
							break;
						}
					}
					renderer.requestRender();
				}
			}
		});
		sizeExProperty = control.findProperty(sizeEx);
		sizeExProperty.getValueObserver().addListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue instanceof Expression) {
					Expression ex = (Expression) newValue;
					updateFont(ex);
				}
			}
		});

		if (sizeExProperty.getValue() instanceof Expression) {
			updateFont((Expression) sizeExProperty.getValue());
		}

		renderer.getResolutionUpdateGroup().addListener(new UpdateGroupListener<Resolution>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<Resolution> group, Resolution data) {
				resolutionUpdate();
			}
		});
	}

	private int getTextX() {
		int textWidth = (int) textObj.getLayoutBounds().getWidth();
		switch (textObj.getTextAlignment()) {
			case LEFT: {
				return renderer.getLeftX() + (int) (renderer.getWidth() * 0.02);
			}
			case RIGHT: {
				return renderer.getRightX() - textWidth - (int) (renderer.getWidth() * 0.02);
			}
			default:
			case CENTER: {
				return renderer.getLeftX() + (renderer.getWidth() - textWidth) / 2;
			}
		}
	}

	private int getTextY() {
		int textHeight = (int) (textObj.getLayoutBounds().getHeight());
		return renderer.getTopY() + (renderer.getHeight() + textHeight) / 2;
	}

	public void paint(GraphicsContext gc) {
		gc.beginPath();
		gc.rect(renderer.getLeftX(), renderer.getTopY(), renderer.getWidth(), renderer.getHeight());
		gc.clip();
		gc.setFont(getFont());
		gc.setFill(textColor);
		gc.fillText(getText(), getTextX(), getTextY());
		gc.closePath();
	}

	private Font getFont() {
		return textObj.getFont();
	}

	public void setText(String text) {
		this.textObj.setText(text);
	}

	public String getText() {
		return this.textObj.getText();
	}

	public void setTextColor(@NotNull Color color) {
		this.textColor = color;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void updateFont(@NotNull Expression sizeEx) {
		textObj.setFont(Font.font(fontSize(sizeEx.getNumVal())));
		renderer.requestRender();
	}

	public double fontSize(double percent) {
		double maxPixels = renderer.getResolution().getViewportHeight();
		return toPoints(maxPixels * percent);
	}

	public double toPoints(double pixels) {
		final double pointsPerInch = 72;
		final double pixelsPerInch = 96;
		return pixels * pointsPerInch / pixelsPerInch;
	}

	public void resolutionUpdate() {
		if (sizeExProperty.getValue() instanceof Expression) {
			updateFont((Expression) sizeExProperty.getValue());
		}
	}
}
