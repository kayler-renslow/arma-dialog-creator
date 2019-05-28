package com.armadialogcreator.data.olddata;

import com.armadialogcreator.core.sv.SVImage;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.data.Arma3ExternalImagePathConverter;
import com.armadialogcreator.data.ImagesTool;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.util.function.Consumer;

/**
 @author Kayler
 @since 06/28/2017 */
public class ImageHelper {
	private static final Image OGV = new Image("/com/armadialogcreator/gui/img/ogv.jpg");

	/**
	 Get an {@link Image} from a {@link SerializableValue}. This method is asynchronous. The provided callback will
	 be executed on a different thread than the thread that invoked this method

	 @param pathValue the {@link SerializableValue} to get the image from
	 @param callback the "callback" function. The parameter of the consumer is the resulted image, which may be null
	 */
	public static void getImageAsync(@Nullable SerializableValue pathValue, @NotNull Consumer<Image> callback) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				File f;
				if (pathValue instanceof SVImage) {
					SVImage image = (SVImage) pathValue;
					if (image.getNonPaaImageFile() != null) {
						f = image.getNonPaaImageFile();
					} else {
						f = image.getImageFile(); //use this file anyways
					}
				} else if (pathValue == null) {
					f = null;
				} else {
					f = new File(pathValue.toString());
				}

				if (f != null && f.getPath().startsWith("\\")) {
					String internalImage = Arma3ExternalImagePathConverter.getInstance().getImagePath(f.getPath());
					if (internalImage != null) {
						callback.accept(new Image(internalImage));
						return;
					}
				}

				if (f != null && f.getName().endsWith(".ogv")) {
					callback.accept(OGV);
					return;
				}

				if (f != null) {
					f = ImagesTool.getImageFile(f.getPath(), new ImagesTool.SimpleImageConversionCallback());
				}

				if (f != null && f.exists()) {
					try {
						FileInputStream fis = new FileInputStream(f);
						Image imageToPaint = new Image(fis);
						fis.close();
						if (imageToPaint.isError()) {
							callback.accept(null);
							return;
						}
						callback.accept(imageToPaint);
						return;
					} catch (Exception ignore) {

					}
				}
				callback.accept(null);
			}
		}, "ADC - ImageHelper.java");
		t.setDaemon(true);
		t.start();

	}
}
