import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Task3Reducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        long totalCount = 0;
        long totalResponseTime = 0;
        long totalErrors = 0;

        for (Text value : values) {
            String[] parts = value.toString().split("\\|");

            if (parts.length < 3) {
                continue;
            }

            try {
                totalCount += Long.parseLong(parts[0]);
                totalResponseTime += Long.parseLong(parts[1]);
                totalErrors += Long.parseLong(parts[2]);
            } catch (Exception e) {
                continue;
            }
        }

        double avgResponseTime = 0.0;

        if (totalCount > 0) {
            avgResponseTime = (double) totalResponseTime / totalCount;
        }

        context.write(
                key,
                new Text(totalCount + "\t" + avgResponseTime + "\t" + totalErrors));
    }
}