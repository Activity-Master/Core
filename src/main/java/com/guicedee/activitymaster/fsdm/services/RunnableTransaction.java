package com.guicedee.activitymaster.fsdm.services;

import com.google.inject.persist.Transactional;

import java.util.function.Supplier;

public class RunnableTransaction implements Runnable
{
	private Supplier<?> consumer;
	
	public static RunnableTransaction get(Supplier<?> consumer)
	{
		RunnableTransaction runnableTransaction = com.guicedee.client.IGuiceContext.get(RunnableTransaction.class);
		runnableTransaction.consumer = consumer;
		return runnableTransaction;
	}
	
	@Override
	//@Transactional()
	public void run()
	{
		consumer.get();
	}
}
