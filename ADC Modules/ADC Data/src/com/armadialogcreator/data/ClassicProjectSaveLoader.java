package com.armadialogcreator.data;

import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.core.DisplayPropertyLookup;
import com.armadialogcreator.expression.Env;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
		Configurable bgControlsConf, mainControlsConf, ccDisplayConf, ctrlCfgClassesConf;
		{
			Configurable nodesConf = new Configurable.Simple("nodes");
			displayConf.addNestedConfigurable(nodesConf);

			ctrlCfgClassesConf = new Configurable.Simple("control-config-classes");
			displayConf.addNestedConfigurable(ctrlCfgClassesConf);

			bgControlsConf = new Configurable.Simple("background");
			mainControlsConf = new Configurable.Simple("main");
			nodesConf.addNestedConfigurable(bgControlsConf);
			nodesConf.addNestedConfigurable(mainControlsConf);
		}
		{
			ccDisplayConf = new Configurable.Simple("config-class");
			ccDisplayConf.addAttribute("name", "MyDialog");
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
					Configurable newDispPropConf = ClassicSaveLoaderUtil.convertPropertyLookupConf(nestedDispConf, dispLookup, env);
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

						ctrlCfgClassesConf.addNestedConfigurable(c.getConfigurable("config-class"));
						addTo.addNestedConfigurable(c.getConfigurable("UINode"));
					}
					break;
				}
			}
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
		ccConf.addAttribute("control-type", controlId);
		Env env = ExpressionEnvManager.instance.getEnv();
		{ //load properties

			// Make initial capacity of arraylist nested count / 2 because in worst case, all properties are inherited,
			// which means there would be a <property> and <inherit-property> tag for every property
			// (worst case number of tags = property count * 2).
			List<Integer> inheritedProperties = new ArrayList<>(ctrlConf.getNestedConfigurableCount() / 2);
			for (Configurable ctrlPropConf : ctrlConf.getNestedConfigurables()) {
				if (ctrlPropConf.getConfigurableName().equals("inherit-property")) {
					String pid = ctrlPropConf.getAttributeValue("id");
					if (pid == null) {
						continue;
					}
					inheritedProperties.add(Integer.parseInt(pid));
				}
			}
			for (Configurable ctrlPropConf : ctrlConf.getNestedConfigurables()) {
				if (ctrlPropConf.getConfigurableName().equals("property")) {
					Configurable propertyConf = ClassicSaveLoaderUtil.convertConfigPropertyConf(
							ctrlPropConf, env, integer -> {
								return inheritedProperties.contains(integer);
							}
					);
					if (propertyConf == null) {
						continue;
					}
					ccConf.addNestedConfigurable(propertyConf);
				}
			}
		}
		{ //load UINode
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
		return ret;
	}
}
