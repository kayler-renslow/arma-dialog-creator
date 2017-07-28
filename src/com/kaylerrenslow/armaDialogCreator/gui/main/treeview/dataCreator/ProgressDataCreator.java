package com.kaylerrenslow.armaDialogCreator.gui.main.treeview.dataCreator;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ProgressControl;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.control.SpecificationRegistry;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 7/27/2017 */
public class ProgressDataCreator extends GenericDataCreator {
	public static final ProgressDataCreator INSTANCE = new ProgressDataCreator();

	public ProgressDataCreator() {
		super(ControlType.Progress);
	}

	@Override
	@NotNull
	public ArmaControl create(@NotNull String className, @NotNull ArmaResolution resolution, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		return new ProgressControl(className, resolution, env, registry);
	}
}
