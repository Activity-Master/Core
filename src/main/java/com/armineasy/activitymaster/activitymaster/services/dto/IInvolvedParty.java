package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.address.Address;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.*;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.*;
import com.armineasy.activitymaster.activitymaster.services.classifications.involvedparty.IInvolvedPartyClassification;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IIdentificationType;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.INameType;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.ITypeValue;

import java.util.UUID;

public interface IInvolvedParty<J extends IInvolvedParty<J>>
		extends IContainsClassifications<InvolvedParty, Classification, InvolvedPartyXClassification, IInvolvedPartyClassification<?>, InvolvedParty>,
				        IContainsResourceItems<InvolvedParty, ResourceItem, InvolvedPartyXResourceItem>,
				        IContainsInvolvedPartyIdentificationTypes<InvolvedParty, InvolvedPartyIdentificationType, InvolvedPartyXInvolvedPartyIdentificationType, IIdentificationType<?>, InvolvedParty>,
				        IContainsInvolvedPartyNameTypes<InvolvedParty, InvolvedPartyNameType, InvolvedPartyXInvolvedPartyNameType, INameType<?>, InvolvedParty>,
				        IContainsInvolvedPartyTypes<InvolvedParty, InvolvedPartyType, InvolvedPartyXInvolvedPartyType, ITypeValue<?>, InvolvedParty>,
				        IContainsAddresses<InvolvedParty, Address, InvolvedPartyXAddress>,
				        IContainsEnterprise<InvolvedParty>,
				        IContainsActiveFlags<InvolvedParty>,
				        IActivityMasterEntity<InvolvedParty>
{
	UUID getSecurityIdentity();
}
