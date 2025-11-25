#!/usr/bin/env python3
"""
main.py

CLI entrypoint that dispatches to the sequential or parallel
implementations of the document similarity harness.
"""

import argparse
import os
import time
from typing import Any, Dict, List

from seq_runner import run_seq
from par_runner import run_par
from shared import FileRecord


def parse_args():
    parser = argparse.ArgumentParser(description="Document similarity harness.")
    parser.add_argument("root_dir", help="Directory containing text files.")
    parser.add_argument(
        "--algo",
        "-a",
        choices=["seq", "par"],
        default="seq",
        help="Algorithm: seq (sequential) or par (parallel).",
    )
    return parser.parse_args()


# ============================================================
# Main
# ============================================================

def main():
    args = parse_args()
    if not os.path.isdir(args.root_dir):
        print(f"ERROR: {args.root_dir!r} is not a directory.")
        return
    
    overall_start = time.perf_counter()
    
    if args.algo == "seq":
        (records, vocab_size, sim_matrix) = run_seq(args.root_dir)
    else:
        (records, vocab_size, sim_matrix) = run_par(args.root_dir)

    wall_time = time.perf_counter() - overall_start

    # Reporting
    stats = aggregate_file_stats(records)
    
    print_run_summary(
        mode_label=args.algo.upper(),
        stats=stats,
        vocab_size=vocab_size,
        total_time=wall_time,
    )

    print_sample_similarities(records, sim_matrix)


# ============================================================
# Shared reporting helpers
# ============================================================

def format_bytes(num_bytes: float) -> str:
    """
    Format a byte count in MB or GB for human-readable output.
    """
    mb = num_bytes / (1024 * 1024)
    gb = num_bytes / (1024 * 1024 * 1024)
    if gb >= 1:
        return f"{gb:.2f} GB"
    else:
        return f"{mb:.2f} MB"


def aggregate_file_stats(records: List[FileRecord]) -> Dict[str, Any]:
    """
    Aggregate basic statistics from a list of FileRecord objects.

    Returns a dict with:
        n_docs, total_bytes, avg_size
    """
    n_docs = len(records)
    total_bytes = sum(r.size_bytes for r in records)
    avg_size = total_bytes / n_docs if n_docs else 0.0

    return {
        "n_docs": n_docs,
        "total_bytes": total_bytes,
        "avg_size": avg_size,
    }


def print_run_summary(
    mode_label: str,
    stats: Dict[str, Any],
    vocab_size: int,
    total_time: float,
) -> None:
    """
    Print a standardized run summary for either sequential or parallel mode.

    Shows:
      - Document count
      - Vocabulary size
      - Total size and average file size
      - Total processing time (end-to-end wall clock)
    """
    print(f"=== {mode_label} Summary ===")
    print(f"Documents:            {stats['n_docs']}")
    print(f"Vocabulary size:      {vocab_size}")
    print()
    print("File statistics:")
    print(f"  {'Total size:':<28}{format_bytes(stats['total_bytes']):>12}")
    print(f"  {'Average file size:':<28}{format_bytes(stats['avg_size']):>12}")
    print(f"  {'Total processing time (sec):':<28}{total_time:>12.3f}")
    print()


def print_sample_similarities(
    records: List[FileRecord],
    sim_matrix: List[List[float]],
    max_show: int = 5,
) -> None:
    """
    Print a small sample of the similarity matrix (upper-left corner),
    using filenames from the corresponding FileRecord objects.
    """
    print("Sample similarities (first {} docs):".format(max_show))
    n_docs = len(records)
    k = min(max_show, n_docs)
    for i in range(k):
        row = sim_matrix[i][:k]
        print(f"{records[i].filename:25s} " +
              " ".join(f"{v:0.3f}" for v in row))


if __name__ == "__main__":
    main()
