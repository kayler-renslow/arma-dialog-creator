package com.kaylerrenslow.armaDialogCreator.data;

import java.util.ArrayList;
import java.util.List;

/**
 Created by Kayler on 07/19/2016.
 */
public class ResourceRegistry {
	private final List<ExternalResource> externalResourceList = new ArrayList<>();
	
	ResourceRegistry() {
	}
	
	public List<ExternalResource> getExternalResourceList() {
		return externalResourceList;
	}
}
