package com.armadialogcreator.arma.stringtable.impl;

import com.armadialogcreator.arma.stringtable.KnownLanguage;
import com.armadialogcreator.arma.stringtable.Language;
import com.armadialogcreator.arma.stringtable.StringTableKey;
import com.armadialogcreator.arma.stringtable.StringTableKeyPath;
import com.armadialogcreator.control.Macro;
import com.armadialogcreator.control.MacroType;
import com.armadialogcreator.control.sv.SVString;
import com.armadialogcreator.util.ValueListener;
import com.armadialogcreator.util.ValueObserver;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 @author Kayler
 @since 12/12/2016 */
public class StringTableKeyImpl extends Macro.BasicMacro<SVString> implements StringTableKey {
	protected final ValueObserver<Language> defaultLanguageObserver = new ValueObserver<>(KnownLanguage.Original);
	protected final ValueObserver<String> idObserver = new ValueObserver<>("str_no_id");
	protected final StringTableKeyPath path;
	protected ObservableMap<Language, String> values;

	public StringTableKeyImpl(@NotNull String id, @NotNull ObservableMap<Language, String> values) {
		this(id, new StringTableKeyPath(""), values);
	}

	public StringTableKeyImpl(@NotNull String id, @NotNull StringTableKeyPath path, @NotNull ObservableMap<Language, String> values) {
		super(id, new SVString());
		this.path = path;
		setMacroType(MacroType.STRING_TABLE);
		this.values = values;
		updateMacroValue();
		setId(id);
		setKey(id);


		getDefaultLanguageObserver().addListener(new ValueListener<Language>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<Language> observer, @Nullable Language oldValue, @Nullable Language newValue) {
				updateMacroValue();
			}
		});
		getIdObserver().addListener(new ValueListener<String>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<String> observer, @Nullable String oldValue, @Nullable String newValue) {
				setKey(newValue);
			}
		});
		getLanguageTokenMap().addListener(new MapChangeListener<Language, String>() {
			@Override
			public void onChanged(Change<? extends Language, ? extends String> change) {
				if (change.getKey().equals(getDefaultLanguage())) {
					updateMacroValue();
				}
			}
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
	public ObservableMap<Language, String> getLanguageTokenMap() {
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
		ObservableMap<Language, String> map = FXCollections.observableHashMap();
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
