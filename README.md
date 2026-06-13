<p align="center">
  <img src="https://img.shields.io/badge/Hadoop-MapReduce-orange?style=for-the-badge&logo=apachehadoop&logoColor=white" />
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/HDFS-Distributed%20Storage-blue?style=for-the-badge&logo=apache&logoColor=white" />
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" />
</p>

<h1 align="center">🐘 SoBigData — Big Data MapReduce Project</h1>

<p align="center">
  <b>Scalable log analytics and salary intelligence powered by Hadoop MapReduce</b><br/>
  <sub>Designed to process datasets exceeding <b>1 GB</b> using advanced MapReduce optimizations</sub>
</p>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Tasks](#-tasks)
  - [Task 3 — URL Categorization](#-task-3--url-categorization)
  - [Task 11 — Department Salary Analysis](#-task-11--department-salary-analysis)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Setup HDFS](#1-setup-hdfs-directory-structure)
  - [Upload Data](#2-upload-data-to-hdfs)
  - [Run Jobs](#3-run-mapreduce-jobs)
- [Combiner Performance Analysis](#-combiner-performance-analysis)
- [Tech Stack](#-tech-stack)
- [Credits](#-credits)
- [License](#-license)

---

## 🎯 Overview

**SoBigData** is a Big Data analytics project built for the **Big Data** course at **Helwan National University**. It demonstrates real-world, production-grade MapReduce pipelines that process and analyze massive datasets — web server access logs and federal employee salary records — on a Hadoop cluster.

> _This is a team project. Refer to [Credits](#-credits) for all contributors._

### Key Highlights

| Feature | Description |
|---|---|
| **Scalable Processing** | Engineered to handle datasets exceeding **1 GB** on multi-node Hadoop clusters |
| **Combiner Optimization** | Custom Combiner classes reduce network shuffle by pre-aggregating data locally |
| **Map-Side Join** | In-memory lookup table loaded during `setup()` for zero-shuffle URL classification |
| **Custom Serializables** | `WritableComparable` implementation for type-safe network serialization |
| **Custom Partitioner** | Hash-based partitioning with negative-index protection for uniform reducer load |
| **Robust Parsing** | Quote-aware CSV parser and multi-layer validation to handle real-world dirty data |
| **Automated Scripts** | End-to-end shell scripts for HDFS setup, data upload, compilation, and execution |

---

## 🏗 Architecture

```
                    ┌─────────────────────────────────────────────────────────┐
                    │                   HADOOP CLUSTER                       │
                    │                                                         │
  ┌──────────┐     │   ┌──────────┐    ┌──────────┐    ┌──────────────────┐  │
  │ Raw Data │────▶│   │  MAPPER   │───▶│ COMBINER │───▶│ SHUFFLE & SORT   │  │
  │  (HDFS)  │     │   │ (Parse &  │    │ (Local   │    │ (Network Phase)  │  │
  └──────────┘     │   │  Validate)│    │  Agg.)   │    └────────┬─────────┘  │
                    │   └──────────┘    └──────────┘             │            │
  ┌──────────┐     │                                             ▼            │
  │  Lookup  │     │                                    ┌──────────────────┐  │
  │  Table   │─ ─ ─│─ ─ Map-Side Join (setup())        │    REDUCER       │  │
  │  (HDFS)  │     │                                    │ (Final Agg. &    │  │
  └──────────┘     │                                    │  Avg. Compute)   │  │
                    │                                    └────────┬─────────┘  │
                    │                                             │            │
                    │                                             ▼            │
                    │                                    ┌──────────────────┐  │
                    │                                    │   OUTPUT (HDFS)  │  │
                    │                                    └──────────────────┘  │
                    └─────────────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
sobigdata/
├── src/
│   ├── task3/                          # URL Categorization
│   │   ├── Task3Driver.java            # Job configuration & execution
│   │   ├── Task3Mapper.java            # CSV parsing, URL classification via Map-Side Join
│   │   ├── Task3Combiner.java          # Local pre-aggregation of counts & times
│   │   └── Task3Reducer.java           # Final aggregation & average computation
│   │
│   └── task11/                         # Department Salary Analysis
│       ├── Task11Driver.java           # Job config with 132 reducers & LazyOutputFormat
│       ├── Task11Mapper.java           # Pipe-delimited parsing & salary extraction
│       ├── Task11Combiner.java         # Local salary & employee count aggregation
│       ├── Task11Reducer.java          # Final stats with MultipleOutputs per department
│       ├── Task11Partitioner.java      # Hash-based partitioner with overflow protection
│       └── DepartmentWritable.java     # Custom WritableComparable for paired data
│
├── data/
│   └── samples/                        # Sample datasets for testing
│       ├── task3_sample.txt            # Sample web server access logs
│       ├── task3_lookup.txt            # URL pattern → category mapping
│       ├── task3_expected_output.txt   # Expected output for validation
│       ├── task11_sample.txt           # Sample employee salary records
│       └── task11_expected_output.txt  # Expected output for validation
│
├── output/
│   ├── task3/                          # Task 3 results & performance screenshots
│   └── task11/                         # Task 11 results & performance screenshots
│
├── scripts/
│   ├── config.sh                       # Shared configuration (HDFS paths)
│   ├── Build_HDFS_Structure.sh         # Creates complete HDFS directory tree
│   ├── upload_task3.sh                 # Uploads Task 3 data to HDFS
│   ├── upload_task11.sh                # Uploads Task 11 data to HDFS
│   ├── run_task3_withCombiner.sh       # Runs Task 3 (full data, with Combiner)
│   ├── run_task3_without_Combiner.sh   # Runs Task 3 (full data, without Combiner)
│   ├── run_task3_sample_withCombiner.sh    # Runs Task 3 (sample, with Combiner)
│   ├── run_task3_sample_without_Combiner.sh # Runs Task 3 (sample, without Combiner)
│   └── run_task11.sh                   # Runs Task 11 (full data)
│
├── .gitignore
└── README.md
```

---

## 🔬 Tasks

### 📊 Task 3 — URL Categorization

> **Objective:** Analyze web server access logs to categorize HTTP traffic by URL pattern, counting total requests, computing average response times, and tracking error rates per category.

#### Data Flow

```
Raw CSV Logs ──▶ Mapper (Parse + Classify) ──▶ Combiner (Local Sum) ──▶ Reducer (Global Avg)
                        ▲
                        │
              Lookup Table (Map-Side Join)
```

#### Input Format

```
ip, timestamp, method, url, status, size, ResponseTimeMs
54.36.149.41, 22/Jan/2019:03:56:14 +0330, GET, /product/31893/..., 200, 41483, 889
```

#### Output Format

```
Category    TotalRequests    AvgResponseTime    ErrorCount
PRODUCT     2                580.00             0
STATIC      7                565.29             0
```

#### Implementation Details

| Component | Key Details |
|---|---|
| **Mapper** | Loads a URL-to-category lookup table into a `HashMap` during `setup()` (Map-Side Join). Uses a custom `parseCsvLine()` to safely handle quoted fields with internal commas. Validates array bounds (`parts.length < 7`) and skips headers. Emits `<category, "1\|responseTime\|errorFlag">`. |
| **Combiner** | Pre-aggregates `requestCount`, `responseTimeSum`, and `errorCount` locally on each map node — reducing shuffle volume. |
| **Reducer** | Performs final global aggregation and computes `avgResponseTime = totalResponseTime / totalRequests`. |
| **Driver** | Passes the lookup file path via `conf.set("task3.lookup.path", ...)` so every Mapper can cache it during initialization. |

---

### 💰 Task 11 — Department Salary Analysis

> **Objective:** Process federal employee salary records to compute total salaries, average salaries, and employee counts grouped by department — outputting each department to its own file.

#### Data Flow

```
Pipe-delimited Records ──▶ Mapper ──▶ Combiner ──▶ Partitioner ──▶ Reducer (MultipleOutputs)
                                                        │
                                          132 Reducers (load-balanced)
```

#### Input Format

```
age_bracket | agency                     | agency_code | annualized_salary | count
25-29       | DEPARTMENT OF AGRICULTURE  | AG          | 64625             | 1
```

#### Output Format (one file per department)

```
DEPARTMENT OF AGRICULTURE    Total: 529541    Avg: 52954    Employees: 10
```

#### Implementation Details

| Component | Key Details |
|---|---|
| **`DepartmentWritable`** | Custom `WritableComparable<DepartmentWritable>` that serializes `(salary, employees)` pairs over the network. Implements `compareTo` (sort by salary, then employees) for Hadoop's Shuffle & Sort phase. |
| **Mapper** | Parses pipe-delimited (`\|`) records, validates non-empty fields, skips rows where `salary <= 0` or is `REDACTED`. Emits `<department, DepartmentWritable(salary, 1)>`. |
| **Combiner** | Locally sums `totalSalary` and `totalEmployees` per department before the shuffle, reducing network I/O. |
| **Partitioner** | Uses `(hash & Integer.MAX_VALUE) % numPartitions` to prevent negative hash index errors and distribute departments uniformly across **132 reducers**. |
| **Reducer** | Computes final `totalSalary`, `avgSalary`, and `employeeCount`. Uses `MultipleOutputs` to write each department to a separate output file, named by a sanitized department key. |
| **Driver** | Configures `LazyOutputFormat` to suppress empty output files from unused reducers. |

#### Data Preprocessing

The dataset was synthesized by merging **12 months of raw federal payroll data** into a single massive file to simulate real-world Big Data volumes. A preprocessing phase filtered the data to retain only the critical columns needed for analysis — reducing noise, shrinking the file size, and improving I/O throughput.

---

## 🚀 Getting Started

### Prerequisites

| Requirement | Details |
|---|---|
| **Hadoop** | 2.x or 3.x with HDFS and YARN running |
| **Java** | JDK 8+ |
| **Cluster** | Cloudera CDH or equivalent Hadoop distribution |

### 1. Clone the Repository

```bash
git clone https://github.com/aliabdou92019/sobigdata.git
cd sobigdata
```

### 2. Setup HDFS Directory Structure

```bash
chmod +x scripts/*.sh
./scripts/Build_HDFS_Structure.sh
```

This creates the full HDFS directory tree:

```
/user/cloudera/Root/big-data-project/
├── task3/
│   ├── input/   (sample/ & real/)
│   ├── lookup/  (sample/ & real/)
│   └── output/  (sample/ & real/)
└── task11/
    ├── input/   (sample/ & real/)
    └── output/  (sample/ & real/)
```

### 3. Upload Data to HDFS

```bash
# Upload Task 3 sample data & lookup table
./scripts/upload_task3.sh

# Upload Task 11 sample data
./scripts/upload_task11.sh
```

> **Note:** For full-scale runs, upload your real datasets to the `real/` subdirectories manually:
> ```bash
> hdfs dfs -put your_full_logs.txt /user/cloudera/Root/big-data-project/task3/input/real/
> hdfs dfs -put your_salary_data.txt /user/cloudera/Root/big-data-project/task11/input/real/
> ```

### 4. Compile & Package JARs

Compile the source files and package them into executable JARs:

```bash
# Task 3
javac -classpath $(hadoop classpath) -d task3_classes src/task3/*.java
jar -cvf Task3_with_Combiner.jar -C task3_classes/ .
cp Task3_with_Combiner.jar /home/cloudera/

# Task 11
javac -classpath $(hadoop classpath) -d task11_classes src/task11/*.java
jar -cvf Task11.jar -C task11_classes/ .
cp Task11.jar /home/cloudera/
```

### 5. Run MapReduce Jobs

```bash
# Task 3 — URL Categorization (with Combiner)
./scripts/run_task3_withCombiner.sh

# Task 3 — URL Categorization (without Combiner, for benchmarking)
./scripts/run_task3_without_Combiner.sh

# Task 11 — Department Salary Analysis
./scripts/run_task11.sh
```

### 6. View Results

```bash
# Task 3 output
hdfs dfs -cat /user/cloudera/Root/big-data-project/task3/output/real/task3_full_result_with_combiner/part-r-00000

# Task 11 output (one file per department)
hdfs dfs -ls /user/cloudera/Root/big-data-project/task11/output/real/task11_full_result/
```

---

## ⚡ Combiner Performance Analysis

A key optimization explored in this project is the use of **Combiner classes** to perform local aggregation before the shuffle phase, reducing network I/O.

### Task 3 — URL Categorization

<table>
<tr>
<th>✅ With Combiner</th>
<th>❌ Without Combiner</th>
</tr>
<tr>
<td><img src="output/task3/Full_with_combiner.jpeg" width="400"/></td>
<td><img src="output/task3/Full_without_combiner.jpeg" width="400"/></td>
</tr>
<tr>
<td><b>CPU Time: 162,340 ms (~2.7 min)</b></td>
<td><b>CPU Time: 209,300 ms (~3.5 min)</b></td>
</tr>
</table>

> **Result:** The Combiner reduced CPU time by **~22%**, demonstrating significant performance gains from local pre-aggregation.

### Task 11 — Department Salary Analysis

<table>
<tr>
<th>✅ With Combiner</th>
<th>❌ Without Combiner</th>
</tr>
<tr>
<td><img src="output/task11/Full_with_combiner.jpeg" width="400"/></td>
<td><img src="output/task11/Full_without_combiner.jpeg" width="400"/></td>
</tr>
<tr>
<td><b>CPU Time: 199,730 ms (~3.3 min)</b></td>
<td><b>CPU Time: 243,100 ms (~4.0 min)</b></td>
</tr>
</table>

> **Result:** The Combiner reduced CPU time by **~18%**, confirming that local aggregation of salary and employee counts effectively cuts down shuffle overhead.

### Conclusion

In **both tasks**, the Combiner optimization resulted in measurably faster execution times. The performance gains stem from reducing the volume of intermediate key-value pairs that must be serialized, sorted, and transferred across the network during the shuffle phase.

---

## 🛠 Tech Stack

<table>
<tr>
<td align="center"><b>Technology</b></td>
<td align="center"><b>Purpose</b></td>
</tr>
<tr>
<td>🐘 <b>Apache Hadoop</b></td>
<td>Distributed computing framework (MapReduce + HDFS)</td>
</tr>
<tr>
<td>☕ <b>Java</b></td>
<td>MapReduce implementation language</td>
</tr>
<tr>
<td>📦 <b>HDFS</b></td>
<td>Distributed file storage for input/output datasets</td>
</tr>
<tr>
<td>🐧 <b>Bash</b></td>
<td>Automation scripts for build, upload, and execution</td>
</tr>
<tr>
<td>☁️ <b>Cloudera CDH</b></td>
<td>Hadoop cluster distribution & management</td>
</tr>
</table>

---

## 👥 Credits

| Contributor | LinkedIn |
|---|---|
| **Ali Abdou** | [![LinkedIn](https://img.shields.io/badge/-Profile-blue?style=flat&logo=linkedin)](https://www.linkedin.com/in/ali-abdouu/) |
| **Amira Azzam** | [![LinkedIn](https://img.shields.io/badge/-Profile-blue?style=flat&logo=linkedin)](https://www.linkedin.com/in/amira-azzam2510/) |
| **Amr Yasser** | [![LinkedIn](https://img.shields.io/badge/-Profile-blue?style=flat&logo=linkedin)](https://www.linkedin.com/in/amryb/) |
| **Maria Gerges** | [![LinkedIn](https://img.shields.io/badge/-Profile-blue?style=flat&logo=linkedin)](https://www.linkedin.com/in/maria-gerges-81b04a30a/) |
| **Yousef Medhat** | [![LinkedIn](https://img.shields.io/badge/-Profile-blue?style=flat&logo=linkedin)](https://www.linkedin.com/in/yousef-medhat-7293232a1/) |
| **Yousef Waheed** | [![LinkedIn](https://img.shields.io/badge/-Profile-blue?style=flat&logo=linkedin)](https://www.linkedin.com/in/youssef-waheed-8462061a7/) |

---

## 📄 License

This project is open source and available under the [MIT License](https://mit-license.org/).

---

<p align="center">
  <sub>Built with 🐘 Hadoop MapReduce at <b>Helwan National University</b></sub>
</p>
