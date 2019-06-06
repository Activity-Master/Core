package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.Arrangement;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementType;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyIdentificationType;
import com.armineasy.activitymaster.activitymaster.services.IArrangementType;
import com.armineasy.activitymaster.activitymaster.services.IIdentificationType;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IArrangementsService<J extends IArrangementsService<J>>
{

	@SuppressWarnings("unchecked")
	Arrangement create(IArrangementType<?> type, String arrangementTypeValue,
	                   ISystems system,
	                   UUID... identityToken);

	ArrangementType createArrangementType(IArrangementType<?> type, ISystems<?> system, UUID... identityToken);

	List<Arrangement> findInvolvedPartyArrangements(InvolvedParty ip, IArrangementType<?> arrType, ISystems<?> systems, UUID... identityToken);

	@CacheResult(cacheName = "ArrangementIdentificationType")
	ArrangementType findArrangementType(@CacheKey IArrangementType<?> idType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens);
}
