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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test of solarYear class.
 */
public class SolarYearTest {
    
    private static final String[] ROW_OK = {
        "2001", "Mar 20  13:31", "Jun 21  07:38", "Sep 22  23:05",
        "Dec 21  19:22"
    };

    private static final String[] ROW_ERR_YEAR = {
        "XXX", "Mar 20  13:31", "Jun 21  07:38", "Sep 22  23:05",
        "Dec 21  19:22"
    };
    
    private static final String[] ROW_ERR_FORMAT = {
        "2001", "Mar 20  13:31", "Jun 21  07:38", "Sep 22  23:05",
        "Dec 21  19:XX"
    };
    
    public SolarYearTest() {
    }

    /**
     * Test of constructor of class SolarYear.
     */
    @Test
    public void testSolarYear() {
        final String[][] rows = {ROW_OK, ROW_ERR_YEAR, ROW_ERR_FORMAT};
        final boolean[] exceptionThrown = {false, true, true};

        for (int i = 0; i < rows.length; i++) {
            boolean thrown = false;
            try {
                @SuppressWarnings("unused")
                SolarYear instance;
                instance = new SolarYear(rows[i]);
                thrown = false;
            } catch (DataException ex) {
                thrown = true;
            } finally {
                if (exceptionThrown[i] != thrown) {
                    String msg = String.format(
                            "Expected exception=%b, obtained %b, row=%s",
                            exceptionThrown[i], thrown,
                            Arrays.toString(rows[i]));
                    fail(msg);
                }
            }
        }
    }
    
    /**
     * Test of getWinterSolstice method, of class SolarYear.
     */
    @Test
    public void testGetWinterSolstice() {
        System.out.println("getWinterSolstice");
        SolarYear instance;
        try {
            instance = new SolarYear(ROW_OK);
        } catch (DataException ex) {
            fail("Unexpected exception thrown.");
            return;
        }
        // 2001 Dec 21  19:22
        ZonedDateTime expResult = ZonedDateTime.of(2001, 12, 21, 19, 22, 0, 0,
                ZoneId.of("GMT"));
        ZonedDateTime result = instance.getWinterSolstice();
        assertEquals(expResult, result);
    }
    
}
