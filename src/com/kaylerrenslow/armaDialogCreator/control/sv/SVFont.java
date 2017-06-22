package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

/**
 All avaialable Arma 3 fonts

 @author Kayler
 @since 05/22/2016. */
public class SVFont extends SerializableValue {
	public static final SVFont PURISTA_LIGHT = new SVFont("PuristaLight");
	public static final SVFont PURISTA_MEDIUM = new SVFont("PuristaMedium");
	public static final SVFont PURISTA_SEMI_BOLD = new SVFont("PuristaSemiBold");
	public static final SVFont PURISTA_BOLD = new SVFont("PuristaBold");
	public static final SVFont LUCIDA_CONSOLE_B = new SVFont("LucidaConsoleB");
	public static final SVFont ETELKA_MONOSPACE_PRO = new SVFont("EtelkaMonospacePro");
	public static final SVFont ETELKA_MONOSPACE_PRO_BOLD = new SVFont("EtelkaMonospaceProBold");
	public static final SVFont ETELKA_NARROW_MEDIUM_PRO = new SVFont("EtelkaNarrowMediumPro");
	public static final SVFont TAHOMA_B = new SVFont("TahomaB");

	/** Default Arma 3 font */
	public static SVFont DEFAULT = PURISTA_MEDIUM;

	public static final ValueConverter<SVFont> CONVERTER = new ValueConverter<SVFont>() {
		@Override
		public SVFont convert(DataContext context, @NotNull String... values) {
			return get(values[0]);
		}
	};

	private final String name;

	private static final SVFont[] values = {PURISTA_LIGHT, PURISTA_MEDIUM, PURISTA_SEMI_BOLD, PURISTA_BOLD, LUCIDA_CONSOLE_B, ETELKA_MONOSPACE_PRO, ETELKA_MONOSPACE_PRO_BOLD, ETELKA_NARROW_MEDIUM_PRO, TAHOMA_B};

	private SVFont(String name) {
		super(name);
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
