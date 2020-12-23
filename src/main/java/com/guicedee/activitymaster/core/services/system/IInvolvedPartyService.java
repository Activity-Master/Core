package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IIdentificationType;
import com.guicedee.activitymaster.core.services.enumtypes.INameType;
import com.guicedee.activitymaster.core.services.enumtypes.ITypeValue;
import com.guicedee.guicedinjection.pairing.Pair;

import java.util.List;
import java.util.UUID;

public interface IInvolvedPartyService<J extends IInvolvedPartyService<J>>
{
	
	IInvolvedParty<?> findByID(UUID id);
	
	IInvolvedPartyNameType<?> createNameType(ITypeValue<?> name, String description, IEnterprise<?> enterprise);
	
	IInvolvedPartyNameType<?> createNameType(String name, String description, IEnterprise<?> enterprise, UUID... identityToken);
	
	IInvolvedPartyIdentificationType<?> createIdentificationType(IEnterprise<?> enterprise, ITypeValue<?> name, String description, UUID... identityToken);
	
	IInvolvedPartyIdentificationType<?> createIdentificationType(IEnterprise<?> enterprise, String name, String description, UUID... identityToken);
	
	IInvolvedPartyType<?> createType(IEnterprise<?> enterprise, ITypeValue<?> name, String description);
	
	IInvolvedPartyType<?> createType(IEnterprise<?> enterprise, String name, String description, UUID... identityToken);
	
	IInvolvedPartyOrganicType<?> createOrganicType(IEnterprise<?> enterprise, ITypeValue<?> name, String description, UUID... identityToken);
	
	IInvolvedPartyIdentificationType<?> findIdentificationType(IIdentificationType<?> idType, IEnterprise<?> enterprise, UUID... tokens);
	
	IInvolvedPartyIdentificationType<?> findIdentificationType(String idType, IEnterprise<?> enterprise, UUID... tokens);
	
	IInvolvedParty<?> findByIdentificationType(IIdentificationType<?> idType, String value, ISystems<?> system, UUID... tokens);
	
	IInvolvedParty<?> findByUsernameAndPassword(String username, String password, ISystems<?> originatingSystem, boolean throwForNoUser, UUID... token);
	
	boolean doesUsernameExist(String username, IEnterprise<?> enterprise, UUID... token);
	
	IInvolvedParty<?> findByUsername(String username, IEnterprise<?> enterprise, UUID... token);
	
	IInvolvedParty<?> addUpdateUsernamePassword(IEvent<?> event, String username, String password, IInvolvedParty<?> involvedParty, ISystems<?> originatingSystem, UUID... token);
	
	IInvolvedParty<?> create(ISystems<?> originatingSystem, Pair<IIdentificationType<?>, String> idTypes,
	                         boolean isOrganic, UUID... identityToken);
	
	IInvolvedPartyType<?> findType(ITypeValue<?> idType, IEnterprise<?> enterprise, UUID... tokens);
	
	IInvolvedPartyType<?> findType(String nameType, IEnterprise<?> enterprise, UUID... tokens);
	
	IInvolvedPartyNameType<?> findNameType(INameType<?> idType, IEnterprise<?> enterprise, UUID... tokens);
	
	IInvolvedPartyNameType<?> findNameType(String nameType, IEnterprise<?> enterprise, UUID... tokens);
	
	IInvolvedParty<?> findByToken(ISecurityToken<?> token, UUID... tokens);
	
	IInvolvedParty<?> findByUUID(UUID token, IEnterprise<?> enterprise, UUID... tokens);
	
	IInvolvedParty<?> findByIdentificationType(String identificationType, String value);
	
	List<IRelationshipValue<IInvolvedParty<?>, IInvolvedPartyIdentificationType<?>, ?>> findAllByIdentificationType(String identificationType, String value);
}
