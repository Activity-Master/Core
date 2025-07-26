package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
import com.guicedee.activitymaster.fsdm.client.services.*;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.UserGroupSecurityTokenClassifications;
import com.guicedee.activitymaster.fsdm.db.entities.activeflag.ActiveFlag;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.security.*;
import com.guicedee.activitymaster.fsdm.db.entities.security.builders.SecurityTokenQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.systems.Systems;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotNull;
import org.hibernate.reactive.mutiny.Mutiny;


import java.util.*;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.SecurityTokenClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.UserGroupSecurityTokenClassifications.*;

@SuppressWarnings("Duplicates")

public class SecurityTokenService
		implements ISecurityTokenService<SecurityTokenService>
{
	@Inject
	private IClassificationService<?> classificationService;

	@Override
	public Uni<ISecurityToken<?, ?>> get()
	{
		return Uni.createFrom().item(new SecurityToken());
	}

	@Override
	public Uni<Void> grantAccessToToken(Mutiny.Session session, ISecurityToken<?,?> fromToken, ISecurityToken<?,?> toToken,
										boolean create, boolean update, boolean delete, boolean read, ISystems<?,?> system)
	{
		return grantAccessToToken(session, fromToken, toToken, create, update, delete, read, system, null, null, null);
	}

	@Override
	public Uni<Void> grantAccessToToken(Mutiny.Session session, @NotNull ISecurityToken<?,?> fromToken, @NotNull ISecurityToken<?,?> toToken,
										boolean create, boolean update, boolean delete, boolean read,
										ISystems<?,?> system, String originalId,
										Date effectiveFromDate, Date effectiveToDate)
	{
			SecurityTokensSecurityToken sta = new SecurityTokensSecurityToken();
			var enterprise = system.getEnterprise();
			return sta.builder(session)
				.withEnterprise(enterprise)
				.inActiveRange()
				.inDateRange()
				.findBySecurityToken((SecurityToken) fromToken, (SecurityToken) toToken)
				.get()
				.onItem().ifNotNull().transform(exists -> exists)
				.onItem().ifNull().switchTo(() -> {
					sta.setSystemID((Systems) system);
					sta.setOriginalSourceSystemID((Systems) system);
					sta.setEnterpriseID(enterprise);
					sta.setOriginalSourceSystemUniqueID(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"));
					IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
					return acService.getActiveFlag(session, enterprise)
						.chain(activeFlag -> {
							sta.setActiveFlagID((ActiveFlag) activeFlag);
							sta.setSecurityTokenID((SecurityToken) fromToken);
							sta.setBase((SecurityToken) toToken);
							sta.setCreateAllowed(create);
							sta.setUpdateAllowed(update);
							sta.setDeleteAllowed(delete);
							sta.setReadAllowed(read);
							return session.persist(sta).replaceWith(Uni.createFrom().item(sta));
						});
				})
				.chain(result -> Uni.createFrom().voidItem());

	}

	@Override
	public Uni<ISecurityToken<?,?>> create(Mutiny.Session session, String classificationValue, String name, String description, ISystems<?,?> system)
	{
		return create(session, classificationValue, name, description, system, null);
	}

	@Override
	public Uni<ISecurityToken<?,?>> create(Mutiny.Session session, String classificationValue, String name, String description, ISystems<?,?> system, ISecurityToken<?,?> parent, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
			return classificationService.find(session, classificationValue, system, identityToken)
				.chain(classification -> {
					SecurityToken st = new SecurityToken();

					// First try to find by security token and enterprise
					return st.builder(session)
						.withEnterprise(enterprise)
						.findBySecurityToken(name, enterprise)
						.inActiveRange()
						.inDateRange()
						.withEnterprise(enterprise)
						.get()
						.onFailure(NoSuchElementException.class).recoverWithNull()
						.chain(existingToken -> {
							if (existingToken != null) {
								// Token already exists
								return Uni.createFrom().item(existingToken);
							}

							// Try to find by name
							return st.builder(session)
								.withName(name)
								.inActiveRange()
								.inDateRange()
								.get()
								.onFailure(NoSuchElementException.class).recoverWithNull()
								.chain(existingNameToken -> {
									if (existingNameToken != null) {
										// Token with this name already exists
										return Uni.createFrom().item(existingNameToken);
									}

									// Create new token
									st.setName(name);
									st.setDescription(description);
									st.setSystemID(system);
									st.setSecurityToken(UUID.randomUUID().toString());
									st.setEnterpriseID(enterprise);
									st.setSystemID(((Classification) classification).getSystemID());
									st.setOriginalSourceSystemID(((Classification) classification).getSystemID());
									st.setSecurityTokenClassificationID((Classification) classification);

									IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
									return acService.getActiveFlag(session, enterprise)
										.chain(activeFlag -> {
											st.setActiveFlagID((ActiveFlag) activeFlag);
											return session.persist(st).replaceWith(Uni.createFrom().item(st))
												.chain(() -> {
													st.createDefaultSecurity(session, system, identityToken);
													return Uni.createFrom().item(st);
												});
										});
								});
						})
						.chain(securityToken -> {
							if (parent == null) {
								return Uni.createFrom().item(securityToken);
							}

							return link(session, parent, securityToken, (Classification) classification)
								.map(v -> securityToken);
						});
				});
	}

	@Override
	public Uni<Void> link(Mutiny.Session session, ISecurityToken<?,?> parent, ISecurityToken<?,?> child, IClassification<?,?> classification, String... identifyingToken)
	{
			SecurityTokenXSecurityToken root = new SecurityTokenXSecurityToken();
			var enterprise = child.getEnterprise();
			return root.builder(session)
				.withEnterprise(enterprise)
				.findLink((SecurityToken) parent, (SecurityToken) child, null)
				.withClassification(classification)
				.inActiveRange()
				.inDateRange()
				.get()
				.onFailure(NoSuchElementException.class).recoverWithItem(() -> {
					// No existing link found, create a new one
					root.setParentSecurityTokenID((SecurityToken) parent);
					root.setChildSecurityTokenID((SecurityToken) child);
					root.setClassificationID(classification);
					root.setSystemID(((SecurityToken) parent).getSystemID());
					root.setOriginalSourceSystemID(((SecurityToken) parent).getSystemID());
					root.setValue(child.getSecurityToken());
					root.setEnterpriseID(enterprise);

					IActiveFlagService<?> acService = com.guicedee.client.IGuiceContext.get(IActiveFlagService.class);
					return acService.getActiveFlag(session, enterprise)
						.chain(activeFlag -> {
							root.setActiveFlagID((ActiveFlag) activeFlag);
							return session.persist(root).replaceWith(Uni.createFrom().item(root))
								.map(v -> {
									updateSecurityHierarchy(child.getId());
									return root;
								});
						})
						.await().indefinitely(); // This is needed to convert from Uni to actual value in this recovery path
				})
				.chain(existingLink -> Uni.createFrom().voidItem());
	}

	private void updateSecurityHierarchy(UUID securityTokenID)
	{

		//TODO hierarchy updates? i wonder
		/*javax.sql.DataSource ds = get(javax.sql.DataSource.class, ActivityMasterDB.class);

		try (java.sql.Connection c = ds.getConnection();
		     java.sql.CallableStatement st = c.prepareCall("{call MergeHierarchy (?)}");
		     java.sql.CallableStatement stPar = c.prepareCall("{call UpdateSecurityHierarchy (?)}")
		)
		{
			st.setLong(1, securityTokenID);
			st.execute();
			stPar.setLong(1, securityTokenID);
			stPar.execute();
		}
		catch (java.sql.SQLException e)
		{
			log.log(Level.SEVERE, "Unable to execute updates to hierarchy", e);
		}*/
	}
	//@CacheResult(cacheName = "SecuritiesGetEveryoneGroup")
	@Override
	public Uni<ISecurityToken<?,?>> getEveryoneGroup(Mutiny.Session session, ISystems<?,?> system, UUID... identityToken)
	{
		
			SecurityToken st = new SecurityToken();
			var enterprise = system.getEnterprise();
			return st.builder(session)
				.findFolder(UserGroup.toString(), system, identityToken)
				.withName(Everyone)
				.inActiveRange()
				.inDateRange()
				.withEnterprise(enterprise)
				//   .canRead(enterprise, identityToken)
				.get()
				.onItem().transform(token -> (ISecurityToken<?,?>) token);

	}
	//@CacheResult(cacheName = "SecuritiesGetEverywhereGroup")
	@Override
	public Uni<ISecurityToken<?,?>> getEverywhereGroup(Mutiny.Session session, ISystems<?,?> system, UUID... identityToken)
	{
	
			SecurityToken st = new SecurityToken();
			var enterprise = system.getEnterprise();
			return st.builder(session)
				.findFolder(UserGroup.toString(), system, identityToken)
				.withName(Everywhere)
				.inActiveRange()
				.inDateRange()
				.withEnterprise(enterprise)
				//      .canRead(enterprise, identityToken)
				.get()
				.onItem().transform(token -> (ISecurityToken<?,?>) token);
	
	}
	//@CacheResult(cacheName = "SecuritiesGetGuestsFolder")
	@Override
	public Uni<ISecurityToken<?,?>> getGuestsFolder(Mutiny.Session session, ISystems<?,?> system, UUID... identityToken)
	{
	
			SecurityToken st = new SecurityToken();
			var enterprise = system.getEnterprise();
			return st.builder(session)
				.findFolder(UserGroup.toString(), system, identityToken)
				.withName(Guests)
				.inActiveRange()
				.inDateRange()
				.withEnterprise(enterprise)
				//     .canRead(enterprise,identityToken)
				.get()
				.onItem().transform(token -> (ISecurityToken<?,?>) token);
	
	}
	//@CacheResult(cacheName = "SecuritiesGetRegisteredGuestsFolder")
	@Override
	public Uni<ISecurityToken<?,?>> getRegisteredGuestsFolder(Mutiny.Session session, ISystems<?,?> system, UUID... identityToken)
	{
			SecurityToken st = new SecurityToken();
			var enterprise = system.getEnterprise();
			return st.builder(session)
				.findFolder(UserGroup.toString(), system, identityToken)
				.withName(Registered)
				.inActiveRange()
				.inDateRange()
				.withEnterprise(enterprise)
				//    .canRead(enterprise, identityToken)
				.get()
				.onItem().transform(token -> (ISecurityToken<?,?>) token);

	}
	//@CacheResult(cacheName = "SecuritiesGetVisitorsFolder")
	@Override
	public Uni<ISecurityToken<?,?>> getVisitorsGuestsFolder(Mutiny.Session session, ISystems<?,?> system, UUID... identityToken)
	{
			SecurityToken st = new SecurityToken();
			var enterprise = system.getEnterprise();
			return st.builder(session)
				.findFolder(UserGroup.toString(), system, identityToken)
				.withName(Visitors)
				.inActiveRange()
				.inDateRange()
				.withEnterprise(enterprise)
				//     .canRead(enterprise, identityToken)
				.get()
				.onItem().transform(token -> (ISecurityToken<?,?>) token);
		
	}
	//@CacheResult(cacheName = "SecuritiesGetAdministratorsFolder")
	@Override
	public Uni<ISecurityToken<?,?>> getAdministratorsFolder(Mutiny.Session session, ISystems<?,?> system, UUID... identityToken)
	{
			SecurityToken st = new SecurityToken();
			var enterprise = system.getEnterprise();
			return st.builder(session)
				.findFolder(UserGroup.toString(), system, identityToken)
				.withName(Administrators)
				.inActiveRange()
				.inDateRange()
				.withEnterprise(enterprise)
				//  .canRead(enterprise, identityToken)
				.get()
				.onItem().transform(token -> (ISecurityToken<?,?>) token);

	}
	//@CacheResult(cacheName = "SecuritiesGetSystemsFolder")
	@Override
	public Uni<ISecurityToken<?,?>> getSystemsFolder(Mutiny.Session session, ISystems<?,?> system, UUID... identityToken)
	{
	
			SecurityToken st = new SecurityToken();
			var enterprise = system.getEnterprise();
			return st.builder(session)
				.findFolder(UserGroupSecurityTokenClassifications.System.toString(), system, identityToken)
				.withName(System)
				.inActiveRange()
				.inDateRange()
				.withEnterprise(enterprise)
				//.canRead(enterprise, identityToken)
				.get()
				.onItem().transform(token -> (ISecurityToken<?,?>) token);
	
	}
	//@CacheResult(cacheName = "SecuritiesGetPluginsFolder")
	@Override
	public Uni<ISecurityToken<?,?>> getPluginsFolder(Mutiny.Session session, ISystems<?,?> system, UUID... identityToken)
	{

			SecurityToken st = new SecurityToken();
			var enterprise = system.getEnterprise();
			return st.builder(session)
				.findFolder(Plugin.toString(), system, identityToken)
				.withName(Plugins)
				.inActiveRange()
				//  .canRead(enterprise, identityToken)
				.inDateRange()
				.withEnterprise(enterprise)
				.get()
				.onItem().transform(token -> (ISecurityToken<?,?>) token);
	
	}
//	//@CacheResult(cacheName = "SecuritiesGetApplicationsFolder")
	@Override
	public Uni<ISecurityToken<?,?>> getApplicationsFolder(Mutiny.Session session, ISystems<?,?> system, UUID... identityToken)
	{
	
			SecurityToken st = new SecurityToken();
		var enterprise = system.getEnterprise();
			return st.builder(session)
				.findFolder(Application.toString(), system, identityToken)
				.withName(Applications)
				.inActiveRange()
				//   .canRead(enterprise, identityToken)
				.inDateRange()
				.withEnterprise(enterprise)
				.get()
				.onItem().transform(token -> (ISecurityToken<?,?>) token);
	
	}
//	//@CacheResult(cacheName = "SecurityGetSecurityToken")
	@Override
	public Uni<ISecurityToken<?,?>> getSecurityToken(Mutiny.Session session, UUID identifyingToken, ISystems<?,?> system, UUID... identityToken)
	{
var enterprise = system.getEnterprise();
			return new SecurityToken().builder(session)
				.findBySecurityToken(identifyingToken.toString())
				.withEnterprise(enterprise)
				.inActiveRange()
				.inDateRange()
				.withEnterprise(enterprise)
				// .canRead(enterprise, identityToken)
				.get()
				.onFailure(NoSuchElementException.class).recoverWithNull()
				.onItem().transform(token -> (ISecurityToken<?,?>) token);
	
	}
	//@CacheResult(cacheName = "SecurityGetSecurityTokenNoActiveFlag")
	@Override
	public Uni<ISecurityToken<?,?>> getSecurityToken(Mutiny.Session session, UUID identifyingToken, boolean overrideActiveFlag, ISystems<?,?> system, UUID... identityToken)
	{

			SecurityTokenQueryBuilder builder = new SecurityToken().builder(session);
			var enterprise = system.getEnterprise();
			builder = builder.findBySecurityToken(identifyingToken.toString())
					.withEnterprise(enterprise)
					.inDateRange();
			if (overrideActiveFlag)
			{
				builder.inActiveRange();
			}

			return builder
				.get()
				.onFailure(NoSuchElementException.class).recoverWithNull()
				.onItem().transform(token -> (ISecurityToken<?,?>) token);

	}
}
