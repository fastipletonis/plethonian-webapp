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

package it.marcoconfalonieri.plethonian.webapp.controller;

import it.marcoconfalonieri.plethonian.calendar.PlethonianCalendar;
import it.marcoconfalonieri.plethonian.calendar.PlethonianDay;
import it.marcoconfalonieri.plethonian.calendar.PlethonianMonth;
import it.marcoconfalonieri.plethonian.calendar.PlethonianWeekName;
import it.marcoconfalonieri.plethonian.calendar.astropixel.PlethonianCalendarImpl;
import java.io.IOException;
import java.time.LocalDate;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Application-scoped bean.
 */
@ApplicationScoped
@Named("app")
public class PlethonianCalendarApp {

    /**
     * The calendar object.
     */
    private PlethonianCalendar calendar;

    private PlethonianDay today;

    private PlethonianMonth currentMonth;

    private final SortedMap<PlethonianWeekName, SortedSet<PlethonianDay>> currentMonthMatrix = new TreeMap<>();

    /**
     * Loads the current month matrix.
     */
    protected void loadCurrentMonthMatrix() {
        getCurrentMonthMatrix().entrySet().forEach(pair -> pair.getValue().clear());
        currentMonth.getDays().forEach(day -> getCurrentMonthMatrix().get(day.getWeek()).add(day)
        );
    }

    /**
     * Updates the loaded data if needed.
     */
    protected void updateData() {
        LocalDate ld = LocalDate.now();
        if (today == null || !today.getGregorianDate().equals(ld)) {
            today = calendar.getDay(ld);
            currentMonth = calendar.getMonth(ld);
            loadCurrentMonthMatrix();
        }
    }

    /**
     * Initializes the current month matrix.
     */
    private void initializeCurrentMonthMatrix() {
        for (PlethonianWeekName week : PlethonianWeekName.values()) {
            currentMonthMatrix.put(week, new TreeSet<>());
        }
    }

    /**
     * Constructor.
     */
    public PlethonianCalendarApp() {
        initializeCurrentMonthMatrix();
    }

    /**
     * initializes the object by creating the internal calendar.
     */
    @PostConstruct
    public void initialize() {
        try {
            calendar = new PlethonianCalendarImpl();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Getter for the calendar object.
     *
     * @return the calendar
     */
    public PlethonianCalendar getCalendar() {
        return calendar;
    }

    /**
     * Getter for today.
     *
     * @return the plethonian day for today.
     */
    public PlethonianDay getToday() {
        updateData();
        return today;
    }

    /**
     * Getter for the current month.
     *
     * @return the current plethonian month.
     */
    public PlethonianMonth getCurrentMonth() {
        updateData();
        return currentMonth;
    }

    /**
     * Gets the month matrix.
     *
     * @return the currentMonthMatrix
     */
    public SortedMap<PlethonianWeekName, SortedSet<PlethonianDay>>
            getCurrentMonthMatrix() {
        updateData();
        return currentMonthMatrix;
    }

}
