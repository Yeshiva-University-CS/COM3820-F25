package edu.yu.parallel.prepare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.yu.parallel.model.BattingLine;
import edu.yu.parallel.model.PlayerStats;

/**
 * Aggregates BattingLine data into PlayerSeason objects.
 * Creates one PlayerSeason entry per player per season, combining multiple team stints.
 */
public class SeasonAggregator {

    /**
     * Aggregates a list of BattingLine objects into a list of PlayerSeason objects.
     * For players with multiple teams in a season, all teams are kept in a Set.
     *
     * @param battingLines The list of individual batting records
     * @return A list of aggregated PlayerSeason objects
     */
    public static List<PlayerStats> aggregateByPlayerSeason(List<BattingLine> battingLines) {
        // Use a map with a composite key (playerID + yearID) to aggregate
        Map<String, PlayerStats> playerSeasonMap = new HashMap<>();

        for (BattingLine battingLine : battingLines) {
            String key = battingLine.getPlayerID() + "|" + battingLine.getYearID();

            PlayerStats playerSeason = playerSeasonMap.computeIfAbsent(key, k -> 
                new PlayerStats(battingLine.getPlayerID(), battingLine.getYearID())
            );

            playerSeason.addBattingLine(battingLine);
        }

        return new ArrayList<>(playerSeasonMap.values());
    }
}
