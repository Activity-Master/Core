package com.armineasy.activitymaster.activitymaster.services.dto;

import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.*;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.resourceitem.ResourceItem;
import com.armineasy.activitymaster.activitymaster.services.capabilities.*;
import com.armineasy.activitymaster.activitymaster.services.classifications.arrangement.IArrangementClassification;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IArrangementTypes;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.systems.ActiveFlagSystem;
import com.jwebmp.guicedinjection.GuiceContext;

import java.time.LocalDateTime;

public interface IArrangement<J extends IArrangement<J>>
		extends IContainsClassifications<Arrangement, Classification, ArrangementXClassification, IArrangementClassification<?>, Arrangement>,
				        IContainsResourceItems<Arrangement, ResourceItem, ArrangementXResourceItem>,
				        IActivityMasterEntity<Arrangement>,
				        IContainsArrangementTypes<Arrangement, ArrangementType, ArrangementXArrangementType, IArrangementTypes<?>, Arrangement>,
				        IContainsInvolvedParties<Arrangement, InvolvedParty, ArrangementXInvolvedParty>,
				        IContainsActiveFlags<Arrangement>,
				        IContainsEnterprise<Arrangement>
{
}
