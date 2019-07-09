package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyIdentificationType;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyNameType;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyType;
import com.armineasy.activitymaster.activitymaster.services.dto.*;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IIdentificationType;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.INameType;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.ITypeValue;
import com.jwebmp.guicedinjection.pairing.Pair;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

public interface IInvolvedPartyService<J extends IInvolvedPartyService<J>>
{

	IInvolvedPartyNameType<?> createNameType(ITypeValue<?> name, String description, IEnterprise<?> enterprise);

	IInvolvedPartyNameType<?> createNameType(String name, String description, IEnterprise<?> enterprise, UUID... identityToken);

	IInvolvedPartyIdentificationType<?> createIdentificationType(IEnterprise<?> enterprise, ITypeValue name, String description, UUID... identityToken);

	IInvolvedPartyType<?> createType(IEnterprise<?> enterprise, ITypeValue<?> name, String description);

	IInvolvedPartyType<?> createType(IEnterprise<?> enterprise, String name, String description, UUID... identityToken);

	IInvolvedPartyOrganicType<?> createOrganicType(IEnterprise<?> enterprise, ITypeValue<?> name, String description, UUID... identityToken);

	IInvolvedPartyIdentificationType<?> findIdentificationType(IIdentificationType<?> idType, IEnterprise<?> enterprise, UUID... tokens);


	IInvolvedParty<?> findByIdentificationType(IIdentificationType<?> idType, String value, ISystems<?> system, UUID... tokens);

	IInvolvedParty<?> findByUsernameAndPassword(String username, String password, ISystems<?> originatingSystem, boolean throwForNoUser, UUID... token);


	boolean doesUsernameExist(String username, IEnterprise<?> enterprise, UUID... token);

	IInvolvedParty<?> findByUsername(String username, IEnterprise<?> enterprise, UUID... token);

	IInvolvedParty<?> addUpdateUsernamePassword(IEvent<?> event, String username, String password, IInvolvedParty<?> involvedParty, ISystems<?> originatingSystem, UUID... token);

	IInvolvedParty<?> create(ISystems<?> originatingSystem, Pair<IIdentificationType<?>, String> idTypes,
	                         boolean isOrganic, UUID... identityToken);

	IInvolvedPartyType<?> findType(ITypeValue<?> idType, IEnterprise<?> enterprise, UUID... tokens);

	IInvolvedPartyNameType<?> findNameType(INameType<?> idType, IEnterprise<?> enterprise, UUID... tokens);

	IInvolvedParty<?> findByToken(@CacheKey ISecurityToken<?> token, @CacheKey UUID... tokens);

	IInvolvedParty<?> findByUUID(@CacheKey UUID token, @CacheKey IEnterprise<?> enterprise, @CacheKey UUID... tokens);
}
