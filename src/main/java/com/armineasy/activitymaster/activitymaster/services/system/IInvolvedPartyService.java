package com.armineasy.activitymaster.activitymaster.services.system;

import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedParty;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyIdentificationType;
import com.armineasy.activitymaster.activitymaster.db.entities.involvedparty.InvolvedPartyNameType;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.services.IIdentificationType;
import com.armineasy.activitymaster.activitymaster.services.INameType;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import java.util.UUID;

public interface IInvolvedPartyService
{
	@CacheResult(cacheName = "InvolvedPartyGetIdentificationType")
	InvolvedPartyIdentificationType findIdentificationType(@CacheKey IIdentificationType<?> idType, @CacheKey Enterprise enterprise, @CacheKey UUID... tokens);

	@CacheResult(cacheName = "InvolvedPartyFindByIdentificationType")
	InvolvedParty findByIdentificationType(
			@CacheKey IIdentificationType<?> idType, @CacheKey String value, @CacheKey Systems system, @CacheKey UUID... tokens);

	InvolvedParty findByUsernameAndPassword(String username, String password, Systems originatingSystem, boolean throwForNoUser, UUID... token);

	@CacheResult(cacheName = "InvolvedPartyGetNameType")
	InvolvedPartyNameType findNameType(INameType<?> idType, Enterprise enterprise, UUID... tokens);
}
