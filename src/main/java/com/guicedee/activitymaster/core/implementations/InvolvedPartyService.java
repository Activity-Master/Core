package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.db.entities.involvedparty.*;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.involvedparty.builders.InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder;
import com.guicedee.activitymaster.core.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.core.db.entities.systems.Systems;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IIdentificationType;
import com.guicedee.activitymaster.core.services.enumtypes.INameType;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;
import com.guicedee.activitymaster.core.services.exceptions.ActivityMasterException;
import com.guicedee.activitymaster.core.services.exceptions.SecurityAccessException;
import com.guicedee.activitymaster.core.services.security.Passwords;
import com.guicedee.activitymaster.core.services.system.IInvolvedPartyService;
import com.guicedee.activitymaster.core.services.system.ISystemsService;
import com.guicedee.activitymaster.core.systems.InvolvedPartySystem;
import com.google.inject.Singleton;
import com.guicedee.guicedinjection.interfaces.JobService;
import com.guicedee.guicedinjection.pairing.Pair;
import com.guicedee.logger.LogFactory;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import javax.persistence.criteria.JoinType;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.core.services.classifications.events.EventClassifications.*;
import static com.guicedee.activitymaster.core.services.classifications.involvedparty.InvolvedPartyClassifications.*;
import static com.guicedee.activitymaster.core.services.types.IdentificationTypes.*;
import static com.entityassist.querybuilder.EntityAssistStrings.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

@SuppressWarnings("Duplicates")
@Singleton
public class InvolvedPartyService
		implements IInvolvedPartyService<InvolvedPartyService>
{
	private static final Logger log = LogFactory.getLog("InvolvedPartyService");

	@Override
	public IInvolvedPartyNameType<?> createNameType(ITypeValue<?> name, String description, IEnterprise<?> enterprise)
	{
		return createNameType(name.classificationValue(), description, enterprise);
	}

	@Override
	public IInvolvedPartyNameType<?> createNameType(String name, String description, IEnterprise<?> enterprise, UUID... identityToken)
	{
		ISystems<?> system = get(SystemsService.class)
				                     .getActivityMaster(enterprise);
		InvolvedPartyNameType xr = new InvolvedPartyNameType();
		Optional<InvolvedPartyNameType> exists = xr.builder()
		                                           .findByName(name)
		                                           .inActiveRange(enterprise, identityToken)
		                                           .inDateRange()
		                                           .withEnterprise(enterprise)
		                                           .get();
		if (exists.isEmpty())
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID((Systems) system);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
			xr.setActiveFlagID((ActiveFlag) get(ActiveFlagService.class).getActiveFlag(xr.getEnterpriseID()));
			xr.persist();
		}
		else
		{
			xr = exists.get();
		}
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			xr.createDefaultSecurity(get(ISystemsService.class)
					                         .getActivityMaster(xr.getEnterpriseID(), identityToken), identityToken);
		}
		return xr;
	}

	@Override
	public IInvolvedPartyIdentificationType<?> createIdentificationType(IEnterprise<?> enterprise, ITypeValue name, String description, UUID... identityToken)
	{
		ISystems<?> system = get(SystemsService.class)
				                     .getActivityMaster(enterprise);

		InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
		Optional<InvolvedPartyIdentificationType> exists = xr.builder()
		                                                     .findByName(name.name())
		                                                     .inActiveRange(enterprise, identityToken)
		                                                     .inDateRange()
		                                                     .withEnterprise(enterprise)
		                                                     .get();
		if (exists.isEmpty())
		{
			xr.setName(name.name());
			xr.setDescription(description);
			xr.setSystemID((Systems) system);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
			xr.setActiveFlagID((ActiveFlag) get(ActiveFlagService.class).getActiveFlag(xr.getEnterpriseID()));
			xr.persist();
		}
		else
		{
			xr = exists.get();
		}
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			xr.createDefaultSecurity(get(ISystemsService.class)
					                         .getActivityMaster(xr.getEnterpriseID(), identityToken), identityToken);
		}

		return xr;
	}

	@Override
	public IInvolvedPartyType<?> createType(IEnterprise<?> enterprise, ITypeValue<?> name, String description)
	{
		return createType(enterprise, name.classificationValue(), description);
	}

	@Override
	public IInvolvedPartyType<?> createType(IEnterprise<?> enterprise, String name, String description, UUID... identityToken)
	{
		ISystems<?> system = get(SystemsService.class)
				                     .getActivityMaster(enterprise);

		InvolvedPartyType xr = new InvolvedPartyType();
		Optional<InvolvedPartyType> exists = xr.builder()
		                                       .findByName(name)
		                                       .inActiveRange(enterprise, identityToken)
		                                       .inDateRange()
		                                       .withEnterprise(enterprise)
		                                       .get();
		if (exists.isEmpty())
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID((Systems) system);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
			xr.setActiveFlagID((ActiveFlag) get(ActiveFlagService.class).getActiveFlag(xr.getEnterpriseID()));
			xr.persist();
		}
		else
		{
			xr = exists.get();
		}
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			xr.createDefaultSecurity(get(ISystemsService.class)
					                         .getActivityMaster(xr.getEnterpriseID(), identityToken), identityToken);
		}
		return xr;
	}

	@Override
	public IInvolvedPartyOrganicType<?> createOrganicType(IEnterprise<?> enterprise, ITypeValue<?> name, String description, UUID... identityToken)
	{
		ISystems<?> system = get(SystemsService.class)
				                     .getActivityMaster(enterprise);

		InvolvedPartyOrganicType xr = new InvolvedPartyOrganicType();
		Optional<InvolvedPartyOrganicType> exists = xr.builder()
		                                              .findByName(name.classificationValue())
		                                              .inActiveRange(enterprise, identityToken)
		                                              .inDateRange()
		                                              .withEnterprise(enterprise)
		                                              .get();
		if (exists.isEmpty())
		{
			xr.setName(name.classificationValue());
			xr.setDescription(description);
			xr.setSystemID((Systems) system);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
			xr.setActiveFlagID((ActiveFlag) get(ActiveFlagService.class).getActiveFlag(xr.getEnterpriseID()));
			xr.persist();
		}
		else
		{
			xr = exists.get();
		}
		if (get(ActivityMasterConfiguration.class)
				    .isSecurityEnabled())
		{
			xr.createDefaultSecurity(get(ISystemsService.class)
					                         .getActivityMaster(xr.getEnterpriseID(), identityToken), identityToken);
		}
		return xr;
	}

	@CacheResult(cacheName = "InvolvedPartyGetIdentificationType")
	@Override
	public IInvolvedPartyIdentificationType<?> findIdentificationType(@CacheKey IIdentificationType<?> idType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens)
	{
		InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
		return xr.builder()
		         .findByName(idType.classificationValue())
		         .inActiveRange(enterprise, tokens)
		         .inDateRange()
		         .canRead(enterprise, tokens)
		         .get()
		         .orElseThrow(() -> new ActivityMasterException("No Read Access or No Item Found"));
	}

	@Override
	public IInvolvedParty<?> findByIdentificationType(@CacheKey IIdentificationType<?> idType, @CacheKey String value, @CacheKey ISystems<?> system, @CacheKey UUID... tokens)
	{
		IEnterprise<?> enterprise = system.getEnterpriseID();
		Optional<InvolvedPartyXInvolvedPartyIdentificationType> builder = new InvolvedPartyXInvolvedPartyIdentificationType()
				                                                                  .builder()
				                                                                  .canRead(enterprise, tokens)
				                                                                  .inActiveRange(enterprise, tokens)
				                                                                  .inDateRange()
				                                                                  .findChildLink(
						                                                                  (InvolvedPartyIdentificationType) findIdentificationType(idType, enterprise, tokens),
						                                                                  value)
				                                                                  .setReturnFirst(true)
				                                                                  .get();
		return builder.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
		              .orElse(null);
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
	public IInvolvedParty<?> findByUsernameAndPassword(String username, String password, ISystems<?> originatingSystem, boolean throwForNoUser, UUID... token)
	{
		IEnterprise<?> enterprise = originatingSystem.getEnterpriseID();
		if (!doesUsernameExist(username, enterprise, token))
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

		UUID identityToken = InvolvedPartySystem.getSystemTokens()
		                                        .get(originatingSystem.getEnterpriseID());
		InvolvedParty foundPart = new InvolvedParty().builder()
		                                             .findByIdentificationType(enterprise, IdentificationTypeUserName, new Passwords().integerEncrypt(username.getBytes()),
		                                                                       identityToken)
		                                             .get()
		                                             .orElse(null);
		if (foundPart == null)
		{
			throw new SecurityAccessException("Unable to find any Involved Party with that username");
		}

		Optional<IRelationshipValue<IInvolvedParty<?>, IClassification<?>, ?>> saltEntity = foundPart.find(SecurityPasswordSalt, originatingSystem, identityToken);
		Optional<IRelationshipValue<IInvolvedParty<?>, IClassification<?>, ?>> passEntity = foundPart.find(SecurityPassword, originatingSystem, identityToken);
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

	@Override
	public boolean doesUsernameExist(String username, IEnterprise<?> enterprise, UUID... token)
	{
		return new InvolvedParty().builder()
		                          .withEnterprise(enterprise)
		                          .findByIdentificationType(enterprise, IdentificationTypeUserName, new Passwords().integerEncrypt(username.getBytes()), token)
		                          .getCount() > 0;
	}

	@Override
	public IInvolvedParty<?> findByUsername(String username, IEnterprise<?> enterprise, UUID... token)
	{
		IInvolvedParty<?> party = new InvolvedParty().builder()
		                                             .withEnterprise(enterprise)
		                                             .findByIdentificationType(enterprise, IdentificationTypeUserName, new Passwords().integerEncrypt(username.getBytes()),
		                                                                       token)
		                                             .get()
		                                             .orElseThrow(() -> new SecurityAccessException("Involved Party Does Not Exist"));
		return party;
	}

	@Override
	public IInvolvedParty<?> addUpdateUsernamePassword(IEvent<?> event, String username, String password, IInvolvedParty<?> involvedParty, ISystems<?> originatingSystem, UUID... token)
	{
		byte[] salt;
		if (involvedParty.has(SecurityPasswordSalt, originatingSystem, token))
		{
			salt = involvedParty.find(SecurityPasswordSalt, originatingSystem, token)
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

		involvedParty.addOrUpdate(SecurityPassword, passEncrypted, originatingSystem, token);
		involvedParty.addOrUpdate(SecurityPasswordSalt, saltEncrypted, originatingSystem, token);
		involvedParty.addOrUpdate(IdentificationTypeUserName, new Passwords().integerEncrypt(username.getBytes()), originatingSystem, token);

		if (event != null)
		{
			event.addOrUpdate(UpdatedPassword, STRING_EMPTY, originatingSystem, token);
			event.addOrUpdate(UpdatedUsername, username, originatingSystem, token);
		}
		return involvedParty;
	}

	@Override
	public IInvolvedParty<?> create(ISystems<?> originatingSystem, Pair<IIdentificationType<?>, String> idTypes,
	                                boolean isOrganic, UUID... identityToken)
	{
		IEnterprise<?> enterprise = originatingSystem.getEnterpriseID();

		InvolvedParty ip = new InvolvedParty();
		Optional<InvolvedParty> exists = ip.builder()
		                                   .withEnterprise(originatingSystem.getEnterprise())
		                                   .findByIdentificationType(enterprise, idTypes.getKey(), idTypes.getValue(), identityToken)
		                                   .get();

		if (exists.isEmpty())
		{
			Systems system = (Systems) get(ISystemsService.class)
					                           .getActivityMaster(enterprise, identityToken);

			ip.setEnterpriseID((Enterprise) enterprise);
			ip.setActiveFlagID(system.getActiveFlagID());
			ip.setSystemID(system);
			ip.setOriginalSourceSystemID(system);
			ip.persist();
			if (get(ActivityMasterConfiguration.class)
					    .isSecurityEnabled())
			{
				ip.createDefaultSecurity(get(ISystemsService.class)
						                         .getActivityMaster(ip.getEnterpriseID(), identityToken)
						, identityToken);
			}

			ip.addOrUpdate(idTypes.getKey(), idTypes.getValue(), originatingSystem, identityToken);

			if (get(ActivityMasterConfiguration.class)
					    .isAsyncEnabled())
			{
				final InvolvedParty ipAsync = ip;
				JobService.getInstance()
				          .addJob("InvolvedPartyOrganicStorage",
				                  () -> setupInvolvedPartyOrganicStatus(isOrganic, ipAsync, enterprise, system, identityToken));
			}
			else
			{
				setupInvolvedPartyOrganicStatus(isOrganic, ip, enterprise, system, identityToken);
			}
		}
		else
		{
			ip = exists.get();
		}

		return ip;
	}

	private void setupInvolvedPartyOrganicStatus(boolean isOrganic, IInvolvedParty<?> ip, IEnterprise<?> enterprise, Systems system, UUID... identityToken)
	{
		if (isOrganic)
		{
			InvolvedPartyOrganic ipo = new InvolvedPartyOrganic();
			ipo.setInvolvedParty((InvolvedParty) ip);
			ipo.setId(ip.getId());
			ipo.setEnterpriseID((Enterprise) enterprise);
			ipo.setActiveFlagID(system.getActiveFlagID());
			ipo.setSystemID(system);
			ipo.setOriginalSourceSystemID(system);
			ipo.persist();
			if (get(ActivityMasterConfiguration.class)
					    .isSecurityEnabled())
			{
				ipo.createDefaultSecurity(get(ISystemsService.class)
						                          .getActivityMaster(ipo.getEnterpriseID(), identityToken)
						, identityToken);
			}
		}
		else
		{
			InvolvedPartyNonOrganic ipo = new InvolvedPartyNonOrganic();
			ipo.setInvolvedParty((InvolvedParty) ip);
			ipo.setId(ip.getId());
			ipo.setEnterpriseID((Enterprise) enterprise);
			ipo.setActiveFlagID(system.getActiveFlagID());
			ipo.setSystemID(system);
			ipo.setOriginalSourceSystemID(system);
			ipo.persist();
			if (get(ActivityMasterConfiguration.class)
					    .isSecurityEnabled())
			{
				ipo.createDefaultSecurity(get(ISystemsService.class)
						                          .getActivityMaster(ipo.getEnterpriseID(), identityToken)
						, identityToken);
			}
		}
	}

	@CacheResult(cacheName = "InvolvedPartyGetNameType")
	@Override
	public IInvolvedPartyType<?> findType(@CacheKey ITypeValue<?> idType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens)
	{
		return findType(idType.classificationValue(), enterprise, tokens);
	}

	private IInvolvedPartyType<?> findType(@CacheKey String nameType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens)
	{
		InvolvedPartyType xr = new InvolvedPartyType();
		return xr.builder()
		         .findByName(nameType)
		         .inActiveRange(enterprise, tokens)
		         .withEnterprise(enterprise)
		         .inDateRange()
		         .canRead(enterprise, tokens)
		         .get()
		         .orElse(null);
	}

	@Override
	@CacheResult(cacheName = "InvolvedPartyGetNameType")
	public IInvolvedPartyNameType<?> findNameType(@CacheKey INameType<?> idType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens)
	{
		return findNameType(idType.classificationValue(), enterprise, tokens);
	}

	private IInvolvedPartyNameType<?> findNameType(@CacheKey String nameType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens)
	{
		InvolvedPartyNameType xr = new InvolvedPartyNameType();
		return xr.builder()
		         .findByName(nameType)
		         .inActiveRange(enterprise, tokens)
		         .inDateRange()
		         .withEnterprise(enterprise)
		         .canRead(enterprise, tokens)
		         .get()
		         .orElse(null);
	}

	@Override
	public IInvolvedParty<?> findByToken(@CacheKey ISecurityToken<?> token, @CacheKey UUID... tokens)
	{
		InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
		InvolvedPartyIdentificationType id = (InvolvedPartyIdentificationType) findIdentificationType(IdentificationTypeUUID, ((SecurityToken) token).getEnterpriseID(), tokens);
		Optional<InvolvedPartyXInvolvedPartyIdentificationType> foundLink = idType.builder()
		                                                                          .findChildLink(id, token.getSecurityToken())
		                                                                          .inActiveRange(((SecurityToken) token).getEnterpriseID())
		                                                                          .inDateRange()
		                                                                          .withEnterprise(((SecurityToken) token).getEnterpriseID())
		                                                                          .canRead(((SecurityToken) token).getEnterpriseID(), tokens)
		                                                                          .get();
		return foundLink.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
		                .orElse(null);

	}

	@Override
	public IInvolvedParty<?> findByUUID(@CacheKey UUID token, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens)
	{
		InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
		InvolvedPartyIdentificationType id = (InvolvedPartyIdentificationType) findIdentificationType(IdentificationTypeUUID, enterprise, tokens);

		Optional<InvolvedPartyXInvolvedPartyIdentificationType> foundLink = idType.builder()
		                                                                          .findChildLink(id, token.toString())
		                                                                          .inActiveRange(enterprise, tokens)
		                                                                          .inDateRange()
		                                                                          .withEnterprise(enterprise)
		                                                                          .canRead(enterprise, tokens)
		                                                                          .get();
		return foundLink.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
		                .orElse(null);
	}

	@Override
	public IInvolvedParty<?> findByIdentificationType(String identificationType, String value)
	{
		InvolvedPartyIdentificationTypeQueryBuilder builder = new InvolvedPartyIdentificationType().builder();
		builder.inDateRange()
		       .where(InvolvedPartyIdentificationType_.name, Equals, identificationType);

		InvolvedPartyXInvolvedPartyIdentificationTypeQueryBuilder ipQb = new InvolvedPartyXInvolvedPartyIdentificationType().builder();
		ipQb.inDateRange()
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
}
