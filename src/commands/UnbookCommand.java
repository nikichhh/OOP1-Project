package commands;

import filehandler.FileHandler;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UnbookCommand implements Command {
    private final FileHandler fileHandler;
    private final Map<String, List<String>> events;

    public UnbookCommand(FileHandler fileHandler, Map<String, List<String>> events) {
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
            System.out.println("Usage: unbook <date> <starttime> <endtime>");
            return;
        }
        String[] params = args[1].split(" ", 3);
        if (params.length < 3) {
            System.out.println("Usage: unbook <date> <starttime> <endtime>");
            return;
        }
        String date = params[0];
        String starttime = params[1];
        String endtime = params[2];
        if (!isValidDate(date) || !isValidTime(starttime) || !isValidTime(endtime)) {
            System.out.println("Invalid date or time format.");
            return;
        }
        if (events.containsKey(date)) {
            List<String> dateEvents = events.get(date);
            Iterator<String> iterator = dateEvents.iterator();
            boolean found = false;
            while (iterator.hasNext()) {
                String event = iterator.next();
                if (event.startsWith(String.format("%s %s %s", date, starttime, endtime))) {
                    iterator.remove();
                    // Save updated events for this date
                    fileHandler.save(dateEvents);
                    System.out.println("Event unbooked: " + date + " " + starttime + " " + endtime);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("No event found to unbook.");
            }
        } else {
            System.out.println("No event found to unbook.");
        }
    }
}
