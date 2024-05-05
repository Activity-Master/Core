package com.guicedee.activitymaster.fsdm.services;

import com.google.inject.persist.Transactional;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class CallableTransaction<V> implements Callable<V>
{
	private Supplier<V> consumer;
	
	public static <V> CallableTransaction<V> get(Supplier<V> consumer)
	{
		CallableTransaction runnableTransaction = com.guicedee.client.IGuiceContext.get(CallableTransaction.class);
		runnableTransaction.consumer = consumer;
		return runnableTransaction;
	}
	
	@Override
	@Transactional()
	public V call() throws Exception
	{
		return consumer.get();
	}
}
