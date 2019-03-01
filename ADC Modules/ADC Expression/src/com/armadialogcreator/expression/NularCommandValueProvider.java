package com.armadialogcreator.expression;

import org.jetbrains.annotations.NotNull;

/**
 Each method in this interface is for getting a value for a unary command.
 The name of the method is the name of the command.
 <p>
 <b>It is required that the name of the method match the name of the command,
 however, case sensitivity doesn't matter.</b>
 <p>
 Any methods that aren't meant for commands should have a _ at the start of the name.

 @author Kayler
 @since 06/28/2017 */
public interface NularCommandValueProvider {

	@NotNull Value safeZoneX();

	@NotNull Value safeZoneY();

	@NotNull Value safeZoneW();

	@NotNull Value safeZoneH();

	/** default implementation returns {@link #safeZoneX()} */
	@NotNull
	default Value safeZoneXAbs() {
		return safeZoneX();
	}

	/** default implementation returns {@link #safeZoneW()} */
	@NotNull
	default Value safeZoneWAbs() {
		return safeZoneW();
	}

	@NotNull Value getResolution();

	/**
	 A helper method for {@link #getResolution()}.

	 @return a value for {@link #getResolution()}
	 */
	@NotNull
	static Value helper_getResolution(
			double width,
			double height,
			double viewportWidth,
			double viewportHeight,
			double aspectRatio,
			double uiScale
	) {
		return new Value.Array(
				new Value.NumVal(width),
				new Value.NumVal(height),
				new Value.NumVal(viewportWidth),
				new Value.NumVal(viewportHeight),
				new Value.NumVal(aspectRatio),
				new Value.NumVal(uiScale)
		);
	}
}
