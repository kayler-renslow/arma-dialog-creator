package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 06/29/2017 */
public class TestArmaControlRenderer extends ArmaControlRenderer {

	public TestArmaControlRenderer(@NotNull ArmaControl control, @NotNull ArmaResolution resolution, @NotNull Env env) {
		super(control, resolution, env);
	}

	@Override
	public void requestRender() {

	}
}
