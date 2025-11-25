## Assignment #2: Document Similarity Harness (Python)

### 1. Goal

You are given a small document-similarity “harness” that:

* Recursively scans a directory of text files
* Builds per-file term-frequency (TF) maps
* Builds a global vocabulary and dense vectors
* Computes an all-pairs cosine similarity matrix

Your job is to implement **an efficient parallel version** of the pipeline in `par_runner.py`, without changing the external behavior of the program.

You are expected to:

* Understand what the harness does end-to-end
* Decide **what** should be parallelized and **how** to break the work into tasks
* Use Python’s concurrency primitives appropriately
* Achieve a substantial speedup over the sequential baseline on test sets containing a large set of files

I will publish timing results from my own implementation and machine so you have a reference target.

---

### 2. Provided Code

The repository already contains the following files:

* `shared.py` – shared data structures and helpers:

* `seq_runner.py` – **sequential** implementation:

* `par_runner.py` – **stub** for the parallel implementation:

  * Contains `run_par(root_dir: str)` with the correct signature, but currently just prints a message and returns empty structures. This is what you must implement and optimize. 

* `main.py` – CLI entrypoint:

  * `python main.py <root_dir> --algo seq` runs the sequential version
  * `python main.py <root_dir> --algo par` runs your parallel version
  * Handles common reporting (document counts, vocabulary size, total bytes, total wall-clock time, sample of the similarity matrix) 

* `generate_files.py` – test file generator:

  * Creates synthetic text files with various size distributions and directory structures
  * Uses realistic, compressible text (common English and CS/tech vocabulary) 

* `requirements.txt` – currently includes `tqdm` (for progress bars). 

You should **not** change function signatures used by `main.py` (e.g., `run_seq`, `run_par`). You may add helpers or internal utilities as needed.

---

### 3. Your Task: Implement `run_par`

You must implement **`run_par(root_dir: str)` in `par_runner.py`** so that:

1. It performs the same logical work as the sequential runner:

2. It returns the same shape of results as `run_seq` so that `main.py` can report using the same code path.

   ```python
   return records, vocab_size, sim_matrix
   ```

3. It is **significantly faster** than the sequential implementation on larger test sets:

4. It preserves correctness:

   * For the same input directory, the **vocabulary size** and similarity matrix structure (dimensions, symmetry, diagonals) should be consistent with the sequential baseline.
   * Small numerical differences due to floating-point ordering are acceptable, but the results should be semantically equivalent.

You are *not* given a step-by-step recipe. Part of the assignment is to:

* Read and understand the existing code
* Decide where parallelism belongs
* Design an appropriate task decomposition strategy
* Reason about overhead, granularity, and CPU utilization

You are free to use `ProcessPoolExecutor`, `ThreadPoolExecutor`, or a combination, as long as your design makes sense for this workload and runs correctly.

---

### 4. Test Data: Using the Generator

Use `generate_files.py` to create test corpora of different sizes. From the root of the repository:

```bash
python generate_files.py -n 50 -o test_tiny   -d category --tiny 50 --dirs 4 --structure nested --seed 4119
python generate_files.py -n 50 -o test_small  -d category --tiny 50 --dirs 4 --structure nested --seed 4119
python generate_files.py -n 50 -o test_medium -d category --tiny 50 --dirs 4 --structure nested --seed 4119
```

For a large test set, use fewer (but bigger) files, e.g.:

```bash
python generate_files.py -n 30 -o test_large -d category --large 50 --dirs 4 --structure nested --seed 4119
```

* You may tweak the parameters if you want to experiment, but these commands let you **recreate my practice sets exactly**.
* The nested directory structure ensures your code correctly handles recursive traversal.

---

### 5. Running and Comparing

Once you have generated the data sets, you can run:

```bash
# Sequential
python main.py test_tiny   --algo seq
python main.py test_small  --algo seq
python main.py test_medium --algo seq
python main.py test_large  --algo seq

# Parallel (your implementation)
python main.py test_tiny   --algo par
python main.py test_small  --algo par
python main.py test_medium --algo par
python main.py test_large  --algo par
```

For each run, note:

* **Total processing time (sec)** printed by `main.py`
* Number of documents and total size
* Sanity checks:

  * Is vocabulary size the same between `seq` and `par` for a given test set?
  * Does the similarity matrix have the right dimensions (N×N) and 1.0 on the diagonal?

I will post **my timing results** for the same test sets on my machine, so you can see how your implementation compares. You are not required to exactly match my times (different machines will differ), but you should clearly outperform the sequential implementation on the larger sets.

---

### 6. Deliverables

You should submit:

1. **Your `par_runner.py` implementation** (this is the main artifact being graded).
2. A **markdown file** named `writeup.md` with a **short write-up** that includes:

   * A brief explanation of:

     * What part(s) of the pipeline you parallelized
     * How you chose your task decomposition (granularity, batching/chunking strategy, etc.)
     * Any trade-offs or design choices you made (e.g., why processes vs threads, how you handled large vs small files)
   * A simple table of timing results for:

     * `test_tiny`, `test_small`, `test_medium`, `test_large`
     * Columns: dataset, number of files, total size (MB/GB), `seq` time, `par` time, speedup


---

### 7. Grading Criteria

Your grade will be based on:

* **Correctness (40%)**

  * The program runs without errors on the test sets.
  * The output is consistent with the sequential implementation (same number of documents, sensible vocabulary size, correctly shaped similarity matrix).

* **Performance (40%)**

  * Measurable speedup over the sequential baseline for all sets.

* **Write-up & analysis (10%)**

  * Articulates why you parallelized the way you did and reflects on the results.

---

