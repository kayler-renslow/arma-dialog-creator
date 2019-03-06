package com.armadialogcreator.gui.main.popup.editor;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.sv.SVColor;
import com.armadialogcreator.data.ConfigClassRegistry;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.gui.StagePopupUndecorated;
import com.armadialogcreator.gui.fxcontrol.ComboBoxMenuButton;
import com.armadialogcreator.gui.fxcontrol.SearchTextField;
import com.armadialogcreator.gui.fxcontrol.inputfield.IdentifierChecker;
import com.armadialogcreator.gui.main.ControlPropertiesEditorPane;
import com.armadialogcreator.gui.main.popup.NameInputFieldDialog;
import com.armadialogcreator.img.icons.ADCIcons;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
	private static final Key<Boolean> KEY_HIDE_INHERITED = new Key<>(
			ControlPropertiesConfigPopup.class.getName() + ".hide_if_inherited",
			false
	);

	private final ResourceBundle bundle = Lang.getBundle("ConfigPropertyEditorBundle");

	private ArmaControl control;
	private ControlPropertiesEditorPane editorPane;
	private Label lblClassName;
	private ComboBoxMenuButton<ConfigClass> menuButtonExtendControls;
	private CheckBox checkBoxIsBackgroundControl;
	private final ValueListener<SVColor> backgroundColorListener = new ValueListener<SVColor>() {
		@Override
		public void valueUpdated(@NotNull ValueObserver<SVColor> observer, SVColor oldValue, SVColor newValue) {
			if (newValue != null) {
				setBorderColor(newValue.toJavaFXColor()); //update the popup's border color
			}
		}
	};
	private final NotNullValueListener<String> classNameListener = new NotNullValueListener<String>() {
		@Override
		public void valueUpdated(@NotNull NotNullValueObserver<String> observer, @NotNull String oldValue, @NotNull String newValue) {
			lblClassName.setText(newValue);
			lblClassName.setTooltip(new Tooltip(newValue));
		}
	};
	private final ValueListener<ConfigClass> controlClassExtendListener = new ValueListener<ConfigClass>() {
		@Override
		public void valueUpdated(@NotNull ValueObserver<ConfigClass> observer, ConfigClass oldValue, ConfigClass newValue) {
			if (newValue == null) {
				menuButtonExtendControls.chooseItem((ConfigClass) null);
			} else {
				ConfigClassRegistry registry = ConfigClassRegistry.instance;
				ConfigClass configClass = registry.findConfigClassByName(newValue.getClassName());
				if (configClass == null) {
					return;
				}
				menuButtonExtendControls.chooseItem(configClass);
			}
		}
	};
	private final UpdateGroupListener<ListObserverChange<ArmaControl>> backgroundControlListener = new UpdateGroupListener<ListObserverChange<ArmaControl>>() {
		@Override
		public void update(@NotNull UpdateListenerGroup<ListObserverChange<ArmaControl>> group, @NotNull ListObserverChange<ArmaControl> data) {
			if (data.wasMoved()) {
				if (data.getMoved().getMoved() == control) {
					checkBoxIsBackgroundControl.setSelected(control.getDisplay().controlIsBackgroundControl(control));
				}
			} else if (data.wasRemoved()) {
				if (data.getRemoved().getRemoved() == control) {
					close();
				}
			} else if (data.wasSet()) {
				if (data.getSet().getOld() == control) {
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
		myRootElement.setMinWidth(520);
		myRootElement.setMaxWidth(720);
		myRootElement.setFillWidth(true);
	}

	private void initializeToControl() {
		editorPane = new ControlPropertiesEditorPane(this.control);
		editorPane.setMaxHeight(720d); //prevent the element from being godly large

		Color bg = this.control.getRenderer().getBackgroundColor();

		setBorderColor(bg);

		addHeader(control);

		myRootElement.getChildren().add(editorPane);
		VBox.setVgrow(editorPane, Priority.ALWAYS);

		addFooter(control);

	}

	private void addFooter(ArmaControl control) {
		checkBoxIsBackgroundControl = new CheckBox(bundle.getString("ControlPropertiesConfig.is_background_control"));
		checkBoxIsBackgroundControl.setSelected(control.getDisplay().controlIsBackgroundControl(control));
		checkBoxIsBackgroundControl.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isBackground) {
				/*
				CanvasDisplay<ArmaControl> display = control.getDisplay();
				if (control.getHolder() instanceof ArmaControlGroup) {
					MoveOutOfControlGroupDialog dialog = new MoveOutOfControlGroupDialog(control);
					dialog.show();
					if (dialog.isMoveOut()) {
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
				*/
			}
		});

		final CheckBox checkBoxHideInherited = new CheckBox(bundle.getString("ControlPropertiesConfig.hide_inherited"));
		checkBoxHideInherited.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean hideInherited) {
				editorPane.hideInheritedProperties(hideInherited);
				KEY_HIDE_INHERITED.put(control.getUserData(), hideInherited);
			}
		});

		checkBoxHideInherited.setSelected(KEY_HIDE_INHERITED.get(control.getUserData()));

		HBox hboxLeft;
		{
			MenuButton menu = new MenuButton(bundle.getString("down_arrow_menu_label"));
			{
				MenuItem miRename = new MenuItem(bundle.getString("rename_control"));
				menu.getItems().add(miRename);
				miRename.setOnAction(event -> {
					NameInputFieldDialog<IdentifierChecker, String> dialog = new NameInputFieldDialog<>(
							miRename.getText(), bundle.getString("new_control_name"),
							new IdentifierChecker()
					);
					dialog.getInputField().getValueObserver().updateValue(control.getClassName());
					dialog.getInputField().selectAll();
					dialog.show();
					String value = dialog.getInputField().getValue();
					if (dialog.wasCancelled() || value == null) {
						return;
					}
					control.setClassName(value);
				});
			}
			hboxLeft = new HBox(15, checkBoxIsBackgroundControl, checkBoxHideInherited, menu);
			hboxLeft.setAlignment(Pos.CENTER_LEFT);
		}

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
		Button btnAutoSize = new Button("", new ImageView(ADCIcons.ICON_AUTO_SIZE));
		btnAutoSize.setTooltip(new Tooltip(bundle.getString("auto_size")));
		btnAutoSize.setOnAction(event1 -> {
			myStage.sizeToScene();
		});

		Button btnClose = new Button("x");
		btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				WindowEvent windowEvent = new WindowEvent(myStage, WindowEvent.WINDOW_CLOSE_REQUEST);
				myStage.getOnCloseRequest().handle(windowEvent);
				List<String> missing = editorPane.getMissingProperties();
				boolean goodValues = missing.size() == 0;
				if (!windowEvent.isConsumed() && goodValues) {
					close();
				}
				if (!goodValues) {
					StageDialog dialog = new MissingControlPropertiesConfigDialog(myStage, missing);
					dialog.show();
					if (!dialog.wasCancelled()) {
						close();
					}
				}
			}
		});
		btnClose.getStyleClass().add("close-button");

		menuButtonExtendControls = new ComboBoxMenuButton<>(
				true, bundle.getString("ControlPropertiesConfig.no_extend_class"), null
		);
		/*
		List<CustomControlClass> customControls = Project.getCurrentProject().getAllCustomControlClasses();
		for (CustomControlClass customControlClass : customControls) {
			ImageContainer imageContainer = null;
			try {
				ConfigProperty type = customControlClass.getControlClass().findProperty(ConfigPropertyLookup.TYPE);
				if (type.getValue() instanceof SVNumericValue) {
					ControlType controlType = ControlType.findById(type.getIntValue());
					imageContainer = new BorderedImageView(controlType.getCustomIconPath());
				}

			} catch (IllegalArgumentException ignore) {

			}
			menuButtonExtendControls.addItem(new CBMBMenuItem<>(customControlClass.getControlClass(), imageContainer));
		}*/
		if (control.getExtendClass() != null) {
			menuButtonExtendControls.chooseItem(control.getExtendClass());
		}
		menuButtonExtendControls.getSelectedValueObserver().addListener(new ValueListener<ConfigClass>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<ConfigClass> observer, ConfigClass oldValue, ConfigClass selected) {
				control.extendConfigClass(selected);
			}
		});

		{
			lblClassName = new Label();
			lblClassName.setFont(Font.font(16));
			//update the label through the listener instance
			classNameListener.valueUpdated(control.getClassNameObserver(), null, control.getClassName());
		}
		Label lblColon = new Label(":");
		lblColon.setFont(lblClassName.getFont());
		final HBox hboxLeft;
		{
			hboxLeft = new HBox(5, lblClassName, lblColon, menuButtonExtendControls);
		}
		hboxLeft.setAlignment(Pos.CENTER_LEFT);

		final SearchTextField tfSearch = new SearchTextField();
		tfSearch.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				newValue = newValue != null ? newValue : "";
				editorPane.showPropertiesWithNameContaining(newValue);
			}
		});
		final HBox hboxRight = new HBox(10, btnAutoSize, tfSearch, btnClose);
		hboxRight.setAlignment(Pos.CENTER_RIGHT);

		BorderPane borderPane = new BorderPane(
				null, //center
				null, //top
				hboxRight, //right
				null, //bottom
				hboxLeft //left
		);
		//help distance the left side from right
		BorderPane.setMargin(hboxLeft, new Insets(0, 40, 0, 0));

		ScrollPane borderPaneScrollPane = new ScrollPane(borderPane);
		borderPaneScrollPane.setFitToWidth(false);
		borderPaneScrollPane.setFitToHeight(false);
		borderPaneScrollPane.setMinHeight(50);
		borderPaneScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		borderPaneScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		borderPaneScrollPane.setPadding(new Insets(5));
		borderPaneScrollPane.setStyle("-fx-background-color:transparent;");
		myRootElement.getChildren().add(borderPaneScrollPane);
	}

	@Override
	protected void closing() {
		handleListeners(false);
	}

	@Override
	public void show() {
		handleListeners(true);
		sizeToScene();
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
			//control.getExtendClassReadOnlyObserver().addListener(controlClassExtendListener);
			//control.getDisplay().getBackgroundControls().getUpdateGroup().addListener(backgroundControlListener);
			editorPane.link();
		} else {
			control.getRenderer().getBackgroundColorObserver().removeListener(backgroundColorListener);
			control.getClassNameObserver().removeListener(classNameListener);
			//control.getExtendClassReadOnlyObserver().removeListener(controlClassExtendListener);
			//if (control.getDisplay() != null) { //might have been removed from display
			//	control.getDisplay().getBackgroundControls().getUpdateGroup().removeListener(backgroundControlListener);
			//}

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
