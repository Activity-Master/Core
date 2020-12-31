package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.product.Product;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.*;
import com.guicedee.activitymaster.core.db.hierarchies.RulesHierarchyView;
import com.guicedee.activitymaster.core.services.capabilities.*;
import com.guicedee.activitymaster.core.services.classifications.resourceitems.IResourceItemClassification;
import com.guicedee.activitymaster.core.services.classifications.rules.IRulesClassification;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.enumtypes.IRulesTypeValue;

public interface IRules<J extends IRules<J>>
		extends IContainsClassifications<Rules, Classification, RulesXClassification, IRulesClassification<?>, IRules<?>, IClassification<?>, Rules>,
		        IContainsResourceItems<Rules, ResourceItem, RulesXResourceItem, IClassificationValue<?>, IRules<?>, IResourceItem<?>, Rules>,
		        IContainsInvolvedParties<Rules, InvolvedParty, RulesXInvolvedParty, IClassificationValue<?>, IRules<?>, IInvolvedParty<?>, Rules>,
		        IContainsProducts<Rules, Product, RulesXProduct, IClassificationValue<?>, IRules<?>, IProduct<?>, Rules>,
		        IContainsRulesTypes<Rules, RulesType, RulesXRulesType, IRulesClassification<?>, IRulesTypeValue<?>, IRules<?>, IRulesType<?>, Rules>,
		        IActivityMasterEntity<Rules>,
		        IContainsHierarchy<Rules, RulesXRules, RulesHierarchyView, IRules<?>, Rules>
{
	String getName();
	String getDescription();
}
