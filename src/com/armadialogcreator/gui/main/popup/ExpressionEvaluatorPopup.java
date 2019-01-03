package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.HelpUrls;
import com.armadialogcreator.ProgramArgument;
import com.armadialogcreator.data.ApplicationData;
import com.armadialogcreator.expression.*;
import com.armadialogcreator.gui.StagePopup;
import com.armadialogcreator.gui.fxcontrol.SyntaxTextArea;
import com.armadialogcreator.gui.main.BrowserUtil;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.KeyValue;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 Popup that has a place for the user to test expressions and see outcomes in the return value and environment overview.

 @author Kayler
 @since 05/24/2017 */
public class ExpressionEvaluatorPopup extends StagePopup<VBox> {

	@NotNull
	private static String getValueAsString(@NotNull Value v) {
		return v.toString();
	}

	private final ResourceBundle bundle = Lang.getBundle("ExpressionEvaluatorPopupBundle");
	private final EnvOverviewPane environmentOverviewPane = new EnvOverviewPane();
	private final CodeAreaPane codeAreaPane = new CodeAreaPane();
	private final StackPane stackPaneResult = new StackPane();
	private final StackPane stackPaneRunTime = new StackPane();
	private final TextArea taConsole = new TextArea();
	private final StackPane stackPaneConsole = new StackPane();
	private boolean showingConsole = false;
	private Task activeEvaluateTask;
	private final Button btnEval, btnTerminate;
	private volatile long evaluateStartTime;
	private volatile long evaluateEndTime;

	public ExpressionEvaluatorPopup() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(0), null);

		setTitle(bundle.getString("popup_title"));
		setStageSize(820, 550);

		taConsole.setText(bundle.getString("CodeArea.console_init") + " ");
		String[] commands = ExpressionInterpreter.getSupportedCommands();
		for (String command : commands) {
			taConsole.appendText(command != commands[commands.length - 1] ? command + ", " : command);
		}
		taConsole.appendText("\n\n");
		taConsole.setWrapText(true);

		//setup toolbar
		ToolBar toolBar;
		{
			btnEval = new Button(bundle.getString("Toolbar.evaluate"));
			btnEval.setOnAction(event -> evaluateText());

			btnTerminate = new Button(bundle.getString("Toolbar.terminate"));
			btnTerminate.setDisable(true);
			btnTerminate.setOnAction(event -> {
				activeEvaluateTask.cancel(true);
				btnTerminate.setDisable(false);
			});

			Button btnToggleConsole = new Button(bundle.getString("Toolbar.toggle_console"));
			btnToggleConsole.setOnAction(event -> toggleConsole());

			Button btnHelp = new Button(Lang.ApplicationBundle().getString("Popups.btn_help"));
			btnHelp.setOnAction(event -> help());

			toolBar = new ToolBar(
					btnEval, btnTerminate,
					new Separator(Orientation.VERTICAL),
					btnToggleConsole,
					new Separator(Orientation.VERTICAL),
					btnHelp
			);
		}

		myRootElement.getChildren().add(toolBar);

		//setup code area pane and environment overview
		VBox vboxAfterToolBar = new VBox(10);
		VBox.setVgrow(vboxAfterToolBar, Priority.ALWAYS);
		vboxAfterToolBar.setPadding(new Insets(10));
		vboxAfterToolBar.setMinWidth(300);
		VBox vboxEnvOverview = new VBox(5, new Label(bundle.getString("EnvOverview.label")), environmentOverviewPane);
		HBox hbox = new HBox(5, codeAreaPane, vboxEnvOverview);
		HBox.setHgrow(codeAreaPane, Priority.ALWAYS);
		HBox.setHgrow(vboxEnvOverview, Priority.SOMETIMES);

		VBox.setVgrow(hbox, Priority.ALWAYS);
		vboxAfterToolBar.getChildren().add(hbox);
		vboxAfterToolBar.getChildren().add(stackPaneConsole);

		vboxAfterToolBar.getChildren().add(new HBox(
				5,
				footerValueLabel(bundle.getString("CodeArea.return_value")), stackPaneResult,
				new Separator(Orientation.VERTICAL),
				footerValueLabel(bundle.getString("CodeArea.run_time")), stackPaneRunTime
		));

		ScrollPane scrollPane = new ScrollPane(vboxAfterToolBar);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);

		VBox.setVgrow(scrollPane, Priority.ALWAYS);

		myRootElement.getChildren().add(scrollPane);

	}

	private void toggleConsole() {
		if (showingConsole) {
			stackPaneConsole.getChildren().clear();
		} else {
			stackPaneConsole.getChildren().add(taConsole);
		}
		showingConsole = !showingConsole;

	}

	private void evaluateText() {
		stackPaneResult.getChildren().clear();
		stackPaneRunTime.getChildren().clear();
		btnTerminate.setDisable(false);
		btnEval.setDisable(true);
		environmentOverviewPane.clearEnv();

		activeEvaluateTask = new Task<Boolean>() {
			ExpressionInterpreter interpreter = ExpressionInterpreter.newInstance();

			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				interpreter.shutdownAndDisable();
				return true;
			}

			@Override
			protected Boolean call() throws Exception {

				SimpleEnv env = new SimpleEnv(
						new ApplicationData.UnaryCommandValueProviderImpl(
								ApplicationData.getManagerInstance()
						)
				);

				String returnValueString;
				String consoleString;

				try {
					evaluateStartTime = System.currentTimeMillis();
					Value returnValue = interpreter.evaluateStatements(codeAreaPane.getText(), env).get();
					evaluateEndTime = System.currentTimeMillis();
					interpreter.shutdownAndDisable();
					returnValueString = getValueAsString(returnValue);
					consoleString = bundle.getString("CodeArea.success");
				} catch (ExpressionEvaluationException e) {
					evaluateEndTime = System.currentTimeMillis();
					if (e instanceof TerminateEvaluationException) {
						returnValueString = bundle.getString("CodeArea.terminated");
						consoleString = returnValueString;
					} else {
						returnValueString = bundle.getString("CodeArea.error");
						consoleString = e.getMessage();
						if (ArmaDialogCreator.containsUnamedLaunchParameter(ProgramArgument.ShowDebugFeatures)) {
							e.printStackTrace(System.out);
						}
					}
				}

				String finalConsoleString = consoleString;
				String finalReturnValueString = returnValueString;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						stackPaneResult.getChildren().add(footerValueLabel(finalReturnValueString));
						environmentOverviewPane.setEnv(env);
						taConsole.appendText(finalConsoleString + "\n\n");

						btnTerminate.setDisable(true);
						btnEval.setDisable(false);
						stackPaneRunTime.getChildren().add(
								footerValueLabel(
										String.format(
												"%d %s",
												(evaluateEndTime - evaluateStartTime),
												bundle.getString("CodeArea.milliseconds")
										)
								)
						);
					}
				});

				return true;
			}
		};
		Thread t = new Thread(activeEvaluateTask, "ADC - Mini SQF Evaluator Popup - Execute SQF Task");
		t.setDaemon(false);
		t.start();
	}

	private Label footerValueLabel(@NotNull String s) {
		Label label = new Label(s);
		label.setFont(Font.font(14));
		return label;
	}

	@Override
	protected void help() {
		BrowserUtil.browse(HelpUrls.MINI_SQF_EVALUATOR);
	}

	private class CodeAreaPane extends SyntaxTextArea {
		@RegExp
		private final String decimal = "(\\.[0-9]+)|([0-9]+\\.[0-9]+)";
		@RegExp
		private final String integer = "[0-9]+";
		private final String exponent = String.format("(%s|%s)[Ee][+-]?[0-9]*", integer, decimal);
		@RegExp
		private final String hex = "0[xX]0*[0-9a-fA-F]+";

		private final Pattern pattern = Pattern.compile(
				"(?<IDENTIFIER>\\b([a-zA-Z_$][a-zA-Z_$0-9]*)\\b)" +
						String.format("|(?<NUMBER>(%s)|(%s)|(%s)|(%s))", exponent, hex, decimal, integer) +
						"|(?<STRING>('[^']*')+|(\"[^\"]*\")+)" +
						"|(?<COMMENT>(//[^\r\n]*)|(/\\*.*?\\*/))"
		);

		public CodeAreaPane() {
			getStylesheets().add("/com/armadialogcreator/gui/expressionSyntax.css");
			//			setParagraphGraphicFactory(LineNumberFactory.get(this));
			richChanges()
					.filter(c -> !c.getInserted().equals(c.getRemoved()))
					.subscribe(c -> {
						setStyleSpans(0, computeHighlighting(getText()));
					});
			getStyleClass().add("bordered-syntax-text-area");

			setWrapText(true);
		}

		private StyleSpans<Collection<String>> computeHighlighting(String text) {
			Matcher matcher = pattern.matcher(text);
			int lastKwEnd = 0;
			StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

			while (matcher.find()) {
				String styleClass = null;
				String s;
				if ((s = matcher.group("IDENTIFIER")) != null) {
					for (String command : ExpressionInterpreter.getSupportedCommands()) {
						if (s.equalsIgnoreCase(command)) {
							styleClass = "command";
							break;
						}
					}
					if (s.equals("_x")) {
						styleClass = "magic-var";
					}
				} else if (matcher.group("NUMBER") != null) {
					styleClass = "number";
				} else if (matcher.group("STRING") != null) {
					styleClass = "string";
				} else if (matcher.group("COMMENT") != null) {
					styleClass = "comment";
				}

				if (styleClass == null) {
					continue;
				}

				spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
				spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
				lastKwEnd = matcher.end();
			}
			spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
			return spansBuilder.create();
		}
	}

	private class EnvOverviewPane extends StackPane {

		private final ListView<String> listView = new ListView<>();

		public EnvOverviewPane() {
			getChildren().add(listView);
			listView.setPlaceholder(new Label(bundle.getString("EnvOverview.no_env")));
			listView.setMinWidth(300);
			listView.setStyle("-fx-font-family:monospace");
			VBox.setVgrow(this, Priority.ALWAYS);
		}

		public void setEnv(@NotNull Env e) {
			listView.getItems().clear();
			List<KeyValue<String, Value>> list = new ArrayList<>();
			for (KeyValue<String, Value> kv : e) {
				list.add(kv);
			}
			int maxVarLength = 1;
			for (KeyValue<String, Value> kv : list) {
				maxVarLength = Math.max(kv.getKey().length(), maxVarLength);
			}
			for (KeyValue<String, Value> kv : list) {
				listView.getItems().add(String.format("%-" + maxVarLength + "s = %s", kv.getKey(), getValueAsString(kv.getValue())));
			}
		}

		public void clearEnv() {
			listView.getItems().clear();
		}
	}

}
