package com.armadialogcreator.data;

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

		@NotNull
		@Override
		public File convertFromString(@NotNull String s) {
			return new File(s);
		}

		@Override
		public @NotNull String convertToString() {
			return v.getAbsolutePath();
		}
	}

	public static class StringSetting extends Setting<String> {

		public StringSetting(@Nullable String initial) {
			super(initial);
		}

		public StringSetting() {
		}

		@NotNull
		@Override
		public String convertFromString(@NotNull String s) {
			return s;
		}

		@Override
		@NotNull
		public String convertToString() {
			return v;
		}
	}

	public static class BooleanSetting extends Setting<Boolean> {

		public BooleanSetting(boolean initial) {
			super(initial);
		}

		public boolean isTrue() {
			Boolean b = super.get();
			return b == null ? false : b;
		}

		@Override
		public void set(@Nullable Boolean aBoolean) {
			super.set(aBoolean);
		}

		@NotNull
		@Override
		public Boolean convertFromString(@NotNull String s) {
			return Boolean.getBoolean(s);
		}

		@Override
		@NotNull
		public String convertToString() {
			return v.toString();
		}
	}

	public static class IntegerSetting extends Setting<Integer> {

		public IntegerSetting(@Nullable Integer initial) {
			super(initial);
		}

		public IntegerSetting() {
		}

		@NotNull
		@Override
		public Integer convertFromString(@NotNull String s) {
			return Integer.parseInt(s);
		}

		@Override
		@NotNull
		public String convertToString() {
			return v.toString();
		}
	}

	public static class ColorSetting extends IntegerSetting {
		public ColorSetting(int r, int g, int b, int a) {
			set(r / 255.0, g / 255.0, b / 255.0, a / 255.0);
		}

		public ColorSetting(int argb) {
			super(argb);
		}

		public int argb() {
			return get();
		}

		@NotNull
		@Override
		public Integer get() {
			Integer integer = super.get();
			return integer == null ? 0 : integer;
		}

		@Override
		public void set(@Nullable Integer integer) {
			super.set(integer == null ? 0 : integer);
		}

		/** Sets the integer value based upon floats ranged 0-1.0 */
		public void set(double r, double g, double b, double a) {
			final double f = 255.0;
			int R = (int) (r * f);
			int G = (int) (g * f);
			int B = (int) (b * f);
			int A = (int) (a * f);
			int color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
			set(color);
		}

		public double r() {
			int r = (argb() >> 16) & 0xFF;
			final double f = 255.0;

			return r / f;
		}

		public double g() {
			int g = (argb() >> 8) & 0xFF;
			final double f = 255.0;
			return g / f;
		}

		public double b() {
			int b = (argb()) & 0xFF;
			final double f = 255.0;
			return b / f;
		}

		public double a() {
			int a = (argb() >> 24) & 0xFF;
			final double f = 255.0;
			return a / f;
		}
	}

	public abstract static class Setting<V> {
		protected V v;

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

		@NotNull
		public abstract V convertFromString(@NotNull String s);

		@NotNull
		public abstract String convertToString();
	}
}
