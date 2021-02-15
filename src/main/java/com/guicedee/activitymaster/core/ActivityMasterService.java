package com.guicedee.activitymaster.core;

import com.google.inject.Inject;
import com.guicedee.activitymaster.core.db.ActivityMasterDB;
import com.guicedee.activitymaster.core.services.*;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.services.system.IActivityMasterService;
import com.guicedee.activitymaster.core.services.system.IEnterpriseService;
import com.guicedee.logger.LogFactory;
import lombok.extern.java.Log;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;

import static com.guicedee.guicedinjection.GuiceContext.*;

@Log
public class ActivityMasterService
		implements IProgressable,
		           IActivityMasterService
{
	@Inject
	private ActivityMasterConfiguration configuration;
	@Inject
	private IEnterprise<?> enterprise;
	@Inject
	private IEnterpriseService<?> enterpriseService;
	
	@Override
	public void loadSystems(IEnterpriseName<?> enterpriseName, IActivityMasterProgressMonitor progressMonitor)
	{
		Set<IActivityMasterSystem<?>> allSystems = configuration.getAllSystems();
		try
		{
			for (IActivityMasterSystem<?> allSystem : allSystems)
			{
				logProgress("System Loading", "Starting up system " + allSystem.getClass()
				                                                               .getName(), 1, progressMonitor);
				allSystem.postStartup(enterprise, progressMonitor);
			}
		}
		catch (NoSuchElementException e)
		{
			log.log(Level.SEVERE, "Unable to load a system!", e);
		}
	}
	
	@Override
	public void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor)
	{
		enterpriseService.loadUpdates(enterprise, progressMonitor);
		if (progressMonitor != null)
		{
			int cur = progressMonitor.getTotalTasks() - progressMonitor.getCurrentTask();
			logProgress("System Updating", "Completed Updating Systems", cur, progressMonitor);
		}
	}
	
	@Override
	public void runScript(String script)
	{
		javax.sql.DataSource ds = get(javax.sql.DataSource.class, ActivityMasterDB.class);
		try (java.sql.Statement st = ds.getConnection()
		                               .createStatement())
		{
			st.executeUpdate(script);
		}
		catch (java.sql.SQLException e)
		{
			LogFactory.getLog("ActivityMasterService")
			          .log(Level.SEVERE, "Unable to execute updates to hierarchy", e);
		}
	}
	
	@Override
	public void updatePartitionBases()
	{
		javax.sql.DataSource ds = get(javax.sql.DataSource.class, ActivityMasterDB.class);
		
		try (java.sql.Connection c = ds.getConnection();
		     java.sql.CallableStatement st = c.prepareCall("{call CreateResourceDataPartitions (?)}");
		     java.sql.CallableStatement stPar = c.prepareCall("{call CreateEventDataPartitions (?)}");
		
		     java.sql.CallableStatement stPar1 = c.prepareCall("{call CreateAddressDataPartitions (?)}");
		     java.sql.CallableStatement stPar2 = c.prepareCall("{call CreateAddressXClassificationDataPartitions (?)}");
		     java.sql.CallableStatement stPar3 = c.prepareCall("{call CreateAddressXGeographyDataPartitions (?)}");
		     java.sql.CallableStatement stPar4 = c.prepareCall("{call CreateAddressXResourceItemDataPartitions (?)}");
		     java.sql.CallableStatement stPar5 = c.prepareCall("{call CreateArrangementXClassificationPartitions (?)}");
		     java.sql.CallableStatement stPar6 = c.prepareCall("{call CreateArrangementXProductPartitions (?)}");
		     java.sql.CallableStatement stPar7 = c.prepareCall("{call CreateArrangementXResourceItemPartitions (?)}");
		     java.sql.CallableStatement stPar71 = c.prepareCall("{call CreateArrangementXRulesPartitions (?)}");
		     java.sql.CallableStatement stPar72 = c.prepareCall("{call CreateArrangementXRulesTypePartitions (?)}");
		     java.sql.CallableStatement stPar8 = c.prepareCall("{call CreateEventXAddressPartitions (?)}");
		     java.sql.CallableStatement stPar9 = c.prepareCall("{call CreateEventXArrangementPartitions (?)}");
		     java.sql.CallableStatement stPar10 = c.prepareCall("{call CreateEventXClassificationPartitions (?)}");
		     java.sql.CallableStatement stPar11 = c.prepareCall("{call CreateEventXGeographyPartitions (?)}");
		     java.sql.CallableStatement stPar12 = c.prepareCall("{call CreateEventXProductPartitions (?)}");
		     java.sql.CallableStatement stPar13 = c.prepareCall("{call CreateEventXResourceItemPartitions (?)}");
		     java.sql.CallableStatement stPar14 = c.prepareCall("{call CreateEventXRulesPartitions (?)}");
		     java.sql.CallableStatement stPar15 = c.prepareCall("{call CreateInvolvedPartyDataPartitions (?)}");
		     java.sql.CallableStatement stPar16 = c.prepareCall("{call CreateResourceDataPartitions (?)}");
		)
		{
			st.setString(1, "ResourceItemData");
			st.execute();
			stPar.setString(1, "EventData");
			stPar.execute();
			stPar1.setString(1, "AddressData");
			stPar1.execute();
			stPar2.setString(1, "AddressXClassificationData");
			stPar2.execute();
			stPar3.setString(1, "AddressXGeographyData");
			stPar3.execute();
			stPar4.setString(1, "AddressXResourceItemData");
			stPar4.execute();
			stPar5.setString(1, "ArrangementXClassification");
			stPar5.execute();
			stPar6.setString(1, "ArrangementXProduct");
			stPar6.execute();
			stPar7.setString(1, "ArrangementXResourceItem");
			stPar7.execute();
			stPar71.setString(1, "ArrangementXRules");
			stPar71.execute();
			stPar72.setString(1, "ArrangementXRulesType");
			stPar72.execute();
			stPar8.setString(1, "EventXAddress");
			stPar8.execute();
			stPar9.setString(1, "EventXArrangement");
			stPar9.execute();
			stPar10.setString(1, "EventXClassification");
			stPar10.execute();
			stPar11.setString(1, "EventXGeography");
			stPar11.execute();
			stPar12.setString(1, "EventXProduct");
			stPar12.execute();
			stPar13.setString(1, "EventXResourceItem");
			stPar13.execute();
			stPar14.setString(1, "EventXRules");
			stPar14.execute();
			stPar15.setString(1, "InvolvedPartyData");
			stPar15.execute();
			stPar16.setString(1, "ResourceData");
			stPar16.execute();
		}
		catch (java.sql.SQLException e)
		{
			LogFactory.getLog("ActivityMasterService")
			          .log(Level.SEVERE, "Unable to execute updates to partitions file structure", e);
		}
	}
	
}
