import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Task3Driver {

    public static void main(String[] args) throws Exception {

        if (args.length != 3) {
            System.err.println("Usage: Task3Driver <input_path> <output_path> <lookup_file_path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        conf.set("task3.lookup.path", args[2]);

        Job job = Job.getInstance(conf, "Task 3 - Simple URL Categorization With Combiner");

        job.setJarByClass(Task3Driver.class);

        job.setMapperClass(Task3Mapper.class);
        job.setCombinerClass(Task3Combiner.class);
        job.setReducerClass(Task3Reducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setNumReduceTasks(1);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}