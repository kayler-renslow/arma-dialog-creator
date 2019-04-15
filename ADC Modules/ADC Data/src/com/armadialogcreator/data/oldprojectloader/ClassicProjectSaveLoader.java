package com.armadialogcreator.data.oldprojectloader;

import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.data.ConfigClassRegistry;
import com.armadialogcreator.data.ExpressionEnvManager;
import com.armadialogcreator.data.SerializableValueConfigurable;
import com.armadialogcreator.expression.Env;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 4/11/19 */
public class ClassicProjectSaveLoader {
	private final Configurable root;

	public ClassicProjectSaveLoader(@NotNull Configurable root) {
		this.root = root;
	}

	private void loadDisplay() {
		Configurable rtDisplayConf = root.getConfigurable("display");
		if (rtDisplayConf == null) {
			return;
		}
		Configurable displayConf = new Configurable.Simple("display");
		Configurable bgControlsConf, mainControlsConf, ccDisplayConf;
		{
			Configurable controlsConf = new Configurable.Simple("controls");
			displayConf.addNestedConfigurable(controlsConf);

			bgControlsConf = new Configurable.Simple("background");
			mainControlsConf = new Configurable.Simple("main");
			controlsConf.addNestedConfigurable(bgControlsConf);
			controlsConf.addNestedConfigurable(mainControlsConf);
		}
		{
			ccDisplayConf = new Configurable.Simple("properties");
			displayConf.addNestedConfigurable(ccDisplayConf);
		}

		ConfigClassRegistry.ProjectClasses projectClasses = ConfigClassRegistry.instance.getProjectClasses();

		for (Configurable nested : rtDisplayConf.getNestedConfigurables()) {
			switch (nested.getConfigurableName()) {
				case "display-property": {
					String sid = nested.getAttributeValue("id");
					if (sid == null) {
						// todo
						continue;
					}
					int id;
					try {
						id = Integer.parseInt(sid);
					} catch (NumberFormatException e) {
						// todo
						continue;
					}

					break;
				}
				case "display-controls": {
					String type = nested.getAttributeValue("type");
					if (type == null) {
						continue;
					}
					Configurable addTo;
					if (type.equals("background")) {
						addTo = bgControlsConf;
					} else {
						addTo = mainControlsConf;
					}
					for (Configurable controlConf : nested.getNestedConfigurables()) {
						if (controlConf.getConfigurableName().equals("control")) {
							Configurable c = convertControl(controlConf);
							if (c == null) {
								continue;
							}
							projectClasses.loadFromConfigurable(c.getConfigurable("config-class"));
							addTo.addNestedConfigurable(c.getConfigurable("UINode"));
						}
					}
					break;
				}
				default: {
					// todo
				}
			}
		}
	}

	@Nullable
	private Configurable convertControl(@NotNull Configurable c) {
		Configurable ret = new Configurable.Simple("");
		Configurable ccConf = new Configurable.Simple("config-class");
		ret.addNestedConfigurable(ccConf);
		Configurable uinodeConf = new Configurable.Simple("UINode");
		String className = c.getAttributeValue("class-name");
		if (className == null) {
			return null;
		}
		ccConf.addAttribute("name", className);
		String extendClass = c.getAttributeValue("extend-class");
		if (extendClass != null) {
			ccConf.addAttribute("extend", extendClass);
		}
		String controlId = c.getAttributeValue("control-id");
		if (controlId == null) {
			return null;
		}
		ControlType type;
		try {
			type = ControlType.findById(Integer.parseInt(controlId));
		} catch (NumberFormatException e) {
			return null;
		}
		ccConf.addAttribute("control-type", type.getTypeId() + "");
		Env env = ExpressionEnvManager.instance.getEnv();
		for (Configurable nested : c.getNestedConfigurables()) {
			if (nested.getConfigurableName().equals("property")) {
				Configurable propertyConf = new Configurable.Simple("property");
				{ //set property name and value
					String pid = nested.getAttributeValue("id");
					if (pid == null) {
						continue;
					}
					ConfigPropertyLookup propertyLookup = ConfigPropertyLookup.findById(Integer.parseInt(pid));
					propertyConf.addAttribute("name", propertyLookup.getPropertyName());
					propertyConf.addAttribute("priority", propertyLookup.priority() + "");

					{ //set property value configurable
						String ptype = c.getAttributeValue("ptype");
						PropertyType propertyType;
						if (ptype == null) {
							propertyType = propertyLookup.getPropertyType();
						} else {
							propertyType = PropertyType.findById(Integer.parseInt(ptype));
						}
						String[] vals = new String[nested.getNestedConfigurableCount()];
						int i = 0;
						for (Configurable vConfg : nested.getNestedConfigurables()) {
							vals[i++] = vConfg.getConfigurableBody();
						}
						SerializableValue sv = SerializableValue.constructNew(env, propertyType, vals);
						propertyConf.addNestedConfigurable(new SerializableValueConfigurable(sv));
					}
				}
				{ //set macro
					String macroName = nested.getAttributeValue("macro-key");
					if (macroName != null) {
						propertyConf.addAttribute("macro", macroName);
					}
				}
			}
			{ //set extend class
				String extend = c.getAttributeValue("extend-class");
				if (extend != null) {
					ccConf.addAttribute("extend", extend);
				}
			}
			{
				uinodeConf.addAttribute("UINodeName", className);
				{
					boolean ghost, enabled;
					{
						String ghostAtt = c.getAttributeValue("ghost");
						String enabledAtt = c.getAttributeValue("enabled");
						if (ghostAtt != null) {
							ghost = ghostAtt.equals("t");
						} else {
							ghost = false;
						}
						if (enabledAtt != null) {
							enabled = enabledAtt.equals("t");
						} else {
							enabled = true;
						}
					}

					Configurable componentConf = new Configurable.Simple("component");
					componentConf.addAttribute("ghost", ghost);
					componentConf.addAttribute("enabled", enabled);
				}
			}
		}
		return ret;
	}
}
