package com.guicedee.activitymaster.core.services.classifications.events;

import com.guicedee.activitymaster.core.services.dto.IEvent;

public class EventThread
{
	public static ThreadLocal<IEvent<?>> event = ThreadLocal.withInitial(() -> null);
}
