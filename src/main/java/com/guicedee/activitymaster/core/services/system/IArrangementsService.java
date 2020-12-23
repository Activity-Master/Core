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
	
	IArrangementType<?> find(IArrangementTypes<?> idType, IEnterprise<?> enterprise, UUID... tokens);
	
	@CacheResult(cacheName = "ArrangementArrangementTypeString")
	IArrangementType<?> find(@CacheKey String idType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens);
	
	IArrangement<?> find(UUID id, IEnterprise<?> enterprise, UUID... tokens);
	
	IArrangement<?> find(UUID id);
	
	List<IArrangement<?>> findAll(IArrangementTypes<?> idType, IEnterprise<?> enterprise, UUID... tokens);
}
