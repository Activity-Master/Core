package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXClassification;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface ISecurityToken<J extends ISecurityToken<J>>
		extends IContainsClassifications<SecurityToken, Classification, SecurityTokenXClassification, IClassificationValue<?>, ISecurityToken<?>, IClassification<?>, SecurityToken>,
		        IActivityMasterEntity<SecurityToken>,
		        IContainsActiveFlags<J>,
		        IContainsEnterprise<J>

{
	String getSecurityToken();
}
