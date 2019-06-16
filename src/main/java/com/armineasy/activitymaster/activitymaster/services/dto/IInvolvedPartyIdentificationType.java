package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyIdentificationType;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsActiveFlags;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsEnterprise;
import com.armineasy.activitymaster.activitymaster.services.capabilities.INameAndDescription;

public interface IInvolvedPartyIdentificationType<J extends IInvolvedPartyIdentificationType<J>>
		extends INameAndDescription<InvolvedPartyIdentificationType>,
				        IContainsEnterprise<InvolvedPartyIdentificationType>,
				        IActivityMasterEntity<InvolvedPartyIdentificationType>,
				        IContainsActiveFlags<InvolvedPartyIdentificationType>
{

}
