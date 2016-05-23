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
public abstract class ArmaControl {
	protected final ControlType myType;
	protected final ArmaControlRenderer renderer;
	protected final Resolution resolution;
	private final ControlStyle style;
	private final ControlProperty idcProperty, styleProperty, xProperty, yProperty, wProperty, hProperty;

	protected double x, y, width, height;
	protected int idc;

	private ArmaControl extend;

	private final ArrayList<ControlProperty> requiredProperties = new ArrayList<>();
	private final ArrayList<ControlProperty> optionalProperties = new ArrayList<>();
	private final ArrayList<ControlProperty> definedProperties = new ArrayList<>();

	public ArmaControl(ControlType type, int idc, ControlStyle style, double x, double y, double width, double height, Resolution resolution, ArmaControlRenderer renderer) {
		this.myType = type;
		this.renderer = renderer;
		this.resolution = resolution;
		this.idc = idc;
		this.style = style;
		idcProperty = new ControlProperty("idc", idc);
		styleProperty = new ControlProperty("style", style.styleId);
		xProperty = new ControlProperty("x", x);
		yProperty = new ControlProperty("y", y);
		wProperty = new ControlProperty("w", width);
		hProperty = new ControlProperty("h", height);
		definedProperties.add(idcProperty);
		definedProperties.add(styleProperty);
		definedProperties.add(xProperty);
		definedProperties.add(yProperty);
		definedProperties.add(wProperty);
		definedProperties.add(hProperty);

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public final void extend(@Nullable ArmaControl armaControl) {
		this.extend = armaControl;
	}

	public void setX(double x) {
		this.x = x;
		xProperty.setValues(x);
		renderer.setX1(PositionCalculator.getScreenX(resolution, x));
	}

	public void setY(double y) {
		this.y = y;
		yProperty.setValues(y);
		renderer.setY1(PositionCalculator.getScreenY(resolution, y));
	}

	public void setWidth(double width) {
		this.width = width;
		wProperty.setValues(width);
		int w = PositionCalculator.getScreenWidth(resolution, width);
		renderer.setX2(renderer.getX1() + w);
	}

	public void setHeight(double height) {
		this.height = height;
		hProperty.setValues(height);
		int h = PositionCalculator.getScreenHeight(resolution, height);
		renderer.setY2(renderer.getY1() + h);
	}

	public void updateResolution(Resolution r) {
		this.resolution.setTo(r);
		setX(this.x);
		setY(this.y);
		setWidth(this.width);
		setHeight(this.height);
	}

	public void setIdc(int idc) {
		this.idc = idc;
		idcProperty.setValues(idc);
	}

	public int getIdc() {
		return idc;
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

	public final ArmaControl getExtendControl() {
		return extend;
	}

	public final ControlProperty[] getMissingRequiredProperties() {
		ArrayList<ControlProperty> missing = new ArrayList<>();
		ControlProperty[] defined = getDefinedProperties();

		boolean found = false;
		for (ControlProperty req : requiredProperties) {
			found = false;
			for (ControlProperty d : defined) {
				if (req.equals(d)) {
					found = true;
					break;
				}
			}
			if (!found) {
				missing.add(req);
			}
		}
		return missing.toArray(new ControlProperty[missing.size()]);
	}

	public final void addDefinedProperty(ControlProperty c) {
		if (!definedProperties.contains(c)) {
			definedProperties.add(c);
		}
	}

	public final void removeDefinedProperty(ControlProperty c) {
		definedProperties.remove(c);
	}

	protected final void addRequiredProperty(ControlProperty c) {
		if (!requiredProperties.contains(c)) {
			requiredProperties.add(c);
		}
	}

	protected final void addOptionalProperty(ControlProperty c) {
		if (!optionalProperties.contains(c)) {
			optionalProperties.add(c);
		}
	}

	@NotNull
	public final ControlProperty[] getRequiredProperties() {
		return requiredProperties.toArray(new ControlProperty[definedProperties.size()]);
	}

	@NotNull
	public final ControlProperty[] getOptionalProperties() {
		return optionalProperties.toArray(new ControlProperty[definedProperties.size()]);
	}

	@NotNull
	public final ControlProperty[] getDefinedProperties() {
		boolean hasInherited = getInheritedProperties() != null;
		int inheritedNum = (hasInherited ? getInheritedProperties().length : 0);
		ControlProperty[] arr = new ControlProperty[inheritedNum + definedProperties.size()];
		int i = 0;
		if (hasInherited) {
			for (ControlProperty inherit : getInheritedProperties()) {
				arr[i++] = inherit;
			}
		}
		for (ControlProperty defined : definedProperties) {
			arr[i++] = defined;
		}
		return arr;
	}

	@Nullable
	private ControlProperty[] getInheritedProperties() {
		if (extend == null) {
			return null;
		}
		ArrayList<ControlProperty> list = new ArrayList<>();
		appendInheritedProperties(extend, list);
		return list.toArray(new ControlProperty[list.size()]);
	}

	private void appendInheritedProperties(@NotNull ArmaControl extend, ArrayList<ControlProperty> list) {
		for (ControlProperty c : extend.getDefinedProperties()) {
			list.add(c);
		}
		if (extend.extend != null) {
			if (extend.extend.getInheritedProperties() != null) {
				for (ControlProperty c : extend.extend.getInheritedProperties()) {
					list.add(c);
				}
			}
			appendInheritedProperties(extend.extend, list);
		}
	}

}
