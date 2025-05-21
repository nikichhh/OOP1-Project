package commands;

import filehandler.FileHandler;
import java.util.*;

public class SaveAsCommand implements Command {
    private final FileHandler fileHandler;
    private final Map<String, List<String>> events;

    public SaveAsCommand(FileHandler fileHandler, Map<String, List<String>> events) {
        this.fileHandler = fileHandler;
        this.events = events;
    }

    private void loadEvents() {
        List<String> allEvents = fileHandler.load();
        for (String event : allEvents) {
            String[] parts = event.split(" ", 6);
            String date = parts[0];
            List<String> dateEvents = events.get(date);
            if (dateEvents == null) {
                dateEvents = new ArrayList<>();
                events.put(date, dateEvents);
            }
            dateEvents.add(event);
        }
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: saveas <file>");
            return;
        }
        String filePath = args[1];
        FileHandler newFileHandler = new FileHandler(filePath);
        List<String> allEvents = new ArrayList<>();
        for (List<String> eventList : events.values()) {
            allEvents.addAll(eventList);
        }
        newFileHandler.save(allEvents);
        System.out.println("Saved changes to: " + filePath);
    }
}
