package com.guicedee.activitymaster.core.services.dto;

import com.google.inject.ImplementedBy;
import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.address.AddressXClassification;
import com.guicedee.activitymaster.core.db.entities.address.AddressXGeography;
import com.guicedee.activitymaster.core.db.entities.address.AddressXResourceItem;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface IAddress<J extends IAddress<?>>
		extends IContainsClassifications<Address, Classification, AddressXClassification, IAddressClassification<?>, IAddress<?>, IClassification<?>, Address>,
		        IContainsGeographies<Address, Geography, AddressXGeography>,
		        IContainsResourceItems<Address, ResourceItem, AddressXResourceItem, IClassificationValue<?>, IAddress<?>, IResourceItem<?>, Address>,
		        IContainsEnterprise<Address>,
		        IContainsActiveFlags<Address>,
		        IActivityMasterEntity<Address>
{
}
