package com.kaylerrenslow.armaDialogCreator.gui.canvas.api;

/**
 Created by Kayler on 08/12/2016.
 */
public interface ControlListChangeListener<C extends CanvasControl> {
	/**
	 A change was invoked on the given control list. Only one operation is performed at once. Therefore, only one of the following will be true:<br>
	 <ul>
	 <li>{@link ControlListChange#wasSet()}</li>
	 <li>{@link ControlListChange#wasAdded()}</li>
	 <li>{@link ControlListChange#wasRemoved()}</li>
	 <li>{@link ControlListChange#wasMoved()}</li>
	 </ul>
	 
	 @param controlList list the change happened to
	 @param change the change that occurred
	 */
	void onChanged(ControlList<C> controlList, ControlListChange<C> change);
}
