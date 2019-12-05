package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.classifications.arrangement.IArrangementClassification;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IArrangementTypes;

import java.util.List;
import java.util.UUID;

public interface IArrangementsService<J extends IArrangementsService<J>>
{
	IArrangement<?> create(IArrangementTypes<?> type, String arrangementTypeValue,
	                       ISystems system,
	                       UUID... identityToken);

	IArrangementType<?> createArrangementType(IArrangementTypes<?> type, ISystems<?> system, UUID... identityToken);

	List<IArrangement<?>> findInvolvedPartyArrangements(IInvolvedParty<?> ip, IArrangementTypes<?> arrType, ISystems<?> systems, UUID... identityToken);

	List<IArrangement<?>> findArrangementsByClassification(IArrangementClassification<?> arrType, String value, ISystems<?> systems, UUID... identityToken);

	IArrangementType<?> find(IArrangementTypes<?> idType, IEnterprise<?> enterprise, UUID... tokens);

	IArrangement<?> find(long id, IEnterprise<?> enterprise, UUID... tokens);

	List<IArrangement<?>> findAll(IArrangementTypes<?> idType, IEnterprise<?> enterprise, UUID... tokens);

	Double sumAll(IArrangementTypes<?> idType, IArrangementClassification<?> classificationValue,
	              IEnterpriseName<?> enterpriseName, UUID... identityToken);
}
