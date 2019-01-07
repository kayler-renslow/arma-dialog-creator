package com.armadialogcreator.application;

/**
 @author K
 @since 01/03/2019 */
public class ApplicationManager {
	private boolean initialized = false;

	public void initialize() {
		if (initialized) {
			return;
		}
		for (ApplicationStateSubscriber sub : ApplicationDataManager.getInstance().getApplicationStateSubs()) {
			sub.applicationInitializing();
		}
		initialized = true;
		for (ApplicationStateSubscriber sub : ApplicationDataManager.getInstance().getApplicationStateSubs()) {
			sub.applicationDataLoaded();
		}
	}

	public void loadProject() {
		for (ApplicationStateSubscriber sub : ApplicationDataManager.getInstance().getApplicationStateSubs()) {

		}
	}
}
