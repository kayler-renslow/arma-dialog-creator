package com.kaylerrenslow.armaDialogCreator.data.changeRegistrars;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.*;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.ControlListChange;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.UpdateGroupListener;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;

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
							ControlClassChange latestControlClassChange = (ControlClassChange) latestChangeDescrip.getChange();

							if (latestControlClassChange.isSimilar(newChange)) {

								//Check if the latest change happened less then a second ago.
								//We want to separate changes that are similar but happened some time ago.
								if (System.currentTimeMillis() - latestControlClassChange.getTimeCreated() < 1000) {
									latestControlClassChange.mergeChanges(newChange);
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
		ControlClassChange classChange = (ControlClassChange) c;
		disableListener = true;
		for (ChangeAction action : classChange.getActions()) {
			action.undo();
		}
		disableListener = false;
	}

	@Override
	public void redo(@NotNull Change c) throws ChangeUpdateFailedException {
		ControlClassChange classChange = (ControlClassChange) c;
		disableListener = true;
		for (ChangeAction action : classChange.getActions()) {
			action.redo();
		}
		disableListener = false;
	}

	private static class ControlClassChange implements Change {

		private final ControlClassChangeRegistrar registrar;
		/** {@link ControlClass} name that triggered the change */
		private final String className;
		private String shortName, description;
		private List<ChangeAction> actions = new ArrayList<>();
		/**
		 Inputs an old {@link ControlClassChange} and outputs the new actions to be used for {@link #actions}
		 (all of the old actions will be replaced).<p>
		 This function is used for {@link #mergeChanges(ControlClassChange)}.
		 If this value is null, old {@link #actions} will be discarded and the newest
		 change actions will be used for {@link #actions}.
		 */
		private Function<ControlClassChange, List<ChangeAction>> mergeActionsFunction;

		private ControlClassChangeType changeType;

		@NotNull
		private String stringForCheckingIfSimilar = "";

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
			@NotNull ChangeAction action;
			if (classUpdate instanceof ControlClassRenameUpdate) {
				//
				// rename
				//
				ControlClassRenameUpdate update = (ControlClassRenameUpdate) classUpdate;
				shortName = bundle.getString("ControlClassChange.Rename.short_name");
				description = String.format(bundle.getString("ControlClassChange.Rename.description_f"), update.getOldName(), update.getNewName());
				action = new ChangeAction() {
					@Override
					public void undo() {
						controlClass.setClassName(update.getOldName());
					}

					@Override
					public void redo() {
						controlClass.setClassName(update.getNewName());
					}

					@Override
					@NotNull
					public String getDebugName() {
						return "ControlClassRenameUpdate_ChangeAction";
					}
				};
				changeType = ControlClassChangeType.ClassRename;
				stringForCheckingIfSimilar = update.getControlClass().getClassName();
				//
				//
				//
			} else if (classUpdate instanceof ControlClassPropertyUpdate) {
				//
				// property update
				//
				action = handlePropertyUpdate((ControlClassPropertyUpdate) classUpdate);
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
				action = new ChangeAction() {
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

					@Override
					@NotNull
					public String getDebugName() {
						return "ControlClassOverridePropertyUpdate_ChangeAction";
					}
				};
				changeType = ControlClassChangeType.OverrideProperty;
				stringForCheckingIfSimilar = update.getControlClass().getClassName() + "\0" + propertyName;
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
				action = new ChangeAction() {
					@Override
					public void undo() {
						controlClass.extendControlClass(update.getOldValue());
					}

					@Override
					public void redo() {
						controlClass.extendControlClass(update.getNewValue());
					}

					@Override
					@NotNull
					public String getDebugName() {
						return "ControlClassExtendUpdate_ChangeAction";
					}
				};
				changeType = ControlClassChangeType.ClassExtend;
				stringForCheckingIfSimilar = update.getControlClass().getClassName() + "\0" + update.getNewValue();
				//
				//
				//
			} else {
				throw new IllegalArgumentException("WARNING: ControlClassChangeRegistrar.ControlClassChangeRegistrar(): unknown control class update:" + classUpdate);

			}

			actions.add(action);
		}

		private ChangeAction handlePropertyUpdate(@NotNull ControlClassPropertyUpdate classUpdate) throws Exception {
			ControlPropertyUpdate propertyUpdate = classUpdate.getPropertyUpdate();
			String propertyName = propertyUpdate.getControlProperty().getName();
			ControlProperty updatedProperty = propertyUpdate.getControlProperty();

			//Use the class name as a prefix to prevent 2 different ControlClass' updates that had the same property names conflicting with each other.
			//Using an ending null character to separate class name from property name
			stringForCheckingIfSimilar = classUpdate.getControlClass().getClassName() + "\0" + propertyName;

			@NotNull ChangeAction changeAction;

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
					changeAction = new PropertyValueChangeAction(updatedProperty, update);
				} else {
					throw new Exception();
				}
				ControlPropertyLookupConstant lookup = update.getControlProperty().getPropertyLookup();
				changeType = ControlClassChangeType.PropertyValue;
				if (lookup == ControlPropertyLookup.X || lookup == ControlPropertyLookup.Y || lookup == ControlPropertyLookup.W || lookup == ControlPropertyLookup.H) {
					//inserted extra null characters to help guarantee no conflicts in property name
					stringForCheckingIfSimilar = classUpdate.getControlClass().getClassName() + "\0POS_UPDATE\0";
					shortName = bundle.getString("ControlClassChange.Property.Value.control_moved_short_name");
					description = bundle.getString("ControlClassChange.Property.Value.control_moved_description");

					//using a merge function so that all x,y,w,h property updates are stored together as 1 change
					mergeActionsFunction = otherClassChange -> {
						List<ChangeAction> actions = new ArrayList<>(4);
						List<String> propertiesUpdatedInActions = new ArrayList<>();

						List<ChangeAction> allActionsToMerge = new ArrayList<>(this.actions.size() + otherClassChange.actions.size());
						allActionsToMerge.addAll(this.actions);
						//append otherClassChange's actions so that the old are iterated first
						//and the old are iterated last
						allActionsToMerge.addAll(otherClassChange.actions);

						for (ChangeAction action : allActionsToMerge) {
							if (action instanceof PropertyValueChangeAction) {
								PropertyValueChangeAction valueChangeAction = (PropertyValueChangeAction) action;

								String updatedPropertyName = valueChangeAction.getUpdatedProperty().getName();

								//if the property isn't already in the actions list, add it
								int i = propertiesUpdatedInActions.indexOf(updatedPropertyName);
								if (i < 0) {
									actions.add(action);
									propertiesUpdatedInActions.add(updatedPropertyName);
								} else {
									//use newest update, but change the old value
									//to use the old action's old value
									ChangeAction oldAction = actions.set(i, action);
									valueChangeAction.setOldValue(((PropertyValueChangeAction) oldAction).oldValue);
								}
							} else {
								throw new IllegalStateException("attempting to merge unrelated actions. My changeAction="
										+ changeAction.getDebugName() + ", unrelated action=" + action.getDebugName()
										+ ", otherClassChange.description=" + otherClassChange.getDescription());
							}
						}
						return actions;
					};
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
				changeAction = new ChangeAction() {
					@Override
					public void undo() {
						updatedProperty.setValueToMacro(update.getOldMacro());
					}

					@Override
					public void redo() {
						updatedProperty.setValueToMacro(update.getNewMacro());
					}

					@Override
					@NotNull
					public String getDebugName() {
						return "ControlPropertyMacroUpdate_ChangeAction";
					}
				};
				changeType = ControlClassChangeType.PropertyMacro;
				//
				//
				//
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

			return changeAction;
		}

		/** @return epoch that this object was created */
		public long getTimeCreated() {
			return timeCreated;
		}

		/** @return true if the change is similar, false if it isn't */
		public boolean isSimilar(@NotNull ControlClassChange change) {
			return this.changeType == change.changeType &&
					this.stringForCheckingIfSimilar.equals(change.stringForCheckingIfSimilar);
		}

		public void mergeChanges(@NotNull ControlClassChange change) {
			if (this.mergeActionsFunction == null) {
				this.actions = change.actions;
				return;
			}
			this.actions = mergeActionsFunction.apply(change);
		}

		@NotNull
		@Override
		public String getShortName() {
			return shortName;
		}

		@NotNull
		@Override
		public String getDescription() {
			return className + ":\n" + description;
		}

		@NotNull
		@Override
		public ChangeRegistrar getRegistrar() {
			return registrar;
		}

		@NotNull
		public List<ControlClassChangeRegistrar.ChangeAction> getActions() {
			return actions;
		}

		private static class PropertyValueChangeAction implements ChangeAction {
			private final ControlProperty updatedProperty;
			private SerializableValue oldValue, newValue;

			public PropertyValueChangeAction(@NotNull ControlProperty updatedProperty, @NotNull ControlPropertyValueUpdate update) {
				this.updatedProperty = updatedProperty;
				oldValue = update.getOldValue();
				newValue = update.getNewValue();
			}

			@NotNull
			public ControlProperty getUpdatedProperty() {
				return updatedProperty;
			}

			@Nullable
			public SerializableValue getOldValue() {
				return oldValue;
			}

			public void setOldValue(@Nullable SerializableValue oldValue) {
				this.oldValue = oldValue;
			}

			@Nullable
			public SerializableValue getNewValue() {
				return newValue;
			}

			public void setNewValue(@Nullable SerializableValue newValue) {
				this.newValue = newValue;
			}

			@Override
			public void undo() {
				updatedProperty.setValue(oldValue);
			}

			@Override
			public void redo() {
				updatedProperty.setValue(newValue);
			}

			@Override
			@NotNull
			public String getDebugName() {
				return "ControlPropertyValueChange_ChangeAction";
			}
		}
	}

	private interface ChangeAction {
		void undo();

		void redo();

		@NotNull String getDebugName();
	}

}
