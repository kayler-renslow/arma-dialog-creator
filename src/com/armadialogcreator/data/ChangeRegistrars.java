package com.armadialogcreator.data;

import com.armadialogcreator.data.changeRegistrars.ControlClassChangeRegistrar;
import com.armadialogcreator.data.changeRegistrars.DisplayChangeRegistrar;
import org.jetbrains.annotations.NotNull;

/**
 A place to initiate all {@link ChangeRegistrar} instances.

 @author Kayler
 @since 08/10/2016. */
public final class ChangeRegistrars {
	ChangeRegistrars(@NotNull ApplicationData data) {
		DisplayChangeRegistrar displayChangeRegistrar = new DisplayChangeRegistrar(data);
		ControlClassChangeRegistrar controlClassChangeRegistrar = new ControlClassChangeRegistrar(data);
	}

}
