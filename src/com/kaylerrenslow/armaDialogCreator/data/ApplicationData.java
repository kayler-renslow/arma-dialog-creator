package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.arma.util.PositionCalculator;
import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.expression.SimpleEnv;
import com.kaylerrenslow.armaDialogCreator.expression.Value;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 06/07/2016.
 */
public final class ApplicationData {

	private ValueObserver<ArmaDisplay> editingDisplay = new ValueObserver<>(new ArmaDisplay(0));

	private final SimpleEnv globalEnv = new SimpleEnv() {
		@Override
		public @Nullable Value getValue(String identifier) {
			Value v = super.getValue(identifier);
			if (v == null) {
				v = getMacroRegistry().getMacroValue(identifier);
			}
			return v;
		}
	};

	/** Get the display that the dialog creator is editing right now. */
	@NotNull
	public ArmaDisplay getEditingDisplay() {
		return editingDisplay.getValue();
	}

	@NotNull
	public ValueObserver<ArmaDisplay> getEditingDisplayObserver() {
		return editingDisplay;
	}

	/** Set the display that the dialog creator is to edit (notifies the valueObserver for the display as well). */
	public void setEditingDisplay(@NotNull ArmaDisplay display) {
		this.editingDisplay.updateValue(display);
	}

	public MacroRegistry getMacroRegistry() {
		return MacroRegistry.INSTANCE;
	}

	/**
	 Get the {@link Env} instance used for converting identifiers that match any Macro instances key retrieved from {@link Macro#getKey()}. The identifiers can also be
	 {@link PositionCalculator#SAFE_ZONE_X},
	 {@link PositionCalculator#SAFE_ZONE_Y},
	 {@link PositionCalculator#SAFE_ZONE_W},
	 {@link PositionCalculator#SAFE_ZONE_H},
	 */
	public Env getGlobalExpressionEnvironment() {
		//update the environment
		globalEnv.put(PositionCalculator.SAFE_ZONE_X, new Value.NumVal(ArmaDialogCreator.getCanvasView().getCurrentResolution().getSafeZoneX()));
		globalEnv.put(PositionCalculator.SAFE_ZONE_Y, new Value.NumVal(ArmaDialogCreator.getCanvasView().getCurrentResolution().getSafeZoneY()));
		globalEnv.put(PositionCalculator.SAFE_ZONE_W, new Value.NumVal(ArmaDialogCreator.getCanvasView().getCurrentResolution().getSafeZoneW()));
		globalEnv.put(PositionCalculator.SAFE_ZONE_H, new Value.NumVal(ArmaDialogCreator.getCanvasView().getCurrentResolution().getSafeZoneH()));
		
		return globalEnv;
	}
}
