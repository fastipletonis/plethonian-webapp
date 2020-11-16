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
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Stores a line of data from AstroPixel.com database.
 * 
 * @author Marco Confalonieri {@literal <marco@marcoconfalonieri.it>}
 */
public class LunarMonth implements Serializable {
    /**
     * RegEx for the new moon date-time.
     */
    private static final String FMT_REGEX_NEW_MOON =
            "\\d{4}\\s\\w{3}\\s\\d{2}\\s\\s\\d{2}:\\d{2}";
    /**
     * Pattern for the new moon date-time.
     */
    private static final String FMT_PATTERN_NEW_MOON = "yyyy MMM dd  HH:mm zzz";
    /**
     * RegEx for the synodic month length.
     */
    private static final String FMT_REGEX_MONTH_LENGTH =
            "(\\d{2})d\\s(\\d{2})h\\s(\\d{2})m";
    /**
     * RegEx for the difference with the mean month.
     */
    private static final String FMT_REGEX_DIFF_FROM_MEAN =
            "([+-])(\\d{2})h\\s(\\d{2})m";
    /**
     * RegEx for the real moon anomaly in degrees.
     */
    private static final String FMT_REGEX_ANOMALY = "(\\d{1,3}\\.\\d)°";

    // Formatter for the new moon date-time.
    private static final DateTimeFormatter newMoonFormatter = DateTimeFormatter.ofPattern(FMT_PATTERN_NEW_MOON);
    // RegEx pattern for the new moon date-time.
    private static final Pattern NEW_MOON_PATTERN = Pattern.compile(FMT_REGEX_NEW_MOON);
    // RegEx pattern for month length.
    private static final Pattern MONTH_LENGTH_PATTERN = Pattern.compile(FMT_REGEX_MONTH_LENGTH);
    // RegEx pattern for the difference between this month's and the mean length
    private static final Pattern DIFF_FROM_MEAN_PATTERN = Pattern.compile(FMT_REGEX_DIFF_FROM_MEAN);
    // RegEx pattern for the difference between this month's and the mean length
    private static final Pattern MOON_ANOMALY_PATTERN = Pattern.compile(FMT_REGEX_ANOMALY);
    // Serial version ID
    private static final long serialVersionUID = -8260919256844275225L;

    // New moon date-time for the month.
    private ZonedDateTime newMoon;
    // Month length.
    private Duration monthLength;
    // Difference between this and the mean month.
    private Duration diffFromMean;
    // Moon anomaly in degrees
    private double moonAnomaly;
    // Text notes.
    private String annotations;

    /**
     * Reads the new moon date-time.
     *
     * @param s string to parse
     * @return the date-time of the new moon.
     * @throws DataException when the string does not match the
     *     pattern
     */
    private static ZonedDateTime readNewMoon(String str)
            throws DataException {
        Matcher match = NEW_MOON_PATTERN.matcher(str);
        if (!match.find()) {
            throw new DataException("Wrong format: " + str);
        }
        return newMoonFormatter.parse(str, temporal -> ZonedDateTime.from(temporal));
    }

    /**
     * Matches a pattern. This method tries to match a string towards a given
     * pattern, returning the capture groups.
     *
     * @param ptn pattern to match
     * @param str input string
     * @return the capture groups
     * @throws DataException when the string does not match the
     *     pattern
     */
    private static String[] readPattern(Pattern ptn, String str)
            throws DataException {
        Matcher match = ptn.matcher(str);
        if (!match.find()) {
            throw new DataException("Wrong format: " + str);
        }

        String[] groups = new String[match.groupCount()];
        for (int i = 0; i < groups.length; i++) {
            groups[i] = match.group(i + 1);
        }

        return groups;
    }

    /**
     * Reads the month length components and calculates it as Duration.
     *
     * @param str input string
     * @return the month length in duration
     * @throws DataException when the string does not match the
     *     pattern
     */
    private static Duration readMonthLength(String str)
            throws DataException {
        String[] parts = readPattern(MONTH_LENGTH_PATTERN, str);
        long days = Long.parseLong(parts[0]);
        long hours = Long.parseLong(parts[1]);
        long minutes = Long.parseLong(parts[2]);
        return Duration.ofDays(days).plusHours(hours).plusMinutes(minutes);
    }

    /**
     * Reads the difference in duration between this month and the mean one.
     *
     * @param str input string
     * @return the difference in duration
     * @throws DataException when the string does not match the
     *     pattern
     */
    private static Duration readDiffFromMean(String str)
            throws DataException {
        String[] parts = readPattern(DIFF_FROM_MEAN_PATTERN, str);
        long mul = (parts[0].equals("+")) ? 1 : -1;
        long hours = Long.parseLong(parts[1]);
        long minutes = Long.parseLong(parts[2]);
        long totalMinutes = mul * (60 * hours + minutes);
        return Duration.ofMinutes(totalMinutes);
    }

    /**
     * Reads the moon anomaly in degrees.
     *
     * @param str input string
     * @return the angle in degrees
     * @throws DataException when the string does not match the
     *     pattern
     */
    private static double readMoonAnomaly(String str)
            throws DataException {
        String[] parts = readPattern(MOON_ANOMALY_PATTERN, str);
        return Double.valueOf(parts[0]);
    }

    /**
     * Constructor for the AstroPixel lunar month.
     *
     * @param row data extracted from a row in AstroPixel's synodic months table
     * @throws DataException when the string does not match the
     *     pattern
     */
    public LunarMonth(String[] row)
            throws DataException {
        switch (row.length) {
            case 5:
                annotations = row[4];
                readBaseArgs(row);
                break;
            case 4:
                readBaseArgs(row);
                break;
            default:
                throw new DataException("Unexpected row size: " + row.length);
        }
    }

    /**
     * Reads the four base arguments from the row.
     *
     * @param row data extracted from a row in AstroPixel's synodic months table
     * @throws DataException when the string does not match the
     *     pattern
     */
    private void readBaseArgs(String[] row) throws DataException {
        newMoon = readNewMoon(row[0] + " GMT");
        monthLength = readMonthLength(row[1]);
        diffFromMean = readDiffFromMean(row[2]);
        moonAnomaly = readMoonAnomaly(row[3]);
    }

    /**
     * Gets the new moon date-time for the current row.
     *
     * @return the new moon date-time
     */
    public ZonedDateTime getNewMoon() {
        return newMoon;
    }

    /**
     * Returns the month length.
     * @return the month length
     */
    public Duration getMonthLength() {
        return monthLength;
    }

    /**
     * Returns the difference with the mean month.
     * @return the difference from mean month
     */
    public Duration getDiffFromMean() {
        return diffFromMean;
    }

    /**
     * Returns the moon anomaly in degrees.
     * @return the moon anomaly
     */
    public double getMoonAnomaly() {
        return moonAnomaly;
    }

    /**
     * Returns the annotations.
     * @return the annotations
     */
    public String getAnnotations() {
        return annotations;
    }
}
