package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXClassification;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.services.capabilities.IActivityMasterEntity;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.capabilities.IContainsResourceItems;
import com.guicedee.activitymaster.core.services.classifications.geography.IGeographyClassification;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;

public interface IGeography<J extends IGeography<J>>
		extends IContainsClassifications<Geography, Classification, GeographyXClassification, IGeographyClassification<?>,IGeography<?>,IClassification<?>, Geography>,
				        IContainsResourceItems<Geography, ResourceItem, GeographyXResourceItem, IResourceItemClassification<?>,IGeography<?>, IResourceItem<?>,Geography>,
				        IActivityMasterEntity<Geography>
{
}
