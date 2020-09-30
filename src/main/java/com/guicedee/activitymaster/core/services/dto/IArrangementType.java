package com.guicedee.activitymaster.core.services.dto;

import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementType;
import com.guicedee.activitymaster.core.db.entities.arrangement.ArrangementTypeXClassification;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.services.capabilities.IContainsClassifications;
import com.guicedee.activitymaster.core.services.classifications.arrangement.IArrangementClassification;

import java.io.Serializable;

public interface IArrangementType<J extends IArrangementType<J>>
		extends IContainsClassifications<ArrangementType, Classification, ArrangementTypeXClassification, IArrangementClassification<?>, IArrangementType<?>, IClassification<?>, ArrangementType>
		,
		        Serializable

{
}
