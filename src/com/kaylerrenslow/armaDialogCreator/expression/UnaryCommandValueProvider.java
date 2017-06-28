package com.kaylerrenslow.armaDialogCreator.expression;

import org.jetbrains.annotations.NotNull;

/**
 Each method in this interface is for getting a value for a unary command.
 The name of the method is the name of the command.
 <p>
 <b>It is required that the name of the method match the name of the command,
 however, case sensitivity doesn't matter.</b>

 @author Kayler
 @since 06/28/2017 */
public interface UnaryCommandValueProvider {

	@NotNull Value safeZoneX();

	@NotNull Value safeZoneY();

	@NotNull Value safeZoneW();

	@NotNull Value safeZoneH();

	@NotNull Value safeZoneXAbs();

	@NotNull Value safeZoneWAbs();

	@NotNull Value getResolution();
}
