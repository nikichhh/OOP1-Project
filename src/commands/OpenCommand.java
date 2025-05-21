package commands;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import filehandler.FileHandler;

public class OpenCommand implements Command {
    private final CommandHandler commandHandler;
    private Map<String, List<String>> events;

    public OpenCommand(CommandHandler commandHandler, Map<String, List<String>> events) {
        this.commandHandler = commandHandler;
        this.events = events;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: open <file>");
            return;
        }
        String filePath = args[1];
        FileHandler newFileHandler = new FileHandler(filePath);
        commandHandler.setFileHandler(newFileHandler);
        if (events != null) {
            events.clear();
            for (String event : newFileHandler.load()) {
                String[] parts = event.split(" ", 6);
                String date = parts[0];
                events.computeIfAbsent(date, k -> new ArrayList<>()).add(event);
            }
        }
        System.out.println("Opened file: " + filePath);
    }
}

