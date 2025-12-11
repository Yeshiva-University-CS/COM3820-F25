package edu.yu.parallel.prepare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import edu.yu.parallel.model.BattingLine;

/**
 * Reads the Batting.csv file from resources and returns a List of BattingLine
 * objects.
 */
public class BattingReader {

    private static long nullRecordCount = 0;

    /**
     * Reads the Batting.csv file from the resources folder and returns a list of
     * BattingLine objects.
     * Empty values are converted to 0.
     *
     * @return A list of BattingLine objects
     * @throws IOException if the file cannot be read
     */
    public static List<BattingLine> readBattingFile() throws IOException {
        nullRecordCount = 0; // Reset counter

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        BattingReader.class.getClassLoader().getResourceAsStream("Batting.csv")))) {

            return reader.lines()
                    .skip(1) // Skip header line
                    .filter(line -> !line.trim().isEmpty())
                    .map(line -> {
                        BattingLine result = parseBattingLine(line);
                        if (result == null) {
                            nullRecordCount++;
                        }
                        return result;
                    })
                    .filter(battingLine -> battingLine != null)
                    .toList();
        }
    }

    /**
     * Returns the number of null records encountered during the last read
     * operation.
     *
     * @return The count of null records
     */
    public static long getNullRecordCount() {
        return nullRecordCount;
    }

    /**
     * Parses a single line from the CSV file into a BattingLine object.
     * The order of columns (excluding G_batting and G_old) is:
     * playerID, yearID, stint, teamID, lgID, G, AB, R, H, 2B, 3B, HR, RBI, SB, CS,
     * BB, SO, IBB, HBP, SH, SF, GIDP
     *
     * @param line The CSV line to parse
     * @return A BattingLine object, or null if parsing fails
     */
    private static BattingLine parseBattingLine(String line) {
        String[] fields = line.split(",", -1); // -1 to keep trailing empty strings

        try {
            // Expected 24 fields (22 fields we use + G_batting + G_old which we skip)
            if (fields.length < 22) {
                return null;
            }

            String playerID = fields[0].trim();
            int yearID = parseInt(fields[1]);
            int stint = parseInt(fields[2]);
            String teamID = fields[3].trim();
            String lgID = fields[4].trim();
            int G = parseInt(fields[5]);
            // Skip G_batting at index 6
            int AB = parseInt(fields[7]);
            int R = parseInt(fields[8]);
            int H = parseInt(fields[9]);
            int twoB = parseInt(fields[10]);
            int threeB = parseInt(fields[11]);
            int HR = parseInt(fields[12]);
            int RBI = parseInt(fields[13]);
            int SB = parseInt(fields[14]);
            int CS = parseInt(fields[15]);
            int BB = parseInt(fields[16]);
            int SO = parseInt(fields[17]);
            int IBB = parseInt(fields[18]);
            int HBP = parseInt(fields[19]);
            int SH = parseInt(fields[20]);
            int SF = parseInt(fields[21]);

            return new BattingLine(playerID, yearID, stint, teamID, lgID, G, AB, R, H,
                    twoB, threeB, HR, RBI, SB, CS, BB, SO, IBB, HBP, SH, SF);

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Parses an integer from a string, returning 0 if the string is empty or
     * invalid.
     *
     * @param value The string value to parse
     * @return The parsed integer, or 0 if parsing fails
     */
    private static int parseInt(String value) {
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
