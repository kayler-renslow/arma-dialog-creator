package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

/**
 @author Kayler
 Checker for Integers
 Created on 05/31/2016.
 */
public class IntegerFieldDataChecker implements InputFieldDataChecker<Integer> {
	@Override
	public boolean validData(String data) {
		try{
			Integer.parseInt(data);
			return true;
		}catch (NumberFormatException e){
			return false;
		}
	}

	@Override
	public Integer parse(String data) {
		return Integer.parseInt(data);
	}

	@Override
	public String getTypeName() {
		return "Integer";
	}

	@Override
	public boolean allowEmptyData() {
		return false;
	}
}
