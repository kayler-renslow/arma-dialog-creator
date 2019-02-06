package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/29/2016.
 */
public class XmlParseException extends Exception {

	public enum Reason {
		/** Xml couldn't be parsed */
		FailedParse,
		/** Xml failed to be re-encoded */
		FailedReEncode,
		/** Unknown Xml parsing or loading error. Check the exception message for (possibly) more info */
		Other,
		/** File couldn't be found to parse */
		FileNotFound

	}

	@NotNull
	private final Reason reason;

	public XmlParseException(@NotNull Reason reason) {
		this.reason = reason;
	}

	public XmlParseException(@NotNull Reason reason, Exception e) {
		super(e);
		this.reason = reason;
	}

	/** Defaults to {@link Reason#Other} */
	public XmlParseException(String message, Throwable cause) {
		super(message, cause);
		this.reason = Reason.Other;
	}

	/** Defaults to {@link Reason#Other} */
	public XmlParseException(String message) {
		super(message);
		this.reason = Reason.Other;
	}

	public XmlParseException(@NotNull Reason reason, String message) {
		super(message);
		this.reason = reason;
	}

	@NotNull
	public Reason getReason() {
		return reason;
	}
}
