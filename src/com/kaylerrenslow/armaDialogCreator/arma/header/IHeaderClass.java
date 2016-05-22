package com.kaylerrenslow.armaDialogCreator.arma.header;

import java.util.List;

/**
 Created by Kayler on 05/21/2016.
 */
public interface IHeaderClass extends IHeaderEntry{
	String getClassName();
	List<IHeaderEntry> getEntries();
}
