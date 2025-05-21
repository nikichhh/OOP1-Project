package cli;

import commands.CommandHandler;
import java.util.Scanner;

public class CLI {
    private final Scanner scanner;
    private final CommandHandler commandHandler;

    public CLI() {
        this.scanner = new Scanner(System.in);
        this.commandHandler = new CommandHandler();
    }

    public void start() {
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

