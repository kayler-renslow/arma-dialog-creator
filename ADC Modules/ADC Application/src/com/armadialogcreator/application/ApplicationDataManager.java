package com.armadialogcreator.application;

import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

/**
 @author K
 @since 01/03/2019 */
public class ApplicationDataManager implements ADCDataListManager<ApplicationData> {
	private static final ApplicationDataManager instance = new ApplicationDataManager();

	private final ListObserver<ApplicationData> dataList = new ListObserver<>(new ArrayList<>());


	@Override
	@NotNull
	public ListObserver<ApplicationData> getDataList() {
		return dataList;
	}

	@NotNull
	public static ApplicationDataManager getInstance() {
		return instance;
	}

	@NotNull
	public static File getFileInApplicationDataDirectory(@NotNull String file) {
		return ApplicationManager.getFileInApplicationDirectory("applicationData" + File.separator + file);
	}

	
}
