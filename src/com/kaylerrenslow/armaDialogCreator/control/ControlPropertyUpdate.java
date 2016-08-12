/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;

/**
 @author Kayler
 Used with {@link ControlClass#getUpdateGroup()}
 Created on 08/11/2016.
 */
public class ControlPropertyUpdate {
	private ControlProperty controlProperty;
	private SerializableValue oldValue;
	private SerializableValue newValue;
	
	public ControlPropertyUpdate(ControlProperty controlProperty, SerializableValue oldValue, SerializableValue newValue) {
		this.controlProperty = controlProperty;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	/** Get the ControlProperty that recieved an update */
	public ControlProperty getControlProperty() {
		return controlProperty;
	}
	
	/** Get the ControlProperty's old value */
	public SerializableValue getOldValue() {
		return oldValue;
	}
	
	/** Get the ControlProperty's new value */
	public SerializableValue getNewValue() {
		return newValue;
	}
	
	/** Set the old value. This method is used so that the class doesn't need to be re-instantiated. Keep package private.*/
	void setOldValue(SerializableValue oldValue) {
		this.oldValue = oldValue;
	}
	
	/** Set the new value. This method is used so that the class doesn't need to be re-instantiated. Keep package private. */
	void setNewValue(SerializableValue newValue) {
		this.newValue = newValue;
	}
}
