package com.armadialogcreator.data.xml;

import com.armadialogcreator.control.CustomControlClass;
import com.armadialogcreator.data.ApplicationData;
import com.armadialogcreator.data.Project;
import com.armadialogcreator.data.xml.ControlClassXmlHelper.CreateCustomControlClassJob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 An {@link XmlLoader} for loading {@link Project#getWorkspaceCustomControlClassRegistry()}

 @author Kayler
 @since 11/06/2017. */
public class WorkspaceCustomControlClassXmlLoader extends XmlLoader {

	private final ControlClassXmlHelper controlClassXmlHelper;
	private final ApplicationData applicationData;
	private final boolean runControlClassXmlHelperJobs;
	private final Project project;

	/**
	 Used to load {@link Project#getWorkspaceCustomControlClassRegistry()}.

	 @param applicationData the current {@link ApplicationData} instance
	 @param helper the {@link ControlClassXmlHelper} to use, or null if this class will create it's own (this shouldn't
	 be null when loading an existing project)
	 @param project the {@link Project} to load the {@link Project#getWorkspaceCustomControlClassRegistry()}
	 @throws XmlParseException when the XML couldn't be parsed
	 */
	public WorkspaceCustomControlClassXmlLoader(@NotNull ApplicationData applicationData,
												@Nullable ControlClassXmlHelper helper,
												@NotNull Project project) throws XmlParseException {
		super(project.getWorkspaceCustomControlClassesFile(), applicationData);
		this.project = project;
		this.controlClassXmlHelper = helper == null ?
				new ControlClassXmlHelper(applicationData.getCurrentProject(), this) :
				helper
		;
		this.applicationData = applicationData;
		runControlClassXmlHelperJobs = helper == null;
	}

	/** Reads the parsed XML and loads the {@link CustomControlClass} instances */
	public void readDocument() throws XmlParseException {
		try {
			ProjectXmlUtil.loadCustomControlClasses(this.document.getDocumentElement(), dataContext, this,
					controlClassSpecification -> {
						controlClassXmlHelper.addJob(new CreateCustomControlClassJob(controlClassSpecification, false));
						return null;
					}
			);

			if (runControlClassXmlHelperJobs) {
				controlClassXmlHelper.runJobs();
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new XmlParseException(e.getMessage(), e);
		}
	}
}
