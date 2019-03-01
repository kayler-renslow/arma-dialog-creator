/**
 @author K
 @since 02/06/2019 */
module ADC.Control {
	requires annotations;
	requires javafx.base;
	requires java.xml;

	requires ADC.Core;
	requires ADC.Canvas;
	requires ADC.Util;

	exports com.armadialogcreator.control;
	exports com.armadialogcreator.control.impl; //todo delete this
}