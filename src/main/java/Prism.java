import java.util.Objects;
import java.util.Scanner;

public class Prism {
    public static void main(String[] args) {
        Task[] list= new Task[100];
        int index=0;
        int taskNum;
        String banner =
                "____________________________________________________________\n"
                +" ____       _               \n"
                + "|  _ \\ _ __(_)___ _ __ ___  \n"
                + "| |_) | '__| / __| '_ ` _ \\ \n"
                + "|  __/| |  | \\__ \\ | | | | |\n"
                + "|_|   |_|  |_|___/_| |_| |_|\n"
                +"Hello! I'm Prism.\n"
                +"What can I do for you?\n"
                +"____________________________________________________________\n";
        System.out.println(banner);
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        while(!Objects.equals(input, "bye")){
            System.out.println( "____________________________________________________________\n");
            if(Objects.equals(input,"list")){
                System.out.println("Here are the tasks in your list:\n");
                for(int i=0; i<index; i++){
                    System.out.println((i+1)+"."+list[i].toString());
                }
            } else if (input.matches("mark \\d+")) {
                taskNum = Integer.parseInt(input.substring(5));
                if(taskNum>0 && taskNum<index+1){
                    list[taskNum-1].markAsDone();
                } else{
                    System.out.println("Invalid task number");
                }

            } else if (input.matches("unmark \\d+")) {
               taskNum = Integer.parseInt(input.substring(7));
                if(taskNum>0 && taskNum<index+1){
                    list[taskNum-1].markAsNotDone();
                } else{
                    System.out.println("Invalid task number");
                }

            }else {
                if(input.matches("^todo\\s+.+$")){
                    list[index++] = new Todo(input.substring(5));

                    System.out.println(
                        "Got it. I've added this task:\n"
                        +list[index-1].toString()+"\n"
                        +"Now you have "+ index + " tasks in the list."
                    );

                } else if (input.matches("^deadline\\s+.+\\s+/by\\s+\\w+$")) {
                    String[] parts = input.substring(9).split(" /by ");
                    list[index++] = new Deadline(parts[0], parts[1]);

                    System.out.println(
                            "Got it. I've added this task:\n"
                            +list[index-1].toString()+"\n"
                            +"Now you have "+ index + " tasks in the list."
                    );
                } else if (input.matches("^event\\s+.+\\s+/from\\s+.+\\s+/to\\s+.+$")) {
                    String[] parts = input.substring(6).split(" /from | /to ");
                    list[index++] = new Event(parts[0],parts[1],parts[2]);
                    System.out.println(
                            "Got it. I've added this task:\n"
                            +list[index-1].toString()+"\n"
                            +"Now you have "+ index + " tasks in the list."
                    );
                } else{
                    System.out.println("Invalid request");
                }


            }
            System.out.println( "____________________________________________________________\n");
            input = sc.nextLine();
        }
        System.out.println(
                "____________________________________________________________\n"
                +"Bye. Hope to see you again soon!\n"
                +"____________________________________________________________\n"
        );

    }
}
