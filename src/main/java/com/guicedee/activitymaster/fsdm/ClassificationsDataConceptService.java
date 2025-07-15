package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationDataConceptService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.activeflag.IActiveFlag;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;



import java.util.NoSuchElementException;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts.*;

@Log4j2
public class ClassificationsDataConceptService
		implements IClassificationDataConceptService<ClassificationsDataConceptService>
{
	@Inject
	private IEnterprise<?,?> enterprise;

	@Override
	public IClassificationDataConcept<?,?> get()
	{
		return new ClassificationDataConcept();
	}

	//@Transactional()
	public Uni<IClassificationDataConcept<?,?>> createDataConcept(EnterpriseClassificationDataConcepts name,
	                                                   String description,
	                                                   ISystems<?,?> system,
	                                                   java.util.UUID... identityToken)
	{
		ClassificationDataConcept newConcept = new ClassificationDataConcept();
		return newConcept.builder()
		                 .withName(name.classificationValue())
		                 .withEnterprise(enterprise)
		                 .inActiveRange()
		                 .inDateRange()
		                 .getCount()
		                 .onFailure().invoke(error -> log.error("Error checking if concept exists: {}", error.getMessage(), error))
		                 .chain(count -> {
		                     boolean exists = count > 0;
		                     if (!exists)
		                     {
		                         newConcept.setDescription(description);
		                         newConcept.setName(name.classificationValue());
		                         newConcept.setSystemID(system);
		                         newConcept.setOriginalSourceSystemID(system.getId());

		                         IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);
		                         return acService.getActiveFlag(enterprise)
		                                        .chain(activeFlag -> {
		                                            newConcept.setActiveFlagID(activeFlag);
		                                            newConcept.setEnterpriseID(enterprise);
		                                            return newConcept.persist()
		                                                           .map(persisted -> {
		                                                               // Start createDefaultSecurity in parallel without waiting for it
		                                                               persisted.createDefaultSecurity(system, identityToken)
		                                                                       .subscribe().with(
		                                                                           result -> {
		                                                                               // Security setup completed successfully
		                                                                           },
		                                                                           error -> {
		                                                                               // Log error but don't fail the main operation
		                                                                               log.warn("Error in createDefaultSecurity", error);
		                                                                           }
		                                                                       );

		                                                               // Return the persisted concept immediately without waiting for security setup
		                                                               return (IClassificationDataConcept<?,?>) persisted;
		                                                           });
		                                        });
		                     }
		                     else
		                     {
		                         return find(name, system, identityToken);
		                     }
		                 });
	}

	@Override
	//@CacheResult(cacheName = "GetGlobalConcept")
	public Uni<IClassificationDataConcept<?,?>> getGlobalConcept( ISystems<?,?> system,  java.util.UUID... identityToken)
	{
		return find(GlobalClassificationsDataConceptName, system, identityToken);
	}

	@Override
	//@CacheResult(cacheName = "FindConceptWithConceptValueAndSystem")
	public Uni<IClassificationDataConcept<?,?>> find( EnterpriseClassificationDataConcepts name,  ISystems<?,?> system,  java.util.UUID... identityToken)
	{
		return find(name.classificationValue(), system, identityToken);
	}

	//@Transactional()
	//@CacheResult(cacheName = "FindConceptWithConceptValueAndSystemString")
	public Uni<IClassificationDataConcept<?,?>> find( String name,  ISystems<?,?> system,  java.util.UUID... identityToken)
	{
		ClassificationDataConcept cdc = new ClassificationDataConcept();
		return cdc.builder()
		         .withName(name)
		         .withEnterprise(enterprise)
		         .inActiveRange()
		         .inDateRange()
		       //  .canRead(system, identityToken)
		         .get()
		         .onFailure().invoke(error -> log.error("Error finding concept by name: {}", error.getMessage(), error))
		         .onItem().ifNull().failWith(() -> new NoSuchElementException("Cannot find Classification Data Concept with name - " + name))
		         .map(concept -> (IClassificationDataConcept<?,?>) concept);
	}

	@Override
	//@CacheResult(cacheName = "NoDataConcept")
	public Uni<IClassificationDataConcept<?,?>> getNoConcept( ISystems<?,?> system,  java.util.UUID... identityToken)
	{
		return find(NoClassificationDataConceptName, system, identityToken);
	}

	@Override
	//@CacheResult(cacheName = "SecurityHierarchyConcept")
	public Uni<IClassificationDataConcept<?,?>> getSecurityHierarchyConcept( ISystems<?,?> system,  java.util.UUID... identityToken)
	{
		return find(EnterpriseClassificationDataConcepts.SecurityTokenXSecurityToken, system, identityToken);
	}
}
