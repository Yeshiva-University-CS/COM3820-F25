#!/usr/bin/env python3
"""
seq_runner.py

Sequential implementation of the document similarity harness.
"""

import os
from typing import List

from tqdm import tqdm

from shared import (
    FileRecord,
    find_all_files,
    build_vocabulary,
    build_dense_vectors,
    compute_similarity_matrix_sequential,
    read_file,
    tokenize,
    build_tf,
    get_file_size,
)


def build_tf_records(file_paths: List[str]) -> List[FileRecord]:
    """
    Sequentially build TF records for each file.
    Shows a tqdm progress bar over files.
    """
    records: List[FileRecord] = []

    for path in tqdm(
        file_paths,
        desc="Processing files (seq)",
        mininterval=0.01,   # redraw every 10ms if needed
        miniters=1,         # update every iteration
    ):
        text = read_file(path)
        tokens = tokenize(text)
        tf = build_tf(tokens)

        rec = FileRecord(
            filename=os.path.basename(path),
            full_path=path,
            size_bytes=get_file_size(path),
            tf_map=tf,
        )        

        records.append(rec)

    return records

#============================================================
# Main sequential runner
#===========================================================

def run_seq(root_dir: str) -> None:
    """
    Run the sequential algorithm
    """
    print(f"\nRunning SEQUENTIAL algorithm on: {root_dir}\n")

    # Discover files
    file_paths = find_all_files(root_dir)
    if not file_paths:
        print("No files found.")
        return

    # Process files sequentially
    records = build_tf_records(file_paths)

    # Build vocab, vectors, and similarity matrix
    vocab = build_vocabulary(records)
    vectors = build_dense_vectors(records, vocab)
    sim_matrix = compute_similarity_matrix_sequential(vectors)
    
    # return the following for reporting in main
    return records, len(vocab), sim_matrix
    


