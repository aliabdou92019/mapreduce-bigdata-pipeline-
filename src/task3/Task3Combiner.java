import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Task3Combiner extends Reducer<Text, CategoryStatsWritable, Text, CategoryStatsWritable> {

    private final CategoryStatsWritable outputValue = new CategoryStatsWritable();

    @Override
    protected void reduce(Text key, Iterable<CategoryStatsWritable> values, Context context)
            throws IOException, InterruptedException {

        long totalRequestCount = 0L;
        long totalResponseTimeSum = 0L;
        long totalErrorCount = 0L;

        for (CategoryStatsWritable value : values) {
            totalRequestCount += value.getRequestCount();
            totalResponseTimeSum += value.getResponseTimeSum();
            totalErrorCount += value.getErrorCount();
        }

        outputValue.set(totalRequestCount, totalResponseTimeSum, totalErrorCount);
        context.write(key, outputValue);
    }
}