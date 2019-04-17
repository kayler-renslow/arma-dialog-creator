package com.armadialogcreator.data;

import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.core.*;
import com.armadialogcreator.core.sv.SerializableValue;
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

	public void load() {
		loadDisplay();
	}

	private void loadDisplay() {
		Configurable rtDisplayConf = root.getConfigurable("display");
		if (rtDisplayConf == null) {
			return;
		}
		Configurable displayConf = new Configurable.Simple("display");
		Configurable bgControlsConf, mainControlsConf, ccDisplayConf, configClassesConf;
		{
			Configurable controlsConf = new Configurable.Simple("controls");
			displayConf.addNestedConfigurable(controlsConf);

			configClassesConf = new Configurable.Simple("");

			bgControlsConf = new Configurable.Simple("background");
			mainControlsConf = new Configurable.Simple("main");
			controlsConf.addNestedConfigurable(bgControlsConf);
			controlsConf.addNestedConfigurable(mainControlsConf);
		}
		{
			ccDisplayConf = new Configurable.Simple("config-class");
			configClassesConf.addNestedConfigurable(ccDisplayConf);

			//root.getConfigurable("export-configuration")
		}

		Env env = ExpressionEnvManager.instance.getEnv();
		for (Configurable nestedDispConf : rtDisplayConf.getNestedConfigurables()) {
			switch (nestedDispConf.getConfigurableName()) {
				case "display-property": {
					String sid = nestedDispConf.getAttributeValue("id");
					if (sid == null) {
						continue;
					}
					DisplayPropertyLookup dispLookup = DisplayPropertyLookup.findById(Integer.parseInt(sid));
					Configurable newDispPropConf = convertPropertyConf(nestedDispConf, dispLookup, env);
					ccDisplayConf.addNestedConfigurable(newDispPropConf);
					break;
				}
				case "display-controls": {
					String type = nestedDispConf.getAttributeValue("type");
					if (type == null) {
						continue;
					}
					Configurable addTo;
					if (type.equals("background")) {
						addTo = bgControlsConf;
					} else {
						addTo = mainControlsConf;
					}
					for (Configurable ctrlConf : nestedDispConf.getNestedConfigurables()) {
						if (!ctrlConf.getConfigurableName().equals("control")) {
							continue;
						}
						Configurable c = convertControl(ctrlConf);
						if (c == null) {
							continue;
						}

						ccDisplayConf.addNestedConfigurable(c.getConfigurable("config-class"));
						addTo.addNestedConfigurable(c.getConfigurable("UINode"));
					}
					break;
				}
			}
			ConfigClassRegistry.instance.getProjectClasses().loadFromConfigurable(configClassesConf);
			EditorManager.instance.loadDisplayFromConfigurable(displayConf);
		}
	}

	@Nullable
	private Configurable convertControl(@NotNull Configurable ctrlConf) {
		Configurable ret = new Configurable.Simple("");
		Configurable ccConf = new Configurable.Simple("config-class");
		ret.addNestedConfigurable(ccConf);
		Configurable uinodeConf = new Configurable.Simple("UINode");
		ret.addNestedConfigurable(uinodeConf);
		String className = ctrlConf.getAttributeValue("class-name");
		if (className == null) {
			return null;
		}
		ccConf.addAttribute("name", className);
		{ //set extend class
			String extend = ctrlConf.getAttributeValue("extend-class");
			if (extend != null) {
				ccConf.addAttribute("extend", extend);
			}
		}
		String controlId = ctrlConf.getAttributeValue("control-id");
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
		for (Configurable ctrlPropConf : ctrlConf.getNestedConfigurables()) {
			if (ctrlPropConf.getConfigurableName().equals("property")) {
				String pid = ctrlPropConf.getAttributeValue("id");
				if (pid == null) {
					continue;
				}
				ConfigPropertyLookup propertyLookup = ConfigPropertyLookup.findById(Integer.parseInt(pid));
				Configurable propertyConf = convertPropertyConf(ctrlPropConf, propertyLookup, env);
				{ //set macro
					String macroName = ctrlPropConf.getAttributeValue("macro-key");
					if (macroName != null) {
						propertyConf.addAttribute("macro", macroName);
					}
				}
				ccConf.addNestedConfigurable(propertyConf);
			}
			{
				uinodeConf.addAttribute("UINodeName", className);
				{
					boolean ghost, enabled;
					{
						String ghostAtt = ctrlConf.getAttributeValue("ghost");
						String enabledAtt = ctrlConf.getAttributeValue("enabled");
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

	@NotNull
	private Configurable convertPropertyConf(@NotNull Configurable propConf, @NotNull ConfigPropertyLookupConstant c, @NotNull Env env) {
		Configurable retPropertyConf = new Configurable.Simple("property");
		{ //set property name and value

			retPropertyConf.addAttribute("name", c.getPropertyName());
			retPropertyConf.addAttribute("priority", c.priority() + "");

			{ //set property value configurable
				String ptype = propConf.getAttributeValue("ptype");
				PropertyType propertyType;
				if (ptype == null) {
					propertyType = c.getPropertyType();
				} else {
					propertyType = PropertyType.findById(Integer.parseInt(ptype));
				}
				String[] vals = new String[propConf.getNestedConfigurableCount()];
				int i = 0;
				for (Configurable vConfg : propConf.getNestedConfigurables()) {
					vals[i++] = vConfg.getConfigurableBody();
				}
				SerializableValue sv = SerializableValue.constructNew(env, propertyType, vals);
				retPropertyConf.addNestedConfigurable(new SerializableValueConfigurable(sv));
			}
		}
		return retPropertyConf;
	}
}
