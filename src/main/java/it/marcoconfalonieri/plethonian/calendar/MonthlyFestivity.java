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

package it.marcoconfalonieri.plethonian.calendar;

/**
 * Festivities.
 */
public enum MonthlyFestivity {
    JUPITER(1), NEPTUNE(8), JUNO(15), GODS(22),
    PLUTO(29), INTROSPECTION(30);
    
    /**
     * Day of the festivity.
     */
    private final int day;

    MonthlyFestivity(int day) {
        this.day = day;
    }
    
    /**
     * Returns the day associated with the festivity.
     * 
     * @return the day
     */
    public int getDay() {
        return day;
    }
}
