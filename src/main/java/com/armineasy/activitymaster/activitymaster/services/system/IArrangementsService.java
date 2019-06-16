package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.Arrangement;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.services.classifications.arrangement.IArrangementClassification;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;
import com.armineasy.activitymaster.activitymaster.services.dto.IArrangement;
import com.armineasy.activitymaster.activitymaster.services.dto.IInvolvedParty;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IArrangementTypes;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;

import java.util.List;
import java.util.UUID;

public interface IArrangementsService<J extends IArrangementsService<J>>
{
	IArrangement<?> create(IArrangementTypes<?> type, String arrangementTypeValue,
	                       ISystems system,
	                       UUID... identityToken);

	com.armineasy.activitymaster.activitymaster.services.dto.IArrangementType<?> createArrangementType(IArrangementTypes<?> type, ISystems<?> system, UUID... identityToken);

	List<IArrangement<?>> findInvolvedPartyArrangements(IInvolvedParty<?> ip, IArrangementTypes<?> arrType, ISystems<?> systems, UUID... identityToken);

	List<IArrangement<?>> findArrangementsByClassification(IArrangementClassification<?> arrType, String value, ISystems<?> systems, UUID... identityToken);

	com.armineasy.activitymaster.activitymaster.services.dto.IArrangementType<?> find(IArrangementTypes<?> idType, IEnterprise<?> enterprise, UUID... tokens);

	IArrangement<?> find(long id, IEnterprise<?> enterprise, UUID... tokens);

	List<IArrangement<?>> findAll(IArrangementTypes<?> idType, IEnterprise<?> enterprise, UUID... tokens);

	Double sumAll(IArrangementTypes<?> idType, IArrangementClassification<?> classificationValue,
	              IEnterpriseName<?> enterpriseName, UUID... identityToken);
}
