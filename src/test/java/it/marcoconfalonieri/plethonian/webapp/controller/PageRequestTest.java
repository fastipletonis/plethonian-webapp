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

import it.marcoconfalonieri.plethonian.calendar.PlethonianDay;
import it.marcoconfalonieri.plethonian.calendar.PlethonianWeekName;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.SortedMap;
import java.util.SortedSet;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test for the method of the PageRequest controller.
 */
public class PageRequestTest {
    /**
     * Test date to be used in the created page.
     */
    private static final String TEST_TODAY = "2020-11-20";
    /**
     * Test date to be used in the created page.
     */
    private static final String TEST_DATE2 = "2020-11-25";
    /**
     * Date of the first day of the test month.
     */
    private static final String TEST_FIRST_DAY_MONTH = "2020-11-16";
    /**
     * Date of the first day of the test year.
     */
    private static final String TEST_FIRST_DAY_YEAR = "2019-12-27";
    
    protected PlethonianCalendarApp createAppInstance() {
        PlethonianCalendarApp app = new PlethonianCalendarApp();
        app.initialize();
        return app;
    }

    /**
     * Creates a test instance of the locale controller.
     * 
     * @param app the application instance
     * 
     * @return locale instance
     */
    protected PlethonianCalendarLocale createLocaleInstance(
            PlethonianCalendarApp app) {
        PlethonianCalendarLocale locale = new PlethonianCalendarLocale() {
            @Override
            protected String readCookie(String name) {
                return null;
            }
            
            @Override
            protected void writeCookie(String name, String value) {
                
            }
            
            @Override
            protected void readContextLocale() {
                
            }
            
            @Override
            protected void writeContextLocale() {
                
            }
        };
        locale.setAppInstance(app);
        locale.initialize();
        return locale;
    }
    
    /**
     * Creates an instance of <code>PageRequest</code> and initializes it.
     * 
     * @return a new instance of <code>PageRequest</code>
     */
    protected PageRequest createPageRequest() {
        PlethonianCalendarApp app = createAppInstance();
        PlethonianCalendarLocale locale = createLocaleInstance(app);
        PageRequest instance = new PageRequest() {
            // Returns the test value.
            @Override
            protected LocalDate getZonedToday(ZoneId zoneId) {
                return LocalDate.parse(TEST_TODAY);
            }
        };
        instance.setAppInstance(app);
        instance.setLocaleInstance(locale);
        instance.initialize();
        return instance;
    }
    
    public PageRequestTest() {
    }

    /**
     * Test of loadCurrentMonthMatrix method, of class PageRequest.
     */
    @Test
    public void testLoadCurrentMonthMatrix() {
        System.out.println("loadCurrentMonthMatrix");
        PageRequest instance = createPageRequest();
        instance.loadCurrentMonthMatrix();
        SortedMap<PlethonianWeekName, SortedSet<PlethonianDay>> result =
                instance.getCurrentMonthMatrix();
        LocalDate firstDay = result.get(result.firstKey())
                .first().getGregorianDate();
        assertEquals(TEST_FIRST_DAY_MONTH, firstDay.toString());
    }

    /**
     * Test of updateToday method, of class PageRequest.
     */
    @Test
    public void testUpdateToday() {
        System.out.println("updateToday");
        PageRequest instance = createPageRequest();
        instance.updateToday();
        assertEquals(TEST_TODAY, instance.getToday().getGregorianDate()
                .toString());
    }

    /**
     * Test of updateSelectedDate method, of class PageRequest.
     */
    @Test
    public void testUpdateSelectedDate() {
        System.out.println("updateSelectedDate");
        PageRequest instance = createPageRequest();
        String expectedDate = "2020-11-29";
        instance.updateSelectedDate(LocalDate.parse(expectedDate));
        String result = instance.getSelectedDay().getGregorianDate()
                .toString();
        assertEquals(expectedDate, result);
    }

    /**
     * Test of getZonedToday method, of class PageRequest.
     */
    @Test
    public void testGetToday() {
        System.out.println("getToday");
        PageRequest instance = createPageRequest();
        PlethonianDay result = instance.getToday();
        assertEquals(TEST_TODAY, result.getGregorianDate().toString());
    }

    /**
     * Test of getCurrentMonthMatrix method, of class PageRequest.
     */
    @Test
    public void testGetCurrentMonthMatrix() {
        System.out.println("getCurrentMonthMatrix");
        PageRequest instance = createPageRequest();
        SortedMap<PlethonianWeekName, SortedSet<PlethonianDay>> result = 
                instance.getCurrentMonthMatrix();
        LocalDate firstDay = result.get(result.firstKey())
                .first().getGregorianDate();
        assertEquals(TEST_FIRST_DAY_MONTH, firstDay.toString());
    }

    /**
     * Test of getSelectedYear method, of class PageRequest.
     */
    @Test
    public void testGetSelectedYear() {
        System.out.println("getSelectedYear");
        PageRequest instance = createPageRequest();
        String expResult = TEST_FIRST_DAY_YEAR;
        String result = instance.getSelectedYear().getFirstDay().toString();
        assertEquals(expResult, result);
 
    }

    /**
     * Test of getSelectedDay method, of class PageRequest.
     */
    @Test
    public void testGetSelectedDay() {
        System.out.println("getSelectedDay");
        PageRequest instance = createPageRequest();
        String expResult = TEST_TODAY;
        String result = instance.getSelectedDay().getGregorianDate()
                .toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDate method, of class PageRequest.
     */
    @Test
    public void testGetDate() {
        System.out.println("getDate");
        PageRequest instance = createPageRequest();
        String expResult = TEST_TODAY;
        String result = instance.getDate();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDate method, of class PageRequest.
     */
    @Test
    public void testSetDate() {
        System.out.println("setDate");
        String[] strDate = {TEST_DATE2, null, "aaaa", "1000-01-02",
            "3000-01-29", "2020-02-31"
        };
        String[] expDate = {TEST_DATE2, TEST_TODAY, TEST_TODAY, TEST_TODAY,
            TEST_TODAY, TEST_TODAY
        };
        for (int i = 0; i < strDate.length; i++) {
            PageRequest instance = createPageRequest();
            instance.setDate(strDate[i]);
            String result = instance.getDate();
            assertEquals(expDate[i], result);
        }
    }

    /**
     * Test of getSelectedMonth method, of class PageRequest.
     */
    @Test
    public void testGetSelectedMonth() {
        System.out.println("getSelectedMonth");
        PageRequest instance = createPageRequest();
        String expResult = TEST_FIRST_DAY_MONTH;
        String result = instance.getSelectedMonth().getFirstDay().toString();
        assertEquals(expResult, result);
    }
}
