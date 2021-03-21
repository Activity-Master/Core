package com.guicedee.activitymaster.core.threads;


import com.guicedee.activitymaster.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.client.services.annotations.ActivityMasterDB;
import com.guicedee.guicedpersistence.db.annotations.Transactional;

import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class IdentifiedThread<J extends IdentifiedThread<J>>
		implements Runnable
{
	private static final Logger log = Logger.getLogger(IdentifiedThread.class.getName());

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
	
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	public abstract void perform();

	public Object call() throws Exception
	{
		run();
		return null;
	}

	public ActivityMasterConfiguration.ActivityMasterConfigurationDTO getActivityMasterConfigurationDTO()
	{
		return this.activityMasterConfigurationDTO;
	}

	public IdentifiedThread<J> setActivityMasterConfigurationDTO(ActivityMasterConfiguration.ActivityMasterConfigurationDTO activityMasterConfigurationDTO)
	{
		this.activityMasterConfigurationDTO = activityMasterConfigurationDTO;
		return this;
	}

	public boolean equals(final Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (!(o instanceof IdentifiedThread))
		{
			return false;
		}
		final IdentifiedThread<?> other = (IdentifiedThread<?>) o;
		if (!other.canEqual((Object) this))
		{
			return false;
		}
		final Object this$activityMasterConfigurationDTO = this.getActivityMasterConfigurationDTO();
		final Object other$activityMasterConfigurationDTO = other.getActivityMasterConfigurationDTO();
		if (this$activityMasterConfigurationDTO == null
		    ? other$activityMasterConfigurationDTO != null
		    : !this$activityMasterConfigurationDTO.equals(other$activityMasterConfigurationDTO))
		{
			return false;
		}
		return true;
	}

	protected boolean canEqual(final Object other)
	{
		return other instanceof IdentifiedThread;
	}

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final Object $activityMasterConfigurationDTO = this.getActivityMasterConfigurationDTO();
		result = result * PRIME + ($activityMasterConfigurationDTO == null ? 43 : $activityMasterConfigurationDTO.hashCode());
		return result;
	}

	public String toString()
	{
		return "IdentifiedThread(activityMasterConfigurationDTO=" + this.getActivityMasterConfigurationDTO() + ")";
	}
}
