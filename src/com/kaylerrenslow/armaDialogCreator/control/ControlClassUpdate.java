package com.kaylerrenslow.armaDialogCreator.control;

import org.jetbrains.annotations.NotNull;

/**
 Used with {@link ControlClass#getControlClassUpdateGroup()}
 @author Kayler
 @since 11/16/16
 */
public interface ControlClassUpdate {

	/**Get the {@link ControlClass} that was updated*/
	@NotNull
	ControlClass getControlClass();
}
