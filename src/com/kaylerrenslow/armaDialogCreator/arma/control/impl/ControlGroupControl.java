/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.control.sv.ControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/04/2016.
 */
public class ControlGroupControl extends ArmaControlGroup {
	public ControlGroupControl(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Env env) {
		super(name, resolution, RendererLookup.CONTROL_GROUP, env);
		defineType(ControlType.CONTROLS_GROUP);
	}

	public ControlGroupControl(@NotNull String name, int idc, @NotNull ControlStyleGroup style, Expression x, Expression y, Expression width, Expression height, @NotNull ArmaResolution resolution, @NotNull Env env) {
		super(name, idc, ControlType.CONTROLS_GROUP, style, x, y, width, height, resolution, RendererLookup.CONTROL_GROUP, env);
	}

}
