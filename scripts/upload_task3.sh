#!/bin/bash

set -e

BASE="/user/cloudera/Root/big-data-project"
LOCAL_DATA="data/samples"

echo "Uploading Task 3 Data to HDFS..."
hdfs dfs -put -f "$LOCAL_DATA/task3_sample.txt" "$BASE/task3/input/sample/"
hdfs dfs -put -f "$LOCAL_DATA/task3_lookup.txt" "$BASE/task3/lookup/sample/"

echo "Task 3 Upload Complete."
