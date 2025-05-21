package commands;

import filehandler.FileHandler;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FindslotCommand implements Command {
    private final FileHandler fileHandler;
    private final Map<String, List<String>> events;

    public FindslotCommand(FileHandler fileHandler, Map<String, List<String>> events) {
        this.fileHandler = fileHandler;
        this.events = events;
        loadEvents();
    }

    private void loadEvents() {
        List<String> allEvents = fileHandler.load();
        for (String event : allEvents) {
            String[] parts = event.split(" ", 6);
            String date = parts[0];
            events.computeIfAbsent(date, _ -> new ArrayList<>()).add(event);
        }
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: findslot <fromdate> <hours>");
            return;
        }
        String[] params = args[1].split(" ", 2);
        if (params.length < 2) {
            System.out.println("Usage: findslot <fromdate> <hours>");
            return;
        }
        String fromdate = params[0];
        int hours;
        try {
            hours = Integer.parseInt(params[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid hours format.");
            return;
        }
        if (!isValidDate(fromdate)) {
            System.out.println("Invalid date format.");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(fromdate, formatter);
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            String formattedDate = date.format(formatter);
            if (!events.containsKey(formattedDate) || events.get(formattedDate).stream().noneMatch(event -> event.startsWith("holiday"))) {
                boolean isFree = true;
                if (events.containsKey(formattedDate)) {
                    for (String event : events.get(formattedDate)) {
                        String[] parts = event.split(" ");
                        LocalTime startTime = LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("HH:mm"));
                        LocalTime endTime = LocalTime.parse(parts[2], DateTimeFormatter.ofPattern("HH:mm"));
                        if (startTime.isBefore(LocalTime.of(8, 0)) || endTime.isAfter(LocalTime.of(17, 0))) {
                            continue;
                        }
                        int duration = (int) java.time.temporal.ChronoUnit.HOURS.between(startTime, endTime);
                        if (duration >= hours) {
                            isFree = false;
                            break;
                        }
                    }
                }
                if (isFree) {
                    System.out.println("Free slot found on " + formattedDate);
                    return;
                }
            }
        }
        System.out.println("No free slot found.");
    }
}
