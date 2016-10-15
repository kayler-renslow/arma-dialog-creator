/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.main;

import java.util.ResourceBundle;

import static com.kaylerrenslow.armaDialogCreator.main.Lang.Application.APPLICATION_NAME;

public interface Lang {

	ResourceBundle ApplicationBundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.ApplicationBundle");
	ResourceBundle EditChangeBundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.EditChangeBundle");
	ResourceBundle FxControlBundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.FxControlBundle");
	ResourceBundle LookupBundle = ResourceBundle.getBundle("com.kaylerrenslow.armaDialogCreator.LookupBundle");

	interface Application {
		String APPLICATION_NAME = "Arma Dialog Creator";
		String VERSION = "1.0.0";
		String APPLICATION_TITLE = APPLICATION_NAME + " " + VERSION;

		interface Executable {
			String FILE_VERSION = "1.0.0.0"; //x.x.x.x
			String TXT_FILE_VERSION = "1.0"; //x.x
			String PRODUCT_VERSION = "1.0.0.0";//x.x.x.x
			String TXT_PRODUCT_VERSION = "1.0.0";//x.x.x
		}
	}

	interface Misc {
		String REPO_URL = "https://github.com/kayler-renslow/arma-dialog-creator";
	}

	static String getPopupWindowTitle(String popupName) {
		return APPLICATION_NAME + " - " + popupName;
	}

}
