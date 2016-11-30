package com.kaylerrenslow.armaDialogCreator.util;

/**
 Created by Kayler on 09/08/2016.
 */
public class KeyValue<K, V> {
	public static final KeyValue[] EMPTY = new KeyValue[0];

	private final K key;
	private V value;

	public KeyValue(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "KeyValue{" +
				"key=" + key +
				", value=" + value +
				'}';
	}
}
