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
import it.marcoconfalonieri.plethonian.calendar.PlethonianYear;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Request data. It holds the data coming from the current request.
 */
@RequestScoped
@Named("req")
public class PageRequest {
    /**
     * Date regex.
     */
    private static final String DATE_REGEX = "\\d{4}-\\d{2}\\d{2}";
    /**
     * Date regex.
     */
    private static final Pattern DATE_PATTERN = Pattern.compile(DATE_REGEX);
    /**
     * The calendar implementation.
     */
    private PlethonianCalendar calendar;
    /**
     * The current day.
     */
    private PlethonianDay today;
    /**
     * The selected day.
     */
    private PlethonianDay selectedDay;
    /**
     * The current month.
     */
    private PlethonianMonth selectedMonth;
    /**
     * The current year.
     */
    private PlethonianYear selectedYear;
    /**
     * The app bean.
     */
    private PlethonianCalendarApp appInstance;
    /**
     * The localeInstance bean.
     */
    private PlethonianCalendarLocale localeInstance;
    

    /**
     * The map with the days of the current month ordered by week.
     */
    private final SortedMap<PlethonianWeekName, SortedSet<PlethonianDay>>
            currentMonthMatrix = new TreeMap<>();

    private LocalDate checkDate(String strDate) {
        final LocalDate defaultDate = today.getGregorianDate();
        if (strDate == null || strDate.length() != 10) {
            return defaultDate;
        }
        
        Matcher matcher = DATE_PATTERN.matcher(strDate);
        if (!matcher.find()) {
            return defaultDate;
        }
        
        try {
            LocalDate ld = LocalDate.parse(strDate);
            if (
                    ld.isBefore(selectedYear.getFirstDay())
                    || ld.isAfter(selectedYear.getMonths().last().getDays()
                            .last().getGregorianDate())) {
                return defaultDate;
            }
            return ld;
        } catch (DateTimeParseException ex) {
            return defaultDate;
        }
    }
    
    /**
     * Loads the current month matrix.
     */
    protected void loadCurrentMonthMatrix() {
        currentMonthMatrix.entrySet().forEach(
                pair -> pair.getValue().clear()
        );

        selectedMonth.getDays().forEach(
                day -> getCurrentMonthMatrix().get(day.getWeek()).add(day)
        );
    }

    /**
     * Updates the current day. It also sets it as selected if no other day is
     * set as such.
     */
    protected void updateToday() {
        ZoneId zoneId = ZoneId.of(PlethonianCalendarLocale.TZ_DEFAULT);
        
        if (localeInstance != null && localeInstance.getZoneId() != null) {
            zoneId = localeInstance.getZoneId();
        }
        
        LocalDate now = LocalDate.now(zoneId);
        
        if ((today == null) || !today.getGregorianDate().equals(now)) {
            today = calendar.getDay(now);
        }

        if (selectedDay == null) {
            updateSelectedDate(now);
        }
    }

    /**
     * Updates the selected date if needed.
     *
     * @param ld selected date
     */
    protected void updateSelectedDate(LocalDate ld) {
        if (selectedDay == null
                || !ld.equals(selectedDay.getGregorianDate())) {
            selectedYear = calendar.getYear(ld);
            PlethonianMonth month = calendar.getMonth(ld);
            if (!month.equals(selectedMonth)) {
                selectedMonth = calendar.getMonth(ld);
                loadCurrentMonthMatrix();
            }
            selectedDay = calendar.getDay(ld);
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
    public PageRequest() {
        initializeCurrentMonthMatrix();
    }

    /**
     * initializes the object by accessing the internal calendar.
     */
    @PostConstruct
    public void initialize() {
        calendar = appInstance.getCalendar();
        updateToday();
    }

    /**
     * Getter for today.
     *
     * @return the plethonian day for today.
     */
    public PlethonianDay getToday() {
        return today;
    }

    /**
     * Gets the month matrix.
     *
     * @return the currentMonthMatrix
     */
    public SortedMap<PlethonianWeekName, SortedSet<PlethonianDay>>
            getCurrentMonthMatrix() {
        return currentMonthMatrix;
    }

    /**
     * Getter for the selected year.
     *
     * @return the selected year
     */
    public PlethonianYear getSelectedYear() {
        return selectedYear;
    }

    /**
     * Returns the selected day.
     *
     * @return the selected day
     */
    public PlethonianDay getSelectedDay() {
        return selectedDay;
    }

    /**
     * Returns the selected date. The date is a string in YYYY-MM-DD format.
     * 
     * @return the selected date
     */
    public String getDate() {
        return (selectedDay != null)? selectedDay.getGregorianDate().toString()
                : null;
    }
    
    /**
     * Sets the selected date. This method takes the a string in the form of a
     * Gregorian date YYYY-MM-DD.
     *
     * @param strDate the day to set
     */
    public void setDate(String strDate) {
        if (strDate == null) { return; }
        LocalDate ld = checkDate(strDate);
        updateSelectedDate(ld);
    }

    /**
     * Returns the selected month.
     *
     * @return the selectedMonth
     */
    public PlethonianMonth getSelectedMonth() {
        return selectedMonth;
    }

    /**
     * Setter for the <code>PlethonianCalendarApp</code> bean.
     * 
     * @param appInstance the appInstance to set
     */
    @Inject
    public void setAppInstance(PlethonianCalendarApp appInstance) {
        this.appInstance = appInstance;
    }

    /**
     * Setter for the <code>PlethonianCalendarLocale</code> bean.
     * @param localeInstance the localeInstance to set
     */
    @Inject
    public void setLocaleInstance(PlethonianCalendarLocale localeInstance) {
        this.localeInstance = localeInstance;
    }
    
}
