package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/07/2016.
 */
public class SimpleArmaControlSpecProvider extends ArmaControlSpecProvider {
		
	private ControlClass[] requiredSubClasses;
	private ControlClass[] optionalSubClasses;
	private ControlPropertyLookup[] requiredProperties;
	private ControlPropertyLookup[] optionalProperties;
	
	public SimpleArmaControlSpecProvider(@NotNull ControlClass[] requiredSubClasses, @NotNull ControlClass[] optionalSubClasses, @NotNull ControlPropertyLookup[] requiredProperties, @NotNull ControlPropertyLookup[] optionalProperties) {
		this.requiredSubClasses = requiredSubClasses;
		this.optionalSubClasses = optionalSubClasses;
		this.requiredProperties = requiredProperties;
		this.optionalProperties = optionalProperties;
	}
	
	@NotNull
	@Override
	public ControlClass[] getRequiredSubClasses() {
		return requiredSubClasses;
	}
	
	@NotNull
	@Override
	public ControlClass[] getOptionalSubClasses() {
		return optionalSubClasses;
	}
	
	@NotNull
	@Override
	public ControlPropertyLookup[] getRequiredProperties() {
		return requiredProperties;
	}
	
	@NotNull
	@Override
	public ControlPropertyLookup[] getOptionalProperties() {
		return optionalProperties;
	}
}
