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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Compile-time information retriever. It follows the Singleton pattern.
 */
public class StaticInfo {
    /**
     * Resource path.
     */
    private static final String RES_VERSION_INFO = "/version.properties";
    /**
     * Version property.
     */
    protected static final String PROPERTY_VERSION = "project.version";
    
    /**
     * Instance.
     */
    private static StaticInfo instance;
    
    /**
     * Properties.
     */
    private Properties properties;
    
    /**
     * Constructor. It builds a new instance of this class.
     */
    protected StaticInfo() {
        properties = new Properties();
        
        InputStream is = StaticInfo.class.getResourceAsStream(
                RES_VERSION_INFO);
        if (is == null) { return; }
        
        try {
            properties.load(is);
        } catch (IOException ex) {
            String msg = "Unexpected error reading properties. "
                    + ex.getMessage();
            System.err.println(msg);
            ex.printStackTrace(System.err);
        }
    }
    
    /**
     * Constructor with properties specification. It is meant to be used only
     * in tests.
     * 
     * @param properties the properties to use.
     */
    protected StaticInfo(Properties properties) {
        this.properties = properties;
    }
    
    /**
     * Return the class instance. If it is <code>null</code>, it is created.
     * 
     * @return class instance
     */
    public static synchronized StaticInfo getInstance() {
        if (instance == null) {
            instance = new StaticInfo();
        }
        return instance;
    }
    
    /**
     * Returns the project version.
     * 
     * @return the version property
     */
    public String getVersion() {
        return properties.getProperty(PROPERTY_VERSION, "");
    }
}
