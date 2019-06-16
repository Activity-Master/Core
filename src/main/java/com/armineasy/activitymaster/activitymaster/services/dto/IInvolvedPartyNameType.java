package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyNameType;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsActiveFlags;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsEnterprise;
import com.armineasy.activitymaster.activitymaster.services.capabilities.INameAndDescription;

public interface IInvolvedPartyNameType<J extends IInvolvedPartyNameType<J>>
		extends INameAndDescription<InvolvedPartyNameType>,
				        IContainsEnterprise<InvolvedPartyNameType>,
				        IActivityMasterEntity<InvolvedPartyNameType>,
				        IContainsActiveFlags<InvolvedPartyNameType>
{
}
