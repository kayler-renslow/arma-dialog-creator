package com.armadialogcreator.core;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 @author K
 @since 01/03/2019 */
public interface ConfigPropertyKey {
	/** @return the name associated with the property. Is not guaranteed to be unique. */
	@NotNull
	String getPropertyName();

	default boolean nameEquals(@NotNull ConfigPropertyKey key) {
		return this.getPropertyName().equalsIgnoreCase(key.getPropertyName());
	}

	default boolean nameEquals(@NotNull String name) {
		return this.getPropertyName().equalsIgnoreCase(name);
	}

	/** @return the sort priority for {@link #PRIORITY_SORT}. By default, returns 100 */
	default int priority() {
		return 100;
	}

	Comparator<ConfigPropertyKey> PRIORITY_SORT = new Comparator<>() {
		@Override
		public int compare(ConfigPropertyKey o1, ConfigPropertyKey o2) {
			if (o1.priority() == o2.priority()) {
				return o1.getPropertyName().compareTo(o2.getPropertyName());
			}
			if (o1.priority() < o2.priority()) {
				return -1;
			}
			return 1;
		}
	};

	class Simple implements ConfigPropertyKey {
		private final String name;
		private final int priority;

		public Simple(@NotNull String name) {
			this.name = name;
			this.priority = Integer.MAX_VALUE;
		}

		public Simple(@NotNull String name, int priority) {
			this.name = name;
			this.priority = priority;
		}

		@Override
		@NotNull
		public String getPropertyName() {
			return name;
		}

		@Override
		public int hashCode() {
			return name.hashCode();
		}

		@Override
		public int priority() {
			return priority;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof ConfigPropertyKey)) {
				return false;
			}
			ConfigPropertyKey other = (ConfigPropertyKey) obj;
			return this.nameEquals(other);
		}
	}
}
