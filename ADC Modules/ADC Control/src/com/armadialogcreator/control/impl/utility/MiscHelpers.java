package com.armadialogcreator.control.impl.utility;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.core.AllowedStyleProvider;
import com.armadialogcreator.core.sv.SVControlStyleGroup;
import com.armadialogcreator.core.sv.SVExpression;
import com.armadialogcreator.core.sv.SVString;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.expression.Env;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 07/26/2017 */
public class MiscHelpers {
	public static void paintFlippedImage(@NotNull GraphicsContext gc, @NotNull Image img, int x, int y, int w, int h) {
		gc.save();
		gc.scale(-1, 1);
		gc.drawImage(img, -x - w, y, w, h);
		gc.restore();
	}

	public static void paintRotatedImage(@NotNull GraphicsContext gc, @NotNull Image img, int x, int y, int w, int h, double rotateDeg) {
		gc.save();
		gc.translate(x + w / 2, y + h / 2); //move to center
		gc.rotate(rotateDeg);
		gc.drawImage(img, -w / 2, -h / 2, w, h);
		gc.restore();
	}

	/**
	 This method will check in various ways if the given {@link SerializableValue} can be converted into a {@link SVControlStyleGroup}

	 @return a {@link SVControlStyleGroup} for the given {@link SerializableValue}, or null if can't be created
	 */
	@Nullable
	public static SVControlStyleGroup getGroup(@NotNull Env env, @Nullable SerializableValue value, @NotNull ArmaControl control) {
		if (value instanceof SVControlStyleGroup) {
			return (SVControlStyleGroup) value;
		}
		if (value instanceof SVExpression) {
			value = new SVString(((SVExpression) value).getExpression()); //use the expression text instead of the returned number
		}
		if (value != null) {
			//attempt to create one
			try {
				return SVControlStyleGroup.getGroupFromString(
						env,
						value.toString(),
						control.getSpecProvider() instanceof AllowedStyleProvider ? (AllowedStyleProvider) control.getSpecProvider() : null
				);
			} catch (Exception ignore) {

			}
		}
		return null;
	}
}
