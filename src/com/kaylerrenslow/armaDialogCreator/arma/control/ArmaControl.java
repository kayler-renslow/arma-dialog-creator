package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.util.screen.PositionCalculator;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.Resolution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 The base class for all controls
 Created on 05/20/2016. */
public class ArmaControl extends ArmaControlClass {
	/** Resolution of the control. Should not change the reference, but rather change the values inside the resolution. */
	protected final Resolution resolution;
	/** Type of the control */
	protected ControlType type = ControlType.STATIC;
	/** Style of the control TODO: allow multiple styles */
	protected ControlStyle style = ControlStyle.CENTER;
	/** Renderer of the control for the canvas */
	protected ArmaControlRenderer renderer;

	protected double x, y, width, height;
	/** Control id (-1 if doesn't matter) */
	protected int idc = -1;

	private final ControlProperty idcProperty, typeProperty, styleProperty, xProperty, yProperty, wProperty, hProperty, accessProperty;

	/**
	 Create a control where the position is to be determined

	 @param name control class name (e.g. RscText or OMGClass). Keep in mind that it should follow normal Identifier rules (letter letterOrDigit*)
	 @param resolution resolution to use
	 @param renderer renderer of the control
	 @param requiredSubClasses required sub-classes of the control (like maybe Scrollbar class)
	 @param optionalSubClasses optional sub-classes of the control
	 */
	public ArmaControl(@NotNull String name, @NotNull Resolution resolution, @NotNull Class<? extends ArmaControlRenderer> renderer, @Nullable ArmaControlClass[] requiredSubClasses, @Nullable ArmaControlClass[] optionalSubClasses) {
		super(name);
		this.resolution = resolution;
		try {
			this.renderer = renderer.newInstance();
			this.renderer.setMyControl(this);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException("Class " + renderer.getName() + " couldn't be instantiated.");
		}
		if (requiredSubClasses != null) {
			addRequiredSubClasses(requiredSubClasses);
		}
		if (optionalSubClasses != null) {
			addOptionalSubClasses(optionalSubClasses);
		}
		idcProperty = ControlPropertyLookup.IDC.getIntProperty(idc);
		typeProperty = ControlPropertyLookup.TYPE.getIntProperty(idc);
		styleProperty = ControlPropertyLookup.STYLE.getIntProperty(idc);
		xProperty = ControlPropertyLookup.X.getFloatProperty(x);
		yProperty = ControlPropertyLookup.Y.getFloatProperty(y);
		wProperty = ControlPropertyLookup.W.getFloatProperty(width);
		hProperty = ControlPropertyLookup.H.getFloatProperty(height);
		accessProperty = ControlPropertyLookup.ACCESS.getPropertyWithNoData();
		addRequiredProperties(idcProperty, typeProperty, styleProperty, xProperty, yProperty, wProperty, hProperty);
		addOptionalProperties(accessProperty);
		//do not define properties x,y,w,h,idc,type,style here so that they are marked as missed when checking what requirements have been filled
	}

	/**
	 Create a control where the position is known

	 @param name control class name (e.g. RscText or OMGClass). Keep in mind that it should follow normal Identifier rules (letter letterOrDigit*)
	 @param idc control id (-1 if doesn't matter)
	 @param type type of the control
	 @param style style of the control
	 @param x x position (abs region)
	 @param y y position (abs region)
	 @param width width (abs region)
	 @param height height (abs region)
	 @param resolution resolution to use
	 @param renderer renderer for the control
	 @param requiredSubClasses required sub-classes of the control (maybe Scrollbar class)
	 @param optionalSubClasses optional sub-classes of the control
	 */
	public ArmaControl(@NotNull String name, int idc, @NotNull ControlType type, @NotNull ControlStyle style, double x, double y, double width, double height, @NotNull Resolution resolution, @NotNull Class<? extends ArmaControlRenderer> renderer, @Nullable ArmaControlClass[] requiredSubClasses, @Nullable ArmaControlClass[] optionalSubClasses) {
		this(name, resolution, renderer, requiredSubClasses, optionalSubClasses);
		defineType(type);
		defineIdc(idc);
		defineStyle(style);
		defineX(x);
		defineY(y);
		defineW(width);
		defineH(height);
	}

	/** Set x and define the x control property */
	public void defineX(double x) {
		xProperty.setValue(x);
		setX(x);
	}

	/** Just set x position without updating the property */
	protected void setX(double x) {
		this.x = x;
		renderer.setX1(PositionCalculator.getScreenX(resolution, x));
	}

	/** Set y and define the y control property */
	public void defineY(double y) {
		yProperty.setValue(y);
		setY(y);
	}

	/** Just set the y position without updating the y property */
	protected void setY(double y) {
		this.y = y;
		renderer.setY1(PositionCalculator.getScreenY(resolution, y));
	}

	/** Set w (width) and define the w control property */
	public void defineW(double width) {
		wProperty.setValue(width);
		setW(width);
	}

	/** Set the width without updating it's control property */
	protected void setW(double width) {
		this.width = width;
		int w = PositionCalculator.getScreenWidth(resolution, width);
		renderer.setX2(renderer.getX1() + w);
	}

	/** Set h (height) and define the h control property */
	public void defineH(double height) {
		hProperty.setValue(height);
		setH(height);
	}

	/** Just set height without setting control property */
	protected void setH(double height) {
		this.height = height;
		int h = PositionCalculator.getScreenHeight(resolution, height);
		renderer.setY2(renderer.getY1() + h);
	}

	protected void setPositionWH(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		renderer.setPositionWHSilent(PositionCalculator.getScreenX(resolution, x), PositionCalculator.getScreenY(resolution, y), PositionCalculator.getScreenWidth(resolution, w), PositionCalculator.getScreenHeight(resolution, h));
	}

	/** Set idc and define the idc control property */
	public void defineIdc(int idc) {
		setIdc(idc);
		idcProperty.setValue(idc);
	}

	/** Set idc without telling control property */
	protected void setIdc(int idc) {
		this.idc = idc;
	}

	/** Set the control type and define the type control property */
	protected void defineType(ControlType type) {
		setType(type);
	}

	/** Just set the control type without changing control property */
	protected void setType(ControlType type) {
		this.type = type;
	}

	/** Set and define the style control property */
	protected void defineStyle(ControlStyle style) {
		setStyle(style);
	}

	/** Just set the style */
	protected void setStyle(ControlStyle style) {
		this.style = style;
	}

	/** Set and define the access property */
	public void defineAccess(int access) {
		this.accessProperty.setValue(access);
	}

	/** Update the resolution to a new resolution and then recalculate positions and size */
	public void updateResolution(Resolution r) {
		this.resolution.setTo(r);
		defineX(this.x);
		defineY(this.y);
		defineW(this.width);
		defineH(this.height);
	}

	/** Get idc (control id for arma) */
	public int getIdc() {
		return idc;
	}

	@Override
	protected void updateProperties() {
		setPositionWH(xProperty.getFloatValue(), yProperty.getFloatValue(), wProperty.getFloatValue(), hProperty.getFloatValue());
		//		defineStyle(styleProperty.);
	}

	public ControlType getType() {
		return type;
	}

	public ControlStyle getStyle() {
		return style;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public ArmaControlRenderer getRenderer() {
		return renderer;
	}

	/** Get the text used for render. */
	public String getRenderText() {
		return renderer.getText();
	}


	/** Set the x and y values (and width and height) based upon the renderer's position */
	protected void calcPositionFromRenderer() {
		this.x = PositionCalculator.getPercentX(this.resolution, renderer.getX1());
		this.y = PositionCalculator.getPercentY(this.resolution, renderer.getY1());
		xProperty.setValueSilent(x);
		yProperty.setValueSilent(y);

		this.width = PositionCalculator.getPercentWidth(this.resolution, renderer.getWidth());
		this.height = PositionCalculator.getPercentHeight(this.resolution, renderer.getHeight());
		wProperty.setValueSilent(width);
		hProperty.setValueSilent(height);

		getControlListener().updateValue(null); //don't execute updateProperties
	}

	public ControlProperty[] getEventProperties() {
		return ControlProperty.EMPTY;
	}
}
