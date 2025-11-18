## Lab Exercise 3: Exception Handling

### Learning Objectives
- Handle exceptions in worker threads
- Implement retry logic
- Collect both successes and failures

### The Problem
You're downloading files from various servers. Some servers are unreliable 
and may fail. You need to handle these failures gracefully and report 
both successes and failures.

### Your Tasks
1. Implement `download_all_with_error_handling()`
2. Run multiple times - observe different failures each time
3. Implement `download_with_retry()` for automatic retries

### Expected Output (will vary due to randomness)
```
=== Downloading Files with Error Handling ===

Downloading http://example.com/file1.zip...
Downloading http://example.com/file2.zip...
Downloading http://example.com/file3.zip...
Downloading http://example.com/file4.zip...
Downloaded http://example.com/file1.zip (456 KB)
Downloading http://example.com/file5.zip...
Failed to download http://example.com/file2.zip
Downloading http://example.com/file6.zip...
Downloaded http://example.com/file4.zip (789 KB)
...

==================================================
Results:
  Successful: 6
  Failed: 2
  Total downloaded: 3456 KB

Failed URLs:
  - http://example.com/file2.zip: Failed to download
  - http://example.com/file5.zip: Failed to download
```

### Questions to Think About
- How do exceptions propagate from worker threads?