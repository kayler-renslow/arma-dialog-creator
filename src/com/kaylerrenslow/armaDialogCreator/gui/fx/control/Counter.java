package com.kaylerrenslow.armaDialogCreator.gui.fx.control;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 Created by Kayler on 06/01/2016.
 */
public class Counter extends HBox {
	private final boolean wrapAround;
	private double currentValue, minValue, maxValue, step;

	private Label label = new Label();

	private List<CounterButton> buttons = new ArrayList<>();

	private CounterButton btnDecr, btnIncr;

	/**
	 Creates an counter control. The control has 2 buttons and a label. The buttons increment/decrement the value and display the current value in the label.

	 @param startValue starting value
	 @param minValue minimum value (won't decrement below this)
	 @param maxValue maximum value (won't increment higher than this)
	 @param step how much each button press increments/decrements the value
	 @param wrapAround true if when the max value or minimum value is reached, the value will go to zero. False if when max/min value is reached and value stays put
	 */
	public Counter(double startValue, double minValue, double maxValue, double step, boolean wrapAround) {
		super(2);
		this.currentValue = startValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.step = step;
		this.wrapAround = wrapAround;


		updateLabel();
		label.setFont(Font.font(Font.getDefault().getFamily(), 15));
		HBox.setMargin(label, new Insets(0, 0, 0, 5));
		btnDecr = new CounterButton(this, -Math.abs(step));
		btnIncr = new CounterButton(this, Math.abs(step));

		final double size = 25;
		label.setMinSize(size, size);
		label.setMaxSize(Double.MAX_VALUE, size);


		getChildren().addAll(btnDecr, btnIncr, label);
		setFocusTraversable(true);
	}

	/** Set the current value */
	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
		updateLabel();
	}

	/** Set the minimum possible value */
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	/** Set the maximum possible value */
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	/** Set the step for the default increment and decrement buttons */
	public void setStep(double step) {
		this.step = step;
		btnDecr.setStep(-Math.abs(step));
		btnIncr.setStep(Math.abs(step));
	}

	/**
	 Adds a new button that can control the value.

	 @param step how much each button press will change the current value
	 @param text text on the button
	 @param index index of where to add button (if >= number of buttons, will add to end. if < 0, will add to front)
	 @return button id to be used to remove the button from the counter
	 */
	public int addUpdateButton(double step, String text, int index) {
		CounterButton btn = new CounterButton(this, step, text);
		buttons.add(btn);
		if (index < 0) {
			index = 0;
		}
		if (index >= buttons.size()) {
			index = getChildren().size() - 1;
		}
		getChildren().add(index, btn);
		return btn.getButtonId();
	}

	/**
	 Removes the custom added button with given id (returned from addUpdateButton)

	 @return true if there was a button with that id and was removed, false if id wasn't used
	 */
	public boolean removeCustomButton(int id) {
		CounterButton match = null;
		for (CounterButton counterButton : buttons) {
			if (counterButton.getButtonId() == id) {
				match = counterButton;
				break;
			}
		}
		if (match != null) {
			buttons.remove(match);
			return true;
		}
		return false;
	}

	private void updateValue(double step) {
		currentValue += step;
		if (currentValue < minValue) {
			if (wrapAround) {
				currentValue = 0;
			} else {
				currentValue = minValue;
			}
		} else if (currentValue > maxValue) {
			if (wrapAround) {
				currentValue = 0;
			} else {
				currentValue = maxValue;
			}
		}
		updateLabel();
	}

	private void updateLabel() {
		label.setText(removeTrail(currentValue));
		this.autosize();
	}

	private static String removeTrail(double value) {
		String s = value + "";
		s = !s.contains(".") ? s : s.replaceAll("0*$", "").replaceAll("\\.$", "");//remove unnecessary zeroes
		return s;
	}

	private static class CounterButton extends Button {
		private static int nextId = 0;

		private double step;
		private final int id = nextId++;

		CounterButton(Counter counter, double step) {
			this(counter, step, (step >= 0 ? ("+" + removeTrail(step)) : removeTrail(step)));
		}

		CounterButton(Counter counter, double step, String text) {
			super(text);
			this.step = step;
			setOnAction(event -> counter.updateValue(this.step));
		}

		void setStep(double step) {
			this.step = step;
			setText(removeTrail(step));
		}

		int getButtonId() {
			return id;
		}
	}
}
