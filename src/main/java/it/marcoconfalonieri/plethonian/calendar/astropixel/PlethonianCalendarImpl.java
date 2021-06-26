/*
 *     plethonian-webapp - Plethonian calendar Web Application
 *
 *     Copyright (C) 2020 Marco Confalonieri <marco at marcoconfalonieri.it>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
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

import it.marcoconfalonieri.plethonian.calendar.PlethonianCalendar;
import it.marcoconfalonieri.plethonian.calendar.PlethonianDay;
import it.marcoconfalonieri.plethonian.calendar.PlethonianMonth;
import it.marcoconfalonieri.plethonian.calendar.PlethonianYear;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
     * Map of the years.
     */
    private final SortedSet<PlethonianYear> yearsSet = new TreeSet<>();

    

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
    private SortedMap<ZonedDateTime, LunarMonth> createMonthsMap()
            throws IOException {
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
     * Creates a set of days for the current month.
     * 
     * @param yearDay the day of the year this month starts with
     * @param days number of days
     * @param numMonths the number of months in the year
     * @param firstDay the first day's Gregorian date
     * @param remDay the value for the remembrance day
     * @param month the month
     * 
     * @return the set of days with the required information
     */
    private SortedSet<PlethonianDay> createMonthDaysSet(int yearDay, int days,
            int numMonths, LocalDate firstDay, int month) {
        SortedSet<PlethonianDay> daysSet = new TreeSet<>();
        LocalDate gregorianDay = firstDay;
        for (int day = 1; day <= days; day++) {
            PlethonianDay pd = DateCreator.createDay(day, month, yearDay,
                    numMonths, days, gregorianDay);
            daysSet.add(pd);
            yearDay++;
            gregorianDay = gregorianDay.plusDays(1);
        }
        return daysSet;
    }
    
    /**
     * Creates a month object from the given parameters.
     * 
     * @param firstDay the first day of the month
     * @param days the number of days in the month
     * @param numMonths the number of months in the year
     * @param monthCount the numeric value of the month, starting from 1
     * @param yearStartDay the day of the year at the beginning of the month
     * @param remDay the remembrance day in the Plethonian calendar
     * 
     * @return a PlethonianMonth object
     */
    private PlethonianMonth createMonth(LocalDate firstDay, int days,
            int numMonths, int monthCount, int yearStartDay) {
        PlethonianMonth month = new PlethonianMonth();
        month.setMonth(DateCreator.getMonth(monthCount));
        month.setFirstDay(firstDay);
        month.setDays(createMonthDaysSet(yearStartDay, days, numMonths,
                firstDay, monthCount));
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
        
        Consumer<ZonedDateTime> mc = new Consumer<ZonedDateTime>() {
            int numMonths = (year.getDays() > 360)? 13 : 12;
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
                            numMonths, monthCounter, startDay);
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
            String msg = "Cannot find a Plethonian month for "
                    + date.toString();
            
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
