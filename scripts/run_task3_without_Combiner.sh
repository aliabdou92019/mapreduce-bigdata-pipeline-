#!/bin/bash

set -e

JAR="/home/cloudera/Task3_without_Combiner.jar"
DRIVER="Task3Driver"

INPUT="/user/cloudera/Root/big-data-project/task3/input/real/task3_full.txt"
OUTPUT="/user/cloudera/Root/big-data-project/task3/output/real/task3_full_result_without_combiner"
LOOKUP="/user/cloudera/Root/big-data-project/task3/lookup/real/task3_lookup.txt"

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

echo "Checking HDFS lookup file..."
hdfs dfs -test -e "$LOOKUP"
if [ $? -ne 0 ]; then
    echo "ERROR: Lookup file not found in HDFS: $LOOKUP"
    exit 1
fi

echo "Removing old output folder if exists..."
hdfs dfs -rm -r -f "$OUTPUT" 2>/dev/null || true

echo "Running Task 3 MapReduce job..."
hadoop jar "$JAR" "$DRIVER" "$INPUT" "$OUTPUT" "$LOOKUP"

echo "Job finished."
echo "Output:"
hdfs dfs -cat "$OUTPUT/part-r-00000"