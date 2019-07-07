package com.armineasy.activitymaster.activitymaster.implementations;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.*;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.classifications.involvedparty.InvolvedPartyClassifications;
import com.armineasy.activitymaster.activitymaster.services.dto.*;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IIdentificationType;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.INameType;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.ITypeValue;
import com.armineasy.activitymaster.activitymaster.services.exceptions.ActivityMasterException;
import com.armineasy.activitymaster.activitymaster.services.exceptions.SecurityAccessException;
import com.armineasy.activitymaster.activitymaster.services.security.Passwords;
import com.armineasy.activitymaster.activitymaster.services.system.IInvolvedPartyService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.armineasy.activitymaster.activitymaster.systems.InvolvedPartySystem;
import com.google.inject.Singleton;
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
import static com.jwebmp.guicedinjection.GuiceContext.*;

@SuppressWarnings("Duplicates")
@Singleton
@Log
public class InvolvedPartyService
		implements IInvolvedPartyService
{
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
		                                           .get();
		if (exists.isEmpty())
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID((Systems) system);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
			xr.setActiveFlagID(get(ActiveFlagService.class).getActiveFlag(xr.getEnterpriseID()));
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
		                                                     .get();
		if (exists.isEmpty())
		{
			xr.setName(name.name());
			xr.setDescription(description);
			xr.setSystemID((Systems) system);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
			xr.setActiveFlagID(get(ActiveFlagService.class).getActiveFlag(xr.getEnterpriseID()));
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
		                                       .get();
		if (exists.isEmpty())
		{
			xr.setName(name);
			xr.setDescription(description);
			xr.setSystemID((Systems) system);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
			xr.setActiveFlagID(get(ActiveFlagService.class).getActiveFlag(xr.getEnterpriseID()));
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
		                                              .get();
		if (exists.isEmpty())
		{
			xr.setName(name.classificationValue());
			xr.setDescription(description);
			xr.setSystemID((Systems) system);
			xr.setOriginalSourceSystemID((Systems) system);
			xr.setEnterpriseID((Enterprise) system.getEnterpriseID());
			xr.setActiveFlagID(get(ActiveFlagService.class).getActiveFlag(xr.getEnterpriseID()));
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
		byte[] passHashed = Passwords.hash(pass, saltDecrypted);
		//String saltEncrypted = Passwords.integerEncrypt(salt);
		String passEncrypted = Passwords.integerEncrypt(passHashed);
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
		                                             .findByIdentificationType(enterprise, IdentificationTypeUserName, Passwords.integerEncrypt(username.getBytes()), identityToken)
		                                             .get()
		                                             .orElse(null);
		if (foundPart == null)
		{
			throw new SecurityAccessException("Unable to find any Involved Party with that username");
		}

		Optional<IRelationshipValue<InvolvedParty, Classification,?>> saltEntity = foundPart.find(SecurityPasswordSalt, originatingSystem, identityToken);
		Optional<IRelationshipValue<InvolvedParty,Classification,?>> passEntity = foundPart.find(SecurityPassword, originatingSystem, identityToken);
		if (saltEntity.isEmpty() || passEntity.isEmpty())
		{
			if (throwForNoUser)
			{
				throw new SecurityAccessException("Involved Party does not have Username/Password credentials");
			}
		}

		String saltString = saltEntity.get()
		                              .getValue();
		saltString = new String(Passwords.integerDecrypt(saltString));
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
		                          .findByIdentificationType(enterprise, IdentificationTypeUserName, Passwords.integerEncrypt(username.getBytes()), token)
		                          .getCount() > 0;
	}

	@Override
	public IInvolvedParty<?> findByUsername(String username, IEnterprise<?> enterprise, UUID... token)
	{
		IInvolvedParty<?> party = new InvolvedParty().builder()
		                                             .findByIdentificationType(enterprise, IdentificationTypeUserName, Passwords.integerEncrypt(username.getBytes()),
		                                                                       token)
		                                             .get()
		                                             .orElseThrow(() -> new SecurityAccessException("Involved Party Does Not Exist"));
		return party;
	}


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
			                                                        .getBytes() : Passwords.getNextSalt();
		}

		String passEncrypted = encrypt(password, new String(salt));
		String saltEncrypted = Passwords.integerEncrypt(salt);

		involvedParty.addOrUpdate(SecurityPassword, passEncrypted, originatingSystem, token);
		involvedParty.addOrUpdate(SecurityPasswordSalt, saltEncrypted, originatingSystem, token);
		involvedParty.addOrUpdate(IdentificationTypeUserName, Passwords.integerEncrypt(username.getBytes()), originatingSystem, token);

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
		Optional<InvolvedParty> exists = ActivityMasterConfiguration
				                                 .get()
				                                 .isDoubleCheckDisabled() ? Optional.empty() :
		                                 ip.builder()
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
		                                                                          .canRead(enterprise, tokens)
		                                                                          .get();
		return foundLink.map(InvolvedPartyXInvolvedPartyIdentificationType::getInvolvedPartyID)
		                .orElse(null);
	}
}
