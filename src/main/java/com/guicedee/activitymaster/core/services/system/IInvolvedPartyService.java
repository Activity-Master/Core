package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.*;
import com.guicedee.guicedinjection.pairing.Pair;
import jakarta.cache.annotation.CacheKey;
import jakarta.cache.annotation.CacheResult;

import java.util.List;
import java.util.UUID;

public interface IInvolvedPartyService<J extends IInvolvedPartyService<J>>
{
	String InvolvedPartySystemName = "Involved Party System";
	
	IInvolvedParty<?> findByID(UUID id);
	
	IInvolvedPartyNameType<?> createNameType(ITypeValue<?> name, String description, ISystems<?> system);
	
	IInvolvedPartyNameType<?> createNameType(String name, String description, ISystems<?> system, UUID... identityToken);
	
	IInvolvedPartyIdentificationType<?> createIdentificationType(ISystems<?> system, ITypeValue<?> name, String description, UUID... identityToken);
	
	IInvolvedPartyIdentificationType<?> createIdentificationType(ISystems<?> system, String name, String description, UUID... identityToken);
	
	IInvolvedPartyType<?> createType(ISystems<?> system, ITypeValue<?> name, String description);
	
	IInvolvedPartyType<?> createType(ISystems<?> system, String name, String description, UUID... identityToken);
	
	IInvolvedPartyOrganicType<?> createOrganicType(ISystems<?> system, ITypeValue<?> name, String description, UUID... identityToken);
	
	IInvolvedPartyIdentificationType<?> findIdentificationType(IIdentificationType<?> idType, ISystems<?> system, UUID... tokens);
	
	IInvolvedPartyIdentificationType<?> findIdentificationType(String idType, ISystems<?> system, UUID... tokens);
	
	IInvolvedParty<?> findByIdentificationType(IIdentificationType<?> idType, String value, ISystems<?> system, UUID... tokens);
	
	@CacheResult(cacheName = "InvolvedPartyFindByIdentificationType")
	IInvolvedParty<?> findByResourceItem(@CacheKey IResourceItem<?> idType, @CacheKey String value, ISystems<?> system, @CacheKey UUID... tokens);
	
	IInvolvedParty<?> findByUsernameAndPassword(String username, String password, ISystems<?> originatingSystem, boolean throwForNoUser, UUID... token);
	
	boolean doesUsernameExist(String username, ISystems<?> system, UUID... token);
	
	IInvolvedParty<?> findByUsername(String username, ISystems<?> system, UUID... token);
	
	IInvolvedParty<?> addUpdateUsernamePassword(IEvent<?> event, String username, String password, IInvolvedParty<?> involvedParty, ISystems<?> originatingSystem, UUID... token);
	
	IInvolvedParty<?> create(ISystems<?> originatingSystem, Pair<IIdentificationType<?>, String> idTypes,
	                         boolean isOrganic, UUID... identityToken);
	
	IInvolvedPartyType<?> findType(ITypeValue<?> idType, ISystems<?> system, UUID... tokens);
	
	IInvolvedPartyType<?> findType(String nameType, ISystems<?> system, UUID... tokens);
	
	IInvolvedPartyNameType<?> findNameType(INameType<?> idType, ISystems<?> system, UUID... tokens);
	
	IInvolvedPartyNameType<?> findNameType(String nameType, ISystems<?> system, UUID... tokens);
	
	IInvolvedParty<?> findByToken(ISecurityToken<?> token, UUID... tokens);
	
	IInvolvedParty<?> findByUUID(UUID token, ISystems<?> system, UUID... tokens);
	
	IInvolvedParty<?> findByIdentificationType(String identificationType, String value);
	
	List<IRelationshipValue<IInvolvedParty<?>, IInvolvedPartyIdentificationType<?>, ?>> findAllByIdentificationType(String identificationType, String value);
	
	List<IInvolvedParty<?>> findByRulesClassification(String classification, String value, ISystems<?> system, UUID... identityToken);
	
	IInvolvedParty<?> findByClassification(String classification, String value, ISystems<?> system, UUID... identityToken);
}
