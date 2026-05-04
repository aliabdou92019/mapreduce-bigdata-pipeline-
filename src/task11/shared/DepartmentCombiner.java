import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class DepartmentCombiner extends Reducer<Text, DepartmentWritable, Text, DepartmentWritable> {

    @Override
    protected void reduce(Text key, Iterable<DepartmentWritable> values, Context context)
            throws IOException, InterruptedException {

        int totalSalary = 0;
        int totalEmployees = 0;

        for (DepartmentWritable value : values) {
            totalSalary += value.getSalary();
            totalEmployees += value.getEmployees();
        }

        context.write(key, new DepartmentWritable(totalSalary, totalEmployees));
    }
}