package com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility;

/**
 Handles alternating between two things

 @author Kayler
 @since 11/22/2016 */
public class AlternatorHelper<T> {
	private long alternateMillis;
	private long lastUpdate;
	private boolean in = false;
	private long durationPast = 0;

	public AlternatorHelper(long alternateMillis) {
		this.alternateMillis = alternateMillis;
	}

	public void setAlternateMillis(long alternateMillis) {
		this.alternateMillis = alternateMillis;
	}

	public double updateAndGetRatio() {
		long now = System.currentTimeMillis();
		long timePast = now - lastUpdate;
		lastUpdate = now;

		if (in) {
			durationPast += timePast;
			if (durationPast >= alternateMillis) {
				in = false;
				durationPast = alternateMillis;
			}
		} else {
			durationPast -= timePast;
			if (durationPast <= 0) {
				in = true;
				durationPast = 0;
			}
		}
		return durationPast * 1.0 / alternateMillis;
	}
}
