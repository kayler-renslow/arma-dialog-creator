package com.kaylerrenslow.armaDialogCreator.data.changeRegistrars;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.data.*;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.ControlListChange;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateGroupListener;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 Tracks changes made to {@link Project#getEditingDisplay()} and its {@link ArmaDisplay#getControls()} and {@link ArmaDisplay#getBackgroundControls()} lists. For each {@link ArmaControl} in those
 two lists, {@link ControlClass#getControlClassUpdateGroup()} will be used to get {@link com.kaylerrenslow.armaDialogCreator.data.Change} instances to undo/redo.

 @author Kayler
 @since 11/21/2016 */
public class ControlClassChangeRegistrar implements ChangeRegistrar {

	private boolean disableListener = false;

	private static final ResourceBundle bundle = Lang.EditChangeBundle();

	public ControlClassChangeRegistrar(@NotNull ApplicationData data) {
		final Changelog changelog = Changelog.getInstance();

		UpdateGroupListener<ControlClassUpdate> classUpdateListener = new UpdateGroupListener<ControlClassUpdate>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, ControlClassUpdate data) {
				if (disableListener) {
					return;
				}
				try {
					ControlClassChange newChange = new ControlClassChange(ControlClassChangeRegistrar.this, data);
					ChangeDescriptor latestChangeDescrip = changelog.getMostRecentChange();

					//attempt to merge/combine changes that are similar to reduce changelog's Change count
					if (latestChangeDescrip != null && latestChangeDescrip.getChangeType() == Change.ChangeType.CREATED) {
						if (latestChangeDescrip.getChange() instanceof ControlClassChange) {
							ControlClassChange latestControlClassChangeHead = (ControlClassChange) latestChangeDescrip.getChange();
							ControlClassChange laterSimilarChange = latestControlClassChangeHead.getLatestSimilarChange();
							ControlClassChange latestControlClassChange = laterSimilarChange != null ? laterSimilarChange :
									latestControlClassChangeHead;

							if (latestControlClassChange.isSimilar(newChange)) {

								//Check if the latest change happened less then a second ago.
								//We want to separate changes that are similar but happened some time ago.
								if (System.currentTimeMillis() - latestControlClassChange.getTimeCreated() < 1000) {
									latestControlClassChangeHead.setLaterSimilarChange(newChange);
									return;
								}
							}
						}
					}

					changelog.addChange(newChange);
				} catch (Exception ignore) {
					//shouldn't add to changelog
				}
			}
		};

		ArmaDisplay display = data.getCurrentProject().getEditingDisplay();
		UpdateGroupListener<ControlListChange<ArmaControl>> listChangeListener = new UpdateGroupListener<ControlListChange<ArmaControl>>() {
			@Override
			public void update(@NotNull UpdateListenerGroup<ControlListChange<ArmaControl>> group, ControlListChange<ArmaControl> change) {
				if (change.wasAdded()) {
					change.getAdded().getControl().getControlClassUpdateGroup().addListener(classUpdateListener);
				} else if (change.wasRemoved()) {
					change.getRemoved().getControl().getControlClassUpdateGroup().removeListener(classUpdateListener);
				} else if (change.wasSet()) {
					change.getSet().getOldControl().getControlClassUpdateGroup().removeListener(classUpdateListener);
					change.getSet().getNewControl().getControlClassUpdateGroup().addListener(classUpdateListener);
				}

			}
		};
		display.getControls().getUpdateGroup().addListener(listChangeListener);
		display.getBackgroundControls().getUpdateGroup().addListener(listChangeListener);

		Consumer<? super ArmaControl> deepIteratorForEach = new Consumer<ArmaControl>() {
			@Override
			public void accept(ArmaControl control) {
				control.getControlClassUpdateGroup().addListener(classUpdateListener);
			}
		};
		display.getControls().deepIterator().forEach(deepIteratorForEach);
		display.getBackgroundControls().deepIterator().forEach(deepIteratorForEach);
	}

	@Override
	public void undo(@NotNull Change c) throws ChangeUpdateFailedException {
		if (c instanceof ControlClassChange) {
			ControlClassChange classChange = (ControlClassChange) c;
			disableListener = true;

			if (classChange.getLatestSimilarChange() != null) {
				classChange.getLatestSimilarChange().getAction().undo();
			}
			classChange.getAction().undo();

			disableListener = false;
		}
	}

	@Override
	public void redo(@NotNull Change c) throws ChangeUpdateFailedException {
		if (c instanceof ControlClassChange) {
			ControlClassChange classChange = (ControlClassChange) c;
			disableListener = true;

			if (classChange.getLatestSimilarChange() != null) {
				classChange.getLatestSimilarChange().getAction().redo();
			}
			classChange.getAction().redo();

			disableListener = false;
		}
	}

	private static class ControlClassChange implements Change {

		private final ControlClassChangeRegistrar registrar;
		/** {@link ControlClass} name that triggered the change */
		private final String className;
		private String shortName, description;
		private ControlClassChangeAction action;

		private ControlClassChange laterSimilarChange = null;
		/** How many times {@link #laterSimilarChange} has been set */
		private int laterSimilarChangeCount = 0;

		private ControlClassChangeType changeType;

		@NotNull
		private String additionalInformationForCheckingIfSimilar = "";

		public enum ControlClassChangeType {
			ClassRename, OverrideProperty, ClassExtend,
			PropertyValue, PropertyMacro, PropertyCustomData
		}

		private final long timeCreated = System.currentTimeMillis();

		/**
		 @throws Exception when the <code>classUpdate</code> isn't a change that should be tracked (may already have been tracked in a different form)
		 */
		public ControlClassChange(@NotNull ControlClassChangeRegistrar registrar, @NotNull ControlClassUpdate classUpdate) throws Exception {
			this.registrar = registrar;

			className = classUpdate.getControlClass().getClassName();
			ControlClass controlClass = classUpdate.getControlClass();
			if (classUpdate instanceof ControlClassRenameUpdate) {
				//
				// rename
				//
				ControlClassRenameUpdate update = (ControlClassRenameUpdate) classUpdate;
				shortName = bundle.getString("ControlClassChange.Rename.short_name");
				description = String.format(bundle.getString("ControlClassChange.Rename.description_f"), update.getOldName(), update.getNewName());
				action = new ControlClassChangeAction() {
					@Override
					public void undo() {
						controlClass.setClassName(update.getOldName());
					}

					@Override
					public void redo() {
						controlClass.setClassName(update.getNewName());
					}
				};
				changeType = ControlClassChangeType.ClassRename;
				additionalInformationForCheckingIfSimilar = update.getControlClass().getClassName();
				//
				//
				//
			} else if (classUpdate instanceof ControlClassPropertyUpdate) {
				//
				// property update
				//
				handlePropertyUpdate((ControlClassPropertyUpdate) classUpdate);
				//
				//
				//
			} else if (classUpdate instanceof ControlClassOverridePropertyUpdate) {
				//
				// Override
				//
				ControlClassOverridePropertyUpdate update = (ControlClassOverridePropertyUpdate) classUpdate;
				String propertyName = update.getOveridden().getName();
				if (update.wasOverridden()) {
					shortName = bundle.getString("ControlClassChange.Override.override_short_name");
					description = String.format(bundle.getString("ControlClassChange.Override.override_description_f"), propertyName, className);
				} else {
					shortName = bundle.getString("ControlClassChange.Override.inherit_short_name");
					description = String.format(bundle.getString("ControlClassChange.Override.inherit_description_f"), propertyName, className);
				}
				action = new ControlClassChangeAction() {
					@Override
					public void undo() {
						if (update.wasOverridden()) {
							controlClass.inheritProperty(update.getOveridden().getPropertyLookup());
						} else {
							controlClass.overrideProperty(update.getOveridden().getPropertyLookup());
						}
					}

					@Override
					public void redo() {
						if (update.wasOverridden()) {
							controlClass.overrideProperty(update.getOveridden().getPropertyLookup());
						} else {
							controlClass.inheritProperty(update.getOveridden().getPropertyLookup());
						}
					}
				};
				changeType = ControlClassChangeType.OverrideProperty;
				additionalInformationForCheckingIfSimilar = update.getControlClass().getClassName() + "\0" + propertyName;
				//
				//
				//
			} else if (classUpdate instanceof ControlClassExtendUpdate) {
				//
				// extend
				//
				ControlClassExtendUpdate update = (ControlClassExtendUpdate) classUpdate;
				shortName = bundle.getString("ControlClassChange.Extend.short_name");
				if (update.getNewValue() == null) {
					description = String.format(bundle.getString("ControlClassChange.Extend.remove_extend_description_f"), className, update.getOldValue().getClassName());
				} else {
					description = String.format(bundle.getString("ControlClassChange.Extend.extend_description_f"), className, update.getNewValue().getClassName());
				}
				action = new ControlClassChangeAction() {
					@Override
					public void undo() {
						controlClass.extendControlClass(update.getOldValue());
					}

					@Override
					public void redo() {
						controlClass.extendControlClass(update.getNewValue());
					}
				};
				changeType = ControlClassChangeType.ClassExtend;
				additionalInformationForCheckingIfSimilar = update.getControlClass().getClassName() + "\0" + update.getNewValue();
				//
				//
				//
			} else {
				throw new IllegalArgumentException("WARNING: ControlClassChangeRegistrar.ControlClassChangeRegistrar(): unknown control class update:" + classUpdate);

			}
		}

		private void handlePropertyUpdate(@NotNull ControlClassPropertyUpdate classUpdate) throws Exception {
			ControlPropertyUpdate propertyUpdate = classUpdate.getPropertyUpdate();
			String propertyName = propertyUpdate.getControlProperty().getName();
			ControlProperty property = propertyUpdate.getControlProperty();

			//Use the class name as a prefix to prevent 2 different ControlClass' updates that had the same property names conflicting with each other.
			//Using an ending null character to separate class name from property name
			additionalInformationForCheckingIfSimilar = classUpdate.getControlClass().getClassName() + "\0" + propertyName;

			if (propertyUpdate instanceof ControlPropertyValueUpdate) {
				//
				// value
				//
				ControlPropertyValueUpdate update = (ControlPropertyValueUpdate) propertyUpdate;

				if (update.getOrigin() == ControlPropertyValueUpdate.ValueOrigin.OTHER) {
					shortName = bundle.getString("ControlClassChange.Property.Value.short_name");
					description = String.format(
							bundle.getString("ControlClassChange.Property.Value.description_f"), propertyName, update.getOldValue(), update.getNewValue()
					);
					action = new ControlClassChangeAction() {
						@Override
						public void undo() {
							property.setValue(update.getOldValue());
						}

						@Override
						public void redo() {
							property.setValue(update.getNewValue());
						}
					};
				} else {
					throw new Exception();
				}
				ControlPropertyLookupConstant lookup = update.getControlProperty().getPropertyLookup();
				changeType = ControlClassChangeType.PropertyValue;
				if (lookup == ControlPropertyLookup.X || lookup == ControlPropertyLookup.Y || lookup == ControlPropertyLookup.W || lookup == ControlPropertyLookup.H) {
					//inserted extra null characters to help guarantee no conflicts in property name
					additionalInformationForCheckingIfSimilar = classUpdate.getControlClass().getClassName() + "\0POS_UPDATE\0";
					shortName = bundle.getString("ControlClassChange.Property.Value.control_moved_short_name");
					description = bundle.getString("ControlClassChange.Property.Value.control_moved_description");
				}

				//
				//
				//
			} else if (propertyUpdate instanceof ControlPropertyMacroUpdate) {
				//
				//macro
				//
				ControlPropertyMacroUpdate update = (ControlPropertyMacroUpdate) propertyUpdate;
				if (update.getNewMacro() != null) {
					shortName = bundle.getString("ControlClassChange.Property.Macro.Set.short_name");
					description = String.format(
							bundle.getString("ControlClassChange.Property.Macro.Set.description_f"), propertyName, update.getNewMacro().getKey()
					);
				} else {
					shortName = bundle.getString("ControlClassChange.Property.Macro.Remove.short_name");
					description = String.format(
							bundle.getString("ControlClassChange.Property.Macro.Remove.description_f"), propertyName, update.getOldMacro().getKey()
					);
				}
				action = new ControlClassChangeAction() {
					@Override
					public void undo() {
						property.setValueToMacro(update.getOldMacro());
					}

					@Override
					public void redo() {
						property.setValueToMacro(update.getNewMacro());
					}
				};
				changeType = ControlClassChangeType.PropertyMacro;
				//
				//
				//
			} else if (propertyUpdate instanceof ControlPropertyCustomDataUpdate) {
				//
				//custom data
				//
				ControlPropertyCustomDataUpdate update = (ControlPropertyCustomDataUpdate) propertyUpdate;
				if (update.isSetValueUpdate()) {
					shortName = bundle.getString("ControlClassChange.Property.CustomData.Value.short_name");
					if (update.getNewCustomData() == null) {
						description = String.format(
								bundle.getString("ControlClassChange.Property.CustomData.Value.null_description_f"), propertyName
						);
					} else {
						description = String.format(
								bundle.getString("ControlClassChange.Property.CustomData.Value.non_null_description_f"), propertyName,
								update.getNewCustomData().toString()
						);
					}
				} else {
					shortName = bundle.getString("ControlClassChange.Property.CustomData.Usage.short_name");
					if (update.isUsingCustomData()) {
						description = String.format(
								bundle.getString("ControlClassChange.Property.CustomData.Usage.using_description_f"), propertyName
						);
					} else {
						description = String.format(
								bundle.getString("ControlClassChange.Property.CustomData.Usage.not_using_description_f"), propertyName
						);
					}
				}
				action = new ControlClassChangeAction() {
					@Override
					public void undo() {
						if (update.isSetValueUpdate()) {
							property.setCustomDataValue(update.getOldCustomData());
						} else {
							property.setUsingCustomData(!update.isUsingCustomData());
						}
					}

					@Override
					public void redo() {
						if (update.isSetValueUpdate()) {
							property.setCustomDataValue(update.getNewCustomData());
						} else {
							property.setUsingCustomData(update.isUsingCustomData());
						}
					}
				};
				changeType = ControlClassChangeType.PropertyCustomData;


			} else if (propertyUpdate instanceof ControlPropertyInheritUpdate) {
				//
				//inherit
				//
				//handled by control class override update
				throw new Exception();
				//
				//
				//
			} else {
				throw new IllegalArgumentException("WARNING: ControlClassChangeRegistrar.handlePropertyUpdate(): unknown control property update:" + propertyUpdate);
			}
		}

		/** @return epoch that this object was created */
		public long getTimeCreated() {
			return timeCreated;
		}

		/**
		 Instead of flooding the changelog with barely different {@link ControlClassChange} instances, we will combine
		 them into a {@link ControlClassChange} and invoke each instance independently.
		 How instances are determined to be combined is up to the {@link ControlClassChangeRegistrar}, however,
		 {@link #isSimilar(ControlClassChange)} will be considered.
		 This instance shouldn't internally affect this list and should only be mutated by the {@link ControlClassChangeRegistrar}.
		 <p>
		 <p>
		 <b>For undo and redo:</b> this must be executed before
		 this instance's undo/redo action (inside {@link #getAction()}) method is invoked<br>

		 @return this instance's latest similar change, or null if not set
		 */
		@Nullable
		public ControlClassChange getLatestSimilarChange() {
			return laterSimilarChange;
		}

		public void setLaterSimilarChange(@NotNull ControlClassChange laterSimilarChange) {
			this.laterSimilarChange = laterSimilarChange;
			laterSimilarChangeCount++;
		}

		/** @return true if the change is similar, false if it isn't */
		public boolean isSimilar(@NotNull ControlClassChange change) {
			return this.changeType == change.changeType &&
					this.additionalInformationForCheckingIfSimilar.equals(change.additionalInformationForCheckingIfSimilar);
		}

		@NotNull
		private String getMoreLabel(int count) {
			return "\n..." + String.format(bundle.getString("more_f"), count) + "...\n";
		}

		@NotNull
		@Override
		public String getShortName() {
			return shortName;
		}

		@NotNull
		@Override
		public String getDescription() {
			String moreLabel = getMoreLabel(laterSimilarChangeCount);
			return className + ":\n" + (
					laterSimilarChange != null ?
							(
									laterSimilarChange.description + moreLabel + description
							)
							: description
			);
		}


		@NotNull
		@Override
		public ChangeRegistrar getRegistrar() {
			return registrar;
		}

		@NotNull
		public ControlClassChangeAction getAction() {
			return action;
		}
	}

	private interface ControlClassChangeAction {
		void undo();

		void redo();
	}
}
