package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVImage;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.util.function.Function;

/**
 @author Kayler
 @since 06/28/2017 */
public class ImageHelper {
	private static final Image OGV = new Image("/com/kaylerrenslow/armaDialogCreator/gui/img/ogv.jpg");

	/**
	 Get an {@link Image} from a {@link SerializableValue}. This method is asynchronous. The provided callback will
	 be executed on a different thread than the thread that invoked this method

	 @param pathValue the {@link SerializableValue} to get the image from
	 @param imageGetFunc the "callback" function. The function can return any value. The parameter of the function
	 is the resulted image, which may be null
	 */
	public static void getImageAsync(@Nullable SerializableValue pathValue, @NotNull Function<Image, Void> imageGetFunc) {
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

				if (f != null) {
					Image internalImage = Arma3ExternalImagePathConverter.getInstance().getImage(f.getPath());
					if (internalImage != null) {
						imageGetFunc.apply(internalImage);
						return;
					}
				}

				if (f != null && f.getName().endsWith(".ogv")) {
					imageGetFunc.apply(OGV);
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
							imageGetFunc.apply(null);
							return;
						}
						imageGetFunc.apply(imageToPaint);
						return;
					} catch (Exception ignore) {

					}
				}
				imageGetFunc.apply(null);
			}
		}, "ADC - ImageHelper.java");
		t.setDaemon(true);
		t.start();

	}
}
