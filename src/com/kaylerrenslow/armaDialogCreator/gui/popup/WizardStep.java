package com.kaylerrenslow.armaDialogCreator.gui.popup;

import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;


/**
 @author Kayler
 @since 11/23/2016 */
public abstract class WizardStep<V extends Node> {
	protected final V content;
	private boolean hasBeenShown = false;
	private int presentCount = 0;

	public WizardStep(@NotNull V content) {
		this.content = content;
	}

	@NotNull
	public V getContent() {
		return content;
	}

	/** Invoked when step has been presented to user. */
	protected void stepPresented() {
		hasBeenShown = true;
		presentCount++;
	}

	/** @return true if the step has been presented at least 1 time. Set to true via {@link #stepPresented()}. */
	protected boolean hasBeenPresented() {
		return hasBeenShown;
	}

	/** @return the number of times this step has been presented. The value will be incremented with each {@link #stepPresented()} call */
	public int getPresentCount() {
		return presentCount;
	}

	/** Invoked when the step is no longer being presented to user */
	protected void stepLeft() {

	}

	/** Return true if the wizard can progress, false if the step isn't complete yet */
	abstract protected boolean stepIsComplete();
}
