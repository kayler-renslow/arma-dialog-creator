package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 All avaialable Arma 3 fonts
 Created on 05/22/2016. */
public class AFont extends SerializableValue {
	public static final AFont PURISTA_LIGHT = new AFont("PuristaLight");
	public static final AFont PURISTA_MEDIUM = new AFont("PuristaMedium");
	public static final AFont PURISTA_SEMI_BOLD = new AFont("PuristaSemiBold");
	public static final AFont PURISTA_BOLD = new AFont("PuristaBold");
	public static final AFont LUCIDA_CONSOLE_B = new AFont("LucidaConsoleB");
	public static final AFont ETELKA_MONOSPACE_PRO = new AFont("EtelkaMonospacePro");
	public static final AFont ETELKA_MONOSPACE_PRO_BOLD = new AFont("EtelkaMonospaceProBold");
	public static final AFont ETELKA_NARROW_MEDIUM_PRO = new AFont("EtelkaNarrowMediumPro");
	public static final AFont TAHOMA_B = new AFont("TahomaB");
	
	/** Default Arma 3 font */
	public static AFont DEFAULT = PURISTA_MEDIUM;
	
	public static final ValueConverter<AFont> CONVERTER = new ValueConverter<AFont>() {
		@Override
		public AFont convert(DataContext context, @NotNull String... values) {
			return get(values[0]);
		}
	};
	
	private final String name;
	
	private static final AFont[] values = {PURISTA_LIGHT, PURISTA_MEDIUM, PURISTA_SEMI_BOLD, PURISTA_BOLD, LUCIDA_CONSOLE_B, ETELKA_MONOSPACE_PRO, ETELKA_MONOSPACE_PRO_BOLD, ETELKA_NARROW_MEDIUM_PRO, TAHOMA_B};
	
	private AFont(String name) {
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
	
	@Override
	public SerializableValue deepCopy() {
		return this;
	}
	
	public static AFont[] values() {
		return values;
	}
	
	/** Return the AFont instance where name.equals({@link #name()}) from any of {@link #values()}. If no match found, returns null */
	public static AFont get(String name) {
		for (AFont font : values()) {
			if (font.name().equals(name)) {
				return font;
			}
		}
		return null;
	}
	
}
