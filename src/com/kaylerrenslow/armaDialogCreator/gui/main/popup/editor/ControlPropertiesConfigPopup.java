package com.kaylerrenslow.armaDialogCreator.gui.main.popup.editor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlGroup;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.data.ProjectControlClassRegistry;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.*;
import com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor.ControlPropertiesEditorPane;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StagePopupUndecorated;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.ControlListChange;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ResourceBundle;

/**
 Used for editing a control and it's control properties.

 @author Kayler
 @since 05/31/2016. */
public class ControlPropertiesConfigPopup extends StagePopupUndecorated<VBox> {
	private final ResourceBundle bundle = Lang.getBundle("ControlPropertyEditorBundle");

	private ArmaControl control;
	private ControlPropertiesEditorPane editorPane;
	private Label lblClassName;
	private ComboBoxMenuButton<ControlClass> menuButtonExtendControls;
	private CheckBox checkBoxIsBackgroundControl;
	private final ValueListener<AColor> backgroundColorListener = new ValueListener<AColor>() {
		@Override
		public void valueUpdated(@NotNull ValueObserver<AColor> observer, AColor oldValue, AColor newValue) {
			if (newValue != null) {
				setBorderColor(newValue.toJavaFXColor()); //update the popup's border color
			}
		}
	};
	private final ValueListener<String> classNameListener = new ValueListener<String>() {
		@Override
		public void valueUpdated(@NotNull ValueObserver<String> observer, String oldValue, String newValue) {
			lblClassName.setText(newValue);
		}
	};
	private final ValueListener<ControlClass> controlClassExtendListener = new ValueListener<ControlClass>() {
		@Override
		public void valueUpdated(@NotNull ValueObserver<ControlClass> observer, ControlClass oldValue, ControlClass newValue) {
			if (newValue == null) {
				menuButtonExtendControls.chooseItem((ControlClass) null);
			} else {
				ProjectControlClassRegistry registry = Project.getCurrentProject().getCustomControlClassRegistry();
				CustomControlClass customControlClass = registry.findCustomControlClassByName(newValue.getClassName());
				if (customControlClass == null) {
					return;
				}
				menuButtonExtendControls.chooseItem(customControlClass.getControlClass());
			}
		}
	};
	private final UpdateGroupListener<ControlListChange<ArmaControl>> backgroundControlListener = new UpdateGroupListener<ControlListChange<ArmaControl>>() {
		@Override
		public void update(@NotNull UpdateListenerGroup<ControlListChange<ArmaControl>> group, ControlListChange<ArmaControl> data) {
			if (data.wasMoved()) {
				if (data.getMoved().getMovedControl() == control) {
					checkBoxIsBackgroundControl.setSelected(control.isBackgroundControl());
				}
			} else if (data.wasRemoved()) {
				if (data.getRemoved().getControl() == control) {
					close();
				}
			} else if (data.wasSet()) {
				if (data.getSet().getOldControl() == control) {
					close();
				}
			}
		}
	};


	public ControlPropertiesConfigPopup(@NotNull ArmaControl control) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null);
		this.control = control;
		initializePopup();
		initializeToControl();
	}

	private void initializePopup() {
		myRootElement.getStyleClass().add("rounded-node");
		myStage.initStyle(StageStyle.TRANSPARENT);

		myScene.setFill(Color.TRANSPARENT);
		myRootElement.setPadding(new Insets(20.0));
		myRootElement.setMaxHeight(720d); //prevent the element from being godly large
	}

	private void initializeToControl() {
		editorPane = new ControlPropertiesEditorPane(this.control);

		Color bg = this.control.getRenderer().getBackgroundColor();

		setBorderColor(bg);

		addHeader(control);

		myRootElement.getChildren().add(editorPane);
		VBox.setVgrow(editorPane, Priority.ALWAYS);

		addFooter(control);

	}

	private void addFooter(ArmaControl control) {
		checkBoxIsBackgroundControl = new CheckBox(bundle.getString("ControlPropertiesConfig.is_background_control"));
		checkBoxIsBackgroundControl.setSelected(control.isBackgroundControl());
		checkBoxIsBackgroundControl.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isBackground) {
				CanvasDisplay<ArmaControl> display = control.getDisplay();
				if (control.getHolder() instanceof ArmaControlGroup) {
					MoveOutOfControlGroupDialog popup = new MoveOutOfControlGroupDialog(control);
					popup.show();
					if (popup.isMoveOut()) {
						ArmaControlGroup group = (ArmaControlGroup) control.getHolder();
						group.getControls().move(control, (isBackground ? display.getBackgroundControls() : display.getControls()));
						return;
					} else {
						return;
					}
				} else if (control.getHolder() instanceof ArmaDisplay) {
					if (isBackground) {
						display.getControls().move(control, display.getBackgroundControls());
					} else {
						display.getBackgroundControls().move(control, display.getControls());
					}
					return;
				} else {
					throw new IllegalStateException("unknown holder type:" + control.getHolder().getClass().getName());
				}
			}
		});

		final CheckBox checkBoxHideInherited = new CheckBox(bundle.getString("ControlPropertiesConfig.hide_inherited"));
		checkBoxHideInherited.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				editorPane.hideInheritedProperties(newValue);
			}
		});

		final HBox hboxLeft = new HBox(15, checkBoxIsBackgroundControl, checkBoxHideInherited);

		myRootElement.getChildren().add(
				new BorderPane(
						null, //center
						null, //top
						null, //right
						null, //bottom
						hboxLeft //left
				)
		);
	}

	private void addHeader(ArmaControl control) {
		Button btnClose = new Button("x");
		btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				WindowEvent windowEvent = new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST);
				myStage.getOnCloseRequest().handle(windowEvent);
				List<ControlProperty> missing = editorPane.getMissingProperties();
				boolean goodValues = missing.size() == 0;
				if (!windowEvent.isConsumed() && goodValues) {
					close();
				}
				if (!goodValues) {
					StagePopup popup = new MissingControlPropertiesConfigPopup(myStage, missing);
					popup.show();
					popup.requestFocus();
				}
			}
		});
		btnClose.getStyleClass().add("close-button");

		menuButtonExtendControls = new ComboBoxMenuButton<>(
				true, bundle.getString("ControlPropertiesConfig.no_extend_class"), null
		);
		ReadOnlyList<CustomControlClass> customControls = Project.getCurrentProject().getCustomControlClassRegistry().getControlClassList();
		for (CustomControlClass customControlClass : customControls) {
			ImageContainer imageContainer = null;
			try {
				ControlProperty type = customControlClass.getControlClass().findProperty(ControlPropertyLookup.TYPE);
				ControlType controlType = ControlType.findById(type.getIntValue());
				imageContainer = new BorderedImageView(controlType.getCustomIcon());

			} catch (IllegalArgumentException ignore) {

			}
			menuButtonExtendControls.addItem(new CBMBMenuItem<>(customControlClass.getControlClass(), imageContainer));
		}
		if (control.getExtendClass() != null) {
			menuButtonExtendControls.chooseItem(control.getExtendClass());
		}
		menuButtonExtendControls.getSelectedValueObserver().addListener(new ReadOnlyValueListener<ControlClass>() {
			@Override
			public void valueUpdated(@NotNull ReadOnlyValueObserver<ControlClass> observer, ControlClass oldValue, ControlClass selected) {
				if (selected != null) {
					control.extendControlClass(selected);
				}
			}
		});

		lblClassName = new Label(control.getClassName());
		final HBox hboxLeft = new HBox(5, lblClassName, new Label(":"), menuButtonExtendControls);
		hboxLeft.setAlignment(Pos.CENTER_LEFT);

		final SearchTextField tfSearch = new SearchTextField();
		tfSearch.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				newValue = newValue != null ? newValue : "";
				editorPane.showPropertiesWithNameContaining(newValue);
			}
		});
		final HBox hboxRight = new HBox(10, tfSearch, btnClose);
		hboxRight.setAlignment(Pos.CENTER_RIGHT);
		myRootElement.getChildren().add(
				new BorderPane(
						null, //center
						null, //top
						hboxRight, //right
						null, //bottom
						hboxLeft //left
				)
		);
	}

	@Override
	protected void closing() {
		handleListeners(false);
	}

	@Override
	public void show() {
		handleListeners(true);
		super.show();
	}

	@NotNull
	public ArmaControl getControl() {
		return control;
	}

	private void setBorderColor(Color bg) {
		myRootElement.setStyle(String.format("-fx-border-color: rgba(%f%%,%f%%,%f%%,%f);", bg.getRed() * 100.0, bg.getGreen() * 100.0, bg.getBlue() * 100.0, bg.getOpacity()));
	}

	private void handleListeners(boolean add) {
		if (add) {
			control.getRenderer().getBackgroundColorObserver().addListener(backgroundColorListener);
			control.getClassNameObserver().addListener(classNameListener);
			control.getExtendClassObserver().addListener(controlClassExtendListener);
			control.getDisplay().getBackgroundControls().getUpdateGroup().addListener(backgroundControlListener);
			editorPane.relink();
		} else {
			control.getRenderer().getBackgroundColorObserver().removeListener(backgroundColorListener);
			control.getClassNameObserver().removeListener(classNameListener);
			control.getExtendClassObserver().removeListener(controlClassExtendListener);
			if (control.getDisplay() != null) { //might have been removed from display
				control.getDisplay().getBackgroundControls().getUpdateGroup().removeListener(backgroundControlListener);
			}

			editorPane.unlink();
		}
	}

	private class MoveOutOfControlGroupDialog extends StageDialog<VBox> {

		private boolean moveOut = false;

		public MoveOutOfControlGroupDialog(ArmaControl c) {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), bundle.getString("ControlPropertiesConfig.MoveOutOfGroupPopup.popup_title"), true, true, false);
			myRootElement.getChildren().addAll(
					new Label(String.format(bundle.getString("ControlPropertiesConfig.MoveOutOfGroupPopup.message_f"), c.getClassName()))
			);
			myStage.initStyle(StageStyle.UTILITY);
			sizeToScene();
		}

		@Override
		protected void ok() {
			moveOut = true;
			super.ok();
		}

		public boolean isMoveOut() {
			return moveOut;
		}
	}

}
