package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.dto.ISystems;

import java.util.UUID;

public interface ISystemsService<J extends ISystemsService<J>>
{
	ISystems<?> getActivityMaster(ISystems<?> system, UUID... token);
	
	ISystems<?> getActivityMaster(IEnterprise<?> requestingSystem, UUID... token);
	
	boolean doesSystemExist(IEnterprise<?> enterprise, String systemName, UUID... token);
	
	ISystems<?> findSystem(IEnterprise<?> enterprise, String systemName, UUID... token);
	
	ISystems<?> findSystem(ISystems<?> system, String systemName, UUID... token);

	ISystems<?> findSystem(ISystems<?> system, UUID token, UUID... identityToken);

	UUID registerNewSystem(IEnterprise<?> enterprise, ISystems<?> newSystem);
	
	ISystems<?> create(IEnterprise<?> enterprise, String systemName, String systemDesc, UUID... identityToken);
	
	ISystems<?> create(IEnterprise<?> enterprise, String systemName, String systemDesc, String historyName, UUID... identityToken);

	UUID getSecurityIdentityToken(ISystems<?> system, UUID... identityToken);
}
