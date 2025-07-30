package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
import com.guicedee.activitymaster.fsdm.ActivityMasterService;
import com.guicedee.activitymaster.fsdm.TimeService;
import com.guicedee.activitymaster.fsdm.client.services.ISystemsService;
import com.guicedee.activitymaster.fsdm.client.services.administration.ActivityMasterDefaultSystem;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.enterprise.IEnterprise;
import com.guicedee.activitymaster.fsdm.client.services.builders.warehouse.systems.ISystems;
import com.guicedee.activitymaster.fsdm.client.services.systems.IActivityMasterSystem;
import com.guicedee.activitymaster.fsdm.client.services.systems.IProgressable;
import com.guicedee.activitymaster.fsdm.db.entities.time.*;
import com.guicedee.activitymaster.fsdm.db.timelord.EnglishNumberToWords;
import com.guicedee.activitymaster.fsdm.services.system.ITimeSystem;
import com.guicedee.client.IGuiceContext;
import com.guicedee.guicedinjection.JobService;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.fsdm.client.services.ITimeService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.DateTimeFormats.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.NumberFormats.*;
import static java.time.temporal.ChronoUnit.*;

/**
 * Reactive implementation of the TimeSystem
 */
public class TimeSystemReactive
        extends ActivityMasterDefaultSystem<TimeSystemReactive>
        implements IActivityMasterSystem<TimeSystemReactive>,
                   ITimeSystem,
                   IProgressable {

    private static final Logger log = Logger.getLogger(TimeSystemReactive.class.getName());

    @Inject
    private ISystemsService<?> systemsService;

    @Inject
    private Mutiny.SessionFactory sessionFactory;

    @Override
    public Uni<Boolean> getDay(Date date) {
        log.info("🔍 Getting day for date: {} with internal session management", date);
        
        return getDayReactive(date);
    }

    private Uni<Boolean> getDayReactive(Date date) {
        return sessionFactory.withStatelessSession(statelessSession -> {
            log.info("🏛️ Created stateless session for day operation: {}", statelessSession.hashCode());
            
            return getDayReactive(statelessSession, date);
        });
    }

    private Uni<Boolean> getDayReactive(Mutiny.StatelessSession statelessSession, Date date) {
        log.info("🔍 Searching for day with date: {} using stateless session {}", date, statelessSession.hashCode());
        
        // Use EntityAssist query builder with stateless session
        return new Days().builder(statelessSession)
            .find(Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(date)))
            .get()
            .onItem().invoke(existingDay -> {
                if (existingDay.isPresent()) {
                    log.info("✅ Day found for date: {} with stateless session", date);
                } else {
                    log.info("❌ Day not found for date: {}, will create new day", date);
                }
            })
            .onFailure().invoke(error -> {
                log.log(Level.SEVERE, "💥 Failed to search for day {}: {}", new Object[]{date, error.getMessage()});
            })
            .chain(existingDay -> {
                if (existingDay.isPresent()) {
                    return Uni.createFrom().item(true);
                } else {
                    // Create new day if not found
                    return createDayReactive(statelessSession, date)
                        .onItem().invoke(created -> {
                            log.info("✅ Day created for date: {} with stateless session", date);
                        })
                        .onFailure().invoke(error -> {
                            log.log(Level.SEVERE, "❌ Failed to create day for {}: {}", new Object[]{date, error.getMessage()});
                        });
                }
            });
    }

    private Uni<Boolean> createDayReactive(Mutiny.StatelessSession statelessSession, Date date) {
        log.info("🔧 Creating new day for date: {} with stateless session {}", date, statelessSession.hashCode());
        
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
                    
                    log.info("💾 Persisting new day with ID: {} using stateless session", newDay.getId());
                    
                    // Insert using stateless session for bulk performance
                    return statelessSession.insert(newDay)
                        .onItem().invoke(persisted -> {
                            log.info("✅ Day persisted successfully with ID: {}", newDay.getId());
                        })
                        .onFailure().invoke(error -> {
                            log.log(Level.SEVERE, "❌ Failed to persist day with ID {}: {}", 
                                new Object[]{newDay.getId(), error.getMessage()});
                        })
                        .map(persisted -> true);
                });
        })
        .onFailure().invoke(error -> {
            log.log(Level.SEVERE, "💥 Failed to create day for {}: {}", new Object[]{date, error.getMessage()});
        })
        .onFailure().recoverWithItem(() -> {
            log.warning("🔄 Recovering from day creation failure, returning false");
            return false;
        });
    }

    private Uni<DayNames> getDayNameReactive(Mutiny.StatelessSession statelessSession, String dayName) {
        log.info("🔍 Searching for day name: {} with stateless session", dayName);
        
        return new DayNames().builder(statelessSession)
            .where(DayNames_.dayName, Equals, dayName)
            .get()
            .onItem().invoke(dayNameOpt -> {
                if (dayNameOpt.isPresent()) {
                    log.info("✅ Day name found: {}", dayName);
                } else {
                    log.info("❌ Day name not found: {}, will create new", dayName);
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

    private Uni<DayNames> createDayNameReactive(Mutiny.StatelessSession statelessSession, String dayName) {
        log.info("🔧 Creating new day name: {} with stateless session", dayName);
        
        return Uni.createFrom().item(() -> {
            DayNames newDayName = new DayNames();
            newDayName.setDayName(dayName);
            
            return statelessSession.insert(newDayName)
                .onItem().invoke(persisted -> {
                    log.info("✅ Day name created successfully: {}", dayName);
                })
                .onFailure().invoke(error -> {
                    log.log(Level.SEVERE, "❌ Failed to create day name {}: {}", new Object[]{dayName, error.getMessage()});
                })
                .map(persisted -> newDayName);
        });
    }

    @Override
    public Uni<Void> loadTimeRange(int startYear, int endYear) {
        log.info("🚀 Starting TimeSystem loadTimeRange from {} to {} with internal session management", startYear, endYear);
        
        return loadTimeRangeReactive(startYear, endYear);
    }

    private Uni<Void> loadTimeRangeReactive(int startYear, int endYear) {
        long startTime = System.currentTimeMillis();
        log.info("🚀 Starting reactive time range loading from {} to {} with stateless sessions", startYear, endYear);
        
        return Uni.createFrom().item(() -> {
            // Calculate total days for progress tracking
            LocalDate startDate = LocalDate.of(startYear, 1, 1);
            LocalDate endDate = LocalDate.of(endYear, 12, 31);
            long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
            
            log.info("📊 Calculated {} total days to process", totalDays);
            return totalDays;
        })
        .chain(totalDays -> {
            // Use stateless session for bulk operations
            return sessionFactory.withStatelessSession(statelessSession -> {
                log.info("🏛️ Created stateless session for bulk time data operations: {}", statelessSession.hashCode());
                
                return loadTimeRangeBatched(statelessSession, startYear, endYear, totalDays);
            });
        })
        .onItem().invoke(() -> {
            long duration = System.currentTimeMillis() - startTime;
            log.info("🎉 Time range loading completed in {}ms", duration);
            
            // Update partition bases after loading
            log.info("🔧 Updating partition bases after time range loading");
            IGuiceContext.get(ActivityMasterService.class).updatePartitionBases();
        })
        .onFailure().invoke(error -> {
            long duration = System.currentTimeMillis() - startTime;
            log.log(Level.SEVERE, "💥 Time range loading failed after {}ms: {}", new Object[]{duration, error.getMessage()});
        })
        .map(result -> null); // Convert to Void for interface compliance
    }

    private Uni<Void> loadTimeRangeBatched(Mutiny.StatelessSession statelessSession, int startYear, int endYear, long totalDays) {
        log.info("🔄 Starting batched time range loading with stateless session {}", statelessSession.hashCode());
        
        List<Uni<Void>> yearOperations = new ArrayList<>();
        
        for (int year = startYear; year <= endYear; year++) {
            final int currentYear = year;
            
            yearOperations.add(
                loadYearReactive(statelessSession, currentYear)
                    .onItem().invoke(() -> {
                        log.info("✅ Year {} loaded successfully with stateless session", currentYear);
                        
                        // Progress tracking every year
                        logProgress("Time Loading", String.format("Loaded year %d", currentYear));
                    })
                    .onFailure().invoke(error -> {
                        log.log(Level.SEVERE, "❌ Failed to load year {} with stateless session: {}", 
                            new Object[]{currentYear, error.getMessage()});
                    })
            );
        }
        
        log.info("🔄 Running {} year loading operations in parallel with stateless session", yearOperations.size());
        
        return Uni.combine()
            .all()
            .unis(yearOperations)
            .discardItems()
            .onItem().invoke(() -> {
                log.info("🎉 All {} years loaded successfully with stateless session {}", 
                    yearOperations.size(), statelessSession.hashCode());
            })
            .onFailure().invoke(error -> {
                log.log(Level.SEVERE, "💥 Failed to load some years with stateless session {}: {}", 
                    new Object[]{statelessSession.hashCode(), error.getMessage()});
            })
            .map(result -> null);
    }

    private Uni<Void> loadYearReactive(Mutiny.StatelessSession statelessSession, int year) {
        log.info("📅 Loading year {} with stateless session {}", year, statelessSession.hashCode());
        
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
                                log.warning("⚠️ Day {} processing returned false", dateToProcess);
                            }
                        })
                        .onFailure().invoke(error -> {
                            log.log(Level.SEVERE, "❌ Failed to process day {}: {}", new Object[]{dateToProcess, error.getMessage()});
                        })
                );
                
                currentDate = currentDate.plusDays(1);
            }
            
            return dayOperations;
        })
        .chain(dayOperations -> {
            log.info("🔄 Processing {} days for year {} with stateless session", dayOperations.size(), year);
            
            // Process days in batches to avoid overwhelming the database
            return processDaysInBatches(dayOperations, 100) // Process 100 days at a time
                .onItem().invoke(() -> {
                    log.info("✅ Year {} completed with {} days processed", year, dayOperations.size());
                });
        })
        .map(result -> null);
    }

    private Uni<Void> processDaysInBatches(List<Uni<Boolean>> dayOperations, int batchSize) {
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
                        log.info("📊 Batch {}-{} completed ({} days)", batchStart + 1, batchEnd, batch.size());
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

    @Override
    public Uni<Void> createTime() {
        log.info("🕒 Starting createTime with internal session management");
        
        return createTimeReactive();
    }

    private Uni<Void> createTimeReactive() {
        return sessionFactory.withStatelessSession(statelessSession -> {
            log.info("🏛️ Created stateless session for createTime operation: {}", statelessSession.hashCode());
            
            return new Hours().builder(statelessSession)
                .where(Hours_.id, Equals, 1)
                .getCount()
                .chain(count -> {
                    if (count == 0) {
                        log.info("🔧 No hours found, creating time entities");
                        return createTimeEntities(statelessSession);
                    } else {
                        log.info("✅ Hours already exist, skipping time creation");
                        return Uni.createFrom().voidItem();
                    }
                });
        });
    }

    private Uni<Void> createTimeEntities(Mutiny.StatelessSession statelessSession) {
        log.info("🔧 Creating time entities with stateless session {}", statelessSession.hashCode());
        
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
                log.info("✅ All time entities created successfully");
            })
            .onFailure().invoke(error -> {
                log.log(Level.SEVERE, "💥 Failed to create time entities: {}", error.getMessage());
            })
            .map(result -> null);
    }

    private Uni<Void> createHourEntity(Mutiny.StatelessSession statelessSession, int hr) {
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
                log.info("✅ Hour {} created with all related entities", hr);
            })
            .onFailure().invoke(error -> {
                log.log(Level.SEVERE, "❌ Failed to create hour {}: {}", new Object[]{hr, error.getMessage()});
            })
            .map(result -> null);
    }

    @Override
    public String getSystemName() {
        return TimeSystemName;
    }

    @Override
    public String getSystemDescription() {
        return "The system for handling the time dimension";
    }

    @Override
    public int totalTasks() {
        return 0;
    }

    @Override
    public Integer sortOrder() {
        return Integer.MIN_VALUE + 6;
    }
}