package com.armadialogcreator.expression;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 @author Kayler
 @since 06/28/2017 */
class UnaryCommandTranslator {
	@NotNull
	public static Value executeUnaryCommand(@NotNull String command, @NotNull NularCommandValueProvider provider) {
		String commandLowercase = command.toLowerCase();
		Method[] methods = provider.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().toLowerCase().equals(commandLowercase)) {
				method.setAccessible(true);
				try {
					return (Value) method.invoke(provider);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		throw new IllegalStateException("didn't match command:" + command);
	}
}
