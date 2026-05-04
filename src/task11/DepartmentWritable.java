import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.WritableComparable;

public class DepartmentWritable implements WritableComparable<DepartmentWritable> {

    private int salary;
    private int employees;

    public DepartmentWritable() {}

    public DepartmentWritable(int salary, int employees) {
        this.salary = salary;
        this.employees = employees;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.salary);
        out.writeInt(this.employees);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.salary = in.readInt();
        this.employees = in.readInt();
    }

    public int getSalary() {
        return salary;
    }

    public int getEmployees() {
        return employees;
    }

    @Override
    public String toString() {
        return salary + "," + employees;
    }

    @Override
    public int compareTo(DepartmentWritable other) {
        int result = Integer.compare(this.salary, other.salary);
        if (result != 0) return result;
        return Integer.compare(this.employees, other.employees);
    }
}