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

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 @author Kayler
 Instead of passing around the classes and saving them in the project save, this is a more robust way of storing the class since the Renderer class can be moved, renamed, or replaced all together.
 Created on 08/03/2016. */
public enum RendererLookup {
	/** Renderer for a basic static control */
	STATIC(0, StaticRenderer.class), CONTROL_GROUP(1, ControlGroupRenderer.class);
	
	/** Unique id for the lookup. */
	public final int id;
	/** Class used for creating the renderer. The renderer is created with reflection. */
	public final Class<? extends ArmaControlRenderer> rendererClass;
	
	RendererLookup(int id, Class<? extends ArmaControlRenderer> rendererClass) {
		this.id = id;
		this.rendererClass = rendererClass;
		for (Integer usedId : IdChecker.usedIds) {
			if (usedId == id) {
				throw new IllegalStateException("duplicate id (" + usedId + ") between for " + this.name());
			}
		}
		IdChecker.usedIds.add(id);
	}
	
	/** @throws IllegalArgumentException when id couldn't be matched */
	@NotNull
	public static RendererLookup getById(int id) {
		for (RendererLookup lookup : values()) {
			if (lookup.id == id) {
				return lookup;
			}
		}
		throw new IllegalArgumentException("bad id:" + id);
	}
	
	private static class IdChecker {
		private static final LinkedList<Integer> usedIds = new LinkedList<>();
	}
}
