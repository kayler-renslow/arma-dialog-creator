package com.armadialogcreator.gui.main.treeview.dataCreator;

import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.impl.ProgressControl;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.control.ControlType;
import com.armadialogcreator.control.SpecificationRegistry;
import com.armadialogcreator.expression.Env;
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
