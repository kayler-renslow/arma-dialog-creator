package com.armadialogcreator.util;

/**
 Used for when data needs to be decoupled from observers, listeners, etc, and prepared for Java garbage collection.
 This is done via {@link #invalidate()}

 @author K
 @since 5/27/19 */
public interface DataInvalidator {
	/** See class level doc */
	void invalidate();
}
