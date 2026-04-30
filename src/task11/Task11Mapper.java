import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class task11mapper extends Mapper<LongWritable, Text, Text, departmentWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();
        String[] fields = line.split(",");

        if (fields.length >= 3) {
            String department = fields[1].trim();
            String salaryText = fields[2].trim();

            if (department.length() == 0 || salaryText.length() == 0) {
                return;
            }

            try {
                int salary = Integer.parseInt(salaryText);

                if (salary > 0) {
                    context.write(new Text(department), new departmentWritable(salary, 1));
                }
            } catch (NumberFormatException e) {
                return;
            }
        }
    }
}