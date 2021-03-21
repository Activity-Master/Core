package com.guicedee.activitymaster.core;

import com.entityassist.enumerations.OrderByType;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.client.services.IArrangementsService;
import com.guicedee.activitymaster.client.services.IClassificationService;
import com.guicedee.activitymaster.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.client.services.builders.warehouse.arrangements.IArrangementType;
import com.guicedee.activitymaster.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.client.services.builders.warehouse.rules.IRulesType;
import com.guicedee.activitymaster.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.client.services.exceptions.ArrangementException;
import com.guicedee.activitymaster.core.db.entities.arrangement.*;
import com.guicedee.activitymaster.core.db.entities.arrangement.builders.*;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.rules.RulesType;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.persistence.criteria.JoinType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.entityassist.SCDEntity.*;
import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.client.services.classifications.DefaultClassifications.*;


public class ArrangementsService
		implements IArrangementsService<ArrangementsService>
{
	@Inject
	private IClassificationService<?> classificationService;
	
	@Inject
	@Named("Active")
	private IActiveFlag<?,?> activeFlag;
	
	@Inject
	private IEnterprise<?,?> enterprise;
	
	@Override
	public IArrangement<?, ?> create(String type,
	                                 String arrangementTypeClassification,
	                                 String arrangementTypeValue,
	                                 ISystems<?,?> system,
	                                 UUID... identityToken)
	{
		ArrangementType tt = (ArrangementType) find(type, system);
		boolean exists = new ArrangementXArrangementType().builder()
		                                                  .withValue(arrangementTypeValue)
		                                                  .inActiveRange(enterprise, identityToken)
		                                                  .inDateRange()
		                                                  .where(ArrangementXArrangementType_.type, Equals, tt)
		                                                  //  .where(ArrangementXArrangementType_.effectiveFromDate, Equals, createdDate)
		                                                  .withClassification(arrangementTypeClassification, system)
		                                                  .withEnterprise(enterprise)
		                                                  .getCount() > 0;
		if (exists)
		{
			return new ArrangementXArrangementType().builder()
			                                        .withValue(arrangementTypeValue)
			                                        .inActiveRange(enterprise, identityToken)
			                                        .inDateRange()
			                                        .where(ArrangementXArrangementType_.type, Equals, tt)
			                                        //.where(ArrangementXArrangementType_.effectiveFromDate, Equals, createdDate)
			                                        .withClassification(arrangementTypeClassification, system)
			                                        .withEnterprise(enterprise)
			                                        .get()
			                                        .orElseThrow()
			                                        .getArrangement();
		}
		
		Arrangement xr = new Arrangement();
		xr.setSystemID(system);
		xr.setOriginalSourceSystemID(system);
		xr.setEnterpriseID(enterprise);
		xr.setActiveFlagID(activeFlag);
		xr.persist();
		
		xr.createDefaultSecurity(system, identityToken);
		
		IArrangementType<?, ?> arrangementType = findArrangementType(type, system, identityToken);
		
		var xarxr = xr.addOrUpdateArrangementType(arrangementTypeClassification, arrangementType,
				arrangementTypeValue,arrangementTypeValue, system, identityToken);
		
		//xarxr.createDefaultSecurity(system, identityToken);
		return xr;
	}
	
	@Override
	@CacheResult(cacheName = "ArrangementTypes")
	public IArrangementType<?, ?> createArrangementType(@CacheKey String type, @CacheKey ISystems<?,?> system, @CacheKey UUID... identityToken)
	{
		ArrangementType xr = new ArrangementType();
		
		boolean exists = xr.builder()
		                   .withName(type)
		                   .inActiveRange(enterprise, identityToken)
		                   .inDateRange()
		                   .withEnterprise(enterprise)
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(type);
			xr.setDescription(type);
			xr.setSystemID(system);
			xr.setOriginalSourceSystemID(system);
			xr.setEnterpriseID(enterprise);
			xr.setActiveFlagID(activeFlag);
			xr.persist();
			xr.createDefaultSecurity(system, identityToken);
			
		}
		else
		{
			return find(type, system, identityToken);
		}
		return xr;
	}
	
	@Override
	public IArrangementType<?, ?> findArrangementType(String type, ISystems<?,?> system, UUID...identityToken)
	{
		ArrangementType xr = new ArrangementType();
		return  xr.builder()
		                   .withName(type)
		                   .inActiveRange(enterprise, identityToken)
		                   .inDateRange()
		                   .withEnterprise(enterprise)
		                   .get()
				.orElseThrow(()-> new ArrangementException("Unable to find arrangement type - " + type));
	}
	
	@Override
	public List<IArrangement<?, ?>> findInvolvedPartyArrangements(IInvolvedParty<?, ?> ip, String arrType, ISystems<?,?> systems, UUID... identityToken)
	{
		List<ArrangementXInvolvedParty> xips =
				new ArrangementXInvolvedParty()
						.builder()
						.withEnterprise(enterprise)
						.findLink(null, (InvolvedParty) ip, null)
						.withValue(arrType)
						.inActiveRange(enterprise, identityToken)
						.inDateRange()
						.getAll();
		
		return xips.stream()
		           .map(ArrangementXInvolvedParty::getArrangementID)
		           .filter(a -> LocalDateTime.now()
		                                     .isBefore(a.getEffectiveToDate()))
		           .collect(Collectors.toList());
	}
	
	@Override
	public List<IArrangement<?, ?>> findArrangementsByClassification(String classificationName, String value, ISystems<?,?> systems, UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(classificationName, systems, identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(enterprise)
		   .inActiveRange(enterprise, identityToken)
		   .inDateRange();
		JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();
		
		
		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(enterprise)
		  .withClassification(classification)
		  .withValue(value)
		  .inActiveRange(enterprise, identityToken)
		  .inDateRange();
		
		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		
		aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);
		
		List<Arrangement> arrangementList = aqb.getAll();
		return (List) arrangementList;
	}
	
	@Override
	public List<IArrangement<?, ?>> findArrangementsByClassificationGT(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?,?> systems, UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(arrType, systems, identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(enterprise)
		   .inActiveRange(enterprise, identityToken)
		   .inDateRange();
		JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();
		
		
		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(enterprise)
		  .withClassification(classification)
		  .withValue(GreaterThan, value)
		  .inActiveRange(enterprise, identityToken)
		  .inDateRange();
		
		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		
		if (withParent != null)
		{
			JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
			ArrangementXArrangementQueryBuilder builder =
					new ArrangementXArrangement()
							.builder()
							.inActiveRange(enterprise)
							.inDateRange()
							.where(ArrangementXArrangement_.parentArrangementID, Equals, (Arrangement) withParent);
			aqb.join(Arrangement_.arrangementXArrangementList,
					builder,
					JoinType.INNER, joinExpression);
		}
		
		aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);
		
		List<Arrangement> arrangementList = aqb.getAll();
		//noinspection unchecked
		return (List) arrangementList;
	}
	
	@Override
	public List<IArrangement<?, ?>> findArrangementsByClassificationGTE(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?,?> systems, UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(arrType, systems, identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(enterprise)
		   .inActiveRange(enterprise, identityToken)
		   .inDateRange();
		JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();
		
		
		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(enterprise)
		  .withClassification(classification)
		  .withValue(GreaterThanEqualTo, value)
		  .inActiveRange(enterprise, identityToken)
		  .inDateRange();
		
		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		
		if (withParent != null)
		{
			JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
			ArrangementXArrangementQueryBuilder builder =
					new ArrangementXArrangement()
							.builder()
							.inActiveRange(enterprise)
							.inDateRange()
							.where(ArrangementXArrangement_.parentArrangementID, Equals, (Arrangement) withParent);
			aqb.join(Arrangement_.arrangementXArrangementList,
					builder,
					JoinType.INNER, joinExpression);
		}
		
		aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);
		
		List<Arrangement> arrangementList = aqb.getAll();
		//noinspection unchecked
		return (List) arrangementList;
	}
	
	@Override
	public List<IArrangement<?, ?>> findArrangementsByClassificationGTEWithIP(String arrangementType, String classificationName,
	                                                                          IInvolvedParty<?, ?> withInvolvedParty,
	                                                                          String ipClassification,
	                                                                          IArrangement<?, ?> withParent,
	                                                                          IResourceItem<?, ?> resourceItem,
	                                                                          String resourceItemClassification,
	                                                                          String value, ISystems<?,?> system,
	                                                                          UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(enterprise)
		   .inActiveRange(enterprise, identityToken)
		   .inDateRange();
		
		if (classificationName != null)
		{
			JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();
			
			ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
			qb.withEnterprise(enterprise)
			  .withClassification(classification)
			  .withValue(GreaterThanEqualTo, value)
			  .inActiveRange(enterprise, identityToken)
			  .inDateRange();
			aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		}
		
		if (withInvolvedParty != null)
		{
			JoinExpression<Arrangement, InvolvedParty, ?> joinExpression = new JoinExpression<>();
			if (Strings.isNullOrEmpty(ipClassification))
			{
				ipClassification = NoClassification.toString();
			}
			ArrangementXInvolvedPartyQueryBuilder builder =
					new ArrangementXInvolvedParty()
							.builder()
							.inActiveRange(enterprise)
							.withClassification(ipClassification, system)
							.inDateRange()
							.where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) withInvolvedParty);
			aqb.join(Arrangement_.parties,
					builder,
					JoinType.INNER, joinExpression);
			
		}
		
		if (resourceItem != null)
		{
			JoinExpression<Arrangement, ResourceItem, ?> joinExpression = new JoinExpression<>();
			if (Strings.isNullOrEmpty(resourceItemClassification))
			{
				resourceItemClassification = NoClassification.toString();
			}
			ArrangementXResourceItemQueryBuilder builder =
					new ArrangementXResourceItem()
							.builder()
							.inActiveRange(enterprise)
							.withClassification(resourceItemClassification, system)
							.inDateRange()
							.where(ArrangementXResourceItem_.resourceItemID, Equals, (ResourceItem) resourceItem);
			aqb.join(Arrangement_.resources,
					builder,
					JoinType.INNER, joinExpression);
			
		}
		
		if (!Strings.isNullOrEmpty(arrangementType))
		{
			JoinExpression<Arrangement, ArrangementType, ?> joinExpressionAt = new JoinExpression<>();
			ArrangementXArrangementTypeQueryBuilder builderAT =
					new ArrangementXArrangementType()
							.builder()
							.inActiveRange(enterprise)
							.withType(arrangementType, system, identityToken)
							.inDateRange();
			aqb.join(Arrangement_.types,
					builderAT,
					JoinType.INNER, joinExpressionAt);
			
			
		}
		
		if (withParent != null)
		{
			JoinExpression<Arrangement, Arrangement, ?> joinExpressionParentJoin = new JoinExpression<>();
			ArrangementXArrangementQueryBuilder builderParent =
					new ArrangementXArrangement()
							.builder()
							.inActiveRange(enterprise)
							.inDateRange()
							.where(ArrangementXArrangement_.parentArrangementID, Equals, (Arrangement) withParent);
			aqb.join(Arrangement_.arrangementXArrangementList,
					builderParent,
					JoinType.INNER, joinExpressionParentJoin);
		}
		
		
		aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);
		
		List<Arrangement> arrangementList = aqb.getAll();
		//noinspection unchecked
		return (List) arrangementList;
	}
	
	@Override
	public List<IArrangement<?, ?>> findArrangementsByClassificationLT(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?,?> systems, UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(arrType, systems, identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(enterprise)
		   .inActiveRange(enterprise, identityToken)
		   .inDateRange();
		JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();
		
		
		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(enterprise)
		  .withClassification(classification)
		  .withValue(LessThan, value)
		  .inActiveRange(enterprise, identityToken)
		  .inDateRange();
		
		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		
		if (withParent != null)
		{
			JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
			ArrangementXArrangementQueryBuilder builder =
					new ArrangementXArrangement()
							.builder()
							.inActiveRange(enterprise)
							.inDateRange()
							.where(ArrangementXArrangement_.parentArrangementID, Equals, (Arrangement) withParent);
			aqb.join(Arrangement_.arrangementXArrangementList,
					builder,
					JoinType.INNER, joinExpression);
		}
		
		aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);
		
		List<Arrangement> arrangementList = aqb.getAll();
		//noinspection unchecked
		return (List) arrangementList;
	}
	
	@Override
	public List<IArrangement<?, ?>> findArrangementsByClassificationLTE(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?,?> systems, UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(arrType, systems, identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(enterprise)
		   .inActiveRange(enterprise, identityToken)
		   .inDateRange();
		JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();
		
		
		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(enterprise)
		  .withClassification(classification)
		  .withValue(LessThanEqualTo, value)
		  .inActiveRange(enterprise, identityToken)
		  .inDateRange();
		
		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		
		if (withParent != null)
		{
			JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
			ArrangementXArrangementQueryBuilder builder =
					new ArrangementXArrangement()
							.builder()
							.inActiveRange(enterprise)
							.inDateRange()
							.where(ArrangementXArrangement_.parentArrangementID, Equals, (Arrangement) withParent);
			aqb.join(Arrangement_.arrangementXArrangementList,
					builder,
					JoinType.INNER, joinExpression);
		}
		
		aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);
		
		List<Arrangement> arrangementList = aqb.getAll();
		//noinspection unchecked
		return (List) arrangementList;
	}
	
	@Override
	public List<IArrangement<?, ?>> findArrangementsByClassification(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?,?> systems, UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(arrType, systems, identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(enterprise)
		   .inActiveRange(enterprise, identityToken)
		   .inDateRange();
		JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();
		
		
		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(enterprise)
		  .withClassification(classification)
		  .withValue(value)
		  .inActiveRange(enterprise, identityToken)
		  .inDateRange();
		
		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		
		if (withParent != null)
		{
			JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
			ArrangementXArrangementQueryBuilder builder =
					new ArrangementXArrangement()
							.builder()
							.inActiveRange(enterprise)
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
		return (List) arrangementList;
	}
	
	@Override
	public IArrangement<?, ?> findArrangementByResourceItem(IResourceItem<?, ?> resourceItem, String classificationName, String value, ISystems<?,?> system, UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		Optional<ArrangementXResourceItem> arrangementXResourceItem = new ArrangementXResourceItem().builder()
		                                                                                            .inActiveRange(enterprise, identityToken)
		                                                                                            .inDateRange()
		                                                                                            .withEnterprise(enterprise)
		                                                                                            .withClassification(classification)
		                                                                                            .withValue(value)
		                                                                                            .where(ArrangementXResourceItem_.resourceItemID, Equals, (ResourceItem) resourceItem)
		                                                                                            .get();
		return arrangementXResourceItem.<IArrangement<?, ?>>map(ArrangementXResourceItem::getArrangementID).orElse(null);
	}
	
	@Override
	public IArrangement<?, ?> findArrangementByInvolvedParty(IInvolvedParty<?, ?> involvedParty, String classificationName, String value, ISystems<?,?> system, UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		Optional<ArrangementXInvolvedParty> arxip = new ArrangementXInvolvedParty().builder()
		                                                                           .inActiveRange(enterprise, identityToken)
		                                                                           .inDateRange()
		                                                                           .withEnterprise(enterprise)
		                                                                           .withClassification(classification)
		                                                                           .withValue(value)
		                                                                           .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
		                                                                           .get();
		return arxip.<IArrangement<?, ?>>map(ArrangementXInvolvedParty::getArrangementID).orElse(null);
	}
	
	@Override
	public List<IArrangement<?, ?>> findArrangementsByRulesType(IRulesType<?, ?> ruleType, String classificationName, String value, ISystems<?,?> system, UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		List collect = new ArrangementXRulesType().builder()
		                                          .inActiveRange(enterprise, identityToken)
		                                          .inDateRange()
		                                          .withEnterprise(enterprise)
		                                          .withClassification(classification)
		                                          .withValue(value)
		                                          .where(ArrangementXRulesType_.rulesTypeID, Equals, (RulesType) ruleType)
		                                          .get()
		                                          .map(ArrangementXRulesType::getArrangement)
		                                          .stream()
		                                          .collect(Collectors.toList());
		return collect;
	}
	
	@Override
	public List<IArrangement<?, ?>> findArrangementsByInvolvedParty(IInvolvedParty<?, ?> involvedParty, String classificationName, String value, LocalDateTime startDate, ISystems<?,?> system, UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		List<ArrangementXInvolvedParty> all = new ArrangementXInvolvedParty().builder()
		                                                                     .inActiveRange(enterprise, identityToken)
		                                                                     .inDateRange(startDate, EndOfTime)
		                                                                     .withEnterprise(enterprise)
		                                                                     .withClassification(classification)
		                                                                     .withValue(value)
		                                                                     .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
		                                                                     .getAll();
		return all.stream()
		          .map(ArrangementXInvolvedParty::getArrangementID)
		          .collect(Collectors.toList());
	}
	
	@Override
	public List<IArrangement<?, ?>> findArrangementsByInvolvedParty(IInvolvedParty<?, ?> involvedParty, String classificationName, String value, LocalDateTime startDate, LocalDateTime endDate, ISystems<?,?> system, UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		List<ArrangementXInvolvedParty> all = new ArrangementXInvolvedParty().builder()
		                                                                     .inActiveRange(enterprise, identityToken)
		                                                                     .inDateRange(startDate, endDate)
		                                                                     .withEnterprise(enterprise)
		                                                                     .withClassification(classification)
		                                                                     .withValue(value)
		                                                                     .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
		                                                                     .getAll();
		return all.stream()
		          .map(ArrangementXInvolvedParty::getArrangementID)
		          .collect(Collectors.toList());
	}
	
	@Override
	public List<IArrangement<?, ?>> findArrangementsByInvolvedParty(IInvolvedParty<?, ?> involvedParty, String classificationName, String value, ISystems<?,?> system, UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		List<ArrangementXInvolvedParty> arxip = new ArrangementXInvolvedParty().builder()
		                                                                       .inActiveRange(enterprise, identityToken)
		                                                                       .inDateRange()
		                                                                       .withEnterprise(enterprise)
		                                                                       .withClassification(classification)
		                                                                       .withValue(value)
		                                                                       .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
		                                                                       .getAll();
		return arxip.stream().<IArrangement<?, ?>>map(ArrangementXInvolvedParty::getArrangementID).collect(Collectors.toList());
	}
	
	@Override
	public List<IInvolvedParty<?, ?>> findArrangementInvolvedParties(IArrangement<?, ?> arrangement, String classificationName, String value, ISystems<?,?> system, UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		List<ArrangementXInvolvedParty> arxip = new ArrangementXInvolvedParty().builder()
		                                                                       .inActiveRange(enterprise, identityToken)
		                                                                       .inDateRange()
		                                                                       .withEnterprise(enterprise)
		                                                                       .withClassification(classification)
		                                                                       .withValue(value)
		                                                                       .where(ArrangementXInvolvedParty_.arrangementID, Equals, (Arrangement) arrangement)
		                                                                       .getAll();
		return arxip.stream().<IInvolvedParty<?, ?>>map(ArrangementXInvolvedParty::getInvolvedPartyID).collect(Collectors.toList());
	}
	
	@CacheResult(cacheName = "ArrangementArrangementTypeString")
	@Override
	public IArrangementType<?, ?> find(@CacheKey String idType, @CacheKey ISystems<?,?> system, @CacheKey UUID... tokens)
	{
		ArrangementType xr = new ArrangementType();
		return xr.builder()
		         .withName(idType)
		         .inActiveRange(enterprise, tokens)
		         .inDateRange()
		         .withEnterprise(enterprise)
		         //   .canRead(system, tokens)
		         .get()
		         .orElseThrow(() -> new ArrangementException("Cannot find active or visible arrangement type - " + idType));
	}
	
	@Override
	public IArrangement<?, ?> find(UUID id, ISystems<?,?> system, UUID... tokens)
	{
		Arrangement xr = new Arrangement();
		return xr.builder()
		         .where(Arrangement_.id, Equals, id)
		         .inActiveRange(enterprise, tokens)
		         .inDateRange()
		         //.canRead(system, tokens)
		         .get()
		         .orElseThrow(() -> new ArrangementException("Cannot find active or visible arrangement with ID " + id));
	}
	
	@Override
	public IArrangement<?, ?> find(UUID id)
	{
		Arrangement xr = new Arrangement();
		return xr.builder()
		         .where(Arrangement_.id, Equals, id)
		         .get()
		         .orElseThrow(() -> new ArrangementException("Cannot find active or visible arrangement with ID " + id));
	}
	
	@Override
	public List<IArrangement<?, ?>> findAll(String arrangementType, ISystems<?,?> system, UUID... identityToken)
	{
		IArrangementType<?, ?> type = find(arrangementType, system, identityToken);
		List<ArrangementXArrangementType> arrs = new ArrangementXArrangementType().builder()
		                                                                          .inActiveRange(enterprise)
		                                                                          .inDateRange()
		                                                                          .canRead(system, identityToken)
		                                                                          .findLink(null, (ArrangementType) type, null)
		                                                                          .getAll();
		List<IArrangement<?, ?>> arrOut = new ArrayList<>();
		for (ArrangementXArrangementType arr : arrs)
		{
			arrOut.add(arr.getArrangement());
		}
		
		return arrOut;
	}
	
	@Override
	public IArrangement<?, ?> completeArrangement(IArrangement<?, ?> arrangement, ISystems<?,?> system, UUID... identityToken)
	{
		Arrangement arr = (Arrangement) arrangement;
		arr.expireIn(Duration.ZERO);
		return arr;
	}
}
