import java.io.IOException;
import java.util.Locale;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Task3Reducer extends Reducer<Text, CategoryStatsWritable, Text, Text> {

    private final Text outputValue = new Text();

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

        double averageResponseTime = 0.0;

        if (totalRequestCount > 0) {
            averageResponseTime = (double) totalResponseTimeSum / totalRequestCount;
        }

        /*
         * Final value format:
         * requestCount averageResponseTime errorCount
         */
        String result = String.format(
                Locale.US,
                "%d\t%.2f\t%d",
                totalRequestCount,
                averageResponseTime,
                totalErrorCount);

        outputValue.set(result);
        context.write(key, outputValue);
    }
}