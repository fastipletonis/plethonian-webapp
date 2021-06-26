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

package it.marcoconfalonieri.plethonian.calendar;

/**
 * Festivity of the day.
 */
public enum Festivity {
    
    // HOLIDAYS REPEATED MONTHLY
    /**
     * The New Moon of each month is consecrated to Jupiter.
     */
    JUPITER(0, 1, true),
    /**
     * The eighth day of the month marks the limit between the Starting Period
     * and the Median Period. It is a festivity, but it is not consecrated.
     */
    FIRST_QUARTER(0, 8, false),
    /**
     * The sixteenth day of the month marks the limit between the Median Period
     * and the Declining Period. It is a festivity, but it is not consecrated.
     */
    FULL_MOON(0, 15, false),
    /**
     * The twenty second day of the month marks the limit between the Declining
     * Period and the Conclusive Period. It is a festivity, but it is not
     * consecrated.
     */
    LAST_QUARTER(0, 22, false),
    /**
     * The twenty ninth day of the month is consecrated to Pluto.
     */
    PLUTO(0, 29, true),
    /**
     * The last day of the month is dedicated to introspection.
     */
    INTROSPECTION(0, -1, false),
    
    // HOLIDAYS REPEATED YEARLY
    /**
     * The second day of the first month.
     */
    SECOND_DAY(1, 2, false),
    /**
     * The third day of the first month.
     */
    THIRD_DAY(1, 3, false),
    /**
     * The eighth day of the fourth month.
     */
    EIGHTH_DAY_FOURTH_MONTH(4, 8, false),
    /**
     * Half of the seventh month.
     */
    HALF_SEVENTH_MONTH(7, 15, false),
    /**
     * Eighth day from the end of the tenth month.
     */
    EIGHTH_FROM_END_TENTH_MONTH(10, -8, false);
    
    /**
     * Month of the festivity.
     */
    private final int month;
    /**
     * Day of the festivity.
     */
    private final int day;
    /**
     * Consecrated day.
     */
    private final boolean consecrated;

    /**
     * The festivity constructor. The parameters describe when does the
     * festivity happen and if it is a consecrated day. If the
     * <code>month</code> parameter is <code>0</code>, the festivity
     * repeat itself monthly. A negative number means that the day must be
     * counted from the end of the month.
     * 
     * @param month the month of the festivity or <code>0</code>
     * @param day the day of the festivity
     * @param consecrated indicates if the day is consecrated
     */
    Festivity(int month, int day, boolean consecrated) {
        this.month = month;
        this.day = day;
        this.consecrated = consecrated;
    }

    /**
     * Returns the month of the festivity, or <code>0</code> for the ones
     * that repeat monthly.
     * 
     * @return the month or <code>0</code>
     */
    public int getMonth() {
        return month;
    }
    
    /**
     * Returns the day associated with the festivity. It can be negative if it
     * is calculated from the end of the month.
     * 
     * @return the day
     */
    public int getDay() {
        return day;
    }

    /**
     * The flag is <code>true</code> if the festivity is a consecration.
     * 
     * @return <code>true</code> if consecrated
     */
    public boolean isConsecrated() {
        return consecrated;
    }
}
