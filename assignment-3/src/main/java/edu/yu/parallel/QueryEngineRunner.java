package edu.yu.parallel;

import java.util.List;
import java.util.Map;

import edu.yu.parallel.query.QueryEngine;
import edu.yu.parallel.query.forkjoin.ForkJoinQueryEngine;
import edu.yu.parallel.query.parallel_streams.ParallelStreamsQueryEngine;
import edu.yu.parallel.query.sequential.SequentialQueryEngine;
import edu.yu.parallel.query.sequential_streams.SequentialStreamsQueryEngine;

public class QueryEngineRunner {

    /** The four execution modes for the assignment. */
    public enum RunType {
        SEQUENTIAL,
        SEQUENTIAL_STREAMS,
        FORK_JOIN,
        PARALLEL_STREAMS
    }

    private final Map<String, String> playerMap; // playerID -> "Last, First"

    public QueryEngineRunner(Map<String, String> playerMap) {
        this.playerMap = playerMap;
    }

    public List<RunType> getAllRunTypes() {
        return List.of(RunType.SEQUENTIAL,
                       RunType.SEQUENTIAL_STREAMS,
                       RunType.FORK_JOIN,
                       RunType.PARALLEL_STREAMS);
    }
    
    // ---- Generic factory using the enum -------------------------------------

    public QueryEngine createEngine(RunType type) {
        return switch (type) {
            case SEQUENTIAL -> createSequentialEngine();
            case SEQUENTIAL_STREAMS -> createSequentialStreamsEngine();
            case FORK_JOIN -> createForkJoinEngine();
            case PARALLEL_STREAMS -> createParallelStreamsEngine();
        };
    }

    // ---- Factory methods ----------------------------------------------------

    private QueryEngine createSequentialEngine() {
        return new SequentialQueryEngine(playerMap);
    }

    private QueryEngine createSequentialStreamsEngine() {
        return new SequentialStreamsQueryEngine(playerMap);
    }

    private QueryEngine createForkJoinEngine() {
        return new ForkJoinQueryEngine(playerMap);
    }

    private QueryEngine createParallelStreamsEngine() {
        return new ParallelStreamsQueryEngine(playerMap);
    }
}
