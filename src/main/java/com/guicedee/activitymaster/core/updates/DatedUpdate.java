package com.guicedee.activitymaster.core.updates;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.*;

@Target(
		{
				ElementType.TYPE, ElementType.TYPE_USE, ElementType.PARAMETER
		})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@BindingAnnotation
public @interface DatedUpdate
{
	String date();
	int taskCount();
}
