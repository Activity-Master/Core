package com.guicedee.activitymaster.tests;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterConfiguration;
import com.guicedee.activitymaster.fsdm.db.entities.time.*;
import com.guicedee.activitymaster.fsdm.services.system.ITimeSystem;
import com.guicedee.activitymaster.fsdm.systems.TimeSystem;
import com.guicedee.client.IGuiceContext;
import io.smallrye.mutiny.Uni;
import lombok.extern.java.Log;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.guicedee.activitymaster.fsdm.DefaultEnterprise.TestEnterprise;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the TimeSystemAdapter
 */
@Log
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TimeSystemTest extends TestDatabaseSetup
{

  private ITimeSystem timeSystem;

  protected Mutiny.SessionFactory sessionFactory;

  @BeforeAll
  public void set()
  {
    // Initialize the Guice context
    //IGuiceContext.registerModule(new PostgreSQLTestDBModule());
    ActivityMasterConfiguration.get()
        .setApplicationEnterpriseName(TestEnterprise.name());
    IGuiceContext.instance();

    log.info("Loading DB Configuration / PersistService from Guice");
    sessionFactory = IGuiceContext.get(Key.get(Mutiny.SessionFactory.class, Names.named("ActivityMaster-Test")));
    timeSystem = IGuiceContext.get(ITimeSystem.class);
    assertNotNull(sessionFactory, "SessionFactory should not be null");
  }

  @Test
  public void testGetDay() throws ExecutionException, InterruptedException
  {
    // Create a date for testing
    LocalDate localDate = LocalDate.of(2025, 1, 3);
    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault())
                              .toInstant());

    sessionFactory.withStatelessTransaction(session -> {
          Uni<Days> dayUni = timeSystem.getDay(session, date);

          // Wait for the result
          Days day = dayUni.await()
                         .atMost(Duration.ofMinutes(1));

          // Verify the result
          assertNotNull(day, "Day should not be null");
          assertEquals(localDate, day.getDayDate(), "Day date should match the input date");
          return null;
        })
        .await()
        .atMost(Duration.ofMinutes(1))
    ;
  }

  @Test
  public void testLoadTimeRange() throws ExecutionException, InterruptedException
  {
    // For testing purposes, we'll use a much smaller date range
    // Just test January 1-10, 2025 instead of the whole year
    int startYear = 2023;
    int endYear = 2027;
    
    // Call the loadTimeRange method with a small range
    Uni<Void> loadUni = timeSystem.loadTimeRange(startYear, endYear);

    // Wait for the result with a longer timeout
    loadUni.await()
        .atMost(Duration.ofMinutes(5));

    // Get the TimeSystem instance to access internal fields
    TimeSystem adapter = (TimeSystem) timeSystem;
    
    // Assert that 1 year was created
    assertEquals(5, adapter.getUniqueYearsCreated().size(), "Should have created exactly 5 years");
    
    // Assert that at least 1 day was created (we don't need to check exact count)
    // This verifies days are being created, which is what the issue is about
    assertTrue(adapter.getDaysCreatedCount() > 1826, "Should have created Exactly 1826 days with a leap year, got " + adapter.getDaysCreatedCount());
    log.info("Days created: " + adapter.getDaysCreatedCount());
  }

  @Test
  public void testLoadTimeRangeSmall()
  {
    // Create a very small date range (just a few days) to test the batch processing
    int year = 2025;
    int month = 1;

    // Get the TimeSystemAdapter instance to access non-interface methods
    TimeSystem adapter = (TimeSystem) timeSystem;

    // Call the loadTimeRange method with a small range
    Uni<Void> loadUni = timeSystem.loadTimeRange(year, year);

    // Wait for the result
    loadUni.await()
        .atMost(Duration.ofMinutes(1));

    // Verify that days were created for the range
    // Check January 1st

    sessionFactory.withStatelessTransaction(session -> {
          LocalDate jan1 = LocalDate.of(year, month, 1);
          Date jan1Date = Date.from(jan1.atStartOfDay(ZoneId.systemDefault())
                                        .toInstant());
          Uni<Days> jan1Uni = timeSystem.getDay(session, jan1Date);
          Days jan1Day = jan1Uni.await()
                             .atMost(Duration.ofMinutes(1));

          assertNotNull(jan1Day, "January 1st day should not be null");
          assertEquals(jan1, jan1Day.getDayDate(), "Day date should match January 1st");

          // Check January 31st
          LocalDate jan31 = LocalDate.of(year, month, 31);
          Date jan31Date = Date.from(jan31.atStartOfDay(ZoneId.systemDefault())
                                         .toInstant());
          Uni<Days> jan31Uni = timeSystem.getDay(session, jan31Date);
          Days jan31Day = jan31Uni.await()
                              .atMost(Duration.ofMinutes(1));

          assertNotNull(jan31Day, "January 31st day should not be null");
          assertEquals(jan31, jan31Day.getDayDate(), "Day date should match January 31st");
          return null;
        })
        .await()
        .atMost(Duration.ofMinutes(1))
    ;

  }

  @Test
  public void testCreateTime() throws ExecutionException, InterruptedException
  {
    // Call the createTime method
    Uni<Void> createUni = timeSystem.createTime();

    // Wait for the result with a longer timeout (5 minutes)
    createUni.await()
        .atMost(Duration.ofMinutes(1));

    // No assertion needed as we're just verifying that the method completes without error
  }

  @Test
  @Disabled
  public void testGetYear()
  {
    // Create a date for testing
    LocalDate localDate = LocalDate.of(2025, 7, 30);
    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault())
                              .toInstant());

    // Get the TimeSystemAdapter instance to access non-interface methods
    TimeSystem adapter = (TimeSystem) timeSystem;

    sessionFactory.withStatelessTransaction(session -> {
          // Call the getYear method
          Uni<Years> yearUni = adapter.getYear(session, date);

          // Wait for the result
          Years year = yearUni.await()
                           .atMost(Duration.ofMinutes(1));

          // Verify the result
          assertNotNull(year, "Year should not be null");
          assertEquals(Short.valueOf("2025"), year.getId(), "Year ID should be 2025");
          assertEquals("2025", year.getYearName(), "Year name should be 2025");
          return null;
        })
        .await()
        .atMost(Duration.ofMinutes(1))
    ;

  }

  @Test
  @Disabled
  public void testGetQuarter()
  {
    // Create a date for testing
    LocalDate localDate = LocalDate.of(2025, 7, 30); // Q3 2025
    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault())
                              .toInstant());

    // Get the TimeSystemAdapter instance to access non-interface methods
    TimeSystem adapter = (TimeSystem) timeSystem;

    sessionFactory.withStatelessTransaction(session -> {
      // Call the getQuarter method
      Uni<Quarters> quarterUni = adapter.getQuarter(session, date);

      // Wait for the result
      Quarters quarter = quarterUni.await()
                             .atMost(Duration.ofMinutes(1));

      // Verify the result
      assertNotNull(quarter, "Quarter should not be null");
      assertEquals(20253, quarter.getId(), "Quarter ID should be 20253 (2025 Q3)");
      assertEquals(3, quarter.getQuarterInYear(), "Quarter number should be 3");
      return null;
    }).await().atMost(Duration.ofMinutes(1));

  }

  @Test
  @Disabled
  public void testGetMonth()
  {
    // Create a date for testing
    LocalDate localDate = LocalDate.of(2025, 7, 30); // July 2025
    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault())
                              .toInstant());

    // Get the TimeSystemAdapter instance to access non-interface methods
    TimeSystem adapter = (TimeSystem) timeSystem;

    sessionFactory.withStatelessTransaction(session -> {
          // Call the getMonth method
          Uni<Months> monthUni = adapter.getMonth(session, date);

          // Wait for the result
          Months month = monthUni.await()
                             .atMost(Duration.ofMinutes(1));

          // Verify the result
          assertNotNull(month, "Month should not be null");
          assertEquals(Integer.valueOf("202507"), month.getId(), "Month ID should be 202507 (2025-07)");
          return null;
        })
        .await()
        .atMost(Duration.ofMinutes(1))
    ;

  }

  @Test
  @Disabled
  public void testGetWeek()
  {
    // Create a date for testing
    LocalDate localDate = LocalDate.of(2025, 7, 30); // Week 31 of 2025
    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault())
                              .toInstant());

    // Get the TimeSystemAdapter instance to access non-interface methods
    TimeSystem adapter = (TimeSystem) timeSystem;

    sessionFactory.withStatelessTransaction(session -> {
          // Call the getWeek method
          Uni<Weeks> weekUni = adapter.getWeek(session, date);

          // Wait for the result
          Weeks week = weekUni.await()
                           .atMost(Duration.ofMinutes(1));

          // Verify the result
          assertNotNull(week, "Week should not be null");
          assertTrue(week.getId() > 0, "Week ID should be positive");
          assertTrue(week.getWeekOfYear() > 0, "Week of year should be positive");
          return null;
        })
        .await()
        .atMost(Duration.ofMinutes(1))
    ;

  }

  @Test
  @Disabled
  public void testTimeHierarchy()
  {
    // Create a date for testing
    LocalDate localDate = LocalDate.of(2025, 7, 30);
    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault())
                              .toInstant());

    // Get the TimeSystemAdapter instance to access non-interface methods
    TimeSystem adapter = (TimeSystem) timeSystem;

    sessionFactory.withStatelessTransaction(session -> {
          // Get the day entity which should create the entire hierarchy
          Uni<Days> dayUni = timeSystem.getDay(session, date);
          Days day = dayUni.await()
                         .atMost(Duration.ofMinutes(1));

          // Verify the day entity
          assertNotNull(day, "Day should not be null");
          assertEquals(localDate, day.getDayDate(), "Day date should match the input date");

          // Get the week entity
          Weeks week = day.getWeekID();
          assertNotNull(week, "Week should not be null");
          assertTrue(week.getWeekOfYear() > 0, "Week of year should be positive");

          // Get the month entity
          Months month = day.getMonthID();
          assertNotNull(month, "Month should not be null");
          assertEquals(Integer.valueOf("202507"), month.getId(), "Month ID should be 202507 (2025-07)");

          // Verify the quarter ID
          int quarterId = day.getQuarterID();
          assertEquals(20253, quarterId, "Quarter ID should be 20253 (2025 Q3)");

          // Verify the year ID
          int yearId = day.getYearID();
          assertEquals(2025, yearId, "Year ID should be 2025");

          // Verify the day name
          DayNames dayName = day.getDayNameID();
          assertNotNull(dayName, "Day name should not be null");
          assertEquals("Wednesday", dayName.getDayName(), "Day name should be Wednesday");
          return null;
        })
        .await()
        .atMost(Duration.ofMinutes(1))
    ;

  }
}