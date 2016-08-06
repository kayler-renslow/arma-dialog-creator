/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.expression;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 @author Kayler
 A simple implementation of {@link Env} that allows for adding identifers to the env and removing identifiers from the env.
 Created on 07/15/2016. */
public class SimpleEnv implements Env {
	private HashMap<String, Value> map = new HashMap<>();

	/**
	 Associates the specified value with the specified identifier in this env. If the env previously contained a mapping for the identifier, the old value is replaced.

	 @return the previous value associated with identifier, or null if there was no mapping for identifier. (A null return can also indicate that the map previously associated null with identifier.)
	 */
	public Value put(String identifier, Value v) {
		return map.put(identifier, v);
	}

	/**
	 Removes the mapping for the specified identifier from this env if present.

	 @return the previous value associated with identifier, or null if there was no mapping for identifier. (A null return can also indicate that the map previously associated null with identifier.)
	 */
	public Value remove(String identifier) {
		return map.remove(identifier);
	}

	@Override
	public @Nullable Value getValue(String identifier) {
		return map.get(identifier);
	}
}
