package com.armadialogcreator.data;

import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.core.sv.*;
import com.armadialogcreator.util.ColorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 @author K
 @since 02/15/2019 */
public class Settings {

	protected final Map<String, Setting> settings = new HashMap<>();

	@NotNull
	public Map<String, Setting> getSettings() {
		return settings;
	}

	public static class FileSetting extends Setting<File> {

		public FileSetting(@Nullable File initial) {
			super(initial);
		}

		public FileSetting() {
		}
	}

	public static class StringSetting extends Setting<SVString> {

		public StringSetting(@Nullable String initial) {
			super(new SVString(initial));
		}

		public StringSetting() {
		}
	}

	public static class BooleanSetting extends Setting<SVBoolean> {

		public BooleanSetting(boolean initial) {
			super(SVBoolean.get(initial));
		}

		public BooleanSetting() {
		}

		public boolean isTrue() {
			SVBoolean b = super.get();
			return b != null && b.isTrue();
		}
	}

	public static class IntegerSetting extends Setting<SVInteger> {

		public IntegerSetting(@Nullable SVInteger initial) {
			super(initial);
		}

		public IntegerSetting() {
		}

	}

	public static class ColorSetting extends Setting<SVColor> {
		public ColorSetting(int r, int g, int b, int a) {
			set(r, g, b, a);
		}

		public ColorSetting(int argb) {
			this(ColorUtil.ri(argb), ColorUtil.gi(argb), ColorUtil.bi(argb), ColorUtil.ai(argb));
		}

		public ColorSetting() {
		}

		public int argb() {
			SVColor color = get();
			if (color == null) {
				return 0;
			}
			return color.toARGB();
		}

		@Override
		public void set(@Nullable SVColor color) {
			super.set(color == null ? new SVColorInt(0) : color);
		}

		/** Sets the integer value based upon ints ranged 0-255 */
		public void set(int r, int g, int b, int a) {
			set(new SVColorIntArray(r, g, b, a));
		}

		/** Sets the integer value based upon floats ranged 0-1.0 */
		public void set(double r, double g, double b, double a) {
			set(new SVColorArray(r, g, b, a));
		}

		public double r() {
			return ColorUtil.rf(argb());
		}

		public double g() {
			return ColorUtil.gf(argb());
		}

		public double b() {
			return ColorUtil.bf(argb());
		}

		public double a() {
			return ColorUtil.af(argb());
		}
	}

	public static abstract class Setting<V> {
		protected V v;
		protected V defaultValue;

		public Setting(@Nullable V initial) {
			this.v = initial;
		}

		public Setting() {
		}

		@Nullable
		public V get() {
			return v;
		}

		public void set(@Nullable V v) {
			this.v = v;
		}

		@Nullable
		public V getDefaultValue() {
			return defaultValue;
		}

		public void setDefaultValue(@Nullable V defaultValue) {
			this.defaultValue = defaultValue;
		}

		@NotNull
		public final V loadFromConfigurable(@NotNull Configurable c) {
			return null;
		}

		@NotNull
		public final Configurable copyToConfigurable() {
			return null;
		}
	}
}
