#!/usr/bin/env python3
"""
Test File Generator for CS 3820 File Compression Assignment

Generates random text files with configurable:
- Number of files
- Size range (min/max)
- Size distribution (uniform, normal, exponential, bimodal, power-law)

Uses a dictionary of common words to create realistic compressible text.
"""

import random
import argparse
from pathlib import Path
from typing import List, Tuple
import math

# Common English words for generating realistic text
COMMON_WORDS = [
    # Articles, prepositions, conjunctions
    "the", "be", "to", "of", "and", "a", "in", "that", "have", "I",
    "it", "for", "not", "on", "with", "he", "as", "you", "do", "at",
    
    # Common nouns
    "time", "person", "year", "way", "day", "thing", "man", "world", "life", "hand",
    "part", "child", "eye", "woman", "place", "work", "week", "case", "point", "government",
    "company", "number", "group", "problem", "fact", "water", "room", "money", "story", "month",
    
    # Common verbs
    "said", "make", "know", "get", "see", "come", "think", "look", "want", "give",
    "use", "find", "tell", "ask", "work", "seem", "feel", "try", "leave", "call",
    "keep", "let", "begin", "help", "show", "hear", "play", "run", "move", "live",
    
    # Common adjectives
    "good", "new", "first", "last", "long", "great", "little", "own", "other", "old",
    "right", "big", "high", "different", "small", "large", "next", "early", "young", "important",
    "few", "public", "bad", "same", "able", "local", "sure", "national", "social", "political",
    
    # Tech/CS related words
    "data", "system", "program", "process", "memory", "thread", "file", "code", "algorithm",
    "function", "class", "object", "array", "string", "integer", "boolean", "loop", "condition",
    "error", "debug", "compile", "execute", "parallel", "concurrent", "sequential", "optimize",
    "performance", "benchmark", "compression", "encryption", "network", "server", "client",
    "database", "query", "index", "cache", "buffer", "queue", "stack", "tree", "graph",
    
    # Additional common words
    "information", "question", "service", "student", "school", "state", "family", "business",
    "issue", "area", "level", "change", "result", "door", "health", "body", "community",
    "name", "president", "university", "research", "book", "study", "course", "power",
    "report", "computer", "technology", "internet", "application", "software", "hardware"
]

def generate_text_content(size_bytes: int) -> str:
    """
    Generate random text content of approximately the specified size.
    
    Uses common English words to create realistic, compressible text.
    Includes line breaks, punctuation, and realistic sentence structure.
    
    Args:
        size_bytes: Target size in bytes
        
    Returns:
        String of generated text
    """
    content = []
    current_size = 0
    
    while current_size < size_bytes:
        # Generate a sentence with 5-20 words
        sentence_length = random.randint(5, 20)
        words = random.choices(COMMON_WORDS, k=sentence_length)
        
        # Capitalize first word
        words[0] = words[0].capitalize()
        
        # Create sentence
        sentence = " ".join(words)
        
        # Add punctuation
        punctuation = random.choice(['.', '.', '.', '!', '?'])  # Bias toward periods
        sentence += punctuation + "\n"
        
        # Occasionally add paragraph breaks
        if random.random() < 0.2:
            sentence += "\n"
        
        content.append(sentence)
        current_size += len(sentence.encode('utf-8'))
    
    return "".join(content)


def generate_file_sizes_uniform(count: int, min_size: int, max_size: int) -> List[int]:
    """
    Generate file sizes with uniform distribution.
    
    All sizes equally likely between min and max.
    """
    return [random.randint(min_size, max_size) for _ in range(count)]


def generate_file_sizes_normal(count: int, min_size: int, max_size: int, 
                                mean_ratio: float = 0.5, std_ratio: float = 0.2) -> List[int]:
    """
    Generate file sizes with normal (Gaussian) distribution.
    
    Args:
        count: Number of files
        min_size: Minimum file size in bytes
        max_size: Maximum file size in bytes
        mean_ratio: Where the mean falls (0-1), default 0.5 (middle)
        std_ratio: Standard deviation as ratio of range, default 0.2
        
    Most files cluster around the mean, fewer at extremes.
    """
    size_range = max_size - min_size
    mean = min_size + (size_range * mean_ratio)
    std_dev = size_range * std_ratio
    
    sizes = []
    for _ in range(count):
        size = int(random.gauss(mean, std_dev))
        # Clamp to range
        size = max(min_size, min(max_size, size))
        sizes.append(size)
    
    return sizes


def generate_file_sizes_exponential(count: int, min_size: int, max_size: int,
                                     lambda_param: float = 2.0) -> List[int]:
    """
    Generate file sizes with exponential distribution.
    
    Args:
        count: Number of files
        min_size: Minimum file size in bytes
        max_size: Maximum file size in bytes
        lambda_param: Rate parameter (higher = more small files)
        
    Many small files, few large files (realistic distribution).
    """
    sizes = []
    size_range = max_size - min_size
    
    for _ in range(count):
        # Generate exponential random variable [0, 1)
        u = random.random()
        # Transform to exponential distribution
        x = -math.log(1 - u) / lambda_param
        # Clamp to [0, 1] and scale
        x = min(1.0, x)
        size = int(min_size + (size_range * x))
        sizes.append(size)
    
    return sizes


def generate_file_sizes_bimodal(count: int, min_size: int, max_size: int,
                                 small_ratio: float = 0.7, peak_separation: float = 0.7) -> List[int]:
    """
    Generate file sizes with bimodal distribution.
    
    Args:
        count: Number of files
        min_size: Minimum file size in bytes
        max_size: Maximum file size in bytes
        small_ratio: Proportion of files in "small" mode (0-1)
        peak_separation: How far apart the peaks are (0-1)
        
    Two clusters: many small files, some large files.
    Realistic for mixed workloads.
    """
    sizes = []
    size_range = max_size - min_size
    
    # Define two modes
    small_peak = min_size + (size_range * (1 - peak_separation) / 2)
    large_peak = max_size - (size_range * (1 - peak_separation) / 2)
    std_dev = size_range * 0.1  # Narrow peaks
    
    for _ in range(count):
        if random.random() < small_ratio:
            # Small file cluster
            size = int(random.gauss(small_peak, std_dev))
        else:
            # Large file cluster
            size = int(random.gauss(large_peak, std_dev))
        
        # Clamp to range
        size = max(min_size, min(max_size, size))
        sizes.append(size)
    
    return sizes


def generate_file_sizes_powerlaw(count: int, min_size: int, max_size: int,
                                  alpha: float = 2.0) -> List[int]:
    """
    Generate file sizes with power-law distribution.
    
    Args:
        count: Number of files
        min_size: Minimum file size in bytes
        max_size: Maximum file size in bytes
        alpha: Power-law exponent (higher = more small files)
        
    Heavy-tailed distribution: very common in real systems.
    """
    sizes = []
    
    for _ in range(count):
        # Generate power-law random variable
        u = random.random()
        # Inverse transform sampling for power law
        x = (min_size ** (1 - alpha) - u * (min_size ** (1 - alpha) - max_size ** (1 - alpha))) ** (1 / (1 - alpha))
        size = int(x)
        # Clamp to range
        size = max(min_size, min(max_size, size))
        sizes.append(size)
    
    return sizes


def generate_file_sizes_category(count: int, 
                                 tiny_pct: float = 25.0,
                                 small_pct: float = 25.0,
                                 medium_pct: float = 25.0,
                                 large_pct: float = 25.0) -> List[int]:
    """
    Generate file sizes based on category percentages.
    
    Categories:
        Tiny:   1 KB - 100 KB
        Small:  100 KB - 1 MB
        Medium: 1 MB - 10 MB
        Large:  10 MB - 100 MB
    
    Args:
        count: Number of files to generate
        tiny_pct: Percentage of tiny files (default: 25%)
        small_pct: Percentage of small files (default: 25%)
        medium_pct: Percentage of medium files (default: 25%)
        large_pct: Percentage of large files (default: 25%)
        
    Returns:
        List of file sizes in bytes
    """
    # Normalize percentages to sum to 100
    total_pct = tiny_pct + small_pct + medium_pct + large_pct
    tiny_pct = (tiny_pct / total_pct) * 100
    small_pct = (small_pct / total_pct) * 100
    medium_pct = (medium_pct / total_pct) * 100
    large_pct = (large_pct / total_pct) * 100
    
    # Calculate file counts for each category using rounding to ensure total == count
    import math
    tiny_count = int(round(count * tiny_pct / 100))
    small_count = int(round(count * small_pct / 100))
    medium_count = int(round(count * medium_pct / 100))
    # Assign remainder to large_count to ensure sum is exactly count
    assigned = tiny_count + small_count + medium_count
    large_count = count - assigned
    # If rounding error causes negative large_count, fix by reducing other categories
    if large_count < 0:
        # Reduce from largest category first
        for cat in ['medium', 'small', 'tiny']:
            if large_count == 0:
                break
            if cat == 'medium' and medium_count > 0:
                medium_count -= 1
                large_count += 1
            elif cat == 'small' and small_count > 0:
                small_count -= 1
                large_count += 1
            elif cat == 'tiny' and tiny_count > 0:
                tiny_count -= 1
                large_count += 1
    
    # Define size ranges for each category (in bytes)
    TINY_MIN = 1024           # 1 KB
    TINY_MAX = 100 * 1024     # 100 KB
    
    SMALL_MIN = 100 * 1024    # 100 KB
    SMALL_MAX = 1024 * 1024   # 1 MB
    
    MEDIUM_MIN = 1024 * 1024      # 1 MB
    MEDIUM_MAX = 10 * 1024 * 1024 # 10 MB
    
    LARGE_MIN = 10 * 1024 * 1024   # 10 MB
    LARGE_MAX = 100 * 1024 * 1024  # 100 MB
    
    sizes = []
    
    # Generate tiny files
    for _ in range(tiny_count):
        sizes.append(random.randint(TINY_MIN, TINY_MAX))
    
    # Generate small files
    for _ in range(small_count):
        sizes.append(random.randint(SMALL_MIN, SMALL_MAX))
    
    # Generate medium files
    for _ in range(medium_count):
        sizes.append(random.randint(MEDIUM_MIN, MEDIUM_MAX))
    
    # Generate large files
    for _ in range(large_count):
        sizes.append(random.randint(LARGE_MIN, LARGE_MAX))
    
    # Shuffle to randomize order
    random.shuffle(sizes)
    
    return sizes


def generate_test_files(output_dir: str, 
                       count: int,
                       min_size: int,
                       max_size: int,
                       distribution: str = "uniform",
                       num_dirs: int = 1,
                       dir_structure: str = "flat",
                       **dist_params) -> List[Tuple[str, int]]:
    """
    Generate test files with specified distribution.
    
    Args:
        output_dir: Base directory to create files in
        count: Number of files to generate
        min_size: Minimum file size in bytes
        max_size: Maximum file size in bytes
        distribution: Distribution type (uniform, normal, exponential, bimodal, powerlaw)
        num_dirs: Number of subdirectories to split files across
        dir_structure: Directory structure (flat, nested, size-based, random)
        **dist_params: Additional parameters for distribution
        
    Returns:
        List of (filepath, size) tuples
    """
    # Create output directory
    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)
    
    # Generate file sizes based on distribution
    print(f"Generating {count} file sizes with {distribution} distribution...")
    
    if distribution == "uniform":
        sizes = generate_file_sizes_uniform(count, min_size, max_size)
    elif distribution == "normal":
        sizes = generate_file_sizes_normal(count, min_size, max_size, **dist_params)
    elif distribution == "exponential":
        sizes = generate_file_sizes_exponential(count, min_size, max_size, **dist_params)
    elif distribution == "bimodal":
        sizes = generate_file_sizes_bimodal(count, min_size, max_size, **dist_params)
    elif distribution == "powerlaw":
        sizes = generate_file_sizes_powerlaw(count, min_size, max_size, **dist_params)
    elif distribution == "category":
        # Category-based distribution (ignores min_size/max_size)
        sizes = generate_file_sizes_category(count, **dist_params)
    else:
        raise ValueError(f"Unknown distribution: {distribution}")
    
    # Create directory structure
    subdirs = create_directory_structure(output_path, num_dirs, dir_structure, sizes)
    
    # Generate files
    print(f"Creating {count} files across {len(subdirs)} directories...")
    created_files = []
    
    for i, size in enumerate(sizes):
        # Determine which directory to use
        if dir_structure == "flat":
            # All files in root
            target_dir = output_path
        elif dir_structure == "round-robin":
            # Distribute evenly across directories
            target_dir = subdirs[i % len(subdirs)]
        elif dir_structure == "size-based":
            # Group by size category
            target_dir = get_size_based_directory(size, subdirs, min_size, max_size)
        elif dir_structure == "nested":
            # Nested hierarchy
            target_dir = subdirs[i % len(subdirs)]
        elif dir_structure == "random":
            # Random distribution
            target_dir = random.choice(subdirs)
        else:
            target_dir = output_path
        
        filename = f"file_{i:04d}.txt"
        filepath = target_dir / filename
        
        # Generate content
        content = generate_text_content(size)
        
        # Write file
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        
        actual_size = filepath.stat().st_size
        created_files.append((str(filepath), actual_size))
        
        # Progress indicator
        if (i + 1) % 100 == 0 or (i + 1) == count:
            print(f"  Created {i + 1}/{count} files...")
    
    return created_files


def create_directory_structure(base_path: Path, num_dirs: int, 
                               structure: str, sizes: List[int]) -> List[Path]:
    """
    Create directory structure based on specified pattern.
    
    Args:
        base_path: Base directory path
        num_dirs: Number of directories to create
        structure: Structure type (flat, nested, size-based, round-robin, random)
        sizes: List of file sizes (for size-based structure)
        
    Returns:
        List of directory paths
    """
    if structure == "flat" or num_dirs <= 1:
        # All files in base directory
        return [base_path]
    
    elif structure == "round-robin" or structure == "random":
        # Simple numbered subdirectories
        subdirs = []
        for i in range(num_dirs):
            subdir = base_path / f"dir_{i:03d}"
            subdir.mkdir(exist_ok=True)
            subdirs.append(subdir)
        return subdirs
    
    elif structure == "size-based":
        # Create directories for size categories
        categories = ["tiny", "small", "medium", "large"]
        subdirs = []
        for category in categories[:min(num_dirs, 4)]:
            subdir = base_path / category
            subdir.mkdir(exist_ok=True)
            subdirs.append(subdir)
        
        # If more dirs requested, add numbered subdirs in each category
        if num_dirs > 4:
            all_subdirs = []
            dirs_per_category = (num_dirs + 3) // 4
            for category_dir in subdirs:
                for i in range(dirs_per_category):
                    nested_dir = category_dir / f"batch_{i:02d}"
                    nested_dir.mkdir(exist_ok=True)
                    all_subdirs.append(nested_dir)
            return all_subdirs[:num_dirs]
        
        return subdirs
    
    elif structure == "nested":
        # Create nested hierarchy (tree structure)
        # Calculate depth based on num_dirs
        if num_dirs <= 3:
            depth = 1
            width = num_dirs
        elif num_dirs <= 9:
            depth = 2
            width = 3
        else:
            depth = 3
            width = 3
        
        subdirs = []
        
        def create_nested(parent: Path, current_depth: int, target_depth: int):
            if current_depth >= target_depth:
                subdirs.append(parent)
                return
            
            for i in range(width):
                if len(subdirs) >= num_dirs:
                    return
                child = parent / f"level_{current_depth}_dir_{i}"
                child.mkdir(exist_ok=True)
                if current_depth == target_depth - 1:
                    subdirs.append(child)
                else:
                    create_nested(child, current_depth + 1, target_depth)
        
        create_nested(base_path, 0, depth)
        return subdirs[:num_dirs]
    
    else:
        return [base_path]


def get_size_based_directory(size: int, subdirs: List[Path], 
                             min_size: int, max_size: int) -> Path:
    """
    Determine which directory based on file size.
    
    Args:
        size: File size in bytes
        subdirs: List of available subdirectories
        min_size: Minimum file size in range
        max_size: Maximum file size in range
        
    Returns:
        Selected directory path
    """
    if len(subdirs) == 1:
        return subdirs[0]
    
    # Categorize by size
    if size < 100_000:  # < 100KB
        category_idx = 0
    elif size < 1_000_000:  # < 1MB
        category_idx = 1
    elif size < 10_000_000:  # < 10MB
        category_idx = 2
    else:  # >= 10MB
        category_idx = 3
    
    # Map to available subdirectories
    idx = min(category_idx, len(subdirs) - 1)
    return subdirs[idx]


def print_statistics(files: List[Tuple[str, int]]):
    """Print statistics about generated files."""
    sizes = [size for _, size in files]
    
    total_size = sum(sizes)
    avg_size = total_size / len(sizes)
    min_size = min(sizes)
    max_size = max(sizes)
    median_size = sorted(sizes)[len(sizes) // 2]
    
    # Size categories
    tiny = sum(1 for s in sizes if s < 100_000)
    small = sum(1 for s in sizes if 100_000 <= s < 1_000_000)
    medium = sum(1 for s in sizes if 1_000_000 <= s < 10_000_000)
    large = sum(1 for s in sizes if s >= 10_000_000)
    
    # Directory distribution
    directories = {}
    for filepath, size in files:
        dir_path = str(Path(filepath).parent)
        if dir_path not in directories:
            directories[dir_path] = {'count': 0, 'total_size': 0}
        directories[dir_path]['count'] += 1
        directories[dir_path]['total_size'] += size
    
    print("\n" + "="*60)
    print("FILE GENERATION SUMMARY")
    print("="*60)
    print(f"Total files:     {len(files)}")
    print(f"Total size:      {total_size / (1024**2):.2f} MB")
    print(f"Average size:    {avg_size / 1024:.2f} KB")
    print(f"Median size:     {median_size / 1024:.2f} KB")
    print(f"Min size:        {min_size / 1024:.2f} KB")
    print(f"Max size:        {max_size / 1024:.2f} KB")
    print(f"\nSize Distribution:")
    print(f"  Tiny   (< 100 KB):     {tiny:4d} files  ({tiny/len(files)*100:.1f}%)")
    print(f"  Small  (100 KB - 1 MB): {small:4d} files  ({small/len(files)*100:.1f}%)")
    print(f"  Medium (1 - 10 MB):     {medium:4d} files  ({medium/len(files)*100:.1f}%)")
    print(f"  Large  (> 10 MB):       {large:4d} files  ({large/len(files)*100:.1f}%)")
    
    if len(directories) > 1:
        print(f"\nDirectory Distribution:")
        # Sort by directory name
        for dir_path in sorted(directories.keys()):
            dir_name = Path(dir_path).name if Path(dir_path).name else "root"
            stats = directories[dir_path]
            print(f"  {dir_name:20s}: {stats['count']:4d} files  "
                  f"({stats['total_size'] / (1024**2):6.2f} MB)")
    
    print("="*60)


def main():
    parser = argparse.ArgumentParser(
        description="Generate test files for compression assignment",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Distribution Types:
  uniform      - All sizes equally likely (flat distribution)
  normal       - Bell curve around mean (most common size)
  exponential  - Many small, few large (realistic)
  bimodal      - Two clusters (small and large files)
  powerlaw     - Heavy-tailed (very realistic for real systems)
  category     - Percentage-based size categories (precise control)

Category Distribution:
  Tiny:   1 KB - 100 KB
  Small:  100 KB - 1 MB
  Medium: 1 MB - 10 MB
  Large:  10 MB - 100 MB
  
  Default: 25% each category
  Specify percentages with --tiny, --small, --medium, --large
  Percentages automatically normalized to 100%

Directory Structures:
  flat         - All files in base directory (default)
  round-robin  - Distribute files evenly across numbered subdirs
  size-based   - Group files by size (tiny/small/medium/large dirs)
  nested       - Create hierarchical tree structure
  random       - Randomly distribute files across subdirs

Examples:
  # Generate 100 files in single directory
  python generate_test_files.py -n 100 -o test_uniform --min 1KB --max 10MB
  
  # Category distribution: 25% each (default)
  python generate_test_files.py -n 200 -o test_balanced -d category
  
  # Category distribution: 50% small files, rest distributed evenly
  python generate_test_files.py -n 200 -o test_small_heavy -d category --small 50
  
  # Category distribution: 70% tiny, 20% small, 10% medium, 0% large
  python generate_test_files.py -n 300 -o test_tiny_heavy -d category --tiny 70 --small 20 --medium 10 --large 0
  
  # Category with size-based directory structure
  python generate_test_files.py -n 400 -o test_organized -d category --tiny 40 --small 30 --medium 20 --large 10 --dirs 4 --structure size-based
  
  # Generate 200 files across 5 subdirectories (round-robin)
  python generate_test_files.py -n 200 -o test_multi --min 1KB --max 100MB --dirs 5 --structure round-robin
  
  # Generate files in nested directory hierarchy
  python generate_test_files.py -n 150 -o test_nested --min 1KB --max 20MB --dirs 9 --structure nested
  
  # Generate with exponential distribution across random directories
  python generate_test_files.py -n 200 -o test_random --min 1KB --max 100MB -d exponential --dirs 8 --structure random
        """
    )
    
    # Required arguments
    parser.add_argument('-n', '--count', type=int, required=True,
                       help='Number of files to generate')
    parser.add_argument('-o', '--output', type=str, required=True,
                       help='Output directory')
    
    # Size arguments (support KB, MB, GB suffixes) - not required for category distribution
    parser.add_argument('--min', type=str, default=None,
                       help='Minimum file size (e.g., 1KB, 100KB, 1MB) - not used with category distribution')
    parser.add_argument('--max', type=str, default=None,
                       help='Maximum file size (e.g., 10MB, 100MB, 1GB) - not used with category distribution')
    
    # Directory structure arguments
    parser.add_argument('--dirs', type=int, default=1,
                       help='Number of subdirectories to create (default: 1)')
    parser.add_argument('--structure', type=str,
                       choices=['flat', 'round-robin', 'size-based', 'nested', 'random'],
                       default='flat',
                       help='Directory structure pattern (default: flat)')
    
    # Distribution arguments
    parser.add_argument('-d', '--distribution', type=str, 
                       choices=['uniform', 'normal', 'exponential', 'bimodal', 'powerlaw', 'category'],
                       default='uniform',
                       help='Size distribution type (default: uniform)')
    
    # Distribution-specific parameters
    parser.add_argument('--mean-ratio', type=float, default=0.5,
                       help='Normal: mean position 0-1 (default: 0.5)')
    parser.add_argument('--std-ratio', type=float, default=0.2,
                       help='Normal: std dev as ratio of range (default: 0.2)')
    parser.add_argument('--lambda', type=float, default=2.0, dest='lambda_param',
                       help='Exponential: rate parameter (default: 2.0)')
    parser.add_argument('--small-ratio', type=float, default=0.7,
                       help='Bimodal: proportion of small files (default: 0.7)')
    parser.add_argument('--peak-separation', type=float, default=0.7,
                       help='Bimodal: separation between peaks 0-1 (default: 0.7)')
    parser.add_argument('--alpha', type=float, default=2.0,
                       help='Power-law: exponent (default: 2.0)')
    
    # Category distribution parameters
    parser.add_argument('--tiny', type=float, default=25.0,
                       help='Category: percentage of tiny files 1KB-100KB (default: 25)')
    parser.add_argument('--small', type=float, default=25.0,
                       help='Category: percentage of small files 100KB-1MB (default: 25)')
    parser.add_argument('--medium', type=float, default=25.0,
                       help='Category: percentage of medium files 1MB-10MB (default: 25)')
    parser.add_argument('--large', type=float, default=25.0,
                       help='Category: percentage of large files 10MB-500MB (default: 25)')
    
    # Other options
    parser.add_argument('--seed', type=int, default=None,
                       help='Random seed for reproducibility')
    
    args = parser.parse_args()
    
    # Set random seed if provided
    if args.seed is not None:
        random.seed(args.seed)
        print(f"Using random seed: {args.seed}")
    
    # Parse sizes (support KB, MB, GB) - only for non-category distributions
    def parse_size(size_str: str) -> int:
        size_str = size_str.upper().strip()
        if size_str.endswith('GB'):
            return int(float(size_str[:-2]) * 1024**3)
        elif size_str.endswith('MB'):
            return int(float(size_str[:-2]) * 1024**2)
        elif size_str.endswith('KB'):
            return int(float(size_str[:-2]) * 1024)
        elif size_str.endswith('B'):
            return int(size_str[:-1])
        else:
            return int(size_str)
    
    # Validate arguments based on distribution type
    if args.distribution == 'category':
        # Category distribution doesn't use min/max
        min_size = 0  # Not used
        max_size = 0  # Not used
        if args.min or args.max:
            print("Note: --min and --max are ignored when using category distribution")
    else:
        # Other distributions require min/max
        if not args.min or not args.max:
            print("Error: --min and --max are required for non-category distributions")
            return 1
        
        min_size = parse_size(args.min)
        max_size = parse_size(args.max)
        
        if min_size >= max_size:
            print("Error: min size must be less than max size")
            return 1
    
    # Prepare distribution parameters
    dist_params = {}
    if args.distribution == 'normal':
        dist_params = {'mean_ratio': args.mean_ratio, 'std_ratio': args.std_ratio}
    elif args.distribution == 'exponential':
        dist_params = {'lambda_param': args.lambda_param}
    elif args.distribution == 'bimodal':
        dist_params = {'small_ratio': args.small_ratio, 'peak_separation': args.peak_separation}
    elif args.distribution == 'powerlaw':
        dist_params = {'alpha': args.alpha}
    elif args.distribution == 'category':
        dist_params = {
            'tiny_pct': args.tiny,
            'small_pct': args.small,
            'medium_pct': args.medium,
            'large_pct': args.large
        }
    
    # Generate files
    print(f"\nGenerating {args.count} test files...")
    if args.distribution != 'category':
        print(f"Size range: {args.min} to {args.max}")
    print(f"Distribution: {args.distribution}")
    if args.dirs > 1:
        print(f"Directory structure: {args.structure} ({args.dirs} directories)")
    if dist_params:
        if args.distribution == 'category':
            # Show normalized percentages
            total = args.tiny + args.small + args.medium + args.large
            print(f"Category percentages:")
            print(f"  Tiny (1KB-100KB):     {args.tiny/total*100:.1f}%")
            print(f"  Small (100KB-1MB):    {args.small/total*100:.1f}%")
            print(f"  Medium (1MB-10MB):    {args.medium/total*100:.1f}%")
            print(f"  Large (10MB-100MB):   {args.large/total*100:.1f}%")
        else:
            print(f"Parameters: {dist_params}")
    print()
    
    files = generate_test_files(
        args.output,
        args.count,
        min_size,
        max_size,
        args.distribution,
        num_dirs=args.dirs,
        dir_structure=args.structure,
        **dist_params
    )
    
    # Print statistics
    print_statistics(files)
    
    print(f"\n✓ Successfully generated {len(files)} files in {args.output}/")
    return 0


if __name__ == "__main__":
    exit(main())