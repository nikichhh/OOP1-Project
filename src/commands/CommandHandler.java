package commands;

import filehandler.FileHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHandler implements CommandExecutor {
    private final Map<CommandType, Command> commands;
    private final Map<String, List<String>> events;
    private FileHandler fileHandler;

    // Refactored command initialization into a separate method
    private void initializeCommands() {
        commands.put(CommandType.OPEN, new OpenCommand(this, events));
        commands.put(CommandType.CLOSE, new CloseCommand(fileHandler, events));
        commands.put(CommandType.SAVE, new SaveCommand(fileHandler, events));
        commands.put(CommandType.SAVEAS, new SaveAsCommand(fileHandler, events));
        commands.put(CommandType.HELP, new HelpCommand());
        commands.put(CommandType.BOOK, new BookCommand(fileHandler, events));
        commands.put(CommandType.UNBOOK, new UnbookCommand(fileHandler, events));
        commands.put(CommandType.AGENDA, new AgendaCommand(fileHandler, events));
        commands.put(CommandType.CHANGE, new ChangeCommand(fileHandler, events));
        commands.put(CommandType.FIND, new FindCommand(events));
        commands.put(CommandType.HOLIDAY, new HolidayCommand(fileHandler, events));
        commands.put(CommandType.BUSYDAYS, new BusydaysCommand(events));
        commands.put(CommandType.FINDSLOT, new FindslotCommand(fileHandler, events));
        commands.put(CommandType.FINDSLOTWITH, new FindslotwithCommand(events));
        commands.put(CommandType.MERGE, new MergeCommand(fileHandler, events));
    }

    public CommandHandler() {
        this.events = new HashMap<>();
        this.fileHandler = new FileHandler("data/calendar.txt");
        this.commands = new HashMap<>();
        initializeCommands();
    }

    @Override
    public void executeCommand(String input) {
        String[] parts = input.split(" ", 2);
        try {
            CommandType commandType = CommandType.valueOf(parts[0].toUpperCase());
            Command cmd = commands.get(commandType);
            if (cmd != null) {
                cmd.execute(parts);
            } else {
                System.out.println("Unknown command: " + parts[0]);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown command: " + parts[0]);
        }
    }

    public void setFileHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        initializeCommands();
    }
}

