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
 * Plethonian year.
 */
public class PlethonianYear implements Serializable, Comparable {
    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = 3856220066722200618L;
    /**
     * The first day of the current Plethonian year.
     */
    private LocalDate firstDay;
    /**
     * The number of days in the year.
     */
    private int days;
    /**
     * The months in the year.
     */
    private SortedSet<PlethonianMonth> months;

    /**
     * Getter for the first day field.
     *
     * @return the firstDay
     */
    public LocalDate getFirstDay() {
        return firstDay;
    }

    /**
     * Setter for the first day field.
     *
     * @param firstDay the firstDay to set
     */
    public void setFirstDay(LocalDate firstDay) {
        this.firstDay = firstDay;
    }

    /**
     * Getter for the days field.
     *
     * @return the days field
     */
    public int getDays() {
        return days;
    }

    /**
     * Setter for the days field.
     *
     * @param days the days field to set
     */
    public void setDays(int days) {
        this.days = days;
    }

    /**
     * Getter for the months.
     *
     * @return the months
     */
    public SortedSet<PlethonianMonth> getMonths() {
        return months;
    }

    /**
     * Setter for the months.
     *
     * @param months the months to set
     */
    public void setMonths(SortedSet<PlethonianMonth> months) {
        this.months = months;
    }

    @Override
    public int compareTo(Object obj) {
        if (!(obj instanceof PlethonianYear)) {
            throw new IllegalArgumentException(
                    "Argument must be of PlethonianMonth type.");
        }
        PlethonianYear py = (PlethonianYear) obj;
        return this.firstDay.compareTo(py.firstDay);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlethonianYear)) {
            return false;
        }
        PlethonianYear py = (PlethonianYear) obj;
        return this.firstDay.equals(py.firstDay);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.firstDay);
        return hash;
    }

}
