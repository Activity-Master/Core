package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.SecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderCore;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.MappedSuperclass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serial;
import java.lang.reflect.ParameterizedType;

import static com.guicedee.client.IGuiceContext.get;


/**
 * @param <J>
 * @author Marc Magon
 * @version 1.0
 * @since 06 Dec 2016
 */
@MappedSuperclass()
public abstract class WarehouseCoreTable<J extends WarehouseCoreTable<J, Q, I, S>,
                                                Q extends QueryBuilderCore<Q, J, I>,
                                                I extends java.util.UUID,
                                                S extends WarehouseSecurityTable<S, ?, ?>
                                                >
        extends WarehouseBaseTable<J, Q, I>
        implements IWarehouseCoreTable<J, Q, I, S>
{
    private static final Logger LOGGER = LogManager.getLogger(WarehouseCoreTable.class);

    @Serial
    private static final long serialVersionUID = 1L;

    public WarehouseCoreTable()
    {

    }

    public abstract void configureSecurityEntity(S securityEntity);

    @Override
    public Uni<Void> createDefaultSecurity(ISystems<?, ?> system, java.util.UUID... identity)
    {
        if (ActivityMasterConfiguration.get()
                    .isSecurityEnabled())
        {
            LOGGER.info("Creating default security access for system: {}", system.getName());

            // For methods that already return Uni<S>, we need to use chain to avoid nesting Unis
            Uni<S> adminAccess = createDefaultAdministratorSecurityAccess(system, identity)
                .onSubscription().invoke(() -> LOGGER.debug("Creating administrator security access"));

            Uni<S> everyoneAccess = createDefaultEveryoneSecurityAccess(system, identity)
                .onSubscription().invoke(() -> LOGGER.debug("Creating everyone security access"));

            Uni<S> everywhereAccess = createDefaultEverywhereSecurityAccess(system, identity)
                .onSubscription().invoke(() -> LOGGER.debug("Creating everywhere security access"));

            Uni<S> systemsAccess = createDefaultSystemsSecurityAccess(system, identity)
                .onSubscription().invoke(() -> LOGGER.debug("Creating systems security access"));

            Uni<S> applicationsAccess = createDefaultApplicationsSecurityAccess(system, identity)
                .onSubscription().invoke(() -> LOGGER.debug("Creating applications security access"));

            Uni<S> pluginsAccess = createDefaultPluginsSecurityAccess(system, identity)
                .onSubscription().invoke(() -> LOGGER.debug("Creating plugins security access"));

            Uni<S> guestReadAccess = createDefaultGuestReadSecurityAccess(system, identity)
                .onSubscription().invoke(() -> LOGGER.debug("Creating guest read security access"));

            // Combine all operations to run in parallel
            Uni.combine()
                    .all()
                    .unis(
                            adminAccess,
                            everyoneAccess,
                            everywhereAccess,
                            systemsAccess,
                            applicationsAccess,
                            pluginsAccess,
                            guestReadAccess
                    )
                    .discardItems()
            ;

            // Return immediately without waiting for the operations to complete
            LOGGER.info("Default security creation initiated for system: {}", system.getName());
            return Uni.createFrom()
                           .voidItem();
        }

        LOGGER.info("Security is disabled, skipping default security creation");
        return Uni.createFrom()
                       .voidItem();
    }

    private Uni<S> createDefaultAdministratorSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getAdministratorsFolder(system, identity);
        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities<?,S,?> securities = stAdmin.builder();

        return securities.findLinkedSecurityToken(administrators, this)
                       //.inActiveRange(enterprise)
                       .inDateRange()
                       .setReturnFirst(true)
                       .get()
                       .onFailure()
                       .invoke(error -> LOGGER.error("Error finding linked security token: {}", error.getMessage(), error))
                       .chain(exists -> {
                           if (exists == null)
                           {
                               // No existing token found, create a new one
                               var stAdmin2 = configureDefaultsForNewToken(stAdmin, system);
                               stAdmin2.setSecurityTokenID(administrators);
                               stAdmin2.setCreateAllowed(true);
                               stAdmin2.setUpdateAllowed(true);
                               stAdmin2.setDeleteAllowed(true);
                               stAdmin2.setReadAllowed(true);
                               configureSecurityEntity(stAdmin2);
                               return stAdmin2.persist();
                           }
                           else
                           {
                               // Use existing token
                               return Uni.createFrom()
                                              .item(exists);
                           }
                       });
    }

    private Uni<S> createDefaultEveryoneSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getEveryoneGroup(system, identity);

        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities<?,S,?> securities = stAdmin.builder();

        return securities.findLinkedSecurityToken(administrators, this)
                       //.inActiveRange(enterprise)
                       .inDateRange()
                       .setReturnFirst(true)
                       .get()
                       .onFailure()
                       .invoke(error -> LOGGER.error("Error finding linked security token: {}", error.getMessage(), error))
                       .chain(exists -> {
                           if (exists == null)
                           {
                               // No existing token found, create a new one
                               var stAdmin2 = configureDefaultsForNewToken(stAdmin, system);
                               stAdmin2.setSecurityTokenID(administrators);
                               stAdmin2.setCreateAllowed(false);
                               stAdmin2.setUpdateAllowed(false);
                               stAdmin2.setDeleteAllowed(false);
                               stAdmin2.setReadAllowed(false);
                               configureSecurityEntity(stAdmin2);
                               return stAdmin2.persist();
                           }
                           else
                           {
                               // Use existing token
                               return Uni.createFrom()
                                              .item(exists);
                           }
                       });
    }

    private Uni<S> createDefaultEverywhereSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getEverywhereGroup(system, identity);

        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities<?,S,?> securities = stAdmin.builder();

        return securities.findLinkedSecurityToken(administrators, this)
                       //.inActiveRange(enterprise)
                       .inDateRange()
                       .setReturnFirst(true)
                       .get()
                       .onFailure()
                       .invoke(error -> LOGGER.error("Error finding linked security token: {}", error.getMessage(), error))
                       .chain(exists -> {
                           if (exists == null)
                           {
                               // No existing token found, create a new one
                               var stAdmin2 = configureDefaultsForNewToken(stAdmin, system);
                               stAdmin2.setSecurityTokenID(administrators);
                               stAdmin2.setCreateAllowed(false);
                               stAdmin2.setUpdateAllowed(false);
                               stAdmin2.setDeleteAllowed(false);
                               stAdmin2.setReadAllowed(false);
                               configureSecurityEntity(stAdmin2);
                               return stAdmin2.persist();
                           }
                           else
                           {
                               // Use existing token
                               return Uni.createFrom()
                                              .item(exists);
                           }
                       });
    }

    private Uni<S> createDefaultSystemsSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getSystemsFolder(system, identity);

        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities<?,S,?> securities = stAdmin.builder();

        return securities.findLinkedSecurityToken(administrators, this)
                       //.inActiveRange(enterprise)
                       .inDateRange()
                       .setReturnFirst(true)
                       .get()
                       .onFailure()
                       .invoke(error -> LOGGER.error("Error finding linked security token: {}", error.getMessage(), error))
                       .chain(exists -> {
                           if (exists == null)
                           {
                               // No existing token found, create a new one
                               var stAdmin2 = configureDefaultsForNewToken(stAdmin, system);
                               stAdmin2.setSecurityTokenID(administrators);
                               stAdmin2.setCreateAllowed(false);
                               stAdmin2.setUpdateAllowed(false);
                               stAdmin2.setDeleteAllowed(false);
                               stAdmin2.setReadAllowed(false);
                               configureSecurityEntity(stAdmin2);
                               return stAdmin2.persist();
                           }
                           else
                           {
                               // Use existing token
                               return Uni.createFrom()
                                              .item(exists);
                           }
                       });
    }

    private Uni<S> createDefaultApplicationsSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getApplicationsFolder(system, identity);

        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities<?,S,?> securities = stAdmin.builder();

        return securities.findLinkedSecurityToken(administrators, this)
                       //.inActiveRange(enterprise)
                       .inDateRange()
                       .setReturnFirst(true)
                       .get()
                       .onFailure()
                       .invoke(error -> LOGGER.error("Error finding linked security token: {}", error.getMessage(), error))
                       .chain(exists -> {
                           if (exists == null)
                           {
                               // No existing token found, create a new one
                               var stAdmin2 = configureDefaultsForNewToken(stAdmin, system);
                               stAdmin2.setSecurityTokenID(administrators);
                               stAdmin2.setCreateAllowed(false);
                               stAdmin2.setUpdateAllowed(false);
                               stAdmin2.setDeleteAllowed(false);
                               stAdmin2.setReadAllowed(false);
                               configureSecurityEntity(stAdmin2);
                               return stAdmin2.persist();
                           }
                           else
                           {
                               // Use existing token
                               return Uni.createFrom()
                                              .item(exists);
                           }
                       });
    }

    private Uni<S> createDefaultPluginsSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getPluginsFolder(system, identity);

        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities<?,S,?> securities = stAdmin.builder();

        return securities.findLinkedSecurityToken(administrators, this)
                       //.inActiveRange(enterprise)
                       .inDateRange()
                       .setReturnFirst(true)
                       .get()
                       .onFailure()
                       .invoke(error -> LOGGER.error("Error finding linked security token: {}", error.getMessage(), error))
                       .chain(exists -> {
                           if (exists == null)
                           {
                               // No existing token found, create a new one
                               var stAdmin2 = configureDefaultsForNewToken(stAdmin, system);
                               stAdmin2.setSecurityTokenID(administrators);
                               stAdmin2.setCreateAllowed(false);
                               stAdmin2.setUpdateAllowed(false);
                               stAdmin2.setDeleteAllowed(false);
                               stAdmin2.setReadAllowed(false);
                               configureSecurityEntity(stAdmin2);
                               return stAdmin2.persist();
                           }
                           else
                           {
                               // Use existing token
                               return Uni.createFrom()
                                              .item(exists);
                           }
                       });
    }

    private Uni<S> createDefaultGuestReadSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getGuestsFolder(system, identity);

        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities<?,S,?> securities = stAdmin.builder();

        return securities.findLinkedSecurityToken(administrators, this)
                       //.inActiveRange(enterprise)
                       .inDateRange()
                       .setReturnFirst(true)
                       .get()
                       .onFailure()
                       .invoke(error -> LOGGER.error("Error finding linked security token: {}", error.getMessage(), error))
                       .chain(exists -> {
                           if (exists == null)
                           {
                               // No existing token found, create a new one
                               var stAdmin2 = configureDefaultsForNewToken(stAdmin, system);
                               stAdmin2.setSecurityTokenID(administrators);
                               stAdmin2.setCreateAllowed(false);
                               stAdmin2.setUpdateAllowed(false);
                               stAdmin2.setDeleteAllowed(false);
                               stAdmin2.setReadAllowed(false);
                               configureSecurityEntity(stAdmin2);
                               return stAdmin2.persist();
                           }
                           else
                           {
                               // Use existing token
                               return Uni.createFrom()
                                              .item(exists);
                           }
                       });
    }

    @SuppressWarnings("unchecked")
    protected Class<S> findPersistentSecurityClass()
    {
        return (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
    }

    // TODO: In a future update, this method should be fully migrated to return Uni<S>
    protected S configureDefaultsForNewToken(S stAdmin, ISystems<?, ?> system)
    {
        // Call methods that return J directly
        stAdmin.setSystemID((Systems) system);
        stAdmin.setActiveFlagID(((Systems) system).getActiveFlagID());

        // Call methods that return Uni<J> but ignore the Uni return value for now
        // In a future update, these should be properly chained
        stAdmin.setOriginalSourceSystemID((Systems) system);
        stAdmin.setOriginalSourceSystemUniqueID(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"));
        stAdmin.setEnterpriseID((Enterprise) system.getEnterprise());

        return stAdmin;
    }

}
