package com.armadialogcreator.data.olddata;

import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.core.sv.SerializableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 @author Kayler
 @since 11/22/2016 */
public class ProjectDefaultValueProvider {
	private static final String PREFIX = "/com/armadialogcreator/defaultValues/";
	private static final String DEFAULT = PREFIX + "_FallbackDefaultPropertyValues.xml";

//	private ApplicationData data;
	private final Map<String, SerializableValue> valuesMap = new HashMap<>();
//
//	public ProjectDefaultValueProvider(@NotNull ApplicationData data) {
//		this.data = data;
//	}

	@Nullable
	public SerializableValue getDefaultValue(@NotNull ConfigPropertyLookupConstant lookup) {
		return valuesMap.get(lookup.getPropertyName());
	}
/*
	public void prefetchValues(@NotNull List<ConfigPropertyLookupConstant> tofetch, @Nullable Context context) {
//		try {
//			String path;
//			if (context != null) {
//				path = PREFIX + buildPath(context);
//			} else {
//				path = DEFAULT;
//			}
//
//			InputStream is = getClass().getResourceAsStream(path);
//
//			if (is == null) {
//				return;
//			}
//			DefaultValueXmlReader loader = new DefaultValueXmlReader(new InputStreamReader(is), data);
//
		//			for (ConfigPropertyLookupConstant lookup : tofetch) {
//				valuesMap.put(lookup.getPropertyName(), loader.fetchValue(lookup));
//			}
//		} catch (XmlParseException ignore) {
//
//		}
	}
/*
	private String buildPath(@NotNull Context context) {
		StringBuilder b = new StringBuilder();

		LinkedList<Context> reverse = new LinkedList<>();
		//Have the oldest ancestor (parent) be at index 0 and the newest child context at the end.
		//This will make it so that if a ControlTypeContext is a parent of a ConfigClassNameContext, the string will be
		//built as such: ControlType_ClassName
		{
			Context cur = context;
			do {
				reverse.addFirst(cur);
				cur = cur.getParentContext();
			} while (cur != null);
		}
		for (Context cur : reverse) {
			if (cur instanceof DefaultValueProvider.ControlTypeContext) {
				b.append(((ControlTypeContext) cur).getControlType().name());
			} else if (cur instanceof ConfigClassNameContext) {
				b.append(((ConfigClassNameContext) cur).getConfigClassName());
			}
			if (cur != reverse.getLast()) {
				b.append("/");
			}
		}
		b.append(".xml");
		return b.toString();
	}

	@Override
	public void cleanup() {
		valuesMap.clear();
	}
	*/
}
