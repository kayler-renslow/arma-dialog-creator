package com.armadialogcreator.control;

import com.armadialogcreator.canvas.*;
import com.armadialogcreator.control.impl.ArmaControlLookup;
import com.armadialogcreator.control.impl.StaticControl;
import com.armadialogcreator.core.ConfigProperty;
import com.armadialogcreator.core.ControlPropertyLookup;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.core.RequirementsConfigClass;
import com.armadialogcreator.core.old.ControlProperty;
import com.armadialogcreator.core.sv.SVExpression;
import com.armadialogcreator.core.sv.SVInteger;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.EmptyIterable;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 The base class for all controls.<br>
 <b>NOTE:</b> any classes that extend this class are SHORT-HAND ways of creating this class. Never check if an {@link ArmaControl} instance is an instance of some short hand class
 (like {@link StaticControl}). Not all controls are created with those shorthand classes. All controls are either {@link ArmaControl} or
 {@link ArmaControlGroup}.

 @author Kayler
 @since 05/20/2016. */
public class ArmaControl extends RequirementsConfigClass implements UINode {
	/** Type of the control */
	private ControlType controlType = ControlType.Static;

	/** Renderer of the control for the canvas */
	protected ArmaControlRenderer renderer;

	protected UINode parentNode;

	private ConfigProperty idcProperty, accessProperty;
	protected ArmaDisplay display = null; //todo
	protected UINode rootNode = null;
	private final ArmaControlLookup armaControlLookup;
	private final DataContext userData = new DataContext();
	protected final UpdateListenerGroup<UINodeChange> updateGroup = new UpdateListenerGroup<>();

	/**
	 Create a control where the position is to be determined

	 @param name control class name (e.g. RscText or OMGClass). Keep in mind that it should follow normal Identifier rules (letter letterOrDigit*)
	 @param lookup lookup to use
	 @param resolution resolution to use
	 @param env the environment used to calculate the control's position and other {@link SVExpression} instances stored inside this control's {@link ControlProperty}'s.
	 */
	protected ArmaControl(@NotNull String name, @NotNull ArmaControlLookup lookup, @NotNull ArmaResolution resolution,
						  @NotNull Env env) {
		super(name);
		this.armaControlLookup = lookup;

		defineType(lookup.controlType);
		Class<? extends ArmaControlRenderer> rendererClass = ArmaControlLookup.findByControlType(controlType).renderer;
		try {
			this.renderer = rendererClass.getConstructor(ArmaControl.class, ArmaResolution.class, Env.class).newInstance(this, resolution, env);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException("Class " + rendererClass.getName() + " couldn't be instantiated.");
		}
		idcProperty = findProperty(ControlPropertyLookup.IDC);
		idcProperty.setValue(-1);
		accessProperty = findProperty(ControlPropertyLookup.ACCESS);
	}


	private void defineType(@NotNull ControlType type) {
		findProperty(ControlPropertyLookup.TYPE).getValueObserver().updateValue(new SVInteger(type.getTypeId()));
		this.controlType = type;
	}

	public void resolutionUpdate(@NotNull Resolution newResolution) {
		renderer.resolutionUpdate(newResolution);
	}

	@NotNull
	public ArmaDisplay getDisplay() {
		return display;
	}

	/** Set and define the access property */
	public final void defineAccess(int access) {
		this.accessProperty.setValue(access);
	}

	@NotNull
	public final ControlType getControlType() {
		return controlType;
	}

	@NotNull
	public final ArmaControlRenderer getRenderer() {
		return renderer;
	}

	@NotNull
	public ArmaControl duplicate(@NotNull String controlName) {
		return ArmaControl.createControl(
				controlName,
				ArmaControlLookup.findByControlType(this.controlType),
				renderer.getResolution(),
				renderer.env
		);
	}

	@NotNull
	public static ArmaControl createControl(@NotNull ControlType type, @NotNull String name,
											@NotNull ArmaResolution resolution, @NotNull Env env) {
		ArmaControlLookup lookup = ArmaControlLookup.findByControlType(type);
		return createControl(name, lookup, resolution, env);
	}

	@NotNull
	public static ArmaControl createControl(@NotNull String name, @NotNull ArmaControlLookup lookup,
											@NotNull ArmaResolution resolution,
											@NotNull Env env) {
		if (lookup.controlType == ControlType.ControlsGroup) {
			return new ArmaControlGroup(name, lookup, resolution, env);
		}
		return new ArmaControl(name, lookup, resolution, env);
	}

	@Override
	@NotNull
	public Iterable<UINode> iterateChildNodes() {
		return new EmptyIterable<>();
	}

	@Override
	@NotNull
	public UpdateListenerGroup<UpdateListenerGroup.NoData> renderUpdateGroup() {
		return display.renderUpdateGroup();
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public int indexOf(@NotNull UINode child) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsChildNode(@NotNull UINode node) {
		return false;
	}

	@Override
	public void addChild(@NotNull UINode node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addChild(@NotNull UINode node, int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeChild(@NotNull UINode node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeChild(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void moveChild(@NotNull UINode child, @NotNull UINode newParent, int destIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void acceptMovedChild(@NotNull UINode child, @NotNull UINode oldParent, int destIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	@NotNull
	public DeepUINodeIterable deepIterateChildren() {
		return new DeepUINodeIterable(new EmptyIterable<>());
	}

	@Override
	@Nullable
	public CanvasComponent getComponent() {
		return renderer;
	}

	@Override
	@Nullable
	public UINode getRootNode() {
		return display;
	}

	@Override
	@Nullable
	public UINode getParentNode() {
		return parentNode;
	}

	@Override
	public void setParentNode(@Nullable UINode newParent) {
		this.parentNode = newParent;
	}

	@Override
	public void setRootNode(@Nullable UINode newRoot) {
		this.rootNode = newRoot;
	}

	@Override
	@NotNull
	public DataContext getUserData() {
		return userData;
	}

	@Override
	@NotNull
	public UpdateListenerGroup<UINodeChange> getUpdateGroup() {
		return updateGroup;
	}
}
