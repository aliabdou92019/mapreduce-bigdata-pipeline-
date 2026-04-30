// I don't care about the package name, you can change it as you like!!
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.WritableComparable;

public class departmentWritable implements WritableComparable<departmentWritable> {

    private int salary;
    private int employees;

    public departmentWritable() {}

    public departmentWritable(int salary, int employees) {
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
    public int compareTo(departmentWritable other) {
        int result = this.salary - other.salary;
        if (result != 0) return result;
        return this.employees - other.employees;
    }
}