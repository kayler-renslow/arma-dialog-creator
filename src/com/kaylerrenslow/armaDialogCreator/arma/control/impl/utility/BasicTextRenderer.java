/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

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
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Resolution;
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
		textObj.setTextOrigin(VPos.BASELINE);

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
		return renderer.getTopY() + renderer.getHeight() / 2 + textHeight / 4;
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
