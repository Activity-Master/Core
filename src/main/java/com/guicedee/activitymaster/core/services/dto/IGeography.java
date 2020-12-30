package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.geography.Geography;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXClassification;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXGeography;
import com.guicedee.activitymaster.core.db.entities.geography.GeographyXResourceItem;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.hierarchies.GeographyHierarchyView;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.geography.IGeographyClassification;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface IGeography<J extends IGeography<J>>
		extends IContainsClassifications<Geography, Classification, GeographyXClassification, IGeographyClassification<?>, IGeography<?>, IClassification<?>, Geography>,
		        IContainsResourceItems<Geography, ResourceItem, GeographyXResourceItem, IClassificationValue<?>, IGeography<?>, IResourceItem<?>, Geography>,
		        IActivityMasterEntity<Geography>,
		        IContainsHierarchy<Geography, GeographyXGeography, GeographyHierarchyView,IGeography<?>, IGeography<?>>,
		        IHasActiveFlags<Geography>,
		        INameAndDescription<Geography>
{
}
