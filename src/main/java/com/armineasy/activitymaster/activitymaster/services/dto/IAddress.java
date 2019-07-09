package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXGeography;
import com.armineasy.activitymaster.activitymaster.db.entities.address.AddressXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.Geography;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.*;
import com.armineasy.activitymaster.activitymaster.services.classifications.address.IAddressClassification;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;

public interface IAddress<J extends IAddress<?>>
		extends IContainsClassifications<Address, Classification, AddressXClassification, IAddressClassification<?>, IAddress<?>, IClassification<?>, Address>,
				        IContainsGeographies<Address, Geography, AddressXGeography>,
				        IContainsResourceItems<Address, ResourceItem, AddressXResourceItem, IResourceItemClassification<?>, IAddress<?>, IResourceItem<?>, Address>,
				        IContainsEnterprise<Address>,
				        IContainsActiveFlags<Address>,
				        IActivityMasterEntity<Address>
{
}
