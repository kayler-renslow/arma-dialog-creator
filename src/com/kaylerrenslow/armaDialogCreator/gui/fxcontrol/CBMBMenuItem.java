package com.kaylerrenslow.armaDialogCreator.gui.fxcontrol;

import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 11/15/2016 */
public class CBMBMenuItem<V> extends MenuItem {
	protected final V value;
	protected final ImageContainer imageContainer;

	public CBMBMenuItem(@NotNull V value, @Nullable ImageContainer image) {
		super(value.toString(), image == null ? null : image.getNode());
		this.value = value;
		this.imageContainer = image;
		setMnemonicParsing(false);
	}

	public CBMBMenuItem(@NotNull V value) {
		this(value, null);
	}

	@NotNull
	public V getValue() {
		return value;
	}

	protected void actionEvent(ActionEvent event) {

	}

}
