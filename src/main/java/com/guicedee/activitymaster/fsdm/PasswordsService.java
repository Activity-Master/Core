package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.guicedee.activitymaster.fsdm.api.Passwords;
import com.guicedee.guicedpersistence.lambda.TransactionalSupplier;
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
import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedinjection.pairing.Pair;
import jakarta.validation.constraints.NotNull;

import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.InvolvedPartyClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.NameTypes.*;
import static com.guicedee.client.IGuiceContext.*;

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
	
	@Transactional()
	@Override
	public IInvolvedParty<?, ?> findByUsername(String username, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		IInvolvedParty<?, ?> party = new InvolvedParty().builder()
		                                                .withEnterprise(enterprise)
		                                                .findByIdentificationType(IdentificationTypeUserName, username,
				                                                system, identityToken)
		                                                .get()
		                                                .orElseThrow(() -> new SecurityAccessException("Involved Party Does Not Exist"));
		return party;
	}
	
	@Transactional()
	@Override
	public IInvolvedParty<?, ?> findByUsernameAndPassword(String username, String password, ISystems<?, ?> system, boolean throwForNoUser, java.util.UUID... identityToken)
	{
		if (!doesUsernameExist(username, system, identityToken))
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
		
		UUID systemToken = get(InvolvedPartySystem.class).getSystemToken(system.getEnterpriseID());
		InvolvedParty foundPart = new InvolvedParty().builder()
		                                             .findByIdentificationType(IdentificationTypeUserName,
				                                             username, system,
				                                             systemToken)
		                                             .get()
		                                             .orElse(null);
		if (foundPart == null)
		{
			throw new SecurityAccessException("Unable to find any Involved Party with that username");
		}
		
		Optional<IRelationshipValue<InvolvedParty, IClassification<?, ?>, ?>> saltEntity = foundPart.findClassification(SecurityPasswordSalt, system, systemToken);
		Optional<IRelationshipValue<InvolvedParty, IClassification<?, ?>, ?>> passEntity = foundPart.findClassification(SecurityPassword, system, systemToken);
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
	
	@Transactional()
	@SuppressWarnings({"unchecked", "rawtypes"})
	@CacheResult(cacheName = "UsersList")
	@Override
	public List<IInvolvedParty<?, ?>> getAllUsers(ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return (List) new InvolvedParty().builder()
		                                 .findByIdentificationType(IdentificationTypeUserName, null, system, identityToken)
		                                 .getAll();
	}
	@Transactional()
	@Override
	@CacheRemove(cacheName = "UsersList")
	//@Transactional()
	public IInvolvedParty<?, ?> addUpdateUsernamePassword(String username, String password, IInvolvedParty<?, ?> involvedParty, ISystems<?, ?> system, java.util.UUID... identityToken)
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
	@Transactional()
	@Override
	public boolean doesUsernameExist(String username, ISystems<?, ?> system, java.util.UUID... identityToken)
	{
		return new InvolvedParty().builder()
		                          .withEnterprise(enterprise)
		                          .inActiveRange()
		                          .inDateRange()
		                          .findByIdentificationType(IdentificationTypeUserName, username, system, identityToken)
		                          .getCount() > 0;
	}
	
	@Override
	@Transactional()
	public IInvolvedParty<?, ?> createAdminAndCreatorUserForEnterprise(ISystems<?, ?> system, String adminUserName,
	                                                                   @NotNull String adminPassword, UUID existingLocalKey)
	{
		logProgress("Checking base administrator user", "The default user is being checked for compliance", 1);
		
		UUID identityToken = get(ISystemsService.class).getSecurityIdentityToken(system);
		
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
			var c = service.create(system, pair, true)
					.whenComplete((adminUser,error)->{
						TransactionalSupplier<IInvolvedParty<?,?>> ts = IGuiceContext.get(TransactionalSupplier.class);
						ts.setConsumer(()->{
							adminUser.addOrReuseInvolvedPartyIdentificationType(NoClassification.toString(), IdentificationTypes.IdentificationTypeUserName.toString(),
									adminUserName, system, identityToken);
							
							adminUser.addOrReuseInvolvedPartyType(NoClassification.toString(), IPTypes.TypeIndividual.toString(), "Creator Individual", system, identityToken);
							adminUser.addOrReuseInvolvedPartyNameType(NoClassification.toString(), PreferredNameType.toString(), "Enterprise Creator", system, identityToken);
							adminUser.addOrReuseInvolvedPartyNameType(NoClassification.toString(), CommonNameType.toString(), "Enterprise Creator", system, identityToken);
							adminUser.addOrReuseInvolvedPartyNameType(NoClassification.toString(), FullNameType.toString(), "Enterprise Creator", system, identityToken);
							adminUser.addOrReuseInvolvedPartyNameType(NoClassification.toString(), FirstNameType.toString(), "Administrator", system, identityToken);
							
							get(SecurityTokenService.class).create(SecurityTokenClassifications.Identity.toString(),
									adminUserName,
									"The creator of the enterprise", system, administratorsGroup, identityToken);
							
							adminUser.addOrReuseInvolvedPartyIdentificationType(NoClassification.toString(), IdentificationTypes.IdentificationTypeEnterpriseCreatorRole.toString(),
									adminUserName, system, identityToken);
							
							addUpdateUsernamePassword(adminUserName, adminPassword, adminUser, system, identityToken);
							((InvolvedParty)adminUser).createDefaultSecurity(system, identityToken);
							return adminUser;
						});
					})
					;
			try
			{
				administratorUser = c.get();
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException(e);
			}
			catch (ExecutionException e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			administratorUser = exists.get();
		}
		return administratorUser;
	}
}
