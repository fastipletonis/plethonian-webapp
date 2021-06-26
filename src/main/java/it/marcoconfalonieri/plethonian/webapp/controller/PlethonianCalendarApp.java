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

import it.marcoconfalonieri.plethonian.calendar.Festivity;
import it.marcoconfalonieri.plethonian.calendar.PlethonianCalendar;
import it.marcoconfalonieri.plethonian.calendar.PlethonianDay;
import it.marcoconfalonieri.plethonian.calendar.PlethonianMonth;
import it.marcoconfalonieri.plethonian.calendar.PlethonianWeekName;
import it.marcoconfalonieri.plethonian.calendar.PlethonianYear;
import it.marcoconfalonieri.plethonian.calendar.astropixel.PlethonianCalendarImpl;
import it.marcoconfalonieri.plethonian.webapp.util.StaticInfo;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
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
@Named("app")
@ApplicationScoped
public class PlethonianCalendarApp {
    
    /**
     * Labels associated to the day number.
     */
    private static final String[] DAY_LABELS = { "new", "2", "3", "4", "5", "6",
        "7", "8", "7", "6", "5", "4", "3", "2", "half", "2", "3", "4", "5", "6",
        "7", "8", "7", "6", "5", "4", "3", "2", "old", "oldnew" };
    /**
     * Map of available locales.
     */
    private static final Map<String, String> AVAILABLE_LOCALES =
            new LinkedHashMap<>();

    /**
     * WARManifest instance.
     */
    private StaticInfo staticInfo;
    /**
     * The calendar implementation.
     */
    private PlethonianCalendar calendar;

    /**
     * The current day.
     */
    private PlethonianDay today;

    /**
     * The current month.
     */
    private PlethonianMonth currentMonth;
    
    /**
     * The current year.
     */
    private PlethonianYear currentYear;

    /**
     * The available timezones.
     */
    private final Set<String> availableTimeZones = new TreeSet<>();
    
    /**
     * The map with the days of the current month ordered by week.
     */
    private final SortedMap<PlethonianWeekName, SortedSet<PlethonianDay>>
            currentMonthMatrix = new TreeMap<>();

    static {
        AVAILABLE_LOCALES.put("la", "Latine");
        AVAILABLE_LOCALES.put("it", "Italiano");
        AVAILABLE_LOCALES.put("en", "English");
        AVAILABLE_LOCALES.put("sv", "Svenska");
        AVAILABLE_LOCALES.put("lij", "Lìgure");
    }
    
    /**
     * Loads the current month matrix.
     */
    protected void loadCurrentMonthMatrix() {
        currentMonthMatrix.entrySet().forEach(
                pair -> pair.getValue().clear()
        );
        
        currentMonth.getDays().forEach(
                day -> getCurrentMonthMatrix().get(day.getWeek()).add(day)
        );
    }

    /**
     * Updates the loaded data if needed.
     */
    protected void updateData() {
        LocalDate ld = LocalDate.now();
        if (today == null || !today.getGregorianDate().equals(ld)) {
            currentYear = calendar.getYear(ld);
            currentMonth = calendar.getMonth(ld);
            today = calendar.getDay(ld);
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
        availableTimeZones.addAll(ZoneId.getAvailableZoneIds());
        availableTimeZones.removeIf(
                tz -> !(
                        tz.equals(PlethonianCalendarLocale.TZ_DEFAULT) ||
                        tz.contains("/")
                )
        );
        initializeCurrentMonthMatrix();
    }

    /**
     * initializes the object by creating the internal calendar.
     */
    @PostConstruct
    public void initialize() {
        staticInfo = StaticInfo.getInstance();
        
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

    /**
     * Getter for the current year.
     * 
     * @return the current year
     */
    public PlethonianYear getCurrentYear() {
        updateData();
        return currentYear;
    }
    
    /**
     * Reads the version from the MANIFEST file.
     * 
     * @return the version
     */
    public String getVersion() {
        return staticInfo.getVersion();
    }
    
    /**
     * Returns the day label.
     * 
     * @param day the day for which finding the label.
     * 
     * @return the label
     */
    public String getDayLabel(PlethonianDay day) {
        int labelIndex = Festivity.INTROSPECTION.equals(day.getFestivity())? 29
            : day.getDayOfMonth() - 1;
        return DAY_LABELS[labelIndex];
    }

    /**
     * Returns a list of available time zone IDs.
     * 
     * @return the available time zones
     */
    public Set<String> getAvailableTimeZones() {
        return availableTimeZones;
    }
    
    public Map<String, String> getAvailableLocales() {
        return AVAILABLE_LOCALES;
    }
}
