package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.IActivityMasterService;
import com.guicedee.activitymaster.fsdm.client.services.IEnterpriseService;
import com.guicedee.activitymaster.fsdm.client.services.annotations.ActivityMasterDB;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.systems.IProgressable;
import com.guicedee.guicedpersistence.db.annotations.Transactional;
import lombok.extern.java.Log;

import java.sql.Connection;
import java.util.logging.Level;

import static com.guicedee.client.IGuiceContext.*;

@Log
public class ActivityMasterService
		implements IProgressable,
		           IActivityMasterService
{
	@Inject
	private IEnterpriseService<?> enterpriseService;
	
	@Override
	public void loadSystems(String enterpriseName)
	{
		enterpriseService.performPostStartup(enterpriseService.getEnterprise(enterpriseName));
	}
	
	@Override
	public void loadUpdates(IEnterprise<?, ?> enterprise)
	{
		enterpriseService.loadUpdates(enterprise);
	}
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public void runScript(String script)
	{
		try (java.sql.Statement st = com.guicedee.client.IGuiceContext.get(Connection.class, ActivityMasterDB.class)
		                                         .createStatement())
		{
			st.executeUpdate(script);
		}
		catch (java.sql.SQLException e)
		{
			log.log(Level.SEVERE, "Unable to execute updates to hierarchy", e);
		}
	}
	@Transactional(entityManagerAnnotation = ActivityMasterDB.class)
	@Override
	public void updatePartitionBases()
	{
		javax.sql.DataSource ds = get(javax.sql.DataSource.class, ActivityMasterDB.class);
		var c = com.guicedee.client.IGuiceContext.get(Connection.class, ActivityMasterDB.class);
		try (
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
			log.log(Level.SEVERE, "Unable to execute updates to partitions file structure", e);
		}
	}
	
}
