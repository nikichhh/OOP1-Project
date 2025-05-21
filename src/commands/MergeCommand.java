package commands;

import filehandler.FileHandler;
import java.util.*;

public class MergeCommand implements Command {
    private final FileHandler fileHandler;
    private final Map<String, List<String>> events;

    public MergeCommand(FileHandler fileHandler, Map<String, List<String>> events) {
        this.fileHandler = fileHandler;
        this.events = events;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: merge <calendar>");
            return;
        }
        String calendar = args[1];
        FileHandler otherCalendar = new FileHandler(calendar);
        Map<String, List<String>> otherEvents = new HashMap<>();
        List<String> allOtherEvents = otherCalendar.load();
        if (allOtherEvents.isEmpty()) {
            System.out.println("File is empty or not found: " + calendar);
            return;
        }
        for (String event : allOtherEvents) {
            String[] parts = event.split(" ", 6);
            String date = parts[0];
            otherEvents.computeIfAbsent(date, _ -> new ArrayList<>()).add(event);
        }
        try (Scanner scanner = new Scanner(System.in)) {
            for (Map.Entry<String, List<String>> entry : otherEvents.entrySet()) {
                String date = entry.getKey();
                List<String> dateEvents = entry.getValue();
                events.computeIfAbsent(date, _ -> new ArrayList<>());
                for (String event : dateEvents) {
                    if (event.startsWith("book")) {
                        String[] parts = event.split(" ", 6);
                        String starttime = parts[1];
                        String endtime = parts[2];
                        boolean conflict = false;
                        for (String currentEvent : events.get(date)) {
                            if (currentEvent.startsWith(String.format("%s %s %s", date, starttime, endtime))) {
                                conflict = true;
                                System.out.println("Conflict found for event: " + event);
                                System.out.println("Current event: " + currentEvent);
                                System.out.print("Do you want to overwrite? (yes/no): ");
                                String answer = scanner.nextLine();
                                if (answer.equalsIgnoreCase("yes")) {
                                    events.get(date).remove(currentEvent);
                                    events.get(date).add(event);
                                    System.out.println("Event overwritten.");
                                } else {
                                    System.out.println("Event skipped.");
                                }
                                break;
                            }
                        }
                        if (!conflict) {
                            events.get(date).add(event);
                            System.out.println("Event merged: " + event);
                        }
                    } else {
                        events.get(date).add(event);
                        System.out.println("Event merged: " + event);
                    }
                }
            }
        }
        // Save merged events
        List<String> allEvents = new ArrayList<>();
        for (List<String> eventList : events.values()) {
            allEvents.addAll(eventList);
        }
        fileHandler.save(allEvents);
        System.out.println("Merge complete.");
    }
}
