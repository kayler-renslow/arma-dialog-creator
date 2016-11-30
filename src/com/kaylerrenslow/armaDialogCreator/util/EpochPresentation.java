package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 @author Kayler
 @since 11/19/2016 */
public class EpochPresentation {
	public enum Format {
		/** hour:minute AM/PM */
		Hour_Minute_AM_PM("hh:mm aaa");

		private final String formatString;

		Format(String formatString) {
			this.formatString = formatString;
		}
	}

	public static String format(@NotNull EpochPresentation.Format format, long time) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return new SimpleDateFormat(format.formatString).format(new Date(time));
	}
}
