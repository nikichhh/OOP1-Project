package commands;

import filehandler.FileHandler;
import java.util.*;

public class SaveCommand implements Command {
    private final FileHandler fileHandler;
    private final Map<String, List<String>> events;

    public SaveCommand(FileHandler fileHandler, Map<String, List<String>> events) {
        this.fileHandler = fileHandler;
        this.events = events;
    }

    @Override
    public void execute(String[] args) {
        List<String> allEvents = new ArrayList<>();
        for (List<String> eventList : events.values()) {
            allEvents.addAll(eventList);
        }
        fileHandler.save(allEvents);
        System.out.println("Saved changes to the current calendar file");
    }
}

