package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 Instead of passing around the classes and saving them in the project save, this is a more robust way of storing the class since the Renderer class can be moved, renamed, or replaced all together.
 @author Kayler
 @since 08/03/2016. */
public enum RendererLookup {
	/** Renderer for a basic static control */
	STATIC(0, StaticRenderer.class),
	CONTROL_GROUP(1, ControlGroupRenderer.class),
	BUTTON(2, ButtonRenderer.class),
	/**Renderer for tests. DO NOT USE FOR CLIENT CODE.*/
	TEST(-1, ArmaControlRenderer.class);
	
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
