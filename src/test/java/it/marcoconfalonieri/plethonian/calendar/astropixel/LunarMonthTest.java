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

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test of LunarMonth class.
 */
public class LunarMonthTest {

    private static final String[] ROW_OK5 = {
        "2001 Jan 24  13:07", "29d 19h 14m", "+07h 01m", "177.9°", "longest"
    };

    private static final String[] ROW_OK4 = {
        "2001 Jan 24  13:07", "29d 19h 14m", "+07h 01m", "177.9°"
    };

    private static final String[] ROW_ERR_NEW_MOON = {
        "2001XX", "29d 19h 14m", "+07h 01m", "177.9°", "longest"
    };

    private static final String[] ROW_ERR_MONTH_LENGTH = {
        "2001 Jan 24  13:07", "XXX", "+07h 01m", "177.9°"
    };

    private static final String[] ROW_ERR_DIFF_FROM_MEAN = {
        "2001 Jan 24  13:07", "29d 19h 14m", "XXX", "177.9°"
    };

    private static final String[] ROW_ERR_MOON_ANOMALY = {
        "2001 Jan 24  13:07", "29d 19h 14m", "+07h 01m", "XXX"
    };

    public LunarMonthTest() {
    }

    /**
     * Test of constructor of class LunarMonth.
     */
    @Test
    public void testLunarMonth() {
        final String[][] rows = {ROW_OK5, ROW_OK4, ROW_ERR_NEW_MOON,
            ROW_ERR_MONTH_LENGTH, ROW_ERR_DIFF_FROM_MEAN, ROW_ERR_MOON_ANOMALY
        };

        final boolean[] exceptionThrown = {false, false, true, true, true, true
        };

        for (int i = 0; i < rows.length; i++) {
            boolean thrown = false;
            try {
                @SuppressWarnings("unused")
                LunarMonth instance;
                instance = new LunarMonth(rows[i]);
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
     * Test of getNewMoon method, of class LunarMonth.
     */
    @Test
    public void testGetNewMoon() {
        System.out.println("getNewMoon");
        LunarMonth instance;
        try {
            instance = new LunarMonth(ROW_OK5);
        } catch (DataException ex) {
            fail("Unexpected exception thrown.");
            return;
        }
        // 2001 Jan 24  13:07
        ZonedDateTime expResult = ZonedDateTime.of(2001, 1, 24, 13, 7, 0, 0,
                ZoneId.of("GMT"));
        ZonedDateTime result = instance.getNewMoon();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMonthLength method, of class LunarMonth.
     */
    @Test
    public void testGetMonthLength() {
        System.out.println("getMonthLength");
        LunarMonth instance;
        try {
            instance = new LunarMonth(ROW_OK5);
        } catch (DataException ex) {
            fail("Unexpected exception thrown.");
            return;
        }
        // 29d 19h 14m
        Duration expResult = Duration.ofDays(29).plusHours(19).plusMinutes(14);
        Duration result = instance.getMonthLength();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDiffFromMean method, of class LunarMonth.
     */
    @org.junit.jupiter.api.Test
    public void testGetDiffFromMean() {
        System.out.println("getDiffFromMean");
        LunarMonth instance;
        try {
            instance = new LunarMonth(ROW_OK5);
        } catch (DataException ex) {
            fail("Unexpected exception thrown.");
            return;
        }
        // +07h 01m
        Duration expResult = Duration.ofHours(7).plusMinutes(1);
        Duration result = instance.getDiffFromMean();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMoonAnomaly method, of class LunarMonth.
     */
    @org.junit.jupiter.api.Test
    public void testGetMoonAnomaly() {
        System.out.println("getMoonAnomaly");
        LunarMonth instance;
        try {
            instance = new LunarMonth(ROW_OK5);
        } catch (DataException ex) {
            fail("Unexpected exception thrown.");
            return;
        }
        // 177.9°
        double expResult = 177.9;
        double result = instance.getMoonAnomaly();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getAnnotations method, of class LunarMonth.
     */
    @org.junit.jupiter.api.Test
    public void testGetAnnotations() {
        System.out.println("getAnnotations");
        LunarMonth instance;
        try {
            instance = new LunarMonth(ROW_OK5);
        } catch (DataException ex) {
            fail("Unexpected exception thrown.");
            return;
        }
        // "longest"
        String expResult = "longest";
        String result = instance.getAnnotations();
        assertEquals(expResult, result);
    }

}
