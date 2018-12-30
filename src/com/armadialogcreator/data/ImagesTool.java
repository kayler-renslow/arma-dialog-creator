package com.armadialogcreator.data;

import com.armadialogcreator.arma.util.ArmaTools;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.geom.IllegalPathStateException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 @author Kayler
 @since 06/28/2017 */
public class ImagesTool {
	private static final List<ImageConversionSubscriber> subs = Collections.synchronizedList(new ArrayList<>());

	/**
	 @param subscriber the subscriber
	 */
	public static void subscribeToConversion(@NotNull ImageConversionSubscriber subscriber) {
		subs.add(subscriber);
	}

	/**
	 Gets an image (can be a .paa image) from the given path. If the image is a .paa, will use <code>callback</code>
	 to help convert the image. The conversion will not be done on a different thread!
	 <p>
	 The provided image file path can be an absolute path (e.g. starts with drive letter) or a relative path.
	 If the path is relative, the image file will be retrieved relative to the current
	 {@link Workspace#getWorkspaceDirectory()}. The current {@link Project} will be {@link Project#getCurrentProject()}.
	 <p>
	 This method will also automatically insert any converted paa images into
	 {@link Workspace#getGlobalResourceRegistry()} with a {@link PaaImageExternalResource} instance.
	 Because of this, a given image file could be a .paa image that was already converted. If the converted image
	 already exists in {@link Workspace#getGlobalResourceRegistry()}, that converted file will be returned.
	 <p>
	 If at any point the conversion is cancelled or fails, <code>imageFilePath</code>'s file will be returned.

	 Note: if the conversion fails or is cancelled, the returned file may be a .paa image!

	 @param imageFilePath the path to the image
	 @param callback callback to use
	 @return the image file, which may be a paa image
	 */
	@NotNull
	public static File getImageFile(@NotNull String imageFilePath, @NotNull ImageConversionCallback callback) {
		Workspace workspace = Workspace.getWorkspace();

		imageFilePath = imageFilePath.trim(); //need to trim or Paths.get() will throw exception

		Path imagePath;
		try {
			imagePath = Paths.get(imageFilePath);
		} catch (IllegalPathStateException e) {
			File f = new File(imageFilePath);
			callback.conversionFailed(f, e);
			return f;
		}
		File imageFile;
		if (imagePath.isAbsolute()) {
			imageFile = imagePath.toFile();
		} else {
			imageFile = workspace.getFileForName(imageFilePath);
		}

		if (imageFilePath.endsWith(".paa")) {
			WorkspaceResourceRegistry globalResourceRegistry = workspace.getGlobalResourceRegistry();

			ExternalResource resource = globalResourceRegistry.getResourceByFile(imageFile);
			File convertDest = globalResourceRegistry.getFileForName(imageFile.getName() + ".png");
			if (resource == null) {
				ImagesTool tool = new ImagesTool(callback, imageFile, convertDest);
				PaaImageExternalResource newResource = tool.convert();
				if (newResource != null) {
					globalResourceRegistry.addResource(newResource);
					return newResource.getConvertedImage();
				}
			} else {
				String convertedPaaPath = resource.getPropertyValue(PaaImageExternalResource.KEY_CONVERTED_IMAGE);
				File convertedImageFile = convertedPaaPath == null ? imageFile : new File(convertedPaaPath);
				if (!convertedImageFile.exists() || convertedImageFile == imageFile) {
					ImagesTool tool = new ImagesTool(callback, imageFile, convertDest);
					PaaImageExternalResource r = tool.convert();
					if (r != null) {
						File newConvertedFile = r.getConvertedImage();
						resource.setPropertyValue(PaaImageExternalResource.KEY_CONVERTED_IMAGE, newConvertedFile.getAbsolutePath());
						return newConvertedFile;
					}
				}
				return convertedImageFile;
			}
		}

		return imageFile;
	}

	private final ImageConversionCallback callback;
	private final File paaImage;
	private File convertDestFile;

	private ImagesTool(@NotNull ImageConversionCallback callback, @NotNull File paaImage, @NotNull File convertDestFile) {
		this.callback = new ImageConversionCallbackWrapper(callback);
		this.paaImage = paaImage;
		this.convertDestFile = convertDestFile;
	}

	@Nullable
	private PaaImageExternalResource convert() {
		File a3Tools = callback.arma3ToolsDirectory();
		if (a3Tools == null) {
			callback.conversionCancelled(paaImage);
			return null;
		}
		if (convertDestFile.exists()) {
			String newFileName = callback.replaceExistingConvertedImage(paaImage, convertDestFile);
			if (newFileName == null) {
				callback.conversionCancelled(paaImage);
				return null;
			}
			File parent = convertDestFile.getAbsoluteFile().getParentFile();
			convertDestFile = new File(parent.getAbsolutePath() + "/" + newFileName);
		}
		boolean good;
		callback.conversionStarted(paaImage);
		try {
			good = ArmaTools.imageToPAA(a3Tools, paaImage, convertDestFile, 1000 * 10 /*ten seconds*/);
		} catch (IOException e) {
			callback.conversionFailed(paaImage, e);
			return null;
		}
		if (!good) {
			callback.conversionFailed(paaImage, null);
			return null;
		}

		callback.conversionSucceeded(paaImage, convertDestFile);

		return new PaaImageExternalResource(paaImage, convertDestFile);
	}

	/**
	 A callback used in conjunction with {@link #getImageFile(String, ImageConversionCallback)}

	 @author Kayler
	 @since 06/28/2017
	 */
	public interface ImageConversionCallback extends ImageConversionBase {

		/**
		 This is invoked when the given image file can't be converted due to the destination file
		 of the converted image already exists.

		 @param image the image file that is being converted
		 @param destination the destination for the to-convert image file
		 @return the file name for the given image to convert to:
		 <ul>
		 <li>If overwriting the existing destination
		 is desired, just return the destination's file name. </li>
		 <li>return null to cancel the conversion</li>
		 <li>if overwriting is not desired, return a new <b>name</b> for the file</li>
		 </ul>
		 */
		@Nullable String replaceExistingConvertedImage(@NotNull File image, @NotNull File destination);

		/**
		 Get the Arma 3 Tools directory. If returns null, the conversion will be cancelled.

		 @return the directory to Arma 3 tools, or null if to cancel the conversion
		 */
		@Nullable File arma3ToolsDirectory();
	}

	private interface ImageConversionBase {

		/**
		 Invoked when a conversion of an image file has begun

		 @param image the image file that is being converted
		 */
		void conversionStarted(@NotNull File image);

		/**
		 Invoked when the conversion failed

		 @param image the image file that is being converted
		 @param e the exception that created the failure, or null if the failure is unknown
		 */
		void conversionFailed(@NotNull File image, @Nullable Exception e);

		/**
		 Invoked when the conversion succeeded

		 @param image the image file that is being converted
		 @param resultFile the image file that was resulted in the conversion
		 */
		void conversionSucceeded(@NotNull File image, @Nullable File resultFile);

		/**
		 Invoked when the conversion was cancelled

		 @param image the image file that is being converted
		 */
		void conversionCancelled(@NotNull File image);
	}

	/**
	 An {@link ImageConversionSubscriber} are used to listen in on conversions that are taking place.
	 The subscribers are notified of changes before the {@link ImageConversionCallback} passed through
	 {@link #getImageFile(String, ImageConversionCallback)}.
	 <p>
	 Every method on this interface will <b>not</b> be on the same thread as the {@link ImageConversionCallback}.

	 @author Kayler
	 @since 06/28/2017
	 */
	public interface ImageConversionSubscriber extends ImageConversionBase {

	}

	private static class ImageConversionCallbackWrapper implements ImageConversionCallback {
		private final ImageConversionCallback callback;
		private final LinkedBlockingQueue<Runnable> subTodo = new LinkedBlockingQueue<>();
		private final Runnable closeSubThreadRunnable = () -> {
		};
		private final Thread subThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Runnable take = subTodo.take();
						if (take == closeSubThreadRunnable) { //can't insert null into queue
							return;
						}
						take.run();
					} catch (InterruptedException ignore) {
					}
				}
			}
		}, "ADC - ImagesTool Subscriber Thread");

		public ImageConversionCallbackWrapper(@NotNull ImageConversionCallback callback) {
			this.callback = callback;
			subThread.setDaemon(true);
			subThread.start();
		}

		@Override
		public @Nullable String replaceExistingConvertedImage(@NotNull File image, @NotNull File destination) {
			return callback.replaceExistingConvertedImage(image, destination);
		}

		@Override
		public @Nullable File arma3ToolsDirectory() {
			return callback.arma3ToolsDirectory();
		}

		@Override
		public void conversionStarted(@NotNull File image) {
			subTodo.add(new Runnable() {
				@Override
				public void run() {
					for (ImageConversionSubscriber sub : subs) {
						sub.conversionStarted(image);
					}
				}
			});
			callback.conversionStarted(image);
		}

		@Override
		public void conversionFailed(@NotNull File image, @Nullable Exception e) {
			subTodo.add(new Runnable() {
				@Override
				public void run() {
					for (ImageConversionSubscriber sub : subs) {
						sub.conversionFailed(image, e);
					}
				}
			});
			closeSubThread();
			callback.conversionFailed(image, e);
		}

		@Override
		public void conversionSucceeded(@NotNull File image, @Nullable File resultFile) {
			subTodo.add(new Runnable() {
				@Override
				public void run() {
					for (ImageConversionSubscriber sub : subs) {
						sub.conversionSucceeded(image, resultFile);
					}
				}
			});

			closeSubThread();
			callback.conversionSucceeded(image, resultFile);
		}

		@Override
		public void conversionCancelled(@NotNull File image) {
			subTodo.add(new Runnable() {
				@Override
				public void run() {
					for (ImageConversionSubscriber sub : subs) {
						sub.conversionCancelled(image);
					}
				}
			});
			closeSubThread();
			callback.conversionCancelled(image);
		}

		private void closeSubThread() {
			subTodo.add(closeSubThreadRunnable);
		}
	}

	/**
	 A simple {@link ImageConversionCallback} instance.

	 @author Kayler
	 @since 06/28/2017
	 */
	public static class SimpleImageConversionCallback implements ImageConversionCallback {

		/** @return destination.getName() */
		@Override
		public @Nullable String replaceExistingConvertedImage(@NotNull File image, @NotNull File destination) {
			return destination.getName();
		}

		/** @return <code>ApplicationProperty.A3_TOOLS_DIR.getValue()</code> */
		@Override
		public @Nullable File arma3ToolsDirectory() {
			return ApplicationProperty.A3_TOOLS_DIR.getValue();
		}

		/** empty implementation */
		@Override
		public void conversionStarted(@NotNull File image) {

		}

		/** empty implementation */
		@Override
		public void conversionFailed(@NotNull File image, @Nullable Exception e) {

		}

		/** empty implementation */
		@Override
		public void conversionSucceeded(@NotNull File image, @Nullable File resultFile) {

		}

		/** empty implementation */
		@Override
		public void conversionCancelled(@NotNull File image) {

		}
	}
}
