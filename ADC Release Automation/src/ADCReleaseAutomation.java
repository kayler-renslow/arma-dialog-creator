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
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;

/**
 This class is used for preparing a build for Arma Dialog Creator and building "Arma Dialog Creator.exe".
 In order to build the .exe, <a href='http://launch4j.sourceforge.net/'>Launch4J</a> will be needed.

 @author Kayler
 @since 10/10/2016. */
public class ADCReleaseAutomation {
	private final String workingDirectoryPath = new File("").getAbsolutePath();
	private final String launch4jPath = "D:\\DATA\\Launch4j";

	/*Steps to building the Arma Dialog Creator.exe and installer:

	1. Run ADCReleaseAutomation.main() with no program arguments
	    * This step will create adc.jar's manifest, launch4j configs for exe's, and remove old exe's previously created
	    * You will need BUILD_NUMBER defined in environment variables
	2. Build adc.jar
	3. Build adc_launcher.jar
	4. Build adc_updater.jar
	5. Run ADCReleaseAutomation.main() with "-buildADCExe" as the program arguments
	    * This step will create "Arma Dialog Creator.exe"
	6. Run ADCReleaseAutomation.main() with "-packInstaller" as the program arguments
	    * This step will copy adc.jar, adc_updater.jar, etc, to the installer's build location
	7. Build adc_installer.jar
	    * This step will take previously copied adc.jar and other files and build adc_installer.jar
	8. Run ADCReleaseAutomation.main() with "-buildInstallerExe" as the program arguments
	    * This step will create "Arma Dialog Creator Installer.exe"
	*/

	public static void main(String[] args) {
		try {
			new ADCReleaseAutomation().run(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void run(String[] args) throws Exception {
		if (args.length > 0 && args[0].equals("-packInstaller")) {
			packInstaller();
		} else if (args.length > 0 && args[0].equals("-buildADCExe")) {
			createExe();
		} else if (args.length > 0 && args[0].equals("-buildInstallerExe")) {
			createInstallerExe(); //create installer last
		} else {
			createManifest();
			createLaunch4jConfig("release_automation/configuration_template.xml", "release_automation/configuration.xml");
			createLaunch4jConfig("release_automation/installer_configuration_template.xml", "release_automation/installer_configuration.xml");
			removeOldExeFiles();
		}

	}

	private void packInstaller() {
		ArrayList<File> filesToPack = new ArrayList<>(
				Arrays.asList(
						new File("out/artifacts/adc_jar/adc.jar"),
						new File("out/artifacts/adc_launcher_jar/Arma Dialog Creator.exe")
						/*,new File("out/artifacts/adc_updater_jar/adc_updater.jar")*/
				)
		);
		ZipFile zip;
		try {
			File zipFile = new File(workingDirectoryPath + "/out/artifacts/adc_installer_jar/adc_installation.zip");
			if (zipFile.exists()) {
				zipFile.delete();
			}
			zip = new ZipFile(zipFile.toPath().toString());

			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			zip.createZipFile(filesToPack, parameters);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		for (File f : filesToPack) {
			try {
				File dest = new File("/out/production/ADC Installer/install/" + f.getName());
				System.out.println("Copying " + f.getAbsoluteFile().toPath() + " to '" + dest.toPath() + "'");
				Files.copy(f.getAbsoluteFile().toPath(), dest.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	/** Ask Launch4j to build the .exe for us */
	private void createExe() {
		ProcessBuilder pb = new ProcessBuilder();
		try {
			pb.inheritIO();
			Process p = Runtime.getRuntime().exec(
					String.format("java -jar launch4j.jar \"%s\\release_automation\\configuration.xml\"", workingDirectoryPath),
					null,
					new File(launch4jPath)
			);
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Ask Launch4j to build the .exe for us */
	private void createInstallerExe() {
		ProcessBuilder pb = new ProcessBuilder();
		try {
			pb.inheritIO();
			Process p = Runtime.getRuntime().exec(
					String.format("java -jar launch4j.jar \"%s\\release_automation\\installer_configuration.xml\"", workingDirectoryPath),
					null,
					new File(launch4jPath)
			);
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Create the Manifest file to be used for the adc.jar. The Manifest holds details like the build and Arma Dialog Creator version */
	private void createManifest() {
		exportNew(new File("release_automation/MANIFEST_template.mf"), new File("src/META-INF/MANIFEST.MF"));
	}

	private void removeOldExeFiles() {
		for (File f : new File[]{
				new File("out/artifacts/adc_launcher_jar/Arma Dialog Creator.exe"),
				new File("out/artifacts/adc_installer_jar/Arma Dialog Creator Installer.exe")
		}) {
			if (f.exists()) {
				try {
					Files.delete(f.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** create the config file that will be used to make "Arma Dialog Creator.exe" via Launch4j */
	private void createLaunch4jConfig(String templateFilePath, String destTemplateFilePath) throws IOException {
		exportNew(new File(templateFilePath), new File(destTemplateFilePath));
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
			StringBuilder variable = new StringBuilder();
			while ((in = fis.read()) >= 0) {
				if (startVariable) {
					if (in == '$') {
						startVariable = false;
					} else {
						variable.append((char) in);
						continue;
					}

					String varName = variable.toString();

					switch (varName) {
						case "ADC_LAUNCHER_PROJECT_OUT_PATH": {
							fos.write((workingDirectoryPath + "\\out\\artifacts\\adc_launcher_jar").getBytes());
							break;
						}
						case "ADC_INSTALLER_PROJECT_OUT_PATH": {
							fos.write((workingDirectoryPath + "\\out\\artifacts\\adc_installer_jar").getBytes());
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
							System.err.println("WARNING: unknown variable:" + varName);
						}
					}
				} else {
					if (in == '$') {
						startVariable = true;
						variable = new StringBuilder();
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
