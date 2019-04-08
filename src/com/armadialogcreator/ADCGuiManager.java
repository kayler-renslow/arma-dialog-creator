package com.armadialogcreator;

import com.armadialogcreator.application.ADCDataWriteException;
import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.ApplicationStateSubscriber;
import com.armadialogcreator.application.Project;
import com.armadialogcreator.canvas.UICanvasConfiguration;
import com.armadialogcreator.canvas.UICanvasEditorColors;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.control.FolderUINode;
import com.armadialogcreator.data.*;
import com.armadialogcreator.gui.CanvasContextMenu;
import com.armadialogcreator.gui.main.ADCMainWindow;
import com.armadialogcreator.gui.main.CanvasView;
import com.armadialogcreator.gui.main.treeview.ControlTreeItemEntry;
import com.armadialogcreator.gui.main.treeview.FolderTreeItemEntry;
import com.armadialogcreator.gui.styles.ADCStyleSheets;
import com.armadialogcreator.util.AColorConstant;
import com.armadialogcreator.util.ApplicationSingleton;
import com.armadialogcreator.util.NotNullValueObserver;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 3/6/19 */
@ApplicationSingleton
public class ADCGuiManager implements ApplicationStateSubscriber {
	public static final ADCGuiManager instance = new ADCGuiManager();

	static {
		ApplicationManager.instance.addStateSubscriber(instance);
	}

	public final NotNullValueObserver<Boolean> darkThemeObserver = new NotNullValueObserver<>(false);

	@Override
	public void projectReady(@NotNull Project project) {
		ADCMainWindow mainWindow = ArmaDialogCreator.getMainWindow();
		mainWindow.runWhenReady(() -> {
			CanvasView canvasView = mainWindow.getCanvasView();
			ArmaDisplay editingDisplay = EditorManager.instance.getEditingDisplay();

			canvasView.getBackgroundControlTreeView().setToUINode(editingDisplay.getBackgroundControlNodes());
			canvasView.getMainControlTreeView().setToUINode(editingDisplay.getControlNodes());

			canvasView.setRootEditingUINode(editingDisplay);

			UICanvasEditorColors colors = canvasView.getColors();
			ProjectSettings projectSettings = SettingsManager.instance.getProjectSettings();
			colors.editorBg = projectSettings.EditorBackgroundSetting.get().toJavaFXColor();
			colors.grid = projectSettings.EditorGridColorSetting.get().toJavaFXColor();

			canvasView.updateCanvas();

			canvasView.setCanvasContextMenu(new CanvasContextMenu());
		});
	}

	void guiReady() {
		applyDarkThemeIfOn();
	}

	public void applyDarkThemeIfOn() {
		Settings.BooleanSetting useDarkTheme = ApplicationProperties.instance.useDarkTheme;
		try {
			ApplicationManager.instance.saveApplicationData();
		} catch (ADCDataWriteException e) {
			//todo report this to error log
			throw new RuntimeException(e);
		}

		final String darkTheme = ADCStyleSheets.getStylesheet("dark.css");
		Stage stage = ArmaDialogCreator.getPrimaryStage();
		ProjectSettings projectSettings = SettingsManager.instance.getProjectSettings();
		if (useDarkTheme.isTrue()) {
			darkThemeObserver.updateValue(true);
			projectSettings.EditorBackgroundSetting.set(
					new AColorConstant(UICanvasEditorColors.DARK_THEME_EDITOR_BG)
			);
			projectSettings.EditorGridColorSetting.set(
					new AColorConstant(UICanvasEditorColors.DARK_THEME_GRID)
			);
			stage.getScene().getStylesheets().add(darkTheme);
		} else {
			darkThemeObserver.updateValue(false);
			stage.getScene().getStylesheets().remove(darkTheme);
			projectSettings.EditorBackgroundSetting.set(
					new AColorConstant(UICanvasEditorColors.DEFAULT_EDITOR_BG)
			);
			projectSettings.EditorGridColorSetting.set(
					new AColorConstant(UICanvasEditorColors.DEFAULT_GRID)
			);
		}

		ADCMainWindow mainWindow = ArmaDialogCreator.getMainWindow();
		if (mainWindow.isShowing()) {
			UICanvasEditorColors colors = mainWindow.getCanvasView().getColors();
			colors.editorBg = projectSettings.EditorBackgroundSetting.get().toJavaFXColor();
			colors.grid = projectSettings.EditorGridColorSetting.get().toJavaFXColor();
			mainWindow.getCanvasView().updateCanvas();
		}
	}

	public boolean isBackgroundControlTreeView(@NotNull TreeView treeView) {
		ADCMainWindow mainWindow = ArmaDialogCreator.getMainWindow();
		CanvasView canvasView = mainWindow.getCanvasView();
		return canvasView.getBackgroundControlTreeView() == treeView;
	}

	public void addControlToTreeView(@NotNull ArmaControl control, boolean backgroundControl) {
		ADCMainWindow mainWindow = ArmaDialogCreator.getMainWindow();
		CanvasView canvasView = mainWindow.getCanvasView();
		if (backgroundControl) {
			canvasView.getBackgroundControlTreeView().addChildDataToRoot(new ControlTreeItemEntry(control));
		} else {
			canvasView.getMainControlTreeView().addChildDataToRoot(new ControlTreeItemEntry(control));
		}
	}

	public void addFolderToTreeView(@NotNull FolderUINode folder, boolean background) {
		ADCMainWindow mainWindow = ArmaDialogCreator.getMainWindow();
		CanvasView canvasView = mainWindow.getCanvasView();
		if (background) {
			canvasView.getBackgroundControlTreeView().addChildDataToRoot(new FolderTreeItemEntry(folder));
		} else {
			canvasView.getMainControlTreeView().addChildDataToRoot(new FolderTreeItemEntry(folder));
		}
	}

	@NotNull
	public UICanvasConfiguration getCanvasConfiguration() {
		ADCMainWindow mainWindow = ArmaDialogCreator.getMainWindow();
		CanvasView canvasView = mainWindow.getCanvasView();
		return canvasView.getConfiguration();
	}
}
