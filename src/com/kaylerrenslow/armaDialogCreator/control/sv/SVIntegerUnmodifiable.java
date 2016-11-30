package com.kaylerrenslow.armaDialogCreator.control.sv;

/**
 An unmodifiable {@link SVInteger}
 @author Kayler
 @since 10/23/2016. */
public class SVIntegerUnmodifiable extends SVInteger {

	public SVIntegerUnmodifiable(int i) {
		super(i);
	}

	@Override
	public void setInt(int i) {
		throw new IllegalStateException("the SVInteger's value is unmodifiable");
	}

	@Override
	protected void setValue(String value) {
		throw new IllegalStateException("the SVInteger's value is unmodifiable");
	}
}
