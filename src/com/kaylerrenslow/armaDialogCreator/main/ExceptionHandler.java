/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.main;

import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 @author Kayler
 Handles all exceptions in the program
 Created on 07/06/2016. */
public final class ExceptionHandler implements Thread.UncaughtExceptionHandler {
	private static final ExceptionHandler INSTANCE = new ExceptionHandler();

	private ExceptionHandler() {
	}

	public static ExceptionHandler getInstance() {
		return INSTANCE;
	}

	/** Make an error window popup with the stack trace printed. Only use this for when the error is recoverable. If the error is non-recoverable, use {@link #fatal(Throwable)} */
	public static void error(Throwable t) {
		error(Thread.currentThread(), t);
	}

	/** Make an error window popup with the stack trace printed. Only use this for when the error is recoverable. If the error is non-recoverable, use {@link #fatal(Throwable)} */
	public static void error(Thread threadWhereErrorOccurred, Throwable t) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				StagePopup<TextArea> popup = new StagePopup<>(ArmaDialogCreator.getPrimaryStage(), getExceptionTextArea(threadWhereErrorOccurred, t), "An internal error occurred.");
				popup.show();
			}
		});
	}

	/** Makes an error window popup with the stack trace printed. This method should be used when a <b>non-recoverable</b> error occurred. After the error window is closed, the application will also close. */
	public static void fatal(Throwable t) {
		fatal(Thread.currentThread(), t);
	}

	/** Makes an error window popup with the stack trace printed. This method should be used when a <b>non-recoverable</b> error occurred. After the error window is closed, the application will also close. */
	public static void fatal(Thread threadWereErrorOccurred, Throwable t) {
		if (ArmaDialogCreator.getApplicationDataManager() == null || !ArmaDialogCreator.getPrimaryStage().isShowing()) { //can be null if this method is called when ApplicationDataManager had an error before constructor finished
			JOptionPane.showMessageDialog(null, getExceptionString(t), "FATAL ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				StagePopup sp = new StagePopup<TextArea>(ArmaDialogCreator.getPrimaryStage(), getExceptionTextArea(threadWereErrorOccurred, t), "A FATAL error occurred.") {
					@Override
					protected void onCloseRequest(WindowEvent event) {
						boolean good = ArmaDialogCreator.getApplicationDataManager().forceSave();
						new StagePopup<TextArea>(ArmaDialogCreator.getPrimaryStage(), new TextArea(good ? "Your entry was successfully saved regardless of the error." : "Your entry couldn't be saved."), "Save notification") {
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

	@NotNull
	private static TextArea getExceptionTextArea(Thread thread, Throwable t) {
		String err = getExceptionString(t);
		TextArea ta = new TextArea();
		ta.setPrefSize(700, 700);
		ta.setText("THREAD:" + thread.getName() + "\n" + err);
		return ta;
	}

	private static String getExceptionString(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return "An error occurred. Please report this message to the developer(s).\n" + sw.toString();
	}

	/** Initializes the exception handling (should be called when application is launched) */
	static void init() {
		Thread.setDefaultUncaughtExceptionHandler(INSTANCE);
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		e.printStackTrace(System.out);
		try {
			error(t, e);
		} catch (Throwable t1) {
			t1.printStackTrace(System.out);
		}
	}

	/**Does same thing as {@link #uncaughtException(Thread, Throwable)} with {@link Thread#currentThread()} as the thread parameter*/
	public void uncaughtException(Throwable e) {
		uncaughtException(Thread.currentThread(), e);
	}
}
