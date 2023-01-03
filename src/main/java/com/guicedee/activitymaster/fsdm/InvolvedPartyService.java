package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.types.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.fsdm.client.types.exceptions.InvolvedPartyException;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;
import com.guicedee.logger.LogFactory;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.JoinType;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.entityassist.enumerations.Operand.*;
import static com.entityassist.enumerations.OrderByType.*;
import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.types.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.types.classifications.types.IdentificationTypes.*;

@SuppressWarnings("Duplicates")
public class InvolvedPartyService
		implements IInvolvedPartyService<InvolvedPartyService>
{
	private static final Logger log = LogFactory.getLog("InvolvedPartyService");
	
	@Inject
	private IClassificationService<?> classificationService;
	
	@Inject
	@com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB
	private EntityManager entityManager;
	
	@Override
	public IInvolvedParty<?, ?> get()
	{
		return new InvolvedParty();
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	@CacheResult(cacheName = "InvolvedPartyByID")
	public IInvolvedParty<?, ?> findByID(@CacheKey UUID id)
	{
		return new InvolvedParty().builder(entityManager)
		                          .find(id.toString())
		                          .get()
		                          .orElseThrow();
	}
	
	@Override
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IInvolvedPartyNameType<?, ?> createNameType(String name, String description, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		InvolvedPartyNameType xr = new InvolvedPartyNameType();
		
		boolean exists = xr.builder(entityManager)
		                   .withName(name)
		                   .inActiveRange()
		                   .inDateRange()
		                   .withEnterprise(system.getEnterpriseID())
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID(system);
			xr.setOriginalSourceSystemID(system);
			xr.setEnterpriseID(system.getEnterpriseID());
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(system.getEnterpriseID());
			xr.setActiveFlagID(activeFlag);
			xr.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
			
			xr.createDefaultSecurity(system, identityToken);
			
		}
		else
		{
			return findInvolvedPartyNameType(name, system, identityToken);
		}
		
		return xr;
	}
	
	
	@Override
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IInvolvedPartyIdentificationType<?, ?> createIdentificationType(ISystems<?, ?> system, String name, String description, java.util.UUID... identityToken)
	{
		InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
		
		boolean exists = xr.builder(entityManager)
		                   .withName(name)
		                   .inActiveRange()
		                   .inDateRange()
		                   .withEnterprise(system.getEnterpriseID())
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID(system);
			xr.setOriginalSourceSystemID(system);
			xr.setEnterpriseID(system.getEnterpriseID());
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(system.getEnterpriseID());
			xr.setActiveFlagID(activeFlag);
			xr.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
			xr.createDefaultSecurity(system, identityToken);
			
		}
		else
		{
			return findInvolvedPartyIdentificationType(name, system, identityToken);
		}
		return xr;
	}
	
	@Override
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IInvolvedPartyType<?, ?> createType(ISystems<?, ?> system, String name, String description, java.util.UUID... identityToken)
	{
		InvolvedPartyType xr = new InvolvedPartyType();
		
		boolean exists = xr.builder(entityManager)
		                   .withName(name)
		                   .inActiveRange()
		                   .inDateRange()
		                   .withEnterprise(system.getEnterpriseID())
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID(system);
			xr.setOriginalSourceSystemID(system);
			xr.setEnterpriseID(system.getEnterpriseID());
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(system.getEnterpriseID());
			xr.setActiveFlagID(activeFlag);
			xr.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
			ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName);
			//UUID activityMasterSystemUUID = IActivityMasterService.getISystemToken(ActivityMasterSystemName);
			xr.createDefaultSecurity(activityMasterSystem, identityToken);
		}
		else
		{
			return findType(name, system, identityToken);
		}
		
		return xr;
	}
	
	////@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public InvolvedPartyOrganicType createOrganicType(ISystems<?, ?> system, String key, String name, String description, java.util.UUID... identityToken)
	{
		InvolvedPartyOrganicType xr = new InvolvedPartyOrganicType();
		xr.setId(key);
		xr.setName(name);
		xr.setDescription(description);
		xr.setSystemID(system);
		xr.setOriginalSourceSystemID(system);
		xr.setEnterpriseID(system.getEnterpriseID());
		IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
		IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(system.getEnterpriseID());
		xr.setActiveFlagID(activeFlag);
		xr.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
		ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName);
		//UUID activityMasterSystemUUID = IActivityMasterService.getISystemToken(ActivityMasterSystemName);
		xr.createDefaultSecurity(activityMasterSystem, identityToken);
		
		return xr;
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "InvolvedPartyGetIdentificationTypeString")
	@Override
	public IInvolvedPartyIdentificationType<?, ?> findInvolvedPartyIdentificationType(@CacheKey String idType, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
		return xr.builder(entityManager)
		         .withName(idType)
		         .inActiveRange()
		         .inDateRange()
		         //     .canRead(enterprise.get(), tokens)
		         .get()
		         .orElseThrow(() -> new ActivityMasterException("No Read Access or No Item Found"));
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	@CacheResult(cacheName = "InvolvedPartyFindByIdentificationType")
	public IInvolvedParty<?, ?> findByResourceItem(@CacheKey IResourceItem<?, ?> idType, @CacheKey String value, ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		Optional<InvolvedPartyXResourceItem> builder = new InvolvedPartyXResourceItem()
				.builder(entityManager)
				.canRead(system, identityToken)
				.inActiveRange()
				.inDateRange()
				.findLink(null,
						(ResourceItem) idType,
						value)
				.setReturnFirst(true)
				.get();
		return builder.map(InvolvedPartyXResourceItem::getInvolvedPartyID)
		              .orElse(null);
	}
	
	@Override
	public IInvolvedParty<?, ?> create(ISystems<?, ?> system, Pair<String, String> idTypes,
	                                   boolean isOrganic, java.util.UUID... identityToken)
	{
		return create(system, null, idTypes, isOrganic, identityToken);
	}
	
	@Override
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IInvolvedParty<?, ?> create(ISystems<?, ?> system, java.lang.String key, Pair<String, String> idTypes,
	                                   boolean isOrganic, java.util.UUID... identityToken)
	{
		InvolvedParty ip = new InvolvedParty();
		ip.setEnterpriseID(system.getEnterpriseID());
		IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
		IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(system.getEnterpriseID());
		
		ip.setId(key);
		ip.setActiveFlagID(activeFlag);
		ip.setSystemID(system);
		ip.setOriginalSourceSystemID(system);
		ip.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
		
		ip.createDefaultSecurity(system, identityToken);
		
		IInvolvedPartyIdentificationType<?, ?> involvedPartyIdentificationType = findInvolvedPartyIdentificationType(idTypes.getKey(), system, identityToken);
		ip.addOrUpdateInvolvedPartyIdentificationType(NoClassification.toString(), involvedPartyIdentificationType, idTypes.getValue(), idTypes.getValue(), system, identityToken);
		InvolvedParty finalIp = ip;
		
		setupInvolvedPartyOrganicStatus(isOrganic, finalIp, system, identityToken);
		return ip;
	}
	
	private void setupInvolvedPartyOrganicStatus(boolean isOrganic, IInvolvedParty<?, ?> ip, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		if (isOrganic)
		{
			InvolvedPartyOrganic ipo = new InvolvedPartyOrganic();
			ipo.setInvolvedParty((InvolvedParty) ip);
			ipo.setId(ip.getId());
			ipo.setEnterpriseID(system.getEnterpriseID());
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(system.getEnterpriseID());
			ipo.setActiveFlagID(activeFlag);
			ipo.setSystemID(system);
			ipo.setOriginalSourceSystemID(system);
			ipo.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
			
			ipo.createDefaultSecurity(system, identityToken);
			
		}
		else
		{
			InvolvedPartyNonOrganic ipo = new InvolvedPartyNonOrganic();
			ipo.setInvolvedParty((InvolvedParty) ip);
			ipo.setId(ip.getId());
			ipo.setEnterpriseID(system.getEnterpriseID());
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?, ?> activeFlag = acService.getActiveFlag(system.getEnterpriseID());
			ipo.setActiveFlagID(activeFlag);
			ipo.setSystemID(system);
			ipo.setOriginalSourceSystemID(system);
			ipo.persist(com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration.entityManager().get());
			
			ipo.createDefaultSecurity(system, identityToken);
			
		}
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "InvolvedPartyFindTypeByString")
	@Override
	public IInvolvedPartyType<?, ?> findType(@CacheKey String nameType, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		InvolvedPartyType xr = new InvolvedPartyType();
		return xr.builder(entityManager)
		         .withName(nameType)
		         .inActiveRange()
		         .withEnterprise(system.getEnterpriseID())
		         .inDateRange()
		         //   .canRead(system, tokens)
		         .get()
		         .orElse(null);
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@CacheResult(cacheName = "InvolvedPartyGetNameTypeString")
	@Override
	public IInvolvedPartyNameType<?, ?> findInvolvedPartyNameType(@CacheKey String nameType, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		InvolvedPartyNameType xr = new InvolvedPartyNameType();
		return xr.builder(entityManager)
		         .withName(nameType)
		         .inActiveRange()
		         .inDateRange()
		         .withEnterprise(system.getEnterpriseID())
		         //   .canRead(system, tokens)
		         .get()
		         .orElse(null);
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	@CacheResult(cacheName = "InvolvedPartyFindByToken")
	public IInvolvedParty<?, ?> findByToken(@CacheKey ISecurityToken<?, ?> token, @CacheKey java.util.UUID... identityToken)
	{
		InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
		InvolvedPartyIdentificationType id = (InvolvedPartyIdentificationType) findInvolvedPartyIdentificationType(IdentificationTypeUUID.toString(), ((SecurityToken) token).getSystemID(), identityToken);
		Optional<InvolvedPartyXInvolvedPartyIdentificationType> foundLink = idType.builder(entityManager)
		                                                                          .findLink(null, id, token.getSecurityToken())
		                                                                          .inActiveRange()
		                                                                          .inDateRange()
		                                                                          .canRead(((SecurityToken) token).getSystemID(), identityToken)
		                                                                          .get();
		return foundLink.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
		                .orElse(null);
		
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IInvolvedParty<?, ?> find(@CacheKey UUID uuid)
	{
		return new InvolvedParty().builder(entityManager)
		                          .find(uuid.toString())
		                          .get()
		                          .orElseThrow(() -> new InvolvedPartyException("The IP does not exist - " + uuid));
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IInvolvedPartyType<?, ?> findType(@CacheKey UUID uuid)
	{
		return new InvolvedPartyType().builder(entityManager)
		                              .find(uuid.toString())
		                              .get()
		                              .orElseThrow(() -> new InvolvedPartyException("The IP Type does not exist - " + uuid));
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IInvolvedPartyNameType<?, ?> findNameType(@CacheKey UUID uuid)
	{
		return new InvolvedPartyNameType().builder(entityManager)
		                                  .find(uuid.toString())
		                                  .get()
		                                  .orElseThrow(() -> new InvolvedPartyException("The IP Name Type does not exist - " + uuid));
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IInvolvedPartyIdentificationType<?, ?> findIdentificationType(@CacheKey UUID uuid)
	{
		return new InvolvedPartyIdentificationType().builder(entityManager)
		                                            .find(uuid.toString())
		                                            .get()
		                                            .orElseThrow(() -> new InvolvedPartyException("The IP Name Type does not exist - " + uuid));
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	@CacheResult(cacheName = "InvolvedPartyFindByUUID")
	public IInvolvedParty<?, ?> findByUUID(@CacheKey UUID token, @CacheKey ISystems<?, ?> system, @CacheKey java.util.UUID... identityToken)
	{
		InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
		InvolvedPartyIdentificationType id = (InvolvedPartyIdentificationType) findInvolvedPartyIdentificationType(IdentificationTypeUUID.toString(), system, identityToken);
		
		Optional<InvolvedPartyXInvolvedPartyIdentificationType> foundLink = idType.builder(entityManager)
		                                                                          .findLink(null, id, token.toString())
		                                                                          .inActiveRange()
		                                                                          .inDateRange()
		                                                                          .withEnterprise(system.getEnterpriseID())
		                                                                          .canRead(system, identityToken)
		                                                                          .get();
		return foundLink.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
		                .orElse(null);
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public List<IRelationshipValue<IInvolvedParty<?, ?>, IInvolvedPartyIdentificationType<?, ?>, ?>> findAllByIdentificationType(String identificationType, String value)
	{
		InvolvedPartyIdentificationTypeQueryBuilder builder = new InvolvedPartyIdentificationType().builder(entityManager);
		builder.inDateRange()
		       .where(InvolvedPartyIdentificationType_.name, Equals, identificationType)
		;
		
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder ipQb = new InvolvedPartyXInvolvedPartyIdentificationType().builder(entityManager);
		if (value != null)
		{
			ipQb.withValue(value);
		}
		
		ipQb.inDateRange()
		    .orderBy(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, DESC)
		    .join(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, builder, JoinType.INNER);
		
		try
		{
			return (List) ipQb.getAll();
		}
		catch (Throwable t)
		{
			log.log(Level.WARNING, "Unable to find involved party for session");
			log.log(Level.FINE, "Unable to find involved party for session", t);
			return null;
		}
	}
	
	@Override
	public IInvolvedParty<?,?> findByIdentificationType(String identificationType, String value)
	{
		InvolvedPartyIdentificationTypeQueryBuilder builder = new InvolvedPartyIdentificationType().builder(entityManager);
		builder.inActiveRange();
		builder.inDateRange()
		       .where(InvolvedPartyIdentificationType_.name, Equals, identificationType)
		;
		
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder ipQb = new InvolvedPartyXInvolvedPartyIdentificationType().builder(entityManager);
		if (value != null)
		{
			ipQb.withValue(value);
		}
		
		ipQb.inDateRange()
				.inActiveRange()
		    .orderBy(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, DESC)
		    .join(InvolvedPartyXInvolvedPartyIdentificationType_.involvedPartyIdentificationTypeID, builder, JoinType.INNER);
		
		try
		{
			Optional<InvolvedPartyXInvolvedPartyIdentificationType> involvedPartyXInvolvedPartyIdentificationType = ipQb.get();
			if (involvedPartyXInvolvedPartyIdentificationType.isPresent())
			{
				return involvedPartyXInvolvedPartyIdentificationType.get()
				                                                    .getInvolvedPartyID();
			}
			return null;
		}
		catch (Throwable t)
		{
			log.log(Level.WARNING, "Unable to find involved party for session");
			log.log(Level.FINE, "Unable to find involved party for session", t);
			return null;
		}
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@SuppressWarnings("unchecked")
	@Override
	public List<IInvolvedParty<?, ?>> findByRulesClassification(String classification, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		IClassification<?, ?> classification1 = classificationService.find(classification, system, identityToken);
		
		@SuppressWarnings("rawtypes")
		List collect = new InvolvedPartyXRules().builder(entityManager)
		                                        .withClassification(classification1)
		                                        .withValue(value)
		                                        .inActiveRange()
		                                        .inDateRange()
		                                        .getAll()
		                                        .stream()
		                                        .map(InvolvedPartyXRules::getInvolvedPartyID)
		                                        .collect(Collectors.toList());
		return collect;
		
	}
	
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public IInvolvedParty<?, ?> findByClassification(String classification, String value, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		IClassification<?, ?> classification1 = classificationService.find(classification, system, identityToken);
		return new InvolvedPartyXClassification().builder(entityManager)
		                                         .withClassification(classification1)
		                                         .withValue(value)
		                                         .inActiveRange()
		                                         .inDateRange()
		                                         .get()
		                                         .orElse(null)
		                                         .getPrimary()
				;
		
	}
}
