package com.armadialogcreator.data.oldprojectloader;

/**
 @author K
 @since 02/07/2019 */
public class ParseError {
	private final String msg;
	private final String recoverMsg;

	public ParseError(String msg, String recoverMsg) {
		this.msg = msg;
		this.recoverMsg = recoverMsg;
	}

	public String getMsg() {
		return msg;
	}

	public String getRecoverMsg() {
		return recoverMsg;
	}
}
