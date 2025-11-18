import time
import random
from concurrent.futures import ThreadPoolExecutor, as_completed

class WebPage:
    """Simulated web page"""
    def __init__(self, url):
        self.url = url
        self.title = f"Page: {url}"
        self.word_count = random.randint(100, 1000)
        self.links = random.randint(5, 50)
    
    def __repr__(self):
        return f"WebPage({self.url}, {self.word_count} words)"

def fetch_page(url):
    """
    Simulate fetching a web page
    20% chance of timeout/failure
    """
    print(f"  Fetching {url}...")
    time.sleep(random.uniform(0.5, 2.0))
    
    # Simulate occasional failures
    if random.random() < 0.2:
        raise TimeoutError(f"Timeout fetching {url}")
    
    return WebPage(url)

def extract_info(page):
    """Extract information from page"""
    return {
        "url": page.url,
        "title": page.title,
        "word_count": page.word_count,
        "links": page.links
    }

# URLs to scrape
urls = [
    "http://example.com/page1",
    "http://example.com/page2",
    "http://example.com/page3",
    "http://example.com/page4",
    "http://example.com/page5",
    "http://example.com/page6",
    "http://example.com/page7",
    "http://example.com/page8",
    "http://example.com/page9",
    "http://example.com/page10",
]

def scrape_websites(urls, max_workers=4):
    """
    Scrape all URLs concurrently with error handling and progress
    """
    # TODO: Implement complete web scraper
    # 1. Use ThreadPoolExecutor with max_workers
    # 2. Submit all fetch tasks
    # 3. Use as_completed() for progress
    # 4. Handle TimeoutError exceptions
    # 5. Extract info from successful fetches
    # 6. Show progress as pages complete
    # 7. Return (successful_results, failed_urls)
    
    pass

def generate_report(results, failures, elapsed_time):
    """Generate summary report"""
    print(f"\n{'='*60}")
    print(f"Scraping Report")
    print(f"{'='*60}")
    print(f"Time elapsed: {elapsed_time:.2f} seconds")
    print(f"Successful: {len(results)}")
    print(f"Failed: {len(failures)}")
    
    if results:
        total_words = sum(r['word_count'] for r in results)
        total_links = sum(r['links'] for r in results)
        print(f"\nStatistics:")
        print(f"  Total words scraped: {total_words:,}")
        print(f"  Total links found: {total_links}")
        print(f"  Average words per page: {total_words/len(results):.0f}")
    
    if failures:
        print(f"\nFailed URLs:")
        for url, error in failures:
            print(f"  - {url}: {error}")

if __name__ == "__main__":
    print("=== Web Scraper with ThreadPoolExecutor ===\n")
    print(f"Scraping {len(urls)} URLs...\n")
    
    start = time.time()
    results, failures = scrape_websites(urls, max_workers=4)
    elapsed = time.time() - start
    
    generate_report(results, failures, elapsed)