package com.armadialogcreator.control;

import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.ImageReference;
import javafx.application.Platform;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 @author K
 @since 5/27/19 */
public class ImagePool {
	private static final Map<ImageProvider, Map<SerializableValue, ImageReference>> imagePool = new HashMap<>();

	public static void addProvider(@NotNull ImageProvider provider) {
		imagePool.put(provider, new HashMap<>());
	}

	public static void getImageAsync(@NotNull SerializableValue path, @NotNull Consumer<ImageReference> callback) {
		Thread t = new Thread(() -> {
			synchronized (imagePool) {
				for (Map.Entry<ImageProvider, Map<SerializableValue, ImageReference>> entry : imagePool.entrySet()) {
					ImageProvider provider = entry.getKey();
					Map<SerializableValue, ImageReference> stringImageMap = entry.getValue();
					if (provider.supportsImagePath(path)) {
						ImageReference image = stringImageMap.computeIfAbsent(path, s -> {
							Image newImage = null;
							int attempts = 0;
							while (attempts < 3) {
								try {
									newImage = provider.createImage(path).get(20, TimeUnit.SECONDS);
									break;
								} catch (Exception ignore) {

								}
								attempts++;
							}

							garbageCollectImages();

							return new ImageReference(newImage);
						});

						Platform.runLater(() -> {
							callback.accept(image);
						});
						return;
					}
				}
				Platform.runLater(() -> {
					callback.accept(null);
				});
			}
		});
		t.setDaemon(true);
		t.start();
	}

	public static void garbageCollectImages() {
		synchronized (imagePool) {
			for (Map.Entry<ImageProvider, Map<SerializableValue, ImageReference>> imageProviderMapEntry : imagePool.entrySet()) {
				ArrayList<SerializableValue> keysToRemove = new ArrayList<>();
				for (Map.Entry<SerializableValue, ImageReference> ifentry : imageProviderMapEntry.getValue().entrySet()) {
					if (ifentry.getValue().isUnused()) {
						keysToRemove.add(ifentry.getKey());
					}
				}
				for (SerializableValue v : keysToRemove) {
					imageProviderMapEntry.getValue().remove(v);
				}
			}
		}
	}

	public interface ImageProvider {
		/**
		 @return the {@link Image} instance for the given path
		 @throws IllegalArgumentException if {@link #supportsImagePath(SerializableValue)} returns false
		 */
		@NotNull Future<Image> createImage(@NotNull SerializableValue path);

		boolean supportsImagePath(@NotNull SerializableValue path);
	}
}
