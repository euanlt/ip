import java.util.Objects;
import java.util.Scanner;

public class Prism {
    public static void main(String[] args) {
        String[] list= new String[100];
        int index=0;
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
                for(int i=0; i<index; i++){
                    System.out.println((i+1)+". "+list[i]);
                }

            }
            else {
                list[index++] = input;
                System.out.println(
                        "added: "
                        + input + "\n"
                );
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
