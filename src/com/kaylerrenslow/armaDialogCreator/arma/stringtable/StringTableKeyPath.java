package com.kaylerrenslow.armaDialogCreator.arma.stringtable;

import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyValueObserver;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

/**
 @author Kayler
 @since 01/01/2017 */
public class StringTableKeyPath implements Observable {
	private final ValueObserver<String> packageNameObserver = new ValueObserver<>(null);
	private final ObservableList<String> containers = FXCollections.observableArrayList();

	public StringTableKeyPath(@Nullable String packageName, @NotNull String... containers) {
		this.packageNameObserver.updateValue(packageName);
		Collections.addAll(this.containers, containers);
	}

	@NotNull
	public ReadOnlyValueObserver<String> getPackageNameObserver() {
		return packageNameObserver.getReadOnlyValueObserver();
	}

	@NotNull
	public String getPackageName() {
		return packageNameObserver.getValue() == null ? "" : packageNameObserver.getValue();
	}

	/** @return true if {@link #getPackageNameObserver()}.getValue()==null, false otherwise */
	public boolean noPackageName() {
		return packageNameObserver.getValue() == null;
	}

	public void setPackageName(@Nullable String packageName) {
		if (packageName == null) {
			packageNameObserver.updateValue(null);
		} else {
			packageNameObserver.updateValue(packageName);
		}
	}

	/** Get the containers. Index 0=child of package. Index 1=child of index 0. Index 2 = child of index 1. If empty, then is a child of package. */
	@NotNull
	public ObservableList<String> getContainers() {
		return containers;
	}

	/** @return true if {@link #getContainers()}.size()==0, false otherwise */
	public boolean noContainer() {
		return getContainers().size() == 0;
	}

	@Override
	public void addListener(InvalidationListener listener) {
		containers.addListener(listener);
		packageNameObserver.addListener(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		containers.removeListener(listener);
		packageNameObserver.removeListener(listener);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof StringTableKeyPath) {
			StringTableKeyPath other = (StringTableKeyPath) o;
			return getPackageName().equals(other.getPackageName()) && getContainers().equals(other.getContainers());
		}
		return false;
	}

	@NotNull
	public StringTableKeyPath deepCopy() {
		StringTableKeyPath path = new StringTableKeyPath(packageNameObserver.getValue());
		path.getContainers().addAll(getContainers());
		return path;
	}

	@Override
	public String toString() {
		return "StringTableKeyPath{" +
				"packageName=" + getPackageName() +
				", containers=" + containers +
				'}';
	}
}
