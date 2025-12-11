package edu.yu.parallel.prepare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads the People.csv file from resources and creates a map of player IDs to full names.
 */
public class PeopleReader {

    /**
     * Reads the People.csv file from the resources folder and returns a map of player IDs to full names.
     * Full names are formatted as "nameFirst nameLast".
     *
     * @return A map of playerID to full name
     * @throws IOException if the file cannot be read
     */
    public static Map<String, String> readPeopleFile() throws IOException {
        Map<String, String> playerMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        PeopleReader.class.getClassLoader().getResourceAsStream("People.csv")))) {

            reader.lines()
                    .skip(1)  // Skip header line
                    .filter(line -> !line.trim().isEmpty())
                    .forEach(line -> {
                        String[] parts = parsePeopleLine(line);
                        if (parts != null) {
                            playerMap.put(parts[0], parts[1]);
                        }
                    });
        }

        return playerMap;
    }

    /**
     * Parses a single line from the People.csv file.
     * Returns an array with [playerID, fullName] or null if parsing fails.
     *
     * @param line The CSV line to parse
     * @return An array with [playerID, fullName], or null if parsing fails
     */
    private static String[] parsePeopleLine(String line) {
        String[] fields = line.split(",", -1);

        try {
            // Expected at least 16 fields (playerID at index 1, nameFirst at 14, nameLast at 15)
            if (fields.length < 16) {
                return null;
            }

            String playerID = fields[1].trim();
            String nameFirst = fields[14].trim();
            String nameLast = fields[15].trim();

            if (playerID.isEmpty() || nameFirst.isEmpty() || nameLast.isEmpty()) {
                return null;
            }

            String fullName = nameFirst + " " + nameLast;
            return new String[]{playerID, fullName};

        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}
