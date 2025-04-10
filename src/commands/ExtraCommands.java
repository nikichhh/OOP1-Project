package commands;

import filehandler.FileHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ExtraCommands {
    private FileHandler fileHandler;
    private Map<String, List<String>> events;

    public ExtraCommands(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.events = new HashMap<>();
        loadEvents();
    }

    private void loadEvents() {
        List<String> allEvents = fileHandler.load();
        for (String event : allEvents) {
            String[] parts = event.split(" ", 6);
            String date = parts[1];
            if (!events.containsKey(date)) {
                events.put(date, new ArrayList<>());
            }
            events.get(date).add(event);
        }
    }

    private void saveEvents() {
        List<String> allEvents = new ArrayList<>();
        for (List<String> eventList : events.values()) {
            allEvents.addAll(eventList);
        }
        fileHandler.save(allEvents);
    }

    public void book(String date, String starttime, String endtime, String name, String note) {
        String event = String.format("book %s %s %s %s %s", date, starttime, endtime, name, note);
        if (!events.containsKey(date)) {
            events.put(date, new ArrayList<>());
        }
        events.get(date).add(event);
        fileHandler.save(event);
        System.out.println("Event booked: " + event);
    }

    public void unbook(String date, String starttime, String endtime) {
        if (events.containsKey(date)) {
            List<String> dateEvents = events.get(date);
            Iterator<String> iterator = dateEvents.iterator();
            while (iterator.hasNext()) {
                String event = iterator.next();
                if (event.startsWith(String.format("book %s %s %s", date, starttime, endtime))) {
                    iterator.remove();
                    fileHandler.save(dateEvents);
                    System.out.println("Event unbooked: " + date + " " + starttime + " " + endtime);
                    return;
                }
            }
        }
        System.out.println("No event found to unbook.");
    }

    public void agenda(String date) {
        if (events.containsKey(date)) {
            System.out.println("Agenda for " + date + ":");
            for (String event : events.get(date)) {
                System.out.println(event);
            }
        } else {
            System.out.println("No events found for " + date);
        }
    }

    public void change(String date, String starttime, String option, String newvalue) {
        if (events.containsKey(date)) {
            List<String> dateEvents = events.get(date);
            for (String event : dateEvents) {
                if (event.startsWith(String.format("book %s %s", date, starttime))) {
                    String[] parts = event.split(" ");
                    switch (option) {
                        case "date":
                            parts[1] = newvalue;
                            break;
                        case "starttime":
                            parts[2] = newvalue;
                            break;
                        case "endtime":
                            parts[3] = newvalue;
                            break;
                        case "name":
                            parts[4] = newvalue;
                            break;
                        case "note":
                            parts[5] = newvalue;
                            break;
                    }
                    String updatedEvent = String.join(" ", parts);
                    dateEvents.remove(event);
                    dateEvents.add(updatedEvent);
                    fileHandler.save(dateEvents);
                    System.out.println("Event changed: " + updatedEvent);
                    return;
                }
            }
        }
        System.out.println("No event found to change.");
    }

    public void find(String searchString) {
        boolean found = false;
        for (List<String> eventList : events.values()) {
            for (String event : eventList) {
                if (event.contains(searchString)) {
                    System.out.println(event);
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No events found containing '" + searchString + "'");
        }
    }

    public void holiday(String date) {
        String event = String.format("holiday %s", date);
        if (!events.containsKey(date)) {
            events.put(date, new ArrayList<>());
        }
        events.get(date).add(event);
        fileHandler.save(event);
        System.out.println("Holiday marked: " + date);
    }

    public void busydays(String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(from, formatter);
        LocalDate endDate = LocalDate.parse(to, formatter);

        Map<String, Integer> busyHours = new HashMap<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String formattedDate = date.format(formatter);
            if (events.containsKey(formattedDate)) {
                int totalHours = 0;
                for (String event : events.get(formattedDate)) {
                    if (event.startsWith("book")) {
                        String[] parts = event.split(" ");
                        LocalTime startTime = LocalTime.parse(parts[2], DateTimeFormatter.ofPattern("HH:mm"));
                        LocalTime endTime = LocalTime.parse(parts[3], DateTimeFormatter.ofPattern("HH:mm"));
                        totalHours += ChronoUnit.HOURS.between(startTime, endTime);
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

    public void findslot(String fromdate, int hours) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(fromdate, formatter);

        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            String formattedDate = date.format(formatter);
            if (!events.containsKey(formattedDate) || events.get(formattedDate).stream().noneMatch(event -> event.startsWith("holiday"))) {
                boolean isFree = true;
                if (events.containsKey(formattedDate)) {
                    for (String event : events.get(formattedDate)) {
                        if (event.startsWith("book")) {
                            String[] parts = event.split(" ");
                            LocalTime startTime = LocalTime.parse(parts[2], DateTimeFormatter.ofPattern("HH:mm"));
                            LocalTime endTime = LocalTime.parse(parts[3], DateTimeFormatter.ofPattern("HH:mm"));
                            if (startTime.isBefore(LocalTime.of(8, 0)) || endTime.isAfter(LocalTime.of(17, 0))) {
                                continue;
                            }
                            int duration = (int) ChronoUnit.HOURS.between(startTime, endTime);
                            if (duration >= hours) {
                                isFree = false;
                                break;
                            }
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

    public void findslotwith(String fromdate, int hours, String calendar) {
        FileHandler otherCalendar = new FileHandler(calendar);
        Map<String, List<String>> otherEvents = new HashMap<>();
        List<String> allOtherEvents = otherCalendar.load();
        for (String event : allOtherEvents) {
            String[] parts = event.split(" ", 6);
            String date = parts[1];
            if (!otherEvents.containsKey(date)) {
                otherEvents.put(date, new ArrayList<>());
            }
            otherEvents.get(date).add(event);
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
                        if (event.startsWith("book")) {
                            String[] parts = event.split(" ");
                            LocalTime startTime = LocalTime.parse(parts[2], DateTimeFormatter.ofPattern("HH:mm"));
                            LocalTime endTime = LocalTime.parse(parts[3], DateTimeFormatter.ofPattern("HH:mm"));
                            if (startTime.isBefore(LocalTime.of(8, 0)) || endTime.isAfter(LocalTime.of(17, 0))) {
                                continue;
                            }
                            int duration = (int) ChronoUnit.HOURS.between(startTime, endTime);
                            if (duration >= hours) {
                                isFree = false;
                                break;
                            }
                        }
                    }
                }
                if (otherEvents.containsKey(formattedDate)) {
                    for (String event : otherEvents.get(formattedDate)) {
                        if (event.startsWith("book")) {
                            String[] parts = event.split(" ");
                            LocalTime startTime = LocalTime.parse(parts[2], DateTimeFormatter.ofPattern("HH:mm"));
                            LocalTime endTime = LocalTime.parse(parts[3], DateTimeFormatter.ofPattern("HH:mm"));
                            if (startTime.isBefore(LocalTime.of(8, 0)) || endTime.isAfter(LocalTime.of(17, 0))) {
                                continue;
                            }
                            int duration = (int) ChronoUnit.HOURS.between(startTime, endTime);
                            if (duration >= hours) {
                                isFree = false;
                                break;
                            }
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

    public void merge(String calendar) {
        FileHandler otherCalendar = new FileHandler(calendar);
        Map<String, List<String>> otherEvents = new HashMap<>();
        List<String> allOtherEvents = otherCalendar.load();
        for (String event : allOtherEvents) {
            String[] parts = event.split(" ", 6);
            String date = parts[1];
            if (!otherEvents.containsKey(date)) {
                otherEvents.put(date, new ArrayList<>());
            }
            otherEvents.get(date).add(event);
        }

        Scanner scanner = new Scanner(System.in);
        for (Map.Entry<String, List<String>> entry : otherEvents.entrySet()) {
            String date = entry.getKey();
            List<String> dateEvents = entry.getValue();
            if (!events.containsKey(date)) {
                events.put(date, new ArrayList<>());
            }
            for (String event : dateEvents) {
                if (event.startsWith("book")) {
                    String[] parts = event.split(" ", 6);
                    String starttime = parts[2];
                    String endtime = parts[3];
                    boolean conflict = false;
                    for (String currentEvent : events.get(date)) {
                        if (currentEvent.startsWith(String.format("book %s %s %s", date, starttime, endtime))) {
                            conflict = true;
                            System.out.println("Conflict found for event: " + event);
                            System.out.println("Current event: " + currentEvent);
                            System.out.println("Please choose an option:");
                            System.out.println("1. Keep current event");
                            System.out.println("2. Replace with new event");
                            int choice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            if (choice == 2) {
                                events.get(date).remove(currentEvent);
                                events.get(date).add(event);
                            }
                        }
                    }
                    if (!conflict) {
                        events.get(date).add(event);
                    }
                }
            }
        }
        saveEvents();
        System.out.println("Events merged successfully.");
    }
}