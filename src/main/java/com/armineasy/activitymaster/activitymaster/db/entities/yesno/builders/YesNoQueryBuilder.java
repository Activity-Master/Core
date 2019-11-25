package com.armineasy.activitymaster.activitymaster.db.entities.yesno.builders;

import com.armineasy.activitymaster.activitymaster.db.abstraction.builders.QueryBuilder;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNo;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNoSecurityToken;
import com.armineasy.activitymaster.activitymaster.db.entities.yesno.YesNo_;

import static com.entityassist.enumerations.Operand.*;

public class YesNoQueryBuilder
		extends QueryBuilder<YesNoQueryBuilder, YesNo, Long, YesNoSecurityToken>
{

	public YesNoQueryBuilder findYes()
	{
		findByName("Yes");
		return this;
	}

	public YesNoQueryBuilder findByName(String name)
	{
		where(YesNo_.yesNoDesc, Equals, name);
		return this;
	}

	public YesNoQueryBuilder findNo()
	{
		findByName("No");
		return this;
	}
}
