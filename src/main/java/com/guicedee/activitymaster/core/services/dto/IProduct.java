package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.product.ProductXClassification;
import com.guicedee.activitymaster.core.db.entities.product.ProductXResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.IContainsResourceItems;
import com.guicedee.activitymaster.core.services.classifications.product.IProductClassification;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;

public interface IProduct<J extends IProduct<J>>
		extends IContainsClassifications<Product, Classification, ProductXClassification, IProductClassification<?>,IProduct<?>,IClassification<?>, Product>,
				        IContainsResourceItems<Product, ResourceItem, ProductXResourceItem, IResourceItemClassification<?>,IProduct<?>, IResourceItem<?>, Product>,
				        IActivityMasterEntity<Product>
{
}
