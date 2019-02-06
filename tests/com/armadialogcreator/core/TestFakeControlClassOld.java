package com.armadialogcreator.core;

import com.armadialogcreator.core.old.ControlClassOld;
import com.armadialogcreator.core.old.ControlClassSpecification;

import java.util.Collections;

/**
 A {@link ControlClassOld} implementation used for testing.
 NEVER USE THIS in a {@link ControlClassOld} or {@link ControlClassSpecification}, including testing related ones.
 If you do use this, you are defeating the purpose of this class,
 which is for guaranteeing no {@link ControlClassOld} is found via a {@link ControlClassOld#findNestedClass(String)} related method.

 @author Kayler
 @since 05/30/2017 */
public class TestFakeControlClassOld extends ControlClassOld {
	public static final ControlClassOld INSTANCE = new TestFakeControlClassOld();

	public TestFakeControlClassOld() {
		super("**FAKE CONTROL CLASS**", new ControlClassSpecification("", Collections.emptyList(), Collections.emptyList()), new TestSpecRegistry());
	}
}
