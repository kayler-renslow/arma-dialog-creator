package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 Used for getting documentation for {@link ControlPropertyLookupConstant}

 @author Kayler
 @since 06/16/2017 */
public class ControlPropertyDocumentationProvider {
	private final ResourceBundle defaultBundle = getBundle("_DefaultBundle");

	/**
	 Get documentation for the given {@link ControlPropertyLookupConstant} and {@link ControlType}.<p>
	 <p>
	 The documentation is organized by having a default bundle and a bundle specific for a {@link ControlType}.
	 When an entry isn't found in the {@link ControlType}'s bundle, the default bundle will be used.
	 If an entry can't be found there either, "No Documentation" will be returned.

	 @param lookup lookup to get the documentation for
	 @param type {@link ControlType} to use, or null if the documentation should be fetched from the default bundle
	 @return the documentation
	 */
	@NotNull
	public String getDocumentation(@NotNull ControlPropertyLookupConstant lookup, @Nullable ControlType type) {
		//check the control type's bundle
		if (type != null) {
			try {
				return getBundle(type.name()).getString(lookup.getPropertyName());
			} catch (MissingResourceException ignore) {
			}
		}

		//attempt to get the property from default
		try {
			return getString(defaultBundle, lookup.getPropertyName());
		} catch (MissingResourceException ignore) {
		}

		//not in control type's bundle and not in default, so there must be no documentation
		return getString(defaultBundle, "no_documentation");
	}

	@NotNull
	private String getString(@NotNull ResourceBundle bundle, @NotNull String propertyName) {
		return bundle.getString(propertyName);
	}

	@NotNull
	private static ResourceBundle getBundle(@NotNull String name) {
		return Lang.getBundle("controlPropertyLookupBundles/" + name);
	}
}
