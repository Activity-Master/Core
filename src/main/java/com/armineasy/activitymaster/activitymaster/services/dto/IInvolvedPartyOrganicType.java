package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyOrganicType;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyType;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsActiveFlags;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsEnterprise;
import com.armineasy.activitymaster.activitymaster.services.capabilities.INameAndDescription;

public interface IInvolvedPartyOrganicType<J extends IInvolvedPartyOrganicType<J>>
		extends INameAndDescription<InvolvedPartyOrganicType>,
				        IContainsEnterprise<InvolvedPartyOrganicType>,
				        IActivityMasterEntity<InvolvedPartyOrganicType>,
				        IContainsActiveFlags<InvolvedPartyOrganicType>
{
}
