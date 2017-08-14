/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.launcher;

import com.kaylerrenslow.armaDialogCreator.updater.ADCUpdater;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.File;
import java.io.IOException;

/**
 Created by Kayler on 10/23/2016.
 */
public class ADCLauncher {
	private static final String ADC_JAR = "adc.jar";
	private static final File ADC_JAR_SAVE_LOCATION = new File("./" + ADC_JAR);

	public static void main(String[] args) throws Exception {
		try {
			Class c = Class.forName("javafx.application.Application");
		} catch (ClassNotFoundException e) {
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(null, "JavaFX 8 isn't installed on this computer. Please install it to run Arma Dialog Creator.", "JavaFX 8 Missing", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			});
			return;
		}

		if (ADC_JAR_SAVE_LOCATION.exists()) {
			launchAdc();
		} else {
			ADCUpdater.main(args);
			launchAdc();
		}
	}

	private static void launchAdc() throws IOException {
		Runtime.getRuntime().exec("java -jar " + ADC_JAR, null, ADC_JAR_SAVE_LOCATION.getParentFile());
	}
}
