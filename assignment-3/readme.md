# **README — Baseball WAR Analytics Engine**

### *Parallel Programming — Assignment Starter Guide*

This assignment asks you to implement a baseball analytics engine using multiple execution models (sequential, sequential streams, ForkJoin, and parallel streams). Your code will analyze MLB batting data and compute several WAR-based statistics as defined in `QueryEngine`.

You are **not** implementing data ingestion or metrics calculation. You will be given:

* `List<PlayerStats>` — one object per player-season (already aggregated)
* `Map<String, String> playerMap` — mapping `playerID → "FirstName LastName"`
* `PlayerAnalytics` — all WAR/wOBA/wRAA calculations are **already implemented**
* A complete **test harness** with benchmarking 

Your job is to:

1. Implement all query methods in the four `QueryEngine` subclasses
2. Aggregate and filter `PlayerAnalytics` data according to specifications
3. Produce exactly the printed output required in `QueryEngine` Javadoc
4. Implement each query **four times** — one per execution model

---

# **2. What Is Provided (Do Not Modify)**

The project contains:

| Component                      | Purpose                                                                     |
| ------------------------------ | --------------------------------------------------------------------------- |
| **PlayerStats**                | Raw per-season batting aggregates (already computed by `SeasonAggregator`) |
| **PlayerAnalytics**            | Advanced metrics: `wOBA()`, `wRAA()`, `getSimpleWAR()` — **all computed**  |
| **QueryEngine** (abstract)     | Full specification of 6 required query methods                             |
| **Main.java** (test harness)   | Runs all 4 engines, calls all 6 queries, prints timing results             |
| **Utils.benchmarkRunnable()**  | Executes a task 6 times, drops slowest 2 + fastest 1, returns avg of 3   |
| **Utils.cosineSimilarity()**   | Computes cosine similarity between two vectors — **you must use this**     |
| **BattingReader**              | Loads MLB Batting.csv                                                      |
| **PeopleReader**               | Loads People.csv and builds `playerMap`                                    |
| **QueryEngineRunner**          | Factory to instantiate all 4 `QueryEngine` implementations                |

You will create only the four `QueryEngine` subclasses.

---

# **3. Files You Will Modify**

Create these four classes (one implementation per execution model):

```
src/edu/yu/parallel/query/sequential/SequentialQueryEngine.java
src/edu/yu/parallel/query/sequential_streams/SequentialStreamsQueryEngine.java
src/edu/yu/parallel/query/forkjoin/ForkJoinQueryEngine.java
src/edu/yu/parallel/query/parallel_streams/ParallelStreamsQueryEngine.java
```

Each must:
* Extend `edu.yu.parallel.query.QueryEngine`
* Override **all 6 methods**
* Call the provided logger with output formatted **exactly** as specified in Javadoc

---

# **4. Required Methods (All Six)**

Implement in each of the four subclasses:

1. **`calculatePlayerAnalytics(List<PlayerStats>)`** — **IMPORTANT:** This is the entry point.
   - Compute **season-average wOBA** across all players for each year
   - For each `PlayerStats`, call `PlayerAnalytics.of(playerStats, seasonAvgWoba)` to create the analytics object
   - Return the list of `PlayerAnalytics` objects
   - **Note:** `PlayerAnalytics` does all the math (wOBA, wRAA, WAR); you provide the season-average context

2. **`printTopTenCareerWAR(List<PlayerAnalytics>)`** — Aggregate career WAR, print top 10

3. **`printTopFiveWARByDecade(List<PlayerAnalytics>)`** — Top 5 total WAR players per decade (1900+)

4. **`printTopTenConsecutiveSevenYearWarWindow(List<PlayerAnalytics>)`** — Best 7-year spans

5. **`printTopTenBestSevenYearWar(List<PlayerAnalytics>)`** — Sum of best 7 seasons (non-consecutive)

6. **`printTopTenMostSimilarCareers(List<PlayerAnalytics>)`** — Cosine similarity of career vectors

The **full specification** (aggregation rules, filtering, output format) is in the `QueryEngine` Javadoc.

---

# **4.5 Understanding `calculatePlayerAnalytics` (Critical)**

This method is the **pipeline entry point**. Here's what you must do:

**Step 1: Compute Season-Average wOBA**
* For each year in the dataset, calculate the **average wOBA across all players that year**
* `PlayerStats` contains the raw batting stats (hits, walks, HRs, etc.)
* To get a player's season wOBA: call `PlayerAnalytics.of(playerStats, 0.0).getWOBA()`
* Aggregate these by year to compute the season average

**Step 2: Create PlayerAnalytics for Each Season**
* For each `PlayerStats`, call: `PlayerAnalytics.of(playerStats, seasonAvgWoba.get(year))`
* Pass the season-average wOBA computed in Step 1
* `PlayerAnalytics` will use this to compute wRAA and SimpleWAR internally
* Collect all results in a list

**Why do this?**
* `PlayerAnalytics.getSimpleWAR()` depends on the season-average wOBA to compute wRAA
* Each player's WAR is relative to the league average that year
* This is why the method takes a `List<PlayerStats>` (you need all players to compute the season average)

---

Implement the same 6 methods once per execution model:

| Class                            | Constraints                                                            |
| -------------------------------- | ---------------------------------------------------------------------- |
| **SequentialQueryEngine**        | Traditional loops only. No streams. No parallelization.               |
| **SequentialStreamsQueryEngine** | Sequential streams only (`stream()`). No parallelization or ForkJoin. |
| **ForkJoinQueryEngine**          | Explicit `RecursiveTask` or `RecursiveAction` using `ForkJoinPool`.   |
| **ParallelStreamsQueryEngine**   | Parallel streams (`parallelStream()`). Thread-safe aggregations.      |

**All four implementations must produce identical textual output.**

---

# **6. Critical WAR Thresholding Rule**

This rule applies **whenever you aggregate WAR** (which is always):

* A season with **PA < 400** contributes **0 WAR** (not included in sums)
* A season with **PA < 400** is not considered for the career vector construction
* Only use `getSimpleWAR()` when `getPA() >= 400` (i.e. the player had less than 400 plate appearances)
* If `getPA() < 400`, treat that season as if it has 0 WAR

**Where this rule applies:**
* Career WAR aggregation
* Decade WAR aggregation
* All 7-year window calculations
* Career vector construction (for cosine similarity)

---

# **6.5 What `PlayerAnalytics` Does vs. What You Do**

| Who Does It | What                                                                  |
| ----------- | --------------------------------------------------------------------- |
| **Provided** | Calculates `wOBA()`, `wRAA()`, `getSimpleWAR()` from season stats     |
| **You Do**  | Compute season-average wOBA and call `PlayerAnalytics.of()`          |
| **Provided** | Stores player ID, year, PA, and all advanced metrics                 |
| **You Do**  | Aggregate those metrics by player, decade, year, or window          |
| **Provided** | Provides getter methods: `getPlayerID()`, `getYear()`, `getPA()`, etc. |
| **You Do**  | Filter (PA >= 400), sort, and print results                          |

You **do not** implement WAR calculations. You **do** implement data aggregation and filtering logic.

---

# **6.6 No Caching Between Query Calls**

**IMPORTANT:** Each query method receives the **same** `List<PlayerAnalytics>` reference, but you must **process it independently**.

* **No caching of intermediate results** across query calls
* For example, **No shared state** between `printTopTenCareerWAR()` and `printTopFiveWARByDecade()`
* Each method must **independently iterate**, filter, aggregate, and sort the input list
* This ensures your implementation is deterministic and testable
* The test harness calls all 6 methods sequentially; each must work as if it's the first call

**Example:** If you compute career WAR totals in `printTopTenCareerWAR()`, you cannot store those totals in an instance variable and reuse them in `printTopTenMostSimilarCareers()`. Recompute everything each time.

This design ensures:
* Code is stateless and thread-safe
* Each execution model can implement parallelization independently
* No subtle bugs from shared mutable state

---

# **7. The Test Harness**

`Main.java` is your **grading test harness**. It:

1. Loads CSV files and builds `playerMap`
2. For each of the 4 execution models:
   - Instantiates the engine
   - Calls `calculatePlayerAnalytics()` once to build the list
   - For each of the 6 queries:
     - Prints a header
     - Calls the query method (output goes to log)
     - Benchmarks the query using `Utils.benchmarkRunnable()`
     - Prints timing results
   - Prints total time for that execution model

**Your output must match exactly.** Do not print headers, rankings, or extra text beyond what `QueryEngine` specifies.

---

# **8. Benchmarking Strategy (Provided)**

`Utils.benchmarkRunnable()`:

1. Runs your task **1 warm-up time** (discarded)
2. Runs your task **6 timed iterations**
3. **Drops the fastest 1 and slowest 2** (keeps middle 3)
4. Returns the **average of the middle 3 timings** (in milliseconds)
5. Automatically **suppresses logging during timing** for accuracy
6. Restores the original log level after timing

You must **not** modify this or the main harness.

---

# **9. Utility Methods (Use These)**

**Do not implement your own versions.**

```java
// In Utils:

public static double cosineSimilarity(double[] vecA, double[] vecB)
    // Computes cosine similarity; used in printTopTenMostSimilarCareers
```

---

# **10. Output Format Rules**

From `QueryEngine` Javadoc:

* **No extra text, no headers** (except decade headers in `printTopFiveWARByDecade`)
* **Use the name map:** print `playerMap.get(playerID)` to get `"FirstName LastName"`
* **Format specifiers are precise:**
  * Career WAR: `"%.2f FirstName LastName"`
  * Best 7-year WAR: `"%.4f FirstName LastName (startYear) <-> FirstName LastName (startYear)"`
  * All output goes via `logger.info()`

Follow the Javadoc exactly.

---

# **11. Example: Career WAR**

Given a `List<PlayerAnalytics>`:

```
For each unique playerID:
  1. Sum SimpleWAR for seasons where PA >= 400
  2. Aggregate by playerID
  3. Sort descending by total WAR
  4. Print top 10 in format: "%.2f FirstName LastName"
```

All four implementations should produce this same output.

---

# **12. Grading Rubric**

Your code will be evaluated on:

* **Correctness of aggregation logic** — proper filtering (PA >= 400) and sorting
* **Output format exactness** — must match Javadoc specifications exactly
* **Proper use of execution model** — sequential loops, streams, ForkJoin, or parallel streams
* **Thread safety** (for parallel/ForkJoin) — no race conditions
* **Identical output across all 4 engines** — same numbers, same order
* **Code clarity** — reasonable organization, minimal duplication
* **Parallel execution performance** — parallel engines (ForkJoin, parallel streams) should be noticeably faster than sequential on multi-core systems; poor parallelization strategy or excessive synchronization will impact this score

---

# **13. Running the Program**

```bash
mvn clean package
java -jar target/baseball-stats-1.jar
```

---

# **14. Key Takeaways**

1. **You implement 4 classes**, not 1. Each is the same business logic in a different execution model.
2. **PlayerAnalytics does the math.** You aggregate and filter.
3. **The test harness calls you.** Don't modify `Main.java`.
4. **Output must be exact.** No extra text. Use the format strings in Javadoc.
5. **Use provided utilities.** Don't rewrite cosine similarity or benchmarking.
6. **All 4 should produce identical output.** Differences in output indicate a bug.

---

# **15. Hints**

* Group analytics by `playerID` early in each query.
* ForkJoin solutions often divide the player list into subranges and merge results.
* Parallel streams are thread-safe if you use collectors properly (e.g., `Collectors.toMap()` with concurrent variants).
* For similarity queries, build a list of players first, then compute all pairwise or one-best matches.
* Test one engine thoroughly before moving to the next.
* Print exactly what the Javadoc says — no more, no less.


