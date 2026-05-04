#!/bin/bash

set -e

BASE="/user/cloudera/Root/big-data-project"
LOCAL_DATA="data/samples"

echo "Uploading Task 11 Data to HDFS..."
hdfs dfs -put -f "$LOCAL_DATA/task11_sample.txt" "$BASE/task11/input/sample/"

echo "Task 11 Upload Complete."
