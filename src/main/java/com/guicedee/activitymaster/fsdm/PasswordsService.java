package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.api.Passwords;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyIdentificationType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.SecurityTokenClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.IPTypes;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.SecurityAccessException;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.systems.InvolvedPartySystem;
import com.guicedee.guicedinjection.pairing.Pair;
import jakarta.cache.annotation.CacheRemove;
import jakarta.cache.annotation.CacheResult;
import jakarta.validation.constraints.NotNull;

import java.util.*;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.InvolvedPartyClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.NameTypes.*;
import static com.guicedee.guicedinjection.GuiceContext.*;

public class PasswordsService implements IPasswordsService<PasswordsService>
{
	@Inject
	private IEnterprise<?, ?> enterprise;
	
	@Inject
	private IInvolvedPartyService<?> involvedPartyService;
	
	private String encrypt(String toEncrypt, byte[] salt)
	{
		Passwords passwords = new Passwords();
		//byte[] salt = saltString.getBytes();
		//byte[] saltDecrypted = salt.getBytes();
		char[] pass = toEncrypt.toCharArray();
		byte[] passHashed = passwords.hash(pass, salt);
		//String saltEncrypted = passwords.integerEncrypt(salt);
		String passEncrypted = passwords.integerEncrypt(passHashed);
		//String passEncrypted = new String(passHashed);
		return passEncrypted;
	}
	
	
	@Override
	public IInvolvedParty<?, ?> findByUsername(String username, ISystems<?, ?> system, UUID... token)
	{
		IInvolvedParty<?, ?> party = new InvolvedParty().builder()
		                                                .withEnterprise(enterprise)
		                                                .findByIdentificationType(IdentificationTypeUserName, username,
				                                                system, token)
		                                                .get()
		                                                .orElseThrow(() -> new SecurityAccessException("Involved Party Does Not Exist"));
		return party;
	}
	
	@Override
	public IInvolvedParty<?, ?> findByUsernameAndPassword(String username, String password, ISystems<?, ?> system, boolean throwForNoUser, UUID... token)
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
		                                             .findByIdentificationType(IdentificationTypeUserName,
				                                             username, system,
				                                             identityToken)
		                                             .get()
		                                             .orElse(null);
		if (foundPart == null)
		{
			throw new SecurityAccessException("Unable to find any Involved Party with that username");
		}
		
		Optional<IRelationshipValue<InvolvedParty, IClassification<?, ?>, ?>> saltEntity = foundPart.findClassification(SecurityPasswordSalt, system, identityToken);
		Optional<IRelationshipValue<InvolvedParty, IClassification<?, ?>, ?>> passEntity = foundPart.findClassification(SecurityPassword, system, identityToken);
		if (saltEntity.isEmpty() || passEntity.isEmpty())
		{
			if (throwForNoUser)
			{
				throw new SecurityAccessException("Involved Party does not have Username/Password credentials");
			}
		}
		
		String saltString = saltEntity.get()
		                              .getValue();
		byte[] salt = new Passwords().integerDecrypt(saltString);
		//saltString = new String();
		String passMatch = passEntity.get()
		                             .getValue();
		String passEncrypted = encrypt(password, salt);
		if (!passEncrypted.equalsIgnoreCase(passMatch))
		{
			throw new SecurityAccessException("Password Incorrect");
		}
		return foundPart;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@CacheResult(cacheName = "UsersList")
	@Override
	public List<IInvolvedParty<?, ?>> getAllUsers(ISystems<?, ?> system, UUID... identityToken)
	{
		return (List) new InvolvedParty().builder()
		                                 .findByIdentificationType(IdentificationTypeUserName, null, system, identityToken)
		                                 .getAll();
	}
	
	@Override
	@CacheRemove(cacheName = "UsersList")
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IInvolvedParty<?, ?> addUpdateUsernamePassword(String username, String password, IInvolvedParty<?, ?> involvedParty, ISystems<?, ?> system, UUID... identityToken)
	{
		byte[] salt = System.getProperty("systemSalt") != null ? System.getProperty("systemSalt")
		                                                               .getBytes() : new Passwords().getNextSalt();
		
		String passEncrypted = encrypt(password, salt);
		String saltEncrypted = new Passwords().integerEncrypt(salt);
		;
		
		involvedParty.addOrUpdateClassification(SecurityPassword, null, passEncrypted, system, identityToken);
		involvedParty.addOrUpdateClassification(SecurityPasswordSalt, null, saltEncrypted, system, identityToken);
		IInvolvedPartyIdentificationType<?, ?> involvedPartyIdentificationType = involvedPartyService.findInvolvedPartyIdentificationType(IdentificationTypeUserName.toString(), system, identityToken);
		involvedParty.addOrUpdateInvolvedPartyIdentificationType(NoClassification.toString(), involvedPartyIdentificationType,
				null, username, system, identityToken);
		
		return involvedParty;
	}
	
	@Override
	public boolean doesUsernameExist(String username, ISystems<?, ?> system, UUID... identityToken)
	{
		return new InvolvedParty().builder()
		                          .withEnterprise(enterprise)
		                          .inActiveRange()
		                          .inDateRange()
		                          .findByIdentificationType(IdentificationTypeUserName, username, system, identityToken)
		                          .getCount() > 0;
	}
	
	@Override
	//@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public IInvolvedParty<?, ?> createAdminAndCreatorUserForEnterprise(ISystems<?, ?> system, String adminUserName,
	                                                                   @NotNull String adminPassword, UUID existingLocalKey)
	{
		logProgress("Checking base administrator user", "The default user is being checked for compliance", 1);
		
		UUID token = get(ISystemsService.class).getSecurityIdentityToken(system);
		
		SecurityToken administratorsGroup = (SecurityToken) get(SecurityTokenService.class).getAdministratorsFolder(system);
		
		InvolvedPartyService service = get(InvolvedPartyService.class);
		
		Pair<String, String> pair = new Pair<>(
				IdentificationTypes.IdentificationTypeEnterpriseCreatorRole.toString(), adminUserName);
		Optional<InvolvedParty> exists = new InvolvedParty().builder()
		                                                    .findByIdentificationType(
				                                                    IdentificationTypes.IdentificationTypeEnterpriseCreatorRole,
				                                                    adminUserName, system)
		                                                    .get();
		
		IInvolvedParty<?, ?> administratorUser;
		if (exists.isEmpty())
		{
			IInvolvedParty<?, ?> adminUser = service.create(system, pair, true);
			
			adminUser.addOrReuseInvolvedPartyIdentificationType(NoClassification.toString(), IdentificationTypes.IdentificationTypeUserName.toString(),
					adminUserName, system, token);
			
			adminUser.addOrReuseInvolvedPartyType(NoClassification.toString(), IPTypes.TypeIndividual.toString(), "Creator Individual", system, token);
			adminUser.addOrReuseInvolvedPartyNameType(NoClassification.toString(), PreferredNameType.toString(), "Enterprise Creator", system, token);
			adminUser.addOrReuseInvolvedPartyNameType(NoClassification.toString(), CommonNameType.toString(), "Enterprise Creator", system, token);
			adminUser.addOrReuseInvolvedPartyNameType(NoClassification.toString(), FullNameType.toString(), "Enterprise Creator", system, token);
			adminUser.addOrReuseInvolvedPartyNameType(NoClassification.toString(), FirstNameType.toString(), "Administrator", system, token);
			
			get(SecurityTokenService.class).create(SecurityTokenClassifications.Identity.toString(),
					adminUserName,
					"The creator of the enterprise", system, administratorsGroup, token);
			
			adminUser.addOrReuseInvolvedPartyIdentificationType(NoClassification.toString(), IdentificationTypes.IdentificationTypeEnterpriseCreatorRole.toString(),
					adminUserName, system, token);
			
			addUpdateUsernamePassword(adminUserName, adminPassword, adminUser, system, token);
			adminUser.createDefaultSecurity(system, token);
			administratorUser = adminUser;
		}
		else
		{
			administratorUser = exists.get();
		}
		return administratorUser;
	}
}
