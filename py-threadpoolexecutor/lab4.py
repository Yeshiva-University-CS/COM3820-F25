import time
import random
from concurrent.futures import ThreadPoolExecutor, as_completed

def process_image(image_path):
    """
    Simulate image processing with varying completion times
    DO NOT MODIFY
    """
    filename = image_path.split('/')[-1]
    processing_time = random.uniform(1, 4)  # 1-4 seconds
    
    print(f"  Processing {filename}...")
    time.sleep(processing_time)
    
    # Simulate different processing results
    operations = ["resized", "filtered", "compressed", "watermarked"]
    result = {
        "filename": filename,
        "operations": operations,
        "processing_time": processing_time,
        "output_size": random.randint(500, 2000)  # KB
    }
    
    return result

# Generate list of image paths
image_paths = [f"images/photo_{i:03d}.jpg" for i in range(1, 21)]

def process_images_with_progress(image_paths):
    """
    Process images and show real-time progress
    """
    # TODO: Implement this function
    # 1. Create ThreadPoolExecutor with 4 workers
    # 2. Submit all image processing tasks
    # 3. Use as_completed() to process results as they finish
    # 4. Show progress: "Completed X/Y images (Z%)"
    # 5. Display info about each completed image
    # 6. Return all results
    
    pass

def calculate_statistics(results):
    """Calculate and display statistics"""
    total_images = len(results)
    total_time = sum(r['processing_time'] for r in results)
    avg_time = total_time / total_images
    total_size = sum(r['output_size'] for r in results)
    
    print(f"\n{'='*60}")
    print(f"Processing Complete!")
    print(f"  Total images: {total_images}")
    print(f"  Average processing time: {avg_time:.2f} seconds")
    print(f"  Total output size: {total_size} KB ({total_size/1024:.2f} MB)")
    print(f"  Fastest image: {min(results, key=lambda x: x['processing_time'])['filename']}")
    print(f"  Slowest image: {max(results, key=lambda x: x['processing_time'])['filename']}")

if __name__ == "__main__":
    print("=== Image Batch Processing with Progress Tracking ===\n")
    print(f"Processing {len(image_paths)} images...\n")
    
    start = time.time()
    results = process_images_with_progress(image_paths)
    elapsed = time.time() - start
    
    calculate_statistics(results)
    print(f"  Wall-clock time: {elapsed:.2f} seconds")
    
    # Calculate speedup
    total_processing = sum(r['processing_time'] for r in results)
    speedup = total_processing / elapsed
    print(f"  Speedup: {speedup:.2f}x")
