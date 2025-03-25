package commands;

import filehandler.FileHandler;

public class CommandHandler implements CommandExecutor {
    private BaseCommands baseCommands;
    private ExtraCommands extraCommands;

    public CommandHandler() {
        FileHandler fileHandler = new FileHandler("data/calendar.txt");
        this.baseCommands = new BaseCommands(fileHandler);
        this.extraCommands = new ExtraCommands();
    }

    @Override
    public void executeCommand(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";

        switch (command.toLowerCase()) {
            case "add":
                baseCommands.addEvent(argument);
                break;
            case "show":
                baseCommands.showEvents();
                break;
            case "help":
                extraCommands.help();
                break;
            default:
                System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }
}