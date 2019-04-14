package com.armadialogcreator.arma.header;

import com.armadialogcreator.util.IndentedStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 03/19/2017 */
public interface HeaderAssignment extends HeaderItem {
	@NotNull String getVariableName();

	@NotNull HeaderValue getValue();

	default boolean equalsAssignment(@NotNull HeaderAssignment o) {
		return o == this || getVariableName().equals(o.getVariableName()) && getValue().equals(o.getValue());
	}

	@Override
	@NotNull
	default String getAsString(@Nullable IndentedStringBuilder indentedBuilder) {
		String s = String.format("%s = %s;\n", getVariableName(), getValue().getContent());
		if (indentedBuilder == null) {
			return s;
		}
		indentedBuilder.append(s);
		return indentedBuilder.toString();
	}
}
