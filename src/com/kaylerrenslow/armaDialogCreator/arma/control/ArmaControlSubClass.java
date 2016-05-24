package com.kaylerrenslow.armaDialogCreator.arma.control;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 A class that goes inside a Control class
 Created on 05/23/2016. */
public class ArmaControlSubClass extends ArmaControlClass {
	public static final ArmaControlSubClass[] EMPTY = new ArmaControlSubClass[0];

	private String name;

	public ArmaControlSubClass(@NotNull String name, @NotNull ControlProperty[] requiredProps, @NotNull ControlProperty[] optionalProps, @Nullable ArmaControlSubClass... subClasses) {
		this.name = name;
		addRequiredProperties(requiredProps);
		addOptionalProperties(optionalProps);
		if (subClasses != null) {
			for (ArmaControlSubClass subClass : subClasses) {
				defineSubClass(subClass);
			}
		}
	}

	public String getClassName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ArmaControlSubClass)) {
			return false;
		}
		ArmaControlSubClass other = (ArmaControlSubClass) o;
		return name.equals(other.name);
	}

}
