package com.guicedee.activitymaster.fsdm.services.system;

import com.guicedee.activitymaster.fsdm.db.entities.time.Days;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.Date;

public interface ITimeSystem
{
	Uni<Void> loadTimeRange(int startYear, int endYear);

	Uni<Days> getDay(Mutiny.Session session, Date date);

	/**
	 * Gets or creates a day for the given date
	 *
	 * @param session
	 * @param date    The date to get or create a day for
	 * @return The day entity
	 */
	Uni<Days> getDay(Mutiny.StatelessSession session, Date date);

	Uni<Void> createTime();
}
