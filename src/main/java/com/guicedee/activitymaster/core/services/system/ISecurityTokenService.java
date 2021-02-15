package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.classifications.securitytokens.ISecurityTokenClassification;
import com.guicedee.activitymaster.core.services.dto.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

public interface ISecurityTokenService<J extends ISecurityTokenService<J>>
{
	String SecurityTokenSystemName = "Security Tokens System";
	
	void grantAccessToToken(ISecurityToken<?> fromToken, ISecurityToken<?> toToken,
	                        boolean create, boolean update, boolean delete, boolean read, ISystems<?> system);
	
	void grantAccessToToken(@NotNull ISecurityToken<?> fromToken, @NotNull ISecurityToken<?> toToken,
	                        boolean create, boolean update, boolean delete, boolean read,
	                        ISystems<?> system, String originalId,
	                        Date effectiveFromDate, Date effectiveToDate);
	
	ISecurityToken<?> create(ISecurityTokenClassification<?> classificationValue, String name, String description, ISystems<?> system);
	
	ISecurityToken<?> create(ISecurityTokenClassification<?> classificationValue, String name, String description, ISystems<?> system, ISecurityToken<?> parent, UUID... identityToken);
	
	void link(ISecurityToken<?> parent, ISecurityToken<?> child, IClassification<?> classification, UUID... identifyingToken);
	
	ISecurityToken<?> getEveryoneGroup(ISystems<?> system, UUID... identityToken);
	
	ISecurityToken<?> getEverywhereGroup(ISystems<?> system, UUID... identityToken);
	
	ISecurityToken<?> getGuestsFolder(ISystems<?> system, UUID... identityToken);
	
	ISecurityToken<?> getRegisteredGuestsFolder(ISystems<?> system, UUID... identityToken);
	
	ISecurityToken<?> getVisitorsGuestsFolder(ISystems<?> system, UUID... identityToken);
	
	ISecurityToken<?> getAdministratorsFolder(ISystems<?> system, UUID... identityToken);
	
	ISecurityToken<?> getSystemsFolder(ISystems<?> system, UUID... identityToken);
	
	ISecurityToken<?> getPluginsFolder(ISystems<?> system, UUID... identityToken);
	
	ISecurityToken<?> getApplicationsFolder(ISystems<?> system, UUID... identityToken);
	
	ISecurityToken<?> getSecurityToken(UUID identifyingToken, ISystems<?> system, UUID... identityToken);
}
