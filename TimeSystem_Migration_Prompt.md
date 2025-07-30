# TimeSystem Migration Prompt

## Overview

This document provides detailed instructions for migrating the TimeSystem to use Mutiny.Session for transaction and session management. The TimeSystem is unique in ActivityMaster as it's the **only system that creates and manages its own sessions** for time-related operations.

## Requirements

1. **In-place Migration**: Implement an in-place migration where a session is started at `createYear` and carried through the chain of method calls.
2. **Method Signature Updates**: Any methods not impacted by the `createYear` process should take a Mutiny.Session as the first parameter.
3. **Public API Preservation**: The public methods defined in the ITimeSystem interface must maintain their current signatures:
   - `void loadTimeRange(int startYear, int endYear)`
   - `boolean getDay(Date date)`
   - `void createTime()`

## Migration Steps

### 1. Add SessionFactory Injection

Add a SessionFactory injection to the TimeSystem class:

```java
@Inject
private Mutiny.SessionFactory sessionFactory;
```

### 2. Modify createYear Method

Update the `createYear` method to accept a Mutiny.Session parameter and use it for database operations:

```java
@Transactional()
private Years createYear(Mutiny.Session session, Date date)
{
    Years year = new Years().setId(Short.parseShort(YearIDFormat.getSimpleDateFormat()
                                                            .format(date)));
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);
    gc.set(Calendar.MONTH, 1);
    year.setLeapYearFlag((short) (gc.getActualMaximum(Calendar.DAY_OF_MONTH) == 29 ? 1 : 0));
    year.setLastYearID((short) getLastYearID(session, date));
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
    year.persist(session);
    year.builder(session)
        .getEntityManager().flush();
    return year;
}
```

### 3. Update getYear Method

Modify the `getYear` method to create a session if one doesn't exist and pass it to `createYear`:

```java
public Years getYear(Date date)
{
    Years year = null;
    try
    {
        year = getYearFromID(date);
    }
    catch (Exception ex)
    {
        // Logger.getLogger(TimeLord.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (year == null)
    {
        logProgress("Time Lord", "Creating Year [" +
                                 YearIDFormat.getSimpleDateFormat()
                                             .format(date) +
                                 "]");
        
        // Create a new session for the createYear chain
        sessionFactory.withTransaction(session -> {
            year = createYear(session, date);
            return Uni.createFrom().item(year);
        }).await().indefinitely();
    }
    return year;
}

// Overloaded method that accepts a session
public Years getYear(Mutiny.Session session, Date date)
{
    Years year = null;
    try
    {
        year = getYearFromID(session, date);
    }
    catch (Exception ex)
    {
        // Logger.getLogger(TimeLord.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (year == null)
    {
        logProgress("Time Lord", "Creating Year [" +
                                 YearIDFormat.getSimpleDateFormat()
                                             .format(date) +
                                 "]");
        year = createYear(session, date);
    }
    return year;
}
```

### 4. Update Call Chain Methods

Modify all methods in the call chain to accept and pass along the Mutiny.Session parameter:

#### getLastYearID Method

```java
@Transactional()
private int getLastYearID(Mutiny.Session session, Date date)
{
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);
    gc.add(Calendar.YEAR, -1);
    return Integer.parseInt(YearIDFormat.getSimpleDateFormat()
                                    .format(gc.getTime()));
}
```

#### getYearFromID Method

```java
@Transactional()
private Years getYearFromID(Mutiny.Session session, Date date)
{
    return new Years().builder(session)
                  .find(Short.parseShort(YearIDFormat.getSimpleDateFormat()
                                                     .format(date)))
                  .get()
                  .orElse(null);
}
```

#### createQuarter Method

```java
@Transactional()
private Quarters createQuarter(Mutiny.Session session, Date date)
{
    Quarters quarter = new Quarters(getQuarterID(date));
    quarter.setLastYearID((short) getLastYearQuarterID(date));
    quarter.setLastQuarterID((short) getLastQuarterID(date));
    quarter.setQuarterDescription("Q" +
                              getQuarterNumber(date) +
                              " " +
                              YearIDFormat.getSimpleDateFormat()
                                          .format(date));
    quarter.setQuarterGraphDescription("Q" +
                                   getQuarterNumber(date) +
                                   " - " +
                                   YearIDFormat.getSimpleDateFormat()
                                               .format(date));
    quarter.setQuarterGridDescription("Quarter " +
                                  getQuarterNumber(date) +
                                  " - " +
                                  YearIDFormat.getSimpleDateFormat()
                                              .format(date));
    quarter.setId(getQuarterID(date));
    quarter.setQuarterInYear(getQuarterNumber(date));
    quarter.setQuarterQQMMDescription("Q" +
                                  DoubleDigits.formatter()
                                              .format(getQuarterNumber(date)) +
                                  " " +
                                  MonthNumberFormat.getSimpleDateFormat()
                                                   .format(date));
    quarter.setQuarterSmallDescription("Quart " +
                                   getQuarterNumber(date) +
                                   " " +
                                   YearIDFormat.getSimpleDateFormat()
                                               .format(date));
    quarter.setQuarterYYMMDescription("'" +
                                  YearShortFormat.getSimpleDateFormat()
                                                 .format(date) +
                                  " Q" +
                                  DoubleDigits.formatter()
                                              .format(getQuarterNumber(date)));
    quarter.setQuarterYearDescription(quarter.getQuarterDescription());
    quarter.setYearID(getYear(session, date));
    return quarter;
}
```

#### getQuarter Method

```java
@Transactional()
public Quarters getQuarter(@CacheKey Date date)
{
    Quarters quarter = null;
    try
    {
        quarter = getQuarterFromID(date);
    }
    catch (Exception ex)
    {
        //   Logger.getLogger(TimeLord.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (quarter == null)
    {
        // Create a new session for the createQuarter chain
        sessionFactory.withTransaction(session -> {
            quarter = createQuarter(session, date);
            quarter.persist(session);
            quarter.builder(session)
                .getEntityManager().flush();
            return Uni.createFrom().item(quarter);
        }).await().indefinitely();
    }
    return quarter;
}

// Overloaded method that accepts a session
@Transactional()
public Quarters getQuarter(Mutiny.Session session, @CacheKey Date date)
{
    Quarters quarter = null;
    try
    {
        quarter = getQuarterFromID(session, date);
    }
    catch (Exception ex)
    {
        //   Logger.getLogger(TimeLord.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (quarter == null)
    {
        quarter = createQuarter(session, date);
        quarter.persist(session);
        quarter.builder(session)
            .getEntityManager().flush();
    }
    return quarter;
}
```

Continue this pattern for all methods in the call chain:
- getQuarterFromID
- getLastQuarterID
- createMonth
- getMonth
- getMonthFromID
- createWeek
- getWeek
- getWeekFromID
- createDay
- getDay
- getDayFromID

### 5. Update Public API Methods

For the public methods defined in the ITimeSystem interface, maintain their current signatures but internally create and manage sessions:

#### loadTimeRange Method

```java
@Override
public void loadTimeRange(int startYear, int endYear)
{
    JobService.INSTANCE
              .setMaxQueueCount("TimeRangeLoading", 500);
    
    logProgress("Starting Time Load", "Time load is starting from " + startYear + " + to " + endYear);
    GregorianCalendar startYearGC = new GregorianCalendar();
    startYearGC.set(Calendar.YEAR, startYear);
    startYearGC.set(Calendar.MONTH, 0);
    startYearGC.set(Calendar.DATE, 1);
    
    GregorianCalendar endGC = new GregorianCalendar();
    endGC.set(Calendar.YEAR, endYear);
    endGC.set(Calendar.MONTH, 11);
    endGC.set(Calendar.DATE, 31);
    
    // Create a session for the entire operation
    sessionFactory.withTransaction(session -> {
        Uni<Void> uni = Uni.createFrom().voidItem();
        
        while (startYearGC.getTime().getTime() <= endGC.getTime().getTime())
        {
            final Date currentDate = startYearGC.getTime();
            uni = uni.chain(ignored -> {
                return Uni.createFrom().item(() -> {
                    getDay(session, currentDate);
                    return null;
                });
            });
            
            startYearGC.add(Calendar.DATE, 1);
        }
        
        return uni;
    }).await().indefinitely();
}
```

#### getDay Method (public API)

```java
@Override
public boolean getDay(Date date)
{
    // Create a session for this operation
    return sessionFactory.withTransaction(session -> {
        return Uni.createFrom().item(getDay(session, date));
    }).await().indefinitely();
}

// Internal method that accepts a session
public boolean getDay(Mutiny.Session session, Date date)
{
    // Implementation that uses the provided session
    // ...
}
```

#### createTime Method

```java
@Override
public void createTime()
{
    // Create a session for this operation
    sessionFactory.withTransaction(session -> {
        // Implementation that uses the session
        // ...
        return Uni.createFrom().voidItem();
    }).await().indefinitely();
}
```

### 6. Update Other Methods

For any remaining methods in TimeSystem that are not part of the createYear call chain, add an overloaded version that accepts a Mutiny.Session as the first parameter:

```java
// Original method
public ReturnType methodName(ParamType param)
{
    // Create a session for this operation
    return sessionFactory.withTransaction(session -> {
        return Uni.createFrom().item(methodName(session, param));
    }).await().indefinitely();
}

// Overloaded method that accepts a session
public ReturnType methodName(Mutiny.Session session, ParamType param)
{
    // Implementation that uses the provided session
    // ...
}
```

## Testing

After implementing the changes, test the TimeSystem to ensure:

1. The public API methods (loadTimeRange, getDay, createTime) work correctly
2. The session is properly passed through the call chain from createYear
3. All methods that accept a Mutiny.Session parameter use it correctly for database operations
4. Performance is maintained, especially for bulk operations

## Conclusion

This migration will ensure that the TimeSystem properly manages sessions and transactions while maintaining backward compatibility with existing code. The createYear method will start a session that is carried through the chain of method calls, and any methods not impacted by the createYear process will accept a Mutiny.Session as the first parameter.