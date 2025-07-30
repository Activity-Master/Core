# TimeSystem Day Methods Update

This document provides updates to the TimeSystem documentation to have the `getDay` and related day methods return the Day entity instead of a boolean value.

## Updated ITimeSystem Interface

```java
public interface ITimeSystem
{
    Uni<Void> loadTimeRange(int startYear, int endYear);
    Uni<Days> getDay(Date date);  // Changed from Uni<Boolean> to Uni<Days>
    Uni<Void> createTime();
}
```

## Updated getDay Method Implementation

```java
@Override
public Uni<Days> getDay(Date date)  // Changed from Uni<Boolean> to Uni<Days>
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Getting day for date: {0}", date);
    
    return getDayReactive(date);
}

private Uni<Days> getDayReactive(Date date)  // Changed from Uni<Boolean> to Uni<Days>
{
    return sessionFactory.withStatelessSession(statelessSession -> {
        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Created stateless session for day operation: {0}", statelessSession.hashCode());
        
        return getDayReactive(statelessSession, date);
    });
}

private Uni<Days> getDayReactive(Mutiny.StatelessSession statelessSession, Date date)  // Changed from Uni<Boolean> to Uni<Days>
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Searching for day with date: {0} using stateless session {1}", new Object[]{date, statelessSession.hashCode()});
    
    // Use EntityAssist query builder with stateless session
    return getDayFromIDReactive(statelessSession, date)
        .onItem().invoke(existingDay -> {
            if (existingDay != null) {
                Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Day found for date: {0}", date);
            } else {
                Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Day not found for date: {0}, will create new day", date);
            }
        })
        .onFailure().invoke(error -> {
            Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to search for day {0}: {1}", new Object[]{date, error.getMessage()});
        })
        .chain(existingDay -> {
            if (existingDay != null) {
                return Uni.createFrom().item(existingDay);  // Return the existing day entity
            } else {
                // Create new day if not found
                return createDayReactive(statelessSession, date)
                    .onItem().invoke(createdDay -> {
                        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Day created for date: {0}", date);
                    })
                    .onFailure().invoke(error -> {
                        Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to create day for {0}: {1}", new Object[]{date, error.getMessage()});
                    });
            }
        });
}
```

## Updated createDay Method

```java
private Uni<Days> createDayReactive(Mutiny.StatelessSession statelessSession, Date date)  // Changed from Uni<Boolean> to Uni<Days>
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
                
                return Uni.combine().all().unis(
                    getLastDayIDReactive(statelessSession, date),
                    getLastMonthDayIDReactive(statelessSession, date),
                    getLastYearDayIDReactive(statelessSession, date),
                    getLastQuarterDayIDReactive(statelessSession, date),
                    getWeekReactive(date),
                    getMonthReactive(date)
                ).asTuple()
                .map(tuple -> {
                    int lastDayId = tuple.getItem1();
                    int lastMonthDayId = tuple.getItem2();
                    int lastYearDayId = tuple.getItem3();
                    int lastQuarterDayId = tuple.getItem4();
                    Weeks week = tuple.getItem5();
                    Months month = tuple.getItem6();
                    
                    // Set additional day properties
                    newDay.setDayOfWeekSort((short) gc.get(Calendar.DAY_OF_WEEK));
                    newDay.setDayOfMonthSort((short) gc.get(Calendar.DAY_OF_MONTH));
                    newDay.setDayOfYearSort((short) gc.get(Calendar.DAY_OF_YEAR));
                    newDay.setDayIsPublicHoliday((short) 0);
                    newDay.setLastDayID(lastDayId);
                    newDay.setLastMonthDayID(lastMonthDayId);
                    newDay.setLastYearDayID(lastYearDayId);
                    newDay.setLastQuarterDayID(lastQuarterDayId);
                    newDay.setWeekID(week);
                    newDay.setMonthID(month);
                    
                    return newDay;
                });
            })
            .chain(newDay -> {
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
                    .map(persisted -> newDay);  // Return the created day entity
            });
    })
    .onFailure().invoke(error -> {
        Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to create day for {0}: {1}", new Object[]{date, error.getMessage()});
    })
    .onFailure().recoverWithItem(null);  // Return null on failure
}
```

## Updated loadTimeRange Method

The `loadTimeRange` method needs to be updated to handle the new return type of `getDay`:

```java
private Uni<Void> loadYearReactive(Mutiny.StatelessSession statelessSession, int year)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Loading year {0} with stateless session {1}", new Object[]{year, statelessSession.hashCode()});
    
    return Uni.createFrom().item(() -> {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        
        List<Uni<Days>> dayOperations = new ArrayList<>();  // Changed from Uni<Boolean> to Uni<Days>
        
        // Process each day in the year
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            final LocalDate dateToProcess = currentDate;
            
            dayOperations.add(
                getDayReactive(statelessSession, Date.from(dateToProcess.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .onItem().invoke(day -> {  // Changed from success to day
                        if (day == null) {
                            Logger.getLogger(TimeSystem.class.getName()).log(Level.WARNING, "Day {0} processing returned null", dateToProcess);
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
        return processDaysInBatches(dayOperations, 100)  // Process 100 days at a time
            .onItem().invoke(() -> {
                Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Year {0} completed with {1} days processed", new Object[]{year, dayOperations.size()});
            });
    })
    .map(result -> null);
}

private Uni<Void> processDaysInBatches(List<Uni<Days>> dayOperations, int batchSize)  // Changed from Uni<Boolean> to Uni<Days>
{
    List<Uni<Void>> batchOperations = new ArrayList<>();
    
    for (int i = 0; i < dayOperations.size(); i += batchSize) {
        final int batchStart = i;
        final int batchEnd = Math.min(i + batchSize, dayOperations.size());
        final List<Uni<Days>> batch = dayOperations.subList(batchStart, batchEnd);  // Changed from Uni<Boolean> to Uni<Days>
        
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

## Summary of Changes

1. Changed the return type of `getDay` in the ITimeSystem interface from `Uni<Boolean>` to `Uni<Days>`
2. Updated the `getDay` method implementation to return the Day entity instead of a boolean
3. Updated the `createDay` method to return the Day entity instead of a boolean
4. Updated the `loadTimeRange` method to handle the new return type of `getDay`
5. Updated related methods to work with Day entities instead of boolean values

These changes ensure that the getDay and related day methods return the Day entity as required.