package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.events.Event;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.*;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.IIdentificationType;
import com.armineasy.activitymaster.activitymaster.services.INameType;
import com.armineasy.activitymaster.activitymaster.services.ITypeValue;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.armineasy.activitymaster.activitymaster.services.dto.ISystems;
import com.armineasy.activitymaster.activitymaster.services.exceptions.ActivityMasterException;
import com.armineasy.activitymaster.activitymaster.services.exceptions.SecurityAccessException;
import com.armineasy.activitymaster.activitymaster.services.security.Passwords;
import com.armineasy.activitymaster.activitymaster.services.system.IInvolvedPartyService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.google.inject.Singleton;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.guicedinjection.interfaces.JobService;
import com.jwebmp.guicedinjection.pairing.Pair;
import lombok.extern.java.Log;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.Optional;
import java.util.UUID;

import static com.armineasy.activitymaster.activitymaster.services.classifications.events.EventClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.classifications.involvedparty.InvolvedPartyClassifications.*;
import static com.armineasy.activitymaster.activitymaster.services.types.IdentificationTypes.*;
import static com.jwebmp.entityassist.querybuilder.EntityAssistStrings.*;

@SuppressWarnings("Duplicates")
@Singleton
@Log
public class InvolvedPartyService
		implements IInvolvedPartyService
{
	public InvolvedPartyNameType createNameType(ITypeValue<?> name, String description, IEnterprise enterprise)
	{
		return createNameType(name.classificationValue(), description, enterprise);
	}

	public InvolvedPartyNameType createNameType(String name, String description, IEnterprise enterprise, UUID... identityToken)
	{
		Systems system = GuiceContext.get(SystemsService.class)
		                             .getActivityMaster(enterprise);
		InvolvedPartyNameType xr = new InvolvedPartyNameType();
		Optional<InvolvedPartyNameType> exists = xr.builder()
		                                           .findByName(name)
		                                           .inActiveRange(enterprise, identityToken)
		                                           .inDateRange()
		                                           .get();
		if (exists.isEmpty())
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setOriginalSourceSystemID(system);
			xr.setSystemID(system);
			xr.setEnterpriseID(system.getEnterpriseID());
			xr.setActiveFlagID(system.getActiveFlagID());
			xr.persist();
		}
		else
		{
			xr = exists.get();
		}
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			xr.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
			                                     .getActivityMaster(xr.getEnterpriseID(), identityToken), identityToken);
		}
		return xr;
	}

	public InvolvedPartyIdentificationType createIdentificationType(IEnterprise enterprise, ITypeValue name, String description, UUID... identityToken)
	{
		Systems system = GuiceContext.get(SystemsService.class)
		                             .getActivityMaster(enterprise);

		InvolvedPartyIdentificationType xr = new InvolvedPartyIdentificationType();
		Optional<InvolvedPartyIdentificationType> exists = xr.builder()
		                                                     .findByName(name.name())
		                                                     .inActiveRange(enterprise, identityToken)
		                                                     .inDateRange()
		                                                     .get();
		if (exists.isEmpty())
		{
			xr.setName(name.name());
			xr.setDescription(description);
			xr.setSystemID(system);
			xr.setOriginalSourceSystemID(system);
			xr.setEnterpriseID(system.getEnterpriseID());
			xr.setActiveFlagID(system.getActiveFlagID());
			xr.persist();
		}
		else
		{
			xr = exists.get();
		}
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			xr.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
			                                     .getActivityMaster(xr.getEnterpriseID(), identityToken), identityToken);
		}

		return xr;
	}

	public InvolvedPartyType createType(IEnterprise enterprise, ITypeValue<?> name, String description)
	{
		return createType(enterprise, name.classificationValue(), description);
	}

	public InvolvedPartyType createType(IEnterprise enterprise, String name, String description, UUID... identityToken)
	{
		Systems system = GuiceContext.get(SystemsService.class)
		                             .getActivityMaster(enterprise);

		InvolvedPartyType xr = new InvolvedPartyType();
		Optional<InvolvedPartyType> exists = xr.builder()
		                                       .findByName(name)
		                                       .inActiveRange(enterprise, identityToken)
		                                       .inDateRange()
		                                       .get();
		if (exists.isEmpty())
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID(system);
			xr.setOriginalSourceSystemID(system);
			xr.setEnterpriseID(system.getEnterpriseID());
			xr.setActiveFlagID(system.getActiveFlagID());
			xr.persist();
		}
		else
		{
			xr = exists.get();
		}
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			xr.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
			                                     .getActivityMaster(xr.getEnterpriseID(), identityToken), identityToken);
		}
		return xr;
	}

	public InvolvedPartyOrganicType createOrganicType(IEnterprise enterprise, ITypeValue<?> name, String description, UUID... identityToken)
	{
		Systems system = GuiceContext.get(SystemsService.class)
		                             .getActivityMaster(enterprise);

		InvolvedPartyOrganicType xr = new InvolvedPartyOrganicType();
		Optional<InvolvedPartyOrganicType> exists = xr.builder()
		                                              .findByName(name.classificationValue())
		                                              .inActiveRange(enterprise, identityToken)
		                                              .inDateRange()
		                                              .get();
		if (exists.isEmpty())
		{
			xr.setName(name.classificationValue());
			xr.setDescription(description);
			xr.setSystemID(system);
			xr.setOriginalSourceSystemID(system);
			xr.setEnterpriseID(system.getEnterpriseID());
			xr.setActiveFlagID(system.getActiveFlagID());
			xr.persist();
		}
		else
		{
			xr = exists.get();
		}
		if (GuiceContext.get(ActivityMasterConfiguration.class)
		                .isSecurityEnabled())
		{
			xr.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
			                                     .getActivityMaster(xr.getEnterpriseID(), identityToken), identityToken);
		}
		return xr;
	}

	@CacheResult(cacheName = "InvolvedPartyGetIdentificationType")
	@Override
	public InvolvedPartyIdentificationType findIdentificationType(@CacheKey IIdentificationType<?> idType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens)
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
	public InvolvedParty findByIdentificationType(
			@CacheKey IIdentificationType<?> idType, @CacheKey String value, @CacheKey ISystems system, @CacheKey UUID... tokens)
	{
		IEnterprise enterprise = system.getEnterpriseID();
		Optional<InvolvedPartyXInvolvedPartyIdentificationType> builder = new InvolvedPartyXInvolvedPartyIdentificationType()
				                                                                  .builder()
				                                                                  .canRead(enterprise, tokens)
				                                                                  .inActiveRange(enterprise, tokens)
				                                                                  .inDateRange()
				                                                                  .findChildLink(findIdentificationType(idType, enterprise, tokens), value)
				                                                                  .get();
		return builder.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
		              .orElse(null);
	}

	private String encrypt(String toEncrypt, String salt)
	{
		//byte[] salt = saltString.getBytes();
		byte[] saltDecrypted = salt.getBytes();
		char[] pass = toEncrypt.toCharArray();
		byte[] passHashed = Passwords.hash(pass, saltDecrypted);
		//String saltEncrypted = Passwords.integerEncrypt(salt);
		String passEncrypted = Passwords.integerEncrypt(passHashed);
		return passEncrypted;
	}

	@Override
	public InvolvedParty findByUsernameAndPassword(String username, String password, ISystems originatingSystem, boolean throwForNoUser, UUID... token)
	{
		IEnterprise enterprise = originatingSystem.getEnterpriseID();
		if (!doesUsernameExist(username, enterprise))
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
		InvolvedParty foundPart = findByIdentificationType(IdentificationTypeUserName, username, originatingSystem, token);
		if (foundPart == null)
		{
			throw new SecurityAccessException("Unable to find any Involved Party with that username");
		}

		Optional<InvolvedPartyXClassification> saltEntity = foundPart.find(SecurityPasswordSalt, originatingSystem, token);
		Optional<InvolvedPartyXClassification> passEntity = foundPart.find(SecurityPassword, originatingSystem, token);
		if (saltEntity.isEmpty() || passEntity.isEmpty())
		{
			if (throwForNoUser)
			{
				throw new SecurityAccessException("Involved Party does not have Username/Password credentials");
			}
		}

		String saltString = saltEntity.get()
		                              .getValue();
		String passMatch = passEntity.get()
		                             .getValue();
		String passEncrypted = encrypt(password, saltString);

		if (!passEncrypted.equalsIgnoreCase(passMatch))
		{
			throw new SecurityAccessException("Password Incorrect");
		}
		return passEntity.get()
		                 .getInvolvedPartyID();
	}

	public boolean doesUsernameExist(String username, IEnterprise enterprise)
	{
		return new InvolvedParty().builder()
		                          .findByIdentificationType(enterprise, IdentificationTypeUserName, username)
		                          .getCount() > 0;
	}

	/**
	 * This method assumes that you have done all the necessary checks
	 *
	 * @param username
	 * @param password
	 * @param involvedParty
	 * @param originatingSystem
	 * @param token
	 *
	 * @return
	 */
	public InvolvedPartyXInvolvedPartyIdentificationType addUpdateUsernamePassword(Event event, String username, String password, InvolvedParty involvedParty, ISystems originatingSystem, UUID... token)
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
			                                                        .getBytes() : Passwords.getNextSalt();
		}

		String passEncrypted = encrypt(password, new String(salt));
		String saltEncrypted = Passwords.integerEncrypt(salt);

		involvedParty.addOrUpdate(SecurityPassword, passEncrypted, originatingSystem, token);
		involvedParty.addOrUpdate(SecurityPasswordSalt, saltEncrypted, originatingSystem, token);
		InvolvedPartyXInvolvedPartyIdentificationType sec =involvedParty.addIdentificationType(IdentificationTypeUserName, originatingSystem, username,token);

		if(event != null)
		{
			event.addOrReuse(UpdatedPassword,STRING_EMPTY, originatingSystem,token);
			event.addOrReuse(UpdatedUsername,username, originatingSystem,token);
		}
		return sec;
	}

	public InvolvedParty create(ISystems originatingSystem, Pair<IIdentificationType, String> idTypes,
	                            boolean isOrganic, UUID... identityToken)
	{
		IEnterprise enterprise = originatingSystem.getEnterpriseID();

		InvolvedParty ip = new InvolvedParty();
		Optional<InvolvedParty> exists =ActivityMasterConfiguration
				                                .get()
				                                .isDoubleCheckDisabled() ? Optional.empty() :
		                                ip.builder()
		                                   .findByIdentificationType(enterprise, idTypes.getKey(), idTypes.getValue(), identityToken)
		                                   .get();

		if (exists.isEmpty())
		{
			Systems system = (Systems) GuiceContext.get(ISystemsService.class)
			                                       .getActivityMaster(enterprise,identityToken);

			ip.setEnterpriseID((Enterprise) enterprise);
			ip.setActiveFlagID(system.getActiveFlagID());
			ip.setSystemID(system);
			ip.setOriginalSourceSystemID(system);
			ip.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				ip.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
				                                     .getActivityMaster(ip.getEnterpriseID(), identityToken)
						, identityToken);
			}

			ip.addIdentificationType(idTypes.getKey(), originatingSystem, idTypes.getValue(), identityToken);

			if (GuiceContext.get(ActivityMasterConfiguration.class)
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

	private void setupInvolvedPartyOrganicStatus(boolean isOrganic, InvolvedParty ip, IEnterprise<?> enterprise, Systems system, UUID... identityToken)
	{
		if (isOrganic)
		{
			InvolvedPartyOrganic ipo = new InvolvedPartyOrganic();
			ipo.setInvolvedParty(ip);
			ipo.setId(ip.getId());
			ipo.setEnterpriseID((Enterprise) enterprise);
			ipo.setActiveFlagID(system.getActiveFlagID());
			ipo.setSystemID(system);
			ipo.setOriginalSourceSystemID(system);
			ipo.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				ipo.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
				                                      .getActivityMaster(ipo.getEnterpriseID(), identityToken)
						, identityToken);
			}
		}
		else
		{
			InvolvedPartyNonOrganic ipo = new InvolvedPartyNonOrganic();
			ipo.setInvolvedParty(ip);
			ipo.setId(ip.getId());
			ipo.setEnterpriseID((Enterprise) enterprise);
			ipo.setActiveFlagID(system.getActiveFlagID());
			ipo.setSystemID(system);
			ipo.setOriginalSourceSystemID(system);
			ipo.persist();
			if (GuiceContext.get(ActivityMasterConfiguration.class)
			                .isSecurityEnabled())
			{
				ipo.createDefaultSecurity(GuiceContext.get(ISystemsService.class)
				                                      .getActivityMaster(ipo.getEnterpriseID(), identityToken)
						, identityToken);
			}
		}
	}

	@CacheResult(cacheName = "InvolvedPartyGetNameType")
	public InvolvedPartyType findType(@CacheKey ITypeValue<?> idType, @CacheKey IEnterprise enterprise, @CacheKey UUID... tokens)
	{
		return findType(idType.classificationValue(), enterprise, tokens);
	}

	private InvolvedPartyType findType(@CacheKey String nameType, @CacheKey IEnterprise enterprise, @CacheKey UUID... tokens)
	{
		InvolvedPartyType xr = new InvolvedPartyType();
		return xr.builder()
		         .findByName(nameType)
		         .inActiveRange(enterprise, tokens)
		         .inDateRange()
		         .canRead(enterprise, tokens)
		         .get()
		         .orElse(null);
	}

	@Override
	@CacheResult(cacheName = "InvolvedPartyGetNameType")
	public InvolvedPartyNameType findNameType(@CacheKey INameType<?> idType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens)
	{
		return findNameType(idType.classificationValue(), enterprise, tokens);
	}

	private InvolvedPartyNameType findNameType(@CacheKey String nameType, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens)
	{
		InvolvedPartyNameType xr = new InvolvedPartyNameType();
		return xr.builder()
		         .findByName(nameType)
		         .inActiveRange(enterprise, tokens)
		         .inDateRange()
		         .canRead(enterprise, tokens)
		         .get()
		         .orElse(null);
	}

	@Override
	public InvolvedParty findByToken(@CacheKey SecurityToken token, @CacheKey UUID... tokens)
	{
		InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
		InvolvedPartyIdentificationType id = findIdentificationType(IdentificationTypeUUID, token.getEnterpriseID(),tokens);
		Optional<InvolvedPartyXInvolvedPartyIdentificationType> foundLink = idType.builder()
		                                                                          .findChildLink(id, token.getSecurityToken())
		                                                                          .inActiveRange(token.getEnterpriseID())
		                                                                          .inDateRange()
		                                                                          .canRead(token.getEnterpriseID(), tokens)
		                                                                          .get();
		return foundLink.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
		                .orElse(null);

	}

	public InvolvedParty findByUUID(@CacheKey UUID token, @CacheKey IEnterprise enterprise, @CacheKey UUID... tokens)
	{
		InvolvedPartyXInvolvedPartyIdentificationType idType = new InvolvedPartyXInvolvedPartyIdentificationType();
		InvolvedPartyIdentificationType id = findIdentificationType(IdentificationTypeUUID, enterprise, tokens);

		Optional<InvolvedPartyXInvolvedPartyIdentificationType> foundLink = idType.builder()
		                                                                          .findChildLink(id, token.toString())
		                                                                          .inActiveRange(enterprise, tokens)
		                                                                          .inDateRange()
		                                                                          .canRead(enterprise, tokens)
		                                                                          .get();
		return foundLink.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
		                .orElse(null);
	}
}
