package com.guicedee.activitymaster.fsdm.db.entities.time.builders;

import com.guicedee.activitymaster.fsdm.db.abstraction.builders.DefaultTimeQueryBuilder;
import com.guicedee.activitymaster.fsdm.db.entities.time.Time;
import com.guicedee.activitymaster.fsdm.db.entities.time.TimePK;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.EntityManager;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class TimeQueryBuilder
		extends DefaultTimeQueryBuilder<TimeQueryBuilder, Time, TimePK>
{

}
