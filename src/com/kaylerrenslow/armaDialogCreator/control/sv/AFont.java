/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

/**
 All avaialable Arma 3 fonts
 @author Kayler
 @since 05/22/2016. */
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
