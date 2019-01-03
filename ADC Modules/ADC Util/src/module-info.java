/**
 @author K
 @since 01/02/2019 */
module ADC.Util {
	requires javafx.base; //do not put ADC.JavaFX because it will be circular dependency
	requires annotations;
	requires java.xml;

	exports com.armadialogcreator.util;
}
