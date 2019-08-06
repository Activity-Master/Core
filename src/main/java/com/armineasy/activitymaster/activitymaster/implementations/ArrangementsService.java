package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.activeflag.ActiveFlag;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.*;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXArrangementTypeQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.arrangement.builders.ArrangementXClassificationQueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.classifications.arrangement.IArrangementClassification;
import com.armineasy.activitymaster.activitymaster.services.classifications.enterprise.IEnterpriseName;
import com.armineasy.activitymaster.activitymaster.services.dto.*;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IArrangementTypes;
import com.armineasy.activitymaster.activitymaster.services.exceptions.ActivityMasterException;
import com.armineasy.activitymaster.activitymaster.services.system.*;
import com.armineasy.activitymaster.activitymaster.systems.InvolvedPartySystem;
import com.google.inject.Singleton;
import com.jwebmp.entityassist.querybuilder.builders.JoinExpression;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.persistence.criteria.JoinType;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.armineasy.activitymaster.activitymaster.services.classifications.classification.Classifications.*;
import static com.jwebmp.entityassist.enumerations.Operand.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

@Singleton
public class ArrangementsService
		implements IArrangementsService<ArrangementsService>
{
	@Override
	public IArrangement<?> create(IArrangementTypes<?> type, String arrangementTypeValue,
	                              ISystems system,
	                              UUID... identityToken)
	{
		Arrangement xr = new Arrangement();
		xr.setSystemID((Systems) system);
		xr.setOriginalSourceSystemID((Systems) system);
		xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
		xr.setActiveFlagID((ActiveFlag)get(IActiveFlagService.class).getActiveFlag(system.getEnterprise(), identityToken));
		xr.persist();

		if (ActivityMasterConfiguration.get()
		                               .isSecurityEnabled())
		{
			xr.createDefaultSecurity(system, identityToken);
		}

		ArrangementXArrangementType xarxr = xr.addOrUpdate(NoClassification, (IArrangementTypes<?>) type,
		                                                   arrangementTypeValue, system, identityToken);

		if (ActivityMasterConfiguration.get()
		                               .isSecurityEnabled())
		{
			xarxr.createDefaultSecurity(system, identityToken);
		}

		return xr;
	}

	@Override
	public IArrangementType<?> createArrangementType(IArrangementTypes<?> type, ISystems<?> system, UUID... identityToken)
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
			xr.setActiveFlagID((ActiveFlag)get(IActiveFlagService.class).getActiveFlag(system.getEnterprise(), identityToken));
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
		JoinExpression aje = new JoinExpression();

		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(systems.getEnterprise())
		  .findChildLink((Classification) classification)
		  .withValue(value)
		  .inActiveRange(systems.getEnterpriseID(), identityToken)
		  .inDateRange();

		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);

		List<Arrangement> arrangementList = aqb.getAll();
		return (List)arrangementList;
	}

	@CacheResult(cacheName = "ArrangementArrangementType")
	@Override
	public IArrangementType<?> find(@CacheKey IArrangementTypes<?> idType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens)
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

	@Override
	public IArrangement<?> find(long id, IEnterprise<?> enterprise, UUID... tokens)
	{
		Arrangement xr = new Arrangement();
		return xr.builder()
		         .where(Arrangement_.id, Equals, id)
		         .inActiveRange(enterprise, tokens)
		         .inDateRange()
		         .canRead(enterprise, tokens)
		         .get()
		         .orElseThrow(() -> new ActivityMasterException("No Read Access or No Item Found"));
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

	@Override
	public Double sumAll(IArrangementTypes<?> idType, IArrangementClassification<?> classificationValue,
	                     IEnterpriseName<?> enterpriseName, UUID... identityToken)
	{
		IEnterprise<?> enterprise = get(IEnterpriseService.class).getEnterprise(enterpriseName);
		IArrangementType<?> type = find(idType, enterprise, identityToken);
		IClassificationService<?> classificationService = get(IClassificationService.class);
		IClassification<?> classification = classificationService.find(classificationValue, enterprise, identityToken);
		ISystems<?> systems = InvolvedPartySystem.getNewSystem()
		                                         .get(enterprise);

		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(systems.getEnterprise())
		   .inActiveRange(systems.getEnterpriseID(), identityToken)
		   .inDateRange();

		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(systems.getEnterprise())
		  .findChildLink((Classification) classification)
		  //.withValue(value)
		  .inActiveRange(systems.getEnterpriseID(), identityToken)
		  .inDateRange();

		ArrangementXArrangementTypeQueryBuilder tqb = new ArrangementXArrangementType().builder();
		tqb.withEnterprise(systems.getEnterprise())
		  .findChildLink((ArrangementType) type)
		  .inActiveRange(systems.getEnterpriseID(), identityToken)
		  .inDateRange();

		JoinExpression aje = new JoinExpression();
		JoinExpression ate = new JoinExpression();

		qb.join(ArrangementXClassification_.arrangementID, aqb, JoinType.INNER, aje);
		ate.setGeneratedRoot(aje.getGeneratedRoot());
		aqb.join(Arrangement_.types, tqb, JoinType.INNER, ate);

		/*Long sumOfAllThings = qb.selectSum(ArrangementXClassification_.value)
		                        .get(Long.class).get();*/
		List<ArrangementXClassification> results = qb.getAll();
		Set<ArrangementXClassification> resultsDistinct = new LinkedHashSet<>(results);
		double d = 0.0d;
		for (ArrangementXClassification arrangementXClassification : resultsDistinct)
		{
			if (LocalDateTime.now()
			                 .isAfter(arrangementXClassification.getEffectiveToDate()))
			{
				continue;
			}
			d += arrangementXClassification.getValueAsDouble();
		}
		return d;
	}
}
