package com.armadialogcreator.data;

import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.core.PropertyType;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.expression.Env;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 @author K
 @since 4/17/19 */
public class ClassicSaveLoaderUtil {
	@NotNull
	public static Configurable convertCustomControlClassConf(@NotNull Configurable cccConf, @NotNull Env env) {
		Configurable retConfigClass = new Configurable.Simple("config-class");
		/* CLASSIC FORMAT
		<custom-control>
			<class-spec ...
			</..
			<comment>...
		*/
		Configurable classSpecConf = cccConf.getConfigurableNotNull("class-spec");
		convertClassSpecConf(classSpecConf, retConfigClass, env);
		Configurable commentConf = cccConf.getConfigurable("comment");
		if (commentConf != null) {
			retConfigClass.addNestedConfigurable(commentConf);
		}

		return retConfigClass;
	}

	private static void convertClassSpecConf(@NotNull Configurable classSpecConf, @NotNull Configurable configClassConf,
											 @NotNull Env env) {
		/* CLASSIC FORMAT
		<class-spec name="RscStructuredText">
			<optional-properties>
				<property ...> ...
			</..
			<required-properties>
				<property ...> ...
			</..
			<inherit-properties>
				<inherit-property id="7"> ...
			</..
		*/

		Configurable opPropsConf = classSpecConf.getConfigurable("optional-properties");
		Configurable reqPropsConf = classSpecConf.getConfigurable("required-properties");
		Configurable inhPropsConf = classSpecConf.getConfigurable("inherit-properties");
		List<Integer> inheritedProps;
		if (inhPropsConf != null) {
			inheritedProps = new ArrayList<>(inhPropsConf.getNestedConfigurableCount());
			for (Configurable inheritPropConf : inhPropsConf.getNestedConfigurables()) {
				if (!inheritPropConf.getConfigurableName().equals("inherit-property")) {
					continue;
				}
				String id = inheritPropConf.getAttributeValueNotNull("id");
				inheritedProps.add(Integer.parseInt(id));
			}
		} else {
			inheritedProps = Collections.emptyList();
		}
		Consumer<Configurable> collectProperties = propsConf -> {
			if (propsConf == null) {
				return;
			}
			for (Configurable existingPropConf : propsConf.getNestedConfigurables()) {
				if (existingPropConf.getConfigurableName().equals("property")) {
					Configurable convertedPropConf = convertConfigPropertyConf(existingPropConf, env, integer -> {
						return inheritedProps.contains(integer);
					});
					if (convertedPropConf != null) {
						configClassConf.addNestedConfigurable(convertedPropConf);
					}
				}
			}
		};
		collectProperties.accept(opPropsConf);
		collectProperties.accept(reqPropsConf);

		configClassConf.addAttribute("name", classSpecConf.getAttributeValueNotNull("name"));
	}

	@Nullable
	public static Configurable convertConfigPropertyConf(@NotNull Configurable cfgPropConf,
														 @NotNull Env env,
														 @NotNull Function<Integer, Boolean> propertyIsInherited) {
		/*  CLASSIC FORMAT
		<property id="0" ptype="1">
				<v>-1</v>
		</property>
		*/
		// or
		/*  CLASSIC FORMAT
		<property id="0">
				<v>-1</v>
		</property>
		*/
		String pid = cfgPropConf.getAttributeValueNotNull("id");
		int id = Integer.parseInt(pid);
		if (propertyIsInherited.apply(id)) {
			return null;
		}
		ConfigPropertyLookup propertyLookup = ConfigPropertyLookup.findById(id);
		Configurable propertyConf = convertPropertyLookupConf(cfgPropConf, propertyLookup, env);
		{ //set macro
			String macroName = cfgPropConf.getAttributeValue("macro-key");
			if (macroName != null) {
				propertyConf.addAttribute("macro", macroName);
			}
		}
		return propertyConf;
	}

	@NotNull
	public static Configurable convertPropertyLookupConf(@NotNull Configurable lookupPropConf,
														 @NotNull ConfigPropertyLookupConstant c, @NotNull Env env) {
		Configurable retPropertyConf = new Configurable.Simple("property");
		{ //set property name and value

			retPropertyConf.addAttribute("name", c.getPropertyName());
			retPropertyConf.addAttribute("priority", c.priority() + "");

			{ //set property value configurable
				String ptype = lookupPropConf.getAttributeValue("ptype");
				PropertyType propertyType;
				if (ptype == null) {
					propertyType = c.getPropertyType();
				} else {
					propertyType = PropertyType.findById(Integer.parseInt(ptype));
				}
				String[] vals = new String[lookupPropConf.getNestedConfigurableCount()];
				int i = 0;
				for (Configurable vConfg : lookupPropConf.getNestedConfigurables()) {
					vals[i++] = vConfg.getConfigurableBody();
				}
				SerializableValue sv = SerializableValue.constructNew(env, propertyType, vals);
				retPropertyConf.addNestedConfigurable(new SerializableValueConfigurable(sv));
			}
		}
		return retPropertyConf;
	}
}
