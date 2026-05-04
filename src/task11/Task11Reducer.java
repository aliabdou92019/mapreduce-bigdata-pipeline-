import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import java.io.IOException;

public class Task11Reducer extends Reducer<Text, DepartmentWritable, Text, Text> {

    private MultipleOutputs<Text, Text> mos;

    @Override
    public void setup(Context context) {
        mos = new MultipleOutputs<>(context);
    }

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

        // Use the department name as the base filename (sanitizing special characters just in case)
        String basePath = key.toString().replaceAll("[^a-zA-Z0-9]", "_");
        mos.write(key, new Text(result), basePath);
    }

    @Override
    public void cleanup(Context context) throws IOException, InterruptedException {
        mos.close();
    }
}