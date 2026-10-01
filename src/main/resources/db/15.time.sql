DO $fsdm_schema$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_catalog.pg_namespace WHERE nspname = 'time') THEN
        CREATE SCHEMA IF NOT EXISTS "time";
    END IF;
END;
$fsdm_schema$;
CREATE TABLE IF NOT EXISTS "time".daynames
(
    daynameid                    integer                NOT NULL primary key,
    dayabbreviation              character varying(50)  NOT NULL,
    daybusinessdayclassification character varying(50)  NOT NULL,
    dayisbusinessday             INTEGER                NOT NULL,
    daylongabbreviation          character varying(50)  NOT NULL,
    dayname                      character varying(100) NOT NULL,
    dayshortname                 character varying(200) NOT NULL,
    daysortorder                 integer                NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".dayparts
(
    daypartid          integer                NOT NULL primary key,
    daypartdescription character varying(100) NOT NULL,
    daypartname        character varying(100) NOT NULL,
    daypartsortorder   integer                NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".days
(
    dayid                        integer                     NOT NULL primary key,
    dayddmmyyyydescription       character varying(50)       NOT NULL,
    dayddmmyyyyhyphendescription character varying(50)       NOT NULL,
    dayddmmyyyyslashdescription  character varying(50)       NOT NULL,
    daydate                      date                        NOT NULL,
    daydatetime                  timestamp(6) with time zone NOT NULL,
    dayfulldescription           character varying(50)       NOT NULL,
    dayinmonth                   integer                     NOT NULL,
    dayinyear                    integer                     NOT NULL,
    dayispublicholiday           INTEGER                     NOT NULL,
    daylongdescription           character varying(50)       NOT NULL,
    daymmqqdescription           character varying(50)       NOT NULL,
    daymonthdescription          character varying(50)       NOT NULL,
    dayyyyymmdescription         character varying(50)       NOT NULL,
    lastdayid                    integer                     NOT NULL,
    lastmonthid                  integer                     NOT NULL,
    lastquarterid                integer                     NOT NULL,
    lastyearid                   integer                     NOT NULL,
    quarterid                    integer                     NOT NULL,
    yearid                       integer                     NOT NULL,
    daynameid                    integer                     NOT NULL,
    monthid                      integer                     NOT NULL,
    weekid                       integer                     NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".halfhourdayparts
(
    halfhourdaypartid integer NOT NULL primary key,
    hourid            integer NOT NULL,
    minuteid          integer NOT NULL,
    daypartid         integer NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".halfhours
(
    hourid                   integer               NOT NULL,
    minuteid                 integer               NOT NULL,
    ampmdesc                 character varying(5)  NOT NULL,
    previoushalfhourminuteid integer               NOT NULL,
    previoushourid           integer               NOT NULL,
    twelvehourclockdesc      character varying(10) NOT NULL,
    twentyfourhourclockdesc  character varying(10) NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".hours
(
    hourid                  integer               NOT NULL primary key,
    ampmdesc                character varying(5)  NOT NULL,
    previoushourid          integer               NOT NULL,
    twelvehourclockdesc     character varying(10) NOT NULL,
    twentyfourhourclockdesc character varying(10) NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".monthofyear
(
    monthofyearid           integer               NOT NULL primary key,
    monthofyearabbreviation character varying(50) NOT NULL,
    monthofyearname         character varying(50) NOT NULL,
    monthofyearshortname    character varying(50) NOT NULL,
    monthinyearnumber       integer               NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".months
(
    monthid                  integer               NOT NULL primary key,
    lastmonthid              integer               NOT NULL,
    lastquarterid            integer               NOT NULL,
    lastyearid               integer               NOT NULL,
    monthdayduration         smallint              NOT NULL,
    monthdescription         character varying(50) NOT NULL,
    monthmmmyydescription    character varying(50) NOT NULL,
    monthmmyyyydescription   character varying(50) NOT NULL,
    monthnameyyyydescription character varying(50) NOT NULL,
    monthshortdescription    character varying(50) NOT NULL,
    monthyydescription       character varying(50) NOT NULL,
    yearid                   integer               NOT NULL,
    monthofyearid            integer               NOT NULL,
    quarterid                integer               NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".publicholidays
(
    publicholidayid   integer                NOT NULL primary key,
    dayid             integer                NOT NULL,
    publicholidayname character varying(250) NOT NULL,
    publicholidaytype character varying(250) NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".quarters
(
    quarterid               integer               NOT NULL primary key,
    lastquarterid           smallint              NOT NULL,
    lastyearid              smallint              NOT NULL,
    quarterdescription      character varying(50) NOT NULL,
    quartergraphdescription character varying(50) NOT NULL,
    quartergriddescription  character varying(50) NOT NULL,
    quarterinyear           integer               NOT NULL,
    quarterqqmmdescription  character varying(50) NOT NULL,
    quartersmalldescription character varying(50) NOT NULL,
    quarteryymmdescription  character varying(50) NOT NULL,
    quarteryeardescription  character varying(50) NOT NULL,
    yearid                  smallint              NOT NULL
);
CREATE TABLE IF NOT EXISTS "time"."time"
(
    hourid                  integer               NOT NULL,
    minuteid                integer               NOT NULL,
    ampmdesc                character varying(5)  NOT NULL,
    previoushourid          integer               NOT NULL,
    previousminuteid        integer               NOT NULL,
    twelvehourclockdesc     character varying(10) NOT NULL,
    twentyfourhourclockdesc character varying(10) NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".trans_fiscal
(
    dayid       integer NOT NULL,
    fiscaldayid integer NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".trans_mtd
(
    dayid    integer NOT NULL,
    mtddayid integer NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".trans_qtd
(
    dayid    integer NOT NULL,
    qtddayid integer NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".trans_qtm
(
    monthid     integer NOT NULL,
    qtm_monthid integer NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".trans_ytd
(
    dayid    integer NOT NULL,
    ytddayid integer NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".weeks
(
    weekid               integer               NOT NULL primary key,
    monthid              integer               NOT NULL,
    quarterid            integer               NOT NULL,
    weekdescription      character varying(50) NOT NULL,
    weekofmonth          integer               NOT NULL,
    weekofyear           integer               NOT NULL,
    weekshortdescription character varying(50) NOT NULL,
    yearid               integer               NOT NULL
);
CREATE TABLE IF NOT EXISTS "time".years
(
    yearid       smallint               NOT NULL primary key,
    century      smallint               NOT NULL,
    lastyearid   smallint               NOT NULL,
    leapyearflag smallint               NOT NULL,
    yyname       character varying(2)   NOT NULL,
    yyyname      character varying(3)   NOT NULL,
    yearfullname character varying(250) NOT NULL,
    yearname     character varying(10)  NOT NULL
);

INSERT INTO "time".monthofyear (monthofyearid, monthofyearabbreviation, monthofyearname, monthofyearshortname,
                                monthinyearnumber)
VALUES (1, 'J', 'January', 'Jan', 0),
       (2, 'F', 'February', 'Feb', 1),
       (3, 'M', 'March', 'Mar', 2),
       (4, 'A', 'April', 'Apr', 3),
       (5, 'M', 'May', 'May', 4),
       (6, 'J', 'June', 'Jun', 5),
       (7, 'J', 'July', 'Jul', 6),
       (8, 'A', 'August', 'Aug', 7),
       (9, 'S', 'September', 'Sep', 8),
       (10, 'O', 'October', 'Oct', 9),
       (11, 'N', 'November', 'Nov', 10),
       (12, 'D', 'December', 'Dec', 11)
ON CONFLICT (monthofyearid) DO NOTHING;

-- Batch insert data into the "time".dayparts table
INSERT INTO "time".dayparts (daypartid, daypartdescription, daypartname, daypartsortorder)
VALUES (2, 'Between 3.30am and 6.30 am', 'Early Morning', 2),
       (3, 'Between 6.30am and 9am', 'Morning', 3),
       (4, 'Between 9am and 10.30am', 'Late Morning', 4),
       (5, 'Between 10.30 and 12pm', 'Early Afternoon', 5),
       (6, 'Between 12pm and 2pm', 'Afternoon', 6),
       (7, 'Between 2pm and 3.30pm', 'Late Afternoon', 7),
       (8, 'Between 3.30pm and 4.30pm', 'Early Evening', 8),
       (9, 'Between 4.30pm and 7pm', 'Evening', 9),
       (10, 'Between 7pm and 9.30pm', 'Late Evening', 10),
       (11, 'Between 9.30pm and 12am', 'Midnight Evening', 11),
       (12, 'Between 12am and 3.30am', 'Midnight Morning', 1)
ON CONFLICT (daypartid) DO NOTHING;

-- Batch insert data into the "time".daynames table
INSERT INTO "time".daynames (daynameid, dayabbreviation, daybusinessdayclassification, dayisbusinessday,
                             daylongabbreviation, dayname, dayshortname, daysortorder)
VALUES (1, 'S', 'Weekend', 0, 'Su', 'Sunday', 'Sun', 0),
       (2, 'M', 'Weekday', 1, 'Mo', 'Monday', 'Mon', 1),
       (3, 'T', 'Weekday', 1, 'Tu', 'Tuesday', 'Tue', 2),
       (4, 'W', 'Weekday', 1, 'We', 'Wednesday', 'Wed', 3),
       (5, 'T', 'Weekday', 1, 'Th', 'Thursday', 'Thur', 4),
       (6, 'F', 'Weekday', 1, 'Fr', 'Friday', 'Fri', 5),
       (7, 'S', 'Weekend', 0, 'Sa', 'Saturday', 'Sat', 6)
ON CONFLICT (daynameid) DO NOTHING;



CREATE INDEX IF NOT EXISTS idx_td_daynameid ON "time".days (daynameid);
CREATE INDEX IF NOT EXISTS idx_td_weekid ON "time".days (weekid);
CREATE INDEX IF NOT EXISTS idx_td_monthid ON "time".days (monthid);
CREATE INDEX IF NOT EXISTS idx_hd_daypartid ON "time".halfhourdayparts (daypartid);
CREATE INDEX IF NOT EXISTS idx_md_monthofyearid ON "time".months (monthofyearid);
CREATE INDEX IF NOT EXISTS idx_md_quarterid ON "time".months (quarterid);
CREATE INDEX IF NOT EXISTS idx_qd_yearid ON "time".quarters (yearid);

CREATE INDEX IF NOT EXISTS idx_pd_daypartname ON "time".dayparts (daypartname);
CREATE INDEX IF NOT EXISTS idx_ph_publicholidayname ON "time".publicholidays (publicholidayname);
CREATE INDEX IF NOT EXISTS idx_my_monthofyearname ON "time".monthofyear (monthofyearname);
CREATE INDEX IF NOT EXISTS idx_my_monthofyearshortname ON "time".monthofyear (monthofyearshortname);
CREATE INDEX IF NOT EXISTS idx_h_ampmdesc ON "time".hours (ampmdesc);
CREATE INDEX IF NOT EXISTS idx_h_twelvehourclockdesc ON "time".hours (twelvehourclockdesc);
CREATE INDEX IF NOT EXISTS idx_h_twentyfourhourclockdesc ON "time".hours (twentyfourhourclockdesc);
CREATE INDEX IF NOT EXISTS idx_t_ampmdesc ON "time".time (ampmdesc);
CREATE INDEX IF NOT EXISTS idx_t_twelvehourclockdesc ON "time".time (twelvehourclockdesc);
CREATE INDEX IF NOT EXISTS idx_t_twentyfourhourclockdesc ON "time".time (twentyfourhourclockdesc);
CREATE INDEX IF NOT EXISTS idx_hd_ampmdesc ON "time".halfhours (ampmdesc);
CREATE INDEX IF NOT EXISTS idx_hd_twelvehourclockdesc ON "time".halfhours (twelvehourclockdesc);
CREATE INDEX IF NOT EXISTS idx_hd_twentyfourhourclockdesc ON "time".halfhours (twentyfourhourclockdesc);
CREATE INDEX IF NOT EXISTS idx_y_yyname ON "time".years (yyname);
CREATE INDEX IF NOT EXISTS idx_y_yyyname ON "time".years (yyyname);
CREATE INDEX IF NOT EXISTS idx_y_yearfullname ON "time".years (yearfullname);
CREATE INDEX IF NOT EXISTS idx_y_yearname ON "time".years (yearname);
CREATE INDEX IF NOT EXISTS idx_d_daydatetime ON "time".days (daydatetime);
