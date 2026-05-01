import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Task3Mapper extends Mapper<LongWritable, Text, Text, Text> {

    private HashMap<String, String> lookup = new HashMap<String, String>();

    @Override
    protected void setup(Context context) throws IOException {

        Configuration conf = context.getConfiguration();
        String lookupPath = conf.get("task3.lookup.path");

        FileSystem fs = FileSystem.get(conf);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(fs.open(new Path(lookupPath))));

        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");

            if (parts.length < 2) {
                continue;
            }

            String pattern = parts[0].trim();
            String category = parts[1].trim();

            if (pattern.equalsIgnoreCase("urlPattern")) {
                continue;
            }

            lookup.put(pattern, category);
        }

        br.close();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();
        String[] parts = line.split(",");

        /*
         * Expected input:
         * ip,timestamp,method,url,status,size,ResponseTime
         */
        if (parts.length < 7) {
            return;
        }

        if (parts[0].equalsIgnoreCase("ip")) {
            return;
        }

        String url = parts[3].trim();
        String statusText = parts[4].trim();
        String responseTimeText = parts[6].trim();

        try {
            int statusCode = Integer.parseInt(statusText);
            long responseTime = Long.parseLong(responseTimeText);

            String category = "OTHER";

            for (String pattern : lookup.keySet()) {
                if (url.startsWith(pattern)) {
                    category = lookup.get(pattern);
                    break;
                }
            }

            int errorFlag = 0;
            if (statusCode >= 400) {
                errorFlag = 1;
            }

            /*
             * value format:
             * requestCount|responseTimeSum|errorCount
             */
            context.write(
                    new Text(category),
                    new Text("1|" + responseTime + "|" + errorFlag));

        } catch (Exception e) {
            return;
        }
    }
    
    private String[] parseCsvLine(String line) {
        java.util.ArrayList<String> result = new java.util.ArrayList<String>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;
    
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
    
            if (ch == '"') {
                insideQuotes = !insideQuotes;
            } else if (ch == ',' && !insideQuotes) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }
    
        result.add(current.toString());
    
        return result.toArray(new String[result.size()]);
    }
}