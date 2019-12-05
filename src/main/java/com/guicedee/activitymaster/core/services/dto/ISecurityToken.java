package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.security.SecurityTokenXClassification;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;

public interface ISecurityToken<J extends ISecurityToken<J>>
		extends IContainsClassifications<SecurityToken, Classification, SecurityTokenXClassification, IResourceItemClassification<?>,ISecurityToken<?>, IClassification<?>,SecurityToken>,
				        IActivityMasterEntity<SecurityToken>

{
	public String getSecurityToken();
}
