import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class Task11Partitioner extends Partitioner<Text, DepartmentWritable> {

    @Override
    public int getPartition(Text key, DepartmentWritable value, int numPartitions) {

   
        String department = key.toString().toUpperCase();

      
        int hash = department.hashCode();

        return (hash & Integer.MAX_VALUE) % numPartitions;
    }
}