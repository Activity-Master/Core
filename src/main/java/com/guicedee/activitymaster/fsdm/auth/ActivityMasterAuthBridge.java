package com.guicedee.activitymaster.fsdm.auth;

import com.google.inject.Singleton;
import com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.party.IInvolvedParty;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.client.IGuiceContext;
import com.guicedee.client.scopes.CallScopeProperties;
import com.guicedee.client.scopes.CallScoper;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.auth.authorization.RoleBasedAuthorization;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.lang.reflect.Method;
import java.util.*;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.NoClassification;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.NameTypes.*;

/**
 * ActivityMaster-native authentication bridge into the Vert.x auth model.
 * <p>
 * When a party authenticates (e.g. the admin username/password login via
 * {@code PasswordsService.findByUsernameAndPassword}) this bridge constructs an
 * {@link io.vertx.ext.auth.User} whose <em>principal</em> carries the caller's details (subject /
 * identity token, username, display name) and whose <em>roles</em> are resolved from the ActivityMaster
 * security model — the friendly names of every {@code SecurityToken} the caller's identity expands to
 * (its own token plus every group/folder it is a member of, transitively). Those roles are also added as
 * Vert.x {@link RoleBasedAuthorization}s so they can be evaluated by the standard authorization APIs.
 * <p>
 * The authenticated {@link User} becomes the current call's auth context:
 * <ul>
 *     <li>it is published on the active {@link CallScopeProperties} under {@link #SCOPE_KEY_VERTX_USER},
 *         so both <strong>internal calls</strong> and ActivityMaster <strong>REST</strong> resources
 *         (which execute inside the same call scope) can read it via {@link #currentUser()};</li>
 *     <li>the caller's identity token is mirrored onto the call-scoped
 *         {@link ActivityMasterConfiguration#setIdentityToken(UUID)} so row-level security uses the
 *         logged-in identity for the remainder of the call;</li>
 *     <li>when an HTTP {@code RoutingContext} is present on the scope, the user is mirrored onto it on a
 *         best-effort basis (see {@link #trySetRoutingContextUser}).</li>
 * </ul>
 * No MicroProfile JWT dependency is involved — this is a pure ActivityMaster + Vert.x bridge.
 */
@Log4j2
@Singleton
public class ActivityMasterAuthBridge
{
	/**
	 * Call-scope property key under which the authenticated Vert.x {@link User} is stored.
	 */
	public static final String SCOPE_KEY_VERTX_USER = "fsdm.vertxUser";

	/**
	 * Call-scope property key under which the request's Vert.x {@code RoutingContext} is published by the
	 * web/rest entry points (see {@code OperationRegistry} / {@code JWebMPVertx}).
	 */
	public static final String SCOPE_KEY_ROUTING_CONTEXT = "RoutingContext";

	/**
	 * The Vert.x authorizations provider id under which ActivityMaster roles are registered on the user.
	 */
	public static final String AUTHORIZATION_PROVIDER_ID = "activitymaster";

	/**
	 * Authenticates a party into the Vert.x auth context: builds the {@link User} (details + roles from the
	 * database) and publishes it on the active call scope. Never fails the caller — any problem resolving
	 * details/roles is logged and the login degrades gracefully.
	 *
	 * @param session             the live reactive session (re-used; no new session/transaction is opened)
	 * @param party               the authenticated involved party
	 * @param username            the username the party logged in with
	 * @param system              the system context
	 * @param callerIdentityToken optional caller identity token(s) used for the security-checked reads
	 * @return a {@link Uni} emitting the established {@link User}, or {@code null} when one could not be built
	 */
	public Uni<User> login(Mutiny.Session session, IInvolvedParty<?, ?> party, String username, ISystems<?, ?> system, UUID... callerIdentityToken)
	{
		return buildUser(session, party, username, system, callerIdentityToken)
				.invoke(this::publish)
				.onFailure()
				.recoverWithUni(error -> {
					log.warn("Authenticated '{}' but could not establish the Vert.x auth context: {}", username, error.getMessage(), error);
					return Uni.createFrom().nullItem();
				});
	}

	/**
	 * Builds a Vert.x {@link User} for an authenticated party, populated with the caller's details and the
	 * roles resolved from the ActivityMaster security hierarchy. Does not touch the call scope.
	 */
	public Uni<User> buildUser(Mutiny.Session session, IInvolvedParty<?, ?> party, String username, ISystems<?, ?> system, UUID... callerIdentityToken)
	{
		return resolveIdentityToken(session, username, system, callerIdentityToken)
				.chain(identity -> resolveDisplayName(session, party, username, system, callerIdentityToken)
						.chain(displayName -> resolveRoles(session, system, identity, callerIdentityToken)
								.map(roles -> assemble(party, username, displayName, identity, roles))));
	}

	/**
	 * Resolves the party's own identity token (the {@code SecurityToken} named after the username, the same
	 * token created by the enterprise install) which is the seed used to expand the caller's roles.
	 *
	 * @return the identity token UUID, or {@code null} when it cannot be resolved
	 */
	private Uni<UUID> resolveIdentityToken(Mutiny.Session session, String username, ISystems<?, ?> system, UUID... callerIdentityToken)
	{
		ISecurityTokenService<?> securityTokenService = IGuiceContext.get(ISecurityTokenService.class);
		return securityTokenService.getSecurityTokenByName(session, username, system, callerIdentityToken)
				.map(token -> token == null ? null : UUID.fromString(((ISecurityToken<?, ?>) token).getSecurityToken()))
				.onFailure()
				.recoverWithItem((UUID) null);
	}

	/**
	 * Resolves a friendly display name for the party: preferred name, then full name, then first name,
	 * finally falling back to the username. Best-effort — never fails.
	 */
	private Uni<String> resolveDisplayName(Mutiny.Session session, IInvolvedParty<?, ?> party, String username, ISystems<?, ?> system, UUID... identityToken)
	{
		return readName(session, party, PreferredNameType.toString(), system, identityToken)
				.chain(preferred -> preferred != null ? Uni.createFrom().item(preferred)
						: readName(session, party, FullNameType.toString(), system, identityToken))
				.chain(name -> name != null ? Uni.createFrom().item(name)
						: readName(session, party, FirstNameType.toString(), system, identityToken))
				.map(name -> name != null && !name.isBlank() ? name : username)
				.onFailure()
				.recoverWithItem(username);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private Uni<String> readName(Mutiny.Session session, IInvolvedParty<?, ?> party, String nameType, ISystems<?, ?> system, UUID... identityToken)
	{
		return ((IInvolvedParty) party).findInvolvedPartyNameType(session, NoClassification.toString(), nameType, null, system, true, true, identityToken)
				.map(rel -> rel == null ? null : ((com.guicedee.activitymaster.fsdm.client.services.IRelationshipValue<?, ?, ?>) rel).getValue())
				.onFailure()
				.recoverWithItem((String) null);
	}

	/**
	 * Expands the identity token into the set of applicable security tokens and reads their friendly names
	 * to use as roles. Best-effort — returns an empty set on any failure.
	 */
	private Uni<Set<String>> resolveRoles(Mutiny.Session session, ISystems<?, ?> system, UUID identity, UUID... callerIdentityToken)
	{
		if (identity == null)
		{
			return Uni.createFrom().item(Collections.emptySet());
		}
		ISecurityTokenService<?> securityTokenService = IGuiceContext.get(ISecurityTokenService.class);
		return securityTokenService.getApplicableSecurityTokenIds(session, system, identity)
				.chain(ids -> {
					if (ids == null || ids.isEmpty())
					{
						return Uni.createFrom().<Set<String>>item(Collections.emptySet());
					}
					return session.createQuery("select st.name from SecurityToken st where st.id in (:ids)", String.class)
							.setParameter("ids", new ArrayList<>(ids))
							.getResultList()
							.map(names -> (Set<String>) new LinkedHashSet<>(names));
				})
				.onFailure()
				.recoverWithItem(Collections.emptySet());
	}

	/**
	 * Assembles the immutable Vert.x {@link User} from the resolved details and roles.
	 */
	private User assemble(IInvolvedParty<?, ?> party, String username, String displayName, UUID identity, Set<String> roles)
	{
		String subject = identity != null ? identity.toString()
				: (party.getId() != null ? party.getId().toString() : username);

		JsonArray roleArray = new JsonArray();
		roles.forEach(roleArray::add);

		JsonObject principal = new JsonObject()
				.put("sub", subject)
				.put("username", username)
				.put("preferred_username", username)
				.put("roles", roleArray)
				.put("groups", roleArray.copy());
		if (displayName != null && !displayName.isBlank())
		{
			principal.put("name", displayName);
		}
		if (party.getId() != null)
		{
			principal.put("involvedPartyId", party.getId().toString());
		}

		JsonObject attributes = new JsonObject()
				.put("amr", new JsonArray().add("pwd"));
		if (identity != null)
		{
			attributes.put("identityToken", identity.toString());
		}

		User user = User.create(principal, attributes);

		if (!roles.isEmpty())
		{
			Set<Authorization> authorizations = new LinkedHashSet<>();
			for (String role : roles)
			{
				authorizations.add(RoleBasedAuthorization.create(role));
			}
			user.authorizations().put(AUTHORIZATION_PROVIDER_ID, authorizations);
		}

		log.debug("Built Vert.x user for '{}' (sub={}, roles={})", username, subject, roles);
		return user;
	}

	/**
	 * Publishes the authenticated user as the current call's auth context (call scope, identity token, and
	 * best-effort onto the {@code RoutingContext}). No-op when there is no started call scope.
	 *
	 * @param user the authenticated user, may be {@code null}
	 */
	public void publish(User user)
	{
		if (user == null)
		{
			return;
		}
		CallScoper callScoper = IGuiceContext.get(CallScoper.class);
		if (callScoper == null || !callScoper.isStartedScope())
		{
			log.debug("No active call scope; Vert.x auth user not published for '{}'", user.principal().getString("username"));
			return;
		}
		CallScopeProperties props = IGuiceContext.get(CallScopeProperties.class);
		if (props == null)
		{
			return;
		}
		props.getProperties().put(SCOPE_KEY_VERTX_USER, user);

		// Mirror the identity token so row-level security uses the logged-in identity for this call.
		String identityToken = user.attributes() != null ? user.attributes().getString("identityToken") : null;
		if (identityToken != null)
		{
			try
			{
				ActivityMasterConfiguration.get().setIdentityToken(UUID.fromString(identityToken));
			}
			catch (RuntimeException ignored)
			{
				// leave the existing identity token untouched on bad input
			}
		}

		trySetRoutingContextUser(props, user);
	}

	/**
	 * Best-effort mirror of the user onto the request's Vert.x {@code RoutingContext}.
	 * <p>
	 * Vert.x 5 removed the public {@code RoutingContext.setUser}/{@code UserContext.setUser} API — the only
	 * setter lives on the internal {@code UserContextInternal} (a qualified export). We attempt it
	 * reflectively so that, where the runtime permits, {@code routingContext.user()} returns the logged-in
	 * user; if it is not accessible (the common case from an application module) we silently rely on the
	 * call-scope auth context instead.
	 */
	private void trySetRoutingContextUser(CallScopeProperties props, User user)
	{
		Object routingContext = props.getProperties().get(SCOPE_KEY_ROUTING_CONTEXT);
		if (routingContext == null)
		{
			return;
		}
		try
		{
			Object userContext = routingContext.getClass().getMethod("userContext").invoke(routingContext);
			if (userContext == null)
			{
				return;
			}
			Method setUser = findSetUserMethod(userContext.getClass());
			if (setUser != null)
			{
				setUser.setAccessible(true);
				setUser.invoke(userContext, user);
				log.debug("Mirrored Vert.x user '{}' onto the RoutingContext", user.principal().getString("username"));
			}
			else
			{
				log.debug("RoutingContext UserContext exposes no setUser; relying on the call-scope auth context");
			}
		}
		catch (Throwable t)
		{
			log.debug("Could not mirror the Vert.x user onto the RoutingContext ({}); relying on the call-scope auth context", t.toString());
		}
	}

	private Method findSetUserMethod(Class<?> type)
	{
		for (Class<?> current = type; current != null; current = current.getSuperclass())
		{
			Method m = setUserOn(current);
			if (m != null)
			{
				return m;
			}
			for (Class<?> itf : current.getInterfaces())
			{
				m = setUserOn(itf);
				if (m != null)
				{
					return m;
				}
			}
		}
		return null;
	}

	private Method setUserOn(Class<?> type)
	{
		try
		{
			return type.getDeclaredMethod("setUser", User.class);
		}
		catch (NoSuchMethodException e)
		{
			return null;
		}
	}

	/**
	 * Returns the Vert.x {@link User} authenticated for the current call, or {@code null} when there is no
	 * active call scope or no user has been logged in.
	 */
	public static User currentUser()
	{
		CallScoper callScoper = IGuiceContext.get(CallScoper.class);
		if (callScoper == null || !callScoper.isStartedScope())
		{
			return null;
		}
		CallScopeProperties props = IGuiceContext.get(CallScopeProperties.class);
		Object user = props == null ? null : props.getProperties().get(SCOPE_KEY_VERTX_USER);
		return user instanceof User u ? u : null;
	}
}

