package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.address.Address;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.hierarchies.InvolvedPartyHierarchyView;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.address.IAddressClassification;
import com.guicedee.activitymaster.core.services.classifications.involvedparty.IInvolvedPartyClassification;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IIdentificationType;
import com.guicedee.activitymaster.core.services.enumtypes.INameType;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;

import java.util.UUID;

public interface IInvolvedParty<J extends IInvolvedParty<J>>
		extends IContainsClassifications<InvolvedParty, Classification, InvolvedPartyXClassification, IInvolvedPartyClassification<?>, IInvolvedParty<?>, IClassification<?>, InvolvedParty>,
		        IContainsResourceItems<InvolvedParty, ResourceItem, InvolvedPartyXResourceItem, IClassificationValue<?>, IInvolvedParty<?>, IResourceItem<?>, InvolvedParty>,
		        IContainsInvolvedPartyIdentificationTypes<InvolvedParty, InvolvedPartyIdentificationType, InvolvedPartyXInvolvedPartyIdentificationType,IClassification<?>, IIdentificationType<?>, IInvolvedParty<?>, IInvolvedPartyIdentificationType<?>, InvolvedParty>,
		        IContainsInvolvedPartyNameTypes<InvolvedParty, InvolvedPartyNameType, InvolvedPartyXInvolvedPartyNameType,IClassification<?>, INameType<?>, IInvolvedParty<?>, IInvolvedPartyNameType<?>, InvolvedParty>,
		        IContainsInvolvedPartyTypes<InvolvedParty, InvolvedPartyType, InvolvedPartyXInvolvedPartyType,IClassification<?>, ITypeValue<?>, IInvolvedParty<?>, IInvolvedPartyType<?>, InvolvedParty>,
		        IContainsAddresses<InvolvedParty, Address, InvolvedPartyXAddress, IAddressClassification<?>, IInvolvedParty<?>, IAddress<?>, InvolvedParty>,
		        IContainsEnterprise<InvolvedParty>,
		        IHasActiveFlags<InvolvedParty>,
		        IActivityMasterEntity<InvolvedParty>,
		        IContainsHierarchy<InvolvedParty, InvolvedPartyXInvolvedParty, InvolvedPartyHierarchyView, IInvolvedParty<?>>,
		IContainsProducts<InvolvedParty, Product,InvolvedPartyXProduct, IClassificationValue<?>,IInvolvedParty<?>,IProduct<?>,InvolvedParty>
{
	IInvolvedParty<?> moveWebClientUUIDToNewInvolvedParty(IInvolvedParty<?> destination, UUID newUUID);
	
	UUID getSecurityIdentity();
}
