## Lab Exercise 5: Real-World Application - Web Scraper

### Learning Objectives
- Combine multiple ThreadPoolExecutor concepts
- Build a complete application
- Handle real HTTP requests (optional)

### The Problem
Build a web scraper that downloads multiple web pages, extracts information, 
and handles errors. This combines everything you've learned.

### Your Tasks
1. Implement complete `scrape_websites()` function
2. Handle errors gracefully
3. Show real-time progress
4. Generate summary statistics

### Bonus Challenges
1. Add retry logic for failed URLs
2. Implement rate limiting (max N requests per second)
3. Save results to a JSON file
4. Use actual HTTP requests with `requests` library


### Questions to Think About
- How would you optimize the number of workers?
