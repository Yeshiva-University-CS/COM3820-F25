package edu.yu.parallel.query.sequential;

import java.util.Map;

import edu.yu.parallel.query.QueryEngine;

/**
 * Sequential (non-streams) implementation of QueryEngine.
 *
 * IMPORTANT: This implementation MUST NOT use Java Streams.
 * Use classic loops (for, while) for all data processing.
 */
public class SequentialQueryEngine extends QueryEngine {

    public SequentialQueryEngine(Map<String, String> playerMap) {
        super(playerMap);
    }
}