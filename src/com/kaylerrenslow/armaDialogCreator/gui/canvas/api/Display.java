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

import java.util.Iterator;

/**
 Created by Kayler on 08/04/2016.
 */
public interface Display<C extends Control> extends ControlHolder<C> {
	
	
	/** Get controls that are rendered first and have no user interaction */
	DisplayControlList<C> getBackgroundControls();

	@Override
	DisplayControlList<C> getControls();

	/**
	 Get an iterator that will cycle through the background controls ({@link #getBackgroundControls()}) and then main controls ({@link #getControls()}). This will not iterate through controls
	 within possible control groups in either controls list or background controls list.
	 
	 @param backwards if true, the iterator will iterate through the other controls from size-1 to 0 and then the background controls in reverse (starting from size-1 to 0).<br>
	 if false, will iterate through the background controls from 0 to size-1 and then other controls from 0 to size-1
	 */
	Iterator<C> iteratorForAllControls(boolean backwards);
		
	void resolutionUpdate(Resolution newResolution);
	
}

