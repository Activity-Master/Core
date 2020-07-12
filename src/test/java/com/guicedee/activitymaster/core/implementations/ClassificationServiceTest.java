package com.guicedee.activitymaster.core.implementations;

import com.guicedee.activitymaster.core.ActivityMasterConfiguration;
import com.guicedee.activitymaster.core.db.entities.classifications.Classification;
import com.guicedee.activitymaster.core.db.entities.enterprise.Enterprise;
import com.guicedee.activitymaster.core.services.classifications.classification.Classifications;
import com.guicedee.activitymaster.core.services.dto.IEnterprise;
import com.guicedee.guicedinjection.GuiceContext;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DefaultTestConfig.class)
@Log
class ClassificationServiceTest
{

	@Test
	void getHierarchyType()
	{
		GuiceContext.get(ActivityMasterConfiguration.class)
		            .setSecurityEnabled(false);
		IEnterprise<?> enterprise = new Enterprise().builder()
		                                            .getAll()
		                                            .get(0);

		ClassificationService service = GuiceContext.get(ClassificationService.class);
		Classification hierarchyType = (Classification) service.find(Classifications.HierarchyTypeClassification,
		                                                             enterprise);
		assertNotNull(hierarchyType);

	}

	@Test
	void getIdentityType()
	{
	}
}
