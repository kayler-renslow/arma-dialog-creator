package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/04/2019 */
public interface ObserverListener {
	/**
	 Invoked when the parent {@link Observer} wants to be destroyed and this listener will no longer be relevant.
	 This is invoked after the parent {@link Observer#invalidate()} method is invoked.
	 Default implementation does nothing.
	 */
	default void observerDeleted() {

	}

	/**
	 Invoked when parent {@link Observer} has {@link Observer#clearListeners()} or {@link Observer#removeListener(ObserverListener)} invoked.
	 Basically this means that the listener will no longer receive updates.
	 Default implementation does nothing.
	 */
	default void listenerDetached() {

	}

	/**
	 Default implementation returns empty String.

	 @return a name for this listener that may be presented to the user (describers what is listening to the {@link Observer}).
	 */
	default @NotNull String getListenerName() {
		return "";
	}
}
