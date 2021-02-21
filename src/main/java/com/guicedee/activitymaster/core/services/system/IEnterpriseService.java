package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.services.IActivityMasterProgressMonitor;
import com.guicedee.activitymaster.core.services.classifications.enterprise.IEnterpriseName;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.activitymaster.core.updates.ISystemUpdate;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.*;

public interface IEnterpriseService<J extends IEnterpriseService<J>>
{
	String EnterpriseSystemName = "Enterprise System";
	
	void loadUpdates(IEnterprise<?> enterprise, IActivityMasterProgressMonitor progressMonitor);
	
//	LocalDate getEnterpriseLastUpdateDate(IEnterprise<?> enterprise);
	
	//Map<LocalDate, Class<? extends ISystemUpdate>> getUpdates(LocalDate lastUpdateDate);
	
	Set<String> getEnterpriseAppliedUpdates(IEnterprise<?> enterprise);
	
	Map<LocalDate, Class<? extends ISystemUpdate>> getUpdates(IEnterprise<?> enterprise);
	
	Map<LocalDate, Class<? extends ISystemUpdate>> getAllUpdates();
	
	List<IEnterprise<?>> findEnterprisesWithClassification(Classification classification);
	
	IEnterprise<?> getEnterprise(String name);
	
	boolean doesEnterpriseExist(String name);
	
	/**
	 * Gets an enterprise or throws an exception.
	 *
	 * Result is cached
	 *
	 * @param name the name of the enterprise
	 *
	 * @return The enterprise
	 */
	IEnterprise<?> getEnterprise(IEnterpriseName<?> name);

	Set<IEnterprise<?>> getIEnterprises();

	IEnterprise<?> getIEnterpriseFromName(IEnterpriseName<?> enterprise);

	IEnterprise<?> getIEnterpriseFromID(UUID enterprise);
	
	IEnterprise<?> startNewEnterprise(IEnterpriseName<?> enterpriseName,
	                                  @NotNull String adminUserName, @NotNull String adminPassword, IActivityMasterProgressMonitor progressMonitor);
	
	IEnterprise<?> startNewEnterprise(String enterpriseName,
	                                  @NotNull String adminUserName, @NotNull String adminPassword, UUID uuidIdentifier, IActivityMasterProgressMonitor progressMonitor);
	
	void createNewEnterprise(@NotNull IEnterprise<?> enterprise,  IActivityMasterProgressMonitor progressMonitor);
}
