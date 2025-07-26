package com.guicedee.activitymaster.fsdm;

import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
import com.guicedee.activitymaster.fsdm.client.services.IActiveFlagService;
import com.guicedee.activitymaster.fsdm.client.services.IClassificationService;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassification;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.classifications.EnterpriseClassificationDataConcepts;
import com.guicedee.activitymaster.fsdm.client.services.exceptions.ClassificationException;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.Classification;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.ClassificationDataConcept;
import com.guicedee.activitymaster.fsdm.db.entities.classifications.builders.ClassificationQueryBuilder;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;


import java.util.UUID;

import static com.guicedee.activitymaster.fsdm.client.services.classifications.DefaultClassifications.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.SecurityTokenClassifications.*;


@Log4j2
public class ClassificationService
		implements IClassificationService<ClassificationService>
{

	@Inject
	private ClassificationsDataConceptService dataConceptService;

	public IClassification<?, ?> get()
	{
		return new Classification();
	}

	@Override
	public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description, EnterpriseClassificationDataConcepts concept,
											 ISystems<?, ?> system, Integer sequenceOrder, String parentName, UUID... identityToken)
	{
		return find(session, parentName, system, identityToken)
		        .chain(classification -> create(session, name, description, concept, system, sequenceOrder, classification, identityToken));
	}

	@Override
	public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name,
											 ISystems<?, ?> system, UUID... identityToken)
	{
		return create(session, name, name, null, system, 0, identityToken);
	}

	@Override
	public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description,
											 ISystems<?, ?> system, UUID... identityToken)
	{
		return create(session, name, description, null, system, 0, identityToken);
	}

	@Override
	public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description, EnterpriseClassificationDataConcepts conceptName,
											 ISystems<?, ?> system, UUID... identityToken)
	{
		return create(session, name, description, conceptName, system, 0, identityToken);
	}

	@Override
	public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description, EnterpriseClassificationDataConcepts conceptName,
											 ISystems<?, ?> system,
											 Integer sequenceNumber, UUID... identityToken)
	{
		return create(session, name, description, conceptName, system, sequenceNumber, (IClassification<?, ?>) null, identityToken);
	}

	@Override
	//@Transactional()
	public Uni<IClassification<?, ?>> create(Mutiny.Session session, String name, String description, EnterpriseClassificationDataConcepts conceptName,
											 ISystems<?, ?> system,
											 Integer sequenceNumber, IClassification<?, ?> parent, UUID... identityToken)
	{
		// Get the data concept based on conceptName
		Uni<com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.classifications.IClassificationDataConcept<?, ?>> dataConceptUni;
		if (conceptName != null)
		{
			dataConceptUni = dataConceptService.find(session, conceptName, system, identityToken);
		}
		else
		{
			dataConceptUni = dataConceptService.find(session, "NoClassification", system, identityToken);
		}

		Classification rootCl = new Classification();
		var enterprise = system.getEnterprise();
		// Check if classification exists
		return rootCl.builder(session)
		             .withName(name)
		             .withEnterprise(enterprise)
		             .inActiveRange()
		             .inDateRange()
		             .getCount()
		             .onFailure().invoke(error -> log.error("Error checking if classification exists: {}", error.getMessage(), error))
		             .chain(count -> {
		                 boolean exists = count > 0;
		                 if (!exists)
		                 {
		                     // Classification doesn't exist, create it
		                     rootCl.setName(name);
		                     rootCl.setDescription(description);
		                     rootCl.setClassificationSequenceNumber(sequenceNumber == null ? 1 : sequenceNumber);
		                     rootCl.setSystemID(system);
		                     rootCl.setOriginalSourceSystemID(system.getId());
		                     rootCl.setOriginalSourceSystemUniqueID(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"));
		                     rootCl.setEnterpriseID(enterprise);

		                     // Get the active flag service
		                     IActiveFlagService<?> acService = IGuiceContext.get(IActiveFlagService.class);

		                     // Chain the data concept and active flag operations
		                     return dataConceptUni
		                             .chain(dataConcept -> {
		                                 // We need to handle the fact that dataConcept is an interface
		                                 // and setConcept expects a concrete ClassificationDataConcept
		                                 if (dataConcept instanceof ClassificationDataConcept) {
		                                     rootCl.setConcept((ClassificationDataConcept) dataConcept);
		                                 } else {
		                                     log.warn("DataConcept is not an instance of ClassificationDataConcept: {}", 
		                                              dataConcept.getClass().getName());
		                                     // In a real implementation, we might need to fetch the concrete instance
		                                     // or handle this differently
		                                 }
		                                 return acService.getActiveFlag(session, enterprise);
		                             })
		                             .chain(activeFlag -> {
		                                 rootCl.setActiveFlagID(activeFlag);
		                                 return rootCl.builder(session).persist(rootCl);
		                             })
		                             .map(persisted -> {
		                                 // Start createDefaultSecurity in parallel without waiting for it
		                                 rootCl.createDefaultSecurity(session, system, identityToken)
		                                       .subscribe().with(
		                                           result -> {
		                                               // Security setup completed successfully
		                                           },
		                                           error -> {
		                                               // Log error but don't fail the main operation
		                                               log.warn("Error in createDefaultSecurity", error);
		                                           }
		                                       );

		                                 // Handle parent relationship if needed
		                                 if (parent != null && !NoClassification.toString().equals(name))
		                                 {
		                                     find(session, parent.getName(), system, identityToken)
		                                         .subscribe().with(
		                                             foundParent -> {
		                                                 try {
		                                                     @SuppressWarnings("unchecked")
		                                                     IClassification<Classification, ClassificationQueryBuilder> pp = 
		                                                         (IClassification<Classification, ClassificationQueryBuilder>) foundParent;
		                                                     pp.addChild(session, rootCl, NoClassification.toString(), null, system, identityToken);
		                                                 } catch (Exception e) {
		                                                     log.warn("Error adding child to parent classification", e);
		                                                 }
		                                             },
		                                             error -> {
		                                                 log.warn("Error finding parent classification", error);
		                                             }
		                                         );
		                                 }

		                                 // Return the persisted classification immediately
		                                 return (IClassification<?, ?>) rootCl;
		                             });
		                 }
		                 else
		                 {
		                     // Classification exists, find and return it
		                     return find(session, name, conceptName, system, identityToken);
		                 }
		             });
	}

	//@CacheResult(cacheName = "ClassificationFindWithSimpleString")
	@Override
	public Uni<IClassification<?, ?>> find(Mutiny.Session session, String name, ISystems<?, ?> system, UUID... identityToken)
	{
		return find(session, name, null, system, identityToken);
	}

	//@Transactional()
	//@CacheResult(cacheName = "ClassificationFindWithSimpleStringWithConceptValue")
	@Override
	public Uni<IClassification<?, ?>> find(Mutiny.Session session, String name, EnterpriseClassificationDataConcepts concept, ISystems<?, ?> system, UUID... identityToken)
	{
		var enterprise = system.getEnterprise();
		Classification search = new Classification();
		return search.builder(session)
		               .withName(name)
		               .inActiveRange()
		               .inDateRange()
		               //   .canRead(system, identityToken)
		               .withEnterprise(enterprise)
		               .get()
		               .onFailure().invoke(error -> log.error("Error finding classification: {}", error.getMessage(), error))
		               .onItem().ifNull().failWith(() -> new ClassificationException("Cannot find Classification with name - [" + name + "] - and concept - [" + concept + "]"))
		               .map(classification -> (IClassification<?, ?>) classification);
	}

	//@Transactional()
	//@CacheResult(cacheName = "GetHierarchyTypeClassification")
	@Override
	public Uni<IClassification<?, ?>> getHierarchyType(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
	{
		return find(session,
				HierarchyTypeClassification.toString(),
				system, identityToken);
	}

	//@Transactional()
	//@CacheResult(cacheName = "GetNoClassification")
	@Override
	public Uni<IClassification<?, ?>> getNoClassification(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
	{
		return find(session,
				NoClassification.toString(),
				system, identityToken);
	}

	//@Transactional()
	//@CacheResult(cacheName = "IdentityTypeClassification")
	@Override
	public Uni<IClassification<?, ?>> getIdentityType(Mutiny.Session session, ISystems<?, ?> system, UUID... identityToken)
	{
		return find(session,
				Identity.name(),
				system, identityToken);
	}
}
