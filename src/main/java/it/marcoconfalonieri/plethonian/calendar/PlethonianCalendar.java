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

import java.time.LocalDate;

/**
 * Interface for a Plethonian calendar.
 */
public interface PlethonianCalendar {
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
    PlethonianYear getYear(LocalDate date);

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
    PlethonianMonth getMonth(LocalDate date);

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
    PlethonianDay getDay(LocalDate date);
}
