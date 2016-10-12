/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;

import java.io.*;

/**
 Created by Kayler on 10/10/2016.
 */
public class ADCReleaseAutomation {
	public static void main(String[] args) throws Exception {
		createLaunch4jConfig();
		createBuildInfo();
	}

	private static void createBuildInfo() throws Exception {
		File buildInfoFile = new File("resources/.build");
		buildInfoFile.createNewFile();
		PrintWriter pw = new PrintWriter(buildInfoFile);
		pw.println(getPropertiesString("build.number", System.getenv("BUILD_NUMBER")));
		pw.println(getPropertiesString("build.vcs.number", System.getenv("BUILD_VCS_NUMBER_ADC_KAYLER_RENSLOW_GITHUB")));

		pw.flush();
		pw.close();
	}

	private static String getPropertiesString(String key, Object value) {
		return key + "=" + (value == null ? "null" : value.toString());
	}

	/** create the config file that will be used to make "Arma Dialog Creator.exe" via Launch4j */
	private static void createLaunch4jConfig() throws IOException {
		File templateFile = new File("launch4j/configuration_template.xml");
		if (!templateFile.exists()) {
			throw new IllegalStateException("templateFile should exist. Current path:" + templateFile.getPath());
		}
		FileInputStream fis = new FileInputStream(templateFile);
		FileOutputStream fos = new FileOutputStream(new File("launch4j/configuration.xml"));
		int in;
		boolean startVariable = false;
		String variable = "";
		while ((in = fis.read()) >= 0) {
			if (startVariable) {
				if (in == '$') {
					startVariable = false;
				} else {
					variable += (char) in;
					continue;
				}

				switch (variable) {
					case "FILE_VERSION": {
						fos.write(Lang.Application.Executable.FILE_VERSION.getBytes());
						break;
					}
					case "TXT_FILE_VERSION": {
						fos.write(Lang.Application.Executable.TXT_FILE_VERSION.getBytes());
						break;
					}
					case "PRODUCT_VERSION": {
						fos.write(Lang.Application.Executable.PRODUCT_VERSION.getBytes());
						break;
					}
					case "TXT_PRODUCT_VERSION": {
						fos.write(Lang.Application.Executable.TXT_PRODUCT_VERSION.getBytes());
						break;
					}
				}
			} else {
				if (in == '$') {
					startVariable = true;
					variable = "";
					continue;
				}
				fos.write(in);
			}
		}
		fos.flush();
		fos.close();
		fis.close();
	}
}
