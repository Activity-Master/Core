package com.guicedee.activitymaster.fsdm.implementations;

import com.google.inject.*;
import com.guicedee.activitymaster.fsdm.client.services.IProductService;
import com.guicedee.activitymaster.fsdm.ProductService;
import com.guicedee.client.services.lifecycle.IGuiceModule;

public class ProductsBinder extends PrivateModule implements IGuiceModule<ProductsBinder>
{
	@Override
	protected void configure()
	{
		@SuppressWarnings("Convert2Diamond")
		Key<IProductService<?>>genericKey = Key.get(new TypeLiteral<IProductService<?>>() {});
		@SuppressWarnings("Convert2Diamond")
		Key<IProductService<ProductService>> realKey
				= Key.get(new TypeLiteral<IProductService<ProductService>>() {});
		
		bind(genericKey).to(realKey);
		bind(realKey).to(ProductService.class);
		bind(IProductService.class).to(genericKey);
		
		expose(genericKey);
		expose(IProductService.class);
	}
}
