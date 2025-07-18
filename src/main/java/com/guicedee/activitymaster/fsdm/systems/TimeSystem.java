package com.guicedee.activitymaster.fsdm.systems;

import com.google.inject.Inject;
//import com.google.inject.persist.Transactional;
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
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.entityassist.enumerations.Operand.*;
import static com.guicedee.activitymaster.fsdm.client.services.ITimeService.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.DateTimeFormats.*;
import static com.guicedee.activitymaster.fsdm.client.services.classifications.types.NumberFormats.*;
import static java.time.temporal.ChronoUnit.*;


public class TimeSystem
		extends ActivityMasterDefaultSystem<TimeSystem>
		implements IActivityMasterSystem<TimeSystem>,
		           ITimeSystem,
		           IProgressable
{
	@Inject
	private ISystemsService<?> systemsService;

	@Override
	public ISystems<?, ?> registerSystem(IEnterprise<?, ?> enterprise)
	{
		// Since we need to return a non-reactive type, we need to block here
		// This is not ideal, but necessary for compatibility
		ISystems<?, ?> iSystems = systemsService
				.create(enterprise, getSystemName(), getSystemDescription())
				.await().atMost(Duration.ofMinutes(1)); // Use timeout instead of indefinitely

		systemsService
				.registerNewSystem(enterprise, getSystem(enterprise))
				.await().atMost(Duration.ofMinutes(1)); // Use timeout instead of indefinitely

		return iSystems;
	}

	@Override
	public void createDefaults(IEnterprise<?, ?> enterprise)
	{
		logProgress("Time System", "Loading Time Classifications...", 4);
		loadTimeRange(2004, LocalDateTime.now()
		                                 .getYear());
	}

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

	private static final Logger log = LogManager.getLogger(TimeSystem.class);

	@Override
	public Uni<Void> loadTimeRange(int startYear, int endYear)
	{
		logProgress("Starting Time Load", "Time load is starting from " + startYear + " + to " + endYear);

		return Uni.createFrom().item(() -> {
			GregorianCalendar startYearGC = new GregorianCalendar();
			startYearGC.set(Calendar.YEAR, startYear);
			startYearGC.set(Calendar.MONTH, 0);
			startYearGC.set(Calendar.DATE, 1);

			GregorianCalendar endGC = new GregorianCalendar();
			endGC.set(Calendar.YEAR, endYear);
			endGC.set(Calendar.MONTH, 11);
			endGC.set(Calendar.DATE, 31);

			// Create a list of all dates to process
			List<Date> datesToProcess = new ArrayList<>();
			while (startYearGC.getTime().getTime() <= endGC.getTime().getTime()) {
				datesToProcess.add(startYearGC.getTime());
				startYearGC.add(Calendar.DATE, 1);
			}

			// Process dates in batches to avoid overwhelming the system
			int batchSize = 30; // Process 30 days at a time
			List<List<Date>> batches = new ArrayList<>();

			for (int i = 0; i < datesToProcess.size(); i += batchSize) {
				batches.add(datesToProcess.subList(i, Math.min(i + batchSize, datesToProcess.size())));
			}

			// Process each batch sequentially
			Uni<Void> result = Uni.createFrom().voidItem();

			AtomicInteger processedCount = new AtomicInteger(0);
			int totalDates = datesToProcess.size();

			for (List<Date> batch : batches) {
				final int batchStartIndex = processedCount.get();
				result = result.chain(() -> {
					// Create operations for each date in the batch
					List<Uni<Boolean>> batchOperations = batch.stream()
						.map(date -> getDayReactive(date))
						.collect(Collectors.toList());

					// Execute batch operations in parallel
					return Uni.combine().all().unis(batchOperations)
						.discardItems()
						.invoke(() -> {
							int currentProcessed = processedCount.addAndGet(batch.size());
							log.info("Processed batch {} to {} of {} dates", 
								batchStartIndex, 
								Math.min(batchStartIndex + batch.size(), totalDates), 
								totalDates);
						})
						.onFailure().invoke(error -> 
							log.error("Error processing batch starting at {}", batchStartIndex, error));
				});
			}

			int difference = endYear - startYear;
			if (difference < 1) {
				difference = 1;
			}
			setTotalTasks(difference * 12);

			return result.invoke(() -> 
				log.info("Time range loading completed for years {} to {}", startYear, endYear));
		}).flatMap(uni -> uni);
	}
	//@Transactional()
	////@CacheResult(cacheName = "Years")
	public Years getYear( Date date)
	{
		Years year = null;
		year = getYearFromID(date);
		if (year == null)
		{
			logProgress("Time Lord", "Creating Year [" +
			                         YearIDFormat.getSimpleDateFormat()
			                                     .format(date) +
			                         "]");
			year = createYear(date);
		}
		return year;
	}

	//@Transactional()
	Years createYear(Date date)
	{
		Years year = new Years().setId(Short.parseShort(YearIDFormat.getSimpleDateFormat()
		                                                            .format(date)));
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(Calendar.MONTH, 1);
		year.setLeapYearFlag((short) (gc.getActualMaximum(Calendar.DAY_OF_MONTH) == 29 ? 1 : 0));
		year.setLastYearID((short) getLastYearID(date));
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
		year.persist();
		year.builder()
		    .getEntityManager().flush();
		return year;
	}
	//@Transactional()
	Years getYearFromID(Date date)
	{
		// Since we need to return a non-reactive type, we need to block here
		// This is not ideal, but necessary for compatibility
		try {
			return new Years().builder()
			                  .find(Short.parseShort(YearIDFormat.getSimpleDateFormat()
			                                                     .format(date)))
			                  .get()
			                  .await().atMost(Duration.ofSeconds(10));
		} catch (Exception e) {
			log.error("Error in getYearFromID", e);
			return null;
		}
	}
	//@Transactional()
	int getLastYearID(Date date)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.YEAR, -1);
		return Integer.parseInt(YearIDFormat.getSimpleDateFormat()
		                                    .format(gc.getTime()));
	}

	/**
	 * @param date
	 * @return
	 */
	//@Transactional()
	Quarters createQuarter(Date date)
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
		quarter.setYearID(getYear(date));
		return quarter;
	}

	//@Transactional()
	//@CacheResult
	public Quarters getQuarter( Date date)
	{
		Quarters month = null;
		try
		{
			month = getQuarterFromID(date);
		}
		catch (Exception ex)
		{
			//   Logger.getLogger(TimeLord.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (month == null)
		{
			month = createQuarter(date);
			month.persist();
			month.builder()
			     .getEntityManager()
			     .flush();
		}
		return month;
	}
	//@Transactional()
	Quarters getQuarterFromID(Date date) throws Exception
	{
		// Since we need to return a non-reactive type, we need to block here
		// This is not ideal, but necessary for compatibility
		try {
			return new Quarters().builder()
			                     .find(getQuarterID(date))
			                     .get()
			                     .await().atMost(Duration.ofSeconds(10));
		} catch (Exception e) {
			log.error("Error in getQuarterFromID", e);
			return null;
		}
	}
	private int getLastQuarterID(Date date)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.MONTH, -4);
		return getQuarterID(gc.getTime());
	}
	private int getQuarterNumber(Date date)
	{
		int quarterNumber = 0;
		int monthNumber = Integer.parseInt(MonthNumberFormat.getSimpleDateFormat()
		                                                    .format(date));
		if (monthNumber <= 3)
		{
			quarterNumber = 1;
		}
		else if (monthNumber <= 6)
		{
			quarterNumber = 2;
		}
		else if (monthNumber <= 9)
		{
			quarterNumber = 3;
		}
		else if (monthNumber <= 12)
		{
			quarterNumber = 4;
		}
		return quarterNumber;
	}
	private int getQuarterID(Date date)
	{
		int quarterNumber = getQuarterNumber(date);
		int returnId = Integer.parseInt(Integer.parseInt(YearIDFormat.getSimpleDateFormat()
		                                                             .format(date)) + "" + quarterNumber);
		return returnId;
	}

	private int getLastYearQuarterID(Date date)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.YEAR, -1);
		return getQuarterID(gc.getTime());
	}
	//@Transactional()
	/**
	 * @param date
	 * @return
	 */
	//@CacheResult
	public Months getMonth( Date date)
	{
		Months month = null;
		month = getMonthFromID(date);
		if (month == null)
		{
			month = createMonth(date);
			logProgress("Time Lord", "Creating Month [" +
			                         month.getMonthDescription() +
			                         "]", 1);
			month.persist();
			month.builder()
			     .getEntityManager()
			     .flush();
		}
		return month;
	}
	//@Transactional()
	MonthOfYear getMonthOfYear(Integer MonthOfYear)
	{
		// Since we need to return a non-reactive type, we need to block here
		// This is not ideal, but necessary for compatibility
		try {
			return new MonthOfYear().builder()
			                        .where(MonthOfYear_.monthInYearNumber, Equals, MonthOfYear)
			                        .get()
			                        .await().atMost(Duration.ofSeconds(10));
		} catch (Exception e) {
			log.error("Error in getMonthOfYear", e);
			return null;
		}
	}

	//@Transactional()
	Months createMonth(Date date)
	{
		Months month = new Months(Integer.parseInt(MonthIDFormat.getSimpleDateFormat()
		                                                        .format(date)));
		month.setLastQuarterID(getLastQuarterMonthID(date));
		month.setLastMonthID(getLastYearMonthID(date));
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		month.setMonthDayDuration((short) gc.getActualMaximum(Calendar.DAY_OF_MONTH));
		month.setMonthDescription(MonthDescriptionFormat.getSimpleDateFormat()
		                                                .format(date));
		month.setId(Integer.parseInt(MonthIDFormat.getSimpleDateFormat()
		                                          .format(date)));
		month.setMonthMMMYYDescription(MonthMMMMYYDescriptionFormat.getSimpleDateFormat()
		                                                           .format(date));
		month.setMonthMMYYYYDescription(MonthMMYYDescriptionFormat.getSimpleDateFormat()
		                                                          .format(date));
		month.setMonthNameYYYYDescription(MonthMonthNameYYYYDescriptionFormat.getSimpleDateFormat()
		                                                                     .format(date));
		month.setMonthOfYearID(getMonthOfYear(gc.get(Calendar.MONTH)));
		month.setMonthShortDescription(MonthMMYYDescriptionFormat.getSimpleDateFormat()
		                                                         .format(date));
		month.setMonthYYDescription(MonthMMYYDescriptionFormat.getSimpleDateFormat()
		                                                      .format(date));
		month.setLastMonthID(getLastMonthID(date));
		month.setLastQuarterID(getLastQuarterMonthID(date));
		month.setLastYearID(getLastYearMonthID(date));
		month.setQuarterID(getQuarter(date));
		month.setYearID(Integer.parseInt(YearIDFormat.getSimpleDateFormat()
		                                             .format(date)));
		return month;
	}

	private int getLastQuarterMonthID(Date date)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.MONTH, -4);
		return Integer.parseInt(MonthIDFormat.getSimpleDateFormat()
		                                     .format(gc.getTime()));
	}

	private int getLastMonthYearID(Date date)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.MONTH, -1);
		return Integer.parseInt(YearIDFormat.getSimpleDateFormat()
		                                    .format(gc.getTime()));
	}

	private int getLastMonthID(Date date)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.MONTH, -1);
		return Integer.parseInt(MonthIDFormat.getSimpleDateFormat()
		                                     .format(gc.getTime()));
	}

	private int getLastYearMonthID(Date date)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.YEAR, -1);
		return Integer.parseInt(MonthIDFormat.getSimpleDateFormat()
		                                     .format(gc.getTime()));
	}
	//@Transactional()
	Months getMonthFromID(Date date)
	{
		// Since we need to return a non-reactive type, we need to block here
		// This is not ideal, but necessary for compatibility
		try {
			return new Months().builder()
			                   .find(Integer.parseInt(MonthIDFormat.getSimpleDateFormat()
			                                                       .format(date)))
			                   .get()
			                   .await().atMost(Duration.ofSeconds(10));
		} catch (Exception e) {
			log.error("Error in getMonthFromID", e);
			return null;
		}
	}

	/**
	 * @param date
	 * @return
	 */
	//@CacheResult
	public Weeks getWeek( Date date)
	{
		Weeks month = null;
		try
		{
			month = getWeekFromID(date);
		}
		catch (Exception ex)
		{
			// Logger.getLogger(TimeLord.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (month == null)
		{
			month = createWeek(date);
			month.persist();
			month.builder()
			     .getEntityManager()
			     .flush();
		}
		return month;
	}
	//@Transactional()
	Weeks getWeekFromID(Date date)
	{
		// Since we need to return a non-reactive type, we need to block here
		// This is not ideal, but necessary for compatibility
		try {
			int weekID = getWeekID(date);
			return new Weeks().builder()
			                  .find(weekID)
			                  .get()
			                  .await().atMost(Duration.ofSeconds(10));
		} catch (Exception e) {
			log.error("Error in getWeekFromID", e);
			return null;
		}
	}

	private int getWeekID(Date date)
	{
		GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
		gc.setTime(date);
		int weekNumber = gc.get(Calendar.WEEK_OF_YEAR);
		int weekID = Integer.parseInt(YearIDFormat.getSimpleDateFormat()
		                                          .format(date) + "" + weekNumber);
		//System.out.println("Week ID : " + weekID);
		return weekID;
	}

	//@Transactional()
	Weeks createWeek(Date date)
	{
		Weeks week = new Weeks();
		week.setMonthID(getMonth(date).getId());
		week.setQuarterID(getQuarterID(date));
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		week.setWeekDescription("Week " + gc.get(Calendar.WEEK_OF_YEAR));
		week.setId(getWeekID(date));
		week.setWeekOfMonth(gc.get(Calendar.WEEK_OF_MONTH));
		week.setWeekOfYear(gc.get(Calendar.WEEK_OF_YEAR));
		week.setWeekShortDescription("W" + gc.get(Calendar.WEEK_OF_YEAR));
		week.setYearID(Integer.parseInt(YearIDFormat.getSimpleDateFormat()
		                                            .format(date)));

		return week;
	}

	/**
	 * Day - Reactive version
	 *
	 * @param date
	 * @return
	 */
	public Uni<Boolean> getDayReactive(Date date) {
		return getDayFromIDReactive(date)
			.onItem().transformToUni(day -> {
				if (day != null) {
					return Uni.createFrom().item(true);
				} else {
					return createDayReactive(date)
						.chain(newDay -> populateTransformationTablesReactive(date, -3))
						.map(result -> true);
				}
			})
			.onFailure().invoke(ex -> log.error("Error getting day", ex))
			.onFailure().recoverWithItem(false);
	}

	/**
	 * Get day from ID - Reactive version
	 * 
	 * @param date
	 * @return
	 */
	private Uni<Days> getDayFromIDReactive(Date date) {
		int dayId = Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(date));
		return new Days().builder()
			.find(dayId)
			.get()
			.onItem().ifNull().continueWith(() -> null);
	}

	/**
	 * Day - Legacy method for backward compatibility
	 *
	 * @param date
	 * @return
	 */
	//@Transactional()
	//@CacheResult
	@Override
	public boolean getDay(Date date) {
		try {
			return getDayReactive(date).await().indefinitely();
		} catch (Exception ex) {
			log.error("Error in getDay", ex);
			return false;
		}
	}

	/**
	 * Create day - Reactive version
	 * 
	 * @param date
	 * @return
	 */
	private Uni<Days> createDayReactive(Date date) {
		return Uni.createFrom().item(() -> {
			Days day = createDay(date);
			return day.persist().map(d -> day);
		}).flatMap(uni -> uni);
	}
	//@Transactional()
	DayNames getDayName(String dayName)
	{
		// Since we need to return a non-reactive type, we need to block here
		// This is not ideal, but necessary for compatibility
		try {
			return new DayNames().builder()
			                     .where(DayNames_.dayName, Equals, dayName)
			                     .get()
			                     .await().atMost(Duration.ofSeconds(10));
		} catch (Exception e) {
			log.error("Error in getDayName", e);
			return null;
		}
	}
	//@Transactional()
	Days getDayFromID(Date date)
	{
		// Since we need to return a non-reactive type, we need to block here
		// This is not ideal, but necessary for compatibility
		try {
			return new Days().builder()
			                 .find(Integer.parseInt(DayIDFormat.getSimpleDateFormat()
			                                                   .format(date)))
			                 .get()
			                 .await().atMost(Duration.ofSeconds(10));
		} catch (Exception e) {
			log.error("Error in getDayFromID", e);
			return null;
		}
	}

	private int getLastDayID(Date date)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.DATE, -1);
		return Integer.parseInt(DayIDFormat.getSimpleDateFormat()
		                                   .format(gc.getTime()));
	}

	private int getLastMonthDayID(Date date)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.MONTH, -1);
		return Integer.parseInt(DayIDFormat.getSimpleDateFormat()
		                                   .format(gc.getTime()));
	}

	private int getLastYearDayID(Date date)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.YEAR, -1);
		return Integer.parseInt(DayIDFormat.getSimpleDateFormat()
		                                   .format(gc.getTime()));
	}

	private int getLastQuarterDayID(Date date)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.MONTH, -4);
		return Integer.parseInt(DayIDFormat.getSimpleDateFormat()
		                                   .format(gc.getTime()));
	}

	//@Transactional()
	Days createDay(Date date)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.set(GregorianCalendar.HOUR, 0);
		gc.set(GregorianCalendar.SECOND, 0);
		gc.set(GregorianCalendar.MINUTE, 0);
		gc.set(GregorianCalendar.MILLISECOND, 0);
		date = gc.getTime();

		Days newDay = new Days();
		newDay.setId(Integer.parseInt(DayIDFormat.getSimpleDateFormat()
		                                         .format(date)));
		newDay.setDayDate(LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()));
		newDay.setDayDateTime(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
		                                   .truncatedTo(DAYS));
		newDay.setDayInMonth(gc.get(Calendar.DAY_OF_MONTH));
		newDay.setDayInYear(gc.get(Calendar.DAY_OF_YEAR));
		newDay.setDayIsPublicHoliday((short) 0);
		newDay.setDayLongDescription(DayLongDescriptionFormat.getSimpleDateFormat()
		                                                     .format(date));
		newDay.setDayMMQQDescription("Q" +
		                             DoubleDigits.formatter()
		                                         .format(getQuarterNumber(date)) +
		                             "-" +
		                             MonthNumberFormat.getSimpleDateFormat()
		                                              .format(date) +
		                             "-" +
		                             DayInMonthFormat.getSimpleDateFormat()
		                                             .format(date));
		newDay.setDayMonthDescription(MonthLongDescriptionFormat.getSimpleDateFormat()
		                                                        .format(date));
		newDay.setDayNameID(getDayName(gc.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)));
		newDay.setDayYYYYMMDescription(DaySlashIDFormat.getSimpleDateFormat()
		                                               .format(date));

		newDay.setDayDDMMYYYYDescription(DayDDMMYYYYFormat.getSimpleDateFormat()
		                                                  .format(date));
		newDay.setDayDDMMYYYYSlashDescription(DaySlashDDMMYYYYFormat.getSimpleDateFormat()
		                                                            .format(date));
		newDay.setDayDDMMYYYYHyphenDescription(DayHyphenDDMMYYYYFormat.getSimpleDateFormat()
		                                                              .format(date));

		newDay.setLastMonthID(getLastMonthDayID(date));
		newDay.setLastQuarterID(getLastQuarterDayID(date));
		newDay.setLastYearID(getLastYearDayID(date));
		newDay.setMonthID(getMonth(date));
		newDay.setLastDayID(getLastDayID(date));
		newDay.setQuarterID(getQuarterID(date));
		newDay.setWeekID(getWeek(date));
		try
		{
			Years y = getYearFromID(date);
			if (y == null)
			{
				y = createYear(date);
			}
			newDay.setYearID(y.getId());
		}
		catch (Exception ex)
		{
			log.error("Error in createDay", ex);
		}

		newDay.setDayFullDescription(DayFullDescriptionFormat.getSimpleDateFormat()
		                                                     .format(date));
		return newDay;
	}

	//@Transactional
	public void populateTransformationTables(Date date, int fiscalLag)
	{
		try {
			populateTransformationTablesReactive(date, fiscalLag).await().indefinitely();
		} catch (Exception e) {
			log.error("Error in populateTransformationTables", e);
		}
	}

	/**
	 * Reactive version of populateTransformationTables
	 * 
	 * @param date
	 * @param fiscalLag
	 * @return
	 */
	public Uni<Void> populateTransformationTablesReactive(Date date, int fiscalLag)
	{
		// Create a list of operations to run in parallel
		List<Uni<?>> operations = new ArrayList<>();

		// Add YTD operation
		operations.add(getDayYTDReactive(date));

		// Add QTD operation
		operations.add(getDayQTDReactive(date));

		// Add MTD operation
		operations.add(getDayMTDReactive(date));

		// Add Fiscal operation
		operations.add(getDayFiscalReactive(date, fiscalLag));

		// Run all operations in parallel
		return Uni.combine().all().unis(operations)
			.discardItems()
			.onFailure().invoke(error -> log.error("Error in populateTransformationTablesReactive", error));
	}

	//@Transactional()
	void getDayYTD(Date date)
	{
		try {
			getDayYTDReactive(date).await().indefinitely();
		} catch (Exception e) {
			log.error("Error in getDayYTD", e);
		}
	}

	/**
	 * Get day YTD - Reactive version
	 * 
	 * @param date
	 * @return
	 */
	Uni<Void> getDayYTDReactive(Date date)
	{
		return Uni.createFrom().item(() -> {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);

			GregorianCalendar startYearGC = new GregorianCalendar();
			startYearGC.set(Calendar.YEAR, gc.get(Calendar.YEAR));
			startYearGC.set(Calendar.MONTH, 0);
			startYearGC.set(Calendar.DATE, 1);

			gc.set(GregorianCalendar.HOUR, 0);
			gc.set(GregorianCalendar.SECOND, 0);
			gc.set(GregorianCalendar.MINUTE, 0);
			gc.set(GregorianCalendar.MILLISECOND, 0);
			startYearGC.set(GregorianCalendar.HOUR, 0);
			startYearGC.set(GregorianCalendar.SECOND, 0);
			startYearGC.set(GregorianCalendar.MINUTE, 0);
			startYearGC.set(GregorianCalendar.MILLISECOND, 0);

			List<Uni<TransYtd>> operations = new ArrayList<>();

			while (startYearGC.getTime().getTime() <= gc.getTime().getTime()) {
				final Date currentDate = startYearGC.getTime();

				TransYtd tran = new TransYtd().setId(
					new TransYtdPK()
						.setDayID(Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(date)))
						.setYtdDayID(Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(currentDate)))
				);

				operations.add(Uni.createFrom().item(() -> {
					tran.persist();
					return tran;
				}));

				startYearGC.add(Calendar.DATE, 1);
			}

			// Process in batches to avoid overwhelming the system
			int batchSize = 50;
			List<List<Uni<TransYtd>>> batches = new ArrayList<>();

			for (int i = 0; i < operations.size(); i += batchSize) {
				batches.add(operations.subList(i, Math.min(i + batchSize, operations.size())));
			}

			// Process each batch sequentially
			Uni<Void> result = Uni.createFrom().voidItem();

			for (List<Uni<TransYtd>> batch : batches) {
				result = result.chain(() -> 
					Uni.combine().all().unis(batch)
						.discardItems()
						.onFailure().invoke(error -> log.error("Error processing YTD batch", error))
				);
			}

			return result;
		}).flatMap(uni -> uni);
	}

	//@Transactional()
	void getDayMTD(Date date)
	{
		try {
			getDayMTDReactive(date).await().indefinitely();
		} catch (Exception e) {
			log.error("Error in getDayMTD", e);
		}
	}

	/**
	 * Get day MTD - Reactive version
	 * 
	 * @param date
	 * @return
	 */
	Uni<Void> getDayMTDReactive(Date date)
	{
		return Uni.createFrom().item(() -> {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);

			GregorianCalendar startYearGC = new GregorianCalendar();
			startYearGC.setTime(date);
			startYearGC.set(Calendar.YEAR, gc.get(Calendar.YEAR));
			startYearGC.set(Calendar.DATE, 1);

			gc.set(GregorianCalendar.HOUR, 0);
			gc.set(GregorianCalendar.SECOND, 0);
			gc.set(GregorianCalendar.MINUTE, 0);
			gc.set(GregorianCalendar.MILLISECOND, 0);
			startYearGC.set(GregorianCalendar.HOUR, 0);
			startYearGC.set(GregorianCalendar.SECOND, 0);
			startYearGC.set(GregorianCalendar.MINUTE, 0);
			startYearGC.set(GregorianCalendar.MILLISECOND, 0);

			List<Uni<TransMtd>> operations = new ArrayList<>();

			while (startYearGC.getTime().getTime() <= gc.getTime().getTime()) {
				final Date currentDate = startYearGC.getTime();

				TransMtd tran = new TransMtd().setId(
					new TransMtdPK()
						.setDayID(Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(gc.getTime())))
						.setMtdDayID(Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(currentDate)))
				);

				operations.add(Uni.createFrom().item(() -> {
					tran.persist();
					return tran;
				}));

				startYearGC.add(Calendar.DATE, 1);
			}

			// Process in batches to avoid overwhelming the system
			int batchSize = 50;
			List<List<Uni<TransMtd>>> batches = new ArrayList<>();

			for (int i = 0; i < operations.size(); i += batchSize) {
				batches.add(operations.subList(i, Math.min(i + batchSize, operations.size())));
			}

			// Process each batch sequentially
			Uni<Void> result = Uni.createFrom().voidItem();

			for (List<Uni<TransMtd>> batch : batches) {
				result = result.chain(() -> 
					Uni.combine().all().unis(batch)
						.discardItems()
						.onFailure().invoke(error -> log.error("Error processing MTD batch", error))
				);
			}

			return result;
		}).flatMap(uni -> uni);
	}

 //@Transactional()
	TransFiscal getDayFiscal(Date date, int fiscalMonthLag)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);

		GregorianCalendar startYearGC = new GregorianCalendar();
		startYearGC.setTime(date);
		startYearGC.add(Calendar.MONTH, fiscalMonthLag);
		TransFiscal tran = new TransFiscal(Integer.parseInt(DayIDFormat.getSimpleDateFormat()
		                                                               .format(date)), Integer.parseInt(DayIDFormat.getSimpleDateFormat()
		                                                                                                           .format(startYearGC.getTime())));
		tran.persist();
		return tran;
	}

	/**
	 * Get day fiscal - Reactive version
	 * 
	 * @param date
	 * @param fiscalMonthLag
	 * @return
	 */
	Uni<Void> getDayFiscalReactive(Date date, int fiscalMonthLag)
	{
		return Uni.createFrom().item(() -> {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);

			GregorianCalendar startYearGC = new GregorianCalendar();
			startYearGC.setTime(date);
			startYearGC.add(Calendar.MONTH, fiscalMonthLag);

			TransFiscal tran = new TransFiscal(
				Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(date)), 
				Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(startYearGC.getTime()))
			);

			tran.persist();
			return null; // Return null for Void
		}).map(result -> null); // Ensure we return Uni<Void>
	}

	//@Transactional()
	void getDayQTD(Date date)
	{
		try {
			getDayQTDReactive(date).await().indefinitely();
		} catch (Exception e) {
			log.error("Error in getDayQTD", e);
		}
	}

	/**
	 * Get day QTD - Reactive version
	 * 
	 * @param date
	 * @return
	 */
	Uni<Void> getDayQTDReactive(Date date)
	{
		return Uni.createFrom().item(() -> {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);

			GregorianCalendar startYearGC = new GregorianCalendar();
			startYearGC.set(Calendar.YEAR, gc.get(Calendar.YEAR));

			gc.set(GregorianCalendar.HOUR, 0);
			gc.set(GregorianCalendar.SECOND, 0);
			gc.set(GregorianCalendar.MINUTE, 0);
			gc.set(GregorianCalendar.MILLISECOND, 0);
			startYearGC.set(GregorianCalendar.HOUR, 0);
			startYearGC.set(GregorianCalendar.SECOND, 0);
			startYearGC.set(GregorianCalendar.MINUTE, 0);
			startYearGC.set(GregorianCalendar.MILLISECOND, 0);

			int qNum = getQuarterNumber(date);
			switch (qNum)
			{
				case 1:
				{
					startYearGC.set(Calendar.MONTH, 0);
					break;
				}
				case 2:
				{
					startYearGC.set(Calendar.MONTH, 3);
					break;
				}
				case 3:
				{
					startYearGC.set(Calendar.MONTH, 6);
					break;
				}
				case 4:
				{
					startYearGC.set(Calendar.MONTH, 9);
					break;
				}
			}

			startYearGC.set(Calendar.DATE, 1);

			List<Uni<TransQtd>> operations = new ArrayList<>();

			while (startYearGC.getTime().getTime() <= gc.getTime().getTime()) {
				final Date currentDate = startYearGC.getTime();

				TransQtd tran = new TransQtd().setId(
					new TransQtdPK()
						.setDayID(Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(date)))
						.setQtdDayID(Integer.parseInt(DayIDFormat.getSimpleDateFormat().format(currentDate)))
				);

				operations.add(Uni.createFrom().item(() -> {
					tran.persist();
					return tran;
				}));

				startYearGC.add(Calendar.DATE, 1);
			}

			// Process in batches to avoid overwhelming the system
			int batchSize = 50;
			List<List<Uni<TransQtd>>> batches = new ArrayList<>();

			for (int i = 0; i < operations.size(); i += batchSize) {
				batches.add(operations.subList(i, Math.min(i + batchSize, operations.size())));
			}

			// Process each batch sequentially
			Uni<Void> result = Uni.createFrom().voidItem();

			for (List<Uni<TransQtd>> batch : batches) {
				result = result.chain(() -> 
					Uni.combine().all().unis(batch)
						.discardItems()
						.onFailure().invoke(error -> log.error("Error processing QTD batch", error))
				);
			}

			return result;
		}).flatMap(uni -> uni);
	}

	@Override
	public Uni<Void> createTime()
	{
		return new Hours().builder()
				.where(Hours_.id, Equals, 1)
				.getCount()
				.chain(count -> {
					if (count == 0) {
						return createTimeEntities();
					} else {
						log.info("Time entities already exist, skipping creation");
						return Uni.createFrom().voidItem();
					}
				})
				.onFailure().invoke(error -> log.error("Error in createTime", error));
	}

	/**
	 * Creates all time-related entities (hours, minutes, half-hours, etc.)
	 * 
	 * @return Uni<Void> when complete
	 */
	private Uni<Void> createTimeEntities() {
		log.info("Creating time entities");
		AtomicInteger dayPartCount = new AtomicInteger(0);

		// Process each hour sequentially
		Uni<Void> result = Uni.createFrom().voidItem();

		for (int hr = 0; hr < 24; hr++) {
			final int hour = hr; // Final copy for lambda
			result = result.chain(() -> createHourEntities(hour, dayPartCount));
		}

		return result.invoke(() -> log.info("Time entities creation completed"));
	}

	/**
	 * Creates entities for a specific hour
	 * 
	 * @param hr The hour to create entities for
	 * @param dayPartCount Counter for day parts
	 * @return Uni<Void> when complete
	 */
	private Uni<Void> createHourEntities(int hr, AtomicInteger dayPartCount) {
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

		List<HalfHours> halfs = new ArrayList<>();
		List<Time> times = new ArrayList<>();

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
			hour.getTimeList().add(time);
			times.add(time);

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
				halfs.add(halfHours);
			}
		}

		// First persist the hour
		return Uni.createFrom().item(() -> hour.persist())
			// Then persist all time entities in parallel
			.chain(() -> {
				List<Uni<?>> timeOperations = times.stream()
					.map(time -> Uni.createFrom().item(() -> {
						time.persist();
						return null;
					}))
					.collect(Collectors.toList());

				return Uni.combine().all().unis(timeOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error persisting time entities for hour {}", hr, error));
			})
			// Then persist all half-hour entities and create half-hour day parts
			.chain(() -> {
				List<Uni<?>> halfOperations = new ArrayList<>();

				for (HalfHours half : halfs) {
					halfOperations.add(Uni.createFrom().item(() -> {
						half.persist();

						HalfHourDayParts halfHourDayParts = new HalfHourDayParts();
						halfHourDayParts.setId(dayPartCount.getAndIncrement());

						halfHourDayParts.setHourID(half.getId().getHourID());
						halfHourDayParts.setMinuteID(half.getId().getMinuteID());

						TimeService<?> timeService = com.guicedee.client.IGuiceContext.get(TimeService.class);
						halfHourDayParts.setDayPartID(timeService.getDayPart(
							half.getId().getHourID(),
							half.getId().getMinuteID()
						));

						halfHourDayParts.persist();
						return null;
					}));
				}

				return Uni.combine().all().unis(halfOperations)
					.discardItems()
					.onFailure().invoke(error -> log.error("Error persisting half-hour entities for hour {}", hr, error));
			});
	}
}
