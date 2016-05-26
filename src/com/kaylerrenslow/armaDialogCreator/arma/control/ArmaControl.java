package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.util.screen.PositionCalculator;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.Resolution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 @author Kayler
 The base class for all controls
 Created on 05/20/2016. */
public class ArmaControl extends ArmaControlClass {
	protected final Resolution resolution;
	protected ControlType type = ControlType.STATIC;
	protected ControlStyle style = ControlStyle.CENTER;
	protected ArmaControlRenderer renderer;
	protected double x, y, width, height;
	protected int idc = -1;

	private final ControlProperty idcProperty, typeProperty, styleProperty, xProperty, yProperty, wProperty, hProperty, accessProperty;

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
		idcProperty = ControlPropertiesLookup.IDC.getIntProperty(idc);
		typeProperty = ControlPropertiesLookup.TYPE.getIntProperty(idc);
		styleProperty = ControlPropertiesLookup.STYLE.getIntProperty(idc);
		xProperty = ControlPropertiesLookup.X.getFloatProperty(x);
		yProperty = ControlPropertiesLookup.Y.getFloatProperty(y);
		wProperty = ControlPropertiesLookup.W.getFloatProperty(width);
		hProperty = ControlPropertiesLookup.H.getFloatProperty(height);
		accessProperty = ControlPropertiesLookup.ACCESS.getProperty("0");
		addRequiredProperties(idcProperty, typeProperty, styleProperty, xProperty, yProperty, wProperty, hProperty);
		addOptionalProperties(accessProperty);
		//do not define properties x,y,w,h,idc,type,style here so that they are marked as missed when checking what requirements have been filled
	}

	public ArmaControl(@NotNull String name, int idc, @NotNull ControlType type, @NotNull ControlStyle style, double x, double y, double width, double height, @NotNull Resolution resolution, @NotNull Class<? extends ArmaControlRenderer> renderer, @Nullable ArmaControlClass[] requiredSubClasses, @Nullable ArmaControlClass[] optionalSubClasses) {
		this(name, resolution, renderer, requiredSubClasses, optionalSubClasses);
		setType(type);
		setIdc(idc);
		setStyle(style);
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}

	public void setX(double x) {
		this.x = x;
		xProperty.setValue(x);
		defineProperty(xProperty);
		renderer.setX1(PositionCalculator.getScreenX(resolution, x));
	}

	public void setY(double y) {
		this.y = y;
		yProperty.setValue(y);
		defineProperty(yProperty);
		renderer.setY1(PositionCalculator.getScreenY(resolution, y));
	}

	public void setWidth(double width) {
		this.width = width;
		wProperty.setValue(width);
		defineProperty(wProperty);
		int w = PositionCalculator.getScreenWidth(resolution, width);
		renderer.setX2(renderer.getX1() + w);
	}

	public void setHeight(double height) {
		this.height = height;
		hProperty.setValue(height);
		defineProperty(hProperty);
		int h = PositionCalculator.getScreenHeight(resolution, height);
		renderer.setY2(renderer.getY1() + h);
	}

	public void setIdc(int idc) {
		this.idc = idc;
		idcProperty.setValue(idc);
		defineProperty(idcProperty);
	}

	protected void setType(ControlType type) {
		this.type = type;
		defineProperty(typeProperty);
	}

	public void setStyle(ControlStyle style) {
		this.style = style;
		defineProperty(styleProperty);
	}

	public void setAccess(int access) {
		this.accessProperty.setValue(access);
	}

	public void updateResolution(Resolution r) {
		this.resolution.setTo(r);
		setX(this.x);
		setY(this.y);
		setWidth(this.width);
		setHeight(this.height);
	}

	public int getIdc() {
		return idc;
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

	protected void calcPositionFromRenderer() {
		this.x = PositionCalculator.getPercentX(this.resolution, renderer.getX1());
		this.y = PositionCalculator.getPercentY(this.resolution, renderer.getY1());
		xProperty.setValue(x);
		yProperty.setValue(y);
	}

	protected void calcSizeFromRenderer() {
		this.width = PositionCalculator.getPercentWidth(this.resolution, renderer.getWidth());
		this.height = PositionCalculator.getPercentHeight(this.resolution, renderer.getHeight());
		wProperty.setValue(width);
		hProperty.setValue(height);
	}
}
