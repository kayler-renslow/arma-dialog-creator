/**
 @author K
 @since 01/02/2019 */
module Arma.Dialog.Creator {
	requires ADC.JavaFX;
	requires ADC.Canvas;
	requires ADC.Util;
	requires ADC.Core;
	requires ADC.Application;
	requires ADC.Updater;
	requires ADC.Lang;
	requires ADC.Images;
	requires ADC.Expression;
	requires ADC.Data;

	requires annotations;
	requires antlr4.runtime;
	requires richtextfx;

	exports com.armadialogcreator;

	//allow JavaFX to access css stylesheets
	opens com.armadialogcreator.gui.styles;

}
