package com.armineasy.activitymaster.activitymaster.threads;

import com.armineasy.activitymaster.activitymaster.ActivityMasterConfiguration;
import com.armineasy.activitymaster.activitymaster.db.entities.enterprise.Enterprise;
import com.armineasy.activitymaster.activitymaster.db.entities.security.SecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.systems.Systems;
import com.armineasy.activitymaster.activitymaster.implementations.EnterpriseService;
import com.armineasy.activitymaster.activitymaster.implementations.SystemsService;
import com.armineasy.activitymaster.activitymaster.services.system.ISecurityTokenService;
import com.armineasy.activitymaster.activitymaster.services.system.ISystemsService;
import com.jwebmp.guicedinjection.GuiceContext;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.java.Log;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import static com.armineasy.activitymaster.activitymaster.DefaultEnterprise.*;
import static com.jwebmp.guicedinjection.GuiceContext.*;

@Data
@Accessors(chain = true)
@Log
public abstract class IdentifiedThread<J extends IdentifiedThread<J>>
		implements Runnable
{
	private ActivityMasterConfiguration.ActivityMasterConfigurationDTO activityMasterConfigurationDTO;

	public IdentifiedThread()
	{
		this.activityMasterConfigurationDTO = new ActivityMasterConfiguration.ActivityMasterConfigurationDTO()
				                                      .fromCurrentThread();
	}

	public void run()
	{
		try
		{
			startup();
			perform();
		}catch(Throwable T)
		{
			log.log(Level.SEVERE, "Unable to run thread", T);
		}
	}

	protected void startup()
	{
		ActivityMasterConfiguration.get()
		                           .configureThread(this.activityMasterConfigurationDTO);
	}

	@SuppressWarnings("unchecked")
	public J fromCurrentThread()
	{
		activityMasterConfigurationDTO = new ActivityMasterConfiguration.ActivityMasterConfigurationDTO()
				                                 .fromCurrentThread();
		return (J) this;
	}

	public abstract void perform();

	public Object call() throws Exception
	{
		run();
		return null;
	}
}