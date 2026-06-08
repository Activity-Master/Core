package com.guicedee.activitymaster.fsdm.rest.security;

import java.util.*;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.SessionUtils;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.rest.security.*;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityToken;
import com.guicedee.activitymaster.fsdm.db.entities.security.SecurityTokenXSecurityToken;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple4;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

/**
 * REST surface for comprehensively managing the ActivityMaster security structure — the
 * {@code SecurityToken} graph of groups/folders, users and identities, their membership edges, the
 * per-token access (grant) matrix, and identity-token expansion.
 * <p>
 * <strong>Library-managed structures are off-limits.</strong> The {@code Systems}, {@code Applications}
 * and {@code Plugins} folders — and any {@code System}/{@code Application}/{@code Plugin}-typed token —
 * are always provisioned as <em>libraries added on</em> (they register themselves at install/boot) and
 * <em>cannot</em> be created, renamed, deleted, re-parented or granted through this endpoint. Any such
 * attempt is rejected with {@code 403 Forbidden} (defence-in-depth on top of the service-level
 * membership policy).
 * <p>
 * Tokens are referenced by their enterprise-unique <em>name</em> (e.g. {@code "Administrators"},
 * {@code "Sales Team"}, {@code "admin"}). All operations run inside the requesting system's security
 * scope via {@link SessionUtils#withActivityMaster}.
 * <p>
 * <strong>Access is restricted to administrators.</strong> The whole resource is annotated
 * {@code @RolesAllowed("Administrators")}, so the rest layer requires an authenticated caller that
 * holds the {@code Administrators} role (resolved from the ActivityMaster security model by the auth
 * bridge, or from a token's role/group claims). Unauthenticated callers receive {@code 401} and
 * non-administrators {@code 403}.
 */
@Path("{enterprise}/security")
@Tag(name = "Security", description = "Manage the security-token hierarchy: groups/folders, users, identities, memberships, the access-grant matrix and identity expansion. Systems/Applications/Plugins are library-managed and cannot be changed here.")
@RolesAllowed("Administrators")
@Log4j2
public class SecurityRestService {

    /** Folder names that are provisioned by libraries and may not be managed through this endpoint. */
    private static final Set<String> MANAGED_FOLDERS = Set.of("System", "Applications", "Plugins");
    /** Token type (classification) names that are provisioned by libraries and may not be managed here. */
    private static final Set<String> MANAGED_TYPES = Set.of("System", "Application", "Plugin");
    /** Token types that may be created/managed through this endpoint. */
    private static final Set<String> CREATABLE_TYPES = Set.of("UserGroup", "User", "Identity", "Guests", "Visitors", "Registered");
    /** Default type when a create request does not specify one. */
    private static final String DEFAULT_TYPE = "UserGroup";

    @Inject
    private ISecurityTokenService<?> securityTokenService;

    @Inject
    private IActiveFlagService<?> activeFlagService;

    // ──────────────────────────────────────────────────────────────────────────
    // Read — list / find / members / resolve
    // ──────────────────────────────────────────────────────────────────────────

    @GET
    @Path("{requestingSystemName}/list")
    @Operation(summary = "List security tokens",
            description = "Lists the enterprise's active, in-date security tokens, optionally filtered by type (classification name). The 'managed' flag marks library-owned structures that cannot be changed here.")
    @ApiResponse(responseCode = "200", description = "Security tokens listed")
    public Uni<List<SecurityTokenDTO>> list(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                            @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                            @Parameter(description = "Optional type filter (e.g. UserGroup, User, Identity)") @QueryParam("type") String typeFilter) {
        return SessionUtils.<List<SecurityTokenDTO>>withActivityMaster(enterpriseName, requestingSystemName,
                (Tuple4<Mutiny.Session, IEnterprise<?, ?>, ISystems<?, ?>, UUID[]> tuple) -> {
                    Mutiny.Session session = tuple.getItem1();
                    IEnterprise<?, ?> enterprise = tuple.getItem2();
                    return new SecurityToken().builder(session)
                            .withEnterprise(enterprise)
                            .inActiveRange()
                            .inDateRange()
                            .getAll()
                            .chain(tokens -> {
                                List<SecurityTokenDTO> result = new ArrayList<>();
                                Uni<Void> chain = Uni.createFrom().voidItem();
                                for (Object next : tokens) {
                                    ISecurityToken<?, ?> token = (ISecurityToken<?, ?>) next;
                                    chain = chain.chain(() -> toDTO(session, enterprise, token, false, false)
                                            .invoke(dto -> {
                                                if (typeFilter == null || typeFilter.isBlank()
                                                        || (dto.type != null && dto.type.equalsIgnoreCase(typeFilter))) {
                                                    result.add(dto);
                                                }
                                            }).replaceWithVoid());
                                }
                                return chain.replaceWith(result);
                            });
                }
        ).onFailure().invoke(e -> log.error("Error listing security tokens for {}: {}", enterpriseName, e.getMessage(), e));
    }

    @POST
    @Path("{requestingSystemName}/find")
    @Operation(summary = "Find a security token",
            description = "Resolves a single token by name (preferred) or by its securityToken varchar, hydrating members / member-of relationships per the includes list.")
    @ApiResponse(responseCode = "200", description = "Security token found")
    @ApiResponse(responseCode = "404", description = "Security token not found")
    public Uni<SecurityTokenDTO> find(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                      @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                      SecurityTokenFindDTO findDto) {
        boolean members = includes(findDto.includes, SecurityTokenDataIncludes.Members);
        boolean memberOf = includes(findDto.includes, SecurityTokenDataIncludes.MemberOf);
        return SessionUtils.<SecurityTokenDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            IEnterprise<?, ?> enterprise = tuple.getItem2();
            ISystems<?, ?> system = tuple.getItem3();
            UUID[] token = tuple.getItem4();
            return resolve(session, system, findDto.name, findDto.securityToken, token)
                    .chain(found -> toDTO(session, enterprise, found, members, memberOf));
        }).onFailure().invoke(e -> log.error("Error finding security token for {}: {}", enterpriseName, e.getMessage(), e));
    }

    @POST
    @Path("{requestingSystemName}/resolve")
    @Operation(summary = "Expand an identity token",
            description = "Expands an identity token into the complete set of security tokens it grants access through — itself plus every group/folder it belongs to, transitively (the recursive membership climb).")
    @ApiResponse(responseCode = "200", description = "Applicable tokens resolved")
    @ApiResponse(responseCode = "404", description = "Identity token not found")
    public Uni<ApplicableTokensDTO> resolve(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                            @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                            SecurityTokenResolveDTO resolveDto) {
        return SessionUtils.<ApplicableTokensDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            ISystems<?, ?> system = tuple.getItem3();
            UUID[] token = tuple.getItem4();

            Uni<UUID> identityUni;
            if (resolveDto.identityToken != null) {
                identityUni = Uni.createFrom().item(resolveDto.identityToken);
            } else if (resolveDto.name != null && !resolveDto.name.isBlank()) {
                identityUni = require(session, system, resolveDto.name, token)
                        .map(t -> UUID.fromString(t.getSecurityToken()));
            } else {
                return Uni.createFrom().failure(new BadRequestException("Either identityToken or name must be supplied"));
            }

            return identityUni.chain(identity -> securityTokenService.getApplicableSecurityTokenIds(session, system, identity)
                    .chain(ids -> {
                        ApplicableTokensDTO dto = new ApplicableTokensDTO();
                        dto.identityToken = identity.toString();
                        dto.applicableIds = new ArrayList<>(ids);
                        List<SecurityTokenRef> refs = new ArrayList<>();
                        Uni<Void> chain = Uni.createFrom().voidItem();
                        for (UUID id : ids) {
                            chain = chain.chain(() -> session.find(SecurityToken.class, id)
                                    .chain(st -> st == null ? Uni.createFrom().voidItem()
                                            : toRef(session, st).invoke(refs::add).replaceWithVoid()));
                        }
                        return chain.replaceWith(() -> {
                            dto.applicable = refs;
                            return dto;
                        });
                    }));
        }).onFailure().invoke(e -> log.error("Error resolving applicable tokens for {}: {}", enterpriseName, e.getMessage(), e));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Create / Update / Delete
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/create")
    @Operation(summary = "Create a security token",
            description = "Creates a manageable group/folder, user or identity token, optionally beneath a parent group/folder. System/Application/Plugin types and the Systems/Applications/Plugins folders are rejected (403).")
    @ApiResponse(responseCode = "200", description = "Security token created")
    @ApiResponse(responseCode = "403", description = "Attempt to manage a library-owned (System/Application/Plugin) structure")
    public Uni<SecurityTokenDTO> create(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                        @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                        SecurityTokenCreateDTO dto) {
        String type = (dto.type == null || dto.type.isBlank()) ? DEFAULT_TYPE : dto.type.trim();
        return SessionUtils.<SecurityTokenDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            IEnterprise<?, ?> enterprise = tuple.getItem2();
            ISystems<?, ?> system = tuple.getItem3();
            UUID[] token = tuple.getItem4();
            try {
                assertNameManageable(dto.name);
                assertTypeManageable(type);
                if (dto.parentName != null && !dto.parentName.isBlank()) {
                    assertNameManageable(dto.parentName);
                }
            } catch (WebApplicationException reject) {
                return Uni.createFrom().failure(reject);
            }

            Uni<ISecurityToken<?, ?>> parentUni = (dto.parentName == null || dto.parentName.isBlank())
                    ? Uni.createFrom().<ISecurityToken<?, ?>>nullItem()
                    : requireManageable(session, system, dto.parentName, token);

            return parentUni.chain(parent -> securityTokenService.create(session, type, dto.name, dto.description, system, parent, token))
                    .chain(created -> toDTO(session, enterprise, created, false, false));
        }).onFailure().invoke(e -> log.error("Error creating security token '{}' for {}: {}", dto.name, enterpriseName, e.getMessage(), e));
    }

    @PUT
    @Path("{requestingSystemName}/update")
    @Operation(summary = "Update a security token",
            description = "Updates a manageable token's description and optionally renames it. Library-owned structures are rejected (403).")
    @ApiResponse(responseCode = "200", description = "Security token updated")
    @ApiResponse(responseCode = "403", description = "Attempt to manage a library-owned structure")
    @ApiResponse(responseCode = "404", description = "Security token not found")
    public Uni<SecurityTokenDTO> update(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                        @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                        SecurityTokenUpdateDTO dto) {
        return SessionUtils.<SecurityTokenDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            IEnterprise<?, ?> enterprise = tuple.getItem2();
            ISystems<?, ?> system = tuple.getItem3();
            UUID[] token = tuple.getItem4();
            try {
                assertNameManageable(dto.name);
                if (dto.newName != null && !dto.newName.isBlank()) {
                    assertNameManageable(dto.newName);
                }
            } catch (WebApplicationException reject) {
                return Uni.createFrom().failure(reject);
            }
            return requireManageable(session, system, dto.name, token)
                    .chain(found -> {
                        SecurityToken st = (SecurityToken) found;
                        if (dto.description != null) {
                            st.setDescription(dto.description);
                        }
                        if (dto.newName != null && !dto.newName.isBlank()) {
                            st.setName(dto.newName);
                        }
                        // The managed entity is flushed by the surrounding transaction.
                        return toDTO(session, enterprise, found, false, false);
                    });
        }).onFailure().invoke(e -> log.error("Error updating security token '{}' for {}: {}", dto.name, enterpriseName, e.getMessage(), e));
    }

    @POST
    @Path("{requestingSystemName}/delete")
    @Operation(summary = "Soft-delete a security token",
            description = "Soft-deletes a manageable token by setting its active flag to Deleted. Library-owned structures are rejected (403).")
    @ApiResponse(responseCode = "200", description = "Security token soft-deleted")
    @ApiResponse(responseCode = "403", description = "Attempt to manage a library-owned structure")
    @ApiResponse(responseCode = "404", description = "Security token not found")
    public Uni<SecurityTokenDTO> delete(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                        @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                        SecurityTokenFindDTO dto) {
        return SessionUtils.<SecurityTokenDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            IEnterprise<?, ?> enterprise = tuple.getItem2();
            ISystems<?, ?> system = tuple.getItem3();
            UUID[] token = tuple.getItem4();
            try {
                assertNameManageable(dto.name);
            } catch (WebApplicationException reject) {
                return Uni.createFrom().failure(reject);
            }
            return requireManageable(session, system, dto.name, token)
                    .chain(found -> activeFlagService.getDeletedFlag(session, enterprise, token)
                            .chain(deleted -> {
                                ((SecurityToken) found).setActiveFlagID((IActiveFlag<?, ?>) deleted);
                                return toDTO(session, enterprise, found, false, false);
                            }));
        }).onFailure().invoke(e -> log.error("Error deleting security token '{}' for {}: {}", dto.name, enterpriseName, e.getMessage(), e));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Membership — add / remove / move
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/member/add")
    @Operation(summary = "Add a member",
            description = "Adds a membership edge: child becomes a member of the parent group/folder. The service-level membership policy still applies; library-owned folders/types are rejected (403).")
    @ApiResponse(responseCode = "200", description = "Membership added")
    @ApiResponse(responseCode = "403", description = "Attempt to manage a library-owned structure")
    public Uni<SecurityTokenDTO> addMember(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                           @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                           SecurityTokenMembershipDTO dto) {
        return SessionUtils.<SecurityTokenDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            IEnterprise<?, ?> enterprise = tuple.getItem2();
            ISystems<?, ?> system = tuple.getItem3();
            UUID[] token = tuple.getItem4();
            try {
                assertNameManageable(dto.parentName);
                assertNameManageable(dto.childName);
            } catch (WebApplicationException reject) {
                return Uni.createFrom().failure(reject);
            }
            return requireManageable(session, system, dto.parentName, token)
                    .chain(parent -> requireManageable(session, system, dto.childName, token)
                            .chain(child -> session.fetch(((SecurityToken) child).getSecurityTokenClassificationID())
                                    .chain(type -> securityTokenService.link(session, parent, child, (Classification) type))
                                    .chain(v -> toDTO(session, enterprise, parent, true, false))));
        }).onFailure().invoke(e -> log.error("Error adding member '{}' to '{}' for {}: {}", dto.childName, dto.parentName, enterpriseName, e.getMessage(), e));
    }

    @POST
    @Path("{requestingSystemName}/member/remove")
    @Operation(summary = "Remove a member",
            description = "Removes a membership edge by temporally closing it (the recursive climb stops traversing it). Library-owned folders/types are rejected (403).")
    @ApiResponse(responseCode = "200", description = "Membership removed")
    @ApiResponse(responseCode = "403", description = "Attempt to manage a library-owned structure")
    @ApiResponse(responseCode = "404", description = "Membership edge not found")
    public Uni<SecurityTokenDTO> removeMember(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                              @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                              SecurityTokenMembershipDTO dto) {
        return SessionUtils.<SecurityTokenDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            IEnterprise<?, ?> enterprise = tuple.getItem2();
            ISystems<?, ?> system = tuple.getItem3();
            UUID[] token = tuple.getItem4();
            try {
                assertNameManageable(dto.parentName);
                assertNameManageable(dto.childName);
            } catch (WebApplicationException reject) {
                return Uni.createFrom().failure(reject);
            }
            return requireManageable(session, system, dto.parentName, token)
                    .chain(parent -> requireManageable(session, system, dto.childName, token)
                            .chain(child -> new SecurityTokenXSecurityToken().builder(session)
                                    .withEnterprise(enterprise)
                                    .findLink((SecurityToken) parent, (SecurityToken) child, null)
                                    .inActiveRange()
                                    .inDateRange()
                                    .get()
                                    .onFailure(NoResultException.class).recoverWithNull()
                                    .chain(edge -> {
                                        if (edge == null) {
                                            return Uni.createFrom().failure(new NotFoundException(
                                                    "No active membership of '" + dto.childName + "' under '" + dto.parentName + "'"));
                                        }
                                        ((SecurityTokenXSecurityToken) edge).setEffectiveToDate(now());
                                        return session.merge(edge).replaceWithVoid();
                                    })
                                    .chain(v -> toDTO(session, enterprise, parent, true, false))));
        }).onFailure().invoke(e -> log.error("Error removing member '{}' from '{}' for {}: {}", dto.childName, dto.parentName, enterpriseName, e.getMessage(), e));
    }

    @POST
    @Path("{requestingSystemName}/member/move")
    @Operation(summary = "Move a member",
            description = "Moves a child from one parent group/folder to another (or, when oldParentName is omitted, an exclusive reparent that detaches from all current parents). Library-owned folders/types are rejected (403).")
    @ApiResponse(responseCode = "200", description = "Member moved")
    @ApiResponse(responseCode = "403", description = "Attempt to manage a library-owned structure")
    public Uni<SecurityTokenDTO> moveMember(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                            @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                            SecurityTokenMoveDTO dto) {
        return SessionUtils.<SecurityTokenDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            IEnterprise<?, ?> enterprise = tuple.getItem2();
            ISystems<?, ?> system = tuple.getItem3();
            UUID[] token = tuple.getItem4();
            try {
                assertNameManageable(dto.childName);
                assertNameManageable(dto.newParentName);
                if (dto.oldParentName != null && !dto.oldParentName.isBlank()) {
                    assertNameManageable(dto.oldParentName);
                }
            } catch (WebApplicationException reject) {
                return Uni.createFrom().failure(reject);
            }
            Uni<ISecurityToken<?, ?>> oldParentUni = (dto.oldParentName == null || dto.oldParentName.isBlank())
                    ? Uni.createFrom().<ISecurityToken<?, ?>>nullItem()
                    : requireManageable(session, system, dto.oldParentName, token);
            return requireManageable(session, system, dto.newParentName, token)
                    .chain(newParent -> requireManageable(session, system, dto.childName, token)
                            .chain(child -> oldParentUni.chain(oldParent ->
                                    session.fetch(((SecurityToken) child).getSecurityTokenClassificationID())
                                            .chain(type -> securityTokenService.moveToken(session, oldParent, newParent, child, (Classification) type))
                                            .chain(v -> toDTO(session, enterprise, newParent, true, false)))));
        }).onFailure().invoke(e -> log.error("Error moving member '{}' to '{}' for {}: {}", dto.childName, dto.newParentName, enterpriseName, e.getMessage(), e));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Access grants
    // ──────────────────────────────────────────────────────────────────────────

    @POST
    @Path("{requestingSystemName}/grant")
    @Operation(summary = "Grant access between tokens",
            description = "Creates (or confirms) a CRUD access edge from one token to another. Granting from/to a library-owned structure is rejected (403).")
    @ApiResponse(responseCode = "200", description = "Grant applied")
    @ApiResponse(responseCode = "403", description = "Attempt to manage a library-owned structure")
    @ApiResponse(responseCode = "404", description = "Security token not found")
    public Uni<SecurityTokenGrantDTO> grant(@Parameter(description = "Owning enterprise name") @PathParam("enterprise") String enterpriseName,
                                            @Parameter(description = "Requesting system name (security scope)") @PathParam("requestingSystemName") String requestingSystemName,
                                            SecurityTokenGrantDTO dto) {
        return SessionUtils.<SecurityTokenGrantDTO>withActivityMaster(enterpriseName, requestingSystemName, tuple -> {
            Mutiny.Session session = tuple.getItem1();
            ISystems<?, ?> system = tuple.getItem3();
            UUID[] token = tuple.getItem4();
            try {
                assertNameManageable(dto.fromName);
                assertNameManageable(dto.toName);
            } catch (WebApplicationException reject) {
                return Uni.createFrom().failure(reject);
            }
            return requireManageable(session, system, dto.fromName, token)
                    .chain(from -> requireManageable(session, system, dto.toName, token)
                            .chain(to -> securityTokenService.grantAccessToToken(session, from, to,
                                    dto.create, dto.update, dto.delete, dto.read, system))
                            .replaceWith(dto));
        }).onFailure().invoke(e -> log.error("Error granting '{}' -> '{}' for {}: {}", dto.fromName, dto.toName, enterpriseName, e.getMessage(), e));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Resolution helpers
    // ──────────────────────────────────────────────────────────────────────────

    /** Resolves a token by name, failing with 404 when absent. */
    private Uni<ISecurityToken<?, ?>> require(Mutiny.Session session, ISystems<?, ?> system, String name, UUID[] token) {
        return securityTokenService.getSecurityTokenByName(session, name, system, token)
                .onItem().ifNull().failWith(() -> new NotFoundException("Security token not found: " + name));
    }

    /** Resolves a token by name (preferred) or securityToken varchar, failing with 404 when absent. */
    private Uni<ISecurityToken<?, ?>> resolve(Mutiny.Session session, ISystems<?, ?> system, String name, String securityToken, UUID[] token) {
        if (name != null && !name.isBlank()) {
            return require(session, system, name, token);
        }
        if (securityToken != null && !securityToken.isBlank()) {
            return securityTokenService.getSecurityToken(session, UUID.fromString(securityToken), system, token)
                    .onItem().ifNull().failWith(() -> new NotFoundException("Security token not found: " + securityToken));
        }
        return Uni.createFrom().failure(new BadRequestException("Either name or securityToken must be supplied"));
    }

    /** Resolves a token by name and asserts it is manageable (not a library-owned folder/type). */
    private Uni<ISecurityToken<?, ?>> requireManageable(Mutiny.Session session, ISystems<?, ?> system, String name, UUID[] token) {
        return require(session, system, name, token)
                .chain(found -> assertTokenManageable(session, found).replaceWith(found));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Guards — keep Systems/Applications/Plugins library-managed
    // ──────────────────────────────────────────────────────────────────────────

    /** Rejects (403) a name that targets one of the library-managed folders. */
    private void assertNameManageable(String name) {
        if (name != null && containsIgnoreCase(MANAGED_FOLDERS, name)) {
            throw new ForbiddenException("The '" + name + "' folder is library-managed (Systems/Applications/Plugins are added on as libraries) and cannot be changed through the security endpoint.");
        }
    }

    /** Rejects a create with a library-managed type (403) or an unknown/unmanageable type (400). */
    private void assertTypeManageable(String type) {
        if (containsIgnoreCase(MANAGED_TYPES, type)) {
            throw new ForbiddenException("System, Application and Plugin tokens are provisioned by their libraries and cannot be created or managed through the security endpoint.");
        }
        if (!containsIgnoreCase(CREATABLE_TYPES, type)) {
            throw new BadRequestException("Unknown or unmanageable security token type '" + type + "'. Allowed types: " + CREATABLE_TYPES);
        }
    }

    /** Reactive guard: a resolved token must be neither a managed folder (by name) nor a managed type. */
    private Uni<Void> assertTokenManageable(Mutiny.Session session, ISecurityToken<?, ?> tokenEntity) {
        if (containsIgnoreCase(MANAGED_FOLDERS, tokenEntity.getName())) {
            return Uni.createFrom().failure(new ForbiddenException(
                    "The '" + tokenEntity.getName() + "' folder is library-managed and cannot be changed through the security endpoint."));
        }
        return session.fetch(((SecurityToken) tokenEntity).getSecurityTokenClassificationID())
                .chain(type -> {
                    if (type != null && containsIgnoreCase(MANAGED_TYPES, ((Classification) type).getName())) {
                        return Uni.createFrom().failure(new ForbiddenException(
                                "'" + tokenEntity.getName() + "' is a " + ((Classification) type).getName()
                                        + "-typed token provisioned by its library and cannot be managed through the security endpoint."));
                    }
                    return Uni.createFrom().voidItem();
                });
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Mapping
    // ──────────────────────────────────────────────────────────────────────────

    private Uni<SecurityTokenDTO> toDTO(Mutiny.Session session, IEnterprise<?, ?> enterprise, ISecurityToken<?, ?> token,
                                        boolean members, boolean memberOf) {
        return toRef(session, token).chain(ref -> {
            SecurityTokenDTO dto = new SecurityTokenDTO();
            dto.securityTokenId = ref.securityTokenId;
            dto.securityToken = ref.securityToken;
            dto.name = ref.name;
            dto.type = ref.type;
            dto.description = token.getDescription();
            dto.managed = containsIgnoreCase(MANAGED_FOLDERS, ref.name)
                    || (ref.type != null && containsIgnoreCase(MANAGED_TYPES, ref.type));
            Uni<SecurityTokenDTO> chain = Uni.createFrom().item(dto);
            if (members) {
                chain = chain.chain(d -> loadEdges(session, enterprise, token, true).invoke(list -> d.members = list).replaceWith(d));
            }
            if (memberOf) {
                chain = chain.chain(d -> loadEdges(session, enterprise, token, false).invoke(list -> d.memberOf = list).replaceWith(d));
            }
            return chain;
        });
    }

    private Uni<SecurityTokenRef> toRef(Mutiny.Session session, ISecurityToken<?, ?> token) {
        SecurityTokenRef ref = new SecurityTokenRef();
        ref.securityTokenId = token.getId();
        ref.securityToken = token.getSecurityToken();
        ref.name = token.getName();
        return session.fetch(((SecurityToken) token).getSecurityTokenClassificationID())
                .map(type -> {
                    ref.type = type == null ? null : ((Classification) type).getName();
                    return ref;
                })
                .onFailure().recoverWithItem(ref);
    }

    /**
     * Loads the membership edges for a token: when {@code children} is {@code true} the direct members
     * (this token as parent); otherwise the groups/folders it belongs to (this token as child).
     */
    private Uni<List<SecurityTokenRef>> loadEdges(Mutiny.Session session, IEnterprise<?, ?> enterprise,
                                                  ISecurityToken<?, ?> token, boolean children) {
        SecurityToken self = (SecurityToken) token;
        return new SecurityTokenXSecurityToken().builder(session)
                .withEnterprise(enterprise)
                .findLink(children ? self : null, children ? null : self, null)
                .inActiveRange()
                .inDateRange()
                .getAll()
                .chain(edges -> {
                    List<SecurityTokenRef> refs = new ArrayList<>();
                    Uni<Void> chain = Uni.createFrom().voidItem();
                    for (Object next : edges) {
                        SecurityTokenXSecurityToken edge = (SecurityTokenXSecurityToken) next;
                        SecurityToken related = children ? edge.getChildSecurityTokenID() : edge.getParentSecurityTokenID();
                        if (related == null) {
                            continue;
                        }
                        chain = chain.chain(() -> session.fetch(related)
                                .chain(fetched -> toRef(session, (ISecurityToken<?, ?>) fetched).invoke(refs::add).replaceWithVoid()));
                    }
                    return chain.replaceWith(refs);
                });
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Small helpers
    // ──────────────────────────────────────────────────────────────────────────

    private static boolean includes(List<SecurityTokenDataIncludes> includes, SecurityTokenDataIncludes value) {
        return includes != null && includes.contains(value);
    }

    private static boolean containsIgnoreCase(Set<String> set, String value) {
        if (value == null) {
            return false;
        }
        for (String s : set) {
            if (s.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    private static java.time.OffsetDateTime now() {
        return com.guicedee.activitymaster.fsdm.db.abstraction.builders.QueryBuilderSCD
                .convertToUTCDateTime(com.entityassist.RootEntity.getNow());
    }
}



