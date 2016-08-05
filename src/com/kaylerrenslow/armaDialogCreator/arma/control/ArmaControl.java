package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.control.impl.RendererLookup;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.arma.util.PositionCalculator;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListener;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 The base class for all controls.<br>
 <b>NOTE: any classes that extend this class are SHORT-HAND ways of creating this class. Never check if an {@link ArmaControl} instance is an instanceof some extended class because when the project is loaded from .xml
 via {@link com.kaylerrenslow.armaDialogCreator.data.io.xml.ProjectXmlLoader}, all controls are only {@link ArmaControl} or {@link ArmaControlGroup} and
 <b>not</b> something like {@link com.kaylerrenslow.armaDialogCreator.arma.control.impl.StaticControl}</b>
 Created on 05/20/2016. */
public class ArmaControl extends ControlClass {
	/** Resolution of the control. Should not change the reference, but rather change the values inside the resolution. */
	protected final ArmaResolution resolution;
	private final Env env;
	private final RendererLookup rendererLookup;
	/** Type of the control */
	protected ControlType type = ControlType.STATIC;
	/** Style of the control TODO: allow multiple styles */
	protected ControlStyle style = ControlStyle.CENTER;
	/** Renderer of the control for the canvas */
	protected ArmaControlRenderer renderer;
	
	protected Expression x, y, width, height;
	
	/** Control id (-1 if doesn't matter) */
	protected int idc = -1;
	
	private final ControlProperty idcProperty, typeProperty, styleProperty, xProperty, yProperty, wProperty, hProperty, accessProperty;
	
	/**
	 Create a control where the position is to be determined
	 
	 @param name control class name (e.g. RscText or OMGClass). Keep in mind that it should follow normal Identifier rules (letter letterOrDigit*)
	 @param resolution resolution to use
	 @param rendererLookup renderer of the control
	 @param env the environment used to calculate the control's position and other {@link Expression} instances stored inside this control's {@link ControlProperty}'s.
	 */
	public ArmaControl(@NotNull String name, @NotNull ArmaControlSpecProvider provider, @NotNull ArmaResolution resolution, @NotNull RendererLookup rendererLookup, @NotNull Env env) {
		super(name, provider);
		this.resolution = resolution;
		resolution.getUpdateGroup().addListener(new UpdateListener<ArmaResolution>() {
			@Override
			public void update(ArmaResolution data) {
				resolutionUpdated();
			}
		});
		this.env = env;
		try {
			this.renderer = rendererLookup.rendererClass.newInstance();
			this.renderer.setMyControl(this);
			this.renderer.init();
			this.rendererLookup = rendererLookup;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException("Class " + rendererLookup.rendererClass.getName() + " couldn't be instantiated.");
		}
		
		idcProperty = findRequiredProperty(ControlPropertyLookup.IDC);
		idcProperty.setDefaultValue(true, -1);
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
	 @param rendererLookup renderer for the control
	 @param env the environment used to calculate the control's position and other {@link Expression} instances stored inside this control's {@link ControlProperty}'s.
	 */
	public ArmaControl(@NotNull String name, @NotNull ArmaControlSpecProvider provider, int idc, @NotNull ControlType type, @NotNull ControlStyle style,
					   @NotNull Expression x, @NotNull Expression y, @NotNull Expression width, @NotNull Expression height, @NotNull ArmaResolution resolution,
					   @NotNull RendererLookup rendererLookup, @NotNull Env env) {
		this(name, provider, resolution, rendererLookup, env);
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
	public void defineX(Expression x) {
		xProperty.setValue(x);
		setX(x);
	}
	
	/** Just set x position without updating the property. This will also update the renderer's position. */
	protected void setX(Expression x) {
		this.x = x;
		renderer.setX1Silent(calcScreenX(x.getNumVal().v()));
	}
	
	protected int calcScreenX(double percentX) {
		return PositionCalculator.getScreenX(resolution, percentX);
	}
	
	/** Set y and define the y control property. This will also update the renderer's position. */
	public void defineY(Expression y) {
		yProperty.setValue(y);
		setY(y);
	}
	
	/** Just set the y position without updating the y property. This will also update the renderer's position. */
	protected void setY(Expression y) {
		this.y = y;
		renderer.setY1Silent(calcScreenY(y.getNumVal().v()));
	}
	
	protected int calcScreenY(double percentY) {
		return PositionCalculator.getScreenY(resolution, percentY);
	}
	
	/** Set w (width) and define the w control property. This will also update the renderer's position. */
	public void defineW(Expression width) {
		wProperty.setValue(width);
		setW(width);
	}
	
	/** Set the width without updating it's control property. This will also update the renderer's position. */
	protected void setW(Expression width) {
		this.width = width;
		int w = calcScreenWidth(width.getNumVal().v());
		renderer.setX2Silent(renderer.getX1() + w);
	}
	
	protected int calcScreenWidth(double percentWidth) {
		return PositionCalculator.getScreenWidth(resolution, percentWidth);
	}
	
	/** Set h (height) and define the h control property. This will also update the renderer's position. */
	public void defineH(Expression height) {
		hProperty.setValue(height);
		setH(height);
	}
	
	/** Just set height without setting control property. This will also update the renderer's position. */
	protected void setH(Expression height) {
		this.height = height;
		int h = calcScreenHeight(height.getNumVal().v());
		renderer.setY2Silent(renderer.getY1() + h);
	}
	
	/** Set the x,y,w,h properties. This will also update the renderer's position. If x, y, w, or h are null, this method will do nothing. */
	protected void setPositionWH(Expression x, Expression y, Expression w, Expression h) {
		//do not use @NotNull annotations for parameters because this method is called from updateProperties. updateProperties is only invoked when a ControlProperty is edited.
		//when a ControlProperty is edited, required (like x,y,w,h), and has no input, the user should not be allowed to exit editing until valid input is entered
		if (x == null || y == null || w == null || h == null) {
			return;
		}
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		renderer.setPositionWHSilent(calcScreenX(x.getNumVal().v()), calcScreenY(y.getNumVal().v()), calcScreenWidth(w.getNumVal().v()), calcScreenHeight(h.getNumVal().v()));
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
	
	private void resolutionUpdated() {
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
		if (xProperty.getValue() != this.x || yProperty.getValue() != this.y || wProperty.getValue() != this.width || hProperty.getValue() != this.height) {
			setPositionWH((Expression) xProperty.getValue(), (Expression) yProperty.getValue(), (Expression) wProperty.getValue(), (Expression) hProperty.getValue());
		}
		renderer.updateProperties();
		//		defineStyle(styleProperty.);
	}
	
	public ControlType getType() {
		return type;
	}
	
	public ControlStyle getStyle() {
		return style;
	}
	
	public ArmaControlRenderer getRenderer() {
		return renderer;
	}
	
	public RendererLookup getRendererLookup() {
		return rendererLookup;
	}
	
	/** Set the x and y values (and width and height) based upon the renderer's position */
	protected void calcPositionFromRenderer() {
		this.x = new Expression(PositionCalculator.getSafeZoneExpressionX(this.resolution, renderer.getX1()), env);
		this.y = new Expression(PositionCalculator.getSafeZoneExpressionY(this.resolution, renderer.getY1()), env);
		xProperty.setValue(x);
		yProperty.setValue(y);
		
		this.width = new Expression(PositionCalculator.getSafeZoneExpressionW(this.resolution, renderer.getWidth()), env);
		this.height = new Expression(PositionCalculator.getSafeZoneExpressionH(this.resolution, renderer.getHeight()), env);
		wProperty.setValue(width);
		hProperty.setValue(height);
		
		getUpdateGroup().update(null); //don't execute updateProperties
	}
	
}
