/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

import com.kaylerrenslow.armaDialogCreator.util.ListMergeIterator;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;

import java.util.Iterator;

/**
 Created by Kayler on 08/04/2016.
 */
public interface Display<C extends Control> extends ControlHolder<C> {
	
	/** Any time the display or its controls get updated, notify this group. */
	UpdateListenerGroup<Object> getUpdateListenerGroup();
	
	/** Get controls that are rendered first and have no user interaction */
	ReadOnlyList<C> getBackgroundControls();
	
	/**
	 Get an iterator that will cycle through the background controls ({@link #getBackgroundControls()}) and then main controls ({@link #getControls()})
	 
	 @param backwards if true, the iterator will iterate through the other controls from size-1 to 0 and then the background controls in reverse (starting from size-1 to 0).<br>
	 if false, will iterate through the background controls from 0 to size-1 and then other controls from 0 to size-1
	 */
	default Iterator<C> iteratorForAllControls(boolean backwards) {
		return new ListMergeIterator<>(backwards, new ReadOnlyList[]{getControls(), getBackgroundControls()});
	}
	
	/**
	 Return true if the contorl is a background control (inside {@link #getBackgroundControls()}), false otherwise.<br>
	 Default implementation is <pre>{@link #getBackgroundControls()}.contains(control)</pre>
	 */
	default boolean isBackgroundControl(C control) {
		return getBackgroundControls().contains(control);
	}
	
	void addBackgroundControl(C control);
	
	void addBackgroundControl(int index, C toAdd);
	
	int indexOfBackgroundControl(C control);
	
	boolean removeBackgroundControl(C control);
	
	void resolutionUpdate(Resolution newResolution);
	
}

