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

import it.marcoconfalonieri.plethonian.calendar.Festivity;
import it.marcoconfalonieri.plethonian.calendar.PlethonianDay;
import it.marcoconfalonieri.plethonian.calendar.PlethonianMonthName;
import it.marcoconfalonieri.plethonian.calendar.PlethonianWeekName;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Date creation static methods.
 */
public class DateCreator {
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
    private static final Map<Integer, Festivity> MONTH_FESTIVITIES
            = new HashMap<>();
    /**
     * Map of yearly festivities. 
     */
    private static final Map<String, Festivity> YEAR_FESTIVITIES
            = new HashMap<>();
    
    /**
     * Creates the key for accessing the <code>YEAR_FESTIVITIES</code> map.
     * 
     * @param month the month
     * @param day the day
     * 
     * @return the resulting key
     */
    private static String getYearFestivityKey(int month, int day) {
        return month + ":" + day;
    }
    
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
        for (Festivity f : Festivity.values()) {
            if (f.getMonth() == 0) {
                MONTH_FESTIVITIES.put(f.getDay(), f);
            } else {
                String key = getYearFestivityKey(f.getMonth(), f.getDay());
                YEAR_FESTIVITIES.put(key, f);
            }
        }
    }

    // Initializes the required static maps.
    static {
        initializeMonthsMap();
        initializeWeeksMap();
        initializeFestivitiesMap();
    }
    
    /**
     * Returns the set of festivities associated with the parameters given.
     * 
     * @param month the month number
     * @param day the day of the month
     * @param numMonths the number of months in the year
     * @param numDays the number of days in the month
     * 
     * @return the set of festivities associated with the day
     */
    protected static SortedSet<Festivity> getFestivities(int month, int day,
            int numMonths, int numDays) {
        // Index for backward day searches
        final int negDay = (day - 1) - numDays;
        // Index for backward month searches
        final int negMonth = (month - 1) - numMonths;
        // Index for forward searches in year festivities
        final String yearDayKey = getYearFestivityKey(month, day);
        // Index for backward searches in year festivities
        final String yearNegDayKey = getYearFestivityKey(month, negDay);
        // Index for forward search in backward year festivities
        final String negYearDayKey = getYearFestivityKey(negMonth, day);
        // Index for backward search in backward year festivities
        final String negYearNegDayKey = getYearFestivityKey(negMonth, negDay);
        
        SortedSet<Festivity> set = new TreeSet<>();
        
        // Add month festivity - forward search
        if (MONTH_FESTIVITIES.containsKey(day)) {
                set.add(MONTH_FESTIVITIES.get(day));
        }
        
        // Add month festivity - backward search
        if (MONTH_FESTIVITIES.containsKey(negDay)) {
                set.add(MONTH_FESTIVITIES.get(negDay));
        }
        
        // Add year festivity - forward search
        if (YEAR_FESTIVITIES.containsKey(yearDayKey)) {
            set.add(YEAR_FESTIVITIES.get(yearDayKey));
        }
        
        // Add year festivity - backward search
        if (YEAR_FESTIVITIES.containsKey(yearNegDayKey)) {
            set.add(YEAR_FESTIVITIES.get(yearNegDayKey));
        }

        // Add year festivity - forward search
        if (YEAR_FESTIVITIES.containsKey(negYearDayKey)) {
            set.add(YEAR_FESTIVITIES.get(negYearDayKey));
        }
        
        // Add year festivity - backward search
        if (YEAR_FESTIVITIES.containsKey(negYearNegDayKey)) {
            set.add(YEAR_FESTIVITIES.get(negYearNegDayKey));
        }        
        return set;
    }
    
    /**
     * Creates a Plethonian day from the parameters.
     * 
     * @param day the day of the month
     * @param month the Plethonian month number
     * @param dayOfYear the day of the year
     * @param numMonths number of months in the year
     * @param monthDays number of days in the month
     * @param gregorianDate the Gregorian date for the day
     * 
     * @return a PlethonianDay object with the required information
     */
    public static PlethonianDay createDay(int day, int month, int dayOfYear,
            int numMonths, int monthDays, LocalDate gregorianDate) {
        
        // Calculates the week
        int weekNumber = (day - 1) / 7 + 1;
        
        // Gets the festivities
        SortedSet<Festivity> festivities = getFestivities(month, day,
                numMonths, monthDays);
        
        // Builds the object.
        PlethonianDay pd = new PlethonianDay();
        pd.setDayOfMonth(day);
        pd.setDayOfYear(dayOfYear);
        pd.setWeek(WEEKS.get(weekNumber));
        pd.setMonth(MONTHS.get(month));
        pd.setGregorianDate(gregorianDate);
        pd.setFestivity(festivities.isEmpty()? null : festivities.last());
        
        return pd;
    }
    
    /**
     * Returns a month from its numeric value. If there is no month associated,
     * it returns <code>null</code>.
     * 
     * @param month the month number
     * 
     * @return the Plethonian month
     */
    public static PlethonianMonthName getMonth(int month) {
        return MONTHS.get(month);
    }

}
