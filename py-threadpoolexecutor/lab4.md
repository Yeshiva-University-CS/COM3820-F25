## Lab Exercise 4: Progress Tracking with as_completed()

### Learning Objectives
- Use as_completed() for dynamic result processing
- Implement progress tracking
- Build responsive applications

### The Problem
You're processing a large batch of images. Users want to see progress as images 
are processed, not wait until all are done. Use as_completed() to show real-time 
progress.

### Your Tasks
1. Implement `process_images_with_progress()` using as_completed()
2. Show progress percentage as each image completes
3. Display information about each completed image

### Expected Output
```
=== Image Batch Processing with Progress Tracking ===

Processing 20 images...

  Processing photo_001.jpg...
  Processing photo_002.jpg...
  Processing photo_003.jpg...
  Processing photo_004.jpg...
Completed photo_003.jpg (1.2s, 1456 KB)
Progress: 1/20 images (5.0%)
  Processing photo_005.jpg...
Completed photo_001.jpg (2.1s, 987 KB)
Progress: 2/20 images (10.0%)
  Processing photo_006.jpg...
Completed photo_004.jpg (1.8s, 1789 KB)
Progress: 3/20 images (15.0%)
...
Completed photo_020.jpg (3.2s, 1123 KB)
Progress: 20/20 images (100.0%)

============================================================
Processing Complete!
  Total images: 20
  Average processing time: 2.34 seconds
  Total output size: 28456 KB (27.78 MB)
  Fastest image: photo_003.jpg
  Slowest image: photo_015.jpg
  Wall-clock time: 12.45 seconds
  Speedup: 3.76x
```

### Bonus Challenge
Add a progress bar using the `tqdm` library:
```python
from tqdm import tqdm

for future in tqdm(as_completed(futures), total=len(futures), desc="Processing"):
    result = future.result()
    # process result
```
