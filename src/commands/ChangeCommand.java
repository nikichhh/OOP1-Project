package commands;

import filehandler.FileHandler;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChangeCommand implements Command {
    private final FileHandler fileHandler;
    private final Map<String, List<String>> events;

    public ChangeCommand(FileHandler fileHandler, Map<String, List<String>> events) {
        this.fileHandler = fileHandler;
        this.events = events;
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidTime(String time) {
        try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: change <date> <starttime> <option> <newvalue>");
            return;
        }
        String[] params = args[1].split(" ", 4);
        if (params.length < 4) {
            System.out.println("Usage: change <date> <starttime> <option> <newvalue>");
            return;
        }
        String date = params[0];
        String starttime = params[1];
        String option = params[2];
        String newvalue = params[3];
        if (!isValidDate(date) || !isValidTime(starttime)) {
            System.out.println("Invalid date or time format.");
            return;
        }
        if (events.containsKey(date)) {
            List<String> dateEvents = events.get(date);
            for (int i = 0; i < dateEvents.size(); i++) {
                String event = dateEvents.get(i);
                if (event.startsWith(String.format("%s %s", date, starttime))) {
                    String[] parts = event.split(" ");
                    switch (option) {
                        case "date":
                            parts[0] = newvalue;
                            break;
                        case "starttime":
                            parts[1] = newvalue;
                            break;
                        case "endtime":
                            parts[2] = newvalue;
                            break;
                        case "name":
                            parts[3] = newvalue;
                            break;
                        case "note":
                            parts[4] = newvalue;
                            break;
                        default:
                            System.out.println("Unknown option: " + option);
                            return;
                    }
                    String updatedEvent = String.join(" ", parts);
                    dateEvents.set(i, updatedEvent);
                    fileHandler.save(dateEvents);
                    System.out.println("Event changed: " + updatedEvent);
                    return;
                }
            }
        }
        System.out.println("No event found to change.");
    }
}
