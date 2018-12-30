package com.armadialogcreator.control;

import com.armadialogcreator.data.DataKeys;
import com.armadialogcreator.main.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 Used for getting documentation for {@link ControlPropertyLookupConstant}

 @author Kayler
 @since 06/16/2017 */
public class ControlPropertyDocumentationProvider {
	private final ResourceBundle defaultBundle = getBundle("_DefaultBundle");

	@NotNull
	private String getString(@NotNull ResourceBundle bundle, @NotNull String propertyName) {
		return bundle.getString(propertyName);
	}

	@NotNull
	private static ResourceBundle getBundle(@NotNull String name) {
		return Lang.getBundle("controlPropertyLookupBundles/" + name);
	}

	/**
	 Get documentation for the given {@link ControlProperty}.
	 The documentation will come from {@link DataKeys#CONTROL_PROPERTY_DOCUMENTATION_PATH} using {@link ControlProperty#getUserData()}<p>
	 <p>
	 The documentation is organized by having a default bundle and a bundle for every {@link ControlType}.
	 When an entry isn't found in the {@link ControlType}'s bundle, the default bundle will be used.
	 If an entry can't be found there either, "No Documentation" will be returned.
	 <p>
	 A {@link ControlProperty#getName()} is used as a key for a matched bundle to retrieve the documentation.

	 @param property what to get the documentation for
	 @return the documentation
	 */
	@NotNull
	public String getDocumentation(@NotNull ControlProperty property) {
		String path = DataKeys.CONTROL_PROPERTY_DOCUMENTATION_PATH.get(property.getUserData());

		if (path != null) {
			try {
				return getBundle(path).getString(property.getName());
			} catch (MissingResourceException ignore) {
			}
		}

		//attempt to get the property from default
		try {
			return getString(defaultBundle, property.getName());
		} catch (MissingResourceException ignore) {
		}

		//not in control type's bundle and not in default, so there must be no documentation
		return getString(defaultBundle, "no_documentation");
	}
}
