package com.guicedee.activitymaster.fsdm.injections;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.ISecurityTokenService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.security.ISecurityToken;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.db.entities.enterprise.Enterprise;
import com.guicedee.client.IGuiceContext;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.UUID;

public class DataCleanupService
{
	@Inject
	private ISystemsService<?> systemsService;
	@Inject
	private ISecurityTokenService<?> securityTokenService;
	
	@Transactional
	public void cleanup()
	{
		EntityManager entityManager = IGuiceContext.get(EntityManager.class);
		
		int totalRecordsRemoved = 0;
		System.out.println("Checking for invalid active flag records.....");
		
		totalRecordsRemoved += entityManager.createNativeQuery("""
		                                                       delete from  party.involvedpartynonorganic where activeflagid not in (
		                                                       	select activeflagid from dbo.activeflag
		                                                       );
		                                                       """)
		                                    .executeUpdate();
		totalRecordsRemoved += entityManager.createNativeQuery("""
		                                                          delete from  party.involvedpartyorganic where activeflagid not in (
		                                                          select activeflagid from dbo.activeflag
		                                                          );
		                                                       """)
		                                    .executeUpdate();
		totalRecordsRemoved += entityManager.createNativeQuery("""
		                                                          delete from  party.involvedpartyxinvolvedpartytype where activeflagid not in (
		                                                                select activeflagid from dbo.activeflag
		                                                            );
		                                                       """)
		                                    .executeUpdate();
		totalRecordsRemoved += entityManager.createNativeQuery("""
		                                                          delete from  party.involvedpartytype where activeflagid not in (
		                                                                  select activeflagid from dbo.activeflag
		                                                              );
		                                                       """)
		                                    .executeUpdate();
		
		System.out.println("Number of records cleaned - " + totalRecordsRemoved);
		
		System.out.println("Checking security records of tables.... This may take a while......");
		entityManager.createQuery("select e from Enterprise e", Enterprise.class)
		             .getResultStream()
		             .forEach(enterpise -> {
			             System.out.println("Checking enterprise - " + enterpise.getName());
						 try
						 {
							 ISystems<?, ?> activityMasterSystem = systemsService.findSystem(enterpise, "Activity Master System", null);
							 UUID securityIdentityToken = systemsService.getSecurityIdentityToken(activityMasterSystem, null);
							 ISystems<?, ?> securityTokensSystem = systemsService.findSystem(enterpise, "Security Tokens System", null);
							 if (enterpise.getSecurities()
							              .isEmpty())
							 {
								 System.out.println("Enterprise requires a security refresh");
								 ISecurityToken<?,?> adminFolder = securityTokenService.getAdministratorsFolder(activityMasterSystem, null);
								 System.out.println("Admin folder exists...");
								 System.out.println("Reinstall Application....");
								 
							 }
						 }catch (Throwable e)
						 {
							 System.out.println("System is not installed");
							// e.printStackTrace();
						 }
		             });
	}
}
