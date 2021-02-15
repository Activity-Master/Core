package com.guicedee.activitymaster.core.services.system;

import com.guicedee.activitymaster.core.services.dto.*;
import com.guicedee.activitymaster.core.services.enumtypes.IRulesTypeValue;

import java.util.List;
import java.util.UUID;

public interface IRulesService<J extends IRulesService<J>>
{
	String RulesSystemName = "Rules System";
	
	IRules<?> createRules(String rulesType, String name, String description, ISystems<?> system, UUID... identityToken);
	
	IRules<?> find(UUID identity);
	
	IRules<?> findRules(String name, IEnterprise<?> enterprise, UUID... identityToken);
	
	IRules<?> findRules(String productName, IClassification<?> classification, IEnterprise<?> enterprise, UUID... identityToken);
	
	IRulesType<?> createRulesType(IRulesTypeValue<?> rulesType, ISystems<?> system, UUID... identityToken);
	
	IRulesType<?> createRulesType(String rulesType, String description, ISystems<?> system, UUID... identityToken);
	
	IRulesType<?> findRulesTypes(IRulesTypeValue<?> rulesType, ISystems<?> system, UUID... identityToken);

	IRulesType<?> findRulesTypes(String rulesType, ISystems<?> system, UUID... identityToken);
	
	List<IRulesType<?>> findRulesTypes(String classifications, String value, ISystems<?> system, UUID... identityToken);
	
	List<IRules<?>> findByRulesTypes(IRulesType<?> rulesType, String classificationName, String value, ISystems<?> system, UUID... identityToken);
	
	List<IRulesType<?>> findRuleTypesByRules(IRules<?> rulesType, String classificationName, String value, ISystems<?> system, UUID... identityToken);
	
	List<IRelationshipValue<IRules<?>,IRulesType<?>,?>> findRuleTypeValuesByRules(IRules<?> rulesType, String classificationName, String value, ISystems<?> system, UUID... identityToken);
	
	List<IRules<?>> findRulesByProduct(IProduct<?> product, String classificationName, String value, ISystems<?> system, UUID... identityToken);
}
