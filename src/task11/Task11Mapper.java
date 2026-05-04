import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class Task11Mapper extends Mapper<LongWritable, Text, Text, DepartmentWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();
        String[] fields = line.split("\\|");

        if (fields.length >= 4) {
            String department = fields[1].trim();
            String salaryText = fields[3].trim();

            if (department.length() == 0 || salaryText.length() == 0) {
                return;
            }

            try {
                int salary = Integer.parseInt(salaryText);

                if (salary > 0) {
                    context.write(new Text(department), new DepartmentWritable(salary, 1));
                }
            } catch (NumberFormatException e) {
                return;
            }
        }
    }
}