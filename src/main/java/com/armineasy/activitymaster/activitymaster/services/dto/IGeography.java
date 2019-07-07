package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.Geography;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXClassification;
import com.armineasy.activitymaster.activitymaster.db.entities.geography.GeographyXResourceItem;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IActivityMasterEntity;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsClassifications;
import com.armineasy.activitymaster.activitymaster.services.capabilities.IContainsResourceItems;
import com.armineasy.activitymaster.activitymaster.services.classifications.geography.IGeographyClassification;
import com.armineasy.activitymaster.activitymaster.services.classifications.resourceitems.IResourceItemClassification;

public interface IGeography<J extends IGeography<J>>
		extends IContainsClassifications<Geography, Classification, GeographyXClassification, IGeographyClassification<?>, Geography>,
				        IContainsResourceItems<Geography, ResourceItem, GeographyXResourceItem, IResourceItemClassification<?>,IGeography<?>, IResourceItem<?>,Geography>,
				        IActivityMasterEntity<Geography>
{
}
