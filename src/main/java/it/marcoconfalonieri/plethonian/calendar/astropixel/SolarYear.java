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

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Stores a line of data from AstroPixel.com database.
 */
public class SolarYear implements Serializable {
    /**
     * RegEx for the solstice date-time.
     */
    private static final String FMT_REGEX_SOLSTICE =
            "\\d{4}\\s\\w{3}\\s\\d{2}\\s\\s\\d{2}:\\d{2}";
    /**
     * Pattern for the solstice date-time.
     */
    private static final String FMT_PATTERN_SOLSTICE = "yyyy MMM dd  HH:mm zzz";
 
    /**
     * Formatter for the solstice date-time.
     */
    private static final DateTimeFormatter solsticeFormatter = DateTimeFormatter.ofPattern(FMT_PATTERN_SOLSTICE);
    /**
     * RegEx pattern for the new moon date-time.
     */
    private static final Pattern SOLSTICE_PATTERN = Pattern.compile(FMT_REGEX_SOLSTICE);
    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = -609192568345275225L;

    /**
     * date-time for the winter solstice.
     */
    private ZonedDateTime winterSolstice;

    /**
     * Reads the winter solstice date-time.
     *
     * @param str string to parse
     * @return the date-time of the new moon.
     * @throws DataException when the string does not match the
     *     pattern
     */
    private static ZonedDateTime readWinterSolstice(String str)
            throws DataException {
        Matcher match = SOLSTICE_PATTERN.matcher(str);
        if (!match.find()) {
            throw new DataException("Wrong format: " + str);
        }
        return solsticeFormatter.parse(str, temporal -> ZonedDateTime.from(temporal));
    }

    /**
     * Constructor for the AstroPixel solar year.
     *
     * @param row data extracted from a row in AstroPixel's solstices table
     * @throws DataException when the string does not match the
     *     pattern
     */
    public SolarYear(String[] row) throws DataException {
        readBaseArgs(row);
    }

    /**
     * Reads the four base arguments from the row.
     *
     * @param row data extracted from a row in AstroPixel's synodic months table
     * @throws DataException when the string does not match the
     *     pattern
     */
    private void readBaseArgs(String[] row) throws DataException {
        if (row.length < 5) {
            throw new DataException("Row has missing fields");
        } else if (row[0].length() < 4) {
            throw new DataException("Year is not long enough");
        }
        String year = row[0].substring(0, 4);
        String str = year + " " + row[4].strip() + " GMT";
        winterSolstice = readWinterSolstice(str);
    }

    /**
     * Gets the winter solstice date-time for the current row.
     *
     * @return the new moon date-time
     */
    public ZonedDateTime getWinterSolstice() {
        return winterSolstice;
    }

}
