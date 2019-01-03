/**
 @author K
 @since 01/02/2019 */
module ADC.Updater {
	requires json.simple;
	requires ADC.Standalone.ProgressWindow;


	exports com.armadialogcreator.updater;
	exports com.armadialogcreator.updater.github;
	exports com.armadialogcreator.updater.tasks;
}
