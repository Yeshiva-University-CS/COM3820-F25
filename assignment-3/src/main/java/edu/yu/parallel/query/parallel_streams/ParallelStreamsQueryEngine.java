package edu.yu.parallel.query.parallel_streams;

import java.util.Map;

import edu.yu.parallel.query.QueryEngine;

/**
 * Parallel streams implementation of QueryEngine.
 * Uses Java parallel streams for concurrent data processing.
 */
public class ParallelStreamsQueryEngine extends QueryEngine {

    public ParallelStreamsQueryEngine(Map<String, String> playerMap) {
        super(playerMap);
    }
}