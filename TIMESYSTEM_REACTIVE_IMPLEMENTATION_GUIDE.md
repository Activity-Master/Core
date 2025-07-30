# TimeSystem Reactive Implementation Guide

This guide provides instructions for updating the TimeSystem to use reactive patterns with Uni return types for the following methods:
- `getDay` - Return `Uni<Boolean>` instead of `boolean`
- `loadTimeRange` - Return `Uni<Void>` instead of `void`
- `createTime` - Return `Uni<Void>` instead of `void`

## Prerequisites

1. Update the ITimeSystem interface to change the return types:

```java
public interface ITimeSystem
{
    Uni<Void> loadTimeRange(int startYear, int endYear);

    /**
     * True if available
     *
     * @param date
     *
     * @return
     */
    Uni<Boolean> getDay(Date date);

    Uni<Void> createTime();
}
```

2. Add the necessary imports to the TimeSystem class:

```java
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import java.time.Duration;
```

3. Add the SessionFactory injection to the TimeSystem class:

```java
@Inject
private Mutiny.SessionFactory sessionFactory;
```

## Implementation Guidelines

### 1. getDay Method

Update the `getDay` method to return `Uni<Boolean>`:

```java
@Override
public Uni<Boolean> getDay(Date date)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Getting day for date: {0}", date);
    
    return getDayReactive(date);
}

private Uni<Boolean> getDayReactive(Date date)
{
    return sessionFactory.withStatelessSession(statelessSession -> {
        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Created stateless session for day operation: {0}", statelessSession.hashCode());
        
        return getDayReactive(statelessSession, date);
    });
}

private Uni<Boolean> getDayReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Searching for day with date: {0} using stateless session {1}", new Object[]{date, statelessSession.hashCode()});
    
    // Use EntityAssist query builder with stateless session
    return new Days().builder(statelessSession)
        .find(Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(date)))
        .get()
        .onItem().invoke(existingDay -> {
            if (existingDay.isPresent()) {
                Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Day found for date: {0}", date);
            } else {
                Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Day not found for date: {0}, will create new day", date);
            }
        })
        .onFailure().invoke(error -> {
            Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to search for day {0}: {1}", new Object[]{date, error.getMessage()});
        })
        .chain(existingDay -> {
            if (existingDay.isPresent()) {
                return Uni.createFrom().item(true);
            } else {
                // Create new day if not found
                return createDayReactive(statelessSession, date)
                    .onItem().invoke(created -> {
                        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Day created for date: {0}", date);
                    })
                    .onFailure().invoke(error -> {
                        Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to create day for {0}: {1}", new Object[]{date, error.getMessage()});
                    });
            }
        });
}

private Uni<Boolean> createDayReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Creating new day for date: {0} with stateless session {1}", new Object[]{date, statelessSession.hashCode()});
    
    return Uni.createFrom().item(() -> {
        // Normalize the date
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(GregorianCalendar.HOUR, 0);
        gc.set(GregorianCalendar.SECOND, 0);
        gc.set(GregorianCalendar.MINUTE, 0);
        gc.set(GregorianCalendar.MILLISECOND, 0);
        Date normalizedDate = gc.getTime();
        
        // Create new day entity
        Days newDay = new Days();
        newDay.setId(Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(normalizedDate)));
        newDay.setDayDate(LocalDate.ofInstant(normalizedDate.toInstant(), ZoneId.systemDefault()));
        newDay.setDayDateTime(LocalDateTime.ofInstant(normalizedDate.toInstant(), ZoneId.systemDefault()));
        
        // Set day name
        String dayName = DayNameFormat.getSimpleDateFormat().format(normalizedDate);
        return getDayNameReactive(statelessSession, dayName)
            .chain(dayNameEntity -> {
                newDay.setDayNameID(dayNameEntity);
                
                // Set additional day properties
                newDay.setDayOfWeekSort((short) gc.get(Calendar.DAY_OF_WEEK));
                newDay.setDayOfMonthSort((short) gc.get(Calendar.DAY_OF_MONTH));
                newDay.setDayOfYearSort((short) gc.get(Calendar.DAY_OF_YEAR));
                newDay.setDayIsPublicHoliday((short) 0);
                
                Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Persisting new day with ID: {0} using stateless session", newDay.getId());
                
                // Insert using stateless session for bulk performance
                return statelessSession.insert(newDay)
                    .onItem().invoke(persisted -> {
                        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Day persisted successfully with ID: {0}", newDay.getId());
                    })
                    .onFailure().invoke(error -> {
                        Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to persist day with ID {0}: {1}", 
                            new Object[]{newDay.getId(), error.getMessage()});
                    })
                    .map(persisted -> true);
            });
    })
    .onFailure().invoke(error -> {
        Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to create day for {0}: {1}", new Object[]{date, error.getMessage()});
    })
    .onFailure().recoverWithItem(false);
}

private Uni<DayNames> getDayNameReactive(Mutiny.StatelessSession statelessSession, String dayName)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Searching for day name: {0} with stateless session", dayName);
    
    return new DayNames().builder(statelessSession)
        .where(DayNames_.dayName, Equals, dayName)
        .get()
        .onItem().invoke(dayNameOpt -> {
            if (dayNameOpt.isPresent()) {
                Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Day name found: {0}", dayName);
            } else {
                Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Day name not found: {0}, will create new", dayName);
            }
        })
        .chain(dayNameOpt -> {
            if (dayNameOpt.isPresent()) {
                return Uni.createFrom().item(dayNameOpt.get());
            } else {
                // Create new day name if not found
                return createDayNameReactive(statelessSession, dayName);
            }
        });
}

private Uni<DayNames> createDayNameReactive(Mutiny.StatelessSession statelessSession, String dayName)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Creating new day name: {0} with stateless session", dayName);
    
    return Uni.createFrom().item(() -> {
        DayNames newDayName = new DayNames();
        newDayName.setDayName(dayName);
        
        return statelessSession.insert(newDayName)
            .onItem().invoke(persisted -> {
                Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Day name created successfully: {0}", dayName);
            })
            .onFailure().invoke(error -> {
                Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to create day name {0}: {1}", new Object[]{dayName, error.getMessage()});
            })
            .map(persisted -> newDayName);
    });
}
```

### 2. loadTimeRange Method

Update the `loadTimeRange` method to return `Uni<Void>`:

```java
@Override
public Uni<Void> loadTimeRange(int startYear, int endYear)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Starting TimeSystem loadTimeRange from {0} to {1}", new Object[]{startYear, endYear});
    
    JobService.INSTANCE.setMaxQueueCount("TimeRangeLoading", 500);
    
    logProgress("Starting Time Load", "Time load is starting from " + startYear + " + to " + endYear);
    
    return loadTimeRangeReactive(startYear, endYear);
}

private Uni<Void> loadTimeRangeReactive(int startYear, int endYear)
{
    long startTime = System.currentTimeMillis();
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Starting reactive time range loading from {0} to {1}", new Object[]{startYear, endYear});
    
    return Uni.createFrom().item(() -> {
        // Calculate total days for progress tracking
        LocalDate startDate = LocalDate.of(startYear, 1, 1);
        LocalDate endDate = LocalDate.of(endYear, 12, 31);
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        
        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Calculated {0} total days to process", totalDays);
        return totalDays;
    })
    .chain(totalDays -> {
        // Use stateless session for bulk operations
        return sessionFactory.withStatelessSession(statelessSession -> {
            Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Created stateless session for bulk time data operations: {0}", statelessSession.hashCode());
            
            return loadTimeRangeBatched(statelessSession, startYear, endYear, totalDays);
        });
    })
    .onItem().invoke(() -> {
        long duration = System.currentTimeMillis() - startTime;
        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Time range loading completed in {0}ms", duration);
        
        // Update partition bases after loading
        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Updating partition bases after time range loading");
        com.guicedee.client.IGuiceContext.get(ActivityMasterService.class).updatePartitionBases();
        
        int difference = endYear - startYear;
        if (difference < 1) {
            difference = 1;
        }
        setTotalTasks(difference * 12);
    })
    .onFailure().invoke(error -> {
        long duration = System.currentTimeMillis() - startTime;
        Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Time range loading failed after {0}ms: {1}", new Object[]{duration, error.getMessage()});
    })
    .map(result -> null); // Convert to Void for interface compliance
}

private Uni<Void> loadTimeRangeBatched(Mutiny.StatelessSession statelessSession, int startYear, int endYear, long totalDays)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Starting batched time range loading with stateless session {0}", statelessSession.hashCode());
    
    List<Uni<Void>> yearOperations = new ArrayList<>();
    
    for (int year = startYear; year <= endYear; year++) {
        final int currentYear = year;
        
        yearOperations.add(
            loadYearReactive(statelessSession, currentYear)
                .onItem().invoke(() -> {
                    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Year {0} loaded successfully with stateless session", currentYear);
                    
                    // Progress tracking every year
                    logProgress("Time Loading", String.format("Loaded year %d", currentYear));
                })
                .onFailure().invoke(error -> {
                    Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to load year {0} with stateless session: {1}", 
                        new Object[]{currentYear, error.getMessage()});
                })
        );
    }
    
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Running {0} year loading operations in parallel with stateless session", yearOperations.size());
    
    return Uni.combine()
        .all()
        .unis(yearOperations)
        .discardItems()
        .onItem().invoke(() -> {
            Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "All {0} years loaded successfully with stateless session {1}", 
                new Object[]{yearOperations.size(), statelessSession.hashCode()});
        })
        .onFailure().invoke(error -> {
            Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to load some years with stateless session {0}: {1}", 
                new Object[]{statelessSession.hashCode(), error.getMessage()});
        })
        .map(result -> null);
}

private Uni<Void> loadYearReactive(Mutiny.StatelessSession statelessSession, int year)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Loading year {0} with stateless session {1}", new Object[]{year, statelessSession.hashCode()});
    
    return Uni.createFrom().item(() -> {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        
        List<Uni<Boolean>> dayOperations = new ArrayList<>();
        
        // Process each day in the year
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            final LocalDate dateToProcess = currentDate;
            
            dayOperations.add(
                getDayReactive(statelessSession, Date.from(dateToProcess.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .onItem().invoke(success -> {
                        if (!success) {
                            Logger.getLogger(TimeSystem.class.getName()).log(Level.WARNING, "Day {0} processing returned false", dateToProcess);
                        }
                    })
                    .onFailure().invoke(error -> {
                        Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to process day {0}: {1}", new Object[]{dateToProcess, error.getMessage()});
                    })
            );
            
            currentDate = currentDate.plusDays(1);
        }
        
        return dayOperations;
    })
    .chain(dayOperations -> {
        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Processing {0} days for year {1} with stateless session", new Object[]{dayOperations.size(), year});
        
        // Process days in batches to avoid overwhelming the database
        return processDaysInBatches(dayOperations, 100) // Process 100 days at a time
            .onItem().invoke(() -> {
                Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Year {0} completed with {1} days processed", new Object[]{year, dayOperations.size()});
            });
    })
    .map(result -> null);
}

private Uni<Void> processDaysInBatches(List<Uni<Boolean>> dayOperations, int batchSize)
{
    List<Uni<Void>> batchOperations = new ArrayList<>();
    
    for (int i = 0; i < dayOperations.size(); i += batchSize) {
        final int batchStart = i;
        final int batchEnd = Math.min(i + batchSize, dayOperations.size());
        final List<Uni<Boolean>> batch = dayOperations.subList(batchStart, batchEnd);
        
        batchOperations.add(
            Uni.combine()
                .all()
                .unis(batch)
                .discardItems()
                .onItem().invoke(() -> {
                    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Batch {0}-{1} completed ({2} days)", new Object[]{batchStart + 1, batchEnd, batch.size()});
                })
                .map(result -> null)
        );
    }
    
    return Uni.combine()
        .all()
        .unis(batchOperations)
        .discardItems()
        .map(result -> null);
}
```

### 3. createTime Method

Update the `createTime` method to return `Uni<Void>`:

```java
@Override
public Uni<Void> createTime()
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Starting createTime with internal session management");
    
    return createTimeReactive();
}

private Uni<Void> createTimeReactive()
{
    return sessionFactory.withStatelessSession(statelessSession -> {
        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Created stateless session for createTime operation: {0}", statelessSession.hashCode());
        
        return new Hours().builder(statelessSession)
            .where(Hours_.id, Equals, 1)
            .getCount()
            .chain(count -> {
                if (count == 0) {
                    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "No hours found, creating time entities");
                    return createTimeEntities(statelessSession);
                } else {
                    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Hours already exist, skipping time creation");
                    return Uni.createFrom().voidItem();
                }
            });
    });
}

private Uni<Void> createTimeEntities(Mutiny.StatelessSession statelessSession)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Creating time entities with stateless session {0}", statelessSession.hashCode());
    
    List<Uni<Void>> hourOperations = new ArrayList<>();
    
    for (int hr = 0; hr < 24; hr++) {
        final int hour = hr;
        
        hourOperations.add(createHourEntity(statelessSession, hour));
    }
    
    return Uni.combine()
        .all()
        .unis(hourOperations)
        .discardItems()
        .onItem().invoke(() -> {
            Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "All time entities created successfully");
        })
        .onFailure().invoke(error -> {
            Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to create time entities: {0}", error.getMessage());
        })
        .map(result -> null);
}

private Uni<Void> createHourEntity(Mutiny.StatelessSession statelessSession, int hr)
{
    Hours hour = new Hours(hr);
    
    hour.setAmPmDesc(hr < 13 ? "AM" : "PM");
    hour.setTwelveHour(hr > 12
            ? "" +
              DoubleDigits.formatter()
                          .format(hr - 12) +
              ":" +
              DoubleDigits.formatter()
                          .format(0)
            : DoubleDigits.formatter()
                          .format(hr) +
              ":" +
              DoubleDigits.formatter()
                          .format(0));
    hour.setTwentyFourHour(DoubleDigits.formatter()
                                       .format(hr) +
                           ":" +
                           DoubleDigits.formatter()
                                       .format(0));
    hour.setPreviousHourID(hr == 0 ? 23 : hr - 1);
    
    List<Time> timeList = new ArrayList<>();
    List<HalfHours> halfHoursList = new ArrayList<>();
    
    for (int min = 0; min < 60; min++) {
        TimePK primKey = new TimePK(hr, min);
        Time time = new Time(primKey);
        time.setAmPmDesc(hr < 13 ? "AM" : "PM");
        time.setTwelveHoursDesc(hr > 12
                ? "" +
                  DoubleDigits.formatter()
                              .format(hr - 12) +
                  ":" +
                  DoubleDigits.formatter()
                              .format(min)
                : DoubleDigits.formatter()
                              .format(hr) +
                  ":" +
                  DoubleDigits.formatter()
                              .format(min));
        time.setTwentyFourHoursDesc(DoubleDigits.formatter()
                                                .format(hr) +
                                    ":" +
                                    DoubleDigits.formatter()
                                                .format(min));
        time.setPreviousHourID(hr == 0 ? 23 : hr - 1);
        time.setPreviousMinuteID(min == 0 ? 59 : min - 1);
        timeList.add(time);
        
        if (min == 30 || min == 0) {
            HalfHours halfHours = new HalfHours().setId(new TimePK(hr, min));
            halfHours.setAmPmDesc(hr < 13 ? "AM" : "PM");
            halfHours.setTwelveHourClockDesc(
                    hr > 12
                            ? "" +
                              DoubleDigits.formatter()
                                          .format(hr - 12) +
                              ":" +
                              DoubleDigits.formatter()
                                          .format(min)
                            : DoubleDigits.formatter()
                                          .format(hr) +
                              ":" +
                              DoubleDigits.formatter()
                                          .format(min));
            halfHours.setTwentyFourHourClockDesc(DoubleDigits.formatter()
                                                             .format(hr) +
                                                 ":" +
                                                 DoubleDigits.formatter()
                                                             .format(min));
            halfHours.setPreviousHourID(hr == 0 ? 23 : hr - 1);
            halfHours.setPreviousHalfHourMinuteID(min == 0 ? 30 : 0);
            halfHoursList.add(halfHours);
        }
    }
    
    // Insert hour
    return statelessSession.insert(hour)
        .chain(inserted -> {
            // Insert time entities
            List<Uni<Void>> timeInserts = new ArrayList<>();
            for (Time time : timeList) {
                timeInserts.add(
                    statelessSession.insert(time)
                        .map(result -> null)
                );
            }
            
            return Uni.combine()
                .all()
                .unis(timeInserts)
                .discardItems();
        })
        .chain(ignored -> {
            // Insert half hour entities
            List<Uni<Void>> halfHourInserts = new ArrayList<>();
            for (HalfHours halfHour : halfHoursList) {
                halfHourInserts.add(
                    statelessSession.insert(halfHour)
                        .chain(inserted -> {
                            // Create and insert HalfHourDayParts
                            HalfHourDayParts halfHourDayParts = new HalfHourDayParts();
                            halfHourDayParts.setId(hr * 2 + (halfHour.getId().getMinuteID() == 0 ? 0 : 1));
                            
                            halfHourDayParts.setHourID(halfHour.getId().getHourID());
                            halfHourDayParts.setMinuteID(halfHour.getId().getMinuteID());
                            
                            TimeService<?> timeService = com.guicedee.client.IGuiceContext.get(TimeService.class);
                            halfHourDayParts.setDayPartID(timeService.getDayPart(halfHour.getId().getHourID(),
                                    halfHour.getId().getMinuteID()));
                            
                            return statelessSession.insert(halfHourDayParts)
                                .map(result -> null);
                        })
                );
            }
            
            return Uni.combine()
                .all()
                .unis(halfHourInserts)
                .discardItems();
        })
        .onItem().invoke(() -> {
            Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Hour {0} created with all related entities", hr);
        })
        .onFailure().invoke(error -> {
            Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to create hour {0}: {1}", new Object[]{hr, error.getMessage()});
        })
        .map(result -> null);
}
```

## Key Reactive Patterns

1. **Chain Operations**: Use `.chain()` to sequence operations that depend on the result of the previous operation.

2. **Error Handling**: Use `.onFailure().invoke()` to log errors and `.onFailure().recoverWithItem()` to provide fallback values.

3. **Stateless Sessions**: Use `sessionFactory.withStatelessSession()` for bulk operations.

4. **Batched Processing**: Process large datasets in batches to avoid overwhelming the database.

5. **Combine Operations**: Use `Uni.combine().all().unis()` to run multiple operations in parallel.

6. **Logging**: Use comprehensive logging to track the progress of operations.

7. **Progress Tracking**: Use `logProgress()` to track the progress of long-running operations.

## Testing

After implementing these changes, test the TimeSystem to ensure:

1. The public API methods (loadTimeRange, getDay, createTime) work correctly
2. The session is properly passed through the call chain
3. All methods that accept a Mutiny.Session parameter use it correctly for database operations
4. Performance is maintained, especially for bulk operations

## Conclusion

This guide provides a comprehensive approach to updating the TimeSystem to use reactive patterns with Uni return types. By following these guidelines, you can ensure that the TimeSystem properly manages sessions and transactions while maintaining backward compatibility with existing code.