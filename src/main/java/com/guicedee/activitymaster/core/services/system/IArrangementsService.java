package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.classifications.arrangement.IArrangementClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IArrangementTypes;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IArrangementsService<J extends IArrangementsService<J>>
{
	IArrangement<?> create(IArrangementTypes<?> type, IClassificationValue<?> arrangementTypeClassification,
	                       String arrangementTypeValue,
	                       ISystems<?> system,
	                       LocalDateTime createdDate,
	                       UUID... identityToken);
	
	IArrangement<?> create(IArrangementTypes<?> type,
	                       IClassificationValue<?> arrangementTypeClassification,
	                       String arrangementTypeValue,
	                       ISystems<?> system,
	                       LocalDateTime createdDate,
	                       LocalDateTime endCompletionDate,
	                       UUID... identityToken);
	
	IArrangementType<?> createArrangementType(IArrangementTypes<?> type, ISystems<?> system, UUID... identityToken);
	
	List<IArrangement<?>> findInvolvedPartyArrangements(IInvolvedParty<?> ip, IArrangementTypes<?> arrType, ISystems<?> systems, UUID... identityToken);
	
	List<IArrangement<?>> findArrangementsByClassification(IArrangementClassification<?> arrType, String value, ISystems<?> systems, UUID... identityToken);
	
	List<IArrangement<?>> findArrangementsByClassification(IArrangementClassification<?> arrType, IArrangement<?> withParent, String value, ISystems<?> systems, UUID... identityToken);
	
	IArrangement<?> findArrangementByResourceItem(IResourceItem<?> resourceItem, String classificationName, String value, ISystems<?> system, UUID... identityToken);
	
	IArrangement<?> findArrangementByInvolvedParty(IInvolvedParty<?> involvedParty, String classificationName, String value, ISystems<?> system, UUID... identityToken);
	
	List<IArrangement<?>> findArrangementsByInvolvedParty(IInvolvedParty<?> involvedParty, String classificationName, String value, ISystems<?> system, UUID... identityToken);
	
	List<IInvolvedParty<?>> findArrangementInvolvedParties(IArrangement<?> arrangement, String classificationName, String value, ISystems<?> system, UUID... identityToken);
	
	IArrangementType<?> find(IArrangementTypes<?> idType, ISystems<?> system, UUID... tokens);

	IArrangementType<?> find(String idType, ISystems<?> system, UUID... tokens);
	
	IArrangement<?> find(UUID id, ISystems<?> system, UUID... tokens);
	
	IArrangement<?> find(UUID id);
	
	List<IArrangement<?>> findAll(IArrangementTypes<?> idType, ISystems<?> system, UUID... tokens);
	
	IArrangement<?> completeArrangement(IArrangement<?> arrangement, ISystems<?> system, UUID... identityToken);
}
