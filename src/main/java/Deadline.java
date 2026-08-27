public class Deadline extends Task{
    protected String by;

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    public String getBy() {
        return this.by;
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() + " | " + by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}