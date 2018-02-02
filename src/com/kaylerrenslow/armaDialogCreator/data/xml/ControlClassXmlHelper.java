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

import java.util.*;

/**
 Helps with loading {@link ControlClass} and {@link CustomControlClass} instances in proper order

 @author Kayler
 @since 11/06/2017 */
class ControlClassXmlHelper {
	private final LinkedList<AfterLoadJob> jobs = new LinkedList<>();
	private final Set<String> existingClasses = new HashSet<>();
	private final Project project;
	private final XmlErrorRecorder recorder;
	private boolean runJobs = false;

	public ControlClassXmlHelper(@NotNull Project project, @NotNull XmlErrorRecorder recorder) {
		this.project = project;
		this.recorder = recorder;
	}

	public void addJob(@NotNull AfterLoadJob job) {
		jobs.add(job);
	}

	/**
	 Sorts the jobs to ensure correct application state and then invokes them. Note: this method can only
	 be invoked once. If invoked a second time, an {@link IllegalStateException} will be thrown.

	 @throws IllegalStateException
	 */
	public void runJobs() {
		if (runJobs) {
			throw new IllegalStateException("Already invoked runJobs()");
		}
		runJobs = true;
		for (AfterLoadJob job : sortJobs()) {
			job.doWork(project, recorder);
		}
	}

	@NotNull
	private LinkedList<AfterLoadJob> sortJobs() {
		LinkedList<AfterLoadJob> jobsSorted = new LinkedList<>();

		HashSet<String> createdClasses = new HashSet<>(existingClasses);

		while (!jobs.isEmpty()) {
			Iterator<AfterLoadJob> iter = jobs.iterator();
			boolean didWork = false;
			while (iter.hasNext()) {
				AfterLoadJob iterJob = iter.next();
				if (iterJob instanceof CreateCustomControlClassJob) {
					CreateCustomControlClassJob job = (CreateCustomControlClassJob) iterJob;
					if (job.spec.getExtendClassName() == null) {
						createdClasses.add(job.spec.getClassName());
						jobsSorted.add(iterJob);
						iter.remove();
						didWork = true;
					} else {
						if (createdClasses.contains(job.spec.getExtendClassName())) {
							createdClasses.add(job.spec.getClassName());
							jobsSorted.add(iterJob);
							iter.remove();
							didWork = true;
						}
					}
				} else if (iterJob instanceof ControlExtendJob) {
					ControlExtendJob job = (ControlExtendJob) iterJob;
					if (createdClasses.contains(job.extendThisControlClassName)) {
						createdClasses.add(job.setMyExtend.getClassName());
						jobsSorted.add(iterJob);
						iter.remove();
						didWork = true;
					}
				} else if (iterJob instanceof ControlNestedClassesJob) {
					ControlNestedClassesJob job = (ControlNestedClassesJob) iterJob;
					if (createdClasses.contains(job.addToMe.getClassName())) {
						jobsSorted.add(iterJob);
						iter.remove();
						didWork = true;
					}
				} else {
					throw new IllegalStateException("Unexpected and unhandled job type: " + iterJob.getClass());
				}
			}
			if (!didWork) {
				throw new IllegalStateException("Job sorting: Went through all jobs for an iteration and did nothing (this error prevents infinite loop)!"
						+ "jobs=:" + jobs
				);
			}
		}

		return jobsSorted;
	}

	/**
	 Used to register a class that already is instantiated
	 and thus can be extended immediately when {@link #runJobs()} is invoked.
	 <p>
	 This method ensures that {@link ControlClass} instances are instantiated in proper order.

	 @see ControlClass#extendControlClass(ControlClass)
	 */
	public void registerExistingControlClass(@NotNull ControlClass controlClass) {
		existingClasses.add(controlClass.getClassName());
	}

	protected interface AfterLoadJob {
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
		public String toString() {
			return "ControlNestedClassesJob{" +
					"addToMe=" + addToMe +
					", requiredNested=" + requiredNested +
					", optionalNested=" + optionalNested +
					'}';
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
	}
}
