package com.armineasy.activitymaster.activitymaster.db.entities.classifications.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationDataConcept;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.ClassificationSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.classifications.Classification_;
import com.armineasy.activitymaster.activitymaster.services.enumtypes.IClassificationValue;
import com.armineasy.activitymaster.activitymaster.services.dto.IEnterprise;
import com.jwebmp.entityassist.enumerations.Operand;

import java.util.Collection;

import static com.jwebmp.entityassist.enumerations.Operand.*;

public class ClassificationQueryBuilder
		extends QueryBuilder<ClassificationQueryBuilder, Classification, Long, ClassificationSecurityToken>
{
	public ClassificationQueryBuilder findByNameAndConcept(String name, ClassificationDataConcept concept, IEnterprise<?> enterprise)
	{
		findByName(name);
		where(Classification_.concept, Operand.Equals, concept);
		return this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public ClassificationQueryBuilder findByName(String name)
	{
		where(Classification_.name, Equals, name);
		return  this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public ClassificationQueryBuilder findByName(IClassificationValue<?> name)
	{
		where(Classification_.name, Equals, name.classificationDescription());
		return  this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public ClassificationQueryBuilder findByDescription(String name)
	{
		where(Classification_.description, Equals, name);
		return  this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public ClassificationQueryBuilder findByDescriptionLike(String name)
	{
		where(Classification_.description, Like, "%" + name + "%");
		return  this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public ClassificationQueryBuilder findByName(String... name)
	{
		where(Classification_.name, InList, name);
		return  this;
	}

	@SuppressWarnings("unchecked")
	@javax.validation.constraints.NotNull
	public ClassificationQueryBuilder findByName(Collection<String> name)
	{
		where(Classification_.name, InList, name);
		return  this;
	}

}
