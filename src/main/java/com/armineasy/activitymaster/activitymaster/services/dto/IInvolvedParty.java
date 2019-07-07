package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.*;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.*;
import com.armineasy.activitymaster.activitymaster.services.classifications.involvedparty.IInvolvedPartyClassification;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IIdentificationType;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.INameType;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IResourceType;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.ITypeValue;

import java.util.UUID;

public interface IInvolvedParty<J extends IInvolvedParty<J>>
		extends IContainsClassifications<InvolvedParty, Classification, InvolvedPartyXClassification, IInvolvedPartyClassification<?>, InvolvedParty>,
				        IContainsResourceItems<InvolvedParty, ResourceItem, InvolvedPartyXResourceItem, IResourceItemClassification<?>,IInvolvedParty<?>,IResourceItem<?>, InvolvedParty>,
				        IContainsInvolvedPartyIdentificationTypes<InvolvedParty, InvolvedPartyIdentificationType, InvolvedPartyXInvolvedPartyIdentificationType, IIdentificationType<?>,IInvolvedParty<?>,IInvolvedPartyIdentificationType<?>, InvolvedParty>,
				        IContainsInvolvedPartyNameTypes<InvolvedParty, InvolvedPartyNameType, InvolvedPartyXInvolvedPartyNameType, INameType<?>,IInvolvedParty<?>,IInvolvedPartyNameType<?>, InvolvedParty>,
				        IContainsInvolvedPartyTypes<InvolvedParty, InvolvedPartyType, InvolvedPartyXInvolvedPartyType, ITypeValue<?>,IInvolvedParty<?>,IInvolvedPartyType<?>, InvolvedParty>,
				        IContainsAddresses<InvolvedParty, Address, InvolvedPartyXAddress>,
				        IContainsEnterprise<InvolvedParty>,
				        IContainsActiveFlags<InvolvedParty>,
				        IActivityMasterEntity<InvolvedParty>
{
	UUID getSecurityIdentity();
}
