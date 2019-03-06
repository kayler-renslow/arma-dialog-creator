package com.armadialogcreator.application;

/**
 @author K
 @since 3/4/19 */
public class ADCDataWriteException extends Exception {
	public ADCDataWriteException() {
	}

	public ADCDataWriteException(String message) {
		super(message);
	}

	public ADCDataWriteException(String message, Throwable cause) {
		super(message, cause);
	}

	public ADCDataWriteException(Throwable cause) {
		super(cause);
	}

	public ADCDataWriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
