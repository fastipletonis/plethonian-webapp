/*
 *     plethonian-webapp - Plethonian calendar Web Application
 *
 *     Copyright (C) 2020 Marco Confalonieri <marco at marcoconfalonieri.it>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.marcoconfalonieri.plethonian.calendar.astropixel;

import it.marcoconfalonieri.plethonian.calendar.MonthlyFestivity;
import it.marcoconfalonieri.plethonian.calendar.PlethonianCalendar;
import it.marcoconfalonieri.plethonian.calendar.PlethonianDay;
import it.marcoconfalonieri.plethonian.calendar.PlethonianMonth;
import it.marcoconfalonieri.plethonian.calendar.PlethonianMonthName;
import it.marcoconfalonieri.plethonian.calendar.PlethonianWeekName;
import it.marcoconfalonieri.plethonian.calendar.PlethonianYear;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Implementation of the Plethonian calendar through AstroPixel's data.
 */
public class PlethonianCalendarImpl implements PlethonianCalendar {
    private static final String[] DAY_LABELS = { "new", "2", "3", "4", "5", "6",
        "7", "8", "7", "6", "5", "4", "3", "2", "half", "2", "3", "4", "5", "6",
        "7", "8", "7", "6", "5", "4", "3", "2", "(", ")(" };
    /**
     * AstroPixel's moon phases table.
     */
    private static final String RES_MOON_PHASES
            = "com/astropixels/moon/lunar-phases.txt";
    /**
     * AstroPixel's moon phases table layout.
     */
    private static final int[] POS_MOON_PHASES = {0, 24, 42, 58, 69};
    /**
     * AstroPixel's sun solstices and equinoxes table.
     */
    private static final String RES_SUN_SOLSTICES
            = "com/astropixels/sun/solstices-equinoxes.txt";
    /**
     * AstroPixel's sun solstices and equinoxes table layout.
     */
    private static final int[] POS_SUN_SOLSTICES = {1, 11, 29, 47, 65};
    /**
     * Map of the plethonian months numeric values.
     */
    private static final Map<Integer, PlethonianMonthName> MONTHS
            = new HashMap<>();
    /**
     * Map of the plethonian week numeric values.
     */
    private static final Map<Integer, PlethonianWeekName> WEEKS
            = new HashMap<>();
    /**
     * Map of the plethonian festivities in the month.
     */
    private static final Map<Integer, MonthlyFestivity> FESTIVITIES
            = new HashMap<>();
    /**
     * Map of the years.
     */
    private final SortedSet<PlethonianYear> yearsSet = new TreeSet<>();

    /**
     * Initializes the map of the months.
     */
    private static void initializeMonthsMap() {
        for (PlethonianMonthName m : PlethonianMonthName.values()) {
            MONTHS.put(m.toInt(), m);
        }
    }

    /**
     * Initializes the map of the weeks.
     */
    private static void initializeWeeksMap() {
        for (PlethonianWeekName w : PlethonianWeekName.values()) {
            WEEKS.put(w.toInt(), w);
        }
    }

    /**
     * Initializes the map of the festivities.
     */
    private static void initializeFestivitiesMap() {
        for (MonthlyFestivity f : MonthlyFestivity.values()) {
            FESTIVITIES.put(f.getDay(), f);
        }
    }

    // Initializes the required static maps.
    static {
        initializeMonthsMap();
        initializeWeeksMap();
        initializeFestivitiesMap();
    }

    /**
     * Reads a table from a resource.
     *
     * @param res the resource name
     * @param pos the table layout
     *
     * @return a matrix of the results
     *
     * @throws IOException in case of errors reading the resource
     */
    private String[][] readTable(String res, int[] pos) throws IOException {
        TableReader tr = new TableReader(res, pos);
        return tr.readTable();
    }

    /**
     * Creates the map with all the phases.
     *
     * @throws IOException in case of errors reading the resource or the data
     */
    private SortedMap<ZonedDateTime, LunarMonth> createMonthsMap() throws IOException {
        SortedMap<ZonedDateTime, LunarMonth> monthsMap = new TreeMap<>();
        String[][] moonPhases = readTable(RES_MOON_PHASES, POS_MOON_PHASES);
        try {
            for (String[] row : moonPhases) {
                LunarMonth lm = new LunarMonth(row);
                monthsMap.put(lm.getNewMoon(), lm);
            }
        } catch (DataException ex) {
            throw new IOException("Cannot read table data", ex);
        }
        return monthsMap;
    }

    /**
     * Creates the solstices set.
     *
     * @throws IOException in case of errors reading the resource or the data
     */
    private SortedSet<ZonedDateTime> createSolsticesSet() throws IOException {
        SortedSet<ZonedDateTime> solsticesSet = new TreeSet<>();
        String[][] solstices = readTable(RES_SUN_SOLSTICES, POS_SUN_SOLSTICES);
        try {
            for (String[] row : solstices) {
                SolarYear sy = new SolarYear(row);
                solsticesSet.add(sy.getWinterSolstice());
            }
        } catch (DataException ex) {
            throw new IOException("Cannot read table data", ex);
        }
        return solsticesSet;
    }

    /**
     * Creates a Plethonian day from the parameters.
     * 
     * @param day the day of the month
     * @param month the Plethonian month name
     * @param dayOfYear the day of the year
     * @param gregorianDate the Gregorian date for the day
     * @param remDayFlag the flag value for remembrance day
     * 
     * @return a PlethonianDay object with the required information
     */
    private PlethonianDay createDay(int day, PlethonianMonthName month,
            int dayOfYear, LocalDate gregorianDate, boolean remDayFlag) {
        // Calculates the week
        int week = (day - 1) / 7 + 1;
        
        PlethonianDay pd = new PlethonianDay();
        pd.setDayOfMonth(day);
        pd.setDayOfYear(dayOfYear);
        pd.setDefunctDay(remDayFlag);
        pd.setWeek(WEEKS.get(week));
        pd.setMonth(month);
        pd.setGregorianDate(gregorianDate);
        pd.setMonthFestivity(FESTIVITIES.get(day));
        pd.setLabel(DAY_LABELS[day - 1]);
        return pd;
    }
    
    /**
     * Creates a set of days for the current month.
     * 
     * @param yearDay the day of the year this month starts with
     * @param days number of days
     * @param firstDay the first day's Gregorian date
     * @param remDay the value for the remembrance day
     * @param monthName the name of the month
     * 
     * @return the set of days with the required information
     */
    private SortedSet<PlethonianDay> createMonthDaysSet(int yearDay, int days,
            LocalDate firstDay, int remDay, PlethonianMonthName monthName) {
        SortedSet<PlethonianDay> daysSet = new TreeSet<>();
        LocalDate gregorianDay = firstDay;
        for (int day = 1; day <= days; day++) {
            boolean remDayFlag = (remDay == yearDay);
            PlethonianDay pd = createDay(day, monthName, yearDay,
                    gregorianDay, remDayFlag);
            daysSet.add(pd);
            yearDay++;
            gregorianDay = gregorianDay.plusDays(1);
        }
        // The last day of the month has ALWAYS the old/new label.
        daysSet.last().setLabel(DAY_LABELS[29]);
        return daysSet;
    }
    
    /**
     * Creates a month object from the given parameters.
     * 
     * @param firstDay the first day of the month
     * @param days the number of days in the month
     * @param monthCount the numeric value of the month, starting from 1
     * @param yearStartDay the day of the year at the beginning of the month
     * @param remDay the remembrance day in the Plethonian calendar
     * 
     * @return a PlethonianMonth object
     */
    private PlethonianMonth createMonth(LocalDate firstDay, int days,
            int monthCount, int yearStartDay, int remDay) {
        PlethonianMonth month = new PlethonianMonth();
        month.setMonth(MONTHS.get(monthCount));
        month.setFirstDay(firstDay);
        month.setDays(createMonthDaysSet(yearStartDay, days, firstDay, remDay,
                month.getMonth()));
        return month;
    }

    /**
     * Creates a Plethonian year.
     * 
     * @param yearMonths the months to include in the year
     * @param next beginning of the next year
     * 
     * @return a PlethonianYear object with the required data
     */
    private PlethonianYear createYear(
            SortedMap<ZonedDateTime, LunarMonth> yearMonths,
            ZonedDateTime next) {
        final PlethonianYear year = new PlethonianYear();
        
        year.setFirstDay(yearMonths.firstKey().toLocalDate().plusDays(1));
        year.setMonths(new TreeSet<>());
        year.setDays(year.getFirstDay().until(next.toLocalDate().plusDays(1))
                .getDays());
        
        final int remDay = year.getDays() - 2;
        Consumer<ZonedDateTime> mc = new Consumer<ZonedDateTime>() {
            int monthCounter = 1;
            LocalDate firstOfMonth = null;
            int startDay = 1;

            @Override
            public void accept(ZonedDateTime zdt) {
                if (firstOfMonth == null) {
                    firstOfMonth = zdt.toLocalDate().plusDays(1);
                } else {
                    LocalDate nextFirstOfMonth
                            = zdt.toLocalDate().plusDays(1);
                    int days = (int) ChronoUnit.DAYS.between(firstOfMonth,
                            nextFirstOfMonth);
                    PlethonianMonth month = createMonth(firstOfMonth, days,
                            monthCounter, startDay, remDay);
                    year.getMonths().add(month);
                    firstOfMonth = nextFirstOfMonth;
                    monthCounter++;
                    startDay += days;
                }
            }
        };
        
        Stream.concat(yearMonths.keySet().stream(), Stream.of(next))
                .forEach(mc);
        return year;
    }

    /**
     * Creates a set with the years.
     * 
     * @throws IOException in case of errors reading the tables
     */
    private void createYearsSet() throws IOException {
        SortedSet<ZonedDateTime> solsticesSet = createSolsticesSet();
        SortedMap<ZonedDateTime, LunarMonth> monthsMap = createMonthsMap();
        while (!solsticesSet.isEmpty()) {
            ZonedDateTime firstSol = solsticesSet.first();
            solsticesSet.remove(firstSol);
            if (solsticesSet.isEmpty()) {
                break;
            }
            ZonedDateTime lastSol = solsticesSet.first();
            SortedMap<ZonedDateTime, LunarMonth> yearMonths
                    = monthsMap.subMap(firstSol, lastSol);
            ZonedDateTime next = monthsMap.tailMap(lastSol).firstKey();
            PlethonianYear year = createYear(yearMonths, next);
            yearsSet.add(year);
        }
    }

    /**
     * Constructor. It reads the tables with the astronomical data and
     * initializes the object.
     *
     * @throws IOException in case of errors reading the required resources
     */
    public PlethonianCalendarImpl() throws IOException {
        createYearsSet();
    }

    /**
     * Returns a year based on the given Gregorian date.
     * 
     * @param date the reference date.
     * 
     * @return the year
     * 
     * @throws IllegalArgumentException if the argument refers to an unsupported
     *     date
     */
    @Override
    public PlethonianYear getYear(LocalDate date) {
        int gregorianYear = date.getYear();
        
        if (gregorianYear < 2001 || gregorianYear > 2100) {
            String msg = String.format("The year %d is not in the valid range",
                    gregorianYear);
            throw new IllegalArgumentException(msg);
        }
        
        PlethonianYear model = new PlethonianYear();
        model.setFirstDay(date);
        
        PlethonianYear year;
        try {
            SortedSet<PlethonianYear> tailSet = yearsSet.tailSet(model);
            year = tailSet.isEmpty()? null : yearsSet.tailSet(model).first();
            if (year == null || !model.equals(year)) {
                year = yearsSet.headSet(model).last();
            }
        } catch (NoSuchElementException ex) {
            String msg = "Cannot find a Plethonian year for " + date.toString();
            
            throw new IllegalArgumentException(msg, ex);
        }
        return year;
    }

    /**
     * returns a month based on the given Gregorian date.
     * 
     * @param date the reference date
     * 
     * @return the month
     * 
     * @throws IllegalArgumentException if the argument refers to an unsupported
     *     date
     */
    @Override
    public PlethonianMonth getMonth(LocalDate date) {
        PlethonianYear year = getYear(date);
        for (PlethonianMonth m : year.getMonths()) {
            System.out.println(m.getMonth() + " " + m.getFirstDay());
        }
        PlethonianMonth model = new PlethonianMonth();
        model.setFirstDay(date);
        
        PlethonianMonth month;
        try {
            SortedSet<PlethonianMonth> tailSet =
                    year.getMonths().tailSet(model);
            month = tailSet.isEmpty()? null :
                    year.getMonths().tailSet(model).first();
            if (month == null || !model.equals(month)) {
                month = year.getMonths().headSet(model).last();
            }
        } catch (NoSuchElementException ex) {
            String msg = "Cannot find a Plethonian month for " + date.toString();
            
            throw new IllegalArgumentException(msg, ex);
        }
        return month;
    }

    /**
     * Returns a day based on the given Gregorian date.
     * 
     * @param date the reference date
     * 
     * @return the day
     * 
     * @throws IllegalArgumentException if the argument refers to an unsupported
     *     date
     */
    @Override
    public PlethonianDay getDay(LocalDate date) {
        PlethonianMonth month = getMonth(date);
        
        PlethonianDay model = new PlethonianDay();
        model.setGregorianDate(date);
        
        PlethonianDay day;
        try {
            SortedSet<PlethonianDay> tailSet = month.getDays().tailSet(model);
            day = tailSet.isEmpty()? null :
                    month.getDays().tailSet(model).first();
            if (day == null || !model.equals(day)) {
                day = month.getDays().headSet(model).last();
            }
        } catch (NoSuchElementException ex) {
            String msg = "Cannot find a Plethonian day for " + date.toString();
            
            throw new IllegalArgumentException(msg, ex);
        }
        return day;
    }
}
