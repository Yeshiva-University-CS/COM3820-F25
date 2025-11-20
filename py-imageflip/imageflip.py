"""
Lab: Image Processing with Executors
Splits an image into tiles, processes each tile, compares performance
"""

import time
import numpy as np
from PIL import Image
from concurrent.futures import ThreadPoolExecutor, ProcessPoolExecutor

# ============================================================================
# Setup: Create Test Image
# ============================================================================

def create_test_image(width=1200, height=800, filename="test_image.png"):
    """Create a colorful test image with gradient and patterns"""
    print(f"Creating test image: {width}x{height}...")
    
    # Create RGB array with gradient
    img_array = np.zeros((height, width, 3), dtype=np.uint8)
    
    # Red gradient (left to right)
    for x in range(width):
        img_array[:, x, 0] = int(255 * (x / width))
    
    # Green gradient (top to bottom)
    for y in range(height):
        img_array[y, :, 1] = int(255 * (y / height))
    
    # Blue pattern (checkerboard)
    tile_size = 50
    for y in range(0, height, tile_size):
        for x in range(0, width, tile_size):
            if ((x // tile_size) + (y // tile_size)) % 2 == 0:
                y_end = min(y + tile_size, height)
                x_end = min(x + tile_size, width)
                img_array[y:y_end, x:x_end, 2] = 200
    
    # Save image
    img = Image.fromarray(img_array)
    img.save(filename)
    print(f"✓ Test image saved: {filename}")
    print(f"  Size: {img.size}, Mode: {img.mode}")
    return filename

# ============================================================================
# Image Processing Functions
# ============================================================================

def process_tile(tile_data):
    """
    Process a single tile: flip horizontally and apply simple filter
    
    Args:
        tile_data: tuple of (tile_array, tile_id, position)
    
    Returns:
        tuple of (processed_array, tile_id, position)
    """
    tile_array, tile_id, position = tile_data
    
    # Flip horizontally
    flipped = np.fliplr(tile_array)
    
    # Apply simple brightness adjustment (simulate processing)
    # This is CPU-intensive
    processed = flipped.astype(np.float32)
    processed = processed * 1.1  # Brighten 10%
    processed = np.clip(processed, 0, 255).astype(np.uint8)
    
    return (processed, tile_id, position)

def split_image_into_tiles(image_path, tile_rows=4, tile_cols=6):
    """
    Split image into tiles for parallel processing
    
    Returns:
        list of (tile_array, tile_id, (row, col)) tuples
    """
    img = Image.open(image_path)
    img_array = np.array(img)
    height, width = img_array.shape[:2]
    
    tile_height = height // tile_rows
    tile_width = width // tile_cols
    
    tiles = []
    tile_id = 0
    
    for row in range(tile_rows):
        for col in range(tile_cols):
            y_start = row * tile_height
            x_start = col * tile_width
            y_end = y_start + tile_height if row < tile_rows - 1 else height
            x_end = x_start + tile_width if col < tile_cols - 1 else width
            
            tile = img_array[y_start:y_end, x_start:x_end]
            tiles.append((tile, tile_id, (row, col)))
            tile_id += 1
    
    return tiles, img_array.shape

def reassemble_tiles(processed_tiles, original_shape, tile_rows=4, tile_cols=6):
    """Reassemble processed tiles into final image"""
    height, width = original_shape[:2]
    result = np.zeros(original_shape, dtype=np.uint8)
    
    tile_height = height // tile_rows
    tile_width = width // tile_cols
    
    for tile_array, tile_id, (row, col) in processed_tiles:
        y_start = row * tile_height
        x_start = col * tile_width
        y_end = y_start + tile_array.shape[0]
        x_end = x_start + tile_array.shape[1]
        
        result[y_start:y_end, x_start:x_end] = tile_array
    
    return result

# ============================================================================
# Processing Methods
# ============================================================================

def process_sequential(image_path, output_path):
    """Process all tiles sequentially"""
    print("\n--- Sequential Processing ---")
    
    # Split into tiles
    tiles, shape = split_image_into_tiles(image_path)
    print(f"Split into {len(tiles)} tiles")
    
    # Process each tile
    start = time.time()
    processed_tiles = []
    for tile_data in tiles:
        result = process_tile(tile_data)
        processed_tiles.append(result)
    elapsed = time.time() - start
    
    # Reassemble
    result_array = reassemble_tiles(processed_tiles, shape)
    result_img = Image.fromarray(result_array)
    result_img.save(output_path)
    
    print(f"Time: {elapsed:.3f}s")
    print(f"✓ Saved: {output_path}")
    return elapsed

def process_with_threads(image_path, output_path, max_workers=4):
    """Process tiles using ThreadPoolExecutor"""
    print(f"\n--- ThreadPoolExecutor (workers={max_workers}) ---")
    
    # Split into tiles
    tiles, shape = split_image_into_tiles(image_path)
    print(f"Split into {len(tiles)} tiles")
    
    # Process with threads
    start = time.time()
    with ThreadPoolExecutor(max_workers=max_workers) as executor:
        processed_tiles = list(executor.map(process_tile, tiles))
    elapsed = time.time() - start
    
    # Reassemble
    result_array = reassemble_tiles(processed_tiles, shape)
    result_img = Image.fromarray(result_array)
    result_img.save(output_path)
    
    print(f"Time: {elapsed:.3f}s")
    print(f"✓ Saved: {output_path}")
    return elapsed

def process_with_processes(image_path, output_path, max_workers=4):
    """Process tiles using ProcessPoolExecutor"""
    print(f"\n--- ProcessPoolExecutor (workers={max_workers}) ---")
    
    # Split into tiles
    tiles, shape = split_image_into_tiles(image_path)
    print(f"Split into {len(tiles)} tiles")
    
    # Process with processes
    start = time.time()
    with ProcessPoolExecutor(max_workers=max_workers) as executor:
        processed_tiles = list(executor.map(process_tile, tiles))
    elapsed = time.time() - start
    
    # Reassemble
    result_array = reassemble_tiles(processed_tiles, shape)
    result_img = Image.fromarray(result_array)
    result_img.save(output_path)
    
    print(f"Time: {elapsed:.3f}s")
    print(f"✓ Saved: {output_path}")
    return elapsed

# ============================================================================
# Main Comparison
# ============================================================================

def run_comparison():
    """Run all three methods and compare results"""
    print("="*60)
    print("CS 3820: Image Processing Comparison Lab")
    print("="*60)
    
    # Create test image
    image_path = create_test_image(width=1200, height=800)
    
    print(f"\nProcessing image: {image_path}")
    print("Task: Flip tiles horizontally + brightness adjustment")
    print("Tiles: 4 rows × 6 cols = 24 tiles total")
    
    # Run tests
    seq_time = process_sequential(image_path, "output_sequential.png")
    thread_time = process_with_threads(image_path, "output_threads.png", max_workers=4)
    process_time = process_with_processes(image_path, "output_processes.png", max_workers=4)
    
    # Summary
    print("\n" + "="*60)
    print("RESULTS SUMMARY")
    print("="*60)
    print(f"Sequential:          {seq_time:.3f}s (baseline)")
    print(f"ThreadPoolExecutor:  {thread_time:.3f}s ({seq_time/thread_time:.2f}x speedup)")
    print(f"ProcessPoolExecutor: {process_time:.3f}s ({seq_time/process_time:.2f}x speedup)")
    
    if thread_time < process_time:
        winner = "ThreadPoolExecutor"
        margin = ((process_time - thread_time) / thread_time) * 100
    else:
        winner = "ProcessPoolExecutor"
        margin = ((thread_time - process_time) / process_time) * 100
    
    print(f"\nWinner: {winner} (by {margin:.1f}%)")
    print("\n" + "="*60)
    print("Check the output images:")
    print("  - output_sequential.png")
    print("  - output_threads.png")
    print("  - output_processes.png")
    print("(All three should look identical)")
    print("="*60)

if __name__ == "__main__":
    run_comparison()
