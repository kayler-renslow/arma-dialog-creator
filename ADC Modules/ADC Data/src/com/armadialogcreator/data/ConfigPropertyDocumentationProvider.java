package com.armadialogcreator.data;

import com.armadialogcreator.core.ConfigPropertyKey;
import com.armadialogcreator.lang.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.MissingResourceException;

/**
 @author K
 @since 4/2/19 */
public class ConfigPropertyDocumentationProvider {
	@NotNull
	public static String getDocumentation(@NotNull String bundle, @NotNull ConfigPropertyKey key) {
		try {
			return Lang.ConfigPropertyLookupBundle(bundle).getString(key.getPropertyName());
		} catch (MissingResourceException ignore) {

		}
		try {
			return Lang.ConfigPropertyLookupBundle("_DefaultBundle").getString(key.getPropertyName());
		} catch (MissingResourceException ignore) {

		}
		return "";
	}

	@NotNull
	public static String getDocumentationNestedClass(@NotNull String bundle, @NotNull ConfigPropertyKey key,
													 @NotNull String nestedClass) {
		try {
			return Lang.ConfigPropertyLookupBundle(bundle).getString(nestedClass + "." + key.getPropertyName());
		} catch (MissingResourceException ignore) {

		}
		return "";
	}
}
