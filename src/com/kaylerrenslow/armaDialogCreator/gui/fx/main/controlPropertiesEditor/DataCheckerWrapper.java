package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield.InputFieldDataChecker;

/**
 Created on 07/13/2016.
 A wrapper for an InputFieldDataChecker that doesn't check for a {@link SerializableValue} instance

 @param <V> Type of the data that the InputFieldDataChecker should return
 @author Kayler */
public class DataCheckerWrapper<V> implements SerializableValue, InputFieldDataChecker {
	private final InputFieldDataChecker<V> dataChecker;
	private String[] arr = new String[1];

	/**
	 Create a wrapper for an InputFieldDataChecker that doesn't check for a {@link SerializableValue} instance

	 @param dataChecker data checker to wrap around
	 */
	public DataCheckerWrapper(InputFieldDataChecker<V> dataChecker) {
		this.dataChecker = dataChecker;
	}

	public void setValue(V t) {
		arr[0] = t.toString();
	}

	@Override
	public String[] getAsStringArray() {
		return arr;
	}

	@Override
	public boolean validData(String data) {
		return dataChecker.validData(data);
	}

	@Override
	public Object parse(String data) {
		return dataChecker.validData(data);
	}

	@Override
	public String getTypeName() {
		return dataChecker.getTypeName();
	}

	@Override
	public boolean allowEmptyData() {
		return dataChecker.allowEmptyData();
	}
}
