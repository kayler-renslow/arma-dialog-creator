/**
 @author K
 @since 01/03/2019 */
module ADC.Images {
	requires annotations;
	requires javafx.graphics;

	//allow JavaFX Image class to access the icon/image png files
	opens com.armadialogcreator.img;
	opens com.armadialogcreator.img.icons;
	opens com.armadialogcreator.img.icons.controls;

	exports com.armadialogcreator.img;
	exports com.armadialogcreator.img.icons;
}