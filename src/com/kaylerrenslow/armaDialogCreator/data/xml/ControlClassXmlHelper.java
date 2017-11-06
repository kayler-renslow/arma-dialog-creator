package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlClassSpecification;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.CustomControlClass;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 Helps with loading {@link ControlClass} and {@link CustomControlClass} instances in proper order

 @author Kayler
 @since 11/06/2017 */
class ControlClassXmlHelper {
	private final LinkedList<AfterLoadJob> jobs = new LinkedList<>();
	private final Project project;
	private final XmlErrorRecorder recorder;

	public ControlClassXmlHelper(@NotNull Project project, @NotNull XmlErrorRecorder recorder) {
		this.project = project;
		this.recorder = recorder;
	}

	public void addJob(@NotNull AfterLoadJob job) {
		jobs.add(job);
	}

	public void runJobs() {
		Collections.sort(jobs);
		for (AfterLoadJob job : jobs) {
			job.doWork(project, recorder);
		}
	}

	protected interface AfterLoadJob extends Comparable<AfterLoadJob> {
		void doWork(@NotNull Project project, @NotNull XmlErrorRecorder recorder);
	}

	public static class CreateCustomControlClassJob implements AfterLoadJob {

		private ControlClassSpecification spec;
		private final boolean loadInProjectRegistry;

		public CreateCustomControlClassJob(@NotNull ControlClassSpecification spec, boolean loadInProjectRegistry) {
			this.spec = spec;
			this.loadInProjectRegistry = loadInProjectRegistry;
		}

		@Override
		public void doWork(@NotNull Project project, @NotNull XmlErrorRecorder recorder) {
			CustomControlClass customControlClass = new CustomControlClass(
					spec, project,
					loadInProjectRegistry ? CustomControlClass.Scope.Project : CustomControlClass.Scope.Workspace
			);
			project.addCustomControlClass(customControlClass);
		}

		@Override
		public int compareTo(@NotNull AfterLoadJob o) {
			if (o instanceof CreateCustomControlClassJob) {
				CreateCustomControlClassJob other = (CreateCustomControlClassJob) o;
				if (other.spec.getExtendClassName() == null) {
					//let other load first
					return 1;
				}
				if (spec.getExtendClassName() == null) {
					//run this first
					return -1;
				}
				if (spec.getExtendClassName().equals(other.spec.getClassName())) {
					//allow the other to load first
					return 1;
				}
				if (other.spec.getExtendClassName().equals(spec.getClassName())) {
					//let this job load first
					return -1;
				}

			}
			return -1;
		}
	}

	public static class ControlNestedClassesJob implements AfterLoadJob {

		private final ControlClass addToMe;
		private final List<ControlClassSpecification> requiredNested;
		private final List<ControlClassSpecification> optionalNested;

		public ControlNestedClassesJob(@NotNull ControlClass addToMe, @Nullable List<ControlClassSpecification> requiredNested, @Nullable List<ControlClassSpecification> optionalNested) {
			this.addToMe = addToMe;
			this.requiredNested = requiredNested;
			this.optionalNested = optionalNested;
		}

		@Override
		public void doWork(@NotNull Project project, @NotNull XmlErrorRecorder recorder) {
			if (requiredNested != null) {
				for (ControlClassSpecification nested : requiredNested) {
					loadClass(project, nested);
				}
			}
			if (optionalNested != null) {
				for (ControlClassSpecification nested : optionalNested) {
					loadClass(project, nested);
				}
			}
		}

		private void loadClass(@NotNull Project project, ControlClassSpecification nested) {
			try {
				ControlClass nestedClass = addToMe.findNestedClass(nested.getClassName());
				nestedClass.setTo(nested.constructNewControlClass(project));
			} catch (IllegalArgumentException ignore) {

			}
		}

		@Override
		public int compareTo(@NotNull AfterLoadJob o) {
			if (o instanceof ControlNestedClassesJob) {
				return 0;
			}
			return 1;
		}
	}

	public static class ControlExtendJob implements AfterLoadJob {
		private final String extendThisControlClassName;
		private final ArmaControl setMyExtend;
		private final List<ControlPropertyLookup> inheritProperties;

		public ControlExtendJob(@NotNull String extendThisControlClassName, @NotNull ArmaControl setMyExtend, @NotNull List<ControlPropertyLookup> inheritProperties) {
			this.extendThisControlClassName = extendThisControlClassName;
			this.setMyExtend = setMyExtend;
			this.inheritProperties = inheritProperties;
		}


		@Override
		public void doWork(@NotNull Project project, @NotNull XmlErrorRecorder recorder) {
			ControlClass cc = project.findControlClassByName(extendThisControlClassName);

			if (cc == null) {
				final ResourceBundle bundle = Lang.getBundle("ProjectXmlParseBundle");
				recorder.addError(new ParseError(
						String.format(bundle.getString("ProjectLoad.couldnt_match_extend_class_f"), extendThisControlClassName, setMyExtend.getClassName()),
						bundle.getString("ProjectLoad.no_extend_class_recover")
				));
				return;
			}

			setMyExtend.extendControlClass(cc);
			for (ControlPropertyLookup inheritProperty : inheritProperties) {
				setMyExtend.inheritProperty(inheritProperty);
			}
		}

		@Override
		public int compareTo(@NotNull AfterLoadJob o) {
			if (o instanceof ControlExtendJob) {
				return 0;
			}
			//always let other jobs go before this one
			return 1;
		}
	}
}
