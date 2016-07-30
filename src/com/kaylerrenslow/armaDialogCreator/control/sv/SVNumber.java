package com.kaylerrenslow.armaDialogCreator.control.sv;

/**
 @author Kayler
 <p>
 Created on 07/15/2016.
 @see SVInteger
 @see SVDouble */
public abstract class SVNumber extends SerializableValue {
	public SVNumber(Number value) {
		this(value.toString() + "");
	}
		
	public SVNumber(String value){
		super(value);
		setValue(value);
	}
	
	protected abstract void setValue(String value);
	
	public abstract double getNumber();
}
