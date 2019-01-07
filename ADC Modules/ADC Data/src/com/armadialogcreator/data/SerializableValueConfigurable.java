package com.armadialogcreator.data;

import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.KeyValueString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 @author K
 @since 01/06/2019 */
public class SerializableValueConfigurable implements Configurable {
	private final SerializableValue sv;

	public SerializableValueConfigurable(@NotNull SerializableValue sv) {
		this.sv = sv;
	}

	@Override
	@NotNull
	public List<Configurable> getNestedConfigurables() {
		String[] stringArray = sv.getAsStringArray();
		List<Configurable> vs = new ArrayList<>(stringArray.length);
		for (String s : stringArray) {
			Simple c = new Simple("v");
			c.setBody(s);
			vs.add(c);
		}
		return vs;
	}

	@Override
	@NotNull
	public List<KeyValueString> getConfigurableAttributes() {
		return new ArrayList<>();
	}

	@Override
	@NotNull
	public String getConfigurableName() {
		return "sv";
	}

	@Override
	@NotNull
	public String getConfigurableBody() {
		return "";
	}
}
