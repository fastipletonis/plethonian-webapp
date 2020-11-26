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

package it.marcoconfalonieri.plethonian.webapp.util;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test class for the static information retriever.
 */
public class StaticInfoTest {
    /**
     * Test version.
     */
    public static final String TEST_VERSION = "1.0.0";
    
    public StaticInfoTest() {
    }

    /**
     * Test of getInstance method, of class StaticInfo.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        
        // Checks the creation of the instance.
        StaticInfo result = StaticInfo.getInstance();
        assertNotNull(result);
        
        // Checks that the instance returned is the same.
        StaticInfo expResult = result;
        result = StaticInfo.getInstance();
        assertSame(expResult, result);
    }

    /**
     * Test of getVersion method, of class StaticInfo.
     */
    @Test
    public void testGetVersion() {
        Properties testProperties = new Properties();
        testProperties.put(StaticInfo.PROPERTY_VERSION, TEST_VERSION);
        System.out.println("getVersion");
        StaticInfo instance = new StaticInfo(testProperties);
        String expResult = TEST_VERSION;
        String result = instance.getVersion();
        assertEquals(expResult, result);
    }
    
}
