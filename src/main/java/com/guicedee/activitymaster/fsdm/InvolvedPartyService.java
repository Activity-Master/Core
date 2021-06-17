package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.resourceitem.IResourceItem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.InvolvedPartyException;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.*;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.implementations.interceptors.EventsAOPInterceptor;
import com.guicedee.guicedinjection.GuiceContext;
import com.guicedee.guicedinjection.interfaces.JobService;
import com.guicedee.guicedinjection.pairing.Pair;
import com.guicedee.logger.LogFactory;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;
import jakarta.persistence.criteria.JoinType;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.entityassist.enumerations.Operand.*;
import static com.entityassist.enumerations.OrderByType.*;
import static com.guicedee.activitymaster.fsdm.SystemsService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes.*;

@SuppressWarnings("Duplicates")
public class InvolvedPartyService
		implements IInvolvedPartyService<InvolvedPartyService>
{
	private static final Logger log = LogFactory.getLog("InvolvedPartyService");
	
	@Inject
	private IEnterprise<?, ?> enterprise;
	
	@Inject
	private IClassificationService<?> classificationService;
	
	@Override
	public IInvolvedParty<?, ?> get()
	{
		return new InvolvedParty();
	}
	
	@Override
	@CacheResult(cacheName = "InvovledPartyByID")
	public IInvolvedParty<?, ?> findByID(@CacheKey UUID id)
	{
		return new InvolvedParty().builder()
		                          .find(id)
		                          .get()
		                          .orElseThrow();
	}
	
	@Override
	public IInvolvedPartyNameType<?, ?> createNameType(String name, String description, ISystems<?, ?> system, UUID... identityToken)
	{
		InvolvedPartyNameType xr = new InvolvedPartyNameType();
		
		boolean exists = xr.builder()
		                   .withName(name)
		                   .inActiveRange()
		                   .inDateRange()
		                   .withEnterprise(enterprise)
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID(system);
			xr.setOriginalSourceSystemID(system);
			xr.setEnterpriseID(enterprise);
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?,?> activeFlag = acService.getActiveFlag(enterprise);
			xr.setActiveFlagID(activeFlag);
			xr.persist();
			
			xr.createDefaultSecurity(system, identityToken);
			
		}
		else
		{
			return findInvolvedPartyNameType(name, system, identityToken);
		}
		
		return xr;
	}
	
	
	@Override
	public IInvolvedPartyIdentificationType<?, ?> createIdentificationType(ISystems<?, ?> system, String name, String description, UUID... identityToken)
	{
		InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
		
		boolean exists = xr.builder()
		                   .withName(name)
		                   .inActiveRange()
		                   .inDateRange()
		                   .withEnterprise(enterprise)
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID(system);
			xr.setOriginalSourceSystemID(system);
			xr.setEnterpriseID(enterprise);
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?,?> activeFlag = acService.getActiveFlag(enterprise);
			xr.setActiveFlagID(activeFlag);
			xr.persist();
			xr.createDefaultSecurity(system, identityToken);
			
		}
		else
		{
			return findInvolvedPartyIdentificationType(name, system, identityToken);
		}
		return xr;
	}
	
	@Override
	public IInvolvedPartyType<?, ?> createType(ISystems<?, ?> system, String name, String description, UUID... identityToken)
	{
		InvolvedPartyType xr = new InvolvedPartyType();
		
		boolean exists = xr.builder()
		                   .withName(name)
		                   .inActiveRange()
		                   .inDateRange()
		                   .withEnterprise(enterprise)
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID(system);
			xr.setOriginalSourceSystemID(system);
			xr.setEnterpriseID(enterprise);
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?,?> activeFlag = acService.getActiveFlag(enterprise);
			xr.setActiveFlagID(activeFlag);
			xr.persist();
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
	
	public InvolvedPartyOrganicType createOrganicType(ISystems<?, ?> system, String name, String description, UUID... identityToken)
	{
		InvolvedPartyOrganicType xr = new InvolvedPartyOrganicType();
		boolean exists = xr.builder()
		                   .withName(name)
		                   .inActiveRange()
		                   .inDateRange()
		                   .withEnterprise(enterprise)
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID(system);
			xr.setOriginalSourceSystemID(system);
			xr.setEnterpriseID(enterprise);
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?,?> activeFlag = acService.getActiveFlag(enterprise);
			xr.setActiveFlagID(activeFlag);
			xr.persist();
			ISystems<?, ?> activityMasterSystem = IActivityMasterService.getISystem(ActivityMasterSystemName);
			//UUID activityMasterSystemUUID = IActivityMasterService.getISystemToken(ActivityMasterSystemName);
			xr.createDefaultSecurity(activityMasterSystem, identityToken);
			
		}
		else
		{
			xr = xr.builder()
			       .withName(name)
			       .inActiveRange()
			       .inDateRange()
			       .withEnterprise(enterprise)
			       .get()
			       .orElseThrow();
		}
		
		return xr;
	}
	
	@CacheResult(cacheName = "InvolvedPartyGetIdentificationTypeString")
	@Override
	public IInvolvedPartyIdentificationType<?, ?> findInvolvedPartyIdentificationType(@CacheKey String idType, @CacheKey ISystems<?, ?> system, @CacheKey UUID... identityToken)
	{
		InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
		return xr.builder()
		         .withName(idType)
		         .inActiveRange()
		         .inDateRange()
		         //     .canRead(enterprise.get(), tokens)
		         .get()
		         .orElseThrow(() -> new ActivityMasterException("No Read Access or No Item Found"));
	}
	
	@Override
	@CacheResult(cacheName = "InvolvedPartyFindByIdentificationType")
	public IInvolvedParty<?, ?> findByResourceItem(@CacheKey IResourceItem<?, ?> idType, @CacheKey String value, ISystems<?, ?> system, @CacheKey UUID... tokens)
	{
		Optional<InvolvedPartyXResourceItem> builder = new InvolvedPartyXResourceItem()
				.builder()
				.canRead(system, tokens)
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
	                                   boolean isOrganic, UUID... identityToken)
	{
		InvolvedParty ip = new InvolvedParty();
		Optional<InvolvedParty> exists = ip.builder()
		                                   .withEnterprise(enterprise)
		                                   .findByIdentificationType(idTypes.getKey(), idTypes.getValue(), system, identityToken)
		                                   .get();
		
		if (exists.isEmpty())
		{
			ip.setEnterpriseID(enterprise);
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?,?> activeFlag = acService.getActiveFlag(enterprise);
			ip.setActiveFlagID(activeFlag);
			ip.setSystemID(system);
			ip.setOriginalSourceSystemID(system);
			ip.persist();
			
			ip.createDefaultSecurity(system, identityToken);
			
			IInvolvedPartyIdentificationType<?, ?> involvedPartyIdentificationType = findInvolvedPartyIdentificationType(idTypes.getKey(), system, identityToken);
			ip.addOrUpdateInvolvedPartyIdentificationType(NoClassification.toString(), involvedPartyIdentificationType, idTypes.getValue(), idTypes.getValue(), system, identityToken);
			InvolvedParty finalIp = ip;
			JobService.getInstance()
			          .addJob("InvolvedPartyOrganicStorage",
					          () -> setupInvolvedPartyOrganicStatus(isOrganic, finalIp, system, identityToken));
		}
		else
		{
			ip = exists.get();
		}
		
		return ip;
	}
	
	private void setupInvolvedPartyOrganicStatus(boolean isOrganic, IInvolvedParty<?, ?> ip, ISystems<?, ?> system, UUID... identityToken)
	{
		if (isOrganic)
		{
			InvolvedPartyOrganic ipo = new InvolvedPartyOrganic();
			ipo.setInvolvedParty((InvolvedParty) ip);
			ipo.setId(ip.getId());
			ipo.setEnterpriseID(enterprise);
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?,?> activeFlag = acService.getActiveFlag(enterprise);
			ipo.setActiveFlagID(activeFlag);
			ipo.setSystemID(system);
			ipo.setOriginalSourceSystemID(system);
			ipo.persist();
			
			ipo.createDefaultSecurity(system, identityToken);
			
		}
		else
		{
			InvolvedPartyNonOrganic ipo = new InvolvedPartyNonOrganic();
			ipo.setInvolvedParty((InvolvedParty) ip);
			ipo.setId(ip.getId());
			ipo.setEnterpriseID(enterprise);
			IActiveFlagService<?> acService = GuiceContext.get(IActiveFlagService.class);
			IActiveFlag<?,?> activeFlag = acService.getActiveFlag(enterprise);
			ipo.setActiveFlagID(activeFlag);
			ipo.setSystemID(system);
			ipo.setOriginalSourceSystemID(system);
			ipo.persist();
			
			ipo.createDefaultSecurity(system, identityToken);
			
		}
	}
	
	@CacheResult(cacheName = "InvolvedPartyFindTypeByString")
	@Override
	public IInvolvedPartyType<?, ?> findType(@CacheKey String nameType, @CacheKey ISystems<?, ?> system, @CacheKey UUID... tokens)
	{
		InvolvedPartyType xr = new InvolvedPartyType();
		return xr.builder()
		         .withName(nameType)
		         .inActiveRange()
		         .withEnterprise(enterprise)
		         .inDateRange()
		         //   .canRead(system, tokens)
		         .get()
		         .orElse(null);
	}
	
	@CacheResult(cacheName = "InvolvedPartyGetNameTypeString")
	@Override
	public IInvolvedPartyNameType<?, ?> findInvolvedPartyNameType(@CacheKey String nameType, @CacheKey ISystems<?, ?> system, @CacheKey UUID... tokens)
	{
		InvolvedPartyNameType xr = new InvolvedPartyNameType();
		return xr.builder()
		         .withName(nameType)
		         .inActiveRange()
		         .inDateRange()
		         .withEnterprise(enterprise)
		         //   .canRead(system, tokens)
		         .get()
		         .orElse(null);
	}
	
	@Override
	@CacheResult(cacheName = "InvolvedPartyFindByToken")
	public IInvolvedParty<?, ?> findByToken(@CacheKey ISecurityToken<?, ?> token, @CacheKey UUID... tokens)
	{
		InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
		InvolvedPartyIdentificationType id = (InvolvedPartyIdentificationType) findInvolvedPartyIdentificationType(IdentificationTypeUUID.toString(), ((SecurityToken) token).getSystemID(), tokens);
		Optional<InvolvedPartyXInvolvedPartyIdentificationType> foundLink = idType.builder()
		                                                                          .findLink(null, id, token.getSecurityToken())
		                                                                          .inActiveRange()
		                                                                          .inDateRange()
		                                                                          .withEnterprise(enterprise)
		                                                                          .canRead(((SecurityToken) token).getSystemID(), tokens)
		                                                                          .get();
		return foundLink.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
		                .orElse(null);
		
	}
	
	@Override
	public IInvolvedParty<?, ?> find(@CacheKey UUID uuid)
	{
		return new InvolvedParty().builder()
		                          .find(uuid)
		                          .get()
		                          .orElseThrow(() -> new InvolvedPartyException("The IP does not exist - " + uuid));
	}
	
	@Override
	public IInvolvedPartyType<?, ?> findType(@CacheKey UUID uuid)
	{
		return new InvolvedPartyType().builder()
		                              .find(uuid)
		                              .get()
		                              .orElseThrow(() -> new InvolvedPartyException("The IP Type does not exist - " + uuid));
	}
	
	@Override
	public IInvolvedPartyNameType<?, ?> findNameType(@CacheKey UUID uuid)
	{
		return new InvolvedPartyNameType().builder()
		                                  .find(uuid)
		                                  .get()
		                                  .orElseThrow(() -> new InvolvedPartyException("The IP Name Type does not exist - " + uuid));
	}
	
	@Override
	public IInvolvedPartyIdentificationType<?, ?> findIdentificationType(@CacheKey UUID uuid)
	{
		return new InvolvedPartyIdentificationType().builder()
		                                            .find(uuid)
		                                            .get()
		                                            .orElseThrow(() -> new InvolvedPartyException("The IP Name Type does not exist - " + uuid));
	}
	
	@Override
	@CacheResult(cacheName = "InvolvedPartyFindByUUID")
	public IInvolvedParty<?, ?> findByUUID(@CacheKey UUID token, @CacheKey ISystems<?, ?> system, @CacheKey UUID... tokens)
	{
		InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
		InvolvedPartyIdentificationType id = (InvolvedPartyIdentificationType) findInvolvedPartyIdentificationType(IdentificationTypeUUID.toString(), system, tokens);
		
		Optional<InvolvedPartyXInvolvedPartyIdentificationType> foundLink = idType.builder()
		                                                                          .findLink(null, id, token.toString())
		                                                                          .inActiveRange()
		                                                                          .inDateRange()
		                                                                          .withEnterprise(enterprise)
		                                                                          .canRead(system, tokens)
		                                                                          .get();
		return foundLink.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
		                .orElse(null);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public List<IRelationshipValue<IInvolvedParty<?, ?>, IInvolvedPartyIdentificationType<?, ?>, ?>> findAllByIdentificationType(String identificationType, String value)
	{
		InvolvedPartyIdentificationTypeQueryBuilder builder = new InvolvedPartyIdentificationType().builder();
		builder.inDateRange()
		       .where(InvolvedPartyIdentificationType_.name, Equals, identificationType)
		;
		
		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder ipQb = new InvolvedPartyXInvolvedPartyIdentificationType().builder();
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IInvolvedParty<?, ?>> findByRulesClassification(String classification, String value, ISystems<?, ?> system, UUID... identityToken)
	{
		IClassification<?, ?> classification1 = classificationService.find(classification, system, identityToken);
		
		@SuppressWarnings("rawtypes")
		List collect = new InvolvedPartyXRules().builder()
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
	
	@Override
	public IInvolvedParty<?, ?> findByClassification(String classification, String value, ISystems<?, ?> system, UUID... identityToken)
	{
		IClassification<?, ?> classification1 = classificationService.find(classification, system, identityToken);
		return new InvolvedPartyXClassification().builder()
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
