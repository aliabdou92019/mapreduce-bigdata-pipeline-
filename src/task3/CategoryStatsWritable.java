import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class CategoryStatsWritable implements Writable {

    private long requestCount;
    private long responseTimeSum;
    private long errorCount;

    // Hadoop requires an empty constructor
    public CategoryStatsWritable() {
        this.requestCount = 0L;
        this.responseTimeSum = 0L;
        this.errorCount = 0L;
    }

    public CategoryStatsWritable(long requestCount, long responseTimeSum, long errorCount) {
        this.requestCount = requestCount;
        this.responseTimeSum = responseTimeSum;
        this.errorCount = errorCount;
    }

    public long getRequestCount() {
        return requestCount;
    }

    public long getResponseTimeSum() {
        return responseTimeSum;
    }

    public long getErrorCount() {
        return errorCount;
    }

    public void set(long requestCount, long responseTimeSum, long errorCount) {
        this.requestCount = requestCount;
        this.responseTimeSum = responseTimeSum;
        this.errorCount = errorCount;
    }

    public void add(CategoryStatsWritable other) {
        this.requestCount += other.requestCount;
        this.responseTimeSum += other.responseTimeSum;
        this.errorCount += other.errorCount;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(requestCount);
        out.writeLong(responseTimeSum);
        out.writeLong(errorCount);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        requestCount = in.readLong();
        responseTimeSum = in.readLong();
        errorCount = in.readLong();
    }

    @Override
    public String toString() {
        return requestCount + "\t" + responseTimeSum + "\t" + errorCount;
    }
}