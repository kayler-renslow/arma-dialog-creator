package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.util.screen.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.PositionCalculator;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 The base class for all controls
 Created on 05/20/2016. */
public class ArmaControl extends ControlClass {
	/** Resolution of the control. Should not change the reference, but rather change the values inside the resolution. */
	protected final ArmaResolution resolution;
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
	 */
	public ArmaControl(@NotNull String name, @NotNull ArmaControlSpecProvider provider, @NotNull ArmaResolution resolution, @NotNull Class<? extends ArmaControlRenderer> renderer) {
		super(name, provider);
		this.resolution = resolution;
		try {
			this.renderer = renderer.newInstance();
			this.renderer.setMyControl(this);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException("Class " + renderer.getName() + " couldn't be instantiated.");
		}

		idcProperty = findRequiredProperty(ControlPropertyLookup.IDC);
		typeProperty = findRequiredProperty(ControlPropertyLookup.TYPE);
		typeProperty.setDefaultValue(true, type.typeId);
		styleProperty = findRequiredProperty(ControlPropertyLookup.STYLE);
		styleProperty.setDefaultValue(true, style.styleId);
		xProperty = findRequiredProperty(ControlPropertyLookup.X);
		yProperty = findRequiredProperty(ControlPropertyLookup.Y);
		wProperty = findRequiredProperty(ControlPropertyLookup.W);
		hProperty = findRequiredProperty(ControlPropertyLookup.H);
		accessProperty = findOptionalProperty(ControlPropertyLookup.ACCESS);
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
	 */
	public ArmaControl(@NotNull String name, @NotNull ArmaControlSpecProvider provider, int idc, @NotNull ControlType type, @NotNull ControlStyle style, double x, double y, double width, double height, @NotNull ArmaResolution resolution, @NotNull Class<? extends ArmaControlRenderer> renderer) {
		this(name, provider, resolution, renderer);
		idcProperty.setDefaultValue(false, idc);
		typeProperty.setDefaultValue(false, type.typeId);
		styleProperty.setDefaultValue(false, style.styleId);
		xProperty.setDefaultValue(false, x);
		yProperty.setDefaultValue(false, y);
		wProperty.setDefaultValue(false, width);
		hProperty.setDefaultValue(false, height);

		defineType(type);
		defineIdc(idc);
		defineStyle(style);
		defineX(x);
		defineY(y);
		defineW(width);
		defineH(height);
	}

	/** Set x and define the x control property. This will also update the renderer's position. */
	public void defineX(double x) {
		xProperty.setValue(x);
		setX(x);
	}

	/** Just set x position without updating the property. This will also update the renderer's position. */
	protected void setX(double x) {
		this.x = ControlProperty.truncate(x);
		renderer.setX1Silent(calcScreenX(x));
	}

	protected int calcScreenX(double percentX) {
		return PositionCalculator.getScreenX(resolution, percentX);
	}

	/** Set y and define the y control property. This will also update the renderer's position. */
	public void defineY(double y) {
		yProperty.setValue(y);
		setY(y);
	}

	/** Just set the y position without updating the y property. This will also update the renderer's position. */
	protected void setY(double y) {
		this.y = ControlProperty.truncate(y);
		renderer.setY1Silent(calcScreenY(y));
	}

	protected int calcScreenY(double percentY) {
		return PositionCalculator.getScreenY(resolution, percentY);
	}

	/** Set w (width) and define the w control property. This will also update the renderer's position. */
	public void defineW(double width) {
		wProperty.setValue(width);
		setW(width);
	}

	/** Set the width without updating it's control property. This will also update the renderer's position. */
	protected void setW(double width) {
		this.width = ControlProperty.truncate(width);
		int w = calcScreenWidth(width);
		renderer.setX2Silent(renderer.getX1() + w);
	}

	protected int calcScreenWidth(double percentWidth) {
		return PositionCalculator.getScreenWidth(resolution, percentWidth);
	}

	/** Set h (height) and define the h control property. This will also update the renderer's position. */
	public void defineH(double height) {
		hProperty.setValue(height);
		setH(height);
	}

	/** Just set height without setting control property. This will also update the renderer's position. */
	protected void setH(double height) {
		this.height = ControlProperty.truncate(height);
		int h = calcScreenHeight(height);
		renderer.setY2Silent(renderer.getY1() + h);
	}

	/** Set the x,y,w,h properties. This will also update the renderer's position. */
	protected void setPositionWH(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		renderer.setPositionWHSilent(calcScreenX(x), calcScreenY(y), calcScreenWidth(w), calcScreenHeight(h));
	}

	protected int calcScreenHeight(double percentHeight) {
		return PositionCalculator.getScreenHeight(resolution, percentHeight);
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
	public void updateResolution(ArmaResolution r) {
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

		getUpdateGroup().update(null); //don't execute updateProperties
	}

}
