package com.guicedee.activitymaster.core;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.resourceitem.ResourceItem;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.*;
import com.guicedee.activitymaster.core.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.core.services.exceptions.SecurityAccessException;
import com.guicedee.activitymaster.core.services.security.Passwords;
import com.guicedee.activitymaster.core.services.system.IClassificationService;
import com.guicedee.activitymaster.core.services.system.IInvolvedPartyService;
import com.guicedee.activitymaster.core.systems.InvolvedPartySystem;
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
import static com.guicedee.activitymaster.core.SystemsService.*;
import static com.guicedee.activitymaster.core.services.classifications.classification.Classifications.*;
import static com.guicedee.activitymaster.core.services.classifications.events.EventClassifications.*;
import static com.guicedee.activitymaster.core.services.classifications.involvedparty.InvolvedPartyClassifications.*;
import static com.guicedee.activitymaster.core.services.types.IdentificationTypes.*;
import static com.guicedee.guicedinjection.GuiceContext.*;
import static com.guicedee.guicedinjection.json.StaticStrings.*;

@SuppressWarnings("Duplicates")
public class InvolvedPartyService
		implements IInvolvedPartyService<InvolvedPartyService>
{
	private static final Logger log = LogFactory.getLog("InvolvedPartyService");
	
	@Inject
	@Named("Active")
	private IActiveFlag<?> activeFlag;
	
	@Inject
	private IEnterprise<?> enterprise;
	
	@Inject
	@Named(ActivityMasterSystemName)
	private ISystems<?> activityMasterSystem;
	
	@Inject
	private IClassificationService<?> classificationService;
	
	@Override
	@CacheResult(cacheName = "InvovledPartyByID")
	public IInvolvedParty<?> findByID(@CacheKey UUID id)
	{
		return new InvolvedParty().builder()
		                          .find(id)
		                          .get()
		                          .orElseThrow();
	}
	
	@Override
	public IInvolvedPartyNameType<?> createNameType(ITypeValue<?> name, String description, ISystems<?> system)
	{
		return createNameType(name.classificationValue(), description, system);
	}
	
	@Override
	public IInvolvedPartyNameType<?> createNameType(String name, String description, ISystems<?> system, UUID... identityToken)
	{
		InvolvedPartyNameType xr = new InvolvedPartyNameType();
		
		boolean exists = xr.builder()
		                   .withName(name)
		                   .inActiveRange(enterprise, identityToken)
		                   .inDateRange()
		                   .withEnterprise(enterprise)
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID((Systems) system);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) enterprise);
			xr.setActiveFlagID(activeFlag);
			xr.persist();
			
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				xr.createDefaultSecurity(system, identityToken);
			}
		}
		else
		{
			return findNameType(name, system, identityToken);
		}
		
		return xr;
	}
	
	@Override
	public IInvolvedPartyIdentificationType<?> createIdentificationType(ISystems<?> system, ITypeValue<?> name, String description, UUID... identityToken)
	{
		return createIdentificationType(system, name.name(), description, identityToken);
	}
	
	@Override
	public IInvolvedPartyIdentificationType<?> createIdentificationType(ISystems<?> system, String name, String description, UUID... identityToken)
	{
		InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
		
		boolean exists = xr.builder()
		                   .withName(name)
		                   .inActiveRange(enterprise, identityToken)
		                   .inDateRange()
		                   .withEnterprise(enterprise)
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID((Systems) system);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) enterprise);
			xr.setActiveFlagID(activeFlag);
			xr.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				xr.createDefaultSecurity(system, identityToken);
			}
		}
		else
		{
			return findIdentificationType(name, system, identityToken);
		}
		return xr;
	}
	
	@Override
	public IInvolvedPartyType<?> createType(ISystems<?> system, ITypeValue<?> name, String description)
	{
		return createType(system, name.classificationValue(), description);
	}
	
	@Override
	public IInvolvedPartyType<?> createType(ISystems<?> system, String name, String description, UUID... identityToken)
	{
		InvolvedPartyType xr = new InvolvedPartyType();
		
		boolean exists = xr.builder()
		                   .withName(name)
		                   .inActiveRange(enterprise, identityToken)
		                   .inDateRange()
		                   .withEnterprise(enterprise)
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID((Systems) system);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) enterprise);
			xr.setActiveFlagID(activeFlag);
			xr.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				xr.createDefaultSecurity(activityMasterSystem, identityToken);
			}
		}
		else
		{
			return findType(name, system, identityToken);
		}
		
		return xr;
	}
	
	@Override
	public IInvolvedPartyOrganicType<?> createOrganicType(ISystems<?> system, ITypeValue<?> name, String description, UUID... identityToken)
	{
		InvolvedPartyOrganicType xr = new InvolvedPartyOrganicType();
		boolean exists = xr.builder()
		                   .withName(name.classificationValue())
		                   .inActiveRange(enterprise, identityToken)
		                   .inDateRange()
		                   .withEnterprise(enterprise)
		                   .getCount() > 0;
		
		if (!exists)
		{
			xr.setName(name.classificationValue());
			xr.setDescription(description);
			xr.setSystemID((Systems) system);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) enterprise);
			xr.setActiveFlagID(activeFlag);
			xr.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				xr.createDefaultSecurity(activityMasterSystem, identityToken);
			}
		}
		else
		{
			xr = xr.builder()
			       .withName(name.classificationValue())
			       .inActiveRange(enterprise, identityToken)
			       .inDateRange()
			       .withEnterprise(enterprise)
			       .get()
			       .orElseThrow();
		}
		
		return xr;
	}
	
	@CacheResult(cacheName = "InvolvedPartyGetIdentificationType")
	@Override
	public IInvolvedPartyIdentificationType<?> findIdentificationType(@CacheKey IIdentificationType<?> idType, @CacheKey ISystems<?> system, @CacheKey UUID... tokens)
	{
		InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
		return xr.builder()
		         .withName(idType.classificationValue())
		         .inActiveRange(enterprise, tokens)
		         .inDateRange()
		         //     .canRead(enterprise, tokens)
		         .get()
		         .orElseThrow(() -> new ActivityMasterException("No Read Access or No Item Found"));
	}
	
	@CacheResult(cacheName = "InvolvedPartyGetIdentificationTypeString")
	@Override
	public IInvolvedPartyIdentificationType<?> findIdentificationType(@CacheKey String idType, @CacheKey ISystems<?> system, @CacheKey UUID... tokens)
	{
		InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
		return xr.builder()
		         .withName(idType)
		         .inActiveRange(enterprise, tokens)
		         .inDateRange()
		         //     .canRead(enterprise, tokens)
		         .get()
		         .orElseThrow(() -> new ActivityMasterException("No Read Access or No Item Found"));
	}
	
	@Override
	@CacheResult(cacheName = "InvolvedPartyFindByIdentificationType")
	public IInvolvedParty<?> findByIdentificationType(@CacheKey IIdentificationType<?> idType, @CacheKey String value, ISystems<?> system, @CacheKey UUID... tokens)
	{
		Optional<InvolvedPartyXInvolvedPartyIdentificationType> builder = new InvolvedPartyXInvolvedPartyIdentificationType()
				.builder()
				.canRead(system, tokens)
				.inActiveRange(enterprise, tokens)
				.inDateRange()
				.findChildLink(
						(InvolvedPartyIdentificationType) findIdentificationType(idType, system, tokens),
						value)
				.setReturnFirst(true)
				.get();
		return builder.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
		              .orElse(null);
	}
	
	
	@Override
	@CacheResult(cacheName = "InvolvedPartyFindByIdentificationType")
	public IInvolvedParty<?> findByResourceItem(@CacheKey IResourceItem<?> idType, @CacheKey String value, ISystems<?> system, @CacheKey UUID... tokens)
	{
		Optional<InvolvedPartyXResourceItem> builder = new InvolvedPartyXResourceItem()
				.builder()
				.canRead(system, tokens)
				.inActiveRange(enterprise, tokens)
				.inDateRange()
				.findChildLink(
						(ResourceItem) idType,
						value)
				.setReturnFirst(true)
				.get();
		return builder.map(InvolvedPartyXResourceItem::getInvolvedPartyID)
		              .orElse(null);
	}
	
	@Override
	public IInvolvedParty<?> findByUsernameAndPassword(String username, String password, ISystems<?> system, boolean throwForNoUser, UUID... token)
	{
		if (!doesUsernameExist(username, system, token))
		{
			if (throwForNoUser)
			{
				throw new SecurityAccessException("Invalid Username");
			}
			else
			{
				return null;
			}
		}
		
		UUID identityToken = get(InvolvedPartySystem.class).getSystemToken(system.getEnterpriseID());
		InvolvedParty foundPart = new InvolvedParty().builder()
		                                             .findByIdentificationType(system, IdentificationTypeUserName, new Passwords().integerEncrypt(username.getBytes()),
				                                             identityToken)
		                                             .get()
		                                             .orElse(null);
		if (foundPart == null)
		{
			throw new SecurityAccessException("Unable to find any Involved Party with that username");
		}
		
		Optional<IRelationshipValue<IInvolvedParty<?>, IClassification<?>, ?>> saltEntity = foundPart.findClassifications(SecurityPasswordSalt, system, identityToken);
		Optional<IRelationshipValue<IInvolvedParty<?>, IClassification<?>, ?>> passEntity = foundPart.findClassifications(SecurityPassword, system, identityToken);
		if (saltEntity.isEmpty() || passEntity.isEmpty())
		{
			if (throwForNoUser)
			{
				throw new SecurityAccessException("Involved Party does not have Username/Password credentials");
			}
		}
		
		String saltString = saltEntity.get()
		                              .getValue();
		saltString = new String(new Passwords().integerDecrypt(saltString));
		String passMatch = passEntity.get()
		                             .getValue();
		String passEncrypted = encrypt(password, saltString);
		
		if (!passEncrypted.equalsIgnoreCase(passMatch))
		{
			throw new SecurityAccessException("Password Incorrect");
		}
		return foundPart;
	}
	
	private String encrypt(String toEncrypt, String salt)
	{
		//byte[] salt = saltString.getBytes();
		byte[] saltDecrypted = salt.getBytes();
		char[] pass = toEncrypt.toCharArray();
		byte[] passHashed = new Passwords().hash(pass, saltDecrypted);
		//String saltEncrypted = Passwords.integerEncrypt(salt);
		String passEncrypted = new Passwords().integerEncrypt(passHashed);
		return passEncrypted;
	}
	
	@Override
	public boolean doesUsernameExist(String username, ISystems<?> system, UUID... token)
	{
		return new InvolvedParty().builder()
		                          .withEnterprise(enterprise)
		                          .findByIdentificationType(system, IdentificationTypeUserName, new Passwords().integerEncrypt(username.getBytes()), token)
		                          .getCount() > 0;
	}
	
	@Override
	@CacheResult(cacheName = "InvolvedPartyByUsername")
	public IInvolvedParty<?> findByUsername(@CacheKey String username, @CacheKey ISystems<?> system, @CacheKey UUID... token)
	{
		IInvolvedParty<?> party = new InvolvedParty().builder()
		                                             .withEnterprise(enterprise)
		                                             .findByIdentificationType(system, IdentificationTypeUserName, new Passwords().integerEncrypt(username.getBytes()),
				                                             token)
		                                             .get()
		                                             .orElseThrow(() -> new SecurityAccessException("Involved Party Does Not Exist"));
		return party;
	}
	
	@Override
	public IInvolvedParty<?> addUpdateUsernamePassword(IEvent<?> event, String username, String password, IInvolvedParty<?> involvedParty, ISystems<?> system, UUID... token)
	{
		byte[] salt;
		if (involvedParty.hasClassifications(SecurityPasswordSalt, system, token))
		{
			salt = involvedParty.findClassifications(SecurityPasswordSalt, system, token)
			                    .get()
			                    .getValue()
			                    .getBytes();
		}
		else
		{
			salt = System.getProperty("systemSalt") != null ? System.getProperty("systemSalt")
			                                                        .getBytes() : new Passwords().getNextSalt();
		}
		
		String passEncrypted = encrypt(password, new String(salt));
		String saltEncrypted = new Passwords().integerEncrypt(salt);
		
		involvedParty.addOrUpdate(SecurityPassword, null, passEncrypted, system, token);
		involvedParty.addOrUpdate(SecurityPasswordSalt, null, saltEncrypted, system, token);
		involvedParty.addOrUpdateIdentificationType(IdentificationTypeUserName, NoClassification.classificationName(),
				new Passwords().integerEncrypt(username.getBytes()), system, token);
		
		
		if (event != null)
		{
			event.addOrUpdate(UpdatedPassword, (String) null, STRING_EMPTY, system, token);
			event.addOrUpdate(UpdatedUsername, (String) null, username, system, token);
		}
		return involvedParty;
	}
	
	@Override
	public IInvolvedParty<?> create(ISystems<?> system, Pair<IIdentificationType<?>, String> idTypes,
	                                boolean isOrganic, UUID... identityToken)
	{
		InvolvedParty ip = new InvolvedParty();
		Optional<InvolvedParty> exists = ip.builder()
		                                   .withEnterprise(enterprise)
		                                   .findByIdentificationType(system, idTypes.getKey(), idTypes.getValue(), identityToken)
		                                   .get();
		
		if (exists.isEmpty())
		{
			ip.setEnterpriseID((Enterprise) enterprise);
			ip.setActiveFlagID(activeFlag);
			ip.setSystemID((Systems) system);
			ip.setOriginalSourceSystemID((Systems) system);
			ip.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				ip.createDefaultSecurity(system, identityToken);
			}
			
			ip.addOrUpdateIdentificationType(idTypes.getKey(), NoClassification.name(), idTypes.getValue(), system, identityToken);
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
	
	private void setupInvolvedPartyOrganicStatus(boolean isOrganic, IInvolvedParty<?> ip, ISystems<?> system, UUID... identityToken)
	{
		if (isOrganic)
		{
			InvolvedPartyOrganic ipo = new InvolvedPartyOrganic();
			ipo.setInvolvedParty((InvolvedParty) ip);
			ipo.setId(ip.getId());
			ipo.setEnterpriseID((Enterprise) enterprise);
			ipo.setActiveFlagID(activeFlag);
			ipo.setSystemID((Systems) system);
			ipo.setOriginalSourceSystemID((Systems) system);
			ipo.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				ipo.createDefaultSecurity(system, identityToken);
			}
		}
		else
		{
			InvolvedPartyNonOrganic ipo = new InvolvedPartyNonOrganic();
			ipo.setInvolvedParty((InvolvedParty) ip);
			ipo.setId(ip.getId());
			ipo.setEnterpriseID((Enterprise) enterprise);
			ipo.setActiveFlagID(activeFlag);
			ipo.setSystemID((Systems) system);
			ipo.setOriginalSourceSystemID((Systems) system);
			ipo.persist();
			if (get(ActivityMasterConfiguration.class)
					.isSecurityEnabled())
			{
				ipo.createDefaultSecurity(system, identityToken);
			}
		}
	}
	
	@CacheResult(cacheName = "InvolvedPartyGetNameType")
	@Override
	public IInvolvedPartyType<?> findType(@CacheKey ITypeValue<?> idType, @CacheKey ISystems<?> system, @CacheKey UUID... tokens)
	{
		return findType(idType.classificationValue(), system, tokens);
	}
	
	@CacheResult(cacheName = "InvolvedPartyFindTypeByString")
	@Override
	public IInvolvedPartyType<?> findType(@CacheKey String nameType, @CacheKey ISystems<?> system, @CacheKey UUID... tokens)
	{
		InvolvedPartyType xr = new InvolvedPartyType();
		return xr.builder()
		         .withName(nameType)
		         .inActiveRange(enterprise, tokens)
		         .withEnterprise(enterprise)
		         .inDateRange()
		         .canRead(system, tokens)
		         .get()
		         .orElse(null);
	}
	
	@Override
	@CacheResult(cacheName = "InvolvedPartyGetNameType")
	public IInvolvedPartyNameType<?> findNameType(@CacheKey INameType<?> idType, @CacheKey ISystems<?> system, @CacheKey UUID... tokens)
	{
		return findNameType(idType.classificationValue(), system, tokens);
	}
	
	@CacheResult(cacheName = "InvolvedPartyGetNameTypeString")
	@Override
	public IInvolvedPartyNameType<?> findNameType(@CacheKey String nameType, @CacheKey ISystems<?> system, @CacheKey UUID... tokens)
	{
		InvolvedPartyNameType xr = new InvolvedPartyNameType();
		return xr.builder()
		         .withName(nameType)
		         .inActiveRange(enterprise, tokens)
		         .inDateRange()
		         .withEnterprise(enterprise)
		         .canRead(system, tokens)
		         .get()
		         .orElse(null);
	}
	
	@Override
	@CacheResult(cacheName = "InvolvedPartyFindByToken")
	public IInvolvedParty<?> findByToken(@CacheKey ISecurityToken<?> token, @CacheKey UUID... tokens)
	{
		InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
		InvolvedPartyIdentificationType id = (InvolvedPartyIdentificationType) findIdentificationType(IdentificationTypeUUID, ((SecurityToken) token).getSystemID(), tokens);
		Optional<InvolvedPartyXInvolvedPartyIdentificationType> foundLink = idType.builder()
		                                                                          .findChildLink(id, token.getSecurityToken())
		                                                                          .inActiveRange(enterprise)
		                                                                          .inDateRange()
		                                                                          .withEnterprise(enterprise)
		                                                                          .canRead(((SecurityToken) token).getSystemID(), tokens)
		                                                                          .get();
		return foundLink.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
		                .orElse(null);
		
	}
	
	@Override
	@CacheResult(cacheName = "InvolvedPartyFindByUUID")
	public IInvolvedParty<?> findByUUID(@CacheKey UUID token, @CacheKey ISystems<?> system, @CacheKey UUID... tokens)
	{
		InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
		InvolvedPartyIdentificationType id = (InvolvedPartyIdentificationType) findIdentificationType(IdentificationTypeUUID, system, tokens);
		
		Optional<InvolvedPartyXInvolvedPartyIdentificationType> foundLink = idType.builder()
		                                                                          .findChildLink(id, token.toString())
		                                                                          .inActiveRange(enterprise, tokens)
		                                                                          .inDateRange()
		                                                                          .withEnterprise(enterprise)
		                                                                          .canRead(system, tokens)
		                                                                          .get();
		return foundLink.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
		                .orElse(null);
	}
	
	@Override
	public IInvolvedParty<?> findByIdentificationType(String identificationType, String value)
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
			Optional<InvolvedPartyXInvolvedPartyIdentificationType> opt = ipQb.get();
			return opt.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
			          .orElse(null);
		}
		catch (Throwable t)
		{
			log.log(Level.WARNING, "Unable to find involved party for session");
			log.log(Level.FINE, "Unable to find involved party for session", t);
			return null;
		}
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public List<IRelationshipValue<IInvolvedParty<?>, IInvolvedPartyIdentificationType<?>, ?>> findAllByIdentificationType(String identificationType, String value)
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
	public List<IInvolvedParty<?>> findByRulesClassification(String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassification<?> classification1 = classificationService.find(classification, system, identityToken);
		
		@SuppressWarnings("rawtypes")
		List collect = new InvolvedPartyXRules().builder()
		                                        .withClassification(classification1)
		                                        .withValue(value)
		                                        .inActiveRange(enterprise, identityToken)
		                                        .inDateRange()
		                                        .getAll()
		                                        .stream()
		                                        .map(InvolvedPartyXRules::getInvolvedPartyID)
		                                        .collect(Collectors.toList());
		return collect;
		
	}
	
	@Override
	public IInvolvedParty<?> findByClassification(String classification, String value, ISystems<?> system, UUID... identityToken)
	{
		IClassification<?> classification1 = classificationService.find(classification, system, identityToken);
		return new InvolvedPartyXClassification().builder()
		                                         .withClassification(classification1)
		                                         .withValue(value)
		                                         .inActiveRange(enterprise, identityToken)
		                                         .inDateRange()
		                                         .get()
		                                         .orElse(null)
		                                         .getPrimary()
				;
		
	}
}
