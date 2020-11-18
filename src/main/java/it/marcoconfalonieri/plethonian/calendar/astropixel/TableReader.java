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

package it.marcoconfalonieri.plethonian.calendar.astropixel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Utility class that reads a tabular file.
 * 
 * @author Marco Confalonieri {@literal <marco@marcoconfalonieri.it>}
 */
public class TableReader {
    /**
     * Resource name.
     */
    private final String res;
    
    /**
     * Positions of the fields.
     */
    private final int[] pos;
    
    /**
     * Constructor. It takes the resource name and the field positions.
     * 
     * @param res resource name
     * @param pos positions of the fields
     */
    public TableReader(String res, int[] pos) {
        this.res = res;
        this.pos = pos;
    }
    
    /**
     * Reads the table file.
     * 
     * @return all the rows and the fields.
     * 
     * @throws IOException in case of errors reading the resources
     */
    public String[][] readTable() throws IOException {
        ClassLoader cl = getClass().getClassLoader();
        ArrayList<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(cl.getResourceAsStream(res)))) {
            String[] row;
            while (true) {
                row = readRow(br);
                if (row == null) {
                    break;
                }
                rows.add(row);
            }
            
        }
        return rows.toArray(new String[rows.size()][]);
    }
    
    /**
     * Reads a row from the buffered reader.
     * 
     * @param br buffered reader
     * 
     * @return the row as an array of strings
     * 
     * @throws IOException in case of errors reading the resources
     */
    protected String[] readRow(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) {
            return null;
        }
        int len = line.length();
        ArrayList<String> row = new ArrayList<>();
        int[] nextpos = Arrays.copyOf(pos, pos.length + 1);
        nextpos[pos.length] = len;
        for (int i = 0; i < pos.length; i++) {
            int fb = pos[i];
            int fl = nextpos[i + 1];
            if (fb >= line.length()) {
                break;
            }
            fl = (fl > line.length())? line.length() : fl;
            row.add(line.substring(fb, fl).strip());
        }
        return row.toArray(new String[row.size()]);
    }
}
