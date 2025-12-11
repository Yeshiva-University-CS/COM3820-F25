package edu.yu.parallel.query.sequential_streams;

import java.util.Map;

import edu.yu.parallel.query.QueryEngine;

/**
 * Sequential streams implementation of QueryEngine.
 * Uses Java Streams API without parallelization.
 */
public class SequentialStreamsQueryEngine extends QueryEngine {

    public SequentialStreamsQueryEngine(Map<String, String> playerMap) {
        super(playerMap);
    }

}
