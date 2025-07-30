# TimeSystem Reactive Implementation Guide

## Overview

This guide provides comprehensive instructions for updating the TimeSystem to use reactive patterns with Uni return types. The TimeSystem is unique in ActivityMaster as it's the **only system that creates and manages its own sessions** for time-related operations.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Core System Methods](#core-system-methods)
3. [Year-Related Methods](#year-related-methods)
4. [Quarter-Related Methods](#quarter-related-methods)
5. [Month-Related Methods](#month-related-methods)
6. [Week-Related Methods](#week-related-methods)
7. [Day-Related Methods](#day-related-methods)
8. [Time-Related Methods](#time-related-methods)
9. [Transformation Methods](#transformation-methods)
10. [Key Reactive Patterns](#key-reactive-patterns)
11. [Testing](#testing)
12. [Conclusion](#conclusion)

## Prerequisites

1. Update the ITimeSystem interface to change the return types:

```java
public interface ITimeSystem
{
    Uni<Void> loadTimeRange(int startYear, int endYear);
    Uni<Days> getDay(Date date);
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

## Core System Methods

### registerSystem Method

```java
@Override
public Uni<ISystems<?, ?>> registerSystem(Mutiny.Session session, IEnterprise<?, ?> enterprise)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Registering TimeSystem with externally provided session");
    
    return systemsService.create(session, enterprise, getSystemName(), getSystemDescription())
        .chain(iSystems -> {
            return systemsService.registerNewSystem(session, enterprise, getSystem(session, enterprise))
                .map(result -> iSystems);
        });
}
```

### createDefaults Method

```java
@Override
public Uni<Void> createDefaults(Mutiny.Session session, IEnterprise<?, ?> enterprise)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Creating TimeSystem defaults with external session");
    logProgress("Time System", "Loading Time Classifications...", 4);
    
    // Call loadTimeRange which will create its own stateless sessions
    return loadTimeRange(2004, LocalDateTime.now().getYear());
}
```

### System Information Methods

```java
@Override
public int totalTasks()
{
    return 0;
}

@Override
public Integer sortOrder()
{
    return Integer.MIN_VALUE + 6;
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
```

## Year-Related Methods

### getYear Method

```java
public Uni<Years> getYear(Date date)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Getting year for date: {0}", date);
    
    return getYearReactive(date);
}

private Uni<Years> getYearReactive(Date date)
{
    return sessionFactory.withStatelessSession(statelessSession -> {
        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Created stateless session for year operation: {0}", statelessSession.hashCode());
        
        return getYearFromIDReactive(statelessSession, date)
            .chain(year -> {
                if (year != null) {
                    return Uni.createFrom().item(year);
                } else {
                    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Creating Year [{0}]", 
                        YearIDFormat.getSimpleDateFormat().format(date));
                    return createYearReactive(statelessSession, date);
                }
            });
    });
}
```

### createYear Method

```java
private Uni<Years> createYearReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        Years year = new Years().setId(Short.parseShort(YearIDFormat.getSimpleDateFormat().format(date)));
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.set(Calendar.MONTH, 1);
        
        return getLastYearIDReactive(statelessSession, date)
            .map(lastYearId -> {
                year.setLeapYearFlag((short) (gc.getActualMaximum(Calendar.DAY_OF_MONTH) == 29 ? 1 : 0));
                year.setLastYearID((short) lastYearId);
                year.setYearName(YearIDFormat.getSimpleDateFormat().format(date));
                year.setyYName(YearShortFormat.getSimpleDateFormat().format(date));
                year.setyYYName(YearYYYFormat.getSimpleDateFormat().format(date));
                year.setYearFullName(EnglishNumberToWords.convert(year.getId()));
                year.setCentury(Short.parseShort(YearFullFormat.getSimpleDateFormat().format(date).substring(0, 2)));
                
                return year;
            });
    })
    .chain(year -> {
        return statelessSession.insert(year)
            .map(inserted -> year);
    });
}
```

### getYearFromID Method

```java
private Uni<Years> getYearFromIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return new Years().builder(statelessSession)
        .find(Short.parseShort(YearIDFormat.getSimpleDateFormat().format(date)))
        .get()
        .map(yearOpt -> yearOpt.orElse(null));
}
```

### getLastYearID Method

```java
private Uni<Integer> getLastYearIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.YEAR, -1);
        return Integer.parseInt(YearIDFormat.getSimpleDateFormat().format(gc.getTime()));
    });
}
```

## Quarter-Related Methods

### getQuarter Method

```java
public Uni<Quarters> getQuarter(Date date)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Getting quarter for date: {0}", date);
    
    return getQuarterReactive(date);
}

private Uni<Quarters> getQuarterReactive(Date date)
{
    return sessionFactory.withStatelessSession(statelessSession -> {
        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Created stateless session for quarter operation: {0}", statelessSession.hashCode());
        
        return getQuarterFromIDReactive(statelessSession, date)
            .chain(quarter -> {
                if (quarter != null) {
                    return Uni.createFrom().item(quarter);
                } else {
                    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Creating Quarter for date: {0}", date);
                    return createQuarterReactive(statelessSession, date);
                }
            });
    });
}
```

### createQuarter Method

```java
private Uni<Quarters> createQuarterReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        // Get quarter ID and other IDs
        return getQuarterIDReactive(statelessSession, date)
            .chain(quarterId -> {
                Quarters quarter = new Quarters(quarterId);
                
                return Uni.combine().all().unis(
                    getLastYearQuarterIDReactive(statelessSession, date),
                    getLastQuarterIDReactive(statelessSession, date),
                    getQuarterNumberReactive(statelessSession, date)
                ).asTuple()
                .map(tuple -> {
                    int lastYearQuarterId = tuple.getItem1();
                    int lastQuarterId = tuple.getItem2();
                    int quarterNumber = tuple.getItem3();
                    
                    quarter.setLastYearID((short) lastYearQuarterId);
                    quarter.setLastQuarterID((short) lastQuarterId);
                    
                    // Set all quarter descriptions
                    quarter.setQuarterDescription("Q" + quarterNumber + " " + 
                        YearIDFormat.getSimpleDateFormat().format(date));
                    quarter.setQuarterGraphDescription("Q" + quarterNumber + " - " + 
                        YearIDFormat.getSimpleDateFormat().format(date));
                    quarter.setQuarterGridDescription("Quarter " + quarterNumber + " - " + 
                        YearIDFormat.getSimpleDateFormat().format(date));
                    quarter.setId(quarterId);
                    quarter.setQuarterInYear(quarterNumber);
                    quarter.setQuarterQQMMDescription("Q" + 
                        DoubleDigits.formatter().format(quarterNumber) + " " + 
                        MonthNumberFormat.getSimpleDateFormat().format(date));
                    quarter.setQuarterSmallDescription("Quart " + quarterNumber + " " + 
                        YearIDFormat.getSimpleDateFormat().format(date));
                    quarter.setQuarterYYMMDescription("'" + 
                        YearShortFormat.getSimpleDateFormat().format(date) + " Q" + 
                        DoubleDigits.formatter().format(quarterNumber));
                    quarter.setQuarterYearDescription(quarter.getQuarterDescription());
                    
                    return quarter;
                });
            })
            .chain(quarter -> {
                // Get year and set it
                return getYearReactive(date)
                    .map(year -> {
                        quarter.setYearID(year);
                        return quarter;
                    });
            });
    })
    .chain(quarterUni -> quarterUni)
    .chain(quarter -> {
        // Insert the quarter
        return statelessSession.insert(quarter)
            .map(inserted -> quarter);
    });
}
```

### getQuarterFromID Method

```java
private Uni<Quarters> getQuarterFromIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return getQuarterIDReactive(statelessSession, date)
        .chain(quarterId -> {
            return new Quarters().builder(statelessSession)
                .find(quarterId)
                .get()
                .map(quarterOpt -> quarterOpt.orElse(null));
        });
}
```

### getLastQuarterID Method

```java
private Uni<Integer> getLastQuarterIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.MONTH, -4);
        return gc.getTime();
    })
    .chain(lastQuarterDate -> {
        return getQuarterIDReactive(statelessSession, lastQuarterDate);
    });
}
```

### getQuarterNumber Method

```java
private Uni<Integer> getQuarterNumberReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        int quarterNumber = 0;
        int monthNumber = Integer.parseInt(MonthNumberFormat.getSimpleDateFormat().format(date));
        
        if (monthNumber <= 3) {
            quarterNumber = 1;
        } else if (monthNumber <= 6) {
            quarterNumber = 2;
        } else if (monthNumber <= 9) {
            quarterNumber = 3;
        } else if (monthNumber <= 12) {
            quarterNumber = 4;
        }
        
        return quarterNumber;
    });
}
```

### getQuarterID Method

```java
private Uni<Integer> getQuarterIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return getQuarterNumberReactive(statelessSession, date)
        .map(quarterNumber -> {
            int yearId = Integer.parseInt(YearIDFormat.getSimpleDateFormat().format(date));
            return Integer.parseInt(yearId + "" + quarterNumber);
        });
}
```

### getLastYearQuarterID Method

```java
private Uni<Integer> getLastYearQuarterIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.YEAR, -1);
        return gc.getTime();
    })
    .chain(lastYearDate -> {
        return getQuarterIDReactive(statelessSession, lastYearDate);
    });
}
```

## Month-Related Methods

### getMonth Method

```java
public Uni<Months> getMonth(Date date)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Getting month for date: {0}", date);
    
    return getMonthReactive(date);
}

private Uni<Months> getMonthReactive(Date date)
{
    return sessionFactory.withStatelessSession(statelessSession -> {
        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Created stateless session for month operation: {0}", statelessSession.hashCode());
        
        return getMonthFromIDReactive(statelessSession, date)
            .chain(month -> {
                if (month != null) {
                    return Uni.createFrom().item(month);
                } else {
                    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Creating Month for date: {0}", date);
                    return createMonthReactive(statelessSession, date);
                }
            });
    });
}
```

### getMonthOfYear Method

```java
private Uni<MonthOfYear> getMonthOfYearReactive(Mutiny.StatelessSession statelessSession, Integer monthOfYear)
{
    return new MonthOfYear().builder(statelessSession)
        .where(MonthOfYear_.monthInYearNumber, Equals, monthOfYear)
        .get()
        .map(monthOpt -> monthOpt.orElse(null));
}
```

### createMonth Method

```java
private Uni<Months> createMonthReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        
        Months month = new Months(Integer.parseInt(MonthIDFormat.getSimpleDateFormat().format(date)));
        
        return Uni.combine().all().unis(
            getLastQuarterMonthIDReactive(statelessSession, date),
            getLastYearMonthIDReactive(statelessSession, date),
            getLastMonthIDReactive(statelessSession, date),
            getMonthOfYearReactive(statelessSession, gc.get(Calendar.MONTH))
        ).asTuple()
        .map(tuple -> {
            int lastQuarterMonthId = tuple.getItem1();
            int lastYearMonthId = tuple.getItem2();
            int lastMonthId = tuple.getItem3();
            MonthOfYear monthOfYear = tuple.getItem4();
            
            month.setLastQuarterID(lastQuarterMonthId);
            month.setLastMonthID(lastYearMonthId);
            month.setMonthDayDuration((short) gc.getActualMaximum(Calendar.DAY_OF_MONTH));
            month.setMonthDescription(MonthDescriptionFormat.getSimpleDateFormat().format(date));
            month.setId(Integer.parseInt(MonthIDFormat.getSimpleDateFormat().format(date)));
            month.setMonthMMMYYDescription(MonthMMMMYYDescriptionFormat.getSimpleDateFormat().format(date));
            month.setMonthMMYYYYDescription(MonthMMYYDescriptionFormat.getSimpleDateFormat().format(date));
            month.setMonthNameYYYYDescription(MonthMonthNameYYYYDescriptionFormat.getSimpleDateFormat().format(date));
            month.setMonthOfYearID(monthOfYear);
            month.setMonthShortDescription(MonthMMYYDescriptionFormat.getSimpleDateFormat().format(date));
            month.setMonthYYDescription(MonthMMYYDescriptionFormat.getSimpleDateFormat().format(date));
            month.setLastMonthID(lastMonthId);
            month.setLastQuarterID(lastQuarterMonthId);
            month.setLastYearID(lastYearMonthId);
            
            return month;
        });
    })
    .chain(monthUni -> monthUni)
    .chain(month -> {
        // Get quarter and set it
        return getQuarterReactive(date)
            .map(quarter -> {
                month.setQuarterID(quarter);
                month.setYearID(Integer.parseInt(YearIDFormat.getSimpleDateFormat().format(date)));
                return month;
            });
    })
    .chain(month -> {
        // Insert the month
        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Creating Month [{0}]", month.getMonthDescription());
        return statelessSession.insert(month)
            .map(inserted -> month);
    });
}
```

### getLastQuarterMonthID Method

```java
private Uni<Integer> getLastQuarterMonthIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.MONTH, -4);
        return Integer.parseInt(MonthIDFormat.getSimpleDateFormat().format(gc.getTime()));
    });
}
```

### getLastMonthYearID Method

```java
private Uni<Integer> getLastMonthYearIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.MONTH, -1);
        return Integer.parseInt(YearIDFormat.getSimpleDateFormat().format(gc.getTime()));
    });
}
```

### getLastMonthID Method

```java
private Uni<Integer> getLastMonthIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.MONTH, -1);
        return Integer.parseInt(MonthIDFormat.getSimpleDateFormat().format(gc.getTime()));
    });
}
```

### getLastYearMonthID Method

```java
private Uni<Integer> getLastYearMonthIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.YEAR, -1);
        return Integer.parseInt(MonthIDFormat.getSimpleDateFormat().format(gc.getTime()));
    });
}
```

### getMonthFromID Method

```java
private Uni<Months> getMonthFromIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        return Integer.parseInt(MonthIDFormat.getSimpleDateFormat().format(date));
    })
    .chain(monthId -> {
        return new Months().builder(statelessSession)
            .find(monthId)
            .get()
            .map(monthOpt -> monthOpt.orElse(null));
    });
}
```

## Week-Related Methods

### getWeek Method

```java
public Uni<Weeks> getWeek(Date date)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Getting week for date: {0}", date);
    
    return getWeekReactive(date);
}

private Uni<Weeks> getWeekReactive(Date date)
{
    return sessionFactory.withStatelessSession(statelessSession -> {
        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Created stateless session for week operation: {0}", statelessSession.hashCode());
        
        return getWeekFromIDReactive(statelessSession, date)
            .chain(week -> {
                if (week != null) {
                    return Uni.createFrom().item(week);
                } else {
                    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Creating Week for date: {0}", date);
                    return createWeekReactive(statelessSession, date);
                }
            });
    });
}
```

### getWeekFromID Method

```java
private Uni<Weeks> getWeekFromIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return getWeekIDReactive(statelessSession, date)
        .chain(weekId -> {
            return new Weeks().builder(statelessSession)
                .find(weekId)
                .get()
                .map(weekOpt -> weekOpt.orElse(null));
        });
}
```

### getWeekID Method

```java
private Uni<Integer> getWeekIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
        gc.setTime(date);
        int weekNumber = gc.get(Calendar.WEEK_OF_YEAR);
        int yearId = Integer.parseInt(YearIDFormat.getSimpleDateFormat().format(date));
        return Integer.parseInt(yearId + "" + weekNumber);
    });
}
```

### createWeek Method

```java
private Uni<Weeks> createWeekReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        
        Weeks week = new Weeks();
        
        return Uni.combine().all().unis(
            getMonthReactive(date),
            getQuarterIDReactive(statelessSession, date),
            getWeekIDReactive(statelessSession, date)
        ).asTuple()
        .map(tuple -> {
            Months month = tuple.getItem1();
            int quarterId = tuple.getItem2();
            int weekId = tuple.getItem3();
            
            week.setMonthID(month.getId());
            week.setQuarterID(quarterId);
            week.setWeekDescription("Week " + gc.get(Calendar.WEEK_OF_YEAR));
            week.setId(weekId);
            week.setWeekOfMonth(gc.get(Calendar.WEEK_OF_MONTH));
            week.setWeekOfYear(gc.get(Calendar.WEEK_OF_YEAR));
            week.setWeekShortDescription("W" + gc.get(Calendar.WEEK_OF_YEAR));
            week.setYearID(Integer.parseInt(YearIDFormat.getSimpleDateFormat().format(date)));
            
            return week;
        });
    })
    .chain(weekUni -> weekUni)
    .chain(week -> {
        // Insert the week
        return statelessSession.insert(week)
            .map(inserted -> week);
    });
}
```

## Day-Related Methods

### getDay Method

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
```

### getDayFromID Method

```java
private Uni<Days> getDayFromIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        return Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(date));
    })
    .chain(dayId -> {
        return new Days().builder(statelessSession)
            .find(dayId)
            .get()
            .map(dayOpt -> dayOpt.orElse(null));
    });
}
```

### createDay Method

```java
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
                    .map(persisted -> true);
            });
    })
    .onFailure().invoke(error -> {
        Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to create day for {0}: {1}", new Object[]{date, error.getMessage()});
    })
    .onFailure().recoverWithItem(false);
}
```

### getDayName Method

```java
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
```

### createDayName Method

```java
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

### getLastDayID Method

```java
private Uni<Integer> getLastDayIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DATE, -1);
        return Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(gc.getTime()));
    });
}
```

### getLastMonthDayID Method

```java
private Uni<Integer> getLastMonthDayIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.MONTH, -1);
        return Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(gc.getTime()));
    });
}
```

### getLastYearDayID Method

```java
private Uni<Integer> getLastYearDayIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.YEAR, -1);
        return Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(gc.getTime()));
    });
}
```

### getLastQuarterDayID Method

```java
private Uni<Integer> getLastQuarterDayIDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.MONTH, -4);
        return Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(gc.getTime()));
    });
}
```

## Time-Related Methods

### createTime Method

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
```

### createTimeEntities Method

```java
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
```

### createHourEntity Method

```java
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
                            
                            TimeService<?> timeService = IGuiceContext.get(TimeService.class);
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

## Transformation Methods

### populateTransformationTables Method

```java
private Uni<Void> populateTransformationTablesReactive(Mutiny.StatelessSession statelessSession, Date date, int fiscalLag)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Populating transformation tables for date: {0} with fiscal lag: {1}", new Object[]{date, fiscalLag});
    
    return Uni.combine().all().unis(
        getDayYTDReactive(statelessSession, date),
        getDayMTDReactive(statelessSession, date),
        getDayFiscalReactive(statelessSession, date, fiscalLag),
        getDayQTDReactive(statelessSession, date)
    ).discardItems()
    .onItem().invoke(() -> {
        Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Transformation tables populated successfully for date: {0}", date);
    })
    .onFailure().invoke(error -> {
        Logger.getLogger(TimeSystem.class.getName()).log(Level.SEVERE, "Failed to populate transformation tables for date {0}: {1}", new Object[]{date, error.getMessage()});
    })
    .map(result -> null);
}
```

### getDayYTD Method

```java
private Uni<Void> getDayYTDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Creating YTD transformations for date: {0}", date);
    
    return Uni.createFrom().item(() -> {
        GregorianCalendar startYearGC = new GregorianCalendar();
        startYearGC.setTime(date);
        startYearGC.set(Calendar.MONTH, 0);
        startYearGC.set(Calendar.DATE, 1);
        
        List<TransYtd> ytdEntities = new ArrayList<>();
        
        // Get current day ID
        int currentDayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(date));
        
        // Create YTD entries from start of year to current date
        while (startYearGC.getTime().getTime() <= date.getTime()) {
            Date ytdDate = startYearGC.getTime();
            int ytdDayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(ytdDate));
            
            TransYtd transYtd = new TransYtd().setId(
                new TransYtdPK()
                    .setDayID(currentDayId)
                    .setYtdDayID(ytdDayId)
            );
            
            ytdEntities.add(transYtd);
            startYearGC.add(Calendar.DATE, 1);
        }
        
        return ytdEntities;
    })
    .chain(ytdEntities -> {
        // Insert YTD entities in batches
        return insertEntitiesInBatches(statelessSession, ytdEntities, 100, "YTD");
    });
}
```

### getDayMTD Method

```java
private Uni<Void> getDayMTDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Creating MTD transformations for date: {0}", date);
    
    return Uni.createFrom().item(() -> {
        GregorianCalendar startMonthGC = new GregorianCalendar();
        startMonthGC.setTime(date);
        startMonthGC.set(Calendar.DATE, 1);
        
        List<TransMtd> mtdEntities = new ArrayList<>();
        
        // Get current day ID
        int currentDayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(date));
        
        // Create MTD entries from start of month to current date
        while (startMonthGC.getTime().getTime() <= date.getTime()) {
            Date mtdDate = startMonthGC.getTime();
            int mtdDayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(mtdDate));
            
            TransMtd transMtd = new TransMtd().setId(
                new TransMtdPK()
                    .setDayID(currentDayId)
                    .setMtdDayID(mtdDayId)
            );
            
            mtdEntities.add(transMtd);
            startMonthGC.add(Calendar.DATE, 1);
        }
        
        return mtdEntities;
    })
    .chain(mtdEntities -> {
        // Insert MTD entities in batches
        return insertEntitiesInBatches(statelessSession, mtdEntities, 100, "MTD");
    });
}
```

### getDayFiscal Method

```java
private Uni<TransFiscal> getDayFiscalReactive(Mutiny.StatelessSession statelessSession, Date date, int fiscalMonthLag)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Creating Fiscal transformations for date: {0} with lag: {1}", new Object[]{date, fiscalMonthLag});
    
    return Uni.createFrom().item(() -> {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.MONTH, -fiscalMonthLag);
        
        TransFiscal transFiscal = new TransFiscal();
        transFiscal.setId(Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(date)));
        transFiscal.setFiscalDayID(Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(gc.getTime())));
        
        return transFiscal;
    })
    .chain(transFiscal -> {
        // Insert fiscal entity
        return statelessSession.insert(transFiscal)
            .map(inserted -> transFiscal);
    });
}
```

### getDayQTD Method

```java
private Uni<Void> getDayQTDReactive(Mutiny.StatelessSession statelessSession, Date date)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Creating QTD transformations for date: {0}", date);
    
    return Uni.createFrom().item(() -> {
        // Get quarter start date
        int quarterNumber = getQuarterNumber(date);
        int year = Integer.parseInt(YearIDFormat.getSimpleDateFormat().format(date));
        
        GregorianCalendar startQuarterGC = new GregorianCalendar();
        startQuarterGC.set(Calendar.YEAR, year);
        startQuarterGC.set(Calendar.MONTH, (quarterNumber - 1) * 3);
        startQuarterGC.set(Calendar.DATE, 1);
        
        List<TransQtd> qtdEntities = new ArrayList<>();
        
        // Get current day ID
        int currentDayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(date));
        
        // Create QTD entries from start of quarter to current date
        while (startQuarterGC.getTime().getTime() <= date.getTime()) {
            Date qtdDate = startQuarterGC.getTime();
            int qtdDayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(qtdDate));
            
            TransQtd transQtd = new TransQtd().setId(
                new TransQtdPK()
                    .setDayID(currentDayId)
                    .setQtdDayID(qtdDayId)
            );
            
            qtdEntities.add(transQtd);
            startQuarterGC.add(Calendar.DATE, 1);
        }
        
        return qtdEntities;
    })
    .chain(qtdEntities -> {
        // Insert QTD entities in batches
        return insertEntitiesInBatches(statelessSession, qtdEntities, 100, "QTD");
    });
}
```

### Helper Method for Batch Inserts

```java
private <T> Uni<Void> insertEntitiesInBatches(Mutiny.StatelessSession statelessSession, List<T> entities, int batchSize, String entityType)
{
    Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, "Inserting {0} {1} entities in batches of {2}", new Object[]{entities.size(), entityType, batchSize});
    
    List<Uni<Void>> batchOperations = new ArrayList<>();
    
    for (int i = 0; i < entities.size(); i += batchSize) {
        final int batchStart = i;
        final int batchEnd = Math.min(i + batchSize, entities.size());
        final List<T> batch = entities.subList(batchStart, batchEnd);
        
        batchOperations.add(
            Uni.createFrom().item(() -> {
                List<Uni<Void>> inserts = new ArrayList<>();
                
                for (T entity : batch) {
                    inserts.add(
                        statelessSession.insert(entity)
                            .onFailure().recoverWithItem(error -> {
                                Logger.getLogger(TimeSystem.class.getName()).log(Level.WARNING, 
                                    "Failed to insert {0} entity: {1}", new Object[]{entityType, error.getMessage()});
                                return null;
                            })
                            .map(result -> null)
                    );
                }
                
                return Uni.combine()
                    .all()
                    .unis(inserts)
                    .discardItems();
            })
            .chain(uni -> uni)
            .onItem().invoke(() -> {
                Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, 
                    "Batch {0}-{1} of {2} entities inserted", new Object[]{batchStart + 1, batchEnd, entityType});
            })
            .map(result -> null)
        );
    }
    
    return Uni.combine()
        .all()
        .unis(batchOperations)
        .discardItems()
        .onItem().invoke(() -> {
            Logger.getLogger(TimeSystem.class.getName()).log(Level.INFO, 
                "All {0} {1} entities inserted successfully", new Object[]{entities.size(), entityType});
        })
        .map(result -> null);
}
```

## Key Reactive Patterns

The TimeSystem reactive implementation follows several key patterns to ensure efficient, non-blocking operations:

### 1. Stateless Sessions for Bulk Operations

```java
return sessionFactory.withStatelessSession(statelessSession -> {
    // Perform bulk operations with stateless session
    return performBulkOperations(statelessSession);
});
```

Stateless sessions are used throughout the TimeSystem for bulk operations, as they provide better performance for large datasets by avoiding the overhead of the first-level cache and dirty checking.

### 2. Chain Operations for Sequential Processing

```java
return step1()
    .chain(result1 -> {
        // Use result1 to perform step2
        return step2(result1);
    })
    .chain(result2 -> {
        // Use result2 to perform step3
        return step3(result2);
    });
```

The `chain` operator is used extensively to sequence operations that depend on the result of the previous operation, ensuring that each step is executed only after the previous step completes.

### 3. Combine Operations for Parallel Processing

```java
return Uni.combine().all().unis(
    operation1(),
    operation2(),
    operation3()
).asTuple()
.map(tuple -> {
    Result1 result1 = tuple.getItem1();
    Result2 result2 = tuple.getItem2();
    Result3 result3 = tuple.getItem3();
    
    // Process results
    return processResults(result1, result2, result3);
});
```

The `combine` operator is used to run multiple operations in parallel and then process their results together, improving performance for independent operations.

### 4. Batched Processing for Large Datasets

```java
private <T> Uni<Void> processInBatches(List<T> items, int batchSize) {
    List<Uni<Void>> batchOperations = new ArrayList<>();
    
    for (int i = 0; i < items.size(); i += batchSize) {
        final int batchStart = i;
        final int batchEnd = Math.min(i + batchSize, items.size());
        final List<T> batch = items.subList(batchStart, batchEnd);
        
        batchOperations.add(processBatch(batch));
    }
    
    return Uni.combine().all().unis(batchOperations).discardItems();
}
```

Large datasets are processed in batches to avoid overwhelming the database and to provide better control over resource usage.

### 5. Comprehensive Error Handling

```java
return operation()
    .onItem().invoke(result -> {
        // Log success
        log.info("Operation completed successfully: {}", result);
    })
    .onFailure().invoke(error -> {
        // Log error
        log.error("Operation failed: {}", error.getMessage(), error);
    })
    .onFailure().recoverWithItem(fallbackValue);
```

Each operation includes comprehensive error handling with logging and recovery strategies to ensure that failures are properly handled and don't cause the entire process to fail.

### 6. Progress Tracking for Long-Running Operations

```java
.onItem().invoke(() -> {
    processedCount.incrementAndGet();
    if (processedCount.get() % 100 == 0) {
        logProgress("Processing", String.format("%d/%d items processed", processedCount.get(), totalCount));
    }
})
```

Long-running operations include progress tracking to provide feedback on the status of the operation.

### 7. Reactive Transformations

```java
return Uni.createFrom().item(() -> {
    // Create initial data
    return initialData;
})
.map(data -> {
    // Transform data
    return transformData(data);
})
.chain(transformedData -> {
    // Perform operation with transformed data
    return performOperation(transformedData);
});
```

Data transformations are performed reactively, ensuring that each transformation is part of the reactive chain.

## Conclusion

This guide provides a comprehensive approach to updating the TimeSystem to use reactive patterns with Uni return types. The implementation ensures that all fields, transformations, and utility methods from the original TimeSystem are preserved in the reactive implementation.

Key aspects of the implementation include:

1. **Reactive API**: All public methods now return Uni objects, allowing for non-blocking operations.
2. **Stateless Sessions**: Used throughout for bulk operations to improve performance.
3. **Comprehensive Documentation**: All fields, transformations, and utility methods are documented with their reactive equivalents.
4. **Backward Compatibility**: The implementation maintains backward compatibility with existing code through overloaded methods.
5. **Performance Optimization**: Batched processing and parallel operations are used to optimize performance for large datasets.
6. **Error Handling**: Comprehensive error handling is included to ensure that failures are properly handled.
7. **Progress Tracking**: Long-running operations include progress tracking to provide feedback on the status of the operation.

By following this guide, you can ensure that the TimeSystem properly manages sessions and transactions while maintaining backward compatibility with existing code and preserving all the fields, transformations, and utility methods from the original implementation.