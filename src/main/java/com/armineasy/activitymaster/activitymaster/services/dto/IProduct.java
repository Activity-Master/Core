package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.product.Product;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.product.ProductXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;
import com.armineasy.activitymaster.activitymaster.services.classifications.product.IProductClassification;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;

public interface IProduct<J extends IProduct<J>>
		extends IContainsClassifications<Product, Classification, ProductXClassification, IProductClassification<?>, Product>,
				        IContainsResourceItems<Product, ResourceItem, ProductXResourceItem, IResourceItemClassification<?>,IProduct<?>, IResourceItem<?>, Product>,
				        IActivityMasterEntity<Product>
{
}
