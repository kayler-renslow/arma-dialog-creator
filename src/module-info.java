/**
 @author K
 @since 01/02/2019 */
module Arma.Dialog.Creator {
	requires ADC.JavaFX;
	requires ADC.Canvas;
	requires ADC.Util;
	requires annotations;
	requires ADC.Updater;
	requires ADC.Lang;
	requires antlr4.runtime;
	requires richtextfx;

	exports com.armadialogcreator;

	//allow JavaFX Image class to access the icon/image png files
	opens com.armadialogcreator.gui.img;
	opens com.armadialogcreator.gui.img.icons;
	opens com.armadialogcreator.gui.img.icons.controls;

	//allow JavaFX to access css stylesheets
	opens com.armadialogcreator.gui.styles;

}
