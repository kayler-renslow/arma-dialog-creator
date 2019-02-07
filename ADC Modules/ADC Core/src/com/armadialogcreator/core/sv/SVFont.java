package com.armadialogcreator.core.sv;

import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

/**
 All available Arma 3 fonts

 @author Kayler
 @since 05/22/2016. */
public class SVFont extends SerializableValue {
	public static final SVFont PuristaLight = new SVFont("PuristaLight");
	public static final SVFont PuristaMedium = new SVFont("PuristaMedium");
	public static final SVFont PuristaSemiBold = new SVFont("PuristaSemiBold");
	public static final SVFont PuristaBold = new SVFont("PuristaBold");
	public static final SVFont LucidaConsoleB = new SVFont("LucidaConsoleB");
	public static final SVFont EtelkaMonospacePro = new SVFont("EtelkaMonospacePro");
	public static final SVFont EtelkaMonospaceProBold = new SVFont("EtelkaMonospaceProBold");
	public static final SVFont EtelkaNarrowMediumPro = new SVFont("EtelkaNarrowMediumPro");
	public static final SVFont TahomaB = new SVFont("TahomaB");

	/** Default Arma 3 font */
	public static SVFont DEFAULT = PuristaMedium;

	public static final ValueConverter<SVFont> CONVERTER = new ValueConverter<SVFont>() {
		@Override
		public SVFont convert(DataContext context, @NotNull String... values) {
			return get(values[0]);
		}
	};

	private final String name;

	private static final SVFont[] values = {PuristaLight, PuristaMedium, PuristaSemiBold, PuristaBold,
			LucidaConsoleB, EtelkaMonospacePro, EtelkaMonospaceProBold, EtelkaNarrowMediumPro, TahomaB
	};

	private SVFont(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name();
	}

	public String name() {
		return this.name;
	}

	@NotNull
	@Override
	public String[] getAsStringArray() {
		return new String[]{this.name};
	}

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		return this;
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.Font;
	}

	@NotNull
	public static SVFont[] values() {
		return values;
	}

	/**
	 @return the instance where name.equals({@link #name()}) from any of {@link #values()}
	 @throws IllegalArgumentException if no match found
	 */
	@NotNull
	public static SVFont get(@NotNull String name) {
		for (SVFont font : values()) {
			if (font.name().equals(name)) {
				return font;
			}
		}
		throw new IllegalArgumentException("name is not a font. name=" + name);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof SVFont) {
			SVFont other = (SVFont) o;
			return this.name.equals(other.name);
		}
		return false;
	}
}
