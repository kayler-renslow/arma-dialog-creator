package com.armadialogcreator.control;

import com.armadialogcreator.control.sv.SerializableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 Get {@link SerializableValue} instances from storage or create them dynamically.

 @author Kayler
 @since 11/22/2016 */
public interface DefaultValueProvider {

	/**
	 Get a default value for the given property lookup. This will not be called before
	 {@link #prefetchValues(List, Context)}.

	 @param lookup lookup
	 */
	@Nullable SerializableValue getDefaultValue(@NotNull ControlPropertyLookupConstant lookup);

	/**
	 Tells the provider that the given properties will be needed for a series of
	 {@link #getDefaultValue(ControlPropertyLookupConstant)} calls.

	 @param tofetch list to fetch that are being requested.
	 @param context the context for getting the values, or null if not to use a {@link Context}
	 @see ControlClassNameContext
	 @see ControlTypeContext
	 */
	void prefetchValues(@NotNull List<ControlPropertyLookupConstant> tofetch, @Nullable Context context);

	/**
	 Invoked after all {@link #getDefaultValue(ControlPropertyLookupConstant)} are executed.
	 This method should be used to clear up any memory from a previous {@link #prefetchValues(List, Context)} call
	 */
	void cleanup();

	/**
	 @see #prefetchValues(List, Context)
	 @since July 26, 2017
	 */
	class Context {
		private final Context parentContext;

		public Context() {
			this.parentContext = null;
		}

		public Context(@Nullable Context parentContext) {
			this.parentContext = parentContext;
		}

		/**
		 Get the {@link Context} that created this {@link Context}. Example use case of this is having a
		 {@link ControlTypeContext} as a parent and a {@link ControlClassNameContext} as its child. This could signify that
		 a {@link ControlClass} with a {@link ControlType} is trying to access a nested class in {@link ControlClass#getAllNestedClasses()}.
		 */
		@Nullable
		public Context getParentContext() {
			return parentContext;
		}
	}

	/**
	 A {@link Context} for retrieving values by a {@link ControlClass#getClassName()}

	 @since July 26, 2017
	 */
	class ControlClassNameContext extends Context {
		private final String controlClassName;

		public ControlClassNameContext(@NotNull String controlClassName) {
			this(null, controlClassName);
		}

		public ControlClassNameContext(@Nullable Context parentContext, @NotNull String controlClassName) {
			super(parentContext);
			this.controlClassName = controlClassName;
		}

		/** The class name of a {@link ControlClass} */
		@NotNull
		public String getControlClassName() {
			return controlClassName;
		}
	}

	/**
	 A {@link Context} for getting properties based upon a {@link ControlType}

	 @since July 26, 2017
	 */
	class ControlTypeContext extends Context {
		private final ControlType controlType;

		public ControlTypeContext(@NotNull ControlType controlType) {
			this(null, controlType);
		}

		public ControlTypeContext(@Nullable Context parentContext, @NotNull ControlType controlType) {
			super(parentContext);
			this.controlType = controlType;
		}

		@NotNull
		public ControlType getControlType() {
			return controlType;
		}
	}
}
