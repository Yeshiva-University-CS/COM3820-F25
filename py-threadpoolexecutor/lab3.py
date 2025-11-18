import time
import random
from concurrent.futures import ThreadPoolExecutor

class DownloadError(Exception):
    """Custom exception for download failures"""
    pass

def download_file(url):
    """
    Simulate file download - randomly fails 30% of the time
    DO NOT MODIFY THIS FUNCTION
    """
    print(f"Downloading {url}...")
    time.sleep(random.uniform(0.5, 2.0))  # Simulate network delay
    
    # 30% chance of failure
    if random.random() < 0.3:
        raise DownloadError(f"Failed to download {url}")
    
    file_size = random.randint(100, 1000)
    print(f"Downloaded {url} ({file_size} KB)")
    return {"url": url, "size": file_size}

# URLs to download
urls = [
    "http://example.com/file1.zip",
    "http://example.com/file2.zip",
    "http://example.com/file3.zip",
    "http://example.com/file4.zip",
    "http://example.com/file5.zip",
    "http://example.com/file6.zip",
    "http://example.com/file7.zip",
    "http://example.com/file8.zip",
]

def download_all_with_error_handling(urls):
    """
    Download all URLs concurrently and handle errors gracefully
    Return: (successes, failures)
    """
    successes = []
    failures = []
    
    # TODO: Implement this function
    # 1. Use ThreadPoolExecutor with 4 workers
    # 2. Submit all download tasks
    # 3. For each future, try to get result
    # 4. If exception, catch it and add to failures
    # 5. If success, add to successes
    # 6. Return both lists
    
    pass

def download_with_retry(url, max_retries=3):
    """
    Download with retry logic - BONUS CHALLENGE
    """
    # TODO: Implement retry logic
    # Try up to max_retries times
    # If all retries fail, return None
    pass

if __name__ == "__main__":
    print("=== Downloading Files with Error Handling ===\n")
    
    successes, failures = download_all_with_error_handling(urls)
    
    print(f"\n{'='*50}")
    print(f"Results:")
    print(f"  Successful: {len(successes)}")
    print(f"  Failed: {len(failures)}")
    
    if successes:
        total_size = sum(s['size'] for s in successes)
        print(f"  Total downloaded: {total_size} KB")
    
    if failures:
        print(f"\nFailed URLs:")
        for url, error in failures:
            print(f"  - {url}: {error}")