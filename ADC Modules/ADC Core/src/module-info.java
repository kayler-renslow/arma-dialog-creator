/**
 @author K
 @since 01/03/2019 */
module ADC.Core {
	requires ADC.Lang;
	requires transitive ADC.Util;
	requires transitive ADC.Expression;
	requires ADC.Images;

	requires transitive javafx.base;
	requires transitive javafx.graphics;
	requires annotations;

	exports com.armadialogcreator.core;
	exports com.armadialogcreator.core.sv;
	exports com.armadialogcreator.core.stringtable;
}