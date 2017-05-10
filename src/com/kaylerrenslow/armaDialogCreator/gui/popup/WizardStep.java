package com.kaylerrenslow.armaDialogCreator.gui.popup;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;


/**
 @author Kayler
 @since 11/23/2016 */
public abstract class WizardStep<V extends Node> {
	protected final V content;
	private boolean hasBeenShown = false;
	private int presentCount = 0;
	/** Boolean property that is initially true. See {@link #completeStep(boolean)} for more info */
	protected final BooleanProperty stepIsCompleteProperty = new SimpleBooleanProperty(true);

	public WizardStep(@NotNull V content) {
		this.content = content;
	}

	@NotNull
	public V getContent() {
		return content;
	}

	/** Invoked when step has been presented to user. If overridden, this method must call the super method!*/
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

	/**
	 Invoked when the step is no longer being presented to user

	 @param movingForward true if the next step is ahead of this step, false if the next step is behind this step
	 */
	protected void stepLeft(boolean movingForward) {

	}

	/** Set {@link #stepIsCompleteProperty} equal to complete */
	protected final void completeStep(boolean complete) {
		stepIsCompleteProperty.set(complete);
	}

	/**
	 Returns whatever {@link #stepIsCompleteProperty} is set to. Default value is true.

	 @return true if the wizard can progress, false if the step isn't complete yet.
	 */
	protected final boolean stepIsComplete() {
		return stepIsCompleteProperty.get();
	}
}
