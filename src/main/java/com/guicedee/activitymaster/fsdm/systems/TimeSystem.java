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
import com.guicedee.activitymaster.fsdm.threads.TimeLoaderThread;
import com.guicedee.guicedinjection.JobService;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
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
		ISystems<?, ?> iSystems = systemsService
				.create(enterprise, getSystemName(), getSystemDescription());
		systemsService
				.registerNewSystem(enterprise, getSystem(enterprise));
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
		while (startYearGC.getTime()
		                  .getTime() <=
		       endGC.getTime()
		            .getTime())
		{
			getDay(startYearGC.getTime());
			startYearGC.add(Calendar.DATE, 1);
		}
		
		int difference = endYear - startYear;
		if (difference < 1)
		{
			difference = 1;
		}
		setTotalTasks(difference * 12);
		
		//Create data storage partitions
		//com.guicedee.client.IGuiceContext.get(ActivityMasterService.class)
		//            .updatePartitionBases();
		
		System.out.println("Waiting for Time Range Loading... 1 Hour");
		JobService.INSTANCE
		          .waitForJob("TimeRangeLoading", 1, TimeUnit.HOURS);
		System.out.println("Removing Executor Service...");
		JobService.INSTANCE
		          .removeJob("TimeRangeLoading");
	}
	//@Transactional()
	////@CacheResult(cacheName = "Years")
	public Years getYear(@CacheKey Date date)
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
		return new Years().builder()
		                  .find(Short.parseShort(YearIDFormat.getSimpleDateFormat()
		                                                     .format(date)))
		                  .get()
		                  .orElse(null);
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
	public Quarters getQuarter(@CacheKey Date date)
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
		return new Quarters().builder()
		                     .find(getQuarterID(date))
		                     .get()
		                     .orElse(null);
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
	public Months getMonth(@CacheKey Date date)
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
	//@CacheResult
	MonthOfYear getMonthOfYear(@CacheKey Integer MonthOfYear)
	{
		return new MonthOfYear().builder()
		                        .where(MonthOfYear_.monthInYearNumber, Equals, MonthOfYear)
		                        .get()
		                        .orElse(null);
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
		return new Months().builder()
		                   .find(Integer.parseInt(MonthIDFormat.getSimpleDateFormat()
		                                                       .format(date)))
		                   .get()
		                   .orElse(null);
	}
	
	/**
	 * @param date
	 * @return
	 */
	//@CacheResult
	public Weeks getWeek(@CacheKey Date date)
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
		int weekID = getWeekID(date);
		return new Weeks().builder()
		                  .find(weekID)
		                  .get()
		                  .orElse(null);
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
	 * Day
	 *
	 * @param date
	 * @return
	 */
	//@Transactional()
	//@CacheResult
	@Override
	public boolean getDay(@CacheKey Date date)
	{
		Days day = null;
		try
		{
			day = getDayFromID(date);
		}
		catch (Exception ex)
		{
			Logger.getLogger(TimeSystem.class.getName())
			      .log(Level.SEVERE, null, ex);
		}
		if (day == null)
		{
			day = createDay(date);
			day.persist();
			day.builder().getEntityManager().flush();
			TimeLoaderThread thread = com.guicedee.client.IGuiceContext.get(TimeLoaderThread.class);
			thread.setDate(date);
			JobService.INSTANCE
			          .addJob("TimeRangeLoading", thread);
			JobService.INSTANCE
			          .waitForJob("TimeRangeLoading",15,TimeUnit.MINUTES);
			JobService.INSTANCE
			          .removeJob("TimeRangeLoading");
		}
		return true;
	}
	//@Transactional()
	//@CacheResult
	DayNames getDayName(@CacheKey String dayName)
	{
		return new DayNames().builder()
		                     .where(DayNames_.dayName, Equals, dayName)
		                     .get()
		                     .orElse(null);
	}
	//@Transactional()
	Days getDayFromID(Date date)
	{
		return new Days().builder()
		                 .find(Integer.parseInt(DayIDFormat.getSimpleDateFormat()
		                                                   .format(date)))
		                 .get()
		                 .orElse(null);
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
			Logger.getLogger(TimeSystem.class.getName())
			      .log(Level.SEVERE, null, ex);
		}
		
		newDay.setDayFullDescription(DayFullDescriptionFormat.getSimpleDateFormat()
		                                                     .format(date));
		return newDay;
	}

	//@Transactional
	public void populateTransformationTables(Date date, int fiscalLag)
	{
		try
		{
			getDayYTD(date);
			getDayQTD(date);
		}
		catch (Exception e)
		{
			Logger.getLogger(TimeSystem.class.getName())
			      .log(Level.SEVERE, null, e);
		}
		getDayMTD(date);
		getDayFiscal(date, fiscalLag);
	}
	
	//@Transactional()
	void getDayYTD(Date date)
	{
		
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
		
		ArrayList<TransYtd> arr = new ArrayList<>();
		while (startYearGC.getTime()
		                  .getTime() <=
		       gc.getTime()
		         .getTime())
		{
			/*System.out.println("Creating YTD Day [" +
			                   DayIDFormat.getSimpleDateFormat()
			                              .format(startYearGC.getTime()) +
			                   "]");*/
			TransYtd tran = new TransYtd().setId(new TransYtdPK().setDayID(Integer.parseInt(DayIDFormat.getSimpleDateFormat()
			                                                                                           .format(date)))
			                                                     .setYtdDayID(Integer.parseInt(DayIDFormat.getSimpleDateFormat()
			                                                                                              .format(startYearGC.getTime()))));
			arr.add(tran);
			startYearGC.add(Calendar.DATE, 1);
		}
		
		for (TransYtd transYtd : arr)
		{
			transYtd.persist();
		}
		arr.clear();
	}
	
	//@Transactional()
	void getDayMTD(Date date)
	{
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		
		GregorianCalendar startYearGC = new GregorianCalendar();
		startYearGC.setTime(date);
		startYearGC.set(Calendar.YEAR, gc.get(Calendar.YEAR));
		//startYearGC.set(Calendar.MONTH, 0);
		startYearGC.set(Calendar.DATE, 1);
		
		gc.set(GregorianCalendar.HOUR, 0);
		gc.set(GregorianCalendar.SECOND, 0);
		gc.set(GregorianCalendar.MINUTE, 0);
		gc.set(GregorianCalendar.MILLISECOND, 0);
		startYearGC.set(GregorianCalendar.HOUR, 0);
		startYearGC.set(GregorianCalendar.SECOND, 0);
		startYearGC.set(GregorianCalendar.MINUTE, 0);
		startYearGC.set(GregorianCalendar.MILLISECOND, 0);
		//System.out.println("Should be An MTD creation here : " + startYearGC.getTime() + " - " + gc.getTime());
		ArrayList<TransMtd> arr = new ArrayList<>();
		while (startYearGC.getTime()
		                  .getTime() <=
		       gc.getTime()
		         .getTime())
		{
			//System.out.println("Creating MTD Day [" + dayIdFormat.format(startYearGC.getTime()) + "]");
			TransMtd tran = new TransMtd().setId(new TransMtdPK().setDayID(Integer.parseInt(DayIDFormat.getSimpleDateFormat()
			                                                                                           .format(gc.getTime())))
			                                                     .setMtdDayID(Integer.parseInt(DayIDFormat.getSimpleDateFormat()
			                                                                                              .format(startYearGC.getTime()))));
			arr.add(tran);
			startYearGC.add(Calendar.DATE, 1);
		}
		for (TransMtd transYtd : arr)
		{
			transYtd.persist();
		}
		arr.clear();
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
	
	//@Transactional()
	void getDayQTD(Date date)
	{
		
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
		ArrayList<TransQtd> arr = new ArrayList<>();
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
		
		//startYearGC.set(Calendar.MONTH, 0);
		startYearGC.set(Calendar.DATE, 1);
		
		while (startYearGC.getTime()
		                  .getTime() <=
		       gc.getTime()
		         .getTime())
		{
			/*System.out.println("Creating QTD Day [" +
			                   DayIDFormat.getSimpleDateFormat()
			                              .format(startYearGC.getTime()) +
			                   "]");*/
			TransQtd tran = new TransQtd().setId(new TransQtdPK().setDayID(Integer.parseInt(DayIDFormat.getSimpleDateFormat()
			                                                                                           .format(date)))
			                                                     .setQtdDayID(
					                                                     Integer.parseInt(DayIDFormat.getSimpleDateFormat()
					                                                                                 .format(startYearGC.getTime()))));
			arr.add(tran);
			startYearGC.add(Calendar.DATE, 1);
		}
		for (TransQtd transYtd : arr)
		{
			transYtd.persist();
		}
		arr.clear();
	}
	
	@Override
	//@Transactional()
	public void createTime()
	{
		if (new Hours().builder()
				.where(Hours_.id,Equals,1)
		               .getCount() == 0)
		{
			int dayPartCount = 0;
			for (int hr = 0; hr < 24; hr++)
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
				
				List<HalfHours> halfs = new ArrayList<>();
				for (int min = 0; min < 60; min++)
				{
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
					hour.getTimeList()
					    .add(time);
					
					if (min == 30 || min == 0)
					{
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
						halfHours.setPreviousHalfHourMinuteID(min == 0 ? 30 : -0);
						halfs.add(halfHours);
					}
				}
				
				hour.persist();
				for (Time time : hour.getTimeList())
				{
					time.persist();
				}
				for (HalfHours half : halfs)
				{
					half.persist();
					HalfHourDayParts halfHourDayParts = new HalfHourDayParts();
					halfHourDayParts.setId(dayPartCount++);
					
					halfHourDayParts.setHourID(half.getId()
					                               .getHourID());
					halfHourDayParts.setMinuteID(half.getId()
					                                 .getMinuteID());
					TimeService<?> timeService = com.guicedee.client.IGuiceContext.get(TimeService.class);
					halfHourDayParts.setDayPartID(timeService.getDayPart(half.getId()
					                                                         .getHourID(),
							half.getId()
							    .getMinuteID()));
					halfHourDayParts.persist();
				}
			}
		}
	}
}
