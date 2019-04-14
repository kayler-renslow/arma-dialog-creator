package com.armadialogcreator.application;

/**
 @author K
 @since 3/4/19 */
public class ADCDataLoadException extends Exception {
	public ADCDataLoadException() {
	}

	public ADCDataLoadException(String message) {
		super(message);
	}

	public ADCDataLoadException(String message, Throwable cause) {
		super(message, cause);
	}

	public ADCDataLoadException(Throwable cause) {
		super(cause);
	}

	public ADCDataLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
