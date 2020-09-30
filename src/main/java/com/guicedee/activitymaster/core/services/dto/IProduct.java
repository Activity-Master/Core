package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.product.ProductXClassification;
import com.guicedee.activitymaster.core.db.entities.product.ProductXResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.product.IProductClassification;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface IProduct<J extends IProduct<J>>
		extends IContainsClassifications<Product, Classification, ProductXClassification, IProductClassification<?>, IProduct<?>, IClassification<?>, Product>,
		        IContainsResourceItems<Product, ResourceItem, ProductXResourceItem, IClassificationValue<?>, IProduct<?>, IResourceItem<?>, Product>,
		        IActivityMasterEntity<Product>,
		        IHasActiveFlags<Product>,
		        INameAndDescription<Product>
{
}
