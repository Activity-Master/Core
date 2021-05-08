package com.guicedee.activitymaster.fsdm.injections;

import com.guicedee.guicedhazelcast.HazelcastProperties;
import com.guicedee.guicedinjection.interfaces.IGuicePreStartup;

public class FSDMHazelcastPreStartup implements IGuicePreStartup<FSDMHazelcastPreStartup>
{
	@Override
	public void onStartup()
	{
		if(!System.getProperties()
		          .containsKey("hazelcast.group"))
		{
			HazelcastProperties.setStartLocal(true);
		}
		
		HazelcastProperties.setGroupName(System.getProperty("hazelcast.group", "local-dev"));
		HazelcastProperties.setInstanceName(HazelcastProperties.getGroupName());
		if (HazelcastProperties.getAddress() == null)
		{
			HazelcastProperties.setAddress(System.getProperty("hazelcast.serverip", "127.0.0.1"));
		}
	}
	
	@Override
	public Integer sortOrder()
	{
		return Integer.MIN_VALUE;
	}
}
