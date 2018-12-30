package com.armadialogcreator.expression;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 07/01/2017 */
public class Scope {
	private final Env myEnv;

	protected Scope(@NotNull Env env) {
		this.myEnv = env;
	}

	protected void clearLocals() {
		String[] idents = myEnv.getMappedIdentifiers();
		for (String ident : idents) {
			if (ident.charAt(0) == '_') {
				myEnv.remove(ident);
			}
		}
	}
}
