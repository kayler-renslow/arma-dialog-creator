package com.kaylerrenslow.armaDialogCreator.expression;

import com.kaylerrenslow.armaDialogCreator.util.KeyValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Environment for evaluating identifiers for {@link ExpressionInterpreter}

 @author Kayler
 @since 07/14/2016. */
public interface Env extends Iterable<KeyValue<String, Value>> {
	/** @return the value for the given identifier. If returns null, means identifier couldn't be resolved to a value. */
	@Nullable
	Value getValue(@NotNull String identifier);

	/**
	 Associates the specified value with the specified identifier in this env.
	 If the env previously contained a mapping for the identifier, the old value is replaced.

	 @return the previous value associated with identifier, or null if there was no mapping for identifier.
	 (A null return can also indicate that the map previously associated null with identifier.)
	 */
	@Nullable
	Value put(@NotNull String identifier, @Nullable Value v);

	/**
	 Removes the mapping for the specified identifier from this env if present.

	 @return the previous value associated with identifier, or null if there was no mapping for identifier.
	 (A null return can also indicate that the map previously associated null with identifier.)
	 */
	@Nullable
	Value remove(@NotNull String identifier);

	/** @return the instance, or null if unary commands should not be handled */
	@Nullable UnaryCommandValueProvider getUnaryCommandValueProvider();

	/** @return all current mapped identifiers (identifiers that have non null values) */
	@NotNull String[] getMappedIdentifiers();
}
