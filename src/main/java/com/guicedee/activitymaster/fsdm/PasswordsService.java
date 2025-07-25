package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
import com.guicedee.activitymaster.fsdm.api.Passwords;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyIdentificationType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyNameType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedPartyType;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.SecurityTokenClassifications;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.IPTypes;
import com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.SecurityAccessException;
import com.guicedee.activitymaster.fsdm.db.entities.involvedparty.InvolvedParty;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.systems.InvolvedPartySystem;
import com.guicedee.guicedinjection.pairing.Pair;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.*;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.InvolvedPartyClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.IdentificationTypes.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.NameTypes.*;
import static com.guicedee.client.IGuiceContext.*;

@Log4j2
public class PasswordsService implements IPasswordsService<PasswordsService>
{

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
    public Uni<IInvolvedParty<?, ?>> findByUsername(Mutiny.Session session, String username, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Finding involved party by username: {}", username);
        var enterprise = system.getEnterprise();
        return new InvolvedParty().builder(session)
                       .withEnterprise(enterprise)
                       .findByIdentificationType(IdentificationTypeUserName, username, system, identityToken)
                       .get()
                       .onItem()
                       .ifNull()
                       .failWith(() -> new SecurityAccessException("Involved Party Does Not Exist"))
                       .map(party -> (IInvolvedParty<?, ?>) party);
    }

    @Override
    public Uni<IInvolvedParty<?, ?>> findByUsernameAndPassword(Mutiny.Session session, String username, String password, ISystems<?, ?> system, boolean throwForNoUser, UUID... identityToken)
    {
        log.debug("Finding involved party by username and password: {}", username);
        return doesUsernameExist(session, username, system, identityToken)
                       .chain(exists -> {
                           if (!exists)
                           {
                               if (throwForNoUser)
                               {
                                   return Uni.createFrom()
                                                  .failure(new SecurityAccessException("Invalid Username"));
                               }
                               else
                               {
                                   return Uni.createFrom()
                                                  .nullItem();
                               }
                           }

                           // Get system token
                           return get(InvolvedPartySystem.class).getSystemToken(session, system.getEnterpriseID())
                                          .chain(systemToken -> {
                                              // Find involved party by username
                                              return new InvolvedParty().builder(session)
                                                             .findByIdentificationType(IdentificationTypeUserName, username, system, systemToken)
                                                             .get()
                                                             .onItem()
                                                             .ifNull()
                                                             .failWith(() -> new SecurityAccessException("Unable to find any Involved Party with that username"))
                                                             .chain(foundPart -> {
                                                                 // Get salt and password classifications
                                                                 Uni<IRelationshipValue<InvolvedParty, IClassification<?, ?>, ?>> saltEntityUni =
                                                                         foundPart.findClassification(session, SecurityPasswordSalt, system, systemToken)
                                                                                 .onItem()
                                                                                 .ifNull()
                                                                                 .failWith(() -> new SecurityAccessException("Involved Party does not have salt credentials"))
                                                                         ;

                                                                 Uni<IRelationshipValue<InvolvedParty, IClassification<?, ?>, ?>> passEntityUni =
                                                                         foundPart.findClassification(session, SecurityPassword, system, systemToken)
                                                                                 .onItem()
                                                                                 .ifNull()
                                                                                 .failWith(() -> new SecurityAccessException("Involved Party does not have password credentials"))
                                                                         ;

                                                                 // Combine salt and password entities
                                                                 return Uni.combine()
                                                                                .all()
                                                                                .unis(saltEntityUni, passEntityUni)
                                                                                .asTuple()
                                                                                .chain(tuple -> {
                                                                                    IRelationshipValue<InvolvedParty, IClassification<?, ?>, ?> saltEntity = tuple.getItem1();
                                                                                    IRelationshipValue<InvolvedParty, IClassification<?, ?>, ?> passEntity = tuple.getItem2();

                                                                                    String saltString = saltEntity.getValue();
                                                                                    byte[] salt = new Passwords().integerDecrypt(saltString);
                                                                                    String passMatch = passEntity.getValue();
                                                                                    String passEncrypted = encrypt(password, salt);

                                                                                    if (!passEncrypted.equalsIgnoreCase(passMatch))
                                                                                    {
                                                                                        return Uni.createFrom()
                                                                                                       .failure(new SecurityAccessException("Password Incorrect"));
                                                                                    }

                                                                                    return Uni.createFrom()
                                                                                                   .item((IInvolvedParty<?, ?>) foundPart);
                                                                                });
                                                             });
                                          });
                       });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Uni<List<IInvolvedParty<?, ?>>> getAllUsers(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Getting all users for system: {}", system.getName());
        return new InvolvedParty().builder(session)
                       .findByIdentificationType(IdentificationTypeUserName, null, system, identityToken)
                       .getAll()
                       .onFailure()
                       .invoke(error -> log.error("Error getting all users: {}", error.getMessage(), error))
                       .map(list -> {
                           List<IInvolvedParty<?, ?>> result = new ArrayList<>();
                           for (Object item : list)
                           {
                               result.add((IInvolvedParty<?, ?>) item);
                           }
                           return result;
                       });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Uni<IInvolvedParty<?, ?>> addUpdateUsernamePassword(Mutiny.Session session, String username, String password, IInvolvedParty<?, ?> involvedParty, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Adding/updating username and password for involved party: {}", involvedParty.getId());
        // Generate salt and encrypt password
        byte[] salt = System.getProperty("systemSalt") != null ?
                              System.getProperty("systemSalt")
                                      .getBytes() :
                              new Passwords().getNextSalt();

        String passEncrypted = encrypt(password, salt);
        String saltEncrypted = new Passwords().integerEncrypt(salt);

        // First add password classification
        return (Uni) involvedParty.addOrUpdateClassification(session, SecurityPassword, null, passEncrypted, system, identityToken)
                       .chain(() -> {
                           // Then add salt classification
                           return involvedParty.addOrUpdateClassification(session, SecurityPasswordSalt, null, saltEncrypted, system, identityToken);
                       })
                       .chain(() -> {
                           // Get identification type
                           return involvedPartyService.findInvolvedPartyIdentificationType(
                                   session, IdentificationTypeUserName.toString(), system, identityToken);
                       })
                       .chain(identificationType -> {
                           // Add identification type
                           return involvedParty.addOrUpdateInvolvedPartyIdentificationType(
                                   session, NoClassification.toString(), identificationType,
                                   null, username, system, identityToken);
                       })
                       .map(result -> involvedParty)
                       .onFailure()
                       .invoke(error -> log.error("Error adding/updating username and password: {}", error.getMessage(), error));
    }

    @Override
    public Uni<Boolean> doesUsernameExist(Mutiny.Session session, String username, ISystems<?, ?> system, UUID... identityToken)
    {
        log.debug("Checking if username exists: {}", username);
        var enterprise = system.getEnterprise();
        return new InvolvedParty().builder(session)
                       .withEnterprise(enterprise)
                       .inActiveRange()
                       .inDateRange()
                       .findByIdentificationType(IdentificationTypeUserName, username, system, identityToken)
                       .getCount()
                       .onFailure()
                       .invoke(error -> log.error("Error checking if username exists: {}", error.getMessage(), error))
                       .map(count -> count > 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Uni<IInvolvedParty<?, ?>> createAdminAndCreatorUserForEnterprise(Mutiny.Session session, ISystems<?, ?> system, String adminUserName,
                                                                            @NotNull String adminPassword, UUID existingLocalKey)
    {
        log.debug("Creating admin and creator user for enterprise: {}", system.getEnterpriseID());
        logProgress("Checking base administrator user", "The default user is being checked for compliance", 1);

        // Get security identity token
        ISystemsService<?> systemsService = get(ISystemsService.class);
        return (Uni) systemsService.getSecurityIdentityToken(session, system)
                       .chain(identityToken -> {
                           // Get administrators folder
                           return Uni.createFrom()
                                          .item(() -> get(SecurityTokenService.class).getAdministratorsFolder(session, system))
                                          .chain(administratorsGroup -> {
                                              // Check if user already exists
                                              Pair<String, String> pair = new Pair<>(
                                                      IdentificationTypes.IdentificationTypeEnterpriseCreatorRole.toString(), adminUserName);

                                              return (Uni) new InvolvedParty().builder(session)
                                                             .findByIdentificationType(
                                                                     IdentificationTypes.IdentificationTypeEnterpriseCreatorRole,
                                                                     adminUserName, system)
                                                             .get()
                                                             .onItem()
                                                             .ifNotNull()
                                                             .transform(existingUser -> (IInvolvedParty<?, ?>) existingUser)
                                                             .onFailure()
                                                             .recoverWithUni(failure -> {
                                                                 // Create new user
                                                                 IInvolvedPartyService<?> service = get(IInvolvedPartyService.class);
                                                                 return (Uni) service.create(session, system, pair, true)
                                                                                .chain(adminUser -> {
                                                                                    // Run all admin user setup operations in parallel
                                                                                    Uni<? extends IRelationshipValue<?, IInvolvedPartyIdentificationType<?, ?>, ?>> userNameIdentification = adminUser.addOrReuseInvolvedPartyIdentificationType(
                                                                                            session, NoClassification.toString(),
                                                                                            IdentificationTypeUserName.toString(),
                                                                                            adminUserName, system, identityToken);

                                                                                    Uni<? extends IRelationshipValue<?, IInvolvedPartyType<?, ?>, ?>> partyType = adminUser.addOrReuseInvolvedPartyType(
                                                                                            session, NoClassification.toString(),
                                                                                            IPTypes.TypeIndividual.toString(),
                                                                                            "Creator Individual", system, identityToken);

                                                                                    Uni<? extends IRelationshipValue<?, IInvolvedPartyNameType<?, ?>, ?>> preferredName = adminUser.addOrReuseInvolvedPartyNameType(
                                                                                            session, NoClassification.toString(),
                                                                                            PreferredNameType.toString(),
                                                                                            "Enterprise Creator", system, identityToken);

                                                                                    Uni<? extends IRelationshipValue<?, IInvolvedPartyNameType<?, ?>, ?>> commonName = adminUser.addOrReuseInvolvedPartyNameType(
                                                                                            session, NoClassification.toString(),
                                                                                            CommonNameType.toString(),
                                                                                            "Enterprise Creator", system, identityToken);

                                                                                    Uni<? extends IRelationshipValue<?, IInvolvedPartyNameType<?, ?>, ?>> fullName = adminUser.addOrReuseInvolvedPartyNameType(
                                                                                            session, NoClassification.toString(),
                                                                                            FullNameType.toString(),
                                                                                            "Enterprise Creator", system, identityToken);

                                                                                    Uni<? extends IRelationshipValue<?, IInvolvedPartyNameType<?, ?>, ?>> firstName = adminUser.addOrReuseInvolvedPartyNameType(
                                                                                            session, NoClassification.toString(),
                                                                                            FirstNameType.toString(),
                                                                                            "Administrator", system, identityToken);

                                                                                    Uni<ISecurityToken<?, ?>> securityToken = get(SecurityTokenService.class).create(
                                                                                            session, SecurityTokenClassifications.Identity.toString(),
                                                                                            adminUserName,
                                                                                            "The creator of the enterprise",
                                                                                            system,
                                                                                            (SecurityToken) administratorsGroup,
                                                                                            identityToken);

                                                                                    Uni<? extends IRelationshipValue<?, IInvolvedPartyIdentificationType<?, ?>, ?>> creatorRole = adminUser.addOrReuseInvolvedPartyIdentificationType(
                                                                                            session, NoClassification.toString(),
                                                                                            IdentificationTypeEnterpriseCreatorRole.toString(),
                                                                                            adminUserName, system, identityToken);

                                                                                    Uni<IInvolvedParty<?, ?>> usernamePassword = addUpdateUsernamePassword(
                                                                                            session, adminUserName, adminPassword, adminUser, system, identityToken);

                                                                                    Uni<Void> defaultSecurity = ((InvolvedParty) adminUser).createDefaultSecurity(session, system, identityToken);

                                                                                    // Combine all parallel operations
                                                                                    return Uni.combine()
                                                                                            .all()
                                                                                            .unis(userNameIdentification, partyType, preferredName, commonName, 
                                                                                                  fullName, firstName, securityToken, creatorRole, 
                                                                                                  usernamePassword, defaultSecurity)
                                                                                            .discardItems();
                                                                                });
                                                             });
                                          });
                       })
                       .onFailure()
                       .invoke(error -> log.error("Error creating admin user: {}", error));
    }
}
