#!/usr/bin/env python3
"""
shared.py

Utilities and data structures shared by both sequential and parallel
implementations of the document similarity harness.
"""

import os
import re
from dataclasses import dataclass
from math import sqrt
from typing import Dict, List


# ============================================================
# Data Record
# ============================================================

@dataclass
class FileRecord:
    filename: str               # basename only
    full_path: str              # original full path
    size_bytes: int             # os.path.getsize
    tf_map: Dict[str, int]      # term frequency map


# ============================================================
# File scanning
# ============================================================

def find_all_files(root_dir: str) -> List[str]:
    """
    Recursively find all files under root_dir.
    """
    file_paths: List[str] = []
    for dirpath, _, filenames in os.walk(root_dir):
        for name in filenames:
            path = os.path.join(dirpath, name)
            if os.path.isfile(path):
                file_paths.append(path)
    return file_paths


# ============================================================
# Text processing helpers
# ============================================================

def get_file_size(path: str) -> int:
    """
    Get the size of the file at the given path.
    Returns 0 if the file cannot be accessed.
    """
    try:
        return os.path.getsize(path)
    except OSError:
        return 0
    
    
def read_file(path: str) -> str:
    """
    Read a file as text. Try utf-8, fallback to latin-1.
    """
    try:
        with open(path, "r", encoding="utf-8") as f:
            return f.read()
    except UnicodeDecodeError:
        try:
            with open(path, "r", encoding="latin-1") as f:
                return f.read()
        except Exception:
            return ""   # unreadable → empty


_token_pattern = re.compile(r"\b[a-zA-Z]{3,}\b")


def tokenize(text: str) -> List[str]:
    """
    Simple tokenizer:
    - lowercase
    - alphabetic words of length >= 3
    """
    return _token_pattern.findall(text.lower())


def build_tf(tokens: List[str]) -> Dict[str, int]:
    """
    Build a term-frequency map (pure Python).
    """
    tf: Dict[str, int] = {}
    for tok in tokens:
        tf[tok] = tf.get(tok, 0) + 1
    return tf


# ============================================================
# TF → Vectors → Similarity
# ============================================================

def build_vocabulary(records: List[FileRecord]) -> List[str]:
    """
    Construct a sorted vocabulary of all tokens.
    """
    vocab = set()
    for rec in records:
        vocab.update(rec.tf_map.keys())
    return sorted(vocab)


def build_dense_vectors(records: List[FileRecord], vocab: List[str]) -> List[List[int]]:
    """
    Convert TF maps to dense vectors aligned to the vocabulary.
    """
    token_index = {tok: i for i, tok in enumerate(vocab)}
    vectors: List[List[int]] = []

    for rec in records:
        vec = [0] * len(vocab)
        for token, count in rec.tf_map.items():
            idx = token_index.get(token)
            if idx is not None:
                vec[idx] = count
        vectors.append(vec)

    return vectors


def cosine_similarity(vec_a: List[int], vec_b: List[int]) -> float:
    """
    Pure-Python cosine similarity.
    """
    dot = 0
    sum_sq_a = 0
    sum_sq_b = 0

    for a, b in zip(vec_a, vec_b):
        dot += a * b
        sum_sq_a += a * a
        sum_sq_b += b * b

    if sum_sq_a == 0 or sum_sq_b == 0:
        return 0.0

    return dot / (sqrt(sum_sq_a) * sqrt(sum_sq_b))


def compute_similarity_matrix_sequential(vectors: List[List[int]]) -> List[List[float]]:
    """
    Compute full NxN similarity matrix sequentially.
    (Can be used as a reference or for smaller corpora even in parallel mode.)
    """
    n = len(vectors)
    sim: List[List[float]] = [[0.0] * n for _ in range(n)]

    for i in range(n):
        sim[i][i] = 1.0
        for j in range(i + 1, n):
            s = cosine_similarity(vectors[i], vectors[j])
            sim[i][j] = s
            sim[j][i] = s

    return sim

