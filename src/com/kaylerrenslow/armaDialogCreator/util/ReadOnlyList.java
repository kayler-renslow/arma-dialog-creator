package com.kaylerrenslow.armaDialogCreator.util;

import java.util.Iterator;
import java.util.List;

/**
 @author Kayler
 Creates a wrapper around a list such that it is read only
 Created on 06/07/2016. */
public class ReadOnlyList<E> implements Iterable<E>{
	private final List<E> dataList;

	public ReadOnlyList(List<E> dataList) {
		this.dataList = dataList;
	}

	public int size() {
		return dataList.size();
	}

	public boolean isEmpty() {
		return dataList.isEmpty();
	}

	public boolean contains(Object o) {
		return dataList.contains(o);
	}

	public E get(int index) {
		return dataList.get(index);
	}

	public Iterator<E> iterator(){
		return dataList.iterator();
	}
	
}
