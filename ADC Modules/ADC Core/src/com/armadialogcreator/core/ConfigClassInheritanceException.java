package com.armadialogcreator.core;

/**
 @author K
 @since 01/07/2019 */
public class ConfigClassInheritanceException extends RuntimeException {
	public ConfigClassInheritanceException() {
	}

	public ConfigClassInheritanceException(String message) {
		super(message);
	}

	public ConfigClassInheritanceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigClassInheritanceException(Throwable cause) {
		super(cause);
	}
}
