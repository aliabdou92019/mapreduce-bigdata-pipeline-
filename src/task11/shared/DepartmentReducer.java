import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class DepartmentReducer extends Reducer<Text, DepartmentWritable, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<DepartmentWritable> values, Context context)
            throws IOException, InterruptedException {

        int totalSalary = 0;
        int totalEmployees = 0;

        for (DepartmentWritable value : values) {
            totalSalary += value.getSalary();
            totalEmployees += value.getEmployees();
        }

        int averageSalary = 0;
        if (totalEmployees > 0) {
            averageSalary = totalSalary / totalEmployees;
        }

        String result = "Total: " + totalSalary +
                        "\tAvg: " + averageSalary +
                        "\tEmployees: " + totalEmployees;

        context.write(key, new Text(result));
    }
}  