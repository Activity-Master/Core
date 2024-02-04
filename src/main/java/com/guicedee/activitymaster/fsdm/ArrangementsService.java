package com.guicedee.activitymaster.fsdm;

import com.entityassist.enumerations.OrderByType;
import com.entityassist.querybuilder.builders.JoinExpression;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangement;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.arrangements.IArrangementType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.rules.IRulesType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ArrangementException;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.*;
import com.guicedee.activitymaster.fsdm.db.entities.arrangement.builders.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.rules.RulesType;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import javax.cache.annotation.CacheKey;
//import javax.cache.annotation.CacheResult;
import jakarta.persistence.criteria.JoinType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.entityassist.SCDEntity.*;
import static com.entityassist.enumerations.Operand.*;
import static com.entityassist.enumerations.OrderByType.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;


public class ArrangementsService
		implements IArrangementsService<ArrangementsService>
{
	@Inject
	private IClassificationService<?> classificationService;
	
	@Inject
	private IEnterprise<?, ?> enterprise;
	
	@Override
	public IArrangement<?, ?> get()
	{
		return new Arrangement();
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IArrangement<?, ?> create(String type,
	                                 String arrangementTypeClassification,
	                                 String arrangementTypeValue,
	                                 ISystems<?, ?> system,
	                                 java.util.UUID... identityToken)
	{
		return create(type, null, arrangementTypeClassification, arrangementTypeValue, system, identityToken);
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IArrangement<?, ?> create(String type, java.lang.String key,
	                                 String arrangementTypeClassification,
	                                 String arrangementTypeValue,
	                                 ISystems<?, ?> system,
	                                 java.util.UUID... identityToken)
	{
		ArrangementType arrangementType = (ArrangementType) find(type, system);
		Arrangement xr = new Arrangement();
		xr.setId(key);
		xr.setSystemID(system);
		xr.setOriginalSourceSystemID(system);
		xr.setEnterpriseID(arrangementType.getEnterpriseID());
		IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
		IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(arrangementType.getEnterprise());
		xr.setActiveFlagID(activeFlag);
		xr.persist();
		
		xr.createDefaultSecurity(system, identityToken);
		
		var xarxr =
				xr.addOrUpdateArrangementType(arrangementTypeClassification, arrangementType,
						arrangementTypeValue, arrangementTypeValue, system, identityToken);
		
		return xr;
	}
	
	@Override
	public IArrangementType<?, ?> createArrangementType(String type, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return createArrangementType(type, null, system, identityToken);
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	//@CacheResult(cacheName = "ArrangementTypes")
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IArrangementType<?, ?> createArrangementType(@CacheKey String type, java.lang.String key, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		ArrangementType xr = new ArrangementType();
		xr.setId(key);
		xr.setName(type);
		xr.setDescription(type);
		xr.setSystemID(system);
		xr.setOriginalSourceSystemID(system);
		xr.setEnterpriseID(enterprise);
		IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
		IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(enterprise);
		xr.setActiveFlagID(activeFlag);
		xr.persist();
		xr.createDefaultSecurity(system, identityToken);
		
		return xr;
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IArrangementType<?, ?> findArrangementType(String type, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		ArrangementType xr = new ArrangementType();
		return xr.builder()
		         .withName(type)
		         .inActiveRange()
		         .inDateRange()
		         .withEnterprise(enterprise)
		         .get()
		         .orElseThrow(() -> new ArrangementException("Unable to find arrangement type - " + type));
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IArrangement<?, ?>> findInvolvedPartyArrangements(IInvolvedParty<?, ?> ip, String arrType, ISystems<?, ?> systems, java.util.UUID... identityToken)
	{
		List<ArrangementXInvolvedParty> xips =
				new ArrangementXInvolvedParty()
						.builder()
						.withEnterprise(enterprise)
						.findLink(null, (InvolvedParty) ip, null)
						.withValue(arrType)
						.inActiveRange()
						.inDateRange()
						.getAll();
		
		return xips.stream()
		           .map(ArrangementXInvolvedParty::getArrangementID)
		           .filter(a -> QueryBuilderSCD.convertToUTCDateTime(com.entityassist.RootEntity.getNow())
		                                       .isBefore(a.getEffectiveToDate()))
		           .collect(Collectors.toList());
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IArrangement<?, ?>> findArrangementsByClassification(String classificationName, String value, ISystems<?, ?> systems, java.util.UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(classificationName, systems, identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(enterprise)
		   .inActiveRange()
		   .inDateRange();
		JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();
		
		
		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(enterprise)
		  .withClassification(classification)
		  .withValue(value)
		  .inActiveRange()
		  .inDateRange();
		
		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		
		aqb.orderBy(Arrangement_.effectiveFromDate, OrderByType.DESC);
		
		List<Arrangement> arrangementList = aqb.getAll();
		return (List) arrangementList;
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IArrangement<?, ?>> findArrangementsByClassificationGT(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, java.util.UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(arrType, systems, identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(enterprise)
		   .inActiveRange()
		   .inDateRange();
		JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();
		
		
		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(enterprise)
		  .withClassification(classification)
		  .withValue(GreaterThan, value)
		  .inActiveRange()
		  .inDateRange();
		
		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		
		if (withParent != null)
		{
			JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
			ArrangementXArrangementQueryBuilder builder =
					new ArrangementXArrangement()
							.builder()
							.inActiveRange()
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
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IArrangement<?, ?>> findArrangementsByClassificationGTE(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, java.util.UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(arrType, systems, identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(enterprise)
		   .inActiveRange()
		   .inDateRange();
		JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();
		
		
		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(enterprise)
		  .withClassification(classification)
		  .withValue(GreaterThanEqualTo, value)
		  .inActiveRange()
		  .inDateRange();
		
		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		
		if (withParent != null)
		{
			JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
			ArrangementXArrangementQueryBuilder builder =
					new ArrangementXArrangement()
							.builder()
							.inActiveRange()
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
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IArrangement<?, ?>> findArrangementsByClassificationGTEWithIP(String arrangementType, String classificationName,
	                                                                          IInvolvedParty<?, ?> withInvolvedParty,
	                                                                          String ipClassification,
	                                                                          IArrangement<?, ?> withParent,
	                                                                          IResourceItem<?, ?> resourceItem,
	                                                                          String resourceItemClassification,
	                                                                          String value, ISystems<?, ?> system,
	                                                                          java.util.UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(enterprise)
		   .inActiveRange()
		   .inDateRange();
		
		if (classificationName != null)
		{
			JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();
			
			ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
			qb.withEnterprise(enterprise)
			  .withClassification(classification)
			  .withValue(GreaterThanEqualTo, value)
			  .inActiveRange()
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
							.inActiveRange()
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
							.inActiveRange()
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
							.inActiveRange()
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
							.inActiveRange()
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
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IArrangement<?, ?>> findArrangementsByClassificationLT(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, java.util.UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(arrType, systems, identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(enterprise)
		   .inActiveRange()
		   .inDateRange();
		JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();
		
		
		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(enterprise)
		  .withClassification(classification)
		  .withValue(LessThan, value)
		  .inActiveRange()
		  .inDateRange();
		
		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		
		if (withParent != null)
		{
			JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
			ArrangementXArrangementQueryBuilder builder =
					new ArrangementXArrangement()
							.builder()
							.inActiveRange()
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
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IArrangement<?, ?>> findArrangementsByClassificationLTE(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, java.util.UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(arrType, systems, identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(enterprise)
		   .inActiveRange()
		   .inDateRange();
		JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();
		
		
		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(enterprise)
		  .withClassification(classification)
		  .withValue(LessThanEqualTo, value)
		  .inActiveRange()
		  .inDateRange();
		
		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		
		if (withParent != null)
		{
			JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
			ArrangementXArrangementQueryBuilder builder =
					new ArrangementXArrangement()
							.builder()
							.inActiveRange()
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
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IArrangement<?, ?>> findArrangementsByClassification(String arrType, IArrangement<?, ?> withParent, String value, ISystems<?, ?> systems, java.util.UUID... identityToken)
	{
		IClassification<?, ?> classification = classificationService.find(arrType, systems, identityToken);
		
		ArrangementQueryBuilder aqb = new Arrangement().builder();
		aqb.withEnterprise(enterprise)
		   .inActiveRange()
		   .inDateRange();
		JoinExpression<Arrangement, Classification, ?> aje = new JoinExpression<>();
		
		
		ArrangementXClassificationQueryBuilder qb = new ArrangementXClassification().builder();
		qb.withEnterprise(enterprise)
		  .withClassification(classification)
		  .withValue(value)
		  .inActiveRange()
		  .inDateRange();
		
		aqb.join(Arrangement_.classifications, qb, JoinType.INNER, aje);
		
		if (withParent != null)
		{
			JoinExpression<Arrangement, Arrangement, ?> joinExpression = new JoinExpression<>();
			ArrangementXArrangementQueryBuilder builder =
					new ArrangementXArrangement()
							.builder()
							.inActiveRange()
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
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IArrangement<?, ?> findArrangementByResourceItem(IResourceItem<?, ?> resourceItem, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		Optional<ArrangementXResourceItem> arrangementXResourceItem = new ArrangementXResourceItem().builder()
		                                                                                            .inActiveRange()
		                                                                                            .inDateRange()
		                                                                                            .withEnterprise(enterprise)
		                                                                                            .withClassification(classification)
		                                                                                            .withValue(value)
		                                                                                            .where(ArrangementXResourceItem_.resourceItemID, Equals, (ResourceItem) resourceItem)
		                                                                                            .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
		                                                                                            .get();
		return arrangementXResourceItem.<IArrangement<?, ?>>map(ArrangementXResourceItem::getArrangementID)
		                               .orElse(null);
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IArrangement<?, ?> findArrangementByInvolvedParty(IInvolvedParty<?, ?> involvedParty, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		Optional<ArrangementXInvolvedParty> arxip = new ArrangementXInvolvedParty().builder()
		                                                                           .inActiveRange()
		                                                                           .inDateRange()
		                                                                           .withEnterprise(enterprise)
		                                                                           .withClassification(classification)
		                                                                           .withValue(value)
		                                                                           .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
		                                                                           .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
		                                                                           .get();
		return arxip.<IArrangement<?, ?>>map(ArrangementXInvolvedParty::getArrangementID)
		            .orElse(null);
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IArrangement<?, ?>> findArrangementsByRulesType(IRulesType<?, ?> ruleType, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		List collect = new ArrangementXRulesType().builder()
		                                          .inActiveRange()
		                                          .inDateRange()
		                                          .withEnterprise(enterprise)
		                                          .withClassification(classification)
		                                          .withValue(value)
		                                          .where(ArrangementXRulesType_.rulesTypeID, Equals, (RulesType) ruleType)
		                                          .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
		                                          .get()
		                                          .map(ArrangementXRulesType::getArrangement)
		                                          .stream()
		                                          .collect(Collectors.toList());
		return collect;
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IArrangement<?, ?>> findArrangementsByInvolvedParty(IInvolvedParty<?, ?> involvedParty, String classificationName, String value, LocalDateTime startDate, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		List<ArrangementXInvolvedParty> all = new ArrangementXInvolvedParty().builder()
		                                                                     .inActiveRange()
		                                                                     .inDateRange(startDate, EndOfTime)
		                                                                     .withEnterprise(enterprise)
		                                                                     .withClassification(classification)
		                                                                     .withValue(value)
		                                                                     .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
		                                                                     .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
		                                                                     .getAll();
		return all.stream()
		          .map(ArrangementXInvolvedParty::getArrangementID)
		          .collect(Collectors.toList());
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IArrangement<?, ?>> findArrangementsByInvolvedParty(IInvolvedParty<?, ?> involvedParty, String classificationName, String value, LocalDateTime startDate, LocalDateTime endDate, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		List<ArrangementXInvolvedParty> all = new ArrangementXInvolvedParty().builder()
		                                                                     .inActiveRange()
		                                                                     .inDateRange(startDate, endDate)
		                                                                     .withEnterprise(enterprise)
		                                                                     .withClassification(classification)
		                                                                     .withValue(value)
		                                                                     .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
		                                                                     .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
		                                                                     .getAll();
		return all.stream()
		          .map(ArrangementXInvolvedParty::getArrangementID)
		          .collect(Collectors.toList());
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IArrangement<?, ?>> findArrangementsByInvolvedParty(IInvolvedParty<?, ?> involvedParty, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		List<ArrangementXInvolvedParty> arxip = new ArrangementXInvolvedParty().builder()
		                                                                       .inActiveRange()
		                                                                       .inDateRange()
		                                                                       .withEnterprise(enterprise)
		                                                                       .withClassification(classification)
		                                                                       .withValue(value)
		                                                                       .where(ArrangementXInvolvedParty_.involvedPartyID, Equals, (InvolvedParty) involvedParty)
		                                                                       .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
		                                                                       .getAll();
		return arxip.stream()
		            .<IArrangement<?, ?>>map(ArrangementXInvolvedParty::getArrangementID)
		            .collect(Collectors.toList());
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IInvolvedParty<?, ?>> findArrangementInvolvedParties(IArrangement<?, ?> arrangement, String classificationName, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (Strings.isNullOrEmpty(classificationName))
		{
			classificationName = NoClassification.toString();
		}
		IClassification<?, ?> classification = classificationService.find(classificationName, system, identityToken);
		List<ArrangementXInvolvedParty> arxip = new ArrangementXInvolvedParty().builder()
		                                                                       .inActiveRange()
		                                                                       .inDateRange()
		                                                                       .withEnterprise(enterprise)
		                                                                       .withClassification(classification)
		                                                                       .withValue(value)
		                                                                       .where(ArrangementXInvolvedParty_.arrangementID, Equals, (Arrangement) arrangement)
		                                                                       .orderBy(ArrangementXInvolvedParty_.effectiveFromDate, DESC)
		                                                                       .getAll();
		return arxip.stream()
		            .<IInvolvedParty<?, ?>>map(ArrangementXInvolvedParty::getInvolvedPartyID)
		            .collect(Collectors.toList());
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	//@CacheResult(cacheName = "ArrangementArrangementTypeString")
	@Override
	public IArrangementType<?, ?> find(@CacheKey String idType, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		ArrangementType xr = new ArrangementType();
		return xr.builder()
		         .withName(idType)
		         .inActiveRange()
		         .inDateRange()
		         .withEnterprise(enterprise)
		         //   .canRead(system, tokens)
		         .get()
		         .orElseThrow(() -> new ArrangementException("Cannot find active or visible arrangement type - " + idType));
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	//@CacheResult
	public IArrangement<?, ?> find(@CacheKey java.util.UUID id, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		Arrangement xr = new Arrangement();
		return xr.builder()
		         .where(Arrangement_.id, Equals, id.toString())
		         .get()
		         .orElseThrow(() -> new ArrangementException("Cannot find active or visible arrangement with ID " + id));
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	//@CacheResult
	public IArrangement<?, ?> find(@CacheKey java.lang.String id)
	{
		Arrangement xr = new Arrangement();
		return xr.builder()
		         .where(Arrangement_.id, Equals, id)
		         .get()
		         .orElse(null);
	}
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public List<IArrangement<?, ?>> findAll(String arrangementType, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		IArrangementType<?, ?> type = find(arrangementType, system, identityToken);
		List<ArrangementXArrangementType> arrs = new ArrangementXArrangementType().builder()
		                                                                          .inActiveRange()
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
	public IArrangement<?, ?> completeArrangement(IArrangement<?, ?> arrangement, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		Arrangement arr = (Arrangement) arrangement;
		arr.expireIn(Duration.ZERO);
		return arr;
	}
}
