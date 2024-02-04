package com.guicedee.activitymaster.fsdm.services;

import com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class CallableTransaction<V> implements Callable<V>
{
	private Supplier<V> consumer;
	
	public static <V> CallableTransaction<V> get(Supplier<V> consumer)
	{
		CallableTransaction runnableTransaction = GuiceContext.get(CallableTransaction.class);
		runnableTransaction.consumer = consumer;
		return runnableTransaction;
	}
	
	@Override
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public V call() throws Exception
	{
		return consumer.get();
	}
}
