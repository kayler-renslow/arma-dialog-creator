package com.kaylerrenslow.armaDialogCreator.gui.notification;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Region;
import org.jetbrains.annotations.NotNull;

/**
 A {@link Notification} is displayed by {@link Notifications}.

 @author Kayler
 @since 11/16/2016 */
public abstract class Notification {

	public static final long DEFAULT_DURATION = 10 * 1000;
	protected final String notificationTitle;
	protected final String notificationText;
	protected final BooleanProperty showProperty = new SimpleBooleanProperty(true);
	protected long displayDurationMilliseconds;
	protected boolean saveToHistory = true;

	/**
	 A non-error notification that will last {@link #DEFAULT_DURATION} seconds

	 @param notificationTitle title of notification
	 @param notificationText body text of notification
	 */
	public Notification(@NotNull String notificationTitle, @NotNull String notificationText) {
		this(notificationTitle, notificationText, DEFAULT_DURATION, false);
	}

	/**
	 A non-error notification that will be displayed for the specified amount of milliseconds.

	 @param notificationTitle title of notification
	 @param notificationText body text of notification
	 @param displayDurationMilliseconds how many milliseconds the notification should be displayed
	 */
	public Notification(@NotNull String notificationTitle, @NotNull String notificationText, long displayDurationMilliseconds) {
		this(notificationTitle, notificationText, displayDurationMilliseconds, false);
	}

	/**
	 A notification that will be displayed for the specified amount of milliseconds. If the notification is an error notification, the header of the notification will be red.

	 @param notificationTitle title of notification
	 @param notificationText body text of notification
	 @param displayDurationMilliseconds how many milliseconds the notification should be displayed
	 @param isErrorNotification true if the notification should have a red header, otherwise the header will be equal to JavaFX css attribute "-fx-accent"
	 */
	public Notification(@NotNull String notificationTitle, @NotNull String notificationText, long displayDurationMilliseconds, boolean isErrorNotification) {
		this.notificationTitle = notificationTitle;
		this.notificationText = notificationText;
		this.displayDurationMilliseconds = displayDurationMilliseconds;
	}

	/**
	 @return true if the notification is showing, false if it isn't
	 */
	public boolean isShowing() {
		return showProperty.get();
	}

	/**
	 Set whether or not the notification is showing

	 @param showing true if showing, false if not showing
	 */
	public void setShowing(boolean showing) {
		showProperty.setValue(showing);
	}

	@NotNull
	public BooleanProperty showingProperty() {
		return showProperty;
	}

	/**
	 Set how long the notification should be shown in milliseconds

	 @param displayDurationMilliseconds how many milliseconds to show notification
	 */
	public void setDisplayDuration(long displayDurationMilliseconds) {
		this.displayDurationMilliseconds = displayDurationMilliseconds;
	}

	public long getDisplayDurationMilliseconds() {
		return displayDurationMilliseconds;
	}

	@NotNull
	public String getNotificationTitle() {
		return notificationTitle;
	}

	@NotNull
	public String getNotificationText() {
		return notificationText;
	}

	/** Get the JavaFX node that represents notification */
	@NotNull
	public abstract Region getContentRoot();

	/**
	 By default, this value is true.

	 @return true if the notification should be stored in {@link Notifications#getPastNotifications()}, false if it shouldn't
	 */
	public boolean saveToHistory() {
		return saveToHistory;
	}

	/** @see #saveToHistory() */
	public void setSaveToHistory(boolean saveToHistory) {
		this.saveToHistory = saveToHistory;
	}

	@Override
	public String toString() {
		return "Notification{" +
				"root=" + getContentRoot() +
				", notificationTitle='" + notificationTitle + '\'' +
				", notificationText='" + notificationText + '\'' +
				", displayDurationMilliseconds=" + displayDurationMilliseconds +
				", showProperty=" + showProperty +
				'}';
	}
}
