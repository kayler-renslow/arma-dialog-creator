/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 Created by Kayler on 07/05/2016.
 */
public class UpdateListenerGroup<T> {
	private final ObservableList<UpdateListener<T>> updateListeners = FXCollections.observableArrayList(new ArrayList<>());

	public ObservableList<UpdateListener<T>> getUpdateListeners() {
		return updateListeners;
	}

	public void addListener(UpdateListener<T> listener) {
		updateListeners.add(listener);
	}

	public boolean removeUpdateListener(UpdateListener<T> listener) {
		return updateListeners.remove(listener);
	}

	public void update(@Nullable T data) {
		for (UpdateListener<T> updateListener : updateListeners) {
			updateListener.update(data);
		}
	}
}
