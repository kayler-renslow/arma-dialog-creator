/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

import com.kaylerrenslow.armaDialogCreator.main.Lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 Created by Kayler on 10/10/2016.
 */
public class ADCReleaseAutomation {
	private final String workingDirectoryPath = new File("").getAbsolutePath();

	public static void main(String[] args) {
		try {
			new ADCReleaseAutomation().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void run() throws Exception {
		createManifest();
		createLaunch4jConfig();
		removeOldExe();
	}

	private void createManifest() {
		exportNew(new File("release_automation/MANIFEST_template.mf"), new File("src/META-INF/MANIFEST.MF"));
	}

	private void removeOldExe() {
		File oldExe = new File("out/artifacts/Arma_Dialog_Creator/Arma Dialog Creator.exe");
		if (oldExe.exists()) {
			try {
				Files.delete(oldExe.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** create the config file that will be used to make "Arma Dialog Creator.exe" via Launch4j */
	private void createLaunch4jConfig() throws IOException {
		exportNew(new File("release_automation/configuration_template.xml"), new File("release_automation/configuration.xml"));
	}

	private void exportNew(File template, File out) {
		if (!template.exists()) {
			throw new IllegalStateException("template should exist. Current path:" + template.getPath());
		}
		try {
			FileInputStream fis = new FileInputStream(template);
			FileOutputStream fos = new FileOutputStream(out);
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
						case "PROJECT_OUT_PATH": {
							fos.write((workingDirectoryPath + "\\out\\artifacts\\Arma_Dialog_Creator").getBytes());
							break;
						}
						case "VERSION": {
							fos.write(Lang.Application.VERSION.getBytes());
							break;
						}
						case "BUILD_NUMBER": {
							String buildNumber = System.getenv("BUILD_NUMBER");
							if (buildNumber == null) {
								fos.write("unversioned".getBytes());
							} else {
								fos.write(buildNumber.getBytes());
							}
							break;
						}
						default: {
							throw new IllegalStateException("unknown variable:" + variable);
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
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
