package commands;

import filehandler.FileHandler;

public class CommandHandler implements CommandExecutor {
    private BaseCommands baseCommands;
    private ExtraCommands extraCommands;

    public CommandHandler() {
        this.baseCommands = new BaseCommands();
        this.extraCommands = new ExtraCommands(new FileHandler("data/calendar.txt"));
    }

    @Override
    public void executeCommand(String input) {
        String[] parts = input.split(" ", 6);
        String command = parts[0];
        switch (command.toLowerCase()) {
            case "open":
                baseCommands.open(parts[1]);
                break;
            case "close":
                baseCommands.close();
                break;
            case "save":
                baseCommands.save();
                break;
            case "saveas":
                baseCommands.saveAs(parts[1]);
                break;
            case "add":
                baseCommands.addEvent(parts[1]);
                break;
            case "show":
                baseCommands.showEvents();
                break;
            case "help":
                baseCommands.help();
                break;
            case "exit":
                System.out.println("Exiting...");
                System.exit(0);
            case "book":
                extraCommands.book(parts[1], parts[2], parts[3], parts[4], parts[5]);
                break;
            case "unbook":
                extraCommands.unbook(parts[1], parts[2], parts[3]);
                break;
            case "agenda":
                extraCommands.agenda(parts[1]);
                break;
            case "change":
                extraCommands.change(parts[1], parts[2], parts[3], parts[4]);
                break;
            case "find":
                extraCommands.find(parts[1]);
                break;
            case "holiday":
                extraCommands.holiday(parts[1]);
                break;
            case "busydays":
                extraCommands.busydays(parts[1], parts[2]);
                break;
            case "findslot":
                extraCommands.findslot(parts[1], Integer.parseInt(parts[2]));
                break;
            case "findslotwith":
                extraCommands.findslotwith(parts[1], Integer.parseInt(parts[2]), parts[3]);
                break;
            case "merge":
                extraCommands.merge(parts[1]);
                break;
            default:
                System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }
}