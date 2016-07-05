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
