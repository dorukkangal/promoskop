package com.mudo.promoskop.jackson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JacksonFilter {
	String[] includeProperties() default {};

	String[] excludeProperties() default {};

	String[] includePaths() default {};

	String[] excludePaths() default {};
}