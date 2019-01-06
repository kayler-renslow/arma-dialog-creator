package com.armadialogcreator.expression;

import com.armadialogcreator.util.KeyValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 A simple implementation of {@link Env} that allows for adding identifiers to the env and removing identifiers from the env.
 All identifiers case don't matter (i.e. AAA==aaa)

 @author Kayler
 @since 07/15/2016. */
public class SimpleEnv implements Env {
	/** Map for identifiers to values */
	protected final HashMap<String, Value> map = new HashMap<>();
	private UnaryCommandValueProvider unaryCommandProvider;

	public SimpleEnv() {

	}

	public SimpleEnv(@Nullable UnaryCommandValueProvider unaryCommandProvider) {
		this.unaryCommandProvider = unaryCommandProvider;
	}

	@Override
	public Value put(@NotNull String identifier, Value v) {
		return map.put(identifier.toLowerCase(), v);
	}

	@Override
	public Value remove(@NotNull String identifier) {
		return map.remove(identifier.toLowerCase());
	}

	public void setUnaryCommandProvider(@Nullable UnaryCommandValueProvider unaryCommandProvider) {
		this.unaryCommandProvider = unaryCommandProvider;
	}

	@Override
	@Nullable
	public UnaryCommandValueProvider getUnaryCommandValueProvider() {
		return unaryCommandProvider;
	}

	@NotNull
	@Override
	public String[] getMappedIdentifiers() {
		String[] idents = new String[map.size()];
		int i = 0;
		for (String ident : map.keySet()) {
			idents[i++] = ident;
		}
		return idents;
	}

	@Override
	@Nullable
	public Value getValue(@NotNull String identifier) {
		return map.get(identifier.toLowerCase());
	}

	@Override
	public String toString() {
		return map.toString();
	}

	@NotNull
	@Override
	public Iterator<KeyValue<String, Value>> iterator() {
		return new MyIterator();
	}

	private class MyIterator implements Iterator<KeyValue<String, Value>> {

		private final Iterator<Map.Entry<String, Value>> iter = map.entrySet().iterator();

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public KeyValue<String, Value> next() {
			Map.Entry<String, Value> next = iter.next();
			return new KeyValue<>(next.getKey(), next.getValue());
		}
	}
}
