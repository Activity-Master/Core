package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.arrangement.*;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.Rules;
import com.guicedee.activitymaster.core.db.hierarchies.ArrangementsHierarchyView;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.arrangement.IArrangementClassification;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IArrangementTypes;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

public interface IArrangement<J extends IArrangement<J>>
		extends IContainsClassifications<Arrangement, Classification, ArrangementXClassification, IArrangementClassification<?>, IArrangement<?>, IClassification<?>, Arrangement>,
		        IContainsResourceItems<Arrangement, ResourceItem, ArrangementXResourceItem, IClassificationValue<?>, IArrangement<?>, IResourceItem<?>, Arrangement>,
		        IActivityMasterEntity<Arrangement>,
		        IContainsArrangementTypes<Arrangement, ArrangementType, ArrangementXArrangementType, IArrangementTypes<?>, IArrangement<?>, IArrangementType<?>, Arrangement>,
		        IContainsInvolvedParties<Arrangement, InvolvedParty, ArrangementXInvolvedParty, IClassificationValue<?>, IArrangement<?>, IInvolvedParty<?>, Arrangement>,
		        IHasActiveFlags<Arrangement>,
		        IContainsHierarchy<Arrangement,ArrangementXArrangement, ArrangementsHierarchyView,IArrangement<J>>,
		        IContainsEnterprise<Arrangement>,
		        IContainsRules<Arrangement, Rules,ArrangementXRules,IClassification<?>,IArrangement<?>,IRules<?>>
{
}
