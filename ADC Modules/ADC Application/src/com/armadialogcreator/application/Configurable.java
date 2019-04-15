package com.armadialogcreator.application;

import com.armadialogcreator.util.IndentedStringBuilder;
import com.armadialogcreator.util.KeyValueString;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 A facade interface for a config XML file

 @author K
 @since 01/06/2019 */
public interface Configurable {
	@NotNull Iterable<Configurable> getNestedConfigurables();

	int getNestedConfigurableCount();

	/** XML safe attributes (attribute_name='attribute_value') */
	@NotNull Iterable<KeyValueString> getConfigurableAttributes();

	int getConfigurableAttributeCount();

	void addNestedConfigurable(@NotNull Configurable c);

	void addAttribute(@NotNull String key, @NotNull String value);

	default void addAttribute(@NotNull String key, boolean b) {
		addAttribute(key, b ? "true" : "false");
	}

	default void addAttribute(@NotNull String key, int i) {
		addAttribute(key, i + "");
	}

	/** An XML safe name (<get_name_result></get_name_result>). If empty string, <b>no data</b> will be saved to file. */
	@NotNull
	@NonNls
	String getConfigurableName();

	/** Misc text that can contain anything */
	@NonNls
	@NotNull
	String getConfigurableBody();

	@Nullable
	default Configurable getConfigurable(@NotNull String name) {
		for (Configurable c : getNestedConfigurables()) {
			if (c.getConfigurableName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	@Nullable
	default String getAttributeValue(@NotNull String key) {
		for (KeyValueString kv : getConfigurableAttributes()) {
			if (kv.getKey().equals(key)) {
				return kv.getValue();
			}
		}
		return null;
	}

	class Simple implements Configurable {
		@NotNull
		private final String configName;
		@NotNull
		private List<Configurable> nested = new ArrayList<>();
		@NotNull
		private List<KeyValueString> atts = new ArrayList<>();
		@NotNull
		private String body = "";

		public Simple(@NotNull String configName) {
			this.configName = configName;
		}

		public Simple(@NotNull String configName, @NotNull String body) {
			this.configName = configName;
			this.body = body;
		}

		public void setBody(@NotNull String body) {
			this.body = body;
		}

		@Override
		@NotNull
		public Iterable<Configurable> getNestedConfigurables() {
			return nested;
		}

		@Override
		public int getNestedConfigurableCount() {
			return atts.size();
		}

		@Override
		@NotNull
		public Iterable<KeyValueString> getConfigurableAttributes() {
			return atts;
		}

		@Override
		public int getConfigurableAttributeCount() {
			return nested.size();
		}

		@Override
		public void addNestedConfigurable(@NotNull Configurable c) {
			nested.add(c);
		}

		@Override
		public void addAttribute(@NotNull String key, @NotNull String value) {
			atts.add(new KeyValueString(key, value));
		}

		@Override
		@NotNull
		public String getConfigurableName() {
			return configName;
		}

		@Override
		@NotNull
		public String getConfigurableBody() {
			return body;
		}
	}

	Configurable EMPTY = new Configurable() {

		@Override
		public @NotNull List<Configurable> getNestedConfigurables() {
			// always return new list so that it is mutable (Collections.emptyList() isn't mutable)
			// and so the previous use case of EMPTY doesn't carry over to a different use case of EMPTY
			return new ArrayList<>();
		}

		@Override
		public int getNestedConfigurableCount() {
			return 0;
		}

		@Override
		@NotNull
		public List<KeyValueString> getConfigurableAttributes() {
			//always return new list so that it is mutable (Collections.emptyList() isn't mutable)
			// and so the previous use case of EMPTY doesn't carry over to a different use case of EMPTY
			return new ArrayList<>();
		}

		@Override
		public int getConfigurableAttributeCount() {
			return 0;
		}

		@Override
		public void addNestedConfigurable(@NotNull Configurable c) {

		}

		@Override
		public void addAttribute(@NotNull String key, @NotNull String value) {

		}

		@Override
		@NotNull
		public String getConfigurableName() {
			return "";
		}

		@Override
		@NotNull
		public String getConfigurableBody() {
			return "";
		}
	};

	@NotNull
	static String toFormattedString(@NotNull Configurable c, int tabCount) {
		IndentedStringBuilder sb = new IndentedStringBuilder(tabCount, true);
		toFormattedString(c, sb);
		return sb.toString();
	}

	private static void toFormattedString(@NotNull Configurable c, @NotNull IndentedStringBuilder sb) {
		sb.append('<');
		sb.append(c.getConfigurableName());
		for (KeyValueString kv : c.getConfigurableAttributes()) {
			sb.append(' ');
			sb.append(kv.getKey());
			sb.append('=');
			sb.append('"');
			sb.append(kv.getValue());
			sb.append('"');
		}
		sb.append('>');
		String body = c.getConfigurableBody().trim();
		if (body.length() > 0) {
			sb.append(c.getConfigurableBody());
		}
		Iterator<Configurable> iterator = c.getNestedConfigurables().iterator();
		if (iterator.hasNext()) {
			sb.incrementTabCount();
			sb.appendNewLine();
			while (iterator.hasNext()) {
				toFormattedString(iterator.next(), sb);
				if (iterator.hasNext()) {
					sb.appendNewLine();
				} else {
					sb.decrementTabCount();
					sb.appendNewLine();
				}
			}
		}
		sb.append('<');
		sb.append('/');
		sb.append(c.getConfigurableName());
		sb.append('>');
	}
}
