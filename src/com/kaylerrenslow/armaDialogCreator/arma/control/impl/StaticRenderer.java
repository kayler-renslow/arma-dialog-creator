package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/25/2016.
 */
public class StaticRenderer extends ArmaControlRenderer {
	
	protected static final Font DEFAULT_FX_FONT = Font.font(20d);
	
	protected Color textColor = backgroundColor.invert();
	
	private Text textObj = new Text();
	
	@Override
	protected void init() {
		textObj.setFont(DEFAULT_FX_FONT);
		
		myControl.findRequiredProperty(ControlPropertyLookup.COLOR_BACKGROUND).getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue instanceof AColor) {
					setBackgroundColor(((AColor) newValue).toJavaFXColor());
				}
			}
		});
		myControl.findRequiredProperty(ControlPropertyLookup.TEXT).getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue == null) {
					setText("");
				} else {
					setText(newValue.toString().replaceAll("\"\"", "\""));
				}
			}
		});
		myControl.findRequiredProperty(ControlPropertyLookup.COLOR_TEXT).getValueObserver().addValueListener(new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				if (newValue instanceof AColor) {
					setTextColor(((AColor) newValue).toJavaFXColor());
				}
			}
		});
		
	}
	
	private int getTextX() {
		int textWidth = (int) textObj.getLayoutBounds().getWidth();
		return getLeftX() + (getWidth() - textWidth) / 2;
	}
	
	private int getTextY() {
		int textHeight = (int) (textObj.getLayoutBounds().getHeight() * 0.25);
		return getTopY() + (getHeight() - textHeight) / 2;
	}
	
	public void paint(GraphicsContext gc) {
		super.paint(gc);
		gc.setFont(getFont());
		gc.setFill(textColor);
		gc.fillText(getText(), getTextX(), getTextY());
	}
	
	public Font getFont() {
		return textObj.getFont();
	}
	
	public void setFont(@NotNull Font font) {
		this.textObj.setFont(font);
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
}
