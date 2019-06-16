package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityTokenXClassification;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;

public interface ISecurityToken<J extends ISecurityToken<J>>
		extends IContainsClassifications<SecurityToken, Classification, SecurityTokenXClassification, IResourceItemClassification<?>, SecurityToken>,
				        IActivityMasterEntity<SecurityToken>

{
	public String getSecurityToken();
}
