package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.fsdm.client.services.systems.IProgressable;
import com.guicedee.activitymaster.fsdm.db.entities.time.*;
import com.guicedee.activitymaster.fsdm.db.timelord.EnglishNumberToWords;
import com.guicedee.activitymaster.fsdm.services.system.ITimeSystem;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.NoResultException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.entityassist.enumerations.Operand.Equals;
import static com.guicedee.activitymaster.fsdm.client.services.ITimeService.TimeSystemName;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.DateTimeFormats.*;

/**
 * Fully reactive implementation of the TimeSystem interface
 * <p>
 * This adapter provides a reactive interface for time-related operations using Mutiny.
 * It implements all the functionality of the original TimeSystem but with reactive patterns:
 * <p>
 * 1. Year-to-date (YTD), quarter-to-date (QTD), month-to-date (MTD), and fiscal period relationships
 * 2. Time entity creation (hours, minutes, half-hours, day parts)
 * 3. Day, week, month, quarter, and year entity management
 * <p>
 * All operations use reactive patterns with Uni return types and proper error handling.
 */
@Log4j2
public class TimeSystem extends ActivityMasterDefaultSystem<TimeSystem>
    implements IActivityMasterSystem<TimeSystem>, ITimeSystem, IProgressable
{
  private final AtomicInteger totalTasksCount = new AtomicInteger(0);

  // Counters for tracking created entities
  private final AtomicInteger daysCreatedCount = new AtomicInteger(0);
  private final Set<Integer> uniqueYearsCreated = new HashSet<>();

  // Getter methods for testing
  public int getDaysCreatedCount()
  {
    return daysCreatedCount.get();
  }

  public Set<Integer> getUniqueYearsCreated()
  {
    return uniqueYearsCreated;
  }

  @Inject
  private Mutiny.SessionFactory sessionFactory;

  @Inject
  private ISystemsService<?> systemsService;


  @Override
  public Uni<ISystems<?, ?>> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
  {
    log.info("🚀 Registering Time System for enterprise: '{}'", enterprise.getName());
    log.debug("📋 Creating Time System with session: {}", session.hashCode());

    return systemsService
               .create(session, enterprise, getSystemName(), getSystemDescription())
               .chain(system -> {
                 log.debug("✅ Created Time System: '{}' with session: {}", system.getName(), session.hashCode());

                 // Chain the registerNewSystem call properly
                 return getSystem(session, enterprise)
                            .chain(sys -> systemsService.registerNewSystem(session, enterprise, sys))
                            .onItem()
                            .invoke(() -> {
                              log.debug("✅ Registered system: {}", getSystemName());
                              log.info("🎉 Successfully registered Time System for enterprise: '{}'", enterprise.getName());
                            })
                            .onFailure()
                            .invoke(error -> log.error("❌ Error registering system: {}", error.getMessage(), error))
                            .chain(() -> Uni.createFrom()
                                             .item(system)); // Chain back to return the original system
               })
               .onFailure()
               .invoke(error -> log.error("❌ Failed to create Time System: '{}' with session {}: {}",
                   getSystemName(), session.hashCode(), error.getMessage(), error))
               .map(result -> result);
  }


  @Override
  public Uni<Void> createDefaults(Mutiny.Session session, com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise<?, ?> enterprise)
  {
    log.info("Creating TimeSystem defaults");
    return Uni.createFrom()
               .voidItem();
  }

  @Override
  public String getSystemName()
  {
    return TimeSystemName;
  }

  @Override
  public String getSystemDescription()
  {
    return "The system for handling the time dimension";
  }

  @Override
  public int totalTasks()
  {
    return totalTasksCount.get();
  }

  @Override
  public Integer sortOrder()
  {
    return Integer.MIN_VALUE + 6;
  }

  @Override
  public Uni<Void> loadTimeRange(int startYear, int endYear)
  {
    log.info("🚀 Starting reactive loadTimeRange from {} to {}", startYear, endYear);

    // Reset counters for tracking created entities
    daysCreatedCount.set(0);
    uniqueYearsCreated.clear();

    return Uni.createFrom()
               .item(() -> {
                 // Calculate total days for progress tracking
                 LocalDate startDate = LocalDate.of(startYear, 1, 1);
                 LocalDate endDate = LocalDate.of(endYear, 12, 31);
                 long totalDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
                 totalTasksCount.set((int) totalDays);

                 log.info("📊 Calculated {} total days to process", totalDays);
                 return totalDays;
               })
               .chain(totalDays -> {
                 // Process each day in the range
                 GregorianCalendar startYearGC = new GregorianCalendar();
                 startYearGC.set(Calendar.YEAR, startYear);
                 startYearGC.set(Calendar.MONTH, 0);
                 startYearGC.set(Calendar.DATE, 1);

                 GregorianCalendar endGC = new GregorianCalendar();
                 endGC.set(Calendar.YEAR, endYear);
                 endGC.set(Calendar.MONTH, 11);
                 endGC.set(Calendar.DATE, 31);

                 log.debug("📋 Preparing day operations for processing in sequence");

                 // Process all days in the range
                 int maxDaysToProcess = Integer.MAX_VALUE; // No limit
                 if (totalDays > 1000)
                 {
                   log.info("⚠️ Processing a large number of days: {}", totalDays);
                 }

                 // Create a chain of operations instead of a list for parallel processing
                 Uni<Void> chain = Uni.createFrom()
                                       .voidItem();
                 int processedCount = 0;

                 // Process days with error handling for each day
                 while (startYearGC.getTime()
                            .getTime() <= endGC.getTime()
                                              .getTime() &&
                            (processedCount < maxDaysToProcess || totalDays <= maxDaysToProcess))
                 {
                   final Date currentDate = startYearGC.getTime();
                   final int currentCount = processedCount;

                   // Get the year for tracking
                   final int currentYear = startYearGC.get(Calendar.YEAR);
                   uniqueYearsCreated.add(currentYear);

                   chain = chain.chain(() -> {
                     log.debug("📋 Processing day: {}", currentDate);
                     return sessionFactory.withStatelessTransaction(session -> {
                       return getDay(session, currentDate)
                                  .onItem()
                                  .invoke(day -> {
                                    // Increment days created counter
                                    daysCreatedCount.incrementAndGet();

                                    if ((currentCount + 1) % 10 == 0 || currentCount + 1 == totalDays || currentCount + 1 == maxDaysToProcess)
                                    {
                                      log.info("📊 Progress: {}/{} days processed",
                                          (currentCount + 1),
                                          (totalDays > maxDaysToProcess ? maxDaysToProcess : totalDays));
                                      logProgress("Time Loading", String.format("Processed %d days", (currentCount + 1)));
                                    }
                                  })
                                  .onFailure()
                                  .recoverWithItem(error -> {
                                    log.fatal("[X] Error processing day {}: {}", currentDate, error.getMessage());
                                    throw new RuntimeException(error);
                                  })
                                  .replaceWithVoid();
                     });
                   });

                   startYearGC.add(Calendar.DATE, 1);
                   processedCount++;

                   // Break if we've reached our testing limit
                   if (processedCount >= maxDaysToProcess && totalDays > maxDaysToProcess)
                   {
                     break;
                   }
                 }

                 return chain;
               })
               .onItem()
               .invoke(() -> {
                 // Log the total number of days and years created
                 log.info("📊 Total days created: {}", daysCreatedCount.get());
                 log.info("📊 Total years created: {}", uniqueYearsCreated.size());
                 log.info("🎉 Time range loading completed");
               })
               .onFailure()
               .invoke(error -> {
                 log.error("❌ Time range loading failed: {}", error.getMessage(), error);
               });
  }

  private Uni<Void> processDaysInBatches(List<Uni<Days>> dayOperations, int batchSize)
  {
    log.debug("📋 Processing {} days in batches of {}", dayOperations.size(), batchSize);

    // This method is kept for compatibility but now processes sequentially
    Uni<Void> chain = Uni.createFrom()
                          .voidItem();

    for (int i = 0; i < dayOperations.size(); i += batchSize)
    {
      final int batchStart = i;
      final int batchEnd = Math.min(i + batchSize, dayOperations.size());
      final List<Uni<Days>> batch = dayOperations.subList(batchStart, batchEnd);

      chain = chain.chain(() -> {
        log.debug("📋 Processing batch {}-{}", batchStart, batchEnd);

        // Process each day in the batch sequentially
        Uni<Void> batchChain = Uni.createFrom()
                                   .voidItem();

        for (Uni<Days> dayOp : batch)
        {
          batchChain = batchChain.chain(() -> dayOp.replaceWithVoid());
        }

        return batchChain
                   .onItem()
                   .invoke(() -> {
                     log.info("✅ Batch {}-{} completed ({} days)", batchStart, batchEnd, batch.size());
                     logProgress("Time Loading", String.format("Processed %d days", batchEnd));
                   });
      });
    }

    return chain;
  }

  /**
   * Gets a Year entity for the given date, creating it if it doesn't exist
   *
   * @param session
   * @param date    The date to get the year for
   * @return A Uni containing the Year entity
   */
  public Uni<Years> getYear(Mutiny.StatelessSession session, Date date)
  {
    log.debug("🔍 Getting year for date: {}", date);

    return getYearFromDatabase(session, date)
               .onItem()
               .invoke(year -> {
                 log.debug("✅ Retrieved year for date: {}", date);
               })
               .onFailure()
               .recoverWithUni(error -> {
                 log.debug("📋 Year not found for date: {}, creating new year", date);
                 return loadTimeRange(date.getYear(),date.getYear())
                            .chain(a->{
                              return getYearFromDatabase(session, date);
                            });
               });
  }

  /**
   * Retrieves a Year entity from the database
   *
   * @param session
   * @param date    The date to get the year for
   * @return A Uni containing the Year entity
   */
  private Uni<Years> getYearFromDatabase(Mutiny.StatelessSession session, Date date)
  {
    Short yearId = Short.parseShort(YearIDFormat.getSimpleDateFormat()
                                        .format(date));
    return new Years().builder(session)
               .find(yearId)
               .get()
               .onFailure(NoResultException.class)
               .recoverWithUni(createYear(date));
  }

  /**
   * Creates a new Year entity for the given date
   *
   * @param date The date to create the year for
   * @return A Uni containing the created Year entity
   */
  private Uni<Years> createYear(Date date)
  {
    return sessionFactory.withTransaction(session -> {
      Short yearId = Short.parseShort(YearIDFormat.getSimpleDateFormat()
                                          .format(date));
      log.debug("🚀 Creating year with ID: {}", yearId);

      // Create a new year entity
      Years year = new Years();
      year.setId(yearId);

      // Set year properties
      GregorianCalendar gc = new GregorianCalendar();
      gc.setTime(date);
      gc.set(Calendar.MONTH, 1);

      // Set leap year flag
      year.setLeapYearFlag((short) (gc.getActualMaximum(Calendar.DAY_OF_MONTH) == 29 ? 1 : 0));

      // Get last year ID
      return getLastYearID(date)
                 .chain(lastYearId -> {
                   year.setLastYearID((short) lastYearId.intValue());

                   // Set year names
                   year.setYearName(YearIDFormat.getSimpleDateFormat()
                                        .format(date));
                   year.setyYName(YearShortFormat.getSimpleDateFormat()
                                      .format(date));
                   year.setyYYName(YearYYYFormat.getSimpleDateFormat()
                                       .format(date));
                   year.setYearFullName(EnglishNumberToWords.convert(year.getId()));
                   year.setCentury(Short.parseShort(YearFullFormat.getSimpleDateFormat()
                                                        .format(date)
                                                        .substring(0, 2)));

                   // Persist the year
                   return session.persist(year)
                              .chain(() -> Uni.createFrom()
                                               .item(year));
                 });
    });
  }

  /**
   * Gets the ID of the previous year
   *
   * @param date The date to get the previous year for
   * @return A Uni containing the previous year's ID
   */
  private Uni<Integer> getLastYearID(Date date)
  {
    return Uni.createFrom()
               .item(() -> {
                 GregorianCalendar gc = new GregorianCalendar();
                 gc.setTime(date);
                 gc.add(Calendar.YEAR, -1);
                 return Integer.parseInt(YearIDFormat.getSimpleDateFormat()
                                             .format(gc.getTime()));
               });
  }

  /**
   * Gets a Quarter entity for the given date, creating it if it doesn't exist
   *
   * @param session
   * @param date    The date to get the quarter for
   * @return A Uni containing the Quarter entity
   */
  public Uni<Quarters> getQuarter(Mutiny.StatelessSession session, Date date)
  {
    log.debug("🔍 Getting quarter for date: {}", date);

    return getQuarterFromDatabase(session, date)
               .onItem()
               .invoke(quarter -> {
                 log.debug("✅ Retrieved quarter for date: {}", date);
               })
               .onFailure()
               .recoverWithUni(error -> {
                 log.debug("📋 Quarter not found for date: {}, creating new quarter", date);
                 return createQuarter(session, date);
               });
  }

  /**
   * Retrieves a Quarter entity from the database
   *
   * @param session
   * @param date    The date to get the quarter for
   * @return A Uni containing the Quarter entity
   */
  private Uni<Quarters> getQuarterFromDatabase(Mutiny.StatelessSession session, Date date)
  {
    int quarterId = getQuarterID(date);
    return new Quarters().builder(session)
               .find(quarterId)
               .get()
               .onItem()
               .transform(optional -> {
                 if (optional != null)
                 {
                   log.debug("✅ Found quarter with ID: {}", quarterId);
                   return (Quarters) optional;
                 }
                 else
                 {
                   log.debug("📋 Quarter not found with ID: {}", quarterId);
                   throw new NoSuchElementException("Quarter not found for date: " + date);
                 }
               });
  }

  /**
   * Creates a new Quarter entity for the given date
   *
   * @param session
   * @param date    The date to create the quarter for
   * @return A Uni containing the created Quarter entity
   */
  private Uni<Quarters> createQuarter(Mutiny.StatelessSession session, Date date)
  {
    int quarterId = getQuarterID(date);
    log.debug("🚀 Creating quarter with ID: {}", quarterId);

    // Create a new quarter entity
    Quarters quarter = new Quarters();
    quarter.setId(quarterId);

    // Get related IDs sequentially using chains instead of parallel processing
    log.debug("📋 Getting last quarter ID for date: {}", date);
    return getLastQuarterID(date)
               .chain(lastQuarterId -> {
                 log.debug("✅ Got last quarter ID: {}", lastQuarterId);
                 quarter.setLastQuarterID(lastQuarterId.shortValue());

                 log.debug("📋 Getting last year quarter ID for date: {}", date);
                 return getLastYearQuarterID(date)
                            .chain(lastYearQuarterId -> {
                              log.debug("✅ Got last year quarter ID: {}", lastYearQuarterId);
                              quarter.setLastYearID(lastYearQuarterId.shortValue());

                              log.debug("📋 Getting year for date: {}", date);
                              return getYear(session, date)
                                         .chain(year -> {
                                           log.debug("✅ Got year: {}", year.getId());
                                           quarter.setYearID(year);

                                           // Get quarter number
                                           log.debug("📋 Getting quarter number for date: {}", date);
                                           return getQuarterNumber(date)
                                                      .chain(quarterNumber -> {
                                                        log.debug("✅ Got quarter number: {}", quarterNumber);

                                                        // Set quarter descriptions
                                                        quarter.setQuarterInYear(quarterNumber);
                                                        quarter.setQuarterDescription("Q" + quarterNumber + " " +
                                                                                          YearIDFormat.getSimpleDateFormat()
                                                                                              .format(date));
                                                        quarter.setQuarterGraphDescription("Q" + quarterNumber + " - " +
                                                                                               YearIDFormat.getSimpleDateFormat()
                                                                                                   .format(date));
                                                        quarter.setQuarterGridDescription("Quarter " + quarterNumber + " - " +
                                                                                              YearIDFormat.getSimpleDateFormat()
                                                                                                  .format(date));
                                                        quarter.setQuarterQQMMDescription("Q" +
                                                                                              String.format("%02d", quarterNumber) + " " +
                                                                                              MonthNumberFormat.getSimpleDateFormat()
                                                                                                  .format(date));
                                                        quarter.setQuarterSmallDescription("Quart " + quarterNumber + " " +
                                                                                               YearIDFormat.getSimpleDateFormat()
                                                                                                   .format(date));
                                                        quarter.setQuarterYYMMDescription("'" +
                                                                                              YearShortFormat.getSimpleDateFormat()
                                                                                                  .format(date) + " Q" +
                                                                                              String.format("%02d", quarterNumber));
                                                        quarter.setQuarterYearDescription(quarter.getQuarterDescription());

                                                        // Persist the quarter
                                                        log.debug("💾 Persisting quarter: {}", quarterId);
                                                        return session.insert(quarter)
                                                                   .replaceWith(quarter);
                                                      });
                                         });
                            });
               });
  }

  /**
   * Gets the quarter ID for the given date
   *
   * @param date The date to get the quarter ID for
   * @return The quarter ID
   */
  private int getQuarterID(Date date)
  {
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);
    return Integer.parseInt(YearIDFormat.getSimpleDateFormat()
                                .format(date) +
                                getQuarterNumber(gc));
  }

  /**
   * Gets the quarter number (1-4) for the given calendar
   *
   * @param gc The calendar to get the quarter number for
   * @return The quarter number (1-4)
   */
  private int getQuarterNumber(GregorianCalendar gc)
  {
    int month = gc.get(Calendar.MONTH);
    if (month < 3)
    {
      return 1;
    }
    else if (month < 6)
    {
      return 2;
    }
    else if (month < 9)
    {
      return 3;
    }
    else
    {
      return 4;
    }
  }

  /**
   * Gets the quarter number (1-4) for the given date
   *
   * @param date The date to get the quarter number for
   * @return A Uni containing the quarter number (1-4)
   */
  private Uni<Integer> getQuarterNumber(Date date)
  {
    return Uni.createFrom()
               .item(() -> {
                 GregorianCalendar gc = new GregorianCalendar();
                 gc.setTime(date);
                 return getQuarterNumber(gc);
               });
  }

  /**
   * Gets the ID of the previous quarter
   *
   * @param date The date to get the previous quarter for
   * @return A Uni containing the previous quarter's ID
   */
  private Uni<Integer> getLastQuarterID(Date date)
  {
    return Uni.createFrom()
               .item(() -> {
                 GregorianCalendar gc = new GregorianCalendar();
                 gc.setTime(date);
                 gc.add(Calendar.MONTH, -3);
                 return getQuarterID(gc.getTime());
               });
  }

  /**
   * Gets the ID of the same quarter in the previous year
   *
   * @param date The date to get the previous year's quarter for
   * @return A Uni containing the previous year's quarter ID
   */
  private Uni<Integer> getLastYearQuarterID(Date date)
  {
    return Uni.createFrom()
               .item(() -> {
                 GregorianCalendar gc = new GregorianCalendar();
                 gc.setTime(date);
                 gc.add(Calendar.YEAR, -1);
                 return getQuarterID(gc.getTime());
               });
  }

  /**
   * Gets a Month entity for the given date, creating it if it doesn't exist
   *
   * @param session
   * @param date    The date to get the month for
   * @return A Uni containing the Month entity
   */
  public Uni<Months> getMonth(Mutiny.StatelessSession session, Date date)
  {
    log.debug("🔍 Getting month for date: {}", date);

    return getMonthFromDatabase(session, date)
               .onItem()
               .invoke(month -> {
                 log.debug("✅ Retrieved month for date: {}", date);
               })
               .onFailure()
               .recoverWithUni(error -> {
                 log.debug("📋 Month not found for date: {}, creating new month", date);
                 return createMonth(session, date);
               });
  }

  /**
   * Retrieves a Month entity from the database
   *
   * @param session
   * @param date    The date to get the month for
   * @return A Uni containing the Month entity
   */
  private Uni<Months> getMonthFromDatabase(Mutiny.StatelessSession session, Date date)
  {
    int monthId = Integer.parseInt(MonthIDFormat.getSimpleDateFormat()
                                       .format(date));
    return new Months().builder(session)
               .find(monthId)
               .get()
               .onItem()
               .transform(optional -> {
                 if (optional != null)
                 {
                   log.debug("✅ Found month with ID: {}", monthId);
                   return (Months) optional;
                 }
                 else
                 {
                   log.debug("📋 Month not found with ID: {}", monthId);
                   throw new jakarta.persistence.NoResultException("Month not found for date: " + date);
                 }
               });
  }

  /**
   * Creates a new Month entity for the given date
   *
   * @param session
   * @param date    The date to create the month for
   * @return A Uni containing the created Month entity
   */
  private Uni<Months> createMonth(Mutiny.StatelessSession session, Date date)
  {
    int monthId = Integer.parseInt(MonthIDFormat.getSimpleDateFormat()
                                       .format(date));
    log.debug("🚀 Creating month with ID: {}", monthId);

    // Create a new month entity
    Months month = new Months();
    month.setId(monthId);

    // Get related entities and IDs sequentially using chains
    log.debug("📋 Getting last month ID for date: {}", date);
    return getLastMonthID(date)
               .chain(lastMonthId -> {
                 log.debug("✅ Got last month ID: {}", lastMonthId);
                 month.setLastMonthID(lastMonthId);

                 log.debug("📋 Getting last year ID for date: {}", date);
                 return getLastYearID(date)
                            .chain(lastYearId -> {
                              log.debug("✅ Got last year ID: {}", lastYearId);
                              month.setLastYearID(lastYearId);

                              log.debug("📋 Getting last quarter ID for date: {}", date);
                              return getLastQuarterID(date)
                                         .chain(lastQuarterId -> {
                                           log.debug("✅ Got last quarter ID: {}", lastQuarterId);
                                           month.setLastQuarterID(lastQuarterId);

                                           log.debug("📋 Getting quarter for date: {}", date);
                                           return getQuarter(session, date)
                                                      .chain(quarter -> {
                                                        log.debug("✅ Got quarter: {}", quarter.getId());
                                                        month.setQuarterID(quarter);

                                                        log.debug("📋 Getting month of year for date: {}", date);
                                                        return getMonthOfYear(session, date)
                                                                   .chain(monthOfYear -> {
                                                                     log.debug("✅ Got month of year: {}", monthOfYear.getId());
                                                                     month.setMonthOfYearID(monthOfYear);

                                                                     // Set month descriptions
                                                                     GregorianCalendar gc = new GregorianCalendar();
                                                                     gc.setTime(date);
                                                                     int yearId = Integer.parseInt(YearIDFormat.getSimpleDateFormat()
                                                                                                       .format(date));
                                                                     month.setYearID(yearId);

                                                                     // Set month descriptions using the correct method names
                                                                     month.setMonthDescription(MonthDescriptionFormat.getSimpleDateFormat()
                                                                                                   .format(date));
                                                                     month.setMonthShortDescription(MonthShortDescriptionFormat.getSimpleDateFormat()
                                                                                                        .format(date));
                                                                     month.setMonthYYDescription(MonthMMYYDescriptionFormat.getSimpleDateFormat()
                                                                                                     .format(date));
                                                                     month.setMonthMMMYYDescription(MonthMMMMYYDescriptionFormat.getSimpleDateFormat()
                                                                                                        .format(date));
                                                                     month.setMonthMMYYYYDescription(MonthMMYYYYDescriptionFormat.getSimpleDateFormat()
                                                                                                         .format(date));
                                                                     month.setMonthNameYYYYDescription(MonthMonthNameYYYYDescriptionFormat.getSimpleDateFormat()
                                                                                                           .format(date));

                                                                     // Set month day duration
                                                                     month.setMonthDayDuration((short) gc.getActualMaximum(Calendar.DAY_OF_MONTH));

                                                                     // Persist the month
                                                                     log.debug("💾 Persisting month: {}", monthId);
                                                                     return session.insert(month)
                                                                                .replaceWith(month);
                                                                   });
                                                      });
                                         });
                            });
               });
  }

  /**
   * Gets the MonthOfYear entity for the given date
   *
   * @param session
   * @param date    The date to get the month of year for
   * @return A Uni containing the MonthOfYear entity
   */
  private Uni<MonthOfYear> getMonthOfYear(Mutiny.StatelessSession session, Date date)
  {
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);
    int monthNumber = gc.get(Calendar.MONTH) + 1;

    return getMonthOfYear(session, monthNumber);
  }

  /**
   * Gets the MonthOfYear entity for the given month number (1-12)
   *
   * @param session
   * @param monthNumber The month number (1-12)
   * @return A Uni containing the MonthOfYear entity
   */
  private Uni<MonthOfYear> getMonthOfYear(Mutiny.StatelessSession session, Integer monthNumber)
  {
    return new MonthOfYear().builder(session)
               .find(monthNumber)
               .get()
               .onItem()
               .transform(optional -> {
                 if (optional != null)
                 {
                   log.debug("✅ Found month of year with ID: {}", monthNumber);
                   return (MonthOfYear) optional;
                 }
                 else
                 {
                   log.warn("⚠️ Month of year not found with ID: {}", monthNumber);
                   throw new NoSuchElementException("Month of year not found for number: " + monthNumber);
                 }
               });
  }

  /**
   * Gets the ID of the previous month
   *
   * @param date The date to get the previous month for
   * @return A Uni containing the previous month's ID
   */
  private Uni<Integer> getLastMonthID(Date date)
  {
    return Uni.createFrom()
               .item(() -> {
                 GregorianCalendar gc = new GregorianCalendar();
                 gc.setTime(date);
                 gc.add(Calendar.MONTH, -1);
                 return Integer.parseInt(MonthIDFormat.getSimpleDateFormat()
                                             .format(gc.getTime()));
               });
  }

  /**
   * Gets a Week entity for the given date, creating it if it doesn't exist
   *
   * @param session
   * @param date    The date to get the week for
   * @return A Uni containing the Week entity
   */
  public Uni<Weeks> getWeek(Mutiny.StatelessSession session, Date date)
  {
    log.debug("🔍 Getting week for date: {}", date);

    return getWeekFromDatabase(session, date)
               .onItem()
               .invoke(week -> {
                 log.debug("✅ Retrieved week for date: {}", date);
               })
               .onFailure()
               .recoverWithUni(error -> {
                 log.debug("📋 Week not found for date: {}, creating new week", date);
                 return createWeek(session, date);
               });
  }

  /**
   * Retrieves a Week entity from the database
   *
   * @param session
   * @param date    The date to get the week for
   * @return A Uni containing the Week entity
   */
  private Uni<Weeks> getWeekFromDatabase(Mutiny.StatelessSession session, Date date)
  {
    int weekId = getWeekID(date);
    return new Weeks().builder(session)
               .find(weekId)
               .get()
               .onItem()
               .transform(optional -> {
                 if (optional != null)
                 {
                   log.debug("✅ Found week with ID: {}", weekId);
                   return (Weeks) optional;
                 }
                 else
                 {
                   log.debug("📋 Week not found with ID: {}", weekId);
                   throw new jakarta.persistence.NoResultException("Week not found for date: " + date);
                 }
               });
  }

  /**
   * Gets the week ID for the given date
   *
   * @param date The date to get the week ID for
   * @return The week ID
   */
  private int getWeekID(Date date)
  {
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);
    gc.setMinimalDaysInFirstWeek(4);
    gc.setFirstDayOfWeek(Calendar.MONDAY);

    int year = gc.get(Calendar.YEAR);
    int weekOfYear = gc.get(Calendar.WEEK_OF_YEAR);

    // Handle week 53/1 at year boundaries
    if (weekOfYear == 1 && gc.get(Calendar.MONTH) == Calendar.DECEMBER)
    {
      year++;
    }
    else if (weekOfYear >= 52 && gc.get(Calendar.MONTH) == Calendar.JANUARY)
    {
      year--;
    }

    return Integer.parseInt(String.format("%d%02d", year, weekOfYear));
  }

  /**
   * Creates a new Week entity for the given date
   *
   * @param session
   * @param date    The date to create the week for
   * @return A Uni containing the created Week entity
   */
  private Uni<Weeks> createWeek(Mutiny.StatelessSession session, Date date)
  {
    int weekId = getWeekID(date);
    log.debug("🚀 Creating week with ID: {}", weekId);

    // Create a new week entity
    Weeks week = new Weeks();
    week.setId(weekId);

    // Get related entities and IDs sequentially using chains
    log.debug("📋 Getting month for date: {}", date);
    return getMonth(session, date)
               .chain(month -> {
                 log.debug("✅ Got month: {}", month.getId());

                 log.debug("📋 Getting quarter for date: {}", date);
                 return getQuarter(session, date)
                            .chain(quarter -> {
                              log.debug("✅ Got quarter: {}", quarter.getId());

                              log.debug("📋 Getting year for date: {}", date);
                              return getYear(session, date)
                                         .chain(year -> {
                                           log.debug("✅ Got year: {}", year.getId());

                                           // Set week properties
                                           GregorianCalendar gc = new GregorianCalendar();
                                           gc.setTime(date);
                                           gc.setMinimalDaysInFirstWeek(4);
                                           gc.setFirstDayOfWeek(Calendar.MONDAY);

                                           week.setMonthID(month.getId());
                                           week.setQuarterID(quarter.getId());
                                           week.setYearID(year.getId());

                                           week.setWeekOfMonth(gc.get(Calendar.WEEK_OF_MONTH));
                                           week.setWeekOfYear(gc.get(Calendar.WEEK_OF_YEAR));

                                           // Set week descriptions
                                           String yearStr = String.valueOf(gc.get(Calendar.YEAR));
                                           String weekOfYearStr = String.format("%02d", gc.get(Calendar.WEEK_OF_YEAR));

                                           week.setWeekDescription("Week " + weekOfYearStr + " " + yearStr);
                                           week.setWeekShortDescription("W" + weekOfYearStr + " " + yearStr);

                                           // Persist the week
                                           log.debug("💾 Persisting week: {}", weekId);
                                           return session.insert(week)
                                                      .replaceWith(week);
                                         });
                            });
               });
  }

  /**
   * Gets a date without checking for any existance, load years in for that
   * @param session
   * @param date
   * @return
   */
   @Override
   public Uni<Days> getDay(Mutiny.Session session, Date date)
  {
    log.debug("🔍 Getting day for date: {}", date);
    return new Days().builder(session)
        .find(Integer.valueOf(DayIDFormat.getSimpleDateFormat().format(date)))
        .get();
  }


  @Override
  public Uni<Days> getDay(Mutiny.StatelessSession session, Date date)
  {
    log.debug("🔍 Getting day for date: {}", date);

    return getDayFromDatabase(session, date)
               .onItem()
               .invoke(day -> {
                 log.debug("✅ Retrieved day for date: {}", date);
               })
               .onFailure()
               .recoverWithUni(error -> {
                 log.debug("📋 Day not found for date: {}, creating new day", date);
                 return createDay(session, date)
                            .chain(days -> {
                              return populateTransformationTables(session, date, -3).onFailure()
                                         .invoke(t -> log.error("Failed to populate transformation tables for day - {}", date, t))
                                         .replaceWith(Uni.createFrom()
                                                          .item(days));
                            });
               });
  }

  private Uni<Days> getDayFromDatabase(Mutiny.StatelessSession session, Date date)
  {
    int dayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat()
                                     .format(date));
    return new Days().builder(session)
               .find(dayId)
               .get()
               .onItem()
               .transform(optional -> {
                 if (optional != null)
                 {
                   log.debug("Found day with ID: " + dayId);
                   return (Days) optional;
                 }
                 else
                 {
                   log.debug("Day not found with ID: " + dayId);
                   throw new NoSuchElementException("Day not found for date: " + date);
                 }
               });
  }

  private Uni<Days> createDay(Mutiny.StatelessSession session, Date date)
  {
    int dayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat()
                                     .format(date));
    log.debug("🚀 Creating day with ID: {}", dayId);

    // Create a new day entity
    Days day = new Days();
    day.setId(dayId);

    // Set day properties
    LocalDate localDate = date.toInstant()
                              .atZone(ZoneId.systemDefault())
                              .toLocalDate()
        ;
    day.setDayDate(localDate);
    // Create an OffsetDateTime at midnight with the system default timezone offset
    LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
    OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime,
        ZoneOffset.systemDefault()
            .getRules()
            .getOffset(localDateTime));
    // Set the OffsetDateTime value
    day.setDayDateTime(offsetDateTime);
    // Log the conversion for debugging
    log.debug("Using OffsetDateTime for day: {}", offsetDateTime);

    // Get related entities and IDs sequentially using chains
    log.debug("📋 Getting week for date: {}", date);
    return getWeek(session, date)
               .chain(week -> {
                 log.debug("✅ Got week: {}", week.getId());
                 day.setWeekID(week);

                 log.debug("📋 Getting month for date: {}", date);
                 return getMonth(session, date)
                            .chain(month -> {
                              log.debug("✅ Got month: {}", month.getId());
                              day.setMonthID(month);

                              log.debug("📋 Getting quarter for date: {}", date);
                              return getQuarter(session, date)
                                         .chain(quarter -> {
                                           log.debug("✅ Got quarter: {}", quarter.getId());
                                           day.setQuarterID(quarter.getId());

                                           log.debug("📋 Getting year for date: {}", date);
                                           return getYear(session, date)
                                                      .chain(year -> {
                                                        log.debug("✅ Got year: {}", year.getId());
                                                        day.setYearID(year.getId());

                                                        log.debug("📋 Getting day name for date: {}", date);
                                                        return getDayNameEntity(session, date)
                                                                   .chain(dayName -> {
                                                                     log.debug("✅ Got day name: {}", dayName.getDayName());
                                                                     day.setDayNameID(dayName);

                                                                     // Set day descriptions
                                                                     GregorianCalendar gc = new GregorianCalendar();
                                                                     gc.setTime(date);
                                                                     day.setDayInMonth(gc.get(Calendar.DAY_OF_MONTH));
                                                                     day.setDayInYear(gc.get(Calendar.DAY_OF_YEAR));

                                                                     // Set last IDs
                                                                     GregorianCalendar prevDayGc = new GregorianCalendar();
                                                                     prevDayGc.setTime(date);
                                                                     prevDayGc.add(Calendar.DAY_OF_MONTH, -1);
                                                                     Date prevDay = prevDayGc.getTime();

                                                                     day.setLastDayID(Integer.parseInt(DayIDFormat.getSimpleDateFormat()
                                                                                                           .format(prevDay)));
                                                                     day.setLastMonthID(Integer.parseInt(MonthIDFormat.getSimpleDateFormat()
                                                                                                             .format(prevDay)));
                                                                     day.setLastQuarterID(getQuarterID(prevDay));
                                                                     day.setLastYearID(Integer.parseInt(YearIDFormat.getSimpleDateFormat()
                                                                                                            .format(prevDay)));

                                                                     // Set day descriptions
                                                                     String dayMonthDesc = DayMonthDescriptionFormat.getSimpleDateFormat()
                                                                                               .format(date);
                                                                     day.setDayMonthDescription(dayMonthDesc);
                                                                     day.setDayDDMMYYYYDescription(DayDDMMYYYYFormat.getSimpleDateFormat()
                                                                                                       .format(date));
                                                                     day.setDayDDMMYYYYSlashDescription(DaySlashDDMMYYYYFormat.getSimpleDateFormat()
                                                                                                            .format(date));
                                                                     day.setDayDDMMYYYYHyphenDescription(DayHyphenDDMMYYYYFormat.getSimpleDateFormat()
                                                                                                             .format(date));
                                                                     day.setDayLongDescription(DayLongDescriptionFormat.getSimpleDateFormat()
                                                                                                   .format(date));
                                                                     day.setDayFullDescription(DayFullDescriptionFormat.getSimpleDateFormat()
                                                                                                   .format(date));

                                                                     // Set additional required descriptions
                                                                     String mmQQDesc = String.format("%02d", gc.get(Calendar.MONTH) + 1) + "Q" +
                                                                                           String.format("%d", getQuarterNumber(gc));
                                                                     day.setDayMMQQDescription(mmQQDesc);

                                                                     String yyyyMMDesc = String.format("%04d", gc.get(Calendar.YEAR)) +
                                                                                             String.format("%02d", gc.get(Calendar.MONTH) + 1);
                                                                     day.setDayYYYYMMDescription(yyyyMMDesc);

                                                                     // Set day is public holiday flag
                                                                     day.setDayIsPublicHoliday((short) 0);

                                                                     // Persist the day
                                                                     log.debug("💾 Persisting day: {}", dayId);
                                                                     return session.insert(day)
                                                                                .replaceWith(day);
                                                                   });
                                                      });
                                         });
                            });
               });
  }

  /**
   * Gets the DayNames entity for the given date
   *
   * @param session
   * @param date    The date to get the day name for
   * @return A Uni containing the DayNames entity
   */
  private Uni<DayNames> getDayNameEntity(Mutiny.StatelessSession session, Date date)
  {
    String dayName = new java.text.SimpleDateFormat("EEEE").format(date);
    return getDayNameEntity(session, dayName);
  }

  /**
   * Gets the DayNames entity for the given day name
   *
   * @param session
   * @param dayName The day name (e.g., "Monday")
   * @return A Uni containing the DayNames entity
   */
  private Uni<DayNames> getDayNameEntity(Mutiny.StatelessSession session, String dayName)
  {
    return new DayNames().builder(session)
               .where(DayNames_.dayName, Equals, dayName)
               .get()
               .onItem()
               .transform(optional -> {
                 if (optional != null)
                 {
                   log.debug("Found day name: " + dayName);
                   return (DayNames) optional;
                 }
                 else
                 {
                   log.debug("Day name not found: " + dayName);
                   throw new NoSuchElementException("Day name not found: " + dayName);
                 }
               })
               .onFailure()
               .recoverWithUni(() -> createDayNameEntity(session, dayName));
  }

  /**
   * Creates a new DayNames entity
   *
   * @param session The session to use
   * @param dayName The day name to create
   * @return A Uni containing the created DayNames entity
   */
  private Uni<DayNames> createDayNameEntity(Mutiny.StatelessSession session, String dayName)
  {
    log.debug("Creating new day name entity: " + dayName);

    DayNames newDayName = new DayNames();
    newDayName.setDayName(dayName);

    return session.insert(newDayName)
               .chain(() -> Uni.createFrom()
                                .item(newDayName));
  }

  /**
   * Creates time-related entities (hours, minutes, half-hours, day parts)
   * <p>
   * This method creates all the necessary time entities for the system:
   * - Hours: 24 hour entities (0-23)
   * - Time: 1440 time entities (24 hours × 60 minutes)
   * - HalfHours: 48 half-hour entities (24 hours × 2 half-hours)
   * - HalfHourDayParts: Relationships between half-hours and day parts
   * <p>
   * The method checks if time entities already exist before creating them to avoid duplication.
   *
   * @return A Uni containing void
   */
  @Override
  public Uni<Void> createTime()
  {
    log.info("🚀 Creating time entities");

    // First check if Hours entities already exist
    return sessionFactory.withStatelessTransaction(session ->
                                                       new Hours().builder(session)
                                                           .where(Hours_.id, Equals, 1)
                                                           .get()
                                                           .onItem()
                                                           .transformToUni(hour -> {
                                                             // Hours already exist, return empty Uni
                                                             log.info("✅ Time entities already exist");
                                                             return Uni.createFrom()
                                                                        .voidItem();
                                                           })
                                                           .onFailure(NoResultException.class)
                                                           .recoverWithUni(() -> {
                                                             // Hours don't exist, create them
                                                             log.info("🚀 Creating time entities (hours, minutes, half-hours)");
                                                             final AtomicInteger dayPartCount = new AtomicInteger(0);

                                                             // Create a chain for sequential processing
                                                             Uni<Void> chain = Uni.createFrom()
                                                                                   .voidItem();

                                                             // Process all 24 hours
                                                             for (int hr = 0; hr < 24; hr++)
                                                             {
                                                               final int hour = hr;

                                                               // Create hour entity
                                                               Hours hourEntity = new Hours(hour);
                                                               hourEntity.setAmPmDesc(hour < 13 ? "AM" : "PM");
                                                               hourEntity.setTwelveHour(hour > 12
                                                                                            ? "" + String.format("%02d", hour - 12) + ":" + String.format("%02d", 0)
                                                                                            : String.format("%02d", hour) + ":" + String.format("%02d", 0));
                                                               hourEntity.setTwentyFourHour(String.format("%02d", hour) + ":" + String.format("%02d", 0));
                                                               hourEntity.setPreviousHourID(hour == 0 ? 23 : hour - 1);

                                                               // Add hour persistence to the chain
                                                               chain = chain.chain(() -> {
                                                                 log.debug("📋 Creating hour: {}", hour);
                                                                 return session.insert(hourEntity)
                                                                            .onItem()
                                                                            .invoke(() -> {
                                                                              log.debug("✅ Created hour: {}", hour);
                                                                            })
                                                                            .onFailure()
                                                                            .invoke(e -> {
                                                                              log.error("❌ Failed to create hour {}: {}", hour, e.getMessage(), e);
                                                                            });
                                                               });

                                                               // Create minute entities for this hour - one at a time
                                                               // For testing, only create a few minutes per hour
                                                               for (int min = 0; min < 60; min += 15)
                                                               {
                                                                 final int minute = min;

                                                                 // Create time entity
                                                                 TimePK timePK = new TimePK(hour, minute);
                                                                 Time time = new Time(timePK);
                                                                 time.setAmPmDesc(hour < 13 ? "AM" : "PM");
                                                                 time.setTwelveHoursDesc(hour > 12
                                                                                             ? "" + String.format("%02d", hour - 12) + ":" + String.format("%02d", minute)
                                                                                             : String.format("%02d", hour) + ":" + String.format("%02d", minute));
                                                                 time.setTwentyFourHoursDesc(String.format("%02d", hour) + ":" + String.format("%02d", minute));
                                                                 time.setPreviousHourID(hour == 0 ? 23 : hour - 1);
                                                                 time.setPreviousMinuteID(minute == 0 ? 59 : minute - 1);

                                                                 // Add time insertence to the chain
                                                                 final Time finalTime = time;
                                                                 chain = chain.chain(() -> {
                                                                   log.debug("📋 Creating time: {}", finalTime.getId());
                                                                   return session.insert(finalTime)
                                                                              .onItem()
                                                                              .invoke(() -> {
                                                                                log.debug("✅ Created time: {}", finalTime.getId());
                                                                              })
                                                                              .onFailure()
                                                                              .invoke(e -> {
                                                                                log.error("❌ Failed to create time {}: {}", finalTime.getId(), e.getMessage(), e);
                                                                              });
                                                                 });

                                                                 // Create half-hour entities at 0 and 30 minutes
                                                                 if (minute == 0 || minute == 30)
                                                                 {
                                                                   HalfHours halfHour = new HalfHours(new TimePK(hour, minute));
                                                                   halfHour.setAmPmDesc(hour < 13 ? "AM" : "PM");
                                                                   halfHour.setTwelveHourClockDesc(hour > 12
                                                                                                       ? "" + String.format("%02d", hour - 12) + ":" + String.format("%02d", minute)
                                                                                                       : String.format("%02d", hour) + ":" + String.format("%02d", minute));
                                                                   halfHour.setTwentyFourHourClockDesc(String.format("%02d", hour) + ":" + String.format("%02d", minute));
                                                                   halfHour.setPreviousHourID(hour == 0 ? 23 : hour - 1);
                                                                   halfHour.setPreviousHalfHourMinuteID(minute == 0 ? 30 : 0);

                                                                   final HalfHours finalHalfHour = halfHour;
                                                                   chain = chain.chain(() -> {
                                                                     log.debug("📋 Creating half-hour: {}", finalHalfHour.getId());
                                                                     return session.insert(finalHalfHour)
                                                                                .chain(() -> {
                                                                                  // Create HalfHourDayParts entity
                                                                                  int currentDayPartCount = dayPartCount.getAndIncrement();
                                                                                  HalfHourDayParts halfHourDayParts = new HalfHourDayParts();
                                                                                  halfHourDayParts.setId(currentDayPartCount);
                                                                                  halfHourDayParts.setHourID(finalHalfHour.getId()
                                                                                                                 .getHourID());
                                                                                  halfHourDayParts.setMinuteID(finalHalfHour.getId()
                                                                                                                   .getMinuteID());

                                                                                  // Get DayPart from TimeService
                                                                                  int dayPartId = getDayPartId(finalHalfHour.getId()
                                                                                                                   .getHourID(),
                                                                                      finalHalfHour.getId()
                                                                                          .getMinuteID());
                                                                                  log.debug("📋 Finding DayPart: {}", dayPartId);
                                                                                  return session.get(DayParts.class, dayPartId)
                                                                                             .chain(dayPart -> {
                                                                                               if (dayPart == null)
                                                                                               {
                                                                                                 // Create the DayPart if it doesn't exist
                                                                                                 log.debug("📋 Creating missing DayPart: {}", dayPartId);
                                                                                                 DayParts newDayPart = new DayParts();
                                                                                                 newDayPart.setId(dayPartId);

                                                                                                 // Set day part descriptions based on the time of day
                                                                                                 String dayPartName;
                                                                                                 String dayPartDesc;
                                                                                                 if (dayPartId == 1)
                                                                                                 {
                                                                                                   dayPartName = "Early Morning";
                                                                                                   dayPartDesc = "12am-6am";
                                                                                                 }
                                                                                                 else if (dayPartId == 2)
                                                                                                 {
                                                                                                   dayPartName = "Morning";
                                                                                                   dayPartDesc = "6am-12pm";
                                                                                                 }
                                                                                                 else if (dayPartId == 3)
                                                                                                 {
                                                                                                   dayPartName = "Afternoon";
                                                                                                   dayPartDesc = "12pm-6pm";
                                                                                                 }
                                                                                                 else
                                                                                                 {
                                                                                                   dayPartName = "Evening";
                                                                                                   dayPartDesc = "6pm-12am";
                                                                                                 }

                                                                                                 newDayPart.setDayPartName(dayPartName);
                                                                                                 newDayPart.setDayPartDescription(dayPartDesc);
                                                                                                 newDayPart.setDayPartSortOrder(dayPartId);

                                                                                                 return session.insert(newDayPart)
                                                                                                            .chain(() -> {
                                                                                                              log.info("✅ Created DayPart: " + dayPartId);
                                                                                                              halfHourDayParts.setDayPartID(newDayPart);
                                                                                                              log.info("📋 Creating HalfHourDayParts: {}", halfHourDayParts.getId());
                                                                                                              return session.insert(halfHourDayParts)
                                                                                                                         .onItem()
                                                                                                                         .invoke(() -> {
                                                                                                                           log.info("✅ Created HalfHourDayParts: {}", halfHourDayParts.getId());
                                                                                                                         })
                                                                                                                         .onFailure()
                                                                                                                         .invoke(e -> {
                                                                                                                           log.error("❌ Failed to create HalfHourDayParts {}: {}",
                                                                                                                               halfHourDayParts.getId(), e.getMessage(), e);
                                                                                                                         });
                                                                                                            })
                                                                                                            .onFailure()
                                                                                                            .invoke(e -> {
                                                                                                              log.error("❌ Failed to create DayPart {}: {}",
                                                                                                                  dayPartId, e.getMessage(), e);
                                                                                                            });
                                                                                               }
                                                                                               else
                                                                                               {
                                                                                                 halfHourDayParts.setDayPartID(dayPart);
                                                                                                 log.info("📋 Creating HalfHourDayParts with existing DayPart: {}", halfHourDayParts.getId());
                                                                                                 return session.insert(halfHourDayParts)
                                                                                                            .onItem()
                                                                                                            .invoke(() -> {
                                                                                                              log.info("✅ Created HalfHourDayParts: {}", halfHourDayParts.getId());
                                                                                                            })
                                                                                                            .onFailure()
                                                                                                            .invoke(e -> {
                                                                                                              log.error("❌ Failed to create HalfHourDayParts {}: {}",
                                                                                                                  halfHourDayParts.getId(), e.getMessage(), e);
                                                                                                            });
                                                                                               }
                                                                                             })
                                                                                             .onFailure()
                                                                                             .invoke(e -> {
                                                                                               log.error("❌ Failed to find DayPart {}: {}",
                                                                                                   dayPartId, e.getMessage(), e);
                                                                                             });
                                                                                })
                                                                                .onItem()
                                                                                .invoke(() -> {
                                                                                  log.info("✅ Created half-hour: " + finalHalfHour.getId());
                                                                                })
                                                                                .onFailure()
                                                                                .invoke(e -> {
                                                                                  log.error("❌ Failed to create half-hour {}: {}",
                                                                                      finalHalfHour.getId(), e.getMessage(), e);
                                                                                });
                                                                   });
                                                                 }
                                                               }
                                                             }

                                                             // Add logging of created entities to the chain
                                                             chain = chain.chain(() -> {
                                                               log.info("🔍 Collecting created hours and half-hours for logging");
                                                               return session.createQuery("SELECT h FROM Hours h ORDER BY h.id", Hours.class)
                                                                          .getResultList()
                                                                          .onItem()
                                                                          .invoke(hours -> {
                                                                            log.info("📊 Collected {} hours", hours.size());
                                                                            log.info("📋 Created Hours Summary:");
                                                                            for (Hours hour : hours)
                                                                            {
                                                                              log.info("  Hour: {}, 12h: {}, 24h: {}",
                                                                                  hour.getId(),
                                                                                  hour.getTwelveHour(),
                                                                                  hour.getTwentyFourHour());
                                                                            }
                                                                          })
                                                                          .chain(() ->
                                                                                     session.createQuery("SELECT h FROM HalfHours h ORDER BY h.id.hourID, h.id.minuteID", HalfHours.class)
                                                                                         .getResultList()
                                                                                         .onItem()
                                                                                         .invoke(halfHours -> {
                                                                                           log.info("📊 Collected {} half-hours", halfHours.size());
                                                                                           log.info("📋 Created Half-Hours Summary:");
                                                                                           for (HalfHours halfHour : halfHours)
                                                                                           {
                                                                                             log.info("  Half-Hour: {}:{}, 12h: {}, 24h: {}",
                                                                                                 halfHour.getId()
                                                                                                     .getHourID(),
                                                                                                 halfHour.getId()
                                                                                                     .getMinuteID(),
                                                                                                 halfHour.getTwelveHourClockDesc(),
                                                                                                 halfHour.getTwentyFourHourClockDesc());
                                                                                           }
                                                                                         })
                                                                                         .replaceWith(Uni.createFrom()
                                                                                                          .voidItem())
                                                                          );
                                                             });

                                                             // Return the chain to ensure all operations are executed
                                                             return chain
                                                                        .onItem()
                                                                        .invoke(() -> {
                                                                          log.info("✅ Time entities creation completed");
                                                                        })
                                                                        .onFailure()
                                                                        .invoke(e -> {
                                                                          log.error("❌ Failed to create time entities: {}", e.getMessage(), e);
                                                                        });
                                                           })
                                                           .onFailure()
                                                           .invoke(e -> {
                                                             log.error("❌ Failed in createTime: {}", e.getMessage(), e);
                                                           })
    );
  }

  /**
   * Gets the day part ID for the given hour and minute
   * This is a simplified implementation that assigns day parts based on time of day
   *
   * @param hour   The hour (0-23)
   * @param minute The minute (0-59)
   * @return The day part ID
   */
  private int getDayPartId(int hour, int minute)
  {
    // Early morning: 12am-6am
    if (hour >= 0 && hour < 6)
    {
      return 1;
    }
    // Morning: 6am-12pm
    else if (hour >= 6 && hour < 12)
    {
      return 2;
    }
    // Afternoon: 12pm-6pm
    else if (hour >= 12 && hour < 18)
    {
      return 3;
    }
    // Evening: 6pm-12am
    else
    {
      return 4;
    }
  }

  /**
   * Populates transformation tables for the given date
   * Creates year-to-date, quarter-to-date, month-to-date, and fiscal period relationships
   * <p>
   * This method creates all the necessary time transformation relationships for a given date:
   * - YTD: Relationships between the given date and all days from the start of the year
   * - QTD: Relationships between the given date and all days from the start of the quarter
   * - MTD: Relationships between the given date and all days from the start of the month
   * - Fiscal: Relationship between the given date and its corresponding fiscal date
   *
   * @param session
   * @param date      The date to create transformations for
   * @param fiscalLag The fiscal month lag (number of months to shift for fiscal year)
   * @return A Uni containing void
   */
  public Uni<Void> populateTransformationTables(Mutiny.StatelessSession session, Date date, int fiscalLag)
  {
    log.info("Populating transformation tables for date: " + date + " with fiscal lag: " + fiscalLag);
    
    // Get all entities from the four methods
    Uni<List<TransYtd>> ytdUni = getDayYTD(session, date);
    Uni<List<TransQtd>> qtdUni = getDayQTD(session, date);
    Uni<List<TransMtd>> mtdUni = getDayMTD(session, date);
    Uni<List<TransFiscal>> fiscalUni = getDayFiscal(session, date, fiscalLag);
    
    // Combine all four Unis to get all entities at once
    return Uni.combine().all().unis(ytdUni, qtdUni, mtdUni, fiscalUni)
              .asTuple()
              .chain(tuple -> {
                List<TransYtd> ytdEntities = tuple.getItem1();
                List<TransQtd> qtdEntities = tuple.getItem2();
                List<TransMtd> mtdEntities = tuple.getItem3();
                List<TransFiscal> fiscalEntities = tuple.getItem4();
                
                // Log the number of entities for each type
                log.info("Created {} YTD relationships", ytdEntities.size());
                log.info("Created {} QTD relationships", qtdEntities.size());
                log.info("Created {} MTD relationships", mtdEntities.size());
                log.info("Created {} fiscal relationships", fiscalEntities.size());
                
                // Combine all entities into a single array
                List<Object> allEntities = new ArrayList<>();
                allEntities.addAll(ytdEntities);
                allEntities.addAll(qtdEntities);
                allEntities.addAll(mtdEntities);
                allEntities.addAll(fiscalEntities);
                
                // Log the total number of entities being inserted
                log.info("Inserting {} total relationships in a single batch", allEntities.size());
                
                // Insert all entities in a single batch
                return session.insertAll(allEntities.toArray());
              })
              .onItem()
              .invoke(() -> {
                log.info("Transformation tables populated for date: " + date);
              })
              .onFailure()
              .invoke(error -> {
                log.error("❌ Failed to populate transformation tables: {}", error.getMessage(), error);
              });
  }

  /**
   * Creates year-to-date relationships for the given date
   * <p>
   * This method creates TransYtd entities that represent relationships between
   * the given date and all days from the start of the year up to the given date.
   * These relationships are used for year-to-date calculations and reporting.
   * <p>
   * For example, if the date is March 15, 2025, this method will create relationships
   * between March 15 and every day from January 1, 2025 to March 15, 2025.
   *
   * @param session
   * @param date    The date to create YTD relationships for
   * @return A Uni containing a list of TransYtd entities
   */
  private Uni<List<TransYtd>> getDayYTD(Mutiny.StatelessSession session, Date date)
  {
    log.info("🚀 Creating YTD relationships for date: " + date);
    // Create calendar for the given date
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);

    // Create calendar for the start of the year
    GregorianCalendar startYearGC = new GregorianCalendar();
    startYearGC.set(Calendar.YEAR, gc.get(Calendar.YEAR));
    startYearGC.set(Calendar.MONTH, 0);
    startYearGC.set(Calendar.DATE, 1);

    // Normalize times to midnight
    gc.set(GregorianCalendar.HOUR, 0);
    gc.set(GregorianCalendar.SECOND, 0);
    gc.set(GregorianCalendar.MINUTE, 0);
    gc.set(GregorianCalendar.MILLISECOND, 0);
    startYearGC.set(GregorianCalendar.HOUR, 0);
    startYearGC.set(GregorianCalendar.SECOND, 0);
    startYearGC.set(GregorianCalendar.MINUTE, 0);
    startYearGC.set(GregorianCalendar.MILLISECOND, 0);

    // Get the day ID for the given date
    int dayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat()
                                     .format(date));

    log.debug("📋 Preparing YTD relationships for day ID: {}", dayId);

    // Create a list to hold all entities
    List<TransYtd> ytdEntities = new ArrayList<>();
    int totalDays = 0;

    // Create TransYtd entities for each day from start of year to given date
    while (startYearGC.getTime()
               .getTime() <= gc.getTime()
                                 .getTime())
    {
      final Date currentDate = startYearGC.getTime();
      final int ytdDayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat()
                                                .format(currentDate));
      totalDays++;

      // Create TransYtd entity
      final TransYtd transYtd = new TransYtd()
                                    .setId(new TransYtdPK()
                                               .setDayID(dayId)
                                               .setYtdDayID(ytdDayId));
      
      // Add to list
      ytdEntities.add(transYtd);
      log.debug("📋 Created YTD relationship: {} -> {}",
          transYtd.getId()
              .getDayID(),
          transYtd.getId()
              .getYtdDayID());

      // Move to next day
      startYearGC.add(Calendar.DATE, 1);
    }

    final int finalTotalDays = totalDays;

    // Return the list of entities
    return Uni.createFrom()
              .item(ytdEntities)
              .onItem()
              .invoke(() -> {
                log.info("✅ YTD relationships created for date: " + date + " (" + finalTotalDays + " relationships)");
              })
              .onFailure()
              .invoke(error -> {
                log.error("❌ Failed to create YTD relationships: {}", error.getMessage(), error);
              });
  }

  /**
   * Creates quarter-to-date relationships for the given date
   * <p>
   * This method creates TransQtd entities that represent relationships between
   * the given date and all days from the start of the quarter up to the given date.
   * These relationships are used for quarter-to-date calculations and reporting.
   * <p>
   * The method determines the start of the quarter based on the quarter number:
   * - Q1: January 1
   * - Q2: April 1
   * - Q3: July 1
   * - Q4: October 1
   * <p>
   * For example, if the date is May 15, 2025 (Q2), this method will create relationships
   * between May 15 and every day from April 1, 2025 to May 15, 2025.
   *
   * @param session
   * @param date    The date to create QTD relationships for
   * @return A Uni containing a list of TransQtd entities
   */
  private Uni<List<TransQtd>> getDayQTD(Mutiny.StatelessSession session, Date date)
  {
    log.info("🚀 Creating QTD relationships for date: " + date);
    // Create calendar for the given date
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);

    // Create calendar for the start of the quarter
    GregorianCalendar startQuarterGC = new GregorianCalendar();
    startQuarterGC.set(Calendar.YEAR, gc.get(Calendar.YEAR));

    // Normalize times to midnight
    gc.set(GregorianCalendar.HOUR, 0);
    gc.set(GregorianCalendar.SECOND, 0);
    gc.set(GregorianCalendar.MINUTE, 0);
    gc.set(GregorianCalendar.MILLISECOND, 0);
    startQuarterGC.set(GregorianCalendar.HOUR, 0);
    startQuarterGC.set(GregorianCalendar.SECOND, 0);
    startQuarterGC.set(GregorianCalendar.MINUTE, 0);
    startQuarterGC.set(GregorianCalendar.MILLISECOND, 0);

    // Determine the start month of the quarter based on the quarter number
    int quarterNumber = getQuarterNumber(gc);
    switch (quarterNumber)
    {
      case 1:
        startQuarterGC.set(Calendar.MONTH, 0); // January
        break;
      case 2:
        startQuarterGC.set(Calendar.MONTH, 3); // April
        break;
      case 3:
        startQuarterGC.set(Calendar.MONTH, 6); // July
        break;
      case 4:
        startQuarterGC.set(Calendar.MONTH, 9); // October
        break;
    }

    // Set to first day of the month
    startQuarterGC.set(Calendar.DATE, 1);

    // Get the day ID for the given date
    int dayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat()
                                     .format(date));

    log.debug("📋 Preparing QTD relationships for day ID: {} (Quarter {})", dayId, quarterNumber);

    // Create a list to hold all entities
    List<TransQtd> qtdEntities = new ArrayList<>();
    int totalDays = 0;

    // Create TransQtd entities for each day from start of quarter to given date
    while (startQuarterGC.getTime()
               .getTime() <= gc.getTime()
                                 .getTime())
    {
      final Date currentDate = startQuarterGC.getTime();
      final int qtdDayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat()
                                                .format(currentDate));
      totalDays++;

      // Create TransQtd entity
      final TransQtd transQtd = new TransQtd()
                                    .setId(new TransQtdPK()
                                               .setDayID(dayId)
                                               .setQtdDayID(qtdDayId));

      // Add to list
      qtdEntities.add(transQtd);
      log.debug("📋 Created QTD relationship: {} -> {}",
          transQtd.getId()
              .getDayID(),
          transQtd.getId()
              .getQtdDayID());

      // Move to next day
      startQuarterGC.add(Calendar.DATE, 1);
    }

    final int finalTotalDays = totalDays;

    // Return the list of entities
    return Uni.createFrom()
              .item(qtdEntities)
              .onItem()
              .invoke(() -> {
                log.info("✅ QTD relationships created for date: " + date + " (" + finalTotalDays + " relationships)");
              })
              .onFailure()
              .invoke(error -> {
                log.error("❌ Failed to create QTD relationships: {}", error.getMessage(), error);
              });
  }

  /**
   * Creates month-to-date relationships for the given date
   * <p>
   * This method creates TransMtd entities that represent relationships between
   * the given date and all days from the start of the month up to the given date.
   * These relationships are used for month-to-date calculations and reporting.
   * <p>
   * The method sets the start date to the first day of the month of the given date.
   * <p>
   * For example, if the date is July 15, 2025, this method will create relationships
   * between July 15 and every day from July 1, 2025 to July 15, 2025.
   *
   * @param session
   * @param date    The date to create MTD relationships for
   * @return A Uni containing a list of TransMtd entities
   */
  private Uni<List<TransMtd>> getDayMTD(Mutiny.StatelessSession session, Date date)
  {
    log.info("🚀 Creating MTD relationships for date: " + date);
    // Create calendar for the given date
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);

    // Create calendar for the start of the month
    GregorianCalendar startMonthGC = new GregorianCalendar();
    startMonthGC.setTime(date);
    startMonthGC.set(Calendar.DATE, 1);

    // Normalize times to midnight
    gc.set(GregorianCalendar.HOUR, 0);
    gc.set(GregorianCalendar.SECOND, 0);
    gc.set(GregorianCalendar.MINUTE, 0);
    gc.set(GregorianCalendar.MILLISECOND, 0);
    startMonthGC.set(GregorianCalendar.HOUR, 0);
    startMonthGC.set(GregorianCalendar.SECOND, 0);
    startMonthGC.set(GregorianCalendar.MINUTE, 0);
    startMonthGC.set(GregorianCalendar.MILLISECOND, 0);

    // Get the day ID for the given date
    int dayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat()
                                     .format(date));

    log.debug("📋 Preparing MTD relationships for day ID: {} (Month: {}/{})",
        dayId, (startMonthGC.get(Calendar.MONTH) + 1), startMonthGC.get(Calendar.YEAR));

    // Create a list to hold all entities
    List<TransMtd> mtdEntities = new ArrayList<>();
    int totalDays = 0;

    // Create TransMtd entities for each day from start of month to given date
    while (startMonthGC.getTime()
               .getTime() <= gc.getTime()
                                 .getTime())
    {
      final Date currentDate = startMonthGC.getTime();
      final int mtdDayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat()
                                                .format(currentDate));
      totalDays++;

      // Create TransMtd entity
      final TransMtd transMtd = new TransMtd()
                                    .setId(new TransMtdPK()
                                               .setDayID(dayId)
                                               .setMtdDayID(mtdDayId));

      // Add to list
      mtdEntities.add(transMtd);
      log.debug("📋 Created MTD relationship: {} -> {}",
          transMtd.getId()
              .getDayID(),
          transMtd.getId()
              .getMtdDayID());

      // Move to next day
      startMonthGC.add(Calendar.DATE, 1);
    }

    final int finalTotalDays = totalDays;

    // Return the list of entities
    return Uni.createFrom()
              .item(mtdEntities)
              .onItem()
              .invoke(() -> {
                log.info("✅ MTD relationships created for date: " + date + " (" + finalTotalDays + " relationships)");
              })
              .onFailure()
              .invoke(error -> {
                log.error("❌ Failed to create MTD relationships: {}", error.getMessage(), error);
              });
  }

  /**
   * Creates fiscal period relationships for the given date
   * <p>
   * This method creates a TransFiscal entity that represents the relationship between
   * the given date and its corresponding fiscal date. The fiscal date is calculated
   * by adding the fiscal month lag to the given date.
   * <p>
   * Unlike YTD, QTD, and MTD relationships which create multiple entities for a range of dates,
   * this method creates a single entity that maps a calendar date to its fiscal equivalent.
   * <p>
   * For example, with a fiscal lag of 3:
   * - January 15, 2025 would map to April 15, 2025
   * - December 31, 2024 would map to March 31, 2025
   * <p>
   * This allows for fiscal year reporting that spans different calendar years.
   *
   * @param session
   * @param date      The date to create fiscal relationship for
   * @param fiscalLag The fiscal month lag (number of months to shift)
   * @return A Uni containing a list with a single TransFiscal entity
   */
  private Uni<List<TransFiscal>> getDayFiscal(Mutiny.StatelessSession session, Date date, int fiscalLag)
  {
    log.info("Creating fiscal relationship for date: " + date + " with fiscal lag: " + fiscalLag);
    // Create calendar for the given date
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);

    // Create calendar for the fiscal date
    GregorianCalendar fiscalGC = new GregorianCalendar();
    fiscalGC.setTime(date);
    fiscalGC.add(Calendar.MONTH, fiscalLag);

    // Normalize times to midnight
    gc.set(GregorianCalendar.HOUR, 0);
    gc.set(GregorianCalendar.SECOND, 0);
    gc.set(GregorianCalendar.MINUTE, 0);
    gc.set(GregorianCalendar.MILLISECOND, 0);
    fiscalGC.set(GregorianCalendar.HOUR, 0);
    fiscalGC.set(GregorianCalendar.SECOND, 0);
    fiscalGC.set(GregorianCalendar.MINUTE, 0);
    fiscalGC.set(GregorianCalendar.MILLISECOND, 0);

    // Get the day IDs
    int dayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat()
                                     .format(date));
    int fiscalDayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat()
                                           .format(fiscalGC.getTime()));

    // Create TransFiscal entity
    TransFiscal transFiscal = new TransFiscal(dayId, fiscalDayId);
    
    // Create a list with the single entity
    List<TransFiscal> fiscalEntities = new ArrayList<>();
    fiscalEntities.add(transFiscal);
    
    log.debug("📋 Created fiscal relationship: {} -> {}", dayId, fiscalDayId);

    // Return the list with the single entity
    return Uni.createFrom()
              .item(fiscalEntities)
              .onItem()
              .invoke(() -> {
                log.info("Fiscal relationship created for date: " + date + " with fiscal date: " + fiscalGC.getTime());
              })
              .onFailure()
              .invoke(error -> {
                log.error("❌ Failed to create fiscal relationship: {}", error.getMessage(), error);
              });
  }

}