package com.armadialogcreator;

import com.armadialogcreator.gui.StagePopup;
import com.armadialogcreator.gui.main.popup.SimpleErrorDialog;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.JOptionPane;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 Handles all exceptions in the program

 @author Kayler
 @since 07/06/2016. */
public final class ExceptionHandler implements Thread.UncaughtExceptionHandler {
	private static final ExceptionHandler INSTANCE = new ExceptionHandler();
	private static volatile Throwable lastThrowable = null;
	private static long lastReportTime = -1;

	private ExceptionHandler() {
	}

	public static ExceptionHandler getInstance() {
		return INSTANCE;
	}

	private static boolean checkRepeat(Throwable t) {
		long lastReportTime = ExceptionHandler.lastReportTime;
		ExceptionHandler.lastReportTime = System.currentTimeMillis();

		if (lastThrowable != null) {
			if (t.getClass().getName().equals(lastThrowable.getClass().getName())
					&& (System.currentTimeMillis() - lastReportTime < 300)) {
				System.err.println(t.getClass().getName());
				return true;
			}
		}
		lastThrowable = t;
		return false;
	}

	/**
	 Make an error window popup with the stack trace printed.
	 Only use this for when the error is recoverable.
	 If the error is non-recoverable, use {@link #fatal(Throwable)}
	 */
	public static void error(Throwable t) {
		if (checkRepeat(t)) {
			return;
		}

		t.printStackTrace(System.out);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Label lblMsg = new Label(
						"An internal error occurred. " +
								(t.getMessage() == null ? "" : t.getMessage())
				);
				lblMsg.setWrapText(true);
				SimpleErrorDialog<Label> errorDialog = new SimpleErrorDialog<>(
						ArmaDialogCreator.getPrimaryStage(),
						"ADC Uncaught Error",
						t,
						lblMsg
				);
				errorDialog.show();
			}
		});
	}

	/**
	 Makes an error window popup with the stack trace printed.
	 This method should be used when a <b>non-recoverable</b> error occurred.
	 After the error window is closed, the application will also close.
	 */
	public static void fatal(Throwable t) {
		if (checkRepeat(t)) {
			return;
		}

		t.printStackTrace(System.out);
		if (ArmaDialogCreator.getApplicationDataManager() == null || !ArmaDialogCreator.getPrimaryStage().isShowing()) {
			//can be null if this method is called when ApplicationDataManager had an error before constructor finished
			JOptionPane.showMessageDialog(null, getExceptionString(t), "FATAL ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Label lblMsg = new Label(
						"A FATAL error occurred. " +
								(t.getMessage() == null ? "" : t.getMessage())
				);
				lblMsg.setWrapText(true);
				SimpleErrorDialog<Label> sp = new SimpleErrorDialog<Label>(
						ArmaDialogCreator.getPrimaryStage(),
						"ADC Uncaught Error", t,
						lblMsg
				) {
					@Override
					protected void onCloseRequest(WindowEvent event) {
						boolean good = ArmaDialogCreator.getApplicationDataManager().rescueSave();
						new StagePopup<TextArea>(
								ArmaDialogCreator.getPrimaryStage(),
								new TextArea(
										good ?
												"Your entry was successfully saved regardless of the error."
												: "Your entry couldn't be saved."
								), "Save notification") {
							@Override
							protected void onCloseRequest(WindowEvent event) {
								ArmaDialogCreator.getPrimaryStage().close();
								super.onCloseRequest(event);
							}

							@Override
							public void show() {
								myStage.initModality(Modality.APPLICATION_MODAL);
								super.show();
							}
						};

					}
				};
				sp.setStageSize(300, 300);
				sp.show();
			}
		});
	}

	/**
	 Returns a TextArea with the Throwable's stack trace printed inside it

	 @param thread thread where the Throwable occurred
	 @param t throwable
	 @return TextArea
	 */
	@NotNull
	public static TextArea getExceptionTextArea(Thread thread, Throwable t) {
		String err = getExceptionString(t);
		TextArea ta = new TextArea();
		ta.setPrefSize(700, 700);
		ta.setText(err + "\nTHREAD:" + thread.getName());
		ta.setEditable(false);
		return ta;
	}

	/**
	 Runs {@link #getExceptionTextArea(Thread, Throwable)} with {@link Thread#currentThread()}

	 @param t throwable
	 @return TextArea
	 */
	@NotNull
	public static TextArea getExceptionTextArea(Throwable t) {
		return getExceptionTextArea(Thread.currentThread(), t);
	}

	@NotNull
	public static String getExceptionString(@NotNull Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		pw.close();
		return sw.toString();
	}

	/** Initializes the exception handling (should be called when application is launched) */
	static void init() {
		Thread.setDefaultUncaughtExceptionHandler(INSTANCE);
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		try {
			error(e);
		} catch (Throwable t1) {
			t1.printStackTrace(System.out);
		}
	}

	/** Does same thing as {@link #uncaughtException(Thread, Throwable)} with {@link Thread#currentThread()} as the thread parameter */
	public void uncaughtException(Throwable e) {
		uncaughtException(Thread.currentThread(), e);
	}
}
