package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.ApplicationStateSubscriber;
import com.armadialogcreator.application.Project;
import com.armadialogcreator.control.ImagePool;
import com.armadialogcreator.util.ApplicationSingleton;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 5/27/19 */
@ApplicationSingleton
public class ImagePoolManager implements ApplicationStateSubscriber {
	public static ImagePoolManager instance = new ImagePoolManager();

	static {
		ApplicationManager.instance.addStateSubscriber(instance);
	}

	@Override
	public void projectClosed(@NotNull Project project) {
		ImagePool.garbageCollectImages();
	}
}
