package com.armadialogcreator.data;

import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ConfigClassSpecification;
import com.armadialogcreator.core.ConfigProperty;
import com.armadialogcreator.core.ConfigPropertyKey;
import com.armadialogcreator.lang.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.MissingResourceException;

/**
 @author K
 @since 4/2/19 */
public class ConfigPropertyDocumentationProvider {
	/**
	 Gets {@link ConfigProperty} documentation for a specific {@link ConfigClass}.

	 @param cc the class which provides context to the property
	 @param key the key for which to get documentation for
	 @return the documentation, or empty string if nothing was present
	 */
	@NotNull
	public static String getDocumentation(@NotNull ConfigClass cc, @NotNull ConfigPropertyKey key) {
		String path;
		if (cc.getOwnerClass() != null) {
			StringBuilder sb = new StringBuilder();
			ConfigClassSpecification cursor = cc;
			do {
				cursor = cursor.getOwnerClass();
				if (cursor != null) {
					sb.append(cursor.getClassName());
					sb.append('.');
				} else {
					break;
				}
			} while (true);

			sb.append(key.getPropertyName());
			path = sb.toString();
		} else {
			path = key.getPropertyName();
		}

		try {
			String bundle = cc.getConfigPropertyDocumentationBundle();
			if (bundle != null) {
				return Lang.ConfigPropertyLookupBundle(bundle).getString(path);
			}
		} catch (MissingResourceException ignore) {

		}

		return getFromDefault(key);
	}

	@NotNull
	private static String getFromDefault(@NotNull ConfigPropertyKey key) {
		try {
			return Lang.ConfigPropertyLookupBundle("_DefaultBundle").getString(key.getPropertyName());
		} catch (MissingResourceException ignore) {

		}
		try {
			return Lang.ConfigPropertyLookupBundle("_DefaultBundle").getString("no_documentation");
		} catch (MissingResourceException ignore) {

		}
		return "No Documentation.";
	}
}
