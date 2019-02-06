package com.armadialogcreator.data;

import com.armadialogcreator.core.Macro;
import com.armadialogcreator.core.stringtable.KnownLanguage;
import com.armadialogcreator.core.stringtable.Language;
import com.armadialogcreator.core.stringtable.StringTableKey;
import com.armadialogcreator.core.stringtable.StringTableKeyPath;
import com.armadialogcreator.core.sv.SVString;
import com.armadialogcreator.util.MapObserver;
import com.armadialogcreator.util.ValueListener;
import com.armadialogcreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 @author Kayler
 @since 12/12/2016 */
class StringTableKeyImpl extends Macro.BasicMacro<SVString> implements StringTableKey {
	protected final ValueObserver<Language> defaultLanguageObserver = new ValueObserver<>(KnownLanguage.Original);
	protected final ValueObserver<String> idObserver = new ValueObserver<>("str_no_id");
	protected final StringTableKeyPath path;
	protected MapObserver<Language, String> values;

	public StringTableKeyImpl(@NotNull String id, @NotNull MapObserver<Language, String> values) {
		this(id, new StringTableKeyPath(""), values);
	}

	public StringTableKeyImpl(@NotNull String id, @NotNull StringTableKeyPath path, @NotNull MapObserver<Language, String> values) {
		super(id, new SVString());
		this.path = path;
		this.values = values;
		updateMacroValue();
		setId(id);
		setKey(id);


		getDefaultLanguageObserver().addListener(new ValueListener<>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<Language> observer, @Nullable Language oldValue, @Nullable Language newValue) {
				updateMacroValue();
			}
		});
		getIdObserver().addListener(new ValueListener<>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<String> observer, @Nullable String oldValue, @Nullable String newValue) {
				setKey(newValue);
			}
		});
		getLanguageTokenMap().addListener((list, change) -> {
			updateMacroValue();
		});

	}

	private void updateMacroValue() {
		String defaultToken = getDefaultLanguageToken();
		if (defaultToken == null) {
			Map.Entry<Language, String> firstEntry = getFirstLanguageTokenEntry();
			if (firstEntry != null) {
				defaultToken = firstEntry.getValue();
			}
		}

		setValue(new SVString(defaultToken));
	}


	@NotNull
	@Override
	public ValueObserver<String> getIdObserver() {
		return idObserver;
	}

	@NotNull
	@Override
	public StringTableKeyPath getPath() {
		return path;
	}

	@NotNull
	@Override
	public MapObserver<Language, String> getLanguageTokenMap() {
		return values;
	}

	@NotNull
	@Override
	public ValueObserver<Language> getDefaultLanguageObserver() {
		return defaultLanguageObserver;
	}

	@Override
	@NotNull
	public StringTableKey deepCopy() {
		MapObserver<Language, String> map = new MapObserver<>(new HashMap<>());
		map.putAll(this.getLanguageTokenMap());
		return new StringTableKeyImpl(getId(), getPath(), map);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof StringTableKey) {
			StringTableKey other = (StringTableKey) o;
			return equalsKey(other);
		}
		return false;
	}

	@Override
	public String toString() {
		return getId();
	}
}
