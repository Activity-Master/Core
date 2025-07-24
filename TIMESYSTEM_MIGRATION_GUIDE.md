# TimeSystem Reactive Migration Guide

## Overview

This guide provides comprehensive migration instructions for converting the TimeSystem from synchronous to reactive patterns while maintaining the exact public API signatures for `loadTimeRange()` and `getDay()` methods. The TimeSystem is unique in ActivityMaster as it's the **only system that creates and manages its own stateless sessions** for time-related operations.

## 🔧 Technology Stack Compliance

TimeSystem follows the same technology stack as other ActivityMaster components:

- 🎯 **Dependency Injection**: Google Guice with GuicedEE wrapper
- 🏗️ **Architecture Pattern**: CRTP (Curiously Recurring Template Pattern)
- 🗄️ **Query Builder**: EntityAssist for criteria query building
- ⚡ **Reactive Framework**: SmallRye Mutiny for non-blocking operations
- 💾 **Database Access**: Hibernate Reactive with Mutiny integration
- 🔄 **Session Management**: **STATELESS sessions created internally (EXCEPTION to library pattern)**

## ⚠️ Important: TimeSystem Session Management Exception

**TimeSystem is the ONLY system in ActivityMaster that creates its own sessions**:

- 🏛️ **Internal Session Creation**: TimeSystem creates stateless sessions for its operations
- 📚 **Exception to Library Pattern**: Unlike other ActivityMaster services, TimeSystem doesn't require external session management
- 🔄 **Stateless Design**: All TimeSystem sessions are stateless for optimal performance with large datasets
- ⚡ **Bulk Operations**: Optimized for very large data row inserts during time range loading
- 🧪 **Testing**: Tests can call TimeSystem methods directly without providing sessions

## Public API Preservation

The following public methods **MUST** maintain their exact signatures and return types:

### Required Public Interface Signatures
```java
public interface ITimeSystem
{
    void loadTimeRange(int startYear, int endYear);  // MUST remain void return type
    boolean getDay(Date date);                       // MUST remain boolean return type
    void createTime();                               // Additional method
}
```

## Core Migration Patterns

### 1. System Structure with CRTP and Internal Session Management

```java
@Log4j2
public class TimeSystem 
        extends ActivityMasterDefaultSystem<TimeSystem>  // CRTP pattern
        implements IActivityMasterSystem<TimeSystem>,     // CRTP pattern
                   ITimeSystem,                           // Public interface
                   IProgressable                          // Progress tracking
{
    @Inject
    private ISystemsService<?> systemsService;
    
    @Inject
    private Mutiny.SessionFactory sessionFactory;  // Internal session factory
    
    // System registration with external session (library pattern)
    @Override
    public ISystems<?, ?> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        log.info("🚀 Registering TimeSystem with externally provided session");
        
        ISystems<?, ?> iSystems = systemsService
            .create(session, enterprise, getSystemName(), getSystemDescription())
            .await().atMost(Duration.ofMinutes(1));
            
        getSystem(session, enterprise).chain(system ->
        {
            return systemsService.registerNewSystem(session, enterprise, system);
        }).await().atMost(Duration.ofMinutes(1));
        
        return iSystems;
    }
    
    // System defaults creation with external session (library pattern)
    @Override
    public void createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        log.info("🔧 Creating TimeSystem defaults with external session");
        logProgress("Time System", "Loading Time Classifications...", 4);
        
        // Call loadTimeRange which will create its own stateless sessions
        loadTimeRange(2004, LocalDateTime.now().getYear());
    }
    
    // Post-startup operations (reactive)
    @Override
    public Uni<Void> postStartup(Mutiny.Session session, IEnterprise<?, ?> enterprise)
    {
        log.info("🚀 TimeSystem post-startup operations with external session");
        
        return systemsService.findSystem(session, enterprise, getSystemName())
            .chain(system ->
            {
                log.debug("✅ TimeSystem found during post-startup: '{}'", system.getName());
                // Perform any post-startup validation
                return Uni.createFrom().voidItem();
            });
    }
}
```

### 2. Reactive loadTimeRange() Implementation

**CRITICAL**: Must remain `void` return type for public API compatibility:

```java
@Override
public void loadTimeRange(int startYear, int endYear)
{
    log.info("🚀 Starting TimeSystem loadTimeRange from {} to {} with internal session management", startYear, endYear);
    
    // Internal reactive implementation with .await() to maintain void signature
    loadTimeRangeReactive(startYear, endYear)
        .await().atMost(Duration.ofHours(2)); // Allow extended time for large datasets
    
    log.info("✅ TimeSystem loadTimeRange completed for years {} to {}", startYear, endYear);
}

private Uni<Void> loadTimeRangeReactive(int startYear, int endYear)
{
    long startTime = System.currentTimeMillis();
    log.info("🚀 Starting reactive time range loading from {} to {} with stateless sessions", startYear, endYear);
    
    return Uni.createFrom().item(() ->
    {
        // Calculate total days for progress tracking
        LocalDate startDate = LocalDate.of(startYear, 1, 1);
        LocalDate endDate = LocalDate.of(endYear, 12, 31);
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        
        log.info("📊 Calculated {} total days to process", totalDays);
        return totalDays;
    })
    .chain(totalDays ->
    {
        // Use stateless session for bulk operations
        return sessionFactory.withStatelessSession(statelessSession ->
        {
            log.debug("🏛️ Created stateless session for bulk time data operations: {}", statelessSession.hashCode());
            
            return loadTimeRangeBatched(statelessSession, startYear, endYear, totalDays);
        });
    })
    .onItem().invoke(() ->
    {
        long duration = System.currentTimeMillis() - startTime;
        log.info("🎉 Time range loading completed in {}ms", duration);
        
        // Update partition bases after loading
        log.debug("🔧 Updating partition bases after time range loading");
        IGuiceContext.get(ActivityMasterService.class).updatePartitionBases();
    })
    .onFailure().invoke(error ->
    {
        long duration = System.currentTimeMillis() - startTime;
        log.error("💥 Time range loading failed after {}ms: {}", duration, error.getMessage(), error);
    })
    .map(result -> null); // Convert to Void for interface compliance
}

private Uni<Void> loadTimeRangeBatched(Mutiny.StatelessSession statelessSession, int startYear, int endYear, long totalDays)
{
    log.info("🔄 Starting batched time range loading with stateless session {}", statelessSession.hashCode());
    
    List<Uni<Void>> yearOperations = new ArrayList<>();
    
    for (int year = startYear; year <= endYear; year++)
    {
        final int currentYear = year;
        
        yearOperations.add(
            loadYearReactive(statelessSession, currentYear)
                .onItem().invoke(() ->
                {
                    log.debug("✅ Year {} loaded successfully with stateless session", currentYear);
                    
                    // Progress tracking every year
                    logProgress("Time Loading", String.format("Loaded year %d", currentYear));
                })
                .onFailure().invoke(error ->
                {
                    log.error("❌ Failed to load year {} with stateless session: {}", 
                        currentYear, error.getMessage(), error);
                })
        );
    }
    
    log.info("🔄 Running {} year loading operations in parallel with stateless session", yearOperations.size());
    
    return Uni.combine()
        .all()
        .unis(yearOperations)
        .discardItems()
        .onItem().invoke(() ->
        {
            log.info("🎉 All {} years loaded successfully with stateless session {}", 
                yearOperations.size(), statelessSession.hashCode());
        })
        .onFailure().invoke(error ->
        {
            log.error("💥 Failed to load some years with stateless session {}: {}", 
                statelessSession.hashCode(), error.getMessage(), error);
        })
        .map(result -> null);
}

private Uni<Void> loadYearReactive(Mutiny.StatelessSession statelessSession, int year)
{
    log.debug("📅 Loading year {} with stateless session {}", year, statelessSession.hashCode());
    
    return Uni.createFrom().item(() ->
    {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        
        List<Uni<Boolean>> dayOperations = new ArrayList<>();
        
        // Process each day in the year
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate))
        {
            final LocalDate dateToProcess = currentDate;
            
            dayOperations.add(
                getDayReactive(statelessSession, Date.from(dateToProcess.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .onItem().invoke(success ->
                    {
                        if (!success)
                        {
                            log.warn("⚠️ Day {} processing returned false", dateToProcess);
                        }
                    })
                    .onFailure().invoke(error ->
                    {
                        log.error("❌ Failed to process day {}: {}", dateToProcess, error.getMessage(), error);
                    })
            );
            
            currentDate = currentDate.plusDays(1);
        }
        
        return dayOperations;
    })
    .chain(dayOperations ->
    {
        log.debug("🔄 Processing {} days for year {} with stateless session", dayOperations.size(), year);
        
        // Process days in batches to avoid overwhelming the database
        return processDaysInBatches(dayOperations, 100) // Process 100 days at a time
            .onItem().invoke(() ->
            {
                log.debug("✅ Year {} completed with {} days processed", year, dayOperations.size());
            });
    })
    .map(result -> null);
}

private Uni<Void> processDaysInBatches(List<Uni<Boolean>> dayOperations, int batchSize)
{
    List<Uni<Void>> batchOperations = new ArrayList<>();
    
    for (int i = 0; i < dayOperations.size(); i += batchSize)
    {
        final int batchStart = i;
        final int batchEnd = Math.min(i + batchSize, dayOperations.size());
        final List<Uni<Boolean>> batch = dayOperations.subList(batchStart, batchEnd);
        
        batchOperations.add(
            Uni.combine()
                .all()
                .unis(batch)
                .discardItems()
                .onItem().invoke(() ->
                {
                    log.debug("📊 Batch {}-{} completed ({} days)", batchStart + 1, batchEnd, batch.size());
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

### 3. Reactive getDay() Implementation

**CRITICAL**: Must remain `boolean` return type for public API compatibility:

```java
@Override
public boolean getDay(Date date)
{
    log.debug("🔍 Getting day for date: {} with internal session management", date);
    
    // Internal reactive implementation with .await() to maintain boolean signature
    return getDayReactive(date)
        .await().atMost(Duration.ofMinutes(1));
}

private Uni<Boolean> getDayReactive(Date date)
{
    return sessionFactory.withStatelessSession(statelessSession ->
    {
        log.debug("🏛️ Created stateless session for day operation: {}", statelessSession.hashCode());
        
        return getDayReactive(statelessSession, date);
    });
}

private Uni<Boolean> getDayReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    log.debug("🔍 Searching for day with date: {} using stateless session {}", date, statelessSession.hashCode());
    
    // Use EntityAssist query builder with stateless session
    return new Days().builder(statelessSession)
        .find(Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(date)))
        .get()
        .onItem().invoke(existingDay ->
        {
            if (existingDay.isPresent())
            {
                log.debug("✅ Day found for date: {} with stateless session", date);
            }
            else
            {
                log.debug("❌ Day not found for date: {}, will create new day", date);
            }
        })
        .onFailure().invoke(error ->
        {
            log.error("💥 Failed to search for day {}: {}", date, error.getMessage(), error);
        })
        .chain(existingDay ->
        {
            if (existingDay.isPresent())
            {
                return Uni.createFrom().item(true);
            }
            else
            {
                // Create new day if not found
                return createDayReactive(statelessSession, date)
                    .onItem().invoke(created ->
                    {
                        log.debug("✅ Day created for date: {} with stateless session", date);
                    })
                    .onFailure().invoke(error ->
                    {
                        log.error("❌ Failed to create day for {}: {}", date, error.getMessage(), error);
                    });
            }
        });
}

private Uni<Boolean> createDayReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    log.debug("🔧 Creating new day for date: {} with stateless session {}", date, statelessSession.hashCode());
    
    return Uni.createFrom().item(() ->
    {
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
            .chain(dayNameEntity ->
            {
                newDay.setDayNameID(dayNameEntity);
                
                // Set additional day properties
                newDay.setDayOfWeekSort((short) gc.get(Calendar.DAY_OF_WEEK));
                newDay.setDayOfMonthSort((short) gc.get(Calendar.DAY_OF_MONTH));
                newDay.setDayOfYearSort((short) gc.get(Calendar.DAY_OF_YEAR));
                newDay.setDayIsPublicHoliday((short) 0);
                
                log.debug("💾 Persisting new day with ID: {} using stateless session", newDay.getId());
                
                // Insert using stateless session for bulk performance
                return statelessSession.insert(newDay)
                    .onItem().invoke(persisted ->
                    {
                        log.debug("✅ Day persisted successfully with ID: {}", newDay.getId());
                    })
                    .onFailure().invoke(error ->
                    {
                        log.error("❌ Failed to persist day with ID {}: {}", 
                            newDay.getId(), error.getMessage(), error);
                    })
                    .map(persisted -> true);
            });
    })
    .onFailure().invoke(error ->
    {
        log.error("💥 Failed to create day for {}: {}", date, error.getMessage(), error);
    })
    .onFailure().recoverWithItem(() ->
    {
        log.warn("🔄 Recovering from day creation failure, returning false");
        return false;
    });
}

private Uni<DayNames> getDayNameReactive(Mutiny.StatelessSession statelessSession, String dayName)
{
    log.debug("🔍 Searching for day name: {} with stateless session", dayName);
    
    return new DayNames().builder(statelessSession)
        .where(DayNames_.dayName, Equals, dayName)
        .get()
        .onItem().invoke(dayNameOpt ->
        {
            if (dayNameOpt.isPresent())
            {
                log.debug("✅ Day name found: {}", dayName);
            }
            else
            {
                log.debug("❌ Day name not found: {}, will create new", dayName);
            }
        })
        .chain(dayNameOpt ->
        {
            if (dayNameOpt.isPresent())
            {
                return Uni.createFrom().item(dayNameOpt.get());
            }
            else
            {
                // Create new day name if not found
                return createDayNameReactive(statelessSession, dayName);
            }
        });
}

private Uni<DayNames> createDayNameReactive(Mutiny.StatelessSession statelessSession, String dayName)
{
    log.debug("🔧 Creating new day name: {} with stateless session", dayName);
    
    return Uni.createFrom().item(() ->
    {
        DayNames newDayName = new DayNames();
        newDayName.setDayName(dayName);
        
        return statelessSession.insert(newDayName)
            .onItem().invoke(persisted ->
            {
                log.debug("✅ Day name created successfully: {}", dayName);
            })
            .onFailure().invoke(error ->
            {
                log.error("❌ Failed to create day name {}: {}", dayName, error.getMessage(), error);
            })
            .map(persisted -> newDayName);
    });
}
```

### 4. Supporting Method Migrations

```java
// Year retrieval with internal session management
public Years getYear(Date date)
{
    log.debug("🔍 Getting year for date: {} with internal session management", date);
    
    return getYearReactive(date)
        .await().atMost(Duration.ofMinutes(1));
}

private Uni<Years> getYearReactive(Date date)
{
    return sessionFactory.withStatelessSession(statelessSession ->
    {
        log.debug("🏛️ Created stateless session for year operation: {}", statelessSession.hashCode());
        
        int yearId = Integer.parseInt(YearIDFormat.getSimpleDateFormat().format(date));
        
        return new Years().builder(statelessSession)
            .find(yearId)
            .get()
            .onItem().invoke(yearOpt ->
            {
                if (yearOpt.isPresent())
                {
                    log.debug("✅ Year found for date: {}", date);
                }
                else
                {
                    log.debug("❌ Year not found for date: {}, will create", date);
                }
            })
            .chain(yearOpt ->
            {
                if (yearOpt.isPresent())
                {
                    return Uni.createFrom().item(yearOpt.get());
                }
                else
                {
                    return createYearReactive(statelessSession, date, yearId);
                }
            });
    });
}

private Uni<Years> createYearReactive(Mutiny.StatelessSession statelessSession, Date date, int yearId)
{
    log.debug("🔧 Creating new year with ID: {} using stateless session", yearId);
    
    return Uni.createFrom().item(() ->
    {
        Years newYear = new Years();
        newYear.setId(yearId);
        
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        
        newYear.setYearNumber((short) gc.get(Calendar.YEAR));
        newYear.setYearDescription(YearNameFormat.getSimpleDateFormat().format(date));
        
        return statelessSession.insert(newYear)
            .onItem().invoke(persisted ->
            {
                log.debug("✅ Year created successfully with ID: {}", yearId);
            })
            .onFailure().invoke(error ->
            {
                log.error("❌ Failed to create year with ID {}: {}", yearId, error.getMessage(), error);
            })
            .map(persisted -> newYear);
    });
}
```

## Performance Optimization for Large Datasets

### Stateless Session Benefits

```java
// Optimized for bulk operations
private Uni<Void> bulkInsertDays(List<Days> days)
{
    return sessionFactory.withStatelessSession(statelessSession ->
    {
        log.info("🚀 Starting bulk insert of {} days with stateless session {}", days.size(), statelessSession.hashCode());
        
        List<Uni<Void>> insertOperations = new ArrayList<>();
        
        // Process in batches for optimal performance
        int batchSize = 1000;
        for (int i = 0; i < days.size(); i += batchSize)
        {
            final int batchStart = i;
            final int batchEnd = Math.min(i + batchSize, days.size());
            final List<Days> batch = days.subList(batchStart, batchEnd);
            
            insertOperations.add(
                Uni.createFrom().item(batch)
                    .chain(batchDays ->
                    {
                        List<Uni<Void>> batchInserts = new ArrayList<>();
                        
                        for (Days day : batchDays)
                        {
                            batchInserts.add(
                                statelessSession.insert(day)
                                    .map(result -> null)
                                    .onFailure().invoke(error ->
                                    {
                                        log.warn("⚠️ Failed to insert day {}: {}", 
                                            day.getId(), error.getMessage());
                                    })
                            );
                        }
                        
                        return Uni.combine().all().unis(batchInserts).discardItems();
                    })
                    .onItem().invoke(() ->
                    {
                        log.debug("📊 Batch {}-{} inserted ({} days)", 
                            batchStart + 1, batchEnd, batch.size());
                    })
                    .map(result -> null)
            );
        }
        
        return Uni.combine()
            .all()
            .unis(insertOperations)
            .discardItems()
            .onItem().invoke(() ->
            {
                log.info("🎉 Bulk insert completed: {} days inserted with stateless session", days.size());
            })
            .map(result -> null);
    });
}
```

## Enhanced Logging Standards for TimeSystem

### Comprehensive Logging with Session Context

```java
// TimeSystem-specific logging patterns with stateless session tracking
log.info("🚀 TimeSystem operation starting with stateless session {}", statelessSession.hashCode());
log.debug("📅 Processing date: {} with stateless session {}", date, statelessSession.hashCode());
log.debug("💾 Bulk inserting {} entities with stateless session {}", entityCount, statelessSession.hashCode());
log.info("🎉 TimeSystem operation completed in {}ms with {} entities processed", duration, entityCount);

// Progress tracking for large datasets
if ((processedCount + 1) % 1000 == 0)
{
    log.info("📊 TimeSystem progress: {}/{} entities processed ({}%)", 
        processedCount + 1, totalCount, ((processedCount + 1) * 100) / totalCount);
    logProgress("Time Loading", String.format("Processed %d/%d entities", processedCount + 1, totalCount));
}

// Error handling with stateless session context
.onFailure().invoke(error ->
{
    log.error("💥 TimeSystem operation failed with stateless session {}: {}", 
        statelessSession.hashCode(), error.getMessage(), error);
    log.debug("🔍 Failure context - Operation: {}, Date: {}, Session: {}", 
        operationName, date, statelessSession.hashCode());
})
```

## Migration Checklist

### ✅ Required Modifications

1. **Public API Preservation**:
   - [ ] `loadTimeRange(int, int)` maintains void return type
   - [ ] `getDay(Date)` maintains boolean return type
   - [ ] All existing callers continue to work without changes

2. **Internal Reactive Implementation**:
   - [ ] All internal operations use Uni<T> patterns
   - [ ] Stateless sessions for bulk operations
   - [ ] EntityAssist query builders throughout
   - [ ] Comprehensive logging with session tracking

3. **Performance Optimization**:
   - [ ] Batched processing for large datasets
   - [ ] Stateless session usage for bulk inserts
   - [ ] Progress tracking for long-running operations
   - [ ] Error recovery for partial failures

4. **CRTP Pattern Compliance**:
   - [ ] TimeSystem extends ActivityMasterDefaultSystem<TimeSystem>
   - [ ] TimeSystem implements IActivityMasterSystem<TimeSystem>
   - [ ] All helper classes follow CRTP where applicable

5. **Session Management**:
   - [ ] External sessions for system registration/defaults
   - [ ] Internal stateless sessions for time operations
   - [ ] Clear documentation of session management exception

### ❌ Migration Restrictions

1. **Cannot Change**:
   - Public method signatures
   - Return types of public methods
   - Method visibility (public/private)
   - Interface implementations

2. **Must Maintain**:
   - Backward compatibility with existing callers
   - Performance characteristics for large datasets
   - Thread safety for concurrent operations

## Testing Strategy

No tests should be created as we are still migrating

This migration guide ensures TimeSystem maintains its public API while gaining the benefits of reactive programming and optimized bulk data processing through stateless sessions.
