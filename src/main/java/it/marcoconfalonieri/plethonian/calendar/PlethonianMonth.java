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

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.SortedSet;

/**
 * Plethonian month.
 */
public class PlethonianMonth implements Serializable, Comparable {
    /**
     * Serial Version ID.
     */
    private static final long serialVersionUID = 4735398479964150289L;
    /**
     * Month.
     */
    private PlethonianMonthName month;
    /**
     * Days.
     */
    private SortedSet<PlethonianDay> days;
    /**
     * Starting day.
     */
    private LocalDate firstDay;

    /**
     * Getter for the month.
     * 
     * @return the month
     */
    public PlethonianMonthName getMonth() {
        return month;
    }

    /**
     * Setter for the month.
     * 
     * @param month the month to set
     */
    public void setMonth(PlethonianMonthName month) {
        this.month = month;
    }

    /**
     * Getter for the days.
     * 
     * @return the days
     */
    public SortedSet<PlethonianDay> getDays() {
        return days;
    }

    /**
     * Setter for the days.
     * 
     * @param days the days to set
     */
    public void setDays(SortedSet<PlethonianDay> days) {
        this.days = days;
    }
    
    @Override
    public int compareTo(Object obj) {
        if (!(obj instanceof PlethonianMonth)) {
            throw new IllegalArgumentException(
                    "Argument must be of PlethonianMonth type.");
        }
        PlethonianMonth pm = (PlethonianMonth) obj;
        return firstDay.compareTo(pm.firstDay);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlethonianMonth)) {
            throw new IllegalArgumentException(
                    "Argument must be of PlethonianMonth type.");
        }
        PlethonianMonth pm = (PlethonianMonth) obj;
        return firstDay.equals(pm.firstDay);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.firstDay);
        return hash;
    }

    /**
     * Getter for the first day.
     * 
     * @return the firstDay
     */
    public LocalDate getFirstDay() {
        return firstDay;
    }

    /**
     * Setter for the first day.
     * 
     * @param firstDay the firstDay to set
     */
    public void setFirstDay(LocalDate firstDay) {
        this.firstDay = firstDay;
    }
}
