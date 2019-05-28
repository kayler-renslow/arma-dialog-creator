/**
 @author K
 @since 01/04/2019 */
module ADC.Data {
	requires ADC.Application;
	requires ADC.Core;
	requires ADC.Expression;
	requires ADC.Util;
	requires ADC.Control;
	requires ADC.Lang;
	requires ADC.Canvas;

	requires annotations;
	requires java.desktop;
	exports com.armadialogcreator.data;

	opens com.armadialogcreator.data.defaultValues;
	opens com.armadialogcreator.data.defaultValues.Combo;
	opens com.armadialogcreator.data.defaultValues.ListBox;
	opens com.armadialogcreator.data.defaultValues.ShortcutButton;
}