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

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * POJO for storing information on a Plethonian day.
 */
public class PlethonianDay implements Serializable, Comparable {
    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = -7766642065144440904L;
    /**
     * The day of the month.
     */
    private int dayOfMonth;
    /**
     * The day of the year.
     */
    private int dayOfYear;
    /**
     * The month.
     */
    private PlethonianMonthName month;
    /**
     * The week.
     */
    private PlethonianWeekName week;
    /**
     * Flag that indicates if it's the Plethonian day dedicated to Pluto and the
     * defunct.
     */
    private boolean defunctDay;
    /**
     * Gregorian date.
     */
    private LocalDate gregorianDate;
    /**
     * Monthly festivity.
     */
    private MonthlyFestivity monthFestivity;
    /**
     * Day label.
     */
    private String label;

    /**
     * Getter for the day of the month.
     * 
     * @return the dayOfMonth field
     */
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * Setter for the day of the month.
     * 
     * @param dayOfMonth the dayOfMonth field to set
     */
    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    /**
     * Getter for the day of the year.
     * 
     * @return the dayOfYear field.
     */
    public int getDayOfYear() {
        return dayOfYear;
    }

    /**
     * Setter for the day of the year.
     * 
     * @param dayOfYear the dayOfYear field to set
     */
    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    /**
     * Getter for the month field.
     * 
     * @return the month field
     */
    public PlethonianMonthName getMonth() {
        return month;
    }

    /**
     * Setter for the month field.
     * 
     * @param month the month field to set
     */
    public void setMonth(PlethonianMonthName month) {
        this.month = month;
    }

    /**
     * Getter for the week field.
     * 
     * @return the week field
     */
    public PlethonianWeekName getWeek() {
        return week;
    }

    /**
     * Setter for the week field.
     * 
     * @param week the week to set
     */
    public void setWeek(PlethonianWeekName week) {
        this.week = week;
    }

    /**
     * Check if the day is marked as defunct day.
     * 
     * @return the defunctDay field
     */
    public boolean isDefunctDay() {
        return defunctDay;
    }

    /**
     * Set or clear the defunctDay flag.
     * 
     * @param defunctDay the defunctDay field to set
     */
    public void setDefunctDay(boolean defunctDay) {
        this.defunctDay = defunctDay;
    }

    @Override
    public int compareTo(Object obj) {
        if (!(obj instanceof PlethonianDay)) {
            throw new IllegalArgumentException(
                    "Argument must be of PlethonianDay type.");
        }
        PlethonianDay pd = (PlethonianDay) obj;
        return this.gregorianDate.compareTo(pd.gregorianDate);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlethonianDay)) {
            throw new IllegalArgumentException(
                    "Argument must be of PlethonianDay type.");
        }
        PlethonianDay pd = (PlethonianDay) obj;
        return gregorianDate.equals(pd.gregorianDate);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.gregorianDate);
        return hash;
    }

    /**
     * Getter for the Gregorian date.
     * 
     * @return the gregorianDate
     */
    public LocalDate getGregorianDate() {
        return gregorianDate;
    }

    /**
     * Setter for the Gregorian date.
     * 
     * @param gregorianDate the gregorianDate to set
     */
    public void setGregorianDate(LocalDate gregorianDate) {
        this.gregorianDate = gregorianDate;
    }

    /**
     * Getter for the monthly festivity.
     * 
     * @return the monthFestivity
     */
    public MonthlyFestivity getMonthFestivity() {
        return monthFestivity;
    }

    /**
     * Setter for the monthly festivity.
     * 
     * @param monthFestivity the monthFestivity to set
     */
    public void setMonthFestivity(MonthlyFestivity monthFestivity) {
        this.monthFestivity = monthFestivity;
    }

    /**
     * Get the label.
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the label.
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

}
