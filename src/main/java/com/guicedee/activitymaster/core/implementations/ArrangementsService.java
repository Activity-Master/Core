package com.guicedee.activitymaster.core.implementations;

import com.entityassist.enumerations.OrderByType;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.google.inject.Singleton;
import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.arrangement.*;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementXArrangementQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.ArrangementXClassificationQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.geography.*;
import com.guicedee.activitymaster.core.db.entities.geography.builders.GeographyXGeographyQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.classifications.arrangement.IArrangementClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IArrangementTypes;
import com.guicedee.activitymaster.core.services.enumtypes.IClassificationValue;
import com.guicedee.activitymaster.core.services.exceptions.ArrangementException;
import com.guicedee.activitymaster.core.services.system.IActiveFlagService;
import com.guicedee.activitymaster.core.services.system.IArrangementsService;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.guicedinjection.GuiceContext;

import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.persistence.criteria.JoinType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.entityassist.SCDEntity.*;
import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.guicedinjection.GuiceContext.*;


public class ArrangementsService
		implements IArrangementsService<ArrangementsService>
{
	@Override
	public IArrangement<?> create(IArrangementTypes<?> type,
	                              IClassificationValue<?> arrangementTypeClassification,
	                              String arrangementTypeValue,
	                              ISystems<?> system,
	                              LocalDateTime createdDate,
	                              UUID... identityToken)
	{
		return create(type, arrangementTypeClassification, arrangementTypeValue, system, createdDate,EndOfTime, identityToken);
	}
	
	@Override
	public IArrangement<?> create(IArrangementTypes<?> type,
	                              IClassificationValue<?> arrangementTypeClassification,
	                              String arrangementTypeValue,
	                              ISystems<?> system,
	                              LocalDateTime createdDate,
	                              LocalDateTime endCompletionDate,
	                              UUID... identityToken)
	{
		ArrangementType tt = (ArrangementType) find(type, system.getEnterprise());
		boolean exists = new ArrangementXArrangementType().builder()
		                                                  .withValue(arrangementTypeValue)
		                                                  .inActiveRange(system.getEnterpriseID(), identityToken)
		                                                  .inDateRange()
		                                                  .where(ArrangementXArrangementType_.type, Equals, tt)
		                                                  .where(ArrangementXArrangementType_.effectiveFromDate, Equals, createdDate)
		                                                  .withClassification(arrangementTypeClassification, system.getEnterprise())
		                                                  .withEnterprise(system.getEnterprise())
		                                                  .getCount() > 0;
		if (exists)
		{
			return new ArrangementXArrangementType().builder()
			                                        .withValue(arrangementTypeValue)
			                                        .inActiveRange(system.getEnterpriseID(), identityToken)
			                                        .inDateRange()
			                                        .where(ArrangementXArrangementType_.type, Equals, tt)
			                                        .where(ArrangementXArrangementType_.effectiveFromDate, Equals, createdDate)
			                                        .withClassification(arrangementTypeClassification, system.getEnterprise())
			                                        .withEnterprise(system.getEnterprise())
			                                        .get()
			                                        .orElseThrow()
			                                        .getArrangement();
		}
		
		Arrangement xr = new Arrangement();
		xr.setEffectiveFromDate(createdDate);
		xr.setWarehouseCreatedTimestamp(createdDate);
		xr.setWarehouseLastUpdatedTimestamp(endCompletionDate);
		xr.setSystemID((Systems) system);
		xr.setOriginalSourceSystemID((Systems) system);
		xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
		xr.setActiveFlagID((ActiveFlag) get(IActiveFlagService.class).getActiveFlag(system.getEnterprise(), identityToken));
		xr.persist();
		
		if (ActivityMasterConfiguration.get()
		                               .isSecurityEnabled())
		{
			xr.createDefaultSecurity(system, identityToken);
		}
		
		ArrangementXArrangementType xarxr = xr.addOrUpdate(arrangementTypeClassification, type,
		                                                   arrangementTypeValue, system, identityToken);
		
		if (ActivityMasterConfiguration.get()
		                               .isSecurityEnabled())
		{
			xarxr.createDefaultSecurity(system, identityToken);
		}
		
		return xr;
	}
	
	@Override
	@CacheResult(cacheName = "ArrangementTypes")
	public IArrangementType<?> createArrangementType(@CacheKey IArrangementTypes<?> type, @CacheKey ISystems<?> system, @CacheKey UUID... identityToken)
	{
		ArrangementType xr = new ArrangementType();
		
		boolean exists = xr.builder()
		                   .withName(type.name())
		                   .inActiveRange(system.getEnterpriseID(), identityToken)
		                   .inDateRange()
		                   .withEnterprise(system.getEnterprise())
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(type.name());
			xr.setDescription(type.classificationDescription());
			xr.setSystemID((Systems) system);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
			xr.setActiveFlagID(((Systems) system).getActiveFlagID());
			xr.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				
				xr.createDefaultSecurity(get(ISystemsService.class)
						                         .getActivityMaster(xr.getEnterpriseID(), identityToken), identityToken);
			}
		}
		else
		{
			return find(type, system.getEnterprise(), identityToken);
		}
		return xr;
	}
	
	@Override
	public List<IArrangement<?>> findInvolvedPartyArrangements(IInvolvedParty<?> ip, IArrangementTypes<?> arrType, ISystems<?> systems, UUID... identityToken)
	{
		List<ArrangementXInvolvedParty> xips =
				new ArrangementXInvolvedParty()
						.builder()
						.withEnterprise(systems.getEnterprise())
						.findChildLink((InvolvedParty) ip)
						.withValue(arrType.classificationValue())
						.inActiveRange(systems.getEnterpriseID(), identityToken)
						.inDateRange()
						.getAll();
		
		return xips.stream()
		           .map(ArrangementXInvolvedParty::getArrangementID)
		           .filter(a -> LocalDateTime.now()
		                                     .isBefore(a.getEffectiveToDate()))
		           .collect(Collectors.toList());
	}
	
	@Override
	public List<IArrangement<?>> findArrangementsByClassification(IArrangementClassification<?> arrType, String value, ISystems<?> systems, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(arrType, systems.getEnterpriseID(), identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(systems.getEnterprise())
		   .inActiveRange(systems.getEnterpriseID(), identityToken)
		   .inDateRange();
		JoinExpression<Arrangement,Classification,?> aje = new JoinExpression<>();
		
		
		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(systems.getEnterprise())
		  .findChildLink((Classification) classification)
		  .withValue(value)
		  .inActiveRange(systems.getEnterpriseID(), identityToken)
		  .inDateRange();
		
		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		
		aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);
		
		List<Arrangement> arrangementList = aqb.getAll();
		return (List) arrangementList;
	}
	
	
	@Override
	public List<IArrangement<?>> findArrangementsByClassification(IArrangementClassification<?> arrType, IArrangement<?> withParent, String value, ISystems<?> systems, UUID... identityToken)
	{
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(arrType, systems.getEnterpriseID(), identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(systems.getEnterprise())
		   .inActiveRange(systems.getEnterpriseID(), identityToken)
		   .inDateRange();
		JoinExpression<Arrangement,Classification,?> aje = new JoinExpression<>();
		
		
		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(systems.getEnterprise())
		  .findChildLink((Classification) classification)
		  .withValue(value)
		  .inActiveRange(systems.getEnterpriseID(), identityToken)
		  .inDateRange();
		
		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		
		if(withParent != null)
		{
			JoinExpression<Arrangement,Arrangement,?> joinExpression = new JoinExpression<>();
			ArrangementXArrangementQueryBuilder builder =
					new ArrangementXArrangement()
							.builder()
							.inActiveRange(classification.getEnterprise())
							.inDateRange()
							.where(ArrangementXArrangement_.parentArrangementID, Equals, (Arrangement) withParent);
			if (!Strings.isNullOrEmpty(value))
			{
				builder.where(ArrangementXClassification_.value, Equals, value);
			}
			
			aqb.join(Arrangement_.arrangementXArrangementList,
			         builder,
			         JoinType.INNER, joinExpression);
		}
		
		aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);
	
		List<Arrangement> arrangementList = aqb.getAll();
		//noinspection unchecked
		return (List)arrangementList;
	}
	
	@CacheResult(cacheName = "ArrangementArrangementType")
	@Override
	public IArrangementType<?> find(@CacheKey IArrangementTypes<?> idType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens)
	{
		ArrangementType xr = new ArrangementType();
		return xr.builder()
		         .withName(idType.classificationValue())
		         .inActiveRange(enterprise, tokens)
		         .inDateRange()
		         .withEnterprise(enterprise)
		         .canRead(enterprise, tokens)
		         .get()
		         .orElseThrow(() -> new ArrangementException("Cannot find active or visible arrangement type - " + idType.classificationValue()));
	}
	
	@CacheResult(cacheName = "ArrangementArrangementTypeString")
	@Override
	public IArrangementType<?> find(@CacheKey String idType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens)
	{
		ArrangementType xr = new ArrangementType();
		return xr.builder()
		         .withName(idType)
		         .inActiveRange(enterprise, tokens)
		         .inDateRange()
		         .withEnterprise(enterprise)
		         .canRead(enterprise, tokens)
		         .get()
		         .orElseThrow(() -> new ArrangementException("Cannot find active or visible arrangement type - " + idType));
	}
	
	@Override
	public IArrangement<?> find(UUID id, IEnterprise<?> enterprise, UUID... tokens)
	{
		Arrangement xr = new Arrangement();
		return xr.builder()
		         .where(Arrangement_.id, Equals, id)
		         .inActiveRange(enterprise, tokens)
		         .inDateRange()
		         .canRead(enterprise, tokens)
		         .get()
		         .orElseThrow(() -> new ArrangementException("Cannot find active or visible arrangement with ID " + id));
	}
	
	@Override
	public IArrangement<?> find(UUID id)
	{
		Arrangement xr = new Arrangement();
		return xr.builder()
		         .where(Arrangement_.id, Equals, id)
		         .get()
		         .orElseThrow(() -> new ArrangementException("Cannot find active or visible arrangement with ID " + id));
	}
	
	@Override
	public List<IArrangement<?>> findAll(IArrangementTypes<?> idType, IEnterprise<?> enterprise, UUID... identityToken)
	{
		IArrangementType<?> type = find(idType, enterprise, identityToken);
		List<ArrangementXArrangementType> arrs = new ArrangementXArrangementType().builder()
		                                                                          .inActiveRange(enterprise)
		                                                                          .inDateRange()
		                                                                          .canRead(enterprise, identityToken)
		                                                                          .findChildLink((ArrangementType) type)
		                                                                          .getAll();
		List<IArrangement<?>> arrOut = new ArrayList<>();
		for (ArrangementXArrangementType arr : arrs)
		{
			arrOut.add(arr.getArrangement());
		}
		
		return arrOut;
	}
}
