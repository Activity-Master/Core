package com.guicedee.activitymaster.fsdm.db.abstraction;

import com.guicedee.activitymaster.fsdm.SecurityTokenService;
import com.guicedee.activitymaster.fsdm.SystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.base.IWarehouseCoreTable;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderCore;
import com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSecurities;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Promise;
import jakarta.persistence.MappedSuperclass;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static com.guicedee.client.IGuiceContext.*;


/**
 * @param <J>
 * @author Marc Magon
 * @version 1.0
 * @since 06 Dec 2016
 */
@SuppressWarnings("unchecked")
@MappedSuperclass()
public abstract class WarehouseCoreTable<J extends WarehouseCoreTable<J, Q, I, S>,
                                                Q extends QueryBuilderCore<Q, J, I>,
                                                I extends java.util.UUID,
                                                S extends WarehouseSecurityTable<S, ?, ?>
                                                >
        extends WarehouseBaseTable<J, Q, I>
        implements IWarehouseCoreTable<J, Q, I, S>
{
    @Serial
    private static final long serialVersionUID = 1L;

    public WarehouseCoreTable()
    {

    }

    public abstract void configureSecurityEntity(S securityEntity);

    @Override
    public Uni<Void> createDefaultSecurity(ISystems<?, ?> system, java.util.UUID... identity)
    {
		/*if (ActivityMasterConfiguration.get()
		                               .isSecurityEnabled())*/
        if (false)
        {
            createDefaultAdministratorSecurityAccess(system, identity);
            createDefaultEveryoneSecurityAccess(system, identity);
            createDefaultEverywhereSecurityAccess(system, identity);
            createDefaultSystemsSecurityAccess(system, identity);
            createDefaultApplicationsSecurityAccess(system, identity);
            createDefaultPluginsSecurityAccess(system, identity);
            createDefaultGuestReadSecurityAccess(system, identity);
        }
        return Uni.createFrom()
                       .voidItem();
    }

    public Uni<Void> updateSecurity(J newCoreTable, Systems system)
    {
        S stAdmin = get(findPersistentSecurityClass());
        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities<?, ?, ?> securities = (QueryBuilderSecurities) stAdmin.builder();
        return securities.findLinkedSecurityTokens(this)
                       //  .inActiveRange()
                       .inDateRange()
                       .getAll()
                       .chain(result -> {
                           Multi.createFrom()
                                   .iterable(result)
                                   .invoke(exist -> {
                                       exist.setId(null);
                                       configureDefaultsForNewToken((S) exist, system);
                                   })
                                   .log()
                           ;
                           return null;
                       })
                       .replaceWith(Uni.createFrom()
                                            .voidItem())
                ;
    }

    private Uni<S> createDefaultAdministratorSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getAdministratorsFolder(system, identity);
        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities<?, ?, ?> securities = stAdmin.builder();
        return (Uni) securities.findLinkedSecurityToken(administrators, this)
                //.inActiveRange(enterprise)
                .inDateRange()
                .setReturnFirst(true)
                .get()
                .onItemOrFailure()
                .call((result, throwable) -> {
                    if (throwable != null)
                    {
                        S stEntity = get(findPersistentSecurityClass());
                        configureDefaultsForNewToken(stEntity, system);
                        stEntity.setSecurityTokenID(administrators);
                        stEntity.setCreateAllowed(true);
                        stEntity.setUpdateAllowed(true);
                        stEntity.setDeleteAllowed(true);
                        stEntity.setReadAllowed(true);

                        var uni = stEntity.persist();
                        uni.chain(s -> {
                            configureSecurityEntity(stEntity);
                            return Uni.createFrom().item(s);
                        });
                        return uni;
                    }else {
                        return Uni.createFrom()
                                       .item(result);
                    }
                });
/*
        Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
                                     //.inActiveRange(enterprise)
                                     .inDateRange()
                                     .setReturnFirst(true)
                                     .get()
                ;

        if (exists.isEmpty())
        {
            stAdmin = configureDefaultsForNewToken(stAdmin, system);
            stAdmin.setSecurityTokenID(administrators);
            stAdmin.setCreateAllowed(true);
            stAdmin.setUpdateAllowed(true);
            stAdmin.setDeleteAllowed(true);
            stAdmin.setReadAllowed(true);
            configureSecurityEntity(stAdmin);
            stAdmin.persist();
        }
        else
        {
            stAdmin = exists.get();
        }*/
        //return stAdmin;
    }

    private Uni<S> createDefaultEveryoneSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getEveryoneGroup(system, identity);

        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities<?, ?, ?> securities = (QueryBuilderSecurities) stAdmin.builder();

        return (Uni) securities.findLinkedSecurityToken(administrators, this)
                //.inActiveRange(enterprise)
                .inDateRange()
                .setReturnFirst(true)
                .get()
                .onItemOrFailure()
                .call((result, throwable) -> {
                    if (throwable != null)
                    {
                        S stEntity = get(findPersistentSecurityClass());
                        configureDefaultsForNewToken(stEntity, system);
                        stEntity.setSecurityTokenID(administrators);
                        stEntity.setCreateAllowed(false);
                        stEntity.setUpdateAllowed(false);
                        stEntity.setDeleteAllowed(false);
                        stEntity.setReadAllowed(false);

                        var uni = stEntity.persist();
                        uni.chain(s -> {
                            configureSecurityEntity(stEntity);
                            return Uni.createFrom().item(s);
                        });
                        return uni;
                    }else {
                        return Uni.createFrom()
                                       .item(result);
                    }
                });

        /*
        Optional<S> exists = (Optional<S>) securities.findLinkedSecurityToken(administrators, this)
                                                   //.inActiveRange(enterprise)
                                                   .inDateRange()
                                                   .setReturnFirst(true)
                                                   .get();
        if (exists.isEmpty())
        {
            stAdmin.setSecurityTokenID(administrators);
            stAdmin = configureDefaultsForNewToken(stAdmin, system);
            stAdmin.setCreateAllowed(false);
            stAdmin.setUpdateAllowed(false);
            stAdmin.setDeleteAllowed(false);
            stAdmin.setReadAllowed(false);
            configureSecurityEntity(stAdmin);
            stAdmin.persist();
        }
        else
        {
            stAdmin = exists.get();
        }
        return stAdmin;*/
    }

    private Uni<S> createDefaultEverywhereSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getEverywhereGroup(system, identity);

        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();


        return (Uni) securities.findLinkedSecurityToken(administrators, this)
                //.inActiveRange(enterprise)
                .inDateRange()
                .setReturnFirst(true)
                .get()
                .onItemOrFailure()
                .call((result, throwable) -> {
                    if (throwable != null)
                    {
                        S stEntity = get(findPersistentSecurityClass());
                        configureDefaultsForNewToken(stEntity, system);
                        stEntity.setSecurityTokenID(administrators);
                        stEntity.setCreateAllowed(false);
                        stEntity.setUpdateAllowed(false);
                        stEntity.setDeleteAllowed(false);
                        stEntity.setReadAllowed(true);

                        var uni = stEntity.persist();
                        uni.chain(s -> {
                            configureSecurityEntity(stEntity);
                            return Uni.createFrom().item(s);
                        });
                        return uni;
                    }else {
                        return Uni.createFrom()
                                       .item(result);
                    }
                });
        /*
        Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
                                     //.inActiveRange(enterprise)
                                     .inDateRange()
                                     .setReturnFirst(true)
                                     .get()
                ;
        if (exists.isEmpty())
        {
            stAdmin.setSecurityTokenID(administrators);
            stAdmin = configureDefaultsForNewToken(stAdmin, system);

            stAdmin.setCreateAllowed(false);
            stAdmin.setUpdateAllowed(false);
            stAdmin.setDeleteAllowed(false);
            stAdmin.setReadAllowed(true);
            configureSecurityEntity(stAdmin);
            stAdmin.persist();
        }
        else
        {
            stAdmin = exists.get();
        }*/

        //return stAdmin;
    }

    private Uni<S> createDefaultSystemsSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getSystemsFolder(system, identity);

        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();


        return (Uni) securities.findLinkedSecurityToken(administrators, this)
                //.inActiveRange(enterprise)
                .inDateRange()
                .setReturnFirst(true)
                .get()
                .onItemOrFailure()
                .call((result, throwable) -> {
                    if (throwable != null)
                    {
                        S stEntity = get(findPersistentSecurityClass());
                        configureDefaultsForNewToken(stEntity, system);
                        stEntity.setSecurityTokenID(administrators);
                        stEntity.setCreateAllowed(true);
                        stEntity.setUpdateAllowed(true);
                        stEntity.setDeleteAllowed(false);
                        stEntity.setReadAllowed(true);

                        var uni = stEntity.persist();
                        uni.chain(s -> {
                            configureSecurityEntity(stEntity);
                            return Uni.createFrom().item(s);
                        });
                        return uni;
                    }else {
                        return Uni.createFrom()
                                       .item(result);
                    }
                });
/*
        Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
                                     //.inActiveRange(enterprise)
                                     .inDateRange()
                                     .setReturnFirst(true)
                                     .get()
                ;
        if (exists.isEmpty())
        {

            stAdmin.setSecurityTokenID(administrators);
            stAdmin = configureDefaultsForNewToken(stAdmin, system);

            stAdmin.setCreateAllowed(true);
            stAdmin.setUpdateAllowed(true);
            stAdmin.setDeleteAllowed(false);
            stAdmin.setReadAllowed(true);
            configureSecurityEntity(stAdmin);
            stAdmin.persist();
        }
        else
        {
            stAdmin = exists.get();
        }
        return stAdmin;*/
    }

    private Uni<S> createDefaultApplicationsSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getApplicationsFolder(system, identity);

        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();


        return (Uni) securities.findLinkedSecurityToken(administrators, this)
                //.inActiveRange(enterprise)
                .inDateRange()
                .setReturnFirst(true)
                .get()
                .onItemOrFailure()
                .call((result, throwable) -> {
                    if (throwable != null)
                    {
                        S stEntity = get(findPersistentSecurityClass());
                        configureDefaultsForNewToken(stEntity, system);
                        stEntity.setSecurityTokenID(administrators);
                        stEntity.setCreateAllowed(true);
                        stEntity.setUpdateAllowed(true);
                        stEntity.setDeleteAllowed(false);
                        stEntity.setReadAllowed(true);

                        var uni = stEntity.persist();
                        uni.chain(s -> {
                            configureSecurityEntity(stEntity);
                            return Uni.createFrom().item(s);
                        });
                        return uni;
                    }else {
                        return Uni.createFrom()
                                       .item(result);
                    }
                });
/*
        Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
                                     //.inActiveRange(enterprise)
                                     .inDateRange()
                                     .setReturnFirst(true)
                                     .get()
                ;
        if (exists.isEmpty())
        {

            stAdmin.setSecurityTokenID(administrators);
            stAdmin = configureDefaultsForNewToken(stAdmin, system);

            stAdmin.setCreateAllowed(true);
            stAdmin.setUpdateAllowed(true);
            stAdmin.setDeleteAllowed(false);
            stAdmin.setReadAllowed(true);
            configureSecurityEntity(stAdmin);
            stAdmin.persist();
        }
        else
        {
            stAdmin = exists.get();
        }
        return stAdmin;*/
    }

    private Uni<S> createDefaultPluginsSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getPluginsFolder(system, identity);

        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();


        return (Uni) securities.findLinkedSecurityToken(administrators, this)
                //.inActiveRange(enterprise)
                .inDateRange()
                .setReturnFirst(true)
                .get()
                .onItemOrFailure()
                .call((result, throwable) -> {
                    if (throwable != null)
                    {
                        S stEntity = get(findPersistentSecurityClass());
                        configureDefaultsForNewToken(stEntity, system);
                        stEntity.setSecurityTokenID(administrators);
                        stEntity.setCreateAllowed(true);
                        stEntity.setUpdateAllowed(true);
                        stEntity.setDeleteAllowed(false);
                        stEntity.setReadAllowed(true);

                        var uni = stEntity.persist();
                        uni.chain(s -> {
                            configureSecurityEntity(stEntity);
                            return Uni.createFrom().item(s);
                        });
                        return uni;
                    }else {
                        return Uni.createFrom()
                                       .item(result);
                    }
                });
/*
        Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
                                     //.inActiveRange(enterprise)
                                     .inDateRange()
                                     .setReturnFirst(true)
                                     .get()
                ;
        if (exists.isEmpty())
        {
            stAdmin.setSecurityTokenID(administrators);
            stAdmin = configureDefaultsForNewToken(stAdmin, system);

            stAdmin.setCreateAllowed(true);
            stAdmin.setUpdateAllowed(true);
            stAdmin.setDeleteAllowed(false);
            stAdmin.setReadAllowed(true);
            configureSecurityEntity(stAdmin);
            stAdmin.persist();
        }
        else
        {
            stAdmin = exists.get();
        }
        return stAdmin;*/
    }

    private Uni<S> createDefaultGuestReadSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getGuestsFolder(system, identity);

        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();


        return (Uni) securities.findLinkedSecurityToken(administrators, this)
                //.inActiveRange(enterprise)
                .inDateRange()
                .setReturnFirst(true)
                .get()
                .onItemOrFailure()
                .call((result, throwable) -> {
                    if (throwable != null)
                    {
                        S stEntity = get(findPersistentSecurityClass());
                        configureDefaultsForNewToken(stEntity, system);
                        stEntity.setSecurityTokenID(administrators);
                        stEntity.setCreateAllowed(false);
                        stEntity.setUpdateAllowed(false);
                        stEntity.setDeleteAllowed(false);
                        stEntity.setReadAllowed(true);

                        var uni = stEntity.persist();
                        uni.chain(s -> {
                            configureSecurityEntity(stEntity);
                            return Uni.createFrom().item(s);
                        });
                        return uni;
                    }else {
                        return Uni.createFrom()
                                       .item(result);
                    }
                });
/*
        Optional<S> exists = securities.findLinkedSecurityToken(administrators, this)
                                     //.inActiveRange(enterprise)
                                     .inDateRange()
                                     .setReturnFirst(true)
                                     .get()
                ;
        if (exists.isEmpty())
        {
            stAdmin.setSecurityTokenID(administrators);
            configureDefaultsForNewToken(stAdmin, system);

            stAdmin.setCreateAllowed(false);
            stAdmin.setUpdateAllowed(false);
            stAdmin.setDeleteAllowed(false);
            stAdmin.setReadAllowed(true);
            configureSecurityEntity(stAdmin);
            stAdmin.persist();
        }
        else
        {
            stAdmin = exists.get();
        }

        return stAdmin;*/
    }

    @SuppressWarnings("unchecked")
    protected Class<S> findPersistentSecurityClass()
    {
        return (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[3];
    }

    protected void configureDefaultsForNewToken(S stAdmin, ISystems<?, ?> system)
    {
        stAdmin.setSystemID((Systems) system);
        stAdmin.setActiveFlagID(((Systems) system).getActiveFlagID());
        stAdmin.setOriginalSourceSystemID((Systems) system);
        stAdmin.setOriginalSourceSystemUniqueID(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"));
        stAdmin.setEnterpriseID((Enterprise) system.getEnterprise());
    }

    public Uni<S> createDefaultGuestNoSecurityAccess(ISystems<?, ?> system, java.util.UUID... identity)
    {
        S stAdmin = get(findPersistentSecurityClass());
        SecurityToken administrators = (SecurityToken) get(SecurityTokenService.class)
                                                               .getGuestsFolder(system, identity);

        @SuppressWarnings("rawtypes")
        QueryBuilderSecurities securities = (QueryBuilderSecurities) stAdmin.builder();


        return (Uni) securities.findLinkedSecurityToken(administrators, this)
                //.inActiveRange(enterprise)
                .inDateRange()
                .setReturnFirst(true)
                .get()
                .onItemOrFailure()
                .call((result, throwable) -> {
                    if (throwable != null)
                    {
                        S stEntity = get(findPersistentSecurityClass());
                        configureDefaultsForNewToken(stEntity, system);
                        stEntity.setSecurityTokenID(administrators);
                        stEntity.setCreateAllowed(false);
                        stEntity.setUpdateAllowed(false);
                        stEntity.setDeleteAllowed(false);
                        stEntity.setReadAllowed(false);

                        var uni = stEntity.persist();
                        uni.chain(s -> {
                            configureSecurityEntity(stEntity);
                            return Uni.createFrom().item(s);
                        });
                        return uni;
                    }else {
                        return Uni.createFrom()
                                       .item(result);
                    }
                });

/*

        Optional<S> exists = ActivityMasterConfiguration.get()
                                     .isSecurityEnabled() ? Optional.empty() :
                                     securities.findLinkedSecurityToken(administrators, this)
                                             //.inActiveRange(enterprise)
                                             .inDateRange()
                                             .get();
        if (exists.isEmpty())
        {
            stAdmin.setSecurityTokenID(administrators);
            stAdmin = configureDefaultsForNewToken(stAdmin, system);

            stAdmin.setCreateAllowed(false);
            stAdmin.setUpdateAllowed(false);
            stAdmin.setDeleteAllowed(false);
            stAdmin.setReadAllowed(false);
            configureSecurityEntity(stAdmin);
            stAdmin.persist();
        }
        else
        {
            stAdmin = exists.get();
        }

        return stAdmin;*/
    }

}
