package com.armadialogcreator.core;

import java.util.Collections;

/**
 A {@link ControlClass} implementation used for testing.
 NEVER USE THIS in a {@link ControlClass} or {@link ControlClassSpecification}, including testing related ones.
 If you do use this, you are defeating the purpose of this class,
 which is for guaranteeing no {@link ControlClass} is found via a {@link ControlClass#findNestedClass(String)} related method.

 @author Kayler
 @since 05/30/2017 */
public class TestFakeControlClass extends ControlClass {
	public static final ControlClass INSTANCE = new TestFakeControlClass();

	public TestFakeControlClass() {
		super("**FAKE CONTROL CLASS**", new ControlClassSpecification("", Collections.emptyList(), Collections.emptyList()), new TestSpecRegistry());
	}
}
