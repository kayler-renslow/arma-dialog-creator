package com.armadialogcreator.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 @author K
 @since 02/15/2019 */
public class ApplicationSettings {

	private final Map<String, Setting> settings = new HashMap<>();

	public final FileSetting ArmaToolsSetting = new FileSetting();

	public ApplicationSettings() {
		settings.put("ArmaTools", ArmaToolsSetting);
	}

	@NotNull
	public Map<String, Setting> getSettings() {
		return settings;
	}

	public static class FileSetting extends Setting<File> {

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

	public abstract static class Setting<V> {
		protected V v;

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
