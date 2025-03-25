import commands.CommandHandler;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Personal Calendar CLI");
        Scanner scanner = new Scanner(System.in);
        CommandHandler commandHandler = new CommandHandler();
        
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting...");
                break;
            }
            commandHandler.executeCommand(input);
        }
        scanner.close();
    }
}