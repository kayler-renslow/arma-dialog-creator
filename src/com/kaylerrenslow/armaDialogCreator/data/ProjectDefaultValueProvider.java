package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookupConstant;
import com.kaylerrenslow.armaDialogCreator.control.DefaultValueProvider;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.xml.DefaultValueXmlLoader;
import com.kaylerrenslow.armaDialogCreator.data.xml.XmlParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 @author Kayler
 @since 11/22/2016 */
public class ProjectDefaultValueProvider implements DefaultValueProvider {
	private static final String DEFAULT_VALUE_XML_ADC_PATH = "/com/kaylerrenslow/armaDialogCreator/data/defaultControlPropertyValues.xml";

	private DefaultValueXmlLoader loader;
	private ApplicationData data;

	public ProjectDefaultValueProvider(@NotNull ApplicationData data) {
		this.data = data;
	}

	@Nullable
	@Override
	public SerializableValue getDefaultValue(@NotNull ControlPropertyLookupConstant lookup) {
		if (loader == null) {
			return null;
		}
		return loader.fetchValue(lookup);
	}

	@Override
	public void prefetchValues(@NotNull List<ControlPropertyLookupConstant> tofetch) {
		try {
			loader = new DefaultValueXmlLoader(getClass().getResourceAsStream(DEFAULT_VALUE_XML_ADC_PATH), data);
		} catch (XmlParseException ignore) {

		}
	}
}
