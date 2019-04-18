package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.core.stringtable.StringTable;
import com.armadialogcreator.util.ApplicationSingleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 @author K
 @since 01/07/2019 */
@ApplicationSingleton
public class StringTableManager implements ApplicationStateSubscriber {
	public static final StringTableManager instance = new StringTableManager();

	static {
		ApplicationManager.instance.addStateSubscriber(instance);
	}

	private StringTable stringTable;

	public void setStringTable(@Nullable StringTable stringTable) {
		this.stringTable = stringTable;
		MacroRegistry.instance.getWorkspaceMacros().bindStringTable(stringTable);
	}

	@Nullable
	public StringTable getStringTable() {
		return stringTable;
	}

	@Override
	public void projectDataLoaded(@NotNull Project project) {
		setStringTable(stringTable);
	}

	@Override
	public void workspaceInitializing(@NotNull Workspace workspace) {
		workspace.getDataList().add(new StringTableWorkspaceData());
	}

	private static class StringTableWorkspaceData implements WorkspaceData {

		@Override
		@NotNull
		public String getDataID() {
			return "string-table";
		}

		@Override
		public void loadFromConfigurable(@NotNull Configurable config) {
			String f = config.getAttributeValueNotNull("f");
			StringTableManager tableManager = StringTableManager.instance;
			if (f.length() == 0) {
				tableManager.setStringTable(null);
			} else {
				ADCFile adcFile = ADCFile.toADCFile(ADCFile.FileType.Workspace, new File(f));
				try {
					tableManager.setStringTable(new DefaultStringTableXmlParser(adcFile.toFile()).createStringTableInstance());
				} catch (Exception e) {
					tableManager.setStringTable(null);
				}
			}

		}

		@Override
		public void exportToConfigurable(@NotNull Configurable configurable) {
			StringTable stringTable = StringTableManager.instance.stringTable;
			if (stringTable != null) {
				ADCFile file = ADCFile.toADCFile(ADCFile.FileType.Workspace, stringTable.getFile());
				configurable.addAttribute("f", file.getRelPath());
			}
		}
	}
}
