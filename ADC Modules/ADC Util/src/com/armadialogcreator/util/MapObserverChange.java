package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @see MapObserver
 @see MapObserver#addListener(MapObserverListener)
 @since 1/7/2019 */
public class MapObserverChange<K, V> {
	public enum ChangeType {
		Put, Replace, Remove,
		/** This change will be created prior to the map being cleared. */
		Clear,
		Move
	}

	private final MapObserver<K, V> modifiedMap;
	private MapObserverChangePut<K, V> put;
	private MapObserverChangeRemove<K, V> removed;
	private MapObserverChangeMove<K, V> moved;
	private MapObserverChangeReplace<K, V> replaced;
	private ChangeType changeType = null;

	protected MapObserverChange(@NotNull MapObserver<K, V> modifiedMap) {
		this.modifiedMap = modifiedMap;
	}

	@NotNull
	public MapObserverChange.ChangeType getChangeType() {
		return changeType;
	}

	void setReplaced(MapObserverChangeReplace<K, V> set) {
		this.replaced = set;
		checkType();
		changeType = ChangeType.Replace;
	}

	void setPut(MapObserverChangePut<K, V> put) {
		this.put = put;
		checkType();
		changeType = ChangeType.Put;
	}

	void setRemoved(MapObserverChangeRemove<K, V> removed) {
		this.removed = removed;
		checkType();
		changeType = ChangeType.Remove;
	}

	void setMoved(MapObserverChangeMove<K, V> move) {
		this.moved = move;
		checkType();
		changeType = ChangeType.Move;
	}

	void setCleared() {
		checkType();
		changeType = ChangeType.Clear;
	}

	private void checkType() {
		if (changeType != null) {
			throw new IllegalStateException("only one changeType is allowed at once");
		}
	}

	/** Get the list that had the change (where the change was triggered) */
	@NotNull
	public MapObserver<K, V> getModifiedMapObserver() {
		return modifiedMap;
	}

	public boolean wasReplaced() {
		return changeType == ChangeType.Replace;
	}

	/** @return {@link #getChangeType()}=={@link ChangeType#Put} */
	public boolean wasPut() {
		return changeType == ChangeType.Put;
	}

	/** @return {@link #getChangeType()}=={@link ChangeType#Remove} */
	public boolean wasRemoved() {
		return changeType == ChangeType.Remove;
	}

	/** @return {@link #getChangeType()}=={@link ChangeType#Move} */
	public boolean wasMoved() {
		return changeType == ChangeType.Move;
	}

	/** @return {@link #getChangeType()}=={@link ChangeType#Clear} */
	public boolean wasCleared() {
		return changeType == ChangeType.Clear;
	}

	/**
	 @return the {@link MapObserverChangeReplace} update
	 @throws IllegalStateException when {@link #wasReplaced()}==false
	 */
	@NotNull
	public MapObserverChangeReplace<K, V> getReplace() {
		if (!wasReplaced()) {
			throw new IllegalStateException("not a replace change");
		}
		return replaced;
	}

	/**
	 @return the {@link MapObserverChangePut} update
	 @throws IllegalStateException when {@link #wasPut()}==false
	 */
	@NotNull
	public MapObserverChangePut<K, V> getPut() {
		if (!wasPut()) {
			throw new IllegalStateException("not an add change");
		}
		return put;
	}

	/**
	 @return the {@link MapObserverChangeRemove} update
	 @throws IllegalStateException when {@link #wasRemoved()}==false
	 */
	@NotNull
	public MapObserverChangeRemove<K, V> getRemoved() {
		if (!wasRemoved()) {
			throw new IllegalStateException("not a remove change");
		}
		return removed;
	}

	/**
	 @return the {@link MapObserverChangeMove} update
	 @throws IllegalStateException when {@link #wasMoved()}==false
	 */
	@NotNull
	public MapObserverChangeMove<K, V> getMoved() {
		if (!wasMoved()) {
			throw new IllegalStateException("not a move change");
		}
		return moved;
	}


}
