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

package it.marcoconfalonieri.plethonian.calendar.astropixel;

import it.marcoconfalonieri.plethonian.calendar.PlethonianDay;
import it.marcoconfalonieri.plethonian.calendar.PlethonianMonth;
import it.marcoconfalonieri.plethonian.calendar.PlethonianYear;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Marco Confalonieri {@literal <marco.confalonieri@elits.com>}
 */
public class PlethonianCalendarImplTest {
    private static final LocalDate REF_DATE = LocalDate.of(2003, Month.MARCH,
            3);
    
    public PlethonianCalendarImplTest() {
    }

    protected PlethonianCalendarImpl createInstance() {
        PlethonianCalendarImpl impl = null;
        try {
            impl = new PlethonianCalendarImpl();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            fail("Unexpected IOException " + ex.getMessage());
        }
        return impl;
    }
    
    /**
     * Test of constructor, of class PlethonianCalendarImpl.
     */
    @Test
    public void testPlethonianCalendarImpl() {
        createInstance();
    }
    
    /**
     * Test of getYear method, of class PlethonianCalendarImpl.
     */
    @Test
    public void testGetYear() {
        System.out.println("getYear");
        PlethonianCalendarImpl instance = createInstance();
        LocalDate expResult = LocalDate.of(2003, Month.JANUARY, 3);
        PlethonianYear result = instance.getYear(REF_DATE);
        System.out.println(result.getFirstDay());
        assertEquals(expResult, result.getFirstDay());
    }

    /**
     * Test of getMonth method, of class PlethonianCalendarImpl.
     */
    @Test
    public void testGetMonth() {
        System.out.println("getMonth");
        PlethonianCalendarImpl instance = createInstance();
        LocalDate expResult = LocalDate.of(2003, Month.FEBRUARY, 2);
        PlethonianMonth result = instance.getMonth(REF_DATE);
        assertEquals(expResult, result.getFirstDay());
    }

    /**
     * Test of getDay method, of class PlethonianCalendarImpl.
     */
    @Test
    public void testGetDay() {
        System.out.println("getDay");
        PlethonianCalendarImpl instance = createInstance();
        PlethonianDay result = instance.getDay(REF_DATE);
        assertEquals(REF_DATE, result.getGregorianDate());

    }
    
    @Test
    public void testGetDaySpecial() {
        System.out.println("getDaySpecial");
        LocalDate date = LocalDate.of(2020, Month.DECEMBER, 20);
        PlethonianCalendarImpl instance = createInstance();
        PlethonianDay result = instance.getDay(date);
        assertEquals(date, result.getGregorianDate());

    }
}
