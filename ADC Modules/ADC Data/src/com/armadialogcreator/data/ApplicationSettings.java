package com.armadialogcreator.data;

/**
 @author K
 @since 02/15/2019 */
public class ApplicationSettings extends Settings {

	public final FileSetting ArmaToolsSetting = new FileSetting();

	public ApplicationSettings() {
		map.put("ArmaTools", ArmaToolsSetting);
	}

}
