package com.armadialogcreator.application;

/**
 @author K
 @since 01/03/2019 */
public class ApplicationManager {
	public void loadProject() {
		for (ApplicationStateSubscriber sub : ApplicationDataManager.getInstance().getApplicationStateSubs()) {
			//sub.projectLoaded(/*blah*/);
		}
	}
}
