package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNo;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNoXClassification;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.classifications.yesno.IYesNoClassification;

public interface IYesNo<J extends IYesNo<J>>
		extends IContainsClassifications<YesNo, Classification, YesNoXClassification, IYesNoClassification<?>, YesNo>,
				        IActivityMasterEntity<YesNo>
{
}
