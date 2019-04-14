package com.armadialogcreator.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 @author K
 @since 02/09/2019 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ApplicationSingleton {
	/** The field that should be set to the instance */
	String field() default "instance";
}
