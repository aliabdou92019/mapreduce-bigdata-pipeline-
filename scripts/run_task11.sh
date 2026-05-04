#!/bin/bash

set -e

JAR="/home/cloudera/Task11.jar"
DRIVER="Task11Driver"

INPUT="/user/cloudera/Root/big-data-project/task11/input/real/task11_full.txt"
OUTPUT="/user/cloudera/Root/big-data-project/task11/output/real/task11_full_result"

echo "Checking local JAR..."
if [ ! -f "$JAR" ]; then
    echo "ERROR: JAR file not found: $JAR"
    exit 1
fi

echo "Checking HDFS input file..."
hdfs dfs -test -e "$INPUT"
if [ $? -ne 0 ]; then
    echo "ERROR: Input file not found in HDFS: $INPUT"
    exit 1
fi

echo "Removing old output folder if exists..."
hdfs dfs -rm -r -f "$OUTPUT" 2>/dev/null || true

echo "Running Task 11 MapReduce job..."
hadoop jar "$JAR" "$DRIVER" "$INPUT" "$OUTPUT"

echo "Job finished."
echo "Output:"
hdfs dfs -cat "$OUTPUT/part-r-00000"
