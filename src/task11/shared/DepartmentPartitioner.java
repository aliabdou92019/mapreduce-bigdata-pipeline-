import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class DepartmentPartitioner extends Partitioner<Text, DepartmentWritable> {

    @Override
    public int getPartition(Text key, DepartmentWritable value, int numPartitions) {

   
        String department = key.toString().toUpperCase();

      
        int hash = department.hashCode();

        hash = Math.abs(hash);

        return hash % numPartitions;
    }
}