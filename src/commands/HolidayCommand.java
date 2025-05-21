package commands;

import filehandler.FileHandler;
import java.util.*;

public class HolidayCommand implements Command {
    private final FileHandler fileHandler;
    private final Map<String, List<String>> events;

    public HolidayCommand(FileHandler fileHandler, Map<String, List<String>> events) {
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

    private boolean isValidDate(String date) {
        try {
            java.time.LocalDate.parse(date, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: holiday <date>");
            return;
        }
        String date = args[1];
        if (!isValidDate(date)) {
            System.out.println("Invalid date format.");
            return;
        }
        String event = String.format("holiday %s", date);
        events.computeIfAbsent(date, _ -> new ArrayList<>()).add(event);
        fileHandler.save(event);
        System.out.println("Holiday marked: " + date);
    }
}
