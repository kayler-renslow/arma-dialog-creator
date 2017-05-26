package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.util.KeyValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 A wrapper {@link Env}. No values are ever removable in the wrapped {@link Env}.
 Useful for creating temporary environments from existing ones without copying an entire environment.

 @author Kayler
 @see #put(String, Value)
 @since 05/25/2017 */
public class DisposableWrapperEnv extends SimpleEnv {
	private Env env;

	/**
	 @param env environment to wrap
	 @see #put(String, Value)
	 */
	public DisposableWrapperEnv(@NotNull Env env) {
		this.env = env;
	}

	/**
	 Put a value in this {@link Env} or {@link #getWrappedEnv()}.
	 Conditions for placing in this {@link Env} (only one condition needs to be satisfied):
	 <ul>
	 <li>this {@link Env} contains a value for the given identifier</li>
	 <li>{@link #getWrappedEnv()} doesn't contain a value for the given identifier</li>
	 </ul>
	 <br>
	 If all of those conditions fail, only then will the value be placed in {@link #getWrappedEnv()}.

	 @param identifier identifier
	 @param v value to place
	 @return the value that was replaced, or null if nothing was replaced.
	 @see #forcePut(String, Value)
	 */
	@Override
	@Nullable
	public Value put(@NotNull String identifier, @Nullable Value v) {
		if (env.getValue(identifier) == null || getValue(identifier) != null) {
			return super.put(identifier, v);
		}
		return env.put(identifier, v);
	}

	/**
	 Force placing a value in this {@link Env} and not in {@link #getWrappedEnv()}.

	 @return the replaced value, or null if nothing was replaced
	 @see #put(String, Value)
	 */
	@Nullable
	public Value forcePut(@NotNull String identifier, @Nullable Value v) {
		return map.put(identifier, v);
	}

	/** No values are ever removable in the wrapped {@link Env}. */
	@Override
	public Value remove(@NotNull String identifier) {
		return super.remove(identifier);
	}

	/** @return the wrapped {@link Env} */
	@NotNull
	public Env getWrappedEnv() {
		return env;
	}

	/**
	 First check if this environment contains the identifier. If it does, it will return that value.
	 If it doesn't, will invoke {@link Env#getValue(String)} on the wrapper {@link Env}

	 @return value from this env, or return the wrapped env's value
	 */
	@Override
	@Nullable
	public Value getValue(@NotNull String identifier) {
		Value v = super.getValue(identifier);
		if (v != null) {
			return v;
		}
		return env.getValue(identifier);
	}

	@Override
	@NotNull
	public Iterator<KeyValue<String, Value>> iterator() {
		return new MyIterator(super.iterator(), env.iterator());
	}

	private class MyIterator implements Iterator<KeyValue<String, Value>> {

		private final Iterator<KeyValue<String, Value>> iter;

		public MyIterator(@NotNull Iterator<KeyValue<String, Value>> myIter, Iterator<KeyValue<String, Value>> wrapIter) {
			//merge envs
			Env env = new SimpleEnv();
			//insert the wrapped env's data first so that the non-wrapped env's data can replace any matched identifiers
			while (wrapIter.hasNext()) {
				KeyValue<String, Value> kv = wrapIter.next();
				env.put(kv.getKey(), kv.getValue());
			}
			while (myIter.hasNext()) {
				KeyValue<String, Value> kv = myIter.next();
				env.put(kv.getKey(), kv.getValue());
			}
			iter = env.iterator();
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public KeyValue<String, Value> next() {
			return iter.next();
		}
	}
}
