package edu.yu.parallel.query.forkjoin;

import java.util.Map;

import edu.yu.parallel.query.QueryEngine;

/**
 * Fork/Join framework implementation of QueryEngine.
 * Uses RecursiveTask or RecursiveAction for parallel computation.
 */
public class ForkJoinQueryEngine extends QueryEngine {

    public ForkJoinQueryEngine(Map<String, String> playerMap) {
        super(playerMap);
    }

}
