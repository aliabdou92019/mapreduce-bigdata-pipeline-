#!/bin/bash

set -e

BASE="/user/cloudera/Root/big-data-project"

echo "Creating HDFS folder structure under: $BASE"

# Base
hdfs dfs -mkdir -p "$BASE"

# Task 3
hdfs dfs -mkdir -p "$BASE/task3/input/sample"
hdfs dfs -mkdir -p "$BASE/task3/input/real"
hdfs dfs -mkdir -p "$BASE/task3/lookup/sample"
hdfs dfs -mkdir -p "$BASE/task3/lookup/real"
hdfs dfs -mkdir -p "$BASE/task3/output/sample"
hdfs dfs -mkdir -p "$BASE/task3/output/real"

# Task 11
hdfs dfs -mkdir -p "$BASE/task11/input/sample"
hdfs dfs -mkdir -p "$BASE/task11/input/real"
hdfs dfs -mkdir -p "$BASE/task11/output/sample"
hdfs dfs -mkdir -p "$BASE/task11/output/real"

echo "Done."
echo "Created HDFS structure:"
hdfs dfs -ls -R "$BASE"