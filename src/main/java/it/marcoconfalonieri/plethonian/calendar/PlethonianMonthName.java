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

package it.marcoconfalonieri.plethonian.calendar;

/**
 * Month representation.
 * 
 * @author Marco Confalonieri {@literal <marco@marcoconfalonieri.it>}
 */
public enum PlethonianMonthName {
    FIRST(1), SECOND(2), THIRD(3), FOURTH(4), FIFTH(5), SIXTH(6), SEVENTH(7),
    EIGHTH(8), NINTH(9), TENTH(10), ELEVENTH(11), TWELFTH(12), THIRTEENTH(13);

    private final int value;

    PlethonianMonthName(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }
}
