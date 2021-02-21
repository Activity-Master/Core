package com.guicedee.activitymaster.core.implementations.interceptors;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Event
{
	/**
	 * The given event type to build for
	 * @return
	 */
	String value();
}
