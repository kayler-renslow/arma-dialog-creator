package com.armadialogcreator.data.changeRegistrars;

//import com.armadialogcreator.arma.control.ArmaControl;
//import com.armadialogcreator.arma.control.ArmaDisplay;
//import com.armadialogcreator.core.ConfigPropertyLookup;
//import com.armadialogcreator.core.old.*;
//import com.armadialogcreator.core.sv.SerializableValue;
//import com.armadialogcreator.data.olddata.*;
//import com.armadialogcreator.lang.Lang;
//import com.armadialogcreator.util.ListObserverChange;
//import com.armadialogcreator.util.UpdateGroupListener;
//import com.armadialogcreator.util.UpdateListenerGroup;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.ResourceBundle;
//import java.util.function.Consumer;
//import java.util.function.Function;

/**
 Tracks changes made to {@link Project#getEditingDisplay()} and its {@link ArmaDisplay#getControls()} and
 {@link ArmaDisplay#getBackgroundControls()} lists. For each {@link ArmaControl} in those
 two lists, {@link ControlClassOld#getControlClassUpdateGroup()} will be used to get
 {@link Change} instances to undo/redo.

 @author Kayler
 @since 11/21/2016 */
public class ControlClassChangeRegistrar /*implements ChangeRegistrar*/ {
//
//	private boolean disableListener = false;
//
//	private static final ResourceBundle bundle = Lang.EditChangeBundle();
//
//	public ControlClassChangeRegistrar(@NotNull ApplicationData data) {
//		final Changelog changelog = data.getChangelog();
//
//		UpdateGroupListener<ControlClassUpdate> classUpdateListener = new UpdateGroupListener<ControlClassUpdate>() {
//			@Override
//			public void update(@NotNull UpdateListenerGroup<ControlClassUpdate> group, @NotNull ControlClassUpdate data) {
//				if (disableListener) {
//					return;
//				}
//				try {
//					ControlClassChange newChange = new ControlClassChange(ControlClassChangeRegistrar.this, data);
//					ChangeDescriptor latestChangeDescrip = changelog.getMostRecentChange();
//
//					//attempt to merge/combine changes that are similar to reduce changelog's Change count
//					if (latestChangeDescrip != null && latestChangeDescrip.getChangeType() == Change.ChangeType.CREATED) {
//						if (latestChangeDescrip.getChange() instanceof ControlClassChange) {
//							ControlClassChange latestControlClassChange = (ControlClassChange) latestChangeDescrip.getChange();
//
//							if (latestControlClassChange.isSimilar(newChange)) {
//
//								//Check if the latest change happened less then a second ago.
//								//We want to separate changes that are similar but happened some time ago.
//								if (System.currentTimeMillis() - latestControlClassChange.getTimeCreated() < 1000) {
//									latestControlClassChange.mergeChanges(newChange);
//									return;
//								}
//							}
//						}
//					}
//
//					changelog.addChange(newChange);
//				} catch (Exception ignore) {
//					//shouldn't add to changelog
//				}
//			}
//		};
//
//		ArmaDisplay display = data.getCurrentProject().getEditingDisplay();
//		UpdateGroupListener<ListObserverChange<ArmaControl>> listChangeListener = new UpdateGroupListener<ListObserverChange<ArmaControl>>() {
//			@Override
//			public void update(@NotNull UpdateListenerGroup<ListObserverChange<ArmaControl>> group, @NotNull ListObserverChange<ArmaControl> change) {
//				if (change.wasAdded()) {
//					change.getAdded().getAdded().getControlClassUpdateGroup().addListener(classUpdateListener);
//				} else if (change.wasRemoved()) {
//					change.getRemoved().getRemoved().getControlClassUpdateGroup().removeListener(classUpdateListener);
//				} else if (change.wasSet()) {
//					change.getSet().getOld().getControlClassUpdateGroup().removeListener(classUpdateListener);
//					change.getSet().getNew().getControlClassUpdateGroup().addListener(classUpdateListener);
//				}
//
//			}
//		};
//		display.getControls().getUpdateGroup().addListener(listChangeListener);
//		display.getBackgroundControls().getUpdateGroup().addListener(listChangeListener);
//
//		Consumer<? super ArmaControl> deepIteratorForEach = new Consumer<ArmaControl>() {
//			@Override
//			public void accept(ArmaControl control) {
//				control.getControlClassUpdateGroup().addListener(classUpdateListener);
//			}
//		};
//		display.getControls().deepIterator().forEach(deepIteratorForEach);
//		display.getBackgroundControls().deepIterator().forEach(deepIteratorForEach);
//	}
//
//	@Override
//	public void undo(@NotNull Change c) throws ChangeUpdateFailedException {
//		ControlClassChange classChange = (ControlClassChange) c;
//		disableListener = true;
//		for (ChangeAction action : classChange.getActions()) {
//			action.undo();
//		}
//		disableListener = false;
//	}
//
//	@Override
//	public void redo(@NotNull Change c) throws ChangeUpdateFailedException {
//		ControlClassChange classChange = (ControlClassChange) c;
//		disableListener = true;
//		for (ChangeAction action : classChange.getActions()) {
//			action.redo();
//		}
//		disableListener = false;
//	}
//
//	private static class ControlClassChange implements Change {
//
//		private final ControlClassChangeRegistrar registrar;
//		/** {@link ControlClassOld} name that triggered the change */
//		private final String className;
//		private final ControlClassUpdate classUpdate;
//		private String shortName, description;
//		private List<ChangeAction> actions = new ArrayList<>();
//		/**
//		 Inputs an old {@link ControlClassChange} and outputs the new actions to be used for {@link #actions}
//		 (all of the old actions will be replaced).<p>
//		 This function is used for {@link #mergeChanges(ControlClassChange)}.
//		 If this value is null, old {@link #actions} will be discarded and the newest
//		 change actions will be used for {@link #actions}.
//		 */
//		private Function<ControlClassChange, List<ChangeAction>> mergeActionsFunction;
//
//		private ControlClassChangeType changeType;
//
//		@NotNull
//		private String stringForCheckingIfSimilar = "";
//
//		public enum ControlClassChangeType {
//			ClassRename, OverrideProperty, ClassExtend,
//			PropertyValue, PropertyMacro, PropertyCustomData
//		}
//
//		@NotNull
//		private Function<ControlClassChange, Boolean> checkIfSimilarFunc = change -> {
//			return this.changeType == change.changeType &&
//					this.stringForCheckingIfSimilar.equals(change.stringForCheckingIfSimilar);
//		};
//
//		private long timeCreated = System.currentTimeMillis();
//
//		/**
//		 @throws Exception when the <code>classUpdate</code> isn't a change that should be tracked (may already have been tracked in a different form)
//		 */
//		public ControlClassChange(@NotNull ControlClassChangeRegistrar registrar, @NotNull ControlClassUpdate classUpdate) throws Exception {
//			this.registrar = registrar;
//			className = classUpdate.getOwnerControlClass().getClassName();
//			this.classUpdate = classUpdate;
//
//			ControlClassOld controlClass = classUpdate.getOwnerControlClass();
//			@NotNull ChangeAction action;
//			if (classUpdate instanceof ControlClassRenameUpdate) {
//				//
//				// rename
//				//
//				ControlClassRenameUpdate update = (ControlClassRenameUpdate) classUpdate;
//				shortName = bundle.getString("ControlClassChange.Rename.short_name");
//				description = String.format(bundle.getString("ControlClassChange.Rename.description_f"), update.getOldName(), update.getNewName());
//				action = new ChangeAction() {
//					@Override
//					public void undo() {
//						controlClass.setClassName(update.getOldName());
//					}
//
//					@Override
//					public void redo() {
//						controlClass.setClassName(update.getNewName());
//					}
//
//					@Override
//					@NotNull
//					public String getDebugName() {
//						return "ControlClassRenameUpdate_ChangeAction";
//					}
//				};
//				changeType = ControlClassChangeType.ClassRename;
//				stringForCheckingIfSimilar = update.getOwnerControlClass().getClassName();
//				//
//				//
//				//
//			} else if (classUpdate instanceof ControlClassPropertyUpdate) {
//				//
//				// property update
//				//
//				action = handlePropertyUpdate((ControlClassPropertyUpdate) classUpdate);
//				//
//				//
//				//
//			} else if (classUpdate instanceof ControlClassInheritPropertyUpdate) {
//				//
//				// Override
//				//
//				ControlClassInheritPropertyUpdate update = (ControlClassInheritPropertyUpdate) classUpdate;
//				if (update.isUpdatingExtendClass()) {
//					throw new Exception();
//				}
//				String propertyName = update.getControlProperty().getName();
//				if (update.wasOverridden()) {
//					shortName = bundle.getString("ControlClassChange.Override.override_short_name");
//					description = String.format(bundle.getString("ControlClassChange.Override.override_description_f"), propertyName, className);
//				} else {
//					shortName = bundle.getString("ControlClassChange.Override.inherit_short_name");
//					description = String.format(bundle.getString("ControlClassChange.Override.inherit_description_f"), propertyName, className);
//				}
//				action = new ChangeAction() {
//					@Override
//					public void undo() {
//						if (update.wasOverridden()) {
//							controlClass.inheritProperty(update.getControlProperty().getPropertyLookup());
//						} else {
//							controlClass.overrideProperty(update.getControlProperty().getPropertyLookup());
//						}
//					}
//
//					@Override
//					public void redo() {
//						if (update.wasOverridden()) {
//							controlClass.overrideProperty(update.getControlProperty().getPropertyLookup());
//						} else {
//							controlClass.inheritProperty(update.getControlProperty().getPropertyLookup());
//						}
//					}
//
//					@Override
//					@NotNull
//					public String getDebugName() {
//						return "ControlClassOverridePropertyUpdate_ChangeAction";
//					}
//				};
//				changeType = ControlClassChangeType.OverrideProperty;
//				stringForCheckingIfSimilar = update.getOwnerControlClass().getClassName() + "\0" + propertyName;
//				//
//				//
//				//
//			} else if (classUpdate instanceof ControlClassExtendUpdate) {
//				//
//				// extend
//				//
//				ControlClassExtendUpdate update = (ControlClassExtendUpdate) classUpdate;
//				shortName = bundle.getString("ControlClassChange.Extend.short_name");
//				if (update.getNewExtendClass() == null) {
//					description = String.format(
//							bundle.getString("ControlClassChange.Extend.remove_extend_description_f"),
//							className, update.getOldExtendClass().getClassName()
//					);
//				} else {
//					description = String.format(
//							bundle.getString("ControlClassChange.Extend.extend_description_f"),
//							className, update.getNewExtendClass().getClassName()
//					);
//				}
//				action = new ChangeAction() {
//					@Override
//					public void undo() {
//						controlClass.extendControlClass(update.getOldExtendClass());
//						for (ControlProperty property : update.getOldInherits()) {
//							controlClass.inheritProperty(property.getPropertyLookup());
//						}
//					}
//
//					@Override
//					public void redo() {
//						controlClass.extendControlClass(update.getNewExtendClass());
//					}
//
//					@Override
//					@NotNull
//					public String getDebugName() {
//						return "ControlClassExtendUpdate_ChangeAction";
//					}
//				};
//				changeType = ControlClassChangeType.ClassExtend;
//				stringForCheckingIfSimilar = update.getOwnerControlClass().getClassName() + "\0" + update.getNewExtendClass();
//				mergeActionsFunction = controlClassChange -> {
//					return this.actions; //do nothing to this.actions
//				};
//				checkIfSimilarFunc = oldChange -> {
//					if (!this.className.equals(oldChange.className)) {
//						return false;
//					}
//					if (oldChange.changeType == ControlClassChangeType.OverrideProperty) {
//						ControlClassInheritPropertyUpdate oldUpdate = (ControlClassInheritPropertyUpdate) oldChange.classUpdate;
//						for (ControlProperty property : update.getOldInherits()) {
//							if (oldUpdate.getControlProperty().nameEquals(property)) {
//								if (update.getNewExtendClass() == null) {
//									/*
//									* For this type of merge, we want to combine all individual override property updates
//									* into the control class extend update. With the merge, only 1 change will be needed to
//									* re-inherit the properties that were in place when the extend change is undone.
//									*
//									* The reason why we only want to merge when the new extend class is null is because
//									* no properties are inherited at the time of extending, except the properties
//									* that are undefined (value is null). Since property value updates handle setting
//									* properties to null or not, it is not necessary to re-inherit/re-override
//									* inherited properties that were undefined at the time of extending because its
//									 * handled implicitly by ControlClassOld.extendControlClass().
//									*/
//									return oldUpdate.wasOverridden();
//								}
//							}
//						}
//					}
//					return false;
//				};
//				//
//				//
//				//
//			} else if (classUpdate instanceof ControlClassTemporaryPropertyUpdate || classUpdate instanceof ControlClassTemporaryNestedClassUpdate) {
//				throw new Exception(); //nothing needs to be done
//			} else {
//				System.err.println("WARNING: ControlClassChangeRegistrar.ControlClassChangeRegistrar():" +
//						"unknown control class update:" + classUpdate);
//				throw new Exception();
//
//			}
//
//			actions.add(action);
//		}
//
//		private ChangeAction handlePropertyUpdate(@NotNull ControlClassPropertyUpdate classUpdate) throws Exception {
//			ControlPropertyUpdate propertyUpdate = classUpdate.getPropertyUpdate();
//			String propertyName = propertyUpdate.getControlProperty().getName();
//			ControlProperty updatedProperty = propertyUpdate.getControlProperty();
//
//			//Use the class name as a prefix to prevent 2 different ControlClassOld' updates that had the same property names conflicting with each other.
//			//Using an ending null character to separate class name from property name
//			stringForCheckingIfSimilar = classUpdate.getOwnerControlClass().getClassName() + "\0" + propertyName;
//
//			@NotNull ChangeAction changeAction;
//
//			if (propertyUpdate instanceof ControlPropertyValueUpdate) {
//				//
//				// value
//				//
//				ControlPropertyValueUpdate update = (ControlPropertyValueUpdate) propertyUpdate;
//
//				if (update.getOrigin() == ControlPropertyValueUpdate.ValueOrigin.OTHER) {
//					shortName = bundle.getString("ControlClassChange.Property.Value.short_name");
//					description = String.format(
//							bundle.getString("ControlClassChange.Property.Value.description_f"), propertyName, update.getOldValue(), update.getNewValue()
//					);
//					changeAction = new PropertyValueChangeAction(updatedProperty, update);
//				} else {
//					throw new Exception();
//				}
	//				ConfigPropertyLookupConstant lookup = update.getControlProperty().getPropertyLookup();
//				changeType = ControlClassChangeType.PropertyValue;
	//				if (lookup == ConfigPropertyLookup.X || lookup == ConfigPropertyLookup.Y || lookup == ConfigPropertyLookup.W || lookup == ConfigPropertyLookup.H) {
//					//inserted extra null characters to help guarantee no conflicts in property name
//					stringForCheckingIfSimilar = classUpdate.getOwnerControlClass().getClassName() + "\0POS_UPDATE\0";
//					shortName = bundle.getString("ControlClassChange.Property.Value.control_moved_short_name");
//					description = bundle.getString("ControlClassChange.Property.Value.control_moved_description");
//
//					//using a merge function so that all x,y,w,h property updates are stored together as 1 change
//					mergeActionsFunction = otherClassChange -> {
//						List<ChangeAction> actions = new ArrayList<>(4);
//						List<String> propertiesUpdatedInActions = new ArrayList<>();
//
//						List<ChangeAction> allActionsToMerge = new ArrayList<>(this.actions.size() + otherClassChange.actions.size());
//						allActionsToMerge.addAll(this.actions);
//						//append otherClassChange's actions so that the old are iterated first
//						//and the old are iterated last
//						allActionsToMerge.addAll(otherClassChange.actions);
//
//						for (ChangeAction action : allActionsToMerge) {
//							if (action instanceof PropertyValueChangeAction) {
//								PropertyValueChangeAction valueChangeAction = (PropertyValueChangeAction) action;
//
//								String updatedPropertyName = valueChangeAction.getUpdatedProperty().getName();
//
//								//if the property isn't already in the actions list, add it
//								int i = propertiesUpdatedInActions.indexOf(updatedPropertyName);
//								if (i < 0) {
//									actions.add(action);
//									propertiesUpdatedInActions.add(updatedPropertyName);
//								} else {
//									//use newest update, but change the old value
//									//to use the old action's old value
//									ChangeAction oldAction = actions.set(i, action);
//									valueChangeAction.setOldValue(((PropertyValueChangeAction) oldAction).oldValue);
//								}
//							} else {
//								throw new IllegalStateException("attempting to merge unrelated actions. My changeAction="
//										+ changeAction.getDebugName() + ", unrelated action=" + action.getDebugName()
//										+ ", otherClassChange.description=" + otherClassChange.getDescription());
//							}
//						}
//						return actions;
//					};
//				}
//
//				//
//				//
//				//
//			} else if (propertyUpdate instanceof ControlPropertyMacroUpdate) {
//				//
//				//macro
//				//
//				ControlPropertyMacroUpdate update = (ControlPropertyMacroUpdate) propertyUpdate;
//				if (update.getNewMacro() != null) {
//					shortName = bundle.getString("ControlClassChange.Property.Macro.Set.short_name");
//					description = String.format(
//							bundle.getString("ControlClassChange.Property.Macro.Set.description_f"), propertyName, update.getNewMacro().getKey()
//					);
//				} else {
//					shortName = bundle.getString("ControlClassChange.Property.Macro.Remove.short_name");
//					description = String.format(
//							bundle.getString("ControlClassChange.Property.Macro.Remove.description_f"), propertyName, update.getOldMacro().getKey()
//					);
//				}
//				changeAction = new ChangeAction() {
//					@Override
//					public void undo() {
//						updatedProperty.setValueToMacro(update.getOldMacro());
//					}
//
//					@Override
//					public void redo() {
//						updatedProperty.setValueToMacro(update.getNewMacro());
//					}
//
//					@Override
//					@NotNull
//					public String getDebugName() {
//						return "ControlPropertyMacroUpdate_ChangeAction";
//					}
//				};
//				changeType = ControlClassChangeType.PropertyMacro;
//				//
//				//
//				//
//			} else if (propertyUpdate instanceof ControlPropertyInheritUpdate) {
//				//
//				//inherit
//				//
//				//handled by control class override update
//				throw new Exception();
//				//
//				//
//				//
//			} else {
//				throw new IllegalArgumentException("WARNING: ControlClassChangeRegistrar.handlePropertyUpdate(): unknown control property update:" + propertyUpdate);
//			}
//
//			return changeAction;
//		}
//
//		/**
//		 @return epoch that this object was created. If this change was merged with another, this value will
//		 change to the newest change of the merge
//		 */
//		public long getTimeCreated() {
//			return timeCreated;
//		}
//
//		/** @return true if the change is similar, false if it isn't */
//		public boolean isSimilar(@NotNull ControlClassChange change) {
//			return checkIfSimilarFunc.apply(change);
//		}
//
//		public void mergeChanges(@NotNull ControlClassChange change) {
//			this.timeCreated = change.timeCreated;
//			if (this.mergeActionsFunction == null) {
//				this.actions = change.actions;
//				return;
//			}
//			this.actions = mergeActionsFunction.apply(change);
//		}
//
//		@NotNull
//		@Override
//		public String getShortName() {
//			return shortName;
//		}
//
//		@NotNull
//		@Override
//		public String getDescription() {
//			return className + ":\n" + description;
//		}
//
//		@NotNull
//		@Override
//		public ChangeRegistrar getRegistrar() {
//			return registrar;
//		}
//
//		@NotNull
//		public List<ControlClassChangeRegistrar.ChangeAction> getActions() {
//			return actions;
//		}
//
//		private static class PropertyValueChangeAction implements ChangeAction {
//			private final ControlProperty updatedProperty;
//			private SerializableValue oldValue, newValue;
//
//			public PropertyValueChangeAction(@NotNull ControlProperty updatedProperty, @NotNull ControlPropertyValueUpdate update) {
//				this.updatedProperty = updatedProperty;
//				oldValue = update.getOldValue();
//				newValue = update.getNewValue();
//			}
//
//			@NotNull
//			public ControlProperty getUpdatedProperty() {
//				return updatedProperty;
//			}
//
//			@Nullable
//			public SerializableValue getOldValue() {
//				return oldValue;
//			}
//
//			public void setOldValue(@Nullable SerializableValue oldValue) {
//				this.oldValue = oldValue;
//			}
//
//			@Nullable
//			public SerializableValue getNewValue() {
//				return newValue;
//			}
//
//			public void setNewValue(@Nullable SerializableValue newValue) {
//				this.newValue = newValue;
//			}
//
//			@Override
//			public void undo() {
//				updatedProperty.setValue(oldValue);
//			}
//
//			@Override
//			public void redo() {
//				updatedProperty.setValue(newValue);
//			}
//
//			@Override
//			@NotNull
//			public String getDebugName() {
//				return "ControlPropertyValueChange_ChangeAction";
//			}
//		}
//	}
//
//	private interface ChangeAction {
//		void undo();
//
//		void redo();
//
//		@NotNull String getDebugName();
//	}
//
}
