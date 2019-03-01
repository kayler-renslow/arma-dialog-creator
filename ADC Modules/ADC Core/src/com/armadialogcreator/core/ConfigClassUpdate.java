package com.armadialogcreator.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/12/2019 */
public interface ConfigClassUpdate {
	enum Type {
		ClassName,
		ExtendClass,
		AddProperty,
		RemoveProperty,
		InheritProperty,
		AddNestedClass,
		RemoveNestedClass
	}

	@NotNull Type getType();

	class ClassNameUpdate implements ConfigClassUpdate {
		private final String oldName;
		private final String newName;

		public ClassNameUpdate(@NotNull String oldName, @NotNull String newName) {
			this.oldName = oldName;
			this.newName = newName;
		}

		@NotNull
		public String getOldName() {
			return oldName;
		}

		@NotNull
		public String getNewName() {
			return newName;
		}

		@Override
		@NotNull
		public Type getType() {
			return Type.ClassName;
		}
	}

	class ExtendClassUpdate implements ConfigClassUpdate {

		private final ConfigClass oldExtendClass;
		private final ConfigClass newExtendClass;

		public ExtendClassUpdate(@Nullable ConfigClass oldExtendClass, @Nullable ConfigClass newExtendClass) {
			this.oldExtendClass = oldExtendClass;
			this.newExtendClass = newExtendClass;
		}

		@Nullable
		public ConfigClass getOldExtendClass() {
			return oldExtendClass;
		}

		@Nullable
		public ConfigClass getNewExtendClass() {
			return newExtendClass;
		}

		@Override
		@NotNull
		public Type getType() {
			return Type.ExtendClass;
		}
	}

	class AddPropertyUpdate implements ConfigClassUpdate {

		private final ImmutableConfigProperty addedProperty;

		public AddPropertyUpdate(@NotNull ConfigProperty addedProperty) {
			this.addedProperty = new ImmutableConfigProperty(addedProperty);
		}

		@NotNull
		public ImmutableConfigProperty getAddedProperty() {
			return addedProperty;
		}

		@Override
		@NotNull
		public Type getType() {
			return Type.AddProperty;
		}
	}

	class RemovePropertyUpdate implements ConfigClassUpdate {

		private final ImmutableConfigProperty removedProperty;

		public RemovePropertyUpdate(@NotNull ConfigProperty removedProperty) {
			this.removedProperty = new ImmutableConfigProperty(removedProperty);
		}

		@NotNull
		public ImmutableConfigProperty getRemovedProperty() {
			return removedProperty;
		}

		@Override
		@NotNull
		public Type getType() {
			return Type.RemoveProperty;
		}
	}

	class InheritPropertyUpdate implements ConfigClassUpdate {

		private final String inheritedPropertyName;

		public InheritPropertyUpdate(@NotNull String inheritedPropertyName) {
			this.inheritedPropertyName = inheritedPropertyName;
		}

		public InheritPropertyUpdate(@NotNull ConfigPropertyKey key) {
			this.inheritedPropertyName = key.getPropertyName();
		}

		@NotNull
		public String getInheritedPropertyName() {
			return inheritedPropertyName;
		}

		@Override
		@NotNull
		public Type getType() {
			return Type.InheritProperty;
		}
	}

	class AddNestedClassUpdate implements ConfigClassUpdate {

		private final ConfigClass addedNestedClass;

		public AddNestedClassUpdate(@NotNull ConfigClass addedNestedClass) {
			this.addedNestedClass = addedNestedClass;
		}

		@NotNull
		public ConfigClass getAddedNestedClass() {
			return addedNestedClass;
		}

		@Override
		@NotNull
		public Type getType() {
			return Type.AddNestedClass;
		}
	}

	class RemoveNestedClassUpdate implements ConfigClassUpdate {

		private final ConfigClass removedNestedClass;

		public RemoveNestedClassUpdate(@NotNull ConfigClass removedNestedClass) {
			this.removedNestedClass = removedNestedClass;
		}

		@NotNull
		public ConfigClass getRemovedNestedClass() {
			return removedNestedClass;
		}

		@Override
		@NotNull
		public Type getType() {
			return Type.RemoveNestedClass;
		}
	}
}
