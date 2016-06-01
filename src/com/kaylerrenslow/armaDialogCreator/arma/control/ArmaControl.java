package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.util.screen.PositionCalculator;
import com.kaylerrenslow.armaDialogCreator.arma.util.screen.Resolution;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	private final ValueObserver<ArmaControl> myselfListener = new ValueObserver<>(this);

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

		myselfListener.addValueListener(new ValueListener<ArmaControl>() {
			@Override
			public void valueUpdated(ArmaControl oldValue, ArmaControl newValue) {
				updateProperties();
			}
		});
	}

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

	public void defineX(double x) {
		xProperty.setValue(x);
		defineProperty(xProperty);
		setX(x);
	}

	protected void setX(double x) {
		this.x = x;
		renderer.setX1(PositionCalculator.getScreenX(resolution, x));
	}

	public void defineY(double y) {
		yProperty.setValue(y);
		defineProperty(yProperty);
		setY(y);
	}

	protected void setY(double y) {
		this.y = y;
		renderer.setY1(PositionCalculator.getScreenY(resolution, y));
	}

	public void defineW(double width) {
		wProperty.setValue(width);
		defineProperty(wProperty);
		setW(width);
	}

	protected void setW(double width) {
		this.width = width;
		int w = PositionCalculator.getScreenWidth(resolution, width);
		renderer.setX2(renderer.getX1() + w);
	}

	public void defineH(double height) {
		hProperty.setValue(height);
		defineProperty(hProperty);
		setH(height);
	}

	protected void setH(double height) {
		this.height = height;
		int h = PositionCalculator.getScreenHeight(resolution, height);
		renderer.setY2(renderer.getY1() + h);
	}

	public void defineIdc(int idc) {
		setIdc(idc);
		idcProperty.setValue(idc);
		defineProperty(idcProperty);
	}

	protected void setIdc(int idc) {
		this.idc = idc;
	}

	protected void defineType(ControlType type) {
		setType(type);
		defineProperty(typeProperty);
	}

	protected void setType(ControlType type) {
		this.type = type;
	}

	protected void defineStyle(ControlStyle style) {
		setStyle(style);
		defineProperty(styleProperty);
	}

	protected void setStyle(ControlStyle style) {
		this.style = style;
	}

	public void defineAccess(int access) {
		this.accessProperty.setValue(access);
		defineProperty(accessProperty);
	}

	public void updateResolution(Resolution r) {
		this.resolution.setTo(r);
		defineX(this.x);
		defineY(this.y);
		defineW(this.width);
		defineH(this.height);
	}

	public int getIdc() {
		return idc;
	}

	/** Called when myselfObserver has be notified of an update*/
	protected void updateProperties() {
		setX(xProperty.getFloatValue());
		setY(yProperty.getFloatValue());
		setW(wProperty.getFloatValue());
		setH(hProperty.getFloatValue());
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

	/**
	 Get the listener that listens to this object. Instead of adding listeners to all properties, anytime a control property is changed inside this control this returned observer should be notified from where it was changed.<br>
	 Also, since it will not automatically change, it will cut down on the number of renders performed by the editor's canvas<br>
	 The value inside the listener <b>SHOULD NOT CHANGE</b> as that would be expensive to constantly recreate the control.
	 */
	public ValueObserver<ArmaControl> getControlListener() {
		return myselfListener;
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
