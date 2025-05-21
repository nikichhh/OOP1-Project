package commands;

import filehandler.FileHandler;
import java.util.*;

public class AgendaCommand implements Command {
    private final FileHandler fileHandler;
    private final Map<String, List<String>> events;

    public AgendaCommand(FileHandler fileHandler, Map<String, List<String>> events) {
        this.fileHandler = fileHandler;
        this.events = events;
        loadEvents();
    }

    private void loadEvents() {
        List<String> allEvents = fileHandler.load();
        for (String event : allEvents) {
            String[] parts = event.split(" ", 6);
            String date = parts[0];
            events.computeIfAbsent(date, k -> new ArrayList<>()).add(event);
        }
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: agenda <date>");
            return;
        }
        String date = args[1];
        if (events.containsKey(date)) {
            System.out.println("Agenda for " + date + ":");
            for (String event : events.get(date)) {
                System.out.println(event);
            }
        } else {
            System.out.println("No events found for " + date);
        }
    }
}
