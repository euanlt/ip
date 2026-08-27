public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void markAsDone(){
        if(this.isDone){
            System.out.println("Task already marked as done\n");
        } else {
            this.isDone = true;
            System.out.println("Nice! I've marked this task as done:\n"+this.toString()+"\n");
        }
    }

    public void markAsNotDone(){
        if(!this.isDone){
            System.out.println("Task already marked as not done\n");
        } else {
            this.isDone = false;
            System.out.println("OK, I've marked this task as not done yet:\n"+this.toString()+"\n");
        }
    }

    public String getDescription(){
        return this.description;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public TaskType getType() {
        return this.type;
    }


    public String toFileFormat() {
        return type.getSymbol() + " | " + (isDone ? "1" : "0") + " | " + description;
    }

    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] " + this.description;
    }
}