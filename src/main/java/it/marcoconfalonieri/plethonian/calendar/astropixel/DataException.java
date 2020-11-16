/*
 *     plethonian-backend - Plethonian calendar backend
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

/**
 * Exception thrown when AstroPixel data cannot be parsed.
 * 
 * @author Marco Confalonieri {@literal <marco@marcoconfalonieri.it>}
 */
public class DataException extends Exception {
    // Serial version ID
    private static final long serialVersionUID = 8957172330506801456L;
    
    /**
     * Constructor with message.
     * 
     * @param msg message
     */
    public DataException(String msg) {
        super(msg);
    }
    
    /**
     * Constructor with message and initial cause.
     * 
     * @param msg message
     * @param initCause initial exception cause
     */
    public DataException(String msg, Throwable initCause) {
        super(msg, initCause);
    }
}
