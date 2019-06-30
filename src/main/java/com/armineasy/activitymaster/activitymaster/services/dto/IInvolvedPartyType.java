package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyType;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsActiveFlags;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsEnterprise;
import com.armineasy.activitymaster.activitymaster.services.capabilities.INameAndDescription;

public interface IInvolvedPartyType<J extends IInvolvedPartyType<J>>
		extends INameAndDescription<InvolvedPartyType>,
				        IContainsEnterprise<InvolvedPartyType>,
				        IActivityMasterEntity<InvolvedPartyType>,
				        IContainsActiveFlags<InvolvedPartyType>
{
}
