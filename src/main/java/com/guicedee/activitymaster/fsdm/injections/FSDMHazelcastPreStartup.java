package com.guicedee.activitymaster.fsdm.injections;

import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedinjection.interfaces.IGuicePreStartup;
import com.guicedee.guicedinjection.properties.GlobalProperties;
import io.vertx.core.Future;

import java.util.List;

public class FSDMHazelcastPreStartup implements IGuicePreStartup<FSDMHazelcastPreStartup>
{
	@Override
	public List<Future<Boolean>> onStartup()
	{
		if (GlobalProperties.getSystemPropertyOrEnvironment("HAZELCAST", "false")
		                     .equals("true"))
		{
			HazelcastProperties.setStartLocal(true);
		}
		if (!GlobalProperties.getSystemPropertyOrEnvironment("GROUP_NAME", "dev")
		                     .equals("dev"))
		{
			String groupName = GlobalProperties.getSystemPropertyOrEnvironment("GROUP_NAME", "dev");
			HazelcastProperties.setGroupName(groupName);
			HazelcastProperties.setInstanceName(HazelcastProperties.getGroupName());
		}
		
		if (!GlobalProperties.getSystemPropertyOrEnvironment("CLIENT_ADDRESS", "localhost")
		                     .equals("localhost"))
		{
			HazelcastProperties.setAddress(GlobalProperties.getSystemPropertyOrEnvironment("CLIENT_ADDRESS", "localhost"));
		}
		return List.of(Future.succeededFuture(true));
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE;
	}
}
