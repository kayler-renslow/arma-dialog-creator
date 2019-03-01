package com.armadialogcreator.data;

import com.armadialogcreator.core.Macro;
import com.armadialogcreator.core.stringtable.KnownLanguage;
import com.armadialogcreator.core.stringtable.Language;
import com.armadialogcreator.core.stringtable.StringTableKey;
import com.armadialogcreator.core.stringtable.StringTableKeyPath;
import com.armadialogcreator.core.sv.SVString;
import com.armadialogcreator.util.MapObserver;
import com.armadialogcreator.util.NotNullValueListener;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 @author Kayler
 @since 12/12/2016 */
public class SimpleStringTableKey extends Macro.BasicMacro<SVString> implements StringTableKey {
	protected final NotNullValueObserver<Language> defaultLanguageObserver = new NotNullValueObserver<>(KnownLanguage.Original);
	protected final NotNullValueObserver<String> idObserver = new NotNullValueObserver<>("str_no_id");
	protected final StringTableKeyPath path;
	protected MapObserver<Language, String> values;

	public SimpleStringTableKey(@NotNull String id, @NotNull MapObserver<Language, String> values) {
		this(id, new StringTableKeyPath(""), values);
	}

	public SimpleStringTableKey(@NotNull String id, @NotNull StringTableKeyPath path, @NotNull MapObserver<Language, String> values) {
		super(id, new SVString());
		this.path = path;
		this.values = values;
		updateMacroValue();
		setId(id);
		setKey(id);


		getDefaultLanguageObserver().addListener(new NotNullValueListener<>() {
			@Override
			public void valueUpdated(@NotNull NotNullValueObserver<Language> observer, @NotNull Language oldValue, @NotNull Language newValue) {
				updateMacroValue();
			}
		});
		getIdObserver().addListener(new NotNullValueListener<>() {
			@Override
			public void valueUpdated(@NotNull NotNullValueObserver<String> observer, @NotNull String oldValue, @NotNull String newValue) {
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
	public NotNullValueObserver<String> getIdObserver() {
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
	public NotNullValueObserver<Language> getDefaultLanguageObserver() {
		return defaultLanguageObserver;
	}

	@Override
	@NotNull
	public StringTableKey deepCopy() {
		MapObserver<Language, String> map = new MapObserver<>(new HashMap<>());
		map.putAll(this.getLanguageTokenMap());
		return new SimpleStringTableKey(getId(), getPath(), map);
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
