package edu.yu.parallel;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.yu.parallel.model.BattingLine;
import edu.yu.parallel.model.PlayerAnalytics;
import edu.yu.parallel.model.PlayerStats;
import edu.yu.parallel.prepare.BattingReader;
import edu.yu.parallel.prepare.PeopleReader;
import edu.yu.parallel.prepare.SeasonAggregator;
import edu.yu.parallel.query.QueryEngine;

/**
 * Main class to run the program.
 */
public class Main {

    private final static Logger logger = LogManager.getLogger("main");

    public static void main(String[] args) throws IOException {
        logger.info("Program started.");

        var playerMap = getPlayerMap();
        var playerSeasonStats = getPlayerSeasonStats();

        logger.info("Read People.csv with {} players.", playerMap.size());
        logger.info("Aggregated into {} PlayerSeason records.", playerSeasonStats.size());

        // *****************************************************************************************

        QueryEngineRunner runner = new QueryEngineRunner(playerMap);

        var runTypes = runner.getAllRunTypes();

        for (var runType : runTypes) {
            logger.info("Running QueryEngine with run type: {}", runType);
            QueryEngine engine = runner.createEngine(runType);
            
            String engineLoggerName = engine.getClass().getName();

            // ===== Calculate Player Analytics =====
            logger.info("");
            logger.info("Calculating Player Analytics...");
            
            List<PlayerAnalytics> playerAnalytics = engine.calculatePlayerAnalytics(playerSeasonStats);

            long cumulativeMs = 0, ms;

            ms = Utils.benchmarkRunnable(() -> engine.calculatePlayerAnalytics(playerSeasonStats), false);
            cumulativeMs += ms;

            logger.info("Calc Player Analytics in: {} ms", ms);

            // ===== Top Ten Career WAR =====
            logger.info("");
            logger.info("Top Ten Career WAR:");
            logger.info("-----");
            engine.printTopTenCareerWAR(playerAnalytics);
            ms = Utils.benchmarkRunnable(() -> engine.printTopTenCareerWAR(playerAnalytics), false);
            cumulativeMs += ms;
            logger.info("Top Ten Career WAR in queried in: {} ms", ms);
            
            // ===== Top Five WAR By Decade =====
            logger.info("");
            logger.info("Top Five WAR By Decade:");
            logger.info("-----");
            engine.printTopFiveWARByDecade(playerAnalytics);
            ms = Utils.benchmarkRunnable(() -> engine.printTopFiveWARByDecade(playerAnalytics), false);
            cumulativeMs += ms;
            logger.info("Top Five WAR By Decade in queried in: {} ms", ms); 
   
            // ===== Top Ten Consecutive Seven Year WAR Window =====
            logger.info("");
            logger.info("Top 10 Consecutive Seven Year WAR Window:");
            logger.info("-----");
            engine.printTopTenConsecutiveSevenYearWarWindow(playerAnalytics);
            ms = Utils.benchmarkRunnable(() -> engine.printTopTenConsecutiveSevenYearWarWindow(playerAnalytics), false);
            cumulativeMs += ms;
            logger.info("Top 10 Consecutive Seven Year WAR Window queried in: {} ms", ms);
            
            // ===== Top Ten Best Seven Year WAR =====
            logger.info("");
            logger.info("Top 10 Best Seven Year WAR:");
            logger.info("-----");
            engine.printTopTenBestSevenYearWar(playerAnalytics);
            ms = Utils.benchmarkRunnable(() -> engine.printTopTenBestSevenYearWar(playerAnalytics), false);
            cumulativeMs += ms;
            logger.info("Top 10 Best Seven Year WAR queried in: {} ms", ms);

            // ===== Top Ten Most Similar Careers =====
            logger.info("");
            logger.info("Top 10 Most Similar Careers:");
            logger.info("-----");
            engine.printTopTenMostSimilarCareers(playerAnalytics);
            ms = Utils.benchmarkRunnable(() -> engine.printTopTenMostSimilarCareers(playerAnalytics), false);
            cumulativeMs += ms;
            logger.info("Top 10 Most Similar Careers queried in: {} ms", ms);

            logger.info("");
            logger.info("Total time for {}: {} ms", runType, cumulativeMs);
            logger.info("==================================================");
        }

        // *****************************************************************************************
        logger.info("Program finished.");
    }

    private static Map<String, String> getPlayerMap() throws IOException {
        return PeopleReader.readPeopleFile();

    }

    private static List<PlayerStats> getPlayerSeasonStats() throws IOException {
        List<BattingLine> battingLines = BattingReader.readBattingFile();

        return SeasonAggregator.aggregateByPlayerSeason(battingLines);
    }
}
