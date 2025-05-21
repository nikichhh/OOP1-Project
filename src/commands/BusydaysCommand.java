package commands;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BusydaysCommand implements Command {
    private final Map<String, List<String>> events;

    public BusydaysCommand(Map<String, List<String>> events) {
        this.events = events;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: busydays <from> <to>");
            return;
        }
        String from = args[1].split(" ", 2)[0];
        String to = args[1].split(" ", 2)[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(from, formatter);
        LocalDate endDate = LocalDate.parse(to, formatter);
        Map<String, Integer> busyHours = new HashMap<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String formattedDate = date.format(formatter);
            if (events.containsKey(formattedDate)) {
                int totalHours = 0;
                for (String event : events.get(formattedDate)) {
                    String[] parts = event.split(" ");
                    if (parts.length >= 3) {
                        LocalTime startTime = LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("HH:mm"));
                        LocalTime endTime = LocalTime.parse(parts[2], DateTimeFormatter.ofPattern("HH:mm"));
                        totalHours += (int) ChronoUnit.HOURS.between(startTime, endTime);
                    }
                }
                busyHours.put(formattedDate, totalHours);
            }
        }
        System.out.println("Busy days from " + from + " to " + to + ":");
        busyHours.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue() + " hours"));
    }
}
