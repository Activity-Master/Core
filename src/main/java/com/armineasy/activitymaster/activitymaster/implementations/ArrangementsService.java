package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.Arrangement;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementType;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXArrangementType;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.ArrangementXInvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.IArrangementType;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.exceptions.ActivityMasterException;
import com.armineasy.activitymaster.activitymaster.services.system.IActiveFlagService;
import com.armineasy.activitymaster.activitymaster.services.system.IArrangementsService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.google.inject.Singleton;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.jwebmp.guicedinjection.GuiceContext.*;

@Singleton
public class ArrangementsService
		implements IArrangementsService<ArrangementsService>
{
	@SuppressWarnings("unchecked")
	@Override
	public Arrangement create(IArrangementType<?> type, String arrangementTypeValue,
	                          ISystems system,
	                          UUID... identityToken)
	{
		Arrangement xr = new Arrangement();
		xr.setSystemID((Systems) system);
		xr.setOriginalSourceSystemID((Systems) system);
		xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
		xr.setActiveFlagID(get(IActiveFlagService.class).getActiveFlag(system.getEnterprise(), identityToken));
		xr.persist();

		if (ActivityMasterConfiguration.get()
		                               .isSecurityEnabled())
		{
			xr.createDefaultSecurity(system, identityToken);
		}

		ArrangementXArrangementType xarxr = xr.addArrangementType(type, system, arrangementTypeValue, identityToken);

		if (ActivityMasterConfiguration.get()
		                               .isSecurityEnabled())
		{
			xarxr.createDefaultSecurity(system, identityToken);
		}

		return xr;
	}

	@Override
	public ArrangementType createArrangementType(IArrangementType<?> type, ISystems<?> system, UUID... identityToken)
	{
		ArrangementType xr = new ArrangementType();
		Optional<ArrangementType> exists = xr.builder()
		                                     .findByName(type.name())
		                                     .inActiveRange(system.getEnterpriseID(), identityToken)
		                                     .inDateRange()
		                                     .get();
		if (exists.isEmpty())
		{
			xr.setName(type.name());
			xr.setDescription(type.classificationDescription());
			xr.setSystemID((Systems) system);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
			xr.setActiveFlagID(get(IActiveFlagService.class).getActiveFlag(system.getEnterprise(), identityToken));
			xr.persist();
		}
		else
		{
			xr = exists.get();
		}
		xr.createDefaultSecurity(get(ISystemsService.class)
				                         .getActivityMaster(xr.getEnterpriseID(), identityToken), identityToken);

		return xr;
	}

	@Override
	public List<Arrangement> findInvolvedPartyArrangements(InvolvedParty ip, IArrangementType<?> arrType, ISystems<?> systems, UUID... identityToken)
	{
		List<ArrangementXInvolvedParty> xips =
				new ArrangementXInvolvedParty()
						.builder()
						.withEnterprise(systems.getEnterprise())
						.findChildLink(ip)
						.withValue(arrType.classificationValue())
						.inActiveRange(systems.getEnterpriseID(), identityToken)
						.inDateRange()
						.getAll();
		return xips.stream()
		           .map(ArrangementXInvolvedParty::getArrangementID)
		           .collect(Collectors.toList());
	}


	@CacheResult(cacheName = "ArrangementArrangementType")
	@Override
	public ArrangementType findArrangementType(@CacheKey IArrangementType<?> idType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens)
	{
		ArrangementType xr = new ArrangementType();
		return xr.builder()
		         .findByName(idType.classificationValue())
		         .inActiveRange(enterprise, tokens)
		         .inDateRange()
		         .canRead(enterprise, tokens)
		         .get()
		         .orElseThrow(() -> new ActivityMasterException("No Read Access or No Item Found"));
	}
}
